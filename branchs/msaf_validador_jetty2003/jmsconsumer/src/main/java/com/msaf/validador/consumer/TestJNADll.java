package com.msaf.validador.consumer;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import com.msaf.framework.utils.CronometroUtils;
import com.msaf.validador.consultaonline.exceptions.ValidadorException;
import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;
import com.msaf.validador.consultaonline.jna.IConsultaOnLineJNA;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.sun.jna.Library;
import com.sun.jna.Native;



/**
 * 
 * 
 * @author dlopes
 *
 */
public class TestJNADll {
	Logger log = Logger.getLogger(CronometroUtils.class);
	
	/**
	 * @param args
	 */
	public void testDllValidador() {
		System.out.println("comeco");
		Native.setProtected(true);
		
		
		Library library = (Library) Native.loadLibrary("ConsultaOnLine", IConsultaOnLineJNA.class);
		
		library = Native.synchronizedLibrary(library);
		
		IConsultaOnLineJNA consultaOnLine = (IConsultaOnLineJNA) library;

		//00362315000189	282878769

/*		String resultado  = consultaOnLine.DBI_EfetuaConsultaServico(1,
				"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"
						+ "<ParametrosConsulta Versao=\"1.0\">"
						//+ "<TipoConsulta>1</TipoConsulta>"
						+ "<TipoConsulta>0</TipoConsulta>"
						+ "<Estado>PE</Estado>"
						//+ "<Valor>27621543833</Valor>" + 
						+ "<Valor>02435676000133</Valor>" +
						"<Proxy>"
						+ "<Ativo>False</Ativo>" + "<Usuario/>" + "<Senha/>"
						+ "<Host>192.168.10.24</Host>" + "<Porta>8002</Porta>"
						+ "</Proxy>" + "</ParametrosConsulta>",
				"C:\\EV Server", "C:\\EV Server");*/
		
//		
//		String resultado  = consultaOnLine.DBI_EfetuaConsultaServico(1,	
//				"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
//				"<ParametrosConsulta Versao=\"1.0\">" +
//				"<TipoConsulta>0</TipoConsulta>" +
//				"<Valor>00173342000456</Valor>" +
//				"<Estado>SP</Estado>" +
//				"<Proxy>" +
//				"<Ativo>false</Ativo>" +
//				"<Usuario/><Senha/>" +
//				"<Host>192.168.10.24</Host" +
//				"><Porta>8002</Porta>" +
//				"</Proxy>" +
//				"</ParametrosConsulta>",
//				
//				"C:\\EV Server", "C:\\EV Server");
		
		String resultado  = consultaOnLine.DBI_EfetuaConsultaServico(1,	
				"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
				"<ParametrosConsulta Versao=\"1.0\">" +
				"<TipoConsulta>0</TipoConsulta>" +
				"<Valor>01967492000151</Valor>" +
				"<Estado>RS</Estado>" +
				"<Proxy>" +
				"<Ativo>false</Ativo>" +
				"<Usuario/><Senha/>" +
				"<Host>192.168.10.24</Host" +
				"><Porta>8002</Porta>" +
				"</Proxy>" +
				"</ParametrosConsulta>",
				
				"C:\\EV Server", "C:\\EV Server");		
	
		
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
			log.debug("...:"+resultado);
			
			
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("fim");
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
		for (int i = 0; i <6; i++) {

			Thread thread = new Thread() {
				@Override
				public void run() {
					TestJNADll dll = new TestJNADll();
					
					dll.testDllValidador();
				}
			};
			thread.start();
		}
		
	}
	
}
