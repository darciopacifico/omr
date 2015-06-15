package br.com.dlp.jazz.svnhook.exceptions;

/**
 * Excecao runtime do projeto
 * @author t_dpacifico
 *
 */
public class JazzSVNHookRTException extends RuntimeException {

	private static final long serialVersionUID = 4908167493234506466L;

	
	/**
	 * construtor padrao
	 */
	public JazzSVNHookRTException() {
		//explicitamente vazio
	}

	/**
	 * 
	 * @param arg0
	 */
	public JazzSVNHookRTException(String arg0) {
		super(arg0);
	}

	/**
	 * 
	 * @param arg0
	 */
	public JazzSVNHookRTException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public JazzSVNHookRTException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
