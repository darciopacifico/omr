package br.com.jazz.jazzwizard.exception;

/**
 * Erro geral na geração de códigos
 * @author darcio
 *
 */
public class GeradorException extends Exception {

	public GeradorException() {
		super();
	}

	public GeradorException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public GeradorException(String arg0) {
		super(arg0);
	}

	public GeradorException(Throwable arg0) {
		super(arg0);
	}

}
