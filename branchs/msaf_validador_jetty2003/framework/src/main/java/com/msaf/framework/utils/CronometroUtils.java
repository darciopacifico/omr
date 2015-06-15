package com.msaf.framework.utils;

import org.apache.log4j.Logger;

/**
 * Calcula o tempo de processamento.
 * @author vista
 *
 */
public class CronometroUtils {

	Logger log = Logger.getLogger(CronometroUtils.class);

	private long inicio  = 0;
	
	private long fim = 0; 
	
	private long milisegundos = 0;
	
	private long segundos = 0;
	
	private String processamentoInfo = new String();


	public void setProcessamentoInfo(String processamentoInfo){
		
		this.processamentoInfo = processamentoInfo;
	}
	
	public void iniciarCronometro(){
		inicio = System.currentTimeMillis();
	}
	
	public void pararCronometro(){
		fim = System.currentTimeMillis();
		calcularTempo();
	}
	
	
	private void calcularTempo() {
		milisegundos = fim - inicio;
		segundos = milisegundos / 1000;
	}
	
	public void resultadoMilisegundos(){
		log.debug("-----Processo:"+processamentoInfo+"-----");
		log.debug("Tempo de processamento:"+milisegundos+"ms" );
		log.debug("-------------------------");
	}
	
	public void resultadoSegundos(){
		log.debug("-----Processo:"+processamentoInfo+"-----");		
		log.debug("Tempo de processamento:"+segundos+"s");
		log.debug("-------------------------");
		
	}
}
