/**
 * 
 */
package com.msaf.validador.consultaonline.exceptions;


/**
 * Erro de negócio ligado ao mecanismo de tarifacao do sistema validador
 * 
 * @author dlopes
 *
 */
public class TarifacaoException extends ValidadorException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2113996421910295049L;

	/**
	 * 
	 */
	public TarifacaoException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public TarifacaoException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public TarifacaoException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public TarifacaoException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
