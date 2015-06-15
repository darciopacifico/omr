/*
 * Created on 08/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.struts.action;

import br.com.dlp.framework.exception.BaseTechnicalError;

/**
 * @author PLDarcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ActionException extends BaseTechnicalError {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2151809455709175069L;

	/**
	 * @param message
	 */
	public ActionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public ActionException(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public ActionException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}
}
