package br.com.dlp.framework.transaction;

import br.com.dlp.framework.exception.BaseTechnicalError;

/**
 * 
 */
public class TransactionException extends BaseTechnicalError {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6820788862685639698L;

	public TransactionException(String message) {
		super(message);
	}

	public TransactionException(String message, String messageKey) {
		super(message, messageKey);
	}

	public TransactionException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public TransactionException(String message, Throwable throwable,
			String messageKey) {
		super(message, throwable, messageKey);
	}

	public TransactionException(Throwable throwable) {
		super(throwable);
	}

	public TransactionException(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
	}
}
