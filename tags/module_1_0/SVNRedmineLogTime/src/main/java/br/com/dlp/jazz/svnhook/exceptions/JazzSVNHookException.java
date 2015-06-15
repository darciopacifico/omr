package br.com.dlp.jazz.svnhook.exceptions;

/**
 * Exceção de negócio principal do projeto
 * @author t_dpacifico
 *
 */
public class JazzSVNHookException extends Exception {

	private static final long serialVersionUID = 7796697100855879229L;

	/**
	 * construtor padrao
	 */
	public JazzSVNHookException() {
		//explicity empty
	}

	/**
	 * 
	 * @param arg0
	 */
	public JazzSVNHookException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public JazzSVNHookException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public JazzSVNHookException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
