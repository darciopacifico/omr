package com.msaf.validador.pesquisa.exception;

import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;

public class ValidadorRuntimeRMIException extends ValidadorRuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidadorRuntimeRMIException(String message){
		super(message);
	}
	
	public ValidadorRuntimeRMIException(Throwable e){
		super(e);
	}
}
