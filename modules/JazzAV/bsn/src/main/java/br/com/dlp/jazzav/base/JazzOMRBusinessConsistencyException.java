/**
 * 
 */
package br.com.dlp.jazzav.base;

import javax.ejb.ApplicationException;

import br.com.dlp.framework.exception.JazzBusinessException;

/**
 * Exceção de consistências de negócio do sistema JazzOMR. 
 * 
 * Normalmente lançada quando uma entidade que já foi utilizada não pode ser alterada para não deixar o estado do banco de dados inconsistente com o negócio.
 * 
 * @author darcio
 */
@ApplicationException(rollback=true)
public class JazzOMRBusinessConsistencyException extends JazzBusinessException {

	private static final long serialVersionUID = 6815026952758243015L;

	/**
	 * 
	 */
	public JazzOMRBusinessConsistencyException() {
	}

	/**
	 * @param message
	 */
	public JazzOMRBusinessConsistencyException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param messageKey
	 */
	public JazzOMRBusinessConsistencyException(String message, String messageKey) {
		super(message, messageKey);
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public JazzOMRBusinessConsistencyException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * @param message
	 * @param throwable
	 * @param messageKey
	 */
	public JazzOMRBusinessConsistencyException(String message, Throwable throwable, String messageKey) {
		super(message, throwable, messageKey);
	}

	/**
	 * @param throwable
	 */
	public JazzOMRBusinessConsistencyException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * @param throwable
	 * @param messageKey
	 */
	public JazzOMRBusinessConsistencyException(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
	}

}
