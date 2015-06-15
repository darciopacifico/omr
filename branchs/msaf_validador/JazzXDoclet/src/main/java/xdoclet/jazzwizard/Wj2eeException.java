/*
 * Data de Criacao 27/05/2005 22:47:37
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard;

import br.com.dlp.framework.exception.BaseTechnicalError;

/**
 * Sr(a). Darcio L Pacifico, comente aqui a utilidade desta classe.
 * 
 * @author Darcio L Pacifico - 27/05/2005 22:47:37
 * 
 * 
 */

public class Wj2eeException extends BaseTechnicalError {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2346345623434563456L;

	/**
	 * Apenas repassa os parâmetros deste construtor para o construtor da
	 * superclasse
	 */
	public Wj2eeException(Exception exception, String message) {
		super(exception, message);
	}

	/**
	 * Apenas repassa os parâmetros deste construtor para o construtor da
	 * superclasse
	 */
	public Wj2eeException(String message) {
		super(message);
	}
}
