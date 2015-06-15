package com.msaf.validador.pesquisa;




/**
 * Interface que define uma pesquisa para o validador 
 * @author ederson
 *
 */
public interface Pesquisador {
	
	public String pesquisar(Integer service, String xmlParametro);
	
	public void freeMemory(String id);
	
}
