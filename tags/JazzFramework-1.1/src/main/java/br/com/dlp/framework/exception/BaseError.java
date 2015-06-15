/*
 * Created on 31/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.exception;

/**
 * Exceção runtime básica do framework
 * 
 * @author dpacifico
 * 
 */
public class BaseError extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8803953025909007798L;
	private String messageKey;
	
	public BaseError(final String message) {
		super(message);
	}
	
	public BaseError(final String message, final String messageKey) {
		super(message);
		this.setMessageKey(messageKey);
	}
	
	public BaseError(final String message, final Throwable throwable) {
		super(message, throwable);
	}
	
	public BaseError(final String message, final Throwable throwable, final String messageKey) {
		super(message, throwable);
		this.setMessageKey(messageKey);
	}
	
	public BaseError(final Throwable throwable) {
		super(throwable);
	}
	
	public BaseError(final Throwable throwable, final String messageKey) {
		super(throwable);
		this.setMessageKey(messageKey);
	}
	
	public final String getMessageKey() {
		return this.messageKey;
	}
	
	public final void setMessageKey(final String messageKey) {
		this.messageKey = messageKey;
	}
}
