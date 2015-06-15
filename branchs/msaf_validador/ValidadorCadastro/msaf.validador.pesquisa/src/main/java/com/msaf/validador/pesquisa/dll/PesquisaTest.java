package com.msaf.validador.pesquisa.dll;

import com.msaf.validador.pesquisa.Pesquisador;

public class PesquisaTest implements Pesquisador{

	@Override
	public void freeMemory(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String pesquisar(Integer service, String xmlParametro) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Pesquisa OK";
	}

}
