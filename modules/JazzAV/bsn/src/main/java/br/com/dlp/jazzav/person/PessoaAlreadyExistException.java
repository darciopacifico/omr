package br.com.dlp.jazzav.person;

import br.com.dlp.framework.exception.JazzBusinessException;

/**
 * 
 * @author darcio
 *
 */
public class PessoaAlreadyExistException extends JazzBusinessException {

	private static final long serialVersionUID = -1688054827571526260L;

	public PessoaAlreadyExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PessoaAlreadyExistException(String message, String messageKey) {
		super(message, messageKey);
		// TODO Auto-generated constructor stub
	}

	public PessoaAlreadyExistException(String message, Throwable throwable, String messageKey) {
		super(message, throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

	public PessoaAlreadyExistException(String message, Throwable throwable) {
		super(message, throwable);
		// TODO Auto-generated constructor stub
	}

	public PessoaAlreadyExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PessoaAlreadyExistException(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
		// TODO Auto-generated constructor stub
	}

	public PessoaAlreadyExistException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

}
