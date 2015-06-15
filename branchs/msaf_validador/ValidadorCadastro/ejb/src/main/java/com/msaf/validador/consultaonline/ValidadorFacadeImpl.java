package com.msaf.validador.consultaonline;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
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
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;
import com.msaf.validador.pesquisa.ValidadorPesquisaOnline;
import com.msaf.validador.pesquisa.exception.TimeOutException;

/**
 * Implementação do serviço do Validador (WebService)
 * 
 * @author dlopes
 * 
 */
public class ValidadorFacadeImpl implements ValidadorFacade {

	// injetado pelo spring.

	public void setDllDadosDTO(DllDadosDTO dllDadosDTO) {
		this.dllDadosDTO = dllDadosDTO;
	}

	private TiposServicoXMLFactory tipoServicoXML;
  	
	Logger log = Logger.getLogger(ValidadorFacadeImpl.class);


	private DadosRetornoInstituicaoDAO dadosRetornoInstituicaoDAO;
	private RegistroPessoaDAO registroPessoaDAO;
	private TipoResultadoDAO tipoResultadoDAO;
	private PedidoValidacaoDAO pedidoValidacaoDAO;
	private TipoValidacaoDAO tipoValidacaoDAO;
	private DllDadosDTO dllDadosDTO;

	private Unmarshaller unmarshaller;
	private JAXBContext jaxbContext;
	
	private int numTentativas;

	public int getNumTentativas() {
		return numTentativas;
	}

	public void setNumTentativas(int numTentativas) {
		this.numTentativas = numTentativas;
	}

	public ValidadorFacadeImpl() {
		if(log.isDebugEnabled()) log.debug("Instanciando servico!");
		tipoServicoXML = new TiposServicoXMLFactory();
	}

	public void consultaDadosCliente(ParametrosConsultaOnLineDTO parameterObject, DllDadosDTO configuracaoDLL, List<ResulConsVO> resultadosConsultas) throws ValidadorConsumerException {
		if(log.isDebugEnabled()) log.debug("Consultando dados de cliente...");
		PessoaVO pessoaVO = parameterObject.getRegistroPessoaVO();
		formataCnpj(pessoaVO);

		List<TpValidVO> listTpValidacao = parameterObject.getTipoValidacaoVO();

		// para cada tipo de valicao seria uma consulta diferente.
		for (TpValidVO tpValidVO : listTpValidacao) {

			if (tpValidVO.getId().intValue() == TipoValidacao.COMPLETA.getCodigo()) {
				// realiza a consulta completa
				processaConsultaCompleta(configuracaoDLL, resultadosConsultas,pessoaVO);
				
			} else {
				
				// processa a consulta aos servicos inidividualmente.
				processaConsultaIndividual(configuracaoDLL, resultadosConsultas, pessoaVO, tpValidVO);
			}

		}
		if(log.isDebugEnabled()) log.debug("Dados de cliente consultados com sucesso!");
	}

	/**
	 * @param configuracaoDLL
	 * @param resultadosConsultas
	 * @param pessoaVO
	 * @param tpValidVO
	 * @throws ConnectException 
	 * @throws PersistenciaException
	 */
	
	@SuppressWarnings("unchecked")
	private void processaConsultaCompleta(DllDadosDTO configuracaoDLL, List<ResulConsVO> resultadosConsultas, PessoaVO pessoaVO) {
		
		if(log.isDebugEnabled()) log.debug("Processa consulta completa...");
		
		// inicialmente realiza a consulta para receita federal.
		if(pessoaVO.getCnpj() != null) {
			
			try {
				
				//seta o tipo de validacao para a RF.
				TpValidVO tpValidacaoRF = new TpValidVO();
				
				tpValidacaoRF = tipoValidacaoDAO.buscarPorId(new Long(TipoValidacao.RECEITA_FEDERAL.getCodigo()));
				
				String consultaXMLReceitaFederal = tipoServicoXML.selecionaXMLTipoValicao(pessoaVO,tpValidacaoRF, TipoDocumento.RECEITA_CNPJ.getCodigo(),configuracaoDLL);
				
				processaConsulta(configuracaoDLL, resultadosConsultas, tpValidacaoRF, consultaXMLReceitaFederal, TipoDocumento.RECEITA_CNPJ.getCodigo());
				
				populaPessoaVOComDadosRetornoRF(resultadosConsultas, pessoaVO);
				
				List<TpValidVO>  todasValidacoes = tipoValidacaoDAO.buscarTodos();
				
				//realiza a consulta para todos os servicos com excecao do id rf e completa. 
				for (TpValidVO tipoValidacao : todasValidacoes) {
					
					if((tipoValidacao.getId() != TipoValidacao.RECEITA_FEDERAL.getCodigo()) && (tipoValidacao.getId() != TipoValidacao.COMPLETA.getCodigo())){
						processaConsultaIndividual(configuracaoDLL, resultadosConsultas, pessoaVO, tipoValidacao);
					}
						
				}// end for.
				
				
			} catch (PersistenciaException e) {
				throw new ValidadorRuntimeException("Erro ao tentar recuperar a lista de validacoes",e);
			}
		
		}
		if(log.isDebugEnabled()) log.debug("Consulta completa executado com sucesso!");
	}


