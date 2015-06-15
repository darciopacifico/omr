/*
 * Data de Criacao 27/05/2005 22:47:37
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package br.com.jazz.jazzwizard;

import br.com.dlp.framework.exception.BaseError;

/**
 * Sr(a). Darcio L Pacifico, comente aqui a utilidade desta classe.
 * 
 * @author Darcio L Pacifico - 27/05/2005 22:47:37
 * 
 * 
 */

public class Wj2eeException extends BaseError {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2346345623434563456L;
	
	/**
	 * Apenas repassa os parametros deste construtor para o construtor da superclasse
	 */
	public Wj2eeException(final Exception exception, final String message) {
		super(exception, message);
	}
	
	/**
	 * Apenas repassa os parametros deste construtor para o construtor da superclasse
	 */
	public Wj2eeException(final String message) {
		super(message);
	}
}
