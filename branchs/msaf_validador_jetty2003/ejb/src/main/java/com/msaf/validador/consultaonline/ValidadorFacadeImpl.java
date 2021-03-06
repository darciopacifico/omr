package com.msaf.validador.consultaonline;

import java.io.StringReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.msaf.framework.utils.DllDadosDTO;
import com.msaf.validador.consultaonline.dao.DadosRetornoInstituicaoDAO;
import com.msaf.validador.consultaonline.dao.PedidoValidacaoDAO;
import com.msaf.validador.consultaonline.dao.RegistroPessoaDAO;
import com.msaf.validador.consultaonline.dao.TipoResultadoDAO;
import com.msaf.validador.consultaonline.dao.TipoValidacaoDAO;
import com.msaf.validador.consultaonline.exceptions.BaseValidadorException;
import com.msaf.validador.consultaonline.exceptions.PersistenciaException;
import com.msaf.validador.consultaonline.exceptions.ValidadorConsumerException;
import com.msaf.validador.consultaonline.exceptions.ValidadorException;
import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;
import com.msaf.validador.consultaonline.jna.IConsultaOnLineJNA;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;
import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Implementa��o do servi�o do Validador (WebService)
 * 
 * @author dlopes
 * 
 */
@WebService
public class ValidadorFacadeImpl implements ValidadorFacade {

	// injetado pelo spring.

  private TiposServicoXMLFactory tipoServicoXML;
  private IConsultaOnLineJNA consultaOnLine;

	Logger log = Logger.getLogger(ValidadorFacadeImpl.class);


	private DadosRetornoInstituicaoDAO dadosRetornoInstituicaoDAO;
	private RegistroPessoaDAO registroPessoaDAO;
	private TipoResultadoDAO tipoResultadoDAO;
	private PedidoValidacaoDAO pedidoValidacaoDAO;
	private TipoValidacaoDAO tipoValidacaoDAO;
	

	private Unmarshaller unmarshaller;
	private JAXBContext jaxbContext;
	
	public ValidadorFacadeImpl() {
		log.debug("Instanciando servico!");
		tipoServicoXML = new TiposServicoXMLFactory();
		Native.setProtected(true);



		Library library = (Library) Native.loadLibrary("ConsultaOnLine", IConsultaOnLineJNA.class);
		library = Native.synchronizedLibrary(library);


		consultaOnLine = (IConsultaOnLineJNA)library;
	}

	

	@WebMethod(exclude=true)
	public void consultaDadosCliente(ParametrosConsultaOnLineDTO parameterObject, DllDadosDTO configuracaoDLL, List<ResulConsVO> resultadosConsultas) throws ValidadorConsumerException {

		PessoaVO pessoaVO = parameterObject.getRegistroPessoaVO();
		formataCnpj(pessoaVO);

		List<TpValidVO> listTpValidacao = parameterObject.getTipoValidacaoVO();

		// para cada tipo de valicao seria uma consulta diferente.
		for (TpValidVO tpValidVO : listTpValidacao) {

			if (tpValidVO.getId().intValue() == TiposServicoXMLFactory.COMPLETA) {
				// realiza a consulta completa
				processaConsultaCompleta(configuracaoDLL, 
						resultadosConsultas,pessoaVO);
				
			} else {
				
				// processa a consulta aos servicos inidividualmente.
				processaConsultaIndividual(configuracaoDLL,
					resultadosConsultas, pessoaVO, tpValidVO);
			}

		}
	}

