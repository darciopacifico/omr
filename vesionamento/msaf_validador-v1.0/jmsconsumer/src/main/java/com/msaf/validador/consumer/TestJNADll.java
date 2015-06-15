package com.msaf.validador.consumer;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import com.msaf.validador.consultaonline.exceptions.ValidadorException;
import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;
import com.msaf.validador.consultaonline.jna.IConsultaOnLineJNA;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.sun.jna.Native;



/**
 * 
 * 
 * @author dlopes
 *
 */
public class TestJNADll {

	/**
	 * @param args
	 */
	public void testDllValidador() {

		Native.setProtected(true);
		IConsultaOnLineJNA consultaOnLine = (IConsultaOnLineJNA) Native.loadLibrary("ConsultaOnLine", IConsultaOnLineJNA.class);

		//00362315000189	282878769

		String resultado  = consultaOnLine.DBI_EfetuaConsultaServico(1,
				"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"
						+ "<ParametrosConsulta Versao=\"1.0\">"
						+ "<TipoConsulta>1</TipoConsulta>"
						+ "<Estado>MS</Estado>"
						+ "<Valor>282878769</Valor>" + "<Proxy>"
						+ "<Ativo>False</Ativo>" + "<Usuario/>" + "<Senha/>"
						+ "<Host>192.168.10.24</Host>" + "<Porta>8002</Porta>"
						+ "</Proxy>" + "</ParametrosConsulta>",
				"C:\\EV Server", "C:\\EV Server");
		
		
//		String resultado  = consultaOnLine.DBI_EfetuaConsultaServico(1,
//				"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"
//						+ "<ParametrosConsulta Versao=\"1.0\">"
//						+ "<TipoConsulta>0</TipoConsulta>"
//						+ "<Estado>PR</Estado>"
//						+ "<Valor>45842622010249</Valor>" + "<Proxy>"
//						+ "<Ativo>False</Ativo>" + "<Usuario/>" + "<Senha/>"
//						+ "<Host>192.168.10.24</Host>" + "<Porta>8002</Porta>"
//						+ "</Proxy>" + "</ParametrosConsulta>",
//				"C:\\EV Server", "C:\\EV Server");
	//					"C:\\MasterSaf\\dll\\EV Server", "C:\\MasterSaf\\dll\\EV Server");
						

///string xml = determinaXML(pessoavo, tv, td);		

//		String resultado  = consultaOnLine.DBI_EfetuaConsultaServico(1,
//				"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"
//						+ "<ParametrosConsulta Versao=\"1.0\">"
//						+ "<TipoConsulta>0</TipoConsulta>"
//						+ "<Estado>PR</Estado>"
//						+ "<Valor>03028470000151</Valor>" + "<Proxy>"
//						+ "<Ativo>False</Ativo>" + "<Usuario/>" + "<Senha/>"
//						+ "<Host>192.168.10.24</Host>" + "<Porta>8002</Porta>"
//						+ "</Proxy>" + "</ParametrosConsulta>",
//				"C:\\EV Server", "C:\\EV Server");
//						"C:\\MasterSaf\\dll\\EV Server", "C:\\MasterSaf\\dll\\EV Server");
		
		
//		String resultado  = consultaOnLine.DBI_EfetuaConsultaServico(1,
//				"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"
//						+ "<ParametrosConsulta Versao=\"1.0\">"
//						+ "<TipoConsulta>0</TipoConsulta>"
//						+ "<Estado>PR</Estado>"
//						+ "<Valor>03028470000151</Valor>" + "<Proxy>"
//						+ "<Ativo>False</Ativo>" + "<Usuario/>" + "<Senha/>"
//						+ "<Host>192.168.10.24</Host>" + "<Porta>8002</Porta>"
//						+ "</Proxy>" + "</ParametrosConsulta>",
//				"C:\\EV Server", "C:\\EV Server");
		
//		
		
		//Consulta dados da RF.
//		String resultado  = consultaOnLine.DBI_EfetuaConsultaServico(0,		
//		"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"
//		+"<ParametrosConsulta Versao=\"1.0\">"
//		+"<TipoConsulta>0</TipoConsulta>"
//		+"<Valor>07613982000136</Valor>"
//		+"<Proxy>"
//		+" <Ativo>False</Ativo>"
//		+"   <Usuario/>"
//		+"   <Senha/>"
//		+"   <Host>192.168.10.24</Host>"
//		+"   <Porta>8002</Porta>"
//		+"  </Proxy>"
//		+"</ParametrosConsulta>",
//		"C:\\EV Server", "C:\\EV Server");

		
		
//		String resultado  = consultaOnLine.DBI_EfetuaConsultaServico(3,	
//				"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
//				"<ParametrosConsulta Versao=\"1.0\">" +
//				"<TipoConsulta>0</TipoConsulta>" +
//				"<Valor>07613982000136</Valor>" +
//				"<Proxy>" +
//				"<Ativo>false</Ativo>" +
//				"<Usuario/><Senha/>" +
//				"<Host>192.168.10.24</Host" +
//				"><Porta>8002</Porta>" +
//				"</Proxy>" +
//				"</ParametrosConsulta>",
//				
//				"C:\\EV Server", "C:\\EV Server");
	
		
//		String resultado  = consultaOnLine.DBI_EfetuaConsultaServico(3,			
//		"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
//		"<ParametrosConsulta Versao=\"1.0\">" +
//		"<Valor>00362315000189</Valor>" +
//		"<Proxy>" +
//		"<Ativo></Ativo>" +
//		"<Usuario><Usuario/>" +
//		"<Senha>False<Senha/>" + 
//		"<Host>192.168.10.24</Host>" +
//		"<Proxy>8002</Proxy></Proxy>" +
//		"</ParametrosConsulta>",
//		"C:\\EV Server", "C:\\EV Server");

		
		System.out.println(resultado);
		
		JAXBContext jc = getJAXBContext();

		try {
			Marshaller marshaller = jc.createMarshaller();
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			
			resultado = resultado.replaceAll("&", "e");
			Source source  = new StreamSource(new StringReader(resultado));
			
			
			ResulConsVO result = (ResulConsVO) unmarshaller.unmarshal(source);
			System.out.println("...:"+result);
			
			
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
//	protected ResulConsVO unmarshallRetornoDll(String resultado){
//		Unmarshaller unmarshaller = getUnmarshaller();
//
//		Source source  = new StreamSource(new StringReader(resultado));
//		
//		ResulConsVO resultadoConsultaVO;
//		try {
//			// unmarshaller da requisicao.
//			resultadoConsultaVO = (ResulConsVO) unmarshaller.unmarshal(source);
//						
//		} catch (JAXBException e) {
//			throw new ValidadorRuntimeException("Erro ao tentar executar o unmarshaller sobre o XML de retorno!",e);
//		}
//		return resultadoConsultaVO;
//	}

	/**
	 * @return
	 * @throws ValidadorException
	 */
	protected JAXBContext getJAXBContext(){
		JAXBContext jc;
		try {
			//jc = JAXBContext.newInstance("com.msaf.validador.consultaonline.vos");
			jc = JAXBContext.newInstance("com.msaf.validador.consultaonline.solicitacaovalidacao");
		} catch (JAXBException e) {
			throw new ValidadorRuntimeException("Erro ao tentar recuperar o JAXBContext dos VOs do Validador.",e);
		}
		return jc;
	}
	
	public static void main(String[] args) {
		TestJNADll dll = new TestJNADll();
		dll.testDllValidador();
		
		
	}
	
}
