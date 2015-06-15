package com.msaf.validador.test;

import org.apache.log4j.Logger;

import com.msaf.validador.consultaonline.jna.IConsultaOnLineJNA;
import com.msaf.validador.consultaonline.validador.ValidadorOnlineRMIClient;

public class ConsultaValidadorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		IConsultaOnLineJNA consultaOnLineJNA = new ValidadorOnlineRMIClient(10000l,"rmi://localhost:1099/ValidadorOnline");
		
		
		
		String xmlEntrada = 				
		"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
		"<ParametrosConsulta Versao=\"\"1.0\"\"\">" +
		"<TipoConsulta>0</TipoConsulta>" +
		"<Valor>01967492000151</Valor>" +
		"<Estado>RS</Estado>" +
		"<Proxy>" +
		"<Ativo>false</Ativo>" +
		"<Usuario/><Senha/>" +
		"<Host>192.168.10.24</Host>" +
		"<Porta>8002</Porta>" +
		"</Proxy>" +
		"</ParametrosConsulta>";
		
		String resposta = consultaOnLineJNA.DBI_EfetuaConsultaServico(1, xmlEntrada, "c:\\EV Server", "c:\\EV Server");

		if(log.isDebugEnabled()) log.debug(resposta);
		
	}
	
	private final static Logger log = Logger.getLogger("Validador");

}
