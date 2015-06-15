/*
 * Data de Criacao 27/05/2005 23:19:56
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.tagvalue;

import xdoclet.jazzwizard.Wj2eeException;

/**
 * Sr(a). Darcio L Pacifico, comente aqui a utilidade desta classe.
 * 
 * @author Darcio L Pacifico - 27/05/2005 23:19:56
 * 
 * 
 */
public class AtributosException extends Wj2eeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1394407272642789776L;

	/**
	 * Apenas repassa os parâmetros deste construtor para o construtor da
	 * superclasse
	 */
	public AtributosException(Exception arg0, String arg1) {
		super(arg0, arg1);
	}

	/**
	 * Apenas repassa os parâmetros deste construtor para o construtor da
	 * superclasse
	 */
	public AtributosException(String arg0) {
		super(arg0);
	}
}
