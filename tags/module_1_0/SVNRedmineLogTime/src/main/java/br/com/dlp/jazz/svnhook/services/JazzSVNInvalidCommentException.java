/**
 * 
 */
package br.com.dlp.jazz.svnhook.services;

import br.com.dlp.jazz.svnhook.exceptions.JazzSVNHookException;

/**
 * Comentário de commit inválido
 * @author t_dpacifico
 */
public class JazzSVNInvalidCommentException extends JazzSVNHookException {

	private static final long serialVersionUID = 160504436498664524L;

	/**
	 * Construtor padrao
	 */
	public JazzSVNInvalidCommentException() {
		//explicity empty
	}

	/**
	 * @param arg0
	 */
	public JazzSVNInvalidCommentException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public JazzSVNInvalidCommentException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public JazzSVNInvalidCommentException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
