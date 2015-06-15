package com.msaf.validador.consultaonline;

import java.io.StringReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
public class ValidadorFacadeFakeImpl implements ValidadorFacade {

	// injetado pelo spring.
  private TiposServicoXMLFactory tipoServicoXML;
  private IConsultaOnLineJNA consultaOnLine;

	Logger log = Logger.getLogger(ValidadorFacadeFakeImpl.class);


	private DadosRetornoInstituicaoDAO dadosRetornoInstituicaoDAO;
	private RegistroPessoaDAO registroPessoaDAO;
	private TipoResultadoDAO tipoResultadoDAO;
	private PedidoValidacaoDAO pedidoValidacaoDAO;
	private TipoValidacaoDAO tipoValidacaoDAO;
	

	private Unmarshaller unmarshaller;
	private JAXBContext jaxbContext;
	
	public ValidadorFacadeFakeImpl() {
		if(log.isDebugEnabled()) log.debug("Instanciando servico!");
		tipoServicoXML = new TiposServicoXMLFactory();
		Native.setProtected(true);



		Library library = (Library) Native.loadLibrary("ConsultaOnLine", IConsultaOnLineJNA.class);
		library = Native.synchronizedLibrary(library);


		consultaOnLine = (IConsultaOnLineJNA)library;
	}