	/**
	 * @param configuracaoDLL
	 * @param resultadosConsultas
	 * @param pessoaVO
	 * @param tpValidVO
	 * @throws PersistenciaException
	 */
	@WebMethod(exclude=true)
	private void processaConsultaCompleta(DllDadosDTO configuracaoDLL,
			List<ResulConsVO> resultadosConsultas, PessoaVO pessoaVO) {
		// inicialmente realiza a consulta para receita federal.
		if(pessoaVO.getCnpj() != null) {
			
			try {
			//seta o tipo de validacao para a RF.
			TpValidVO tpValidacaoRF = new TpValidVO();
			tpValidacaoRF = tipoValidacaoDAO.buscarPorId(new Long(TiposServicoXMLFactory.RECEITA_FEDERAL));
			
			String consultaXMLReceitaFederal = tipoServicoXML.selecionaXMLTipoValicao(pessoaVO,tpValidacaoRF,TiposServicoXMLFactory.TIPO_DOCUMENTO_RECEITA_CNPJ,configuracaoDLL);
			processaConsulta(configuracaoDLL, resultadosConsultas, tpValidacaoRF, consultaXMLReceitaFederal, TiposServicoXMLFactory.TIPO_DOCUMENTO_RECEITA_CNPJ);
			
			populaPessoaVOComDadosRetornoRF(resultadosConsultas, pessoaVO);		
			
				List<TpValidVO>  todasValidacoes = tipoValidacaoDAO.buscarTodos();
				
				//realiza a consulta para todos os servicos com excecao do id rf e completa. 
				for (TpValidVO tipoValidacao : todasValidacoes) {
					if((tipoValidacao.getId() != TiposServicoXMLFactory.RECEITA_FEDERAL) &&
					   (tipoValidacao.getId() != TiposServicoXMLFactory.COMPLETA)){
						processaConsultaIndividual(configuracaoDLL, resultadosConsultas, pessoaVO, tipoValidacao);
					}
						
				}// end for.
				
				
			} catch (PersistenciaException e) {
				throw new ValidadorRuntimeException("Erro ao tentar recuperar a lista de validacoes",e);
			}
		
		}
	}

	
	
	/**
	 * Metodo respons�vel por popular o pessoa VO 
	 * com os dados recuperados da consulta a RF.
	 * @param resultadosConsultas
	 * @param pessoaVO pessoaVO.
	 * @return pessoaVO pessoaVO populado com os dados da RF.
	 */
	@WebMethod(exclude=true)
	private void populaPessoaVOComDadosRetornoRF(
			List<ResulConsVO> resultadosConsultas, PessoaVO pessoaVO) {
		//recupera o resultado da consulta a receita federal e popula o pessoaVO com as informacoes necess�rias.
		for (ResulConsVO resultadoConsulta : resultadosConsultas) {
			Set<DadosRetInstVO> dadosRetInstList = resultadoConsulta.getRegistrosRetorno();

			for (DadosRetInstVO dadosRetornoInstituicaoVO : dadosRetInstList) {

				pessoaVO.setEstado(dadosRetornoInstituicaoVO.getEstado());

				// validacao para nao sobrescrever as informacoes originais
				// fornecidas pelo usu�rio em caso
				if (dadosRetornoInstituicaoVO.getCnpj() != null)
					pessoaVO.setCnpj(limparCNPJ_IE_CPF(dadosRetornoInstituicaoVO.getCnpj()));

				if (dadosRetornoInstituicaoVO.getIe() != null)
					pessoaVO.setIe(limparCNPJ_IE_CPF(dadosRetornoInstituicaoVO.getIe()));

				if (dadosRetornoInstituicaoVO.getCpf() != null)
					pessoaVO.setCpf(limparCNPJ_IE_CPF(dadosRetornoInstituicaoVO.getCpf()));

				
			}
		}
	}

	
	/**
	 * Limpa retira o ./ e - do cnpj, ie e cpf.
	 * @param valor
	 * @return
	 */
	@WebMethod(exclude=true)
	private String limparCNPJ_IE_CPF(String valor) {
		if((valor != null) &&(valor != "")) {
			valor = valor.replace(".", "");
			valor = valor.replace("/", "");
			valor = valor.replace("-", "");
		}	
		return valor;
    }
	
