package com.msaf.validador.pesquisa;

import javax.naming.ServiceUnavailableException;

import org.apache.log4j.Logger;
import org.springframework.remoting.RemoteAccessException;

import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;
import com.msaf.validador.pesquisa.exception.TimeOutException;

public class ValidadorPesquisaOnline {
	
	private static final transient Logger log = Logger.getLogger(ValidadorPesquisaOnline.class);

	private Pesquisador pesquisaDll;
	
	private long timeout;
	
	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public void setPesquisaDll(Pesquisador pesquisaDll) {
		this.pesquisaDll = pesquisaDll;
	}

	public void freeMemory(String id) {
		
		try{
			pesquisaDll.freeMemory(id);
		}catch(Exception e){
			if(log.isDebugEnabled()) log.debug("Erro ao efetuar a pesquisa");
			throw new ValidadorRuntimeException(e);			
		}
	}

	public String pesquisar(Integer service, String xmlParametro) throws TimeOutException {
		if(log.isDebugEnabled()) log.debug("Efetuando pesquisa...");
		return this.pesquisar(pesquisaDll, service, xmlParametro);
	}

	public void destroy(){
	}

	
	private String pesquisar(Pesquisador pesquisador, Integer service, String xmlParametro) throws TimeOutException {
		
		ThreadPesquisa threadPesquisa = new ThreadPesquisa(pesquisador, service, xmlParametro, log);
		
		Thread thread = new Thread(threadPesquisa);
		
		long timeFinal = System.currentTimeMillis() + getTimeout();
		
		thread.start();
		
		while((threadPesquisa.getResult() == null) && (System.currentTimeMillis() < timeFinal)) {
			
			try {
				
				Thread.sleep(50);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// verifica qual foi o motivo do fim da execução
		if(threadPesquisa.getResult() != null) {
			if(log.isDebugEnabled()) log.debug("Pesquisa efetuada com sucesso!");
			return threadPesquisa.getResult();
		} else {
			//thread.interrupt();
			throw new TimeOutException("Timeout ao esperar o processo finalizar");
		}
	}
}

class ThreadPesquisa implements Runnable {

	private Pesquisador pesquisador;
	private String result;
	private Integer service;
	private String xmlParametro;
	private Logger log;
	
	public ThreadPesquisa(Pesquisador pesquisador, Integer service, String xmlParametro, Logger log){
		this.pesquisador = pesquisador;
		this.service = service;
		this.xmlParametro = xmlParametro;
		this.log = log;
	}
	
	@Override
	public void run() {
		try{
			long timeIni = System.currentTimeMillis();
			result = pesquisador.pesquisar(this.service, this.xmlParametro);
			if(log.isDebugEnabled()) log.debug("Pesquisa com DLL executada em " + ((System.currentTimeMillis() - timeIni) / 1000) + " segundos.");
		}catch(RemoteAccessException e) {
			if(log.isDebugEnabled()) log.debug("Erro ao pesquisar: " + e.getMessage());
			System.out.flush();
			throw new RuntimeException(new ServiceUnavailableException("Erro ao obter client RMI: " + e.getMessage()));
		}
	}

	
	public synchronized String getResult() {
		return result;
	}
}