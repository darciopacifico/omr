/**
 * 
 */
package com.msaf.validador.consultaonline.exceptions;

/**
 * Denota qualquer tipo de erro ao consumir uma mensagem JMS. Lembrando que,
 * pela especificação JMS, o consumidor não pode subir exceções checked.
 * 
 * @author dlopes
 * 
 */
public class ValidadorConsumerException extends BaseValidadorException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2669846197663172293L;

	/**
	 * 
	 */
	public ValidadorConsumerException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ValidadorConsumerException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ValidadorConsumerException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ValidadorConsumerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
