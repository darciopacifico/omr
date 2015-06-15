package com.msaf.validador.core.server;

import java.io.StringReader;
import java.text.MessageFormat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;
import com.msaf.validador.consultaonline.jna.ConsultaOnlineJNA;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;



public class WebServiceServer {
	
	private ConsultaOnlineJNA consultaOnlineJNA;
	private Unmarshaller unmarshaller;
	private JAXBContext jaxbContext;

	public WebServiceServer(){}
	public WebServiceServer(ConsultaOnlineJNA consultaOnlineJNA){
		this.consultaOnlineJNA = consultaOnlineJNA;
		
	}
	
	private String TIPO_CNPJ = "0";
//	private String TIPO_IE = "1";
	
	public ResulConsVO processarRegistro(PessoaVO pessoaVO,String tipo) {
		 
		String XMLConsulta = pedidoXML(pessoaVO, tipo);
		
		String XMLRetorno = consultaOnlineJNA.executaConsultaDLL(Integer.parseInt(tipo), XMLConsulta);
		
		return unmarshallRetornoDll(XMLRetorno);
		
	}
	


	private ResulConsVO unmarshallRetornoDll(String resultado){
		Unmarshaller unmarshaller = getUnmarshaller();
		resultado = resultado.replaceAll("&", "e");

		Source source  = new StreamSource(new StringReader(resultado));

		ResulConsVO resultadoConsultaVO;
		try {
			resultadoConsultaVO = (ResulConsVO) unmarshaller.unmarshal(source);

		} catch (Exception e) {
			resultadoConsultaVO = new ResulConsVO();
			resultadoConsultaVO.setCodErro("10001");
			resultadoConsultaVO.setErro("Serviço de consulta ao órgão indisponível!");
		}
		return resultadoConsultaVO;
	}
	
	private Unmarshaller getUnmarshaller(){
		if(this.unmarshaller==null){
			
			try {
				this.unmarshaller = getJAXBContext().createUnmarshaller();
			} catch (JAXBException e) {
				throw new ValidadorRuntimeException("Erro ao tentar recuperar um ummarshaller para o XML de retorno!",e);
			}
		}
		
		return this.unmarshaller;
	}
	
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
	
	private String pedidoXML(PessoaVO pessoaVO, String tipo) {
		switch (Integer.parseInt(tipo)) {
		case 1:
			return geraXmlDePedidoValidacaoSintegra(TIPO_CNPJ, pessoaVO.getEstado(), pessoaVO.getCnpj());
		case 3:
			return geraXmlDePedidoValidacaoFederal(pessoaVO.getCnpj());
		};
		return null;
	}

	private String geraXmlDePedidoValidacaoSintegra(String tipo, String estado,	String documento) {

		return MessageFormat
				.format(
						"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><ParametrosConsulta Versao=\"1.0\">"
								+ "<TipoConsulta>{0}</TipoConsulta><Estado>{1}</Estado><Valor>{2}</Valor><Proxy>"
								+ "<Ativo>False</Ativo><Usuario/><Senha/><Host>192.168.10.24</Host><Porta>8002</Porta>"
								+ "</Proxy>" + "</ParametrosConsulta>", tipo,
						estado, documento);
	}
	
	private String geraXmlDePedidoValidacaoFederal(	String documento) {

		return MessageFormat
				.format(
						"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><ParametrosConsulta Versao=\"1.0\">"
								+ "<Valor>{0}</Valor><Proxy>"
								+ "<Ativo>False</Ativo><Usuario/><Senha/><Host>192.168.10.24</Host><Porta>8002</Porta>"
								+ "</Proxy>" + "</ParametrosConsulta>", documento);
	}
}