	/**
	 * Metodo responsável por popular o pessoa VO 
	 * com os dados recuperados da consulta a RF.
	 * @param resultadosConsultas
	 * @param pessoaVO pessoaVO.
	 * @return pessoaVO pessoaVO populado com os dados da RF.
	 */
	private void populaPessoaVOComDadosRetornoRF(List<ResulConsVO> resultadosConsultas, PessoaVO pessoaVO) {
		// recupera o resultado da consulta a receita federal e popula o
		// pessoaVO com as informacoes necessárias.
		if(log.isDebugEnabled()) log.debug("Popula pessoa VO...");
		for (ResulConsVO resultadoConsulta : resultadosConsultas) {
			Set<DadosRetInstVO> dadosRetInstList = resultadoConsulta.getRegistrosRetorno();

			for (DadosRetInstVO dadosRetornoInstituicaoVO : dadosRetInstList) {

				pessoaVO.setEstado(dadosRetornoInstituicaoVO.getEstado());

				// validacao para nao sobrescrever as informacoes originais
				// fornecidas pelo usuário em caso
				if (dadosRetornoInstituicaoVO.getCnpj() != null)
					pessoaVO.setCnpj(limparCNPJ_IE_CPF(dadosRetornoInstituicaoVO.getCnpj()));

				if (dadosRetornoInstituicaoVO.getIe() != null)
					pessoaVO.setIe(limparCNPJ_IE_CPF(dadosRetornoInstituicaoVO.getIe()));

				if (dadosRetornoInstituicaoVO.getCpf() != null)
					pessoaVO.setCpf(limparCNPJ_IE_CPF(dadosRetornoInstituicaoVO.getCpf()));

				
			}
		}
		if(log.isDebugEnabled()) log.debug("Popula pessoaVO executado com sucesso!");
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
	 * @throws ConnectException 
	 */
	
	private void processaConsultaIndividual(DllDadosDTO configuracaoDLL, List<ResulConsVO> resultadosConsultas, PessoaVO pessoaVO, TpValidVO tpValidVO) {
		
		// se tipo de validacao eh igual sintegra
		// fazer consulta por cnpj e ie
		
		if(log.isDebugEnabled()) log.debug("Processa consulta individual...");
		
		if (tpValidVO.getId().intValue() == TipoValidacao.SINTEGRA.getCodigo()) {
			
			if(pessoaVO.getCnpj() != null){
				String consultaXMLSintegraCnpj = tipoServicoXML.selecionaXMLTipoValicao(pessoaVO,tpValidVO,TipoDocumento.SINTEGRA_CNPJ.getCodigo(),configuracaoDLL);
				processaConsulta(configuracaoDLL, resultadosConsultas, tpValidVO, consultaXMLSintegraCnpj, TipoDocumento.SINTEGRA_CNPJ.getCodigo());
			}

			if(pessoaVO.getIe() != null) {
				
				if((pessoaVO.getCnpj() != null) && (resultadosConsultas != null && !resultadosConsultas.isEmpty())){
					
					// deixa apenas com números
					String patern = "[ -._]*";
					String iePesquisa = pessoaVO.getIe().replace(patern, "");
					String ieResultado = "";
					
					/* percorre os resultados anteriores para verificar se a inscrição estadual informada 
					 * é igual ao retorno obtido na pesquisa anterior.
					 * sendo igual não há a necessidade de realizar uma nova pesquisa 
					 */
					for (ResulConsVO resulConsVO : resultadosConsultas) {
						
						/*
						 * percorre os resultados anteriores
						 */
						for (DadosRetInstVO dados : resulConsVO.getRegistrosRetorno()) {
							
							ieResultado = dados.getIe();
							
							if(ieResultado != null) {
								
								ieResultado = ieResultado.replaceAll(patern, "");
								
								// verifica se a Inscrição estadual informada é igual ao do resultado obtido na pesquisa
								if(iePesquisa.equals(ieResultado)) {
									return;
								}
							}
						}
					}
				}
				
				// realiza a pesquisa por inscrição estadual
				String consultaXMLSintegraIe = tipoServicoXML.selecionaXMLTipoValicao(pessoaVO,tpValidVO,TipoDocumento.SINTEGRA_IE.getCodigo(),configuracaoDLL);
				processaConsulta(configuracaoDLL, resultadosConsultas, tpValidVO, consultaXMLSintegraIe, TipoDocumento.SINTEGRA_IE.getCodigo());
			}

		} else {
			// realiza a consulta normalmente.
			String consultaXML = tipoServicoXML.selecionaXMLTipoValicao(pessoaVO,tpValidVO, configuracaoDLL);
			processaConsulta(configuracaoDLL, resultadosConsultas, tpValidVO, consultaXML, "");
		}
		
		if(log.isDebugEnabled()) log.debug("Processa consulta individual executado com sucesso!");
	}

	/**
	 * @param pessoaVO
	 */
	
	protected void formataCnpj(PessoaVO pessoaVO) {
		String cnpj = (pessoaVO.getCnpj() == null ? "" : pessoaVO.getCnpj());
		try{
			cnpj = String.format("%014d", Long.parseLong(cnpj) ); 
			
		}catch (Throwable e) {//
		}

		pessoaVO.setCnpj(cnpj);
	}

	/**
	 * @param configuracaoDLL
	 * @param resultadoConsultaVOList
	 * @param tpValidVO
	 * @param consultaXML
	 * @param tipoDocumento 
	 * @param numTentativas TODO
	 * @param tipoValidacao
	 * @throws ConnectException 
	 */
	
	public Boolean processaConsulta(DllDadosDTO configuracaoDLL, List<ResulConsVO> resultadoConsultaVOList, TpValidVO tpValidVO, String consultaXML, String tipoDocumento) {
		
		if(log.isDebugEnabled()) log.debug("Processa consulta...");
		
		int count = 1;
    	System.out.println("Efetuando tentativa " + count + " de "+ numTentativas +"...");

		//chama a DLL do EV
		ResulConsVO resultadoConsultaVO = executaConsultaDLL(tpValidVO, consultaXML, configuracaoDLL);

    	Boolean retorno = avaliaRetornos(tpValidVO, resultadoConsultaVO, tipoDocumento);

	    while(!retorno.booleanValue() && (count < numTentativas)) {
	    	count++;
	    	System.out.println("Efetuando tentativa " + count + " de "+ numTentativas +"...");

	    	resultadoConsultaVO = executaConsultaDLL(tpValidVO, consultaXML, configuracaoDLL);

			retorno = avaliaRetornos(tpValidVO, resultadoConsultaVO, tipoDocumento);
	    }

    	System.out.println("Finalizou com " + (retorno.booleanValue() ? "sucesso" : "fracasso") + " em "+ count +" tentativas...");

    	// armazena na lista de resultados geral, contemplando todos os tipos de
		// validacao selecionados pelo usuário
		resultadoConsultaVOList.add(resultadoConsultaVO);
		
		if(log.isDebugEnabled()) log.debug("Processa consulta executado com sucesso!");
		return retorno;
	}

	/**
	 * @param tpValidVO
	 * @param consultaXML
	 * @return
	 */
	
	protected ResulConsVO executaConsultaDLL(TpValidVO tpValidVO, String consultaXML, DllDadosDTO configuracaoDLL) {
		if(log.isDebugEnabled()) log.debug("Executa consulta DLL...");
		Integer tpValidacao = tpValidVO.getId().intValue();
		
		String resultado;

		try {
			resultado = validadorPesquisa.pesquisar(tpValidacao, consultaXML);
			resultado = resultado.substring(resultado.indexOf(">") + 1).trim(); // TODO Retirar!!!!
		} catch (TimeOutException e) {
			throw new RuntimeException(e);
		} // TODO RMI
		
		ResulConsVO resultadoConsultaVO = unmarshallRetornoDll(resultado);
		if(log.isDebugEnabled()) log.debug("Consulta DLL executado com sucesso!");
		return resultadoConsultaVO;
	}
	
	private ValidadorPesquisaOnline validadorPesquisa;

	public void setValidadorPesquisa(ValidadorPesquisaOnline validadorPesquisa) {
		this.validadorPesquisa = validadorPesquisa;
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
		if(log.isDebugEnabled()) log.debug("Unmarshall retorno DLL...");
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
			resultadoConsultaVO.setErro("Serviço de consulta ao órgão indisponível!");

			//throw new ValidadorRuntimeException("Erro ao tentar executar o unmarshaller sobre o XML de retorno!",e);
		}
		if(log.isDebugEnabled()) log.debug("Unmarshall retorno DLL executado com sucesso!");
		return resultadoConsultaVO;
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
	 * Responsável por armazenar os resultados da validacao na base de dados.
	 * @param resultadoConsultaVO vo com as informacaoes consultadas na dll.
	 * @author Ekler Paulino de Mattos
	 */

	/**
	 * IMplementar a gravacao dos resultados da consulta (parametro de entrada + resultado vindo da DLL)
	 * 
	 */
	@Transactional
	
	public void gravaResultado(ParametrosConsultaOnLineDTO consultaOnLineDTO, List<ResulConsVO> resultadoList) {
		this.senderDadosPersistence(new Object[]{consultaOnLineDTO, resultadoList});
	}

	

	
	/**
	 * Responsável por inserir o tipo de validacao das consultas
	 * no resultado de consulta.
	 * @param tipoValidacaoVO tipo de validacao realizada.
	 * @return dadosRetornoList lista de resultados.
	 * @author Ekler Paulino de Mattos
	 * @param tipoDocumento 
	 * @throws ConnectException 
	 */
	public Boolean avaliaRetornos(TpValidVO tipoValidacaoVO, ResulConsVO resultado, String tipoDocumento) {
		
		if(log.isDebugEnabled()) log.debug("Avalia retornos...");

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
			//cria um único dados de retorno vazio com retorno referente à mensagem de erro retornada
			dadosRetornoList = criaDadoRetInstVazio(tipoValidacaoVO, resultado, tipoDocumento);
		
		}
		resultado.setRegistrosRetorno(dadosRetornoList);

		for (DadosRetInstVO dadosRetInstVO : dadosRetornoList) {
			if(dadosRetInstVO.getTipoResultadoFk() != null && dadosRetInstVO.getTipoResultadoFk().isRestartable())
				return Boolean.FALSE;
		}
		
		if(log.isDebugEnabled()) log.debug("Avalia retornos executado com sucesso!");
		
		return Boolean.TRUE;
		
	}

