/*
 * Created on 31/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.exception;

/**
 * Exceção runtime básica do framework
 * @author dpacifico
 *
 */
public class BaseError extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8803953025909007798L;
	private String messageKey;

	public BaseError(String message) {
		super(message);
	}

	public BaseError(String message, Throwable throwable) {
		super(message, throwable);
	}

	public BaseError(Throwable throwable) {
		super(throwable);
	}

	public BaseError(String message, String messageKey) {
		super(message);
		setMessageKey(messageKey);
	}

	public BaseError(String message, Throwable throwable, String messageKey) {
		super(message, throwable);
		setMessageKey(messageKey);
	}

	public BaseError(Throwable throwable, String messageKey) {
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
