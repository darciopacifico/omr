package br.com.dlp.framework.exception;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Exceção de negócio básica
 * 
 * @author dpacifico
 * 
 */
public class BaseBusinessException extends Exception {
	
	private static final long serialVersionUID = -5325682946373890249L;
	
	private Collection<BaseBusinessException> baseBusinessExceptions = new ArrayList<BaseBusinessException>(0);
	
	private String messageKey;
	
	public BaseBusinessException() {
		super("Esta excecao e um composite de outras excecoes ");
	}
	
	public BaseBusinessException(final String message) {
		super(message);
	}
	
	/**
	 * @param message
	 * @param messageKey
	 */
	public BaseBusinessException(final String message, final String messageKey) {
		super(message);
		setMessageKey(messageKey);
	}
	
	public BaseBusinessException(final String message, final Throwable throwable) {
		super(message, throwable);
	}
	
	/**
	 * @param message
	 * @param throwable
	 * @param messageKey
	 */
	public BaseBusinessException(final String message, final Throwable throwable, final String messageKey) {
		super(message, throwable);
		setMessageKey(messageKey);
		// TODO Auto-generated constructor stub
	}
	
	public BaseBusinessException(final Throwable throwable) {
		super(throwable);
	}
	
	/**
	 * @param throwable
	 * @param messageKey
	 */
	public BaseBusinessException(final Throwable throwable, final String messageKey) {
		super(throwable);
		setMessageKey(messageKey);
		
	}
	
	public void addBaseBusinessException(final BaseBusinessException baseBusinessException) {
		getBaseBusinessExceptions().add(baseBusinessException);
	}
	
	public Collection<BaseBusinessException> getBaseBusinessExceptions() {
		return baseBusinessExceptions;
	}
	
	public final String getMessageKey() {
		return messageKey;
	}
	
	public void setBaseBusinessExceptions(final Collection<BaseBusinessException> baseBusinessExceptions) {
		this.baseBusinessExceptions = baseBusinessExceptions;
	}
	
	public final void setMessageKey(final String messageKey) {
		this.messageKey = messageKey;
	}
	
}