	/**
	 * @param tipoValidacaoVO
	 * @param resultado
	 * @param tipoDocumento 
	 * @return
	 * @throws ConnectException 
	 */
	protected Set<DadosRetInstVO> criaDadoRetInstVazio(TpValidVO tipoValidacaoVO, ResulConsVO resultado, String tipoDocumento) {

		//retorno padrão vazio
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
	 * @throws ConnectException 
	 */
	@Transactional
	private TpResultVO identificaTipoResultado(ResulConsVO resultado) {
		
		assert (resultado != null);
		
		if(log.isDebugEnabled()) log.debug("Identifica tipo resultado...");
		
		TpResultVO tipoResultado;
		
		Long idTipoResultado = identificaTpResult(resultado);

		if(log.isDebugEnabled()) log.debug("TIPO DE RESULTADO: " + idTipoResultado);

		// busca na base de dados o resultado.
		try {
			tipoResultado = getTipoResultadoDAO().buscarPorId(idTipoResultado);
		} catch(Exception e){
			if(log.isDebugEnabled()) log.debug("Erro ao buscar tipo de resultado: " + e.getMessage());
			throw new ValidadorRuntimeException("Erro ao tentar buscar tipo de resultado",e);
		}

		// insere o tipo de resultado.
		if (tipoResultado == null) {
			tipoResultado = new TpResultVO();
			tipoResultado.setId(idTipoResultado);
			// insercao da descricao do resultado seja de erro ou nao.
			if (idTipoResultado == TpResultVO.PROCESSADO_SUCESSO)
				tipoResultado.setDescricao("Processado com sucesso.");
			else
				tipoResultado.setDescricao(resultado.getErro());
			
			try {
				getTipoResultadoDAO().criarTipoResultadoVO(tipoResultado);
			} catch(Exception e){
				if(log.isDebugEnabled()) log.debug("Erro ao criar tipo de resultado: " + e.getMessage());
				throw new ValidadorRuntimeException("Erro ao tentar criar tipo de resultado",e);
			}

		}
		
		try {
			tipoResultado = getTipoResultadoDAO().buscarPorId(tipoResultado.getId());
		} catch (PersistenciaException e) {
			if(log.isDebugEnabled()) log.debug("Mensagem: " + e.getMessage());
			throw new ValidadorRuntimeException("Erro ao tentar buscar tipo de resultado",e);
		}

		if(log.isDebugEnabled()) log.debug("Identifica tipo resultado executado com sucesso...");
		return tipoResultado;

	}


	/**
	 * @param resultado
	 * @return
	 */
	protected Long identificaTpResult(ResulConsVO resultado) {
		Long idTipoResultado = TpResultVO.PROCESSADO_SUCESSO;
		// se existir o codigo de erro.
		if (!"".equals(resultado.getCodErro()))
			idTipoResultado = Long.valueOf(resultado.getCodErro());
		return idTipoResultado;
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
	public void registraErroDeProcessamento(Exception e, ParametrosConsultaOnLineDTO consultaOnLineDTO) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void registraErroDeProcessamento(ValidadorConsumerException e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void close() {
		//this.consultaOnLine.close();
	}

		@Override
		public Set<DadosRetInstVO> consultaCliente(String documento, Integer tpConsulta, Integer tpServico, String uf) throws ValidadorConsumerException {
			
			if(log.isDebugEnabled()) log.debug("Chegou no webService - Documento " + documento);
			
			Set<DadosRetInstVO> retorno = new HashSet<DadosRetInstVO>();
			
			final List<ResulConsVO>  resulConsVO = new ArrayList<ResulConsVO>();
			
			if(log.isDebugEnabled()) log.debug("Retorna tipos");
			
			TpValidVO tpValidVO = null;
			
			try {
				
				tpValidVO = getTipoValidacaoDAO().buscarPorId(tpServico.longValue());
				
			} catch (PersistenciaException e) {
				throw new ValidadorConsumerException(e.getMessage());
			}
			
			if(tpValidVO == null) {
				throw new ValidadorConsumerException("Tipo de validacao nao encontrado!");
			}
			
			final PessoaVO pessoa = criarPessoa( tpConsulta, documento, tpValidVO, uf);

			if(log.isDebugEnabled()) log.debug("Formata CNPJ");
			
			formataCnpj(pessoa);
			
			if(log.isDebugEnabled()) log.debug("Executa consulta");
			
			executaConsulta(pessoa, tpValidVO, resulConsVO);
			
			for (ResulConsVO resulCons : resulConsVO) {
				
				for(DadosRetInstVO dado : resulCons.getRegistrosRetorno()){
					dado.setTipoValidacaoFk(null); // Como excluir do XML?
				}
				
				retorno.addAll(resulCons.getRegistrosRetorno());
			}
			
			if(log.isDebugEnabled()) log.debug("Devolve resultado" + retorno);		

			return retorno;
		}
	
		/**
		 * Executa a consulta conforme serviço informado.
		 * @param pessoaVO
		 * @param tpValidVO
		 * @throws ConnectException 
		 */
		private void executaConsulta(PessoaVO pessoaVO, final TpValidVO tpValidVO, List<ResulConsVO> resulConsVO) {
			
			if (tpValidVO.getId().intValue() == TipoValidacao.COMPLETA.getCodigo()) {
				// realiza a consulta completa
				processaConsultaCompleta(this.getDllDadosDTO(), resulConsVO,pessoaVO);
				
			} else {
				
				if(log.isDebugEnabled()) log.debug("Consultar sintegra...");
				
				// processa a consulta aos servicos inidividualmente.
				processaConsultaIndividual(this.getDllDadosDTO(),resulConsVO, pessoaVO, tpValidVO);
				
				if(log.isDebugEnabled()) log.debug("Consultou sintegra..." + resulConsVO.size());
			}
		}	

		/**
		 * Cria instancia de pessoaVo conforme Tipo de Serviço e documento solicitado.
		 * @param tpConsulta
		 * @param documento
		 * @param tpValidVO
		 * @param uf 
		 * @return
		 */
		private PessoaVO criarPessoa(Integer tpConsulta, String documento, TpValidVO tpValidVO, String uf) {
			
			final PessoaVO pessoa = new PessoaVO();
			
			if(log.isDebugEnabled()) log.debug("UF - " + uf);
			pessoa.setEstado(uf);
			
			if (tpConsulta.intValue() == 0) {
				pessoa.setCnpj(documento);
				return pessoa;
			}
			
			if(tpValidVO.getId().intValue() == TipoValidacao.SINTEGRA.getCodigo()) {
				pessoa.setIe(documento);
			} else if (tpValidVO.getId().intValue() == TipoValidacao.RECEITA_FEDERAL.getCodigo()) {
				pessoa.setCpf(documento);
			}
			
			return pessoa;
		}

		public DllDadosDTO getDllDadosDTO() {
			return dllDadosDTO;
		}

		@Override
		public void senderDadosPersistence(Object[] parameterObject) {
			ValidadorFacadeMQImpl mqImpl = new ValidadorFacadeMQImpl("jndi/validadorOnlinePersist");
			mqImpl.senderDadosPersistence(parameterObject);
			try {
				mqImpl.destroyQueue();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
	
	
}