	/**
	 * Metodo responsavel por realizar a consulta para um servico por vez.
	 * @param configuracaoDLL
	 * @param resultadosConsultas
	 * @param pessoaVO
	 * @param tpValidVO
	 */
	@WebMethod(exclude=true)
	private void processaConsultaIndividual(DllDadosDTO configuracaoDLL,
			List<ResulConsVO> resultadosConsultas, PessoaVO pessoaVO,
			TpValidVO tpValidVO) {
		// se tipo de validacao eh igual sintegra
		// fazer consulta por cnpj e ie
		if (tpValidVO.getId().intValue() == TiposServicoXMLFactory.SINTEGRA) {
			
			if(pessoaVO.getCnpj() != null){
				String consultaXMLSintegraCnpj = tipoServicoXML.selecionaXMLTipoValicao(pessoaVO,tpValidVO,TiposServicoXMLFactory.TIPO_DOCUMENTO_SINTEGRA_CNPJ,configuracaoDLL);
				processaConsulta(configuracaoDLL, resultadosConsultas, tpValidVO, consultaXMLSintegraCnpj, TiposServicoXMLFactory.TIPO_DOCUMENTO_SINTEGRA_CNPJ);
			}

			if(pessoaVO.getIe() != null){
				String consultaXMLSintegraIe = tipoServicoXML.selecionaXMLTipoValicao(pessoaVO,tpValidVO,TiposServicoXMLFactory.TIPO_DOCUMENTO_SINTEGRA_IE,configuracaoDLL);
				processaConsulta(configuracaoDLL, resultadosConsultas, tpValidVO, consultaXMLSintegraIe, TiposServicoXMLFactory.TIPO_DOCUMENTO_SINTEGRA_IE);
			}

		} else {
			// realiza a consulta normalmente.
			String consultaXML = tipoServicoXML.selecionaXMLTipoValicao(pessoaVO,tpValidVO, configuracaoDLL);
			processaConsulta(configuracaoDLL, resultadosConsultas, tpValidVO, consultaXML, "");
		}
	}

	/**
	 * @param pessoaVO
	 */
	@WebMethod(exclude=true)
	protected void formataCnpj(PessoaVO pessoaVO) {
		String cnpj = pessoaVO.getCnpj();
		try{
			cnpj = String.format("%014d", Long.parseLong(cnpj) ); 
			
		}catch (Throwable e) {
		}

		pessoaVO.setCnpj(cnpj);
	}

	/**
	 * @param configuracaoDLL
	 * @param resultadoConsultaVOList
	 * @param tpValidVO
	 * @param tipoValidacao
	 * @param consultaXML
	 * @param tipoDocumento 
	 */
	@WebMethod(exclude=true)
	private void processaConsulta(DllDadosDTO configuracaoDLL, List<ResulConsVO> resultadoConsultaVOList, TpValidVO tpValidVO, String consultaXML, String tipoDocumento) {

		//chama a DLL do EV
		ResulConsVO resultadoConsultaVO = executaConsultaDLL(tpValidVO, consultaXML, configuracaoDLL );

		//analisa e critica os retornos da DLL
		avaliaRetornos(tpValidVO, resultadoConsultaVO, tipoDocumento );

		// armazena na lista de resultados geral, contemplando todos os tipos de
		// validacao selecionados pelo usu�rio
		resultadoConsultaVOList.add(resultadoConsultaVO);
	}

	
	
	
	
	
	
	/**
	 * @param tpValidVO
	 * @param consultaXML
	 * @return
	 */
	@WebMethod
	public ResulConsVO consultaValidador(TpValidVO tpValidVO, PessoaVO pessoaVO) {
		return null;
	}	
	
	
	
	
	
	/**
	 * @param tpValidVO
	 * @param consultaXML
	 * @return
	 */
	@WebMethod(exclude=true)
	protected ResulConsVO executaConsultaDLL(TpValidVO tpValidVO, String consultaXML, DllDadosDTO configuracaoDLL) {

		Integer tpValidacao = tpValidVO.getId().intValue();
		
		String resultado = consultaOnLine.DBI_EfetuaConsultaServico(tpValidacao, consultaXML, configuracaoDLL.getDllPath(), configuracaoDLL.getEvServer());
		
		ResulConsVO resultadoConsultaVO = unmarshallRetornoDll(resultado);
		return resultadoConsultaVO;
	}



