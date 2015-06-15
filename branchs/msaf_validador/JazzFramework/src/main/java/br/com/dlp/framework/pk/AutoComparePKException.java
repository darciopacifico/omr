package br.com.dlp.framework.pk;

import br.com.dlp.framework.exception.BaseTechnicalError;

public class AutoComparePKException extends BaseTechnicalError {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5760086295410685379L;

	public AutoComparePKException(String message, String messageKey) {
		super(message, messageKey);
		// TODO Auto-generated constructor stub
	}

	public AutoComparePKException(String message, Throwable throwable,
			String messageKey) {
		super(message, throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

	public AutoComparePKException(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
	}

	public AutoComparePKException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AutoComparePKException(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

	public AutoComparePKException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

}
