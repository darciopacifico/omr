/**
 * 
 */
package br.com.dlp.framework.gof.strategy;

import br.com.dlp.framework.exception.JazzBusinessException;

/**
 * @author darcio
 *
 */
public class StrategyExecutionException extends JazzBusinessException {

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
	 * @param messageKey
	 */
	public StrategyExecutionException(String message, String messageKey) {
		super(message, messageKey);
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
	 * @param message
	 * @param throwable
	 * @param messageKey
	 */
	public StrategyExecutionException(String message, Throwable throwable, String messageKey) {
		super(message, throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public StrategyExecutionException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 * @param messageKey
	 */
	public StrategyExecutionException(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

}
