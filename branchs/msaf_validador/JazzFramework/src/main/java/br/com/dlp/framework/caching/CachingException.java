/*
 * Created on 29/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.caching;

import br.com.dlp.framework.exception.BaseTechnicalError;

/**
 * @author PLDarcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class CachingException extends BaseTechnicalError {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public CachingException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param messageKey
	 */
	public CachingException(String message, String messageKey) {
		super(message, messageKey);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public CachingException(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 * @param messageKey
	 */
	public CachingException(String message, Throwable throwable,
			String messageKey) {
		super(message, throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public CachingException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 * @param messageKey
	 */
	public CachingException(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
		// TODO Auto-generated constructor stub
	}
}
