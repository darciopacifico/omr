package com.msaf.validador.pesquisa.exception;

public class TimeOutException extends Exception {

	private static final long serialVersionUID = 1959790316292210381L;

	public TimeOutException() {
		super();
	}
	
	public TimeOutException(String message) {
		super(message);
	}
	
	public TimeOutException(Throwable t) {
		super(t);
	}
}
