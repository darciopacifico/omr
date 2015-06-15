package br.com.dlp.framework.exception;

import java.util.ArrayList;
import java.util.Collection;

public class BaseBusinessException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5325682946373890249L;

	private Collection baseBusinessExceptions = new ArrayList(0);

	public BaseBusinessException() {
		super("Esta exceção é um composite de outras Exceções ");
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
		super(message, messageKey);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 * @param messageKey
	 */
	public BaseBusinessException(String message, Throwable throwable,
			String messageKey) {
		super(message, throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 * @param messageKey
	 */
	public BaseBusinessException(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

	public Collection getBaseBusinessExceptions() {
		return baseBusinessExceptions;
	}

	public void setBaseBusinessExceptions(Collection baseBusinessExceptions) {
		this.baseBusinessExceptions = baseBusinessExceptions;
	}

	public void addBaseBusinessException(
			BaseBusinessException baseBusinessException) {
		getBaseBusinessExceptions().add(baseBusinessException);
	}

}
