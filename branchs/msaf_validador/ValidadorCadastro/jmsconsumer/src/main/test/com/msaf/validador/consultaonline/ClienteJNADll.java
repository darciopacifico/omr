package com.msaf.validador.consultaonline;

import org.apache.log4j.Logger;

import com.msaf.validador.consultaonline.jna.IConsultaOnLineJNA;
import com.sun.jna.Native;

/**
 * 
 * 
 * @author dlopes
 * 
 */
public class ClienteJNADll {

	private Logger log = Logger.getLogger(ClienteJNADll.class);

	private static Integer instance=1;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		for (int i = 0; i < 1; i++) {
			criaDisparaThreadConsultaOnLine();
		}

	}

	/**
	 * 
	 */
	protected static void criaDisparaThreadConsultaOnLine() {

		
		/**
		ClienteJNADll clienteJNADll = new ClienteJNADll();
		clienteJNADll.consultaOnLine();
		
		 * 
		 */
		Runnable runnable = criaRunnable();
		Thread thread = new Thread(runnable);
		thread.start();
	}

	/**
	 * 
	 */
	protected static Runnable criaRunnable() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				ClienteJNADll clienteJNADll = new ClienteJNADll();
				clienteJNADll.consultaOnLine();

			}
		};

		return r;
	}

	/**
	 * Invoca DLL de consulta onLine
	 */
	protected void consultaOnLine() {
		IConsultaOnLineJNA consultaOnLine = criaDllWrapper();

		//consultaOnLine = sincronizarDllWrapper(consultaOnLine);
		
		String resultado = consultaOnLine.DBI_EfetuaConsultaServico(1,
				"<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"
						+ "<ParametrosConsulta Versao=\"1.0\">"
						+ "<TipoConsulta>0</TipoConsulta>"
						+ "<Estado>PR</Estado>"
						+ "<Valor>45842622010249</Valor>" + "<Proxy>"
						+ "<Ativo>False</Ativo>" + "<Usuario/>" + "<Senha/>"
						+ "<Host>192.168.10.24</Host>" + "<Porta>8002</Porta>"
						+ "</Proxy>" + 
						"</ParametrosConsulta>",
				"C:\\EV Server", "C:\\EV Server");
	
		
		if(log.isDebugEnabled()) log.debug("Exec OK!!");
		if(log.isDebugEnabled()) log.debug(resultado);

		//consultaOnLine.DBI_DLLStrFree(resultado);
		
	}

	/**
	 * Cria wrapper da dll de consulta on line
	 * @return
	 */
	protected IConsultaOnLineJNA criaDllWrapper() {
		Native.setProtected(true);
		IConsultaOnLineJNA consultaOnLineJNA;
		
		
		//consultaOnLineJNA = (IConsultaOnLineJNA) Native.loadLibrary("ConsultaOnLine"+instance++, IConsultaOnLineJNA.class);
		consultaOnLineJNA = (IConsultaOnLineJNA) Native.loadLibrary("ConsultaOnLine", IConsultaOnLineJNA.class);

		
		return consultaOnLineJNA;
	}

	/**
	 * @param consultaOnLineJNA
	 * @return
	 */
	private IConsultaOnLineJNA sincronizarDllWrapper(
			IConsultaOnLineJNA consultaOnLineJNA) {
		return (IConsultaOnLineJNA) Native.synchronizedLibrary(consultaOnLineJNA);
	}

}
