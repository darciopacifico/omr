/**
 * 
 */
package br.com.jazz.codegen.exception;

import br.com.jazz.jazzwizard.exception.GeradorException;

/**
 * Denota que um artefato que iria ser criado pelo gerador de código já existe e a sua sobrescrita não é permitida. Verifique a regra de
 * geração de código do template e da entidade
 * 
 * @author darcio
 * @since 09/Jun/2009
 * 
 */
public class OverwriteNotAllowedException extends GeradorException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2151528788690753842L;
	
	/**
	 * @param arg0
	 */
	public OverwriteNotAllowedException(final String arg0) {
		super(arg0);
	}
	
	/**
	 * @param arg0
	 * @param arg1
	 */
	public OverwriteNotAllowedException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}
	
	/**
	 * @param arg0
	 */
	public OverwriteNotAllowedException(final Throwable arg0) {
		super(arg0);
	}
	
}
