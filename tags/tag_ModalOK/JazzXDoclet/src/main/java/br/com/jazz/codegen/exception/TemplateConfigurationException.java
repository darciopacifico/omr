/**
 * 
 */
package br.com.jazz.codegen.exception;

import br.com.jazz.jazzwizard.exception.GeradorException;

/**
 * Erro na utilização de um ITemplateRulesFactory
 * @author darcio
 *
 */
public class TemplateConfigurationException extends GeradorException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5500985575514422173L;

	/**
	 * 
	 */
	public TemplateConfigurationException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public TemplateConfigurationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public TemplateConfigurationException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public TemplateConfigurationException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
