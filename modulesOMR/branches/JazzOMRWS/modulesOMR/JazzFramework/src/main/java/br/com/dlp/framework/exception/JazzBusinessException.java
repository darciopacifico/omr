package br.com.dlp.framework.exception;


import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.ApplicationException;

/**
 * Exceção de negócio básica
 * 
 * @author dpacifico
 * 
 */
@ApplicationException(rollback=true)
public class JazzBusinessException extends Exception {
	
	private static final long serialVersionUID = -5325682946373890249L;
	
	private Collection<JazzBusinessException> baseBusinessExceptions = new ArrayList<JazzBusinessException>(0);
	
	private String messageKey;
	
	public JazzBusinessException() {
		super("Esta excecao e um composite de outras excecoes ");
	}
	
	public JazzBusinessException(final String message) {
		super(message);
	}
	
	/**
	 * @param message
	 * @param messageKey
	 */
	public JazzBusinessException(final String message, final String messageKey) {
		super(message);
		setMessageKey(messageKey);
	}
	
	public JazzBusinessException(final String message, final Throwable throwable) {
		super(message, throwable);
	}
	
	/**
	 * @param message
	 * @param throwable
	 * @param messageKey
	 */
	public JazzBusinessException(final String message, final Throwable throwable, final String messageKey) {
		super(message, throwable);
		setMessageKey(messageKey);
		// TODO Auto-generated constructor stub
	}
	
	public JazzBusinessException(final Throwable throwable) {
		super(throwable);
	}
	
	/**
	 * @param throwable
	 * @param messageKey
	 */
	public JazzBusinessException(final Throwable throwable, final String messageKey) {
		super(throwable);
		setMessageKey(messageKey);
		
	}
	
	public void addBaseBusinessException(final JazzBusinessException baseBusinessException) {
		getBaseBusinessExceptions().add(baseBusinessException);
	}
	
	public Collection<JazzBusinessException> getBaseBusinessExceptions() {
		return baseBusinessExceptions;
	}
	
	public final String getMessageKey() {
		return messageKey;
	}
	
	public void setBaseBusinessExceptions(final Collection<JazzBusinessException> baseBusinessExceptions) {
		this.baseBusinessExceptions = baseBusinessExceptions;
	}
	
	public final void setMessageKey(final String messageKey) {
		this.messageKey = messageKey;
	}
	
}
