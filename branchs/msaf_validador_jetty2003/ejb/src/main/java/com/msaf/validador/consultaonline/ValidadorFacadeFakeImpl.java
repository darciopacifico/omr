package com.msaf.validador.consultaonline;

import java.io.StringReader;
import java.util.Collection;
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
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpResultVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;
import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Implementação do serviço do Validador (WebService)
 * 
 * @author dlopes
 * 
 */
@WebService(endpointInterface = "com.msaf.validador.consultaonline.ValidadorFacade")
public class ValidadorFacadeFakeImpl implements ValidadorFacade {

	// injetado pelo spring.

	  private TiposServicoXMLFactory tipoServicoXML;
	  private IConsultaOnLineJNA consultaOnLine;

	Logger log = Logger.getLogger(ValidadorFacadeFakeImpl.class);

	private Unmarshaller unmarshaller;
	private JAXBContext jaxbContext;
	
	public ValidadorFacadeFakeImpl() {
		log.debug("Instanciando servico!");
		tipoServicoXML = new TiposServicoXMLFactory();
		Native.setProtected(true);



		Library library = (Library) Native.loadLibrary("ConsultaOnLine", IConsultaOnLineJNA.class);
		library = Native.synchronizedLibrary(library);


		consultaOnLine = (IConsultaOnLineJNA)library;
	}

	/**
	 * Implementação da consulta de dados de cliente
	 * @throws ValidadorConsumerException 
	 */
	@WebMethod
	public void consultaDadosCliente(ParametrosConsultaOnLineDTO parameterObject, DllDadosDTO configuracaoDLL, List<ResulConsVO> resultadosConsultas) throws ValidadorConsumerException {

		PessoaVO pessoaVO = parameterObject.getRegistroPessoaVO();
		formataCnpj(pessoaVO);

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
		System.out.println("Identificador Encontrado:"+pessoaVO.getIdentif());
	}

	
	
	/**
	 * Metodo responsável por popular o pessoa VO 
	 * com os dados recuperados da consulta a RF.
	 * @param resultadosConsultas
	 * @param pessoaVO pessoaVO.
	 * @return pessoaVO pessoaVO populado com os dados da RF.
	 */
	private void populaPessoaVOComDadosRetornoRF(
			List<ResulConsVO> resultadosConsultas, PessoaVO pessoaVO) {
		//recupera o resultado da consulta a receita federal e popula o pessoaVO com as informacoes necessárias.
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
		//  fornecidas pelo usuário em caso
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
		    "<DescricaoAE>- Comércio varejista de mercadorias em geral, com predominância de produtos alimentícios - supermercados</DescricaoAE>"+
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
			resultadoConsultaVO.setErro("Serviço de consulta ao órgão indisponível!");

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

		PessoaVO pessoaVO = consultaOnLineDTO.getRegistroPessoaVO();
		
		System.out.println("Gravar Pessoa:"+pessoaVO.getIdentif());
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
		System.out.println("ID enviado no XLS:"+parameterObject.getRegistroPessoaVO().getIdentif());
		
	}


	/**
	 * @param dadosRetornoInstituicaoDAO the dadosRetornoInstituicaoDAO to set
	 */
	public void setDadosRetornoInstituicaoDAO(DadosRetornoInstituicaoDAO dadosRetornoInstituicaoDAO) {
	}


	/**
	 * @return the dadosRetornoInstituicaoDAO
	 */
	public DadosRetornoInstituicaoDAO getDadosRetornoInstituicaoDAO() {
		return null;
	}


	/**
	 * @param registroPessoaDAO the registroPessoaDAO to set
	 */
	public void setRegistroPessoaDAO(RegistroPessoaDAO registroPessoaDAO) {
	}


	/**
	 * @return the registroPessoaDAO
	 */
	public RegistroPessoaDAO getRegistroPessoaDAO() {
		return null;
	}


	/**
	 * @param tipoResultadoDAO the tipoResultadoDAO to set
	 */
	public void setTipoResultadoDAO(TipoResultadoDAO tipoResultadoDAO) {
	}


	/**
	 * @return the tipoResultadoDAO
	 */
	public TipoResultadoDAO getTipoResultadoDAO() {
		return null;
	}


	/**
	 * @param pedidoValidacaoDAO the pedidoValidacaoDAO to set
	 */
	public void setPedidoValidacaoDAO(PedidoValidacaoDAO pedidoValidacaoDAO) {
	}


	/**
	 * @return the pedidoValidacaoDAO
	 */
	public PedidoValidacaoDAO getPedidoValidacaoDAO() {
		return null;
	}

	
	public TipoValidacaoDAO getTipoValidacaoDAO() {
		return null;
	}

	
	public void setTipoValidacaoDAO(TipoValidacaoDAO tipoValidacaoDAO) {
	}

	
	
	
}
