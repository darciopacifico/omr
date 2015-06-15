package com.msaf.test.process;

import org.apache.log4j.Logger;


public class ChamadorContador {

	private static Logger log = Logger.getLogger(ChamadorContador.class);

	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable {
		Contador contador = new Contador();
		Thread thread = new Thread(contador);
		thread.start();
		
		
		
		
		long tempoMaximo = contador.getTempo()+1000;
		
		if(log.isDebugEnabled()) log.debug("momento "+System.currentTimeMillis());
		if(log.isDebugEnabled()) log.debug("maximo  "+tempoMaximo);
		
		while (System.currentTimeMillis() <= tempoMaximo  && !contador.isFinalizado()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		if(contador.isFinalizado()){
			if(log.isDebugEnabled()) log.debug("terminou OK");
		}else{
			if(log.isDebugEnabled()) log.debug("Timeout");
			//thread.stop();
			//System.exit(0);
			throw new RuntimeException("Saida por timeout");
		}
		
		
	}

}
