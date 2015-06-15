package br.com.dlp.jazzav.person;

import br.com.dlp.framework.exception.JazzBusinessException;

public class EmailNotFoundException extends JazzBusinessException {

	public EmailNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public EmailNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public EmailNotFoundException(String message, String messageKey) {
		super(message, messageKey);
		// TODO Auto-generated constructor stub
	}

	public EmailNotFoundException(String message, Throwable throwable) {
		super(message, throwable); 
		// TODO Auto-generated constructor stub
	}

	public EmailNotFoundException(String message, Throwable throwable, String messageKey) {
		super(message, throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

	public EmailNotFoundException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	public EmailNotFoundException(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

}
