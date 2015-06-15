package br.com.dlp.framework.exception;

public class BaseTechnicalError extends BaseError {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5284004744284518796L;

	private String messageKey;

	public BaseTechnicalError(String message) {
		super(message);
	}

	public BaseTechnicalError(String message, Throwable throwable) {
		super(message, throwable);
	}

	public BaseTechnicalError(Throwable throwable) {
		super(throwable.getMessage(), throwable);
	}

	public BaseTechnicalError(String message, String messageKey) {
		super(message, messageKey);
	}

	public BaseTechnicalError(String message, Throwable throwable,
			String messageKey) {
		super(message, throwable, messageKey);
	}

	public BaseTechnicalError(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getMessageKey() {
		return messageKey;
	}

}
