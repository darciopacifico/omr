/*
 * Created on 03/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.dao;

import br.com.dlp.framework.exception.BaseTechnicalError;

/**
 * @author PLDarcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DAOFactoryException extends BaseTechnicalError {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DAOFactoryException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DAOFactoryException(Throwable throwable) {
		super(throwable);
	}

	public DAOFactoryException(String message) {
		super(message);
	}

}
