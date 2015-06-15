package br.com.dlp.framework.exception;

public abstract class BaseException extends Exception {
	private String messageKey;

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public BaseException(Throwable throwable) {
		super(throwable);
	}

	public BaseException(String message, String messageKey) {
		super(message);
		setMessageKey(messageKey);
	}

	public BaseException(String message, Throwable throwable, String messageKey) {
		super(message, throwable);
		setMessageKey(messageKey);
	}

	public BaseException(Throwable throwable, String messageKey) {
		super(throwable);
		setMessageKey(messageKey);
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getMessageKey() {
		return messageKey;
	}
}
