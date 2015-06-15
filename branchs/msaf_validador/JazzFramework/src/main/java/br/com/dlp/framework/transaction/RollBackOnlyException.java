package br.com.dlp.framework.transaction;

public class RollBackOnlyException extends TransactionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4460461348937298009L;

	public RollBackOnlyException(String message) {
		super(message);
	}

	public RollBackOnlyException(String message, String messageKey) {
		super(message, messageKey);
	}

	public RollBackOnlyException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public RollBackOnlyException(String message, Throwable throwable,
			String messageKey) {
		super(message, throwable, messageKey);
	}

	public RollBackOnlyException(Throwable throwable) {
		super(throwable);

	}

	public RollBackOnlyException(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
	}
}
