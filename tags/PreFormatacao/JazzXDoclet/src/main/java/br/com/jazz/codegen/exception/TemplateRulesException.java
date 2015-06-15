/**
 * 
 */
package br.com.jazz.codegen.exception;

import br.com.jazz.jazzwizard.exception.GeradorException;

/**
 * Erro ao tentar processar um template
 * @author darcio
 *
 */
public class TemplateRulesException extends GeradorException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3406180227731104424L;

	/**
	 * 
	 */
	public TemplateRulesException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public TemplateRulesException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public TemplateRulesException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public TemplateRulesException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
