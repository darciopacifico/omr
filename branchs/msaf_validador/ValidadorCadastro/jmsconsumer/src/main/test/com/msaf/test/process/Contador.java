package com.msaf.test.process;

import com.msaf.validador.consumer.TestJNADll;

public class Contador implements Runnable {

  private long tempo;

  private boolean finalizado=false;
  
  
  public Contador() {
		tempo = System.currentTimeMillis();
	}
  
  
  public void run() {

      //pesquisar
      TestJNADll testJNADll = new TestJNADll();
      //testJNADll.testDllValidador();
      finalizado = true;
  }
  

	/**
	 * @return the tempo
	 */
	public long getTempo() {
		return tempo;
	}

	/**
	 * @param tempo the tempo to set
	 */
	public void setTempo(long tempo) {
		this.tempo = tempo;
	}

	public boolean isFinalizado() {
		// TODO Auto-generated method stub
		return this.finalizado;
	}

}
