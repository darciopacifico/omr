/*
 * Created on 08/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.struts.form;

import br.com.dlp.framework.exception.BaseTechnicalError;

/**
 * @author PLDarcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ActionFormException extends BaseTechnicalError {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4067659687753526615L;

	/**
	 * @param message
	 */
	public ActionFormException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public ActionFormException(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public ActionFormException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}
}
