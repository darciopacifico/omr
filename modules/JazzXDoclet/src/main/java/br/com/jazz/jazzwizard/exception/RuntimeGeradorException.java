package br.com.jazz.jazzwizard.exception;

/**
 * Erro geral na geração de códigos
 * 
 * @author darcio
 * 
 */
public class RuntimeGeradorException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1710918657963756140L;
	
	public RuntimeGeradorException() {
		super();
	}
	
	public RuntimeGeradorException(final String arg0) {
		super(arg0);
	}
	
	public RuntimeGeradorException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}
	
	public RuntimeGeradorException(final Throwable arg0) {
		super(arg0);
	}
	
}
