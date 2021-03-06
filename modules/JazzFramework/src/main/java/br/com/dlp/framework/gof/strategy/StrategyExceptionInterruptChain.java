/**
 * 
 */
package br.com.dlp.framework.gof.strategy;

/**
 * @author darcio
 *
 */
public class StrategyExceptionInterruptChain extends StrategyExecutionException {

	/**
	 * 
	 */
	public StrategyExceptionInterruptChain() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public StrategyExceptionInterruptChain(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param messageKey
	 */
	public StrategyExceptionInterruptChain(String message, String messageKey) {
		super(message, messageKey);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public StrategyExceptionInterruptChain(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 * @param messageKey
	 */
	public StrategyExceptionInterruptChain(String message, Throwable throwable, String messageKey) {
		super(message, throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public StrategyExceptionInterruptChain(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 * @param messageKey
	 */
	public StrategyExceptionInterruptChain(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

}
