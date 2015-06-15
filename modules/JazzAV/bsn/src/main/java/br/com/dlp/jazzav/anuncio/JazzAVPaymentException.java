/**
 * 
 */
package br.com.dlp.jazzav.anuncio;

import br.com.dlp.framework.exception.JazzBusinessException;

/**
 * @author dpacifico
 *
 */
public class JazzAVPaymentException extends JazzBusinessException {

	/**
	 * 
	 */
	public JazzAVPaymentException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public JazzAVPaymentException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param messageKey
	 */
	public JazzAVPaymentException(String message, String messageKey) {
		super(message, messageKey);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public JazzAVPaymentException(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 * @param messageKey
	 */
	public JazzAVPaymentException(String message, Throwable throwable,
			String messageKey) {
		super(message, throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public JazzAVPaymentException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 * @param messageKey
	 */
	public JazzAVPaymentException(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

}
