package br.com.dlp.framework.exception;

public class LockException extends BaseTechnicalError {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6056771317936021691L;

	public LockException(String message, String messageKey) {
		super(message, messageKey);
		// TODO Auto-generated constructor stub
	}

	public LockException(String message, Throwable throwable, String messageKey) {
		super(message, throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

	public LockException(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
	}

	public LockException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public LockException(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

	public LockException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

}