	/**
	 * Implementa��o da consulta de dados de cliente
	 * @throws ValidadorConsumerException 
	 */
	public void consultaDadosCliente(ParametrosConsultaOnLineDTO parameterObject, DllDadosDTO configuracaoDLL, List<ResulConsVO> resultadosConsultas) throws ValidadorConsumerException {

		PessoaVO pessoaVO = parameterObject.getRegistroPessoaVO();
		formataCnpj(pessoaVO);

		List<TpValidVO> listTpValidacao = parameterObject.getTipoValidacaoVO();

		// para cada tipo de valicao seria uma consulta diferente.
		for (TpValidVO tpValidVO : listTpValidacao) {

			if (tpValidVO.getId().intValue() == TipoValidacao.COMPLETA.getCodigo()) {
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
	private void processaConsultaCompleta(DllDadosDTO configuracaoDLL,
			List<ResulConsVO> resultadosConsultas, PessoaVO pessoaVO) {
		// inicialmente realiza a consulta para receita federal.
		if(pessoaVO.getCnpj() != null) {
			
			try {
			//seta o tipo de validacao para a RF.
			TpValidVO tpValidacaoRF = new TpValidVO();
			tpValidacaoRF = tipoValidacaoDAO.buscarPorId(new Long(TipoValidacao.RECEITA_FEDERAL.getCodigo()));
			
			String consultaXMLReceitaFederal = tipoServicoXML.selecionaXMLTipoValicao(pessoaVO,tpValidacaoRF,TipoDocumento.RECEITA_CNPJ.getCodigo(),configuracaoDLL);
			processaConsulta(configuracaoDLL, resultadosConsultas, tpValidacaoRF, consultaXMLReceitaFederal);
			
			populaPessoaVOComDadosRetornoRF(resultadosConsultas, pessoaVO);		
			
				List<TpValidVO>  todasValidacoes = tipoValidacaoDAO.buscarTodos();
				
				//realiza a consulta para todos os servicos com excecao do id rf e completa. 
				for (TpValidVO tipoValidacao : todasValidacoes) {
					if((tipoValidacao.getId() != TipoValidacao.RECEITA_FEDERAL.getCodigo()) &&
					   (tipoValidacao.getId() != TipoValidacao.COMPLETA.getCodigo())){
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
	private void populaPessoaVOComDadosRetornoRF(
			List<ResulConsVO> resultadosConsultas, PessoaVO pessoaVO) {
		//recupera o resultado da consulta a receita federal e popula o pessoaVO com as informacoes necess�rias.
		for (ResulConsVO resultadoConsulta : resultadosConsultas) {
		 Set<DadosRetInstVO>dadosRetInstList =	resultadoConsulta.getRegistrosRetorno();
		
		 for (DadosRetInstVO dadosRetornoInstituicaoVO : dadosRetInstList) {
			
		pessoaVO.setRazaoSocial( 	dadosRetornoInstituicaoVO.getRazaoSocial());
		pessoaVO.setLogradouro(		dadosRetornoInstituicaoVO.getLogradouro());
		pessoaVO.setNumero(			dadosRetornoInstituicaoVO.getNumero());
		pessoaVO.setComplemento(	dadosRetornoInstituicaoVO.getComplemento()); 
		pessoaVO.setBairro(			dadosRetornoInstituicaoVO.getBairro());      
		pessoaVO.setCep(	   		dadosRetornoInstituicaoVO.getCep());         
		pessoaVO.setCidade(	   		dadosRetornoInstituicaoVO.getCidade());      
		pessoaVO.setEstado(	   		dadosRetornoInstituicaoVO.getEstado());
		
		// validacao para nao sobrescrever as informacoes originais
		//  fornecidas pelo usu�rio em caso
		if(dadosRetornoInstituicaoVO.getCnpj()!= null)
		pessoaVO.setCnpj(	   		limparCNPJ_IE_CPF(dadosRetornoInstituicaoVO.getCnpj()));
		
		if(dadosRetornoInstituicaoVO.getIe()!= null)
		pessoaVO.setIe(	   			limparCNPJ_IE_CPF(dadosRetornoInstituicaoVO.getIe()));
		
		if(dadosRetornoInstituicaoVO.getCpf()!= null)
		pessoaVO.setCpf(	   		limparCNPJ_IE_CPF(dadosRetornoInstituicaoVO.getCpf()));        
		
		pessoaVO.setDataConsulta(	dadosRetornoInstituicaoVO.getDataConsulta());
		
		pessoaVO.setSituacao( 		dadosRetornoInstituicaoVO.getSituacao());
		pessoaVO.setNumeroConsulta(	dadosRetornoInstituicaoVO.getNumeroConsulta());
		pessoaVO.setDataBaixa(		dadosRetornoInstituicaoVO.getDataBaixa());
		
		pessoaVO.setIesEncontradas(	dadosRetornoInstituicaoVO.getIesEncontradas());
		pessoaVO.setDataInclusao(	dadosRetornoInstituicaoVO.getDataInclusao());
		pessoaVO.setQtdeEncontrada(	dadosRetornoInstituicaoVO.getQtdeEncontrada());
		pessoaVO.setRegimeApuracao(	dadosRetornoInstituicaoVO.getRegimeApuracao());		
		pessoaVO.setEnquandramentoEmpresa(dadosRetornoInstituicaoVO.getEnquandramentoEmpresa());
		pessoaVO.setIdentif(		dadosRetornoInstituicaoVO.getIdentif());
		//pessoaVO.setId(dadosRetornoInstituicaoVO.getId());
		 }
	  }
	}

	
	/**
	 * Limpa retira o ./ e - do cnpj, ie e cpf.
	 * @param valor
	 * @return
	 */
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
	private void processaConsultaIndividual(DllDadosDTO configuracaoDLL,
			List<ResulConsVO> resultadosConsultas, PessoaVO pessoaVO,
			TpValidVO tpValidVO) {
		// se tipo de validacao eh igual sintegra
		// fazer consulta por cnpj e ie
		if (tpValidVO.getId().intValue() == TipoValidacao.SINTEGRA.getCodigo()) {
			
			if(pessoaVO.getCnpj() != null){
				String consultaXMLSintegraCnpj = tipoServicoXML.selecionaXMLTipoValicao(pessoaVO,tpValidVO,TipoDocumento.SINTEGRA_CNPJ.getCodigo(),configuracaoDLL);
				processaConsulta(configuracaoDLL, resultadosConsultas, tpValidVO, consultaXMLSintegraCnpj);
			}

			if(pessoaVO.getIe() != null){
				String consultaXMLSintegraIe = tipoServicoXML.selecionaXMLTipoValicao(pessoaVO,tpValidVO,TipoDocumento.SINTEGRA_IE.getCodigo(),configuracaoDLL);
				processaConsulta(configuracaoDLL, resultadosConsultas, tpValidVO, consultaXMLSintegraIe);
			}

		} else {
			// realiza a consulta normalmente.
			String consultaXML = tipoServicoXML.selecionaXMLTipoValicao(pessoaVO,tpValidVO,configuracaoDLL);
			processaConsulta(configuracaoDLL, resultadosConsultas, tpValidVO, consultaXML);
		}
	}

	/**
	 * @param pessoaVO
	 */
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
	 */
	private void processaConsulta(DllDadosDTO configuracaoDLL, List<ResulConsVO> resultadoConsultaVOList, TpValidVO tpValidVO, String consultaXML) {

		//chama a DLL do EV
		ResulConsVO resultadoConsultaVO = executaConsultaDLL(tpValidVO, consultaXML, configuracaoDLL );

		//analisa e critica os retornos da DLL
		avaliaRetornos(tpValidVO, resultadoConsultaVO );

		// armazena na lista de resultados geral, contemplando todos os tipos de
		// validacao selecionados pelo usu�rio
		resultadoConsultaVOList.add(resultadoConsultaVO);
	}

	
	
	private String getDadosRetorno(){
		String xmlRetorno = new String();
		xmlRetorno =
		"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"+
		"<ResultadoConsulta Versao=\"1.0\" Quantidade=\"1\">"+
		  "<Registro Id=\"0\">"+
		    "<CNPJ>07.613.982/0001-36</CNPJ>"+
		    "<DataAbertura>30/09/2005</DataAbertura>"+
		    "<RazaoSocial>SUPERMERCADO DO MARCAO LTDA</RazaoSocial>"+
		    "<NomeFantasia>********</NomeFantasia>"+
		    "<CodigoAE>47.11-3-02</CodigoAE>"+
		    "<DescricaoAE>- Com�rcio varejista de mercadorias em geral, com predomin�ncia de produtos aliment�cios - supermercados</DescricaoAE>"+
		    "<CodigoNJ>206-2</CodigoNJ>"+
		    "<DescricaoNJ>- SOCIEDADE EMPRESARIA LIMITADA</DescricaoNJ>"+
		    "<Logradouro>R EMILIO ALTEMBURG</Logradouro>"+
		    "<Numero>255</Numero>"+
		    "<Complemento/>"+
		    "<CEP>88.400-000</CEP>"+
		    "<Bairro>CENTRO</Bairro>"+
		    "<Cidade>ITUPORANGA</Cidade>"+
		    "<Estado>SC</Estado>"+
		    "<Situacao>ATIVA</Situacao>"+
		    "<DataSituacao>30/09/2005</DataSituacao>"+
		    "<SituacaoEspecial>********</SituacaoEspecial>"+
		    "<DataSituacaoEspecial>********</DataSituacaoEspecial>"+
		    "<DataConsulta>09/03/2009</DataConsulta>"+
		    "</Pagina>" +		
		  "</Registro>"+
		"</ResultadoConsulta>";

		    return xmlRetorno;
		
	}
	
	
	/**
	 * @param tpValidVO
	 * @param consultaXML
	 * @return
	 */
	protected ResulConsVO executaConsultaDLL(TpValidVO tpValidVO, String consultaXML, DllDadosDTO configuracaoDLL) {

		Integer tpValidacao = tpValidVO.getId().intValue();
		
		//String resultado = consultaOnLine.DBI_EfetuaConsultaServico(tpValidacao, consultaXML, configuracaoDLL.getDllPath(), configuracaoDLL.getEvServer());
		String resultado = this.getDadosRetorno();
		
		ResulConsVO resultadoConsultaVO = unmarshallRetornoDll(resultado);
		return resultadoConsultaVO;
	}
	

	/**
	 * registra erro em log ...
	 * @param e
	 */
	public void registraErroDeProcessamento(ValidadorConsumerException e) {
		// TODO Auto-generated method stub

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
	
		if(log.isDebugEnabled()) log.debug("imprimeRetorno versao do documento:"+resultadoConsultaVO.getVersao());
		
		Collection<DadosRetInstVO> dadosRetornoList = 
			resultadoConsultaVO.getRegistrosRetorno();
		
		for (Iterator iterator = dadosRetornoList.iterator(); iterator.hasNext();) {
			DadosRetInstVO dadosRetornoInstituicaoVO = (DadosRetInstVO) iterator.next();
			
			if(log.isDebugEnabled()) log.debug(dadosRetornoInstituicaoVO.getSituacao());			
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
	public void gravaResultado(ParametrosConsultaOnLineDTO consultaOnLineDTO, List<ResulConsVO> resultadoList) {

		PessoaVO pessoaVO = consultaOnLineDTO.getRegistroPessoaVO();
		
		// apenas para garantir o pedido de validacao
		pessoaVO.setPedValidFk(pesquisaPedValidacaoVO(consultaOnLineDTO));

		for (ResulConsVO resultado : resultadoList) {
			Set<DadosRetInstVO> dadosRetornoList = resultado.getRegistrosRetorno();

			// atribui todos os retornos encontrados ao usu�rio
			
			pessoaVO.getDadosRetornoFk().addAll(dadosRetornoList);
		}

		try {
			this.incluiRegistroPessoa(pessoaVO); // -- SIMPLES: APENAS INVOCA O EntityManager
		} catch (PersistenciaException e) {
			if(log.isDebugEnabled()) log.debug("Erro ao tentar incluir dados retorno instituicao!");
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
	 */
	private void avaliaRetornos(TpValidVO tipoValidacaoVO, ResulConsVO resultado) {

		Set<DadosRetInstVO> dadosRetornoList = resultado.getRegistrosRetorno();

		if (!dadosRetornoList.isEmpty()) {
			//se houve resultado identifica o tipo de resultado e atribui ao retorno
			for (DadosRetInstVO dadosRetInstVO : dadosRetornoList) {

				TpResultVO tpResultVO = identificaTipoResultado(resultado);
				
				dadosRetInstVO.setTipoValidacaoFk(tipoValidacaoVO);
				dadosRetInstVO.setTipoResultadoFk(tpResultVO);
			}
		} else {
			//cria um �nico dados de retorno vazio com retorno referente � mensagem de erro retornada
			dadosRetornoList = criaDadoRetInstVazio(tipoValidacaoVO, resultado);
		
		}
		resultado.setRegistrosRetorno(dadosRetornoList);
		
	}

	/**
	 * @param tipoValidacaoVO
	 * @param resultado
	 * @return
	 */
	protected Set<DadosRetInstVO> criaDadoRetInstVazio(TpValidVO tipoValidacaoVO, ResulConsVO resultado) {

		//retorno padr�o vazio
		DadosRetInstVO dadoRetVazio = new DadosRetInstVO();
		dadoRetVazio.setTipoValidacaoFk(tipoValidacaoVO);
		dadoRetVazio.setTipoResultadoFk(identificaTipoResultado(resultado));
		
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

	

	/**
	 * Implementa um tratamento ger�rico para o erro
	 */
	@Override
	public void registraErroDeProcessamento(Exception e, ParametrosConsultaOnLineDTO consultaOnLineDTO) {
		// TODO Auto-generated method stub
		
	}

	// FIXME Revisar
	@Override
	public void senderDadosCliente(ParametrosConsultaOnLineDTO parameterObject)
			throws BaseValidadorException, ValidadorConsumerException {
	}


	/**
	 * @param dadosRetornoInstituicaoDAO the dadosRetornoInstituicaoDAO to set
	 */
	public void setDadosRetornoInstituicaoDAO(DadosRetornoInstituicaoDAO dadosRetornoInstituicaoDAO) {
		this.dadosRetornoInstituicaoDAO = dadosRetornoInstituicaoDAO;
	}


	/**
	 * @return the dadosRetornoInstituicaoDAO
	 */
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

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<DadosRetInstVO> consultaCliente(String documento, Integer tpConsulta,
			Integer tpServico, String uf) throws ValidadorConsumerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void senderDadosPersistence(Object[] parameterObject) {
		// TODO Auto-generated method stub
		
	}
	
}
