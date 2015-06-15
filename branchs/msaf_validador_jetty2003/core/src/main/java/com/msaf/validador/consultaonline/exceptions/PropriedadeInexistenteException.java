package com.msaf.validador.consultaonline.exceptions;


public class PropriedadeInexistenteException extends RuntimeException {
	private static final long serialVersionUID = 5914945631097075073L;

	public PropriedadeInexistenteException() {
		super();
	}

	public PropriedadeInexistenteException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropriedadeInexistenteException(String message) {
		super(message);
	}

	public PropriedadeInexistenteException(Throwable cause) {
		super(cause);
	}
}
