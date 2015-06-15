/**
 * 
 */
package br.com.dlp.jazzav.exceptions;

/**
 * Erro ao tentar parsear imagens
 * @author darcio
 *
 */
public class JazzOMRParseException extends JazzOMRException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1691582081087752655L;

	/**
	 * 
	 */
	public JazzOMRParseException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public JazzOMRParseException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public JazzOMRParseException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public JazzOMRParseException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