	/**
	 * @param resultado
	 * @return
	 * @throws JAXBException
	 * @throws ValidadorException
	 *
	 * @FIXME Tratar retorno no XML de erros previsto na DLL do validador: vide manual
	 */
	//protected ResultadoConsultaVO unmarshallRetornoDll(String resultado){
	protected ResulConsVO unmarshallRetornoDll(String resultado){
		Unmarshaller unmarshaller = getUnmarshaller();
		//	FIXME: MONTAR UMA ESTRAT'EGIA DE TRATAMENTO DE CARACT. ESPECIAIS.
		resultado = resultado.replaceAll("&", "e");

		Source source  = new StreamSource(new StringReader(resultado));

		ResulConsVO resultadoConsultaVO;
		try {
			// unmarshaller da requisicao.
			resultadoConsultaVO = (ResulConsVO) unmarshaller.unmarshal(source);

		} catch (Exception e) {
			resultadoConsultaVO = new ResulConsVO();
			resultadoConsultaVO.setCodErro("10001");
			resultadoConsultaVO.setErro("Servi�o de consulta ao �rg�o indispon�vel!");

			//throw new ValidadorRuntimeException("Erro ao tentar executar o unmarshaller sobre o XML de retorno!",e);
		}
		return resultadoConsultaVO;
	}




	private void imprimeRetorno(ResulConsVO resultadoConsultaVO){
	
		log.debug("imprimeRetorno versao do documento:"+resultadoConsultaVO.getVersao());
		
		Collection<DadosRetInstVO> dadosRetornoList = 
			resultadoConsultaVO.getRegistrosRetorno();
		
		for (Iterator iterator = dadosRetornoList.iterator(); iterator.hasNext();) {
			DadosRetInstVO dadosRetornoInstituicaoVO = (DadosRetInstVO) iterator.next();
			
			log.debug(dadosRetornoInstituicaoVO.getSituacao());			
		}
	}
	
	
	
	
	/**
	 * @return
	 * @throws JAXBException
	 * @throws ValidadorException
	 */
	protected Unmarshaller getUnmarshaller(){
		if(this.unmarshaller==null){
			
			try {
				this.unmarshaller = getJAXBContext().createUnmarshaller();
			} catch (JAXBException e) {
				throw new ValidadorRuntimeException("Erro ao tentar recuperar um ummarshaller para o XML de retorno!",e);
			}
		}
		
		return this.unmarshaller;
	}

	
	
	/**
	 * Respons�vel por armazenar os resultados da validacao na base de dados.
	 * @param resultadoConsultaVO vo com as informacaoes consultadas na dll.
	 * @author Ekler Paulino de Mattos
	 */

	/**
	 * IMplementar a gravacao dos resultados da consulta (parametro de entrada + resultado vindo da DLL)
	 * 
	 */
	@Transactional
	@WebMethod(exclude=true)
	public void gravaResultado(ParametrosConsultaOnLineDTO consultaOnLineDTO, List<ResulConsVO> resultadoList) {

		PessoaVO pessoaVO = consultaOnLineDTO.getRegistroPessoaVO();
		
		// apenas para garantir o pedido de validacao
		//pessoaVO.setPedValidFk(pesquisaPedValidacaoVO(consultaOnLineDTO));
		pessoaVO.setPedValidFk(consultaOnLineDTO.getPedidoValidacaoVO());
		for (ResulConsVO resultado : resultadoList) {
			Set<DadosRetInstVO> dadosRetornoList = resultado.getRegistrosRetorno();

			// atribui todos os retornos encontrados ao usu�rio
			
			pessoaVO.getDadosRetornoFk().addAll(dadosRetornoList);
		}

		try {
			this.incluiRegistroPessoa(pessoaVO); // -- SIMPLES: APENAS INVOCA O EntityManager
		} catch (PersistenciaException e) {
			log.error("Erro ao tentar incluir dados retorno instituicao!", e);
		}
	}

	

