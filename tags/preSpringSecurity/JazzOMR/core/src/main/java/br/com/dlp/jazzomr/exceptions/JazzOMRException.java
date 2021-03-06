/**
 * 
 */
package br.com.dlp.jazzomr.exceptions;

import javax.ejb.ApplicationException;

import br.com.dlp.framework.exception.JazzBusinessException;

/**
 * @author darcio
 *
 */
@ApplicationException(rollback=true)
public class JazzOMRException extends JazzBusinessException {

	private static final long serialVersionUID = -6957202431029948157L;

	public JazzOMRException() {
	}
	
	/**
	 * @param message
	 */
	public JazzOMRException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public JazzOMRException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public JazzOMRException(String message, Throwable cause) {
		super(message, cause);
	}

}
