package com.msaf.validador.consultaonline;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;


public class TimeoutTest {

	   
	private static Logger log = Logger.getLogger(TimeoutTest.class);
		
	public String resultado = new String();
	public static void main(String args[]) {
	
		TimeoutTest validador = new TimeoutTest();
		
		Timer timer;
		int tempoMiliSegundos = 4000;
		timer = new Timer();
		RemindTask relogio = validador.new RemindTask(timer, 3);
		
		timer.schedule(relogio, 0, 1 * tempoMiliSegundos);
		
		if(log.isDebugEnabled()) log.debug("passou por aqui...");

		if(log.isDebugEnabled()) log.debug("Task scheduled.");
		if(log.isDebugEnabled()) log.debug(":"+ validador.resultado);
	}





class RemindTask extends TimerTask {

	Timer timer;
	int tentativas = 0;

	String TESTE_DE_TEXTO = "";

	public RemindTask(Timer timer2, int tentativas) {
		this.timer = timer2;
		this.tentativas = tentativas;
	}

	public void run() {
		if(log.isDebugEnabled()) log.debug("Inicio...");
		if (tentativas > 0) {
			// FIXME colocar aki o ProcessBiulder
			if(log.isDebugEnabled()) log.debug("TESTES");
			//TESTE_DE_TEXTO = "Valor";
			if (!TESTE_DE_TEXTO.equals("")) {
				if(log.isDebugEnabled()) log.debug("TERMINOU DE PROCESSAR");
				
				timer.cancel();
			}
			resultado = "TESTES";
			tentativas--;
		} else {
			// FIXME Devolver erro e rava no banco.
			timer.cancel();
			if(log.isDebugEnabled()) log.debug("FIN");
		}
	}

	public String getTESTE_DE_TEXTO() {
		return TESTE_DE_TEXTO;
	}

	public void setTESTE_DE_TEXTO(String teste_de_texto) {
		TESTE_DE_TEXTO = teste_de_texto;
	}
	
}

}