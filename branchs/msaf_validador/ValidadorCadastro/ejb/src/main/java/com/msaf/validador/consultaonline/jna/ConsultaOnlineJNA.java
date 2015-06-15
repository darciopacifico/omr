package com.msaf.validador.consultaonline.jna;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;
import com.msaf.validador.pesquisa.ValidadorPesquisaOnline;
import com.msaf.validador.pesquisa.exception.TimeOutException;

/**
 * Implementa��o do servi�o do Validador (WebService)
 * 
 * @author eAmaral
 * 
 */
public class ConsultaOnlineJNA {

	private static final transient Logger log = Logger.getLogger(ConsultaOnlineJNA.class);
	private ValidadorPesquisaOnline pesquisa = null;
	
	public ConsultaOnlineJNA(){
		this.pesquisa= new ValidadorPesquisaOnline();
	}

	/**
	 * @param tpValidVO
	 * @param consultaXML
	 * @return string
	 * @throws TimeOutException 
	 */
	public String executaConsultaDLL(Integer tpValidacao, String consultaXML) throws TimeOutException {
		return pesquisa.pesquisar(tpValidacao, consultaXML);
	}

	/**
	 * Retorna se a String passada est� vazia
	 * 
	 * @param texto
	 * @return
	 */
	public boolean isEmpty(final String texto) {
		return texto == null || texto.trim().length() == 0;
	}
	
	public void close() {
		this.pesquisa.destroy();
	}
	
}
