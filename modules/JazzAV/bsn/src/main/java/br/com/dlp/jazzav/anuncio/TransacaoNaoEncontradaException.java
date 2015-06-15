/**
 * 
 */
package br.com.dlp.jazzav.anuncio;

import br.com.dlp.framework.exception.JazzBusinessException;

/**
 * @author darcio
 *
 */
public class TransacaoNaoEncontradaException extends JazzBusinessException {

	/**
	 * 
	 */
	public TransacaoNaoEncontradaException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public TransacaoNaoEncontradaException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param messageKey
	 */
	public TransacaoNaoEncontradaException(String message, String messageKey) {
		super(message, messageKey);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public TransacaoNaoEncontradaException(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param throwable
	 * @param messageKey
	 */
	public TransacaoNaoEncontradaException(String message, Throwable throwable, String messageKey) {
		super(message, throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public TransacaoNaoEncontradaException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 * @param messageKey
	 */
	public TransacaoNaoEncontradaException(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

}
