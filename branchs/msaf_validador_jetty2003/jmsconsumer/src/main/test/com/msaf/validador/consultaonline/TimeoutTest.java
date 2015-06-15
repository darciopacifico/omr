package com.msaf.validador.consultaonline;

import java.util.Timer;
import java.util.TimerTask;







public class TimeoutTest {

	   
		
	public String resultado = new String();
	public static void main(String args[]) {
	
		TimeoutTest validador = new TimeoutTest();
		
		Timer timer;
		int tempoMiliSegundos = 4000;
		timer = new Timer();
		RemindTask relogio = validador.new RemindTask(timer, 3);
		
		timer.schedule(relogio, 0, 1 * tempoMiliSegundos);
		
		System.out.println("passou por aqui...");

		System.out.println("Task scheduled.");
		System.out.println(":"+ validador.resultado);
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
		System.out.println("Inicio...");
		if (tentativas > 0) {
			// FIXME colocar aki o ProcessBiulder
			System.out.println("TESTES");
			//TESTE_DE_TEXTO = "Valor";
			if (!TESTE_DE_TEXTO.equals("")) {
				System.out.println("TERMINOU DE PROCESSAR");
				
				timer.cancel();
			}
			resultado = "TESTES";
			tentativas--;
		} else {
			// FIXME Devolver erro e rava no banco.
			timer.cancel();
			System.out.println("FIN");
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