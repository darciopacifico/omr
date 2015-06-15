package com.msaf.validador.consumer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.consultaonline.exceptions.ValidadorException;
import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;
import com.msaf.validador.consultaonline.jna.IConsultaOnLineJNA;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.msaf.validador.consultaonline.xml.GeradorXmlMunicipiosIBGE;
import com.sun.jna.Library;
import com.sun.jna.Native;



/**
 * 
 * 
 * @author dlopes
 *
 */
public class TestJNADll {
	public static long contador=0;
	
	public static List<RetornoCronometro> retornos = Collections.synchronizedList(new ArrayList<RetornoCronometro>()); 
	
	protected class RetornoCronometro{
		DadosRetInstVO dadosRetInstVO;
		long inicio;
		long fim;
		
		public RetornoCronometro(DadosRetInstVO dadosRetInstVO, long inicio, long fim) {
			super();
			this.dadosRetInstVO = dadosRetInstVO;
			this.inicio = inicio;
			this.fim = fim;
		}
	}
	
	protected static final class ThreadCnpjUF extends Thread {
		String cnpj;
		String estado;
		
		public ThreadCnpjUF(String cnpj2, String uf) {
			this.cnpj = cnpj2;
			this.estado = uf;
		}

		@Override
		public void run() {

			TestJNADll dll = new TestJNADll();
			dll.testDllValidador(cnpj,estado);

		}
	}

	Logger log = Logger.getLogger(TestJNADll.class);
	
	/**
	 * @param args
	 * @return 
	 */
	public ResulConsVO testDllValidador(String cnpj,String estado) {
//		Native.setProtected(true);
		
		Library library = (Library) Native.loadLibrary("ConsultaOnLine", IConsultaOnLineJNA.class);
		
		//library = Native.synchronizedLibrary(library);
		
		IConsultaOnLineJNA consultaOnLine = (IConsultaOnLineJNA) library;


		

		long inicio = System.currentTimeMillis();
		
		
		String resultado  = consultaOnLine.DBI_EfetuaConsultaServico(3,	
				"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
				"<ParametrosConsulta Versao=\"1.0\">" +
				"<TipoConsulta>0</TipoConsulta>" +
				"<Valor>"+cnpj+"</Valor>" +
				"<Estado>"+estado+"</Estado>" +
				"<Proxy>" +
				"<Ativo>false</Ativo>" +
					"<Usuario/>" +
					"<Senha/>" +
					"<Host/>" +
					"<Porta/>" +
				"</Proxy>" +
				"</ParametrosConsulta>",
				
				"C:\\EV Server", "C:\\EV Server");		
		
		
		/*
		String resultado  = consultaOnLine.DBI_EfetuaConsultaServico(3,
		"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"
		+ "<ParametrosConsulta Versao=\"1.0\"> "
		+ "<Municipio>CAMPINAS</Municipio>" + "<Estado>SP</Estado>"

		+ "<Proxy>"
			+ "<Ativo>true</Ativo>"
			+ "<Usuario>/"
			+ "<Senha/>"
			+ "<Host/>"
			+ "<Porta/>"
		+ "</Proxy>" + 
		"</ParametrosConsulta>", 
		"C:\\EV Server", "C:\\EV Server");
		*/
		System.out.println(resultado);
		
		
		ResulConsVO result = unMarshall(resultado);
		
		Set<DadosRetInstVO> dadosRetInst = result.getRegistrosRetorno();
		if(!result.getCodErro().equals(10000)){
		System.out.println("Inicio results..."+result.getCodErro()+" "+result.getErro());
		}
		
		for (DadosRetInstVO dadosRetInstVO : dadosRetInst) {
			RetornoCronometro retornoCronometro = new RetornoCronometro(dadosRetInstVO,inicio,System.currentTimeMillis());
			TestJNADll.retornos.add(retornoCronometro);
			
			System.out.print(retornoCronometro.fim - retornoCronometro.inicio+" ");
			System.out.print(retornoCronometro.dadosRetInstVO.getRazaoSocial());
			System.out.println();
		}
		//System.out.println("...fim results");
		
		
		
		return null;
	}


