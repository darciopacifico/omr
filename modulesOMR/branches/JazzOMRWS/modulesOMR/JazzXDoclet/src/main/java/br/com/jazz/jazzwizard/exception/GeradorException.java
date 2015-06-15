package br.com.jazz.jazzwizard.exception;

/**
 * Erro geral na geração de códigos
 * 
 * @author darcio
 * 
 */
public class GeradorException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1710918657963756140L;
	
	public GeradorException() {
		super();
	}
	
	public GeradorException(final String arg0) {
		super(arg0);
	}
	
	public GeradorException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}
	
	public GeradorException(final Throwable arg0) {
		super(arg0);
	}
	
}
