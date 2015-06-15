/**
 * 
 */
package br.com.dlp.framework.gof.strategy;

import br.com.dlp.framework.exception.JazzBusinessException;

/**
 * Erro do componente generico Strategy (GoF)
 * 
 * @author darcio
 *
 */
public class StrategyNotFoundException extends JazzBusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1341065444253367376L;

	/**
	 * @param message
	 */
	public StrategyNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public StrategyNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public StrategyNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
