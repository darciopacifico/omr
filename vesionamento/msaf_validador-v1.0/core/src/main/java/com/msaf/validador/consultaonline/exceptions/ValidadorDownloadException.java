/**
 * 
 */
package com.msaf.validador.consultaonline.exceptions;

/**
 * Erro ao tentar executar o download do arquivo
 * 
 * Ex: Pedido de validacao não completado, pedido de validacao inválido etc...
 * @author dlopes
 *
 */
public class ValidadorDownloadException extends ValidadorException {

	public ValidadorDownloadException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ValidadorDownloadException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ValidadorDownloadException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ValidadorDownloadException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
