package com.msaf.validador.consultaonline;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

import com.msaf.validador.consultaonline.jna.IConsultaOnLineJNA;
import com.sun.jna.Native;

/**
 * 
 * 
 * @author dlopes
 * 
 */
public class TestJNADll {
	Logger log = Logger.getLogger(TestJNADll.class);

	/**
	 * @param args
	 */
	public void testDllValidador() {
		String dllServer = "C:/dll/EV Server";
		String resultado;
		IConsultaOnLineJNA consultaOnLine = (IConsultaOnLineJNA) Native
				.loadLibrary("ConsultaOnLine", IConsultaOnLineJNA.class);

//		 log.debug("Inicio consulta Sintegra");
//		 resultado = consultaOnLine.DBI_EfetuaConsultaServico(1,
//		 geraXmlDePedidoValidacaoSintegra
//		 ("0","PR","45842622010249"),dllServer, dllServer);
//		 System.out.println(resultado);
//		 log.debug("Feita uma consulta! Sintegra");
		 log.debug("Inicio consulta CRM");
		 resultado =
		consultaOnLine.DBI_EfetuaConsultaServico(8,geraXmlDePedidoValidacaoCRM
		 ("33484","SP"),dllServer, dllServer);
		 System.out.println(resultado);
		 log.debug("Feita uma consulta! CRM");
//		 log.debug("Inicio consulta Anvisa");
//		 resultado = consultaOnLine.DBI_EfetuaConsultaServico(9,
//		 geraXmlDePedidoValidacaoAnvisa("06273675000271"),dllServer,
//		 dllServer);
//		 System.out.println(resultado);
//		 log.debug("Feita uma consulta! Anvisa");
//		 log.debug("Inicio consulta MunicipioIBGE");
//		 resultado = consultaOnLine.DBI_EfetuaConsultaServico(10,
//		 geraXmlDePedidoValidacaoMunicipioIBGE("Sao Paulo", "SP"),dllServer,
//		 dllServer);
//		 System.out.println(resultado);
//		 log.debug("Feita uma consulta! MunicipioIBGE");
//		 log.debug("Inicio consulta ANP");
//		 resultado =
//		 consultaOnLine.DBI_EfetuaConsultaServico(13,geraXmlDePedidoValidacaoANP
//		 ("03222638000165"),dllServer, dllServer);
//		 System.out.println(resultado);
//		 log.debug("Feita uma consulta! ANP");

		 log.debug("Inicio consulta Federal");
		 resultado = consultaOnLine.DBI_EfetuaConsultaServico(3,
				 geraXmlDePedidoValidacaoFederal("45842622010249"),dllServer,
		 dllServer);
		 System.out.println(resultado);
		 log.debug("Feita uma consulta! Federal");
	}

	private String geraXmlDePedidoValidacaoSintegra(String tipo, String estado,
			String documento) {

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

	private String geraXmlDePedidoValidacaoCRM(String documento, String estado) {

		return MessageFormat
				.format(
						"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?> <ParametrosConsulta Versao=\"1.0\">"
								+ "<CRM>{0}</CRM><Estado></Estado><Proxy><Ativo>False</Ativo><Usuario/><Senha/><Host>192.168.10.24</Host>"
								+ "<Porta>8002</Porta></Proxy></ParametrosConsulta>",
						documento, estado);
	}

	private String geraXmlDePedidoValidacaoAnvisa(String documento) {

		return MessageFormat
				.format(
						"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><ParametrosConsulta Versao=\"1.0\">"
								+ "<CNPJ>{0}</CNPJ><Proxy> <Ativo>False</Ativo>  <Usuario/> <Senha/> "
								+ "<Host>192.168.10.24</Host> <Porta>8002</Porta> </Proxy> </ParametrosConsulta>",
						documento);
	} //

	private String geraXmlDePedidoValidacaoMunicipioIBGE(String municipio,
			String estado) {

		return MessageFormat
				.format(
						"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><ParametrosConsulta Versao=\"1.0\"> "
								+ "<Municipio>{0}</Municipio><Estado>{1}</Estado><Proxy><Ativo>False</Ativo><Usuario/><Senha/> "
								+ "<Host>192.168.10.24</Host><Porta>8002</Porta></Proxy></ParametrosConsulta>",
						municipio, estado);
	}

	private String geraXmlDePedidoValidacaoANP(String documento) {

		return MessageFormat
				.format(
						"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><ParametrosConsulta Versao=\"1.0\"><Valor>{0}</Valor>"
								+ "<Proxy><Ativo>False</Ativo><Usuario/><Senha/><Host>192.168.10.24</Host><Porta>8002</Porta></Proxy>"
								+ "</ParametrosConsulta>", documento);
	} //



	public static void main(String[] args) {
		TestJNADll dll = new TestJNADll();

		dll.testDllValidador();

	}

}

// String resultado2 =consultaOnLine.DBI_EfetuaConsultaServico(1,
// geraXmlDePedidoValidacaoSintegra("0","RS","03405822000140") ,dllServer,
// dllServer);
// System.out.println(resultado2);
// log.debug("Feita uma consulta! RS");
//
// String resultado3 =consultaOnLine.DBI_EfetuaConsultaServico(1,
// geraXmlDePedidoValidacaoSintegra("0","SP","00382468000198"), dllServer,
// dllServer);
// System.out.println(resultado3);
// log.debug("Feita uma consulta! SP");