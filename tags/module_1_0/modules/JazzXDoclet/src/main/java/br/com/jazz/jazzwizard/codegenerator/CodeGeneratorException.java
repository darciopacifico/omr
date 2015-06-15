/*
 * Data de Criacao 05/06/2005 21:08:55
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package br.com.jazz.jazzwizard.codegenerator;

import br.com.jazz.jazzwizard.Wj2eeException;

/**
 * e lancado toda vez que ocorre uma excecao no contrado de ICodeGenerador
 * 
 * @author Darcio L Pacifico - 05/06/2005 21:08:55
 */
public class CodeGeneratorException extends Wj2eeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8333475354944432867L;
	
	/**
	 * Apenas repassa os parAmetros deste construtor para o construtor da superclasse
	 */
	public CodeGeneratorException(final Exception arg0, final String arg1) {
		super(arg0, arg1);
	}
	
	/**
	 * Apenas repassa os parAmetros deste construtor para o construtor da superclasse
	 */
	public CodeGeneratorException(final String arg0) {
		super(arg0);
	}
}
