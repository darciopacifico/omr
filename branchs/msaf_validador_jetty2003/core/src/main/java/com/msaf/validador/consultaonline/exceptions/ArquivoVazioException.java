/**
 * 
 */
package com.msaf.validador.consultaonline.exceptions;

/**
 * 
 * Lancado caso o arquivo enviar seja vazio
 * @author dlopes
 *
 */
public class ArquivoVazioException extends ArquivoInvalidoException {

	/**
	 * 
	 */
	public ArquivoVazioException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ArquivoVazioException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ArquivoVazioException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ArquivoVazioException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
