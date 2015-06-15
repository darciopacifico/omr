/**
 * 
 */
package br.com.dlp.framework.gof.strategy;

import br.com.dlp.jazzomr.exceptions.JazzOMRException;

/**
 * @author darcio
 *
 */
public class StrategyExecutionException extends JazzOMRException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1938717923141186677L;

	/**
	 * 
	 */
	public StrategyExecutionException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public StrategyExecutionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}


	
	/**
	 * @param message
	 * @param throwable
	 */
	public StrategyExecutionException(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public StrategyExecutionException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}
}
