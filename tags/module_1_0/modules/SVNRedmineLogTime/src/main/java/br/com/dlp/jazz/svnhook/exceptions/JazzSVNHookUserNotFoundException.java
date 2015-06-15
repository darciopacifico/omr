/**
 * 
 */
package br.com.dlp.jazz.svnhook.exceptions;

/**
 * Usuario nao encontrado no redmine
 * @author t_dpacifico
 */
public class JazzSVNHookUserNotFoundException extends JazzSVNHookException {

	private static final long serialVersionUID = 2723494966503180673L;

	/**
	 * Construtor padrao 
	 */
	public JazzSVNHookUserNotFoundException() {
		//explicitamente vazio
	}

	/**
	 * @param arg0
	 */
	public JazzSVNHookUserNotFoundException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public JazzSVNHookUserNotFoundException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public JazzSVNHookUserNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
