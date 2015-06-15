/*
 * Data de Criacao 03/06/2005 08:54:54
 *
 * Propriedade intelectual de Aché Laboratórios Farmacêuticos (pldarcio)
 */
package br.com.dlp.framework.util;

import br.com.dlp.framework.exception.BaseTechnicalError;

/**
 * Exceção lançada para erros de formatação
 * 
 * @author pldarcio - 03/06/2005 08:54:54
 */
public class UtilFormatterException extends BaseTechnicalError {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4595016841639160580L;

	/**
	 * Repassa os parâmetros deste construtor ao construtor da superclasse, nada
	 * de mais
	 */
	public UtilFormatterException(String message) {
		super(message);
	}

	/**
	 * Repassa os parâmetros deste construtor ao construtor da superclasse, nada
	 * de mais
	 */
	public UtilFormatterException(String message, String messageKey) {
		super(message, messageKey);
	}

	/**
	 * Repassa os parâmetros deste construtor ao construtor da superclasse, nada
	 * de mais
	 */
	public UtilFormatterException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Repassa os parâmetros deste construtor ao construtor da superclasse, nada
	 * de mais
	 */
	public UtilFormatterException(String message, Throwable throwable,
			String messageKey) {
		super(message, throwable, messageKey);
	}

	/**
	 * Repassa os parâmetros deste construtor ao construtor da superclasse, nada
	 * de mais
	 */
	public UtilFormatterException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Repassa os parâmetros deste construtor ao construtor da superclasse, nada
	 * de mais
	 */
	public UtilFormatterException(Throwable throwable, String messageKey) {
		super(throwable, messageKey);
	}
}