	/**
	 * @param consultaOnLineDTO
	 * @return
	 * @throws PersistenciaException 
	 */
	protected PedValidacaoVO pesquisaPedValidacaoVO(ParametrosConsultaOnLineDTO consultaOnLineDTO)  {
		PedValidacaoVO pedidoValidacao = consultaOnLineDTO.getPedidoValidacaoVO();
			try {
				pedidoValidacao = pedidoValidacaoDAO.buscarPorId(pedidoValidacao.getId());
			} catch (PersistenciaException e) {
				throw new ValidadorRuntimeException("Pedido de validacao nao encontrado! Abortar!",e);
			}
		return pedidoValidacao;
	}
	
	
	/**
	 * Respons�vel por inserir o tipo de validacao das consultas
	 * no resultado de consulta.
	 * @param tipoValidacaoVO tipo de validacao realizada.
	 * @return dadosRetornoList lista de resultados.
	 * @author Ekler Paulino de Mattos
	 * @param tipoDocumento 
	 */
	private void avaliaRetornos(TpValidVO tipoValidacaoVO, ResulConsVO resultado, String tipoDocumento) {

		Set<DadosRetInstVO> dadosRetornoList = resultado.getRegistrosRetorno();

		if (!dadosRetornoList.isEmpty()) {
			//se houve resultado identifica o tipo de resultado e atribui ao retorno
			for (DadosRetInstVO dadosRetInstVO : dadosRetornoList) {

				TpResultVO tpResultVO = identificaTipoResultado(resultado);
				
				dadosRetInstVO.setTipoValidacaoFk(tipoValidacaoVO);
				dadosRetInstVO.setTipoResultadoFk(tpResultVO);
				dadosRetInstVO.setTipoDocumento(tipoDocumento);
			}
		} else {
			//cria um �nico dados de retorno vazio com retorno referente � mensagem de erro retornada
			dadosRetornoList = criaDadoRetInstVazio(tipoValidacaoVO, resultado, tipoDocumento);
		
		}
		resultado.setRegistrosRetorno(dadosRetornoList);
		
	}

	/**
	 * @param tipoValidacaoVO
	 * @param resultado
	 * @param tipoDocumento 
	 * @return
	 */
	protected Set<DadosRetInstVO> criaDadoRetInstVazio(TpValidVO tipoValidacaoVO, ResulConsVO resultado, String tipoDocumento) {

		//retorno padr�o vazio
		DadosRetInstVO dadoRetVazio = new DadosRetInstVO();
		dadoRetVazio.setTipoValidacaoFk(tipoValidacaoVO);
		dadoRetVazio.setTipoResultadoFk(identificaTipoResultado(resultado));
		dadoRetVazio.setTipoDocumento(tipoDocumento);
		
		//lista contendo apenas o retorno vazio
		Set<DadosRetInstVO> dadosRetornoList = new HashSet<DadosRetInstVO>();
		dadosRetornoList.add(dadoRetVazio);
		
		return dadosRetornoList;
	}

	

