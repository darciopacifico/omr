package com.msaf.validador.consultaonline.validador;
//com.msaf.validador.consultaonline.validador.ValidadorOnlineRMIService;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;
import com.msaf.validador.consultaonline.jna.IConsultaOnLineJNA;

/**
 * Servico RMI para validacao de cadastro. Normalmente compoe uma instância de EBJ Validador
 * 
 * @author dlopes
 * 
 */
public class ValidadorOnlineRMIService implements IConsultaOnLineJNA, Remote  {
	//private Logger log = Logger.getLogger(ValidadorOnlineRMIService.class);
	//private static Logger stLog = Logger.getLogger(ValidadorOnlineRMIService.class);
	
	IConsultaOnLineJNA consultaOnLine ;
	
	public ValidadorOnlineRMIService() {
		//consultaOnLine = new ConsultaOnLineImpl();
	}
	
	/**
	 * Cria serviço RMI para validacao
	 * @param args
	 */
	public static void main(String[] args) {
		
		ValidadorOnlineRMIService validadorOnlineEscravo = new ValidadorOnlineRMIService();
		//stif(log.isDebugEnabled()) log.debug("Iniciando main do servidor RMI");
		//TODO: RECEBER NOME DO SERVIÇO E PORTA: INSTANCIAR RMI DINAMICAMENTE.
		String bindName = getBindName(args);
		
		try {
			Naming.bind(bindName, validadorOnlineEscravo);
		} catch (RemoteException e) {
		//	stif(log.isDebugEnabled()) log.debug("Erro ao tentar registrar servico rmi",e);
			throw new ValidadorRuntimeException("Erro ao tentar criar serviço RMI para validacao!",e);
		} catch (MalformedURLException e) {
			//stif(log.isDebugEnabled()) log.debug("Erro ao tentar registrar servico rmi",e);
			throw new ValidadorRuntimeException("Erro ao tentar criar serviço RMI para validacao. URL invalida!!",e);
		} catch (AlreadyBoundException e) {
			//stif(log.isDebugEnabled()) log.debug("Erro ao tentar registrar servico rmi",e);
			throw new ValidadorRuntimeException("Erro ao tentar criar serviço RMI para validacao. O Endereco ja esta sendo usado!!",e);
		}
		
	}

	/**
	 * @param args
	 * @return
	 */
	protected static String getBindName(String[] args) {
		
		if(args.length==0){
			throw new ValidadorRuntimeException("Por favor, informar o nome que será associado ao servico RMI de validacao");
		}
		
		return args[0];
	}


	
	@Override
	public void DBI_DLLStrFree(String memId) {
		this.DBI_DLLStrFree(memId);
	}

	@Override
	public String DBI_EfetuaConsultaServico(Integer servico, String parametros, String dllPath, String evServer) {
		return this.consultaOnLine.DBI_EfetuaConsultaServico(servico, parametros, dllPath, evServer);
	}

}