	private JAXBContext jcontext;
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	
	protected ResulConsVO unMarshall(String resultado) {
		
		if(this.jcontext==null){
			this.jcontext = getJAXBContext();
		}

		
		ResulConsVO result=null;
		try {
			if(this.marshaller==null)
			this.marshaller = jcontext.createMarshaller();
			
			if(this.unmarshaller==null)
			this.unmarshaller = jcontext.createUnmarshaller();
			
			resultado = resultado.replaceAll("&", "e");
			
			resultado = limpaCaracteresEstranhos(resultado);

			
			Source source  = new StreamSource(new StringReader(resultado));
			
			
			
			result = (ResulConsVO) unmarshaller.unmarshal(source);
			log.debug("...:"+resultado);
			
			
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
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
	 * Retira primeira linha do XML
	 * FIXME: NÃO COMITAR ESTE TRATAMENTO
	 */
	protected String limpaCaracteresEstranhos(String resultado) {
		
		String[] full = resultado.split(""+'\n');
		
		String resultadoLimpo="";
		
		for(int i=1; i<full.length; i++){
			resultadoLimpo = resultadoLimpo+full[i]+'\n';  
		}
		
		
		return resultadoLimpo;
	}



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
		System.out.println(System.currentTimeMillis());
		
		
		Map<String, String> mapCnpjUF = new HashMap<String, String>();
		
		/***** INI Consultas OK *****/
		mapCnpjUF.put("01967492000151","RS");

//		mapCnpjUF.put("04994256000112","CE");
//		mapCnpjUF.put("05906447000148","CE");
//		mapCnpjUF.put("07789902000106","CE");
//		mapCnpjUF.put("05484099000168","CE");
//		mapCnpjUF.put("01967492000151","RS");
//		mapCnpjUF.put("11590296003937","CE");
//		mapCnpjUF.put("07329048000197","CE");
//		mapCnpjUF.put("00930087000104","PB");
//		mapCnpjUF.put("07656686000112","PB");
//		mapCnpjUF.put("04864902000127","MA");
//		mapCnpjUF.put("03935284000104","MA");		
//		mapCnpjUF.put("31784176000105","ES");
//		mapCnpjUF.put("27394584000183","ES");
//		mapCnpjUF.put("31483241000162","ES");
//		mapCnpjUF.put("31700784000194","ES");
//		mapCnpjUF.put("28399582000140","ES");
//		mapCnpjUF.put("04643369000173","ES");
//		mapCnpjUF.put("27176494000116","ES");
//		mapCnpjUF.put("08078777000180","ES");
//		mapCnpjUF.put("39396395000129","ES");
//		mapCnpjUF.put("06916424000187","ES");

		TestJNADll.contador = mapCnpjUF.size();

		/***** FIM Consultas OK *****/
		

		//mapCnpjUF.put("16631087000135","MG");
//		mapCnpjUF.put("78391612000140","PR");
//		mapCnpjUF.put("86740669000135","PR");
//		mapCnpjUF.put("60184405000663","PR");
//		mapCnpjUF.put("76840537000121","SC");
//		mapCnpjUF.put("16670085000155","MG");
//		mapCnpjUF.put("20502605000198","MG");
//		mapCnpjUF.put("16502551000193","MG");
//		mapCnpjUF.put("33337122014187","RJ");
//		mapCnpjUF.put("40374951000145","RJ");
//		mapCnpjUF.put("00431383000152","PR");
//		mapCnpjUF.put("02773629000280","RJ");
//		mapCnpjUF.put("00260413000105","PE");
//		mapCnpjUF.put("11481173000195","PE");
//		mapCnpjUF.put("01253053000187","DF");
//		mapCnpjUF.put("01253053000187","DF");
//		mapCnpjUF.put("02710353000100","PI");
		
		System.out.println("Enviados : " + mapCnpjUF.size());
		for (String cnpj : mapCnpjUF.keySet()) {
			
			String uf = mapCnpjUF.get(cnpj);
			
			Thread thread = new ThreadCnpjUF(cnpj, uf);
			thread.start();
		}
		
	}
	
}
