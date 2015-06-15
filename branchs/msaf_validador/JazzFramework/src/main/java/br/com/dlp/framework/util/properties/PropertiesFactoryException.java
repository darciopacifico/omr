/*
 * Created on 19/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.util.properties;

import br.com.dlp.framework.exception.BaseTechnicalError;

/**
 * @author darcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PropertiesFactoryException extends BaseTechnicalError {

	/**
	 * 
	 */
	private static final long serialVersionUID = -615270063332708430L;

	/**
	 * @param message
	 */
	public PropertiesFactoryException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public PropertiesFactoryException(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public PropertiesFactoryException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}
}
