/*
 * Criado em 21/06/2004
 *
 */
package br.com.dlp.framework.dao;

import br.com.dlp.framework.exception.BaseTechnicalError;

/**
 * Exception que será utilizada em todos os DAO, para tratamento de erros dos
 * métodos do DAO
 * 
 */
public class DAOException extends BaseTechnicalError {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DAOException(String mensagem) {
		super(mensagem);
	}

	public DAOException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DAOException(Throwable throwable) {
		super(throwable);
	}
}