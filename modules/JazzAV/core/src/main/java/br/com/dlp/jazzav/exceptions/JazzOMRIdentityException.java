/**
 * 
 */
package br.com.dlp.jazzav.exceptions;

/**
 * Erro ao tentar identificar a identificacao da imagem
 * @author darcio
 */
public class JazzOMRIdentityException extends JazzOMRException {

	/**
	 * @param message
	 */
	public JazzOMRIdentityException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public JazzOMRIdentityException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public JazzOMRIdentityException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
