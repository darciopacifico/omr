/*
 * Data de Criacao 05/06/2005 21:08:55
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator;

import xdoclet.jazzwizard.Wj2eeException;

/**
 * É lancado toda vez que ocorre uma exceção no contrado de ICodeGenerador
 * 
 * @author Darcio L Pacifico - 05/06/2005 21:08:55
 */
public class CodeGeneratorException extends Wj2eeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8333475354944432867L;

	/**
	 * Apenas repassa os parâmetros deste construtor para o construtor da
	 * superclasse
	 */
	public CodeGeneratorException(Exception arg0, String arg1) {
		super(arg0, arg1);
	}

	/**
	 * Apenas repassa os parâmetros deste construtor para o construtor da
	 * superclasse
	 */
	public CodeGeneratorException(String arg0) {
		super(arg0);
	}
}
