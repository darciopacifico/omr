package br.com.dlp.framework.transaction;

import br.com.dlp.framework.exception.BaseTechnicalError;

public class TransactionFactoryException extends BaseTechnicalError {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8933497729970508020L;

	/**
	 * @param message
	 */
	public TransactionFactoryException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param messageKey
	 */
	public TransactionFactoryException(String message, String messageKey) {
		super(message, messageKey);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public TransactionFactoryException(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 * @param messageKey
	 */
	public TransactionFactoryException(String message, Throwable throwable,
			String messageKey) {
		super(message, throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public TransactionFactoryException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 * @param messageKey
	 */
	public TransactionFactoryException(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
		// TODO Auto-generated constructor stub
	}
}
