package br.com.dlp.framework.exception;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Exceção de negócio básica
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

	public BaseBusinessException(String message) {
		super(message);
	}

	public BaseBusinessException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public BaseBusinessException(Throwable throwable) {
		super(throwable);
	}

	
	
	
	/**
	 * @param message
	 * @param messageKey
	 */
	public BaseBusinessException(String message, String messageKey) {
		super(message);
		setMessageKey(messageKey);
	}

	/**
	 * @param message
	 * @param throwable
	 * @param messageKey
	 */
	public BaseBusinessException(String message, Throwable throwable, String messageKey) {
		super(message, throwable);
		setMessageKey(messageKey);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 * @param messageKey
	 */
	public BaseBusinessException(Throwable throwable, String messageKey) {
		super(throwable);
		setMessageKey(messageKey);
		
	}

	public Collection<BaseBusinessException> getBaseBusinessExceptions() {
		return baseBusinessExceptions;
	}

	public void setBaseBusinessExceptions(Collection<BaseBusinessException> baseBusinessExceptions) {
		this.baseBusinessExceptions = baseBusinessExceptions;
	}

	public void addBaseBusinessException(BaseBusinessException baseBusinessException) {
		getBaseBusinessExceptions().add(baseBusinessException);
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getMessageKey() {
		return messageKey;
	}

}
