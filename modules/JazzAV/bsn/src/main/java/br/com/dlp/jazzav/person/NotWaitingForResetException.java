/**
 * 
 */
package br.com.dlp.jazzav.person;

import br.com.dlp.framework.exception.JazzBusinessException;

/**
 * @author darcio
 *
 */
public class NotWaitingForResetException extends JazzBusinessException {

	/**
	 * 
	 */
	public NotWaitingForResetException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NotWaitingForResetException(String message) {
		super(message);
		// TODO Auto-generated constructor stub 
	}

	/**
	 * @param message
	 * @param messageKey
	 */
	public NotWaitingForResetException(String message, String messageKey) {
		super(message, messageKey);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public NotWaitingForResetException(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 * @param messageKey
	 */
	public NotWaitingForResetException(String message, Throwable throwable, String messageKey) {
		super(message, throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public NotWaitingForResetException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 * @param messageKey
	 */
	public NotWaitingForResetException(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

}
