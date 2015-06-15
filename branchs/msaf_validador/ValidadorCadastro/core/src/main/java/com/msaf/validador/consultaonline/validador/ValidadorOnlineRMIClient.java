package com.msaf.validador.consultaonline.validador;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;
import com.msaf.validador.consultaonline.jna.IConsultaOnLineJNA;

/**
 * 
 * @author Ederson de Lima
 *
 */
public class ValidadorOnlineRMIClient implements IConsultaOnLineJNA {
	public static final String RETORNO_PARAMETROS_INVALIDO="retornoParametrosInvalidos";
	public static final String RETORNO_TIMEOUT="retornoTimeout";
	
	Logger log = Logger.getLogger(ValidadorOnlineRMIClient.class);

	//processo do servidor RMI
	private Process rmiServerProcess;
	
	//Stub apontando para o processo que está rodando em this.rmiServerProcess
	private IConsultaOnLineJNA consultaOnLineJNA;
	
	//timeout para destruir o processo
	private long timeOut;
	

	
	/**
	 * 
	 * @param envServer
	 * @param pathServer
	 * @param timeOut
	 */
	public ValidadorOnlineRMIClient(long timeOut,String serviceName) {

		//this.rmiServerProcess = instanciaProcessoValidadorRMI(serviceName);
				
		try {
			this.consultaOnLineJNA = localizaServico("hhh");
		} catch (MalformedURLException e) {
			throw new ValidadorRuntimeException("Erro ao tentar localizar serviço! URL mal formada!",e);
		} catch (RemoteException e) {
			throw new ValidadorRuntimeException("Erro ao tentar localizar serviço!",e);
		} catch (NotBoundException e) {
			throw new ValidadorRuntimeException("Erro ao tentar localizar serviço! Serviço não encontrado!",e);
		}
		
		this.timeOut = timeOut;
	}
	

	
	/**
	 * Localiza serviço RMI
	 * @param serviceName
	 * @return
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 */
	private IConsultaOnLineJNA localizaServico(String serviceName) throws MalformedURLException, RemoteException, NotBoundException {
		
		IConsultaOnLineJNA consultaOnLineJNA = (IConsultaOnLineJNA) Naming.lookup(serviceName);
		
		return consultaOnLineJNA;
		
	}


	/**
	 * Inicializar processo do servidor RMI
	 * @param serviceName
	 * @return 
	 */
	protected Process instanciaProcessoValidadorRMI(String serviceName) {
		List<String> comandos = new ArrayList<String>();
		
		comandos.add("java");
		comandos.add("-jar");
		comandos.add("msaf.validador.jmsconsumer-1.0.jar");
		comandos.add(serviceName);
		//comandos.add("-Xdebug");
		//comandos.add("-Xrunjdwp:transport=dt_socket,address=4145,server=y,suspend=y");
		
		ProcessBuilder processBuilder = new ProcessBuilder(comandos);
		processBuilder.directory(new File("C:\\darcio\\trabalho\\SVN_Validador\\jmsconsumer\\target"));
		try {
			return processBuilder.start();
		} catch (IOException e) {
			throw new ValidadorRuntimeException("Erro ao tentar inicializar processo!",e);
		}
	}



	/**
	 * Limpa área de memoria da DLL
	 */
	@Override
	public void DBI_DLLStrFree(String memId) {
		this.DBI_DLLStrFree(memId);
	}


	/**
	 * Efetuar consulta pela DLL
	 */
	@Override
	public String DBI_EfetuaConsultaServico(Integer servico, String parametros, String dllPath, String evServer) {
		return this.consultaOnLineJNA.DBI_EfetuaConsultaServico(servico, parametros, dllPath, evServer);
	}

	/**
	 * Cria thread monitora do timeout das consultas
	 * @param process
	 */
	protected void criarThreadTimeout(final Process process, final Long timeout) {
		Thread controlaTimeoutProcesso = new Thread(){
			@Override
			public void run() {
				long tempoMaximo = System.currentTimeMillis()+timeout;
				
				while(System.currentTimeMillis() < tempoMaximo){
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				process.destroy();
			}
		};
		controlaTimeoutProcesso.start();
	}


}