	/**
	 * Insere o tipo de resultao gerado.
	 * @param dadosRetInstVO
	 * @return
	 */
	@Transactional
	private TpResultVO identificaTipoResultado(ResulConsVO resultado) {


		TpResultVO tipoResultado;
		try {
			Long idTipoResultado = identificaTpResult(resultado);

			// busca na base de dados o resultado.
			tipoResultado = getTipoResultadoDAO().buscarPorId(idTipoResultado);

			// insere o tipo de resultado.
			if (tipoResultado == null) {
				tipoResultado = new TpResultVO();
				tipoResultado.setId(idTipoResultado);
				// insercao da descricao do resultado seja de erro ou nao.
				if (idTipoResultado == TpResultVO.PROCESSADO_SUCESSO)
					tipoResultado.setDescricao("Processado com sucesso.");
				else
					tipoResultado.setDescricao(resultado.getErro());
				
				getTipoResultadoDAO().criarTipoResultadoVO(tipoResultado);
			}
		} catch (PersistenciaException e) {
			throw new ValidadorRuntimeException("Erro ao tentar inserir tipo de resultado",e);
		}
		try {
			tipoResultado = getTipoResultadoDAO().buscarPorId(tipoResultado.getId());
		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tipoResultado;

	}


	/**
	 * @param resultado
	 * @return
	 */
	protected Long identificaTpResult(ResulConsVO resultado) {
		Long idTipoResultado = null;
		// se existir o codigo de erro.
		if (!resultado.getCodErro().equals(("")))
			idTipoResultado = new Long(resultado.getCodErro());
		else
			idTipoResultado = TpResultVO.PROCESSADO_SUCESSO;
		return idTipoResultado;
	}
	
	
	
	@Transactional
	private void incluiRegistroPessoa(PessoaVO registroPessoaVO)throws PersistenciaException {
		
		getRegistroPessoaDAO().criarRegistroPessoa(registroPessoaVO);
		//registroPessoaDAO.
	}


	/**
	 * @return
	 * @throws JAXBException 
	 * @throws ValidadorException
	 * @throws JAXBException
	 */
	protected Marshaller getMarshaller() throws JAXBException{
		
		JAXBContext jc = getJAXBContext();

		Marshaller marshaller = jc.createMarshaller();
		return marshaller;
	}

	/**
	 * @return
	 * @throws ValidadorException
	 */
	protected JAXBContext getJAXBContext(){

		if(this.jaxbContext==null){
			try {
				this.jaxbContext = JAXBContext.newInstance("com.msaf.validador.consultaonline.solicitacaovalidacao");
			} catch (JAXBException e) {
				throw new ValidadorRuntimeException("Erro ao tentar recuperar o JAXBContext dos VOs do Validador.",e);
			}
		}
		return this.jaxbContext;
	}

	

	// FIXME Revisar
	@Override
	@WebMethod(exclude=true)
	public void senderDadosCliente(ParametrosConsultaOnLineDTO parameterObject)
			throws BaseValidadorException, ValidadorConsumerException {
	}


	/**
	 * @param dadosRetornoInstituicaoDAO the dadosRetornoInstituicaoDAO to set
	 */
	@WebMethod(exclude=true)
	public void setDadosRetornoInstituicaoDAO(DadosRetornoInstituicaoDAO dadosRetornoInstituicaoDAO) {
		this.dadosRetornoInstituicaoDAO = dadosRetornoInstituicaoDAO;
	}


	/**
	 * @return the dadosRetornoInstituicaoDAO
	 */
	@WebMethod(exclude=true)
	public DadosRetornoInstituicaoDAO getDadosRetornoInstituicaoDAO() {
		return dadosRetornoInstituicaoDAO;
	}


	/**
	 * @param registroPessoaDAO the registroPessoaDAO to set
	 */
	public void setRegistroPessoaDAO(RegistroPessoaDAO registroPessoaDAO) {
		this.registroPessoaDAO = registroPessoaDAO;
	}


	/**
	 * @return the registroPessoaDAO
	 */
	public RegistroPessoaDAO getRegistroPessoaDAO() {
		return registroPessoaDAO;
	}


	/**
	 * @param tipoResultadoDAO the tipoResultadoDAO to set
	 */
	public void setTipoResultadoDAO(TipoResultadoDAO tipoResultadoDAO) {
		this.tipoResultadoDAO = tipoResultadoDAO;
	}


	/**
	 * @return the tipoResultadoDAO
	 */
	public TipoResultadoDAO getTipoResultadoDAO() {
		return tipoResultadoDAO;
	}


	/**
	 * @param pedidoValidacaoDAO the pedidoValidacaoDAO to set
	 */
	public void setPedidoValidacaoDAO(PedidoValidacaoDAO pedidoValidacaoDAO) {
		this.pedidoValidacaoDAO = pedidoValidacaoDAO;
	}


	/**
	 * @return the pedidoValidacaoDAO
	 */
	public PedidoValidacaoDAO getPedidoValidacaoDAO() {
		return pedidoValidacaoDAO;
	}

	
	public TipoValidacaoDAO getTipoValidacaoDAO() {
		return tipoValidacaoDAO;
	}

	
	public void setTipoValidacaoDAO(TipoValidacaoDAO tipoValidacaoDAO) {
		this.tipoValidacaoDAO = tipoValidacaoDAO;
	}

	
	
	
}
