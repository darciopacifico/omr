package com.msaf.validador.tipoValidacao.regra.exception;

import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;

public class InterruptedPesquisaException extends ValidadorRuntimeException {

	private static final long serialVersionUID = 1L;

	public InterruptedPesquisaException() {
	}

	public InterruptedPesquisaException(String message, Throwable cause) {
		super(message, cause);
	}

	public InterruptedPesquisaException(String message) {
		super(message);
	}

	public InterruptedPesquisaException(Throwable cause) {
		super(cause);
	}	
	
}
