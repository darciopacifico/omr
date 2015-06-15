package br.com.dlp.framework.servicelocator;

import br.com.dlp.framework.exception.BaseTechnicalError;

public class ServiceLocatorException extends BaseTechnicalError {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7659933191197692469L;

	public ServiceLocatorException(String message) {
		super(message);
	}

	public ServiceLocatorException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ServiceLocatorException(Throwable throwable) {
		super(throwable);
	}
}