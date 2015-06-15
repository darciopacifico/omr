/*
 * Created on 10/07/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.popup;

import java.util.Date;

import br.com.dlp.framework.vo.IBaseVO;

/**
 * Abstrai um argumento de pesquisa para PopUp<br>
 * Será agrupado numa Collection e renderizado de forma indexada com Struts
 */
public class PopUpArgument implements IBaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3353093276512162154L;

	public static final String VALIDACAO_FORMATACAO_DATA = "validacaoFormacacaoData";

	public static final String VALIDACAO_FORMATACAO_DECIMAL = "validacaoFormacacaoDecimal";

	public static final String VALIDACAO_FORMATACAO_INTEIRO = "validacaoFormacacaoInteiro";

	public static final String VALIDACAO_FORMATACAO_STRING = "validacaoFormacacaoString";

	private Class argumentType;

	private String value;

	/* INTERNACIONALIZADO */
	private String messageKey;

	private String titleKey;

	/* NAO INTERNACIONALIZADO */
	private String message;

	private String title;

	/* ...COM ISSO!!!!! */
	/*
	 * VALOR A SER UTILIZADO CASO NENHUM VALOR SEJA ESPECIFICADO PARA O
	 * ARGUMENTO DE PESQUISA
	 */
	private String defaultValue;

	public PopUpArgument(Class argumentType, String defaultValue, String value) {
		setArgumentType(argumentType);
		setDefaultValue(defaultValue);
		setValue(value);
	}

	/* CONSTRUTORES COM INTERNACIONALIZAÇÃO */
	public PopUpArgument(Class argumentType, String defaultValue, String value,
			String messageKey, String titleKey) {
		this(argumentType, defaultValue, value);
		setMessageKey(messageKey);
		setTitleKey(titleKey);
	}

	public PopUpArgument(Class argumentType, String defaultValue, String value,
			String messageKey) {
		this(argumentType, defaultValue, value, messageKey, "");
	}

	/* CONSTRUTORES SEM INTERNACIONALIZAÇÃO */
	public PopUpArgument(String message, String title, Class argumentType,
			String defaultValue, String value) {
		this(argumentType, defaultValue, value);
		setMessage(message);
		setTitle(title);
	}

	public PopUpArgument(String message, Class argumentType,
			String defaultValue, String value) {
		this(message, "", argumentType, defaultValue, value);
	}

	/**
	 * @return Returns the titleKey.
	 */
	public String getTitleKey() {
		return titleKey;
	}

	/**
	 * @param titleKey
	 *            The titleKey to set.
	 */
	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	/**
	 * @return Returns the messageKey.
	 */
	public String getMessageKey() {
		return messageKey;
	}

	/**
	 * @param messageKey
	 *            The messageKey to set.
	 */
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	/**
	 * @return Returns the argumentType.
	 */
	public Class getArgumentType() {
		return argumentType;
	}

	/**
	 * @param argumentType
	 *            The argumentType to set.
	 */
	public void setArgumentType(Class argumentType) {
		this.argumentType = argumentType;
	}

	/**
	 * @return Returns the value.
	 */
	public String getValue() {

		if (value == null || value.trim().equals("")) {
			value = getDefaultValue();
		}

		return value;
	}

	/**
	 * @param value
	 *            The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return Returns the defaultValue.
	 */
	protected String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue
	 *            The defaultValue to set.
	 */
	protected void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            The message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getValidacaoFormacacao() {
		Class argumentType = getArgumentType();

		String returnValue;

		if (Double.class.isAssignableFrom(argumentType)) {
			returnValue = VALIDACAO_FORMATACAO_DECIMAL;

		} else if (Float.class.isAssignableFrom(argumentType)) {
			returnValue = VALIDACAO_FORMATACAO_DECIMAL;

		} else if (Number.class.isAssignableFrom(argumentType)) {
			returnValue = VALIDACAO_FORMATACAO_INTEIRO;

		} else if (String.class.isAssignableFrom(argumentType)) {
			returnValue = VALIDACAO_FORMATACAO_STRING;

		} else if (Date.class.isAssignableFrom(argumentType)) {
			returnValue = VALIDACAO_FORMATACAO_DATA;

		} else {
			returnValue = VALIDACAO_FORMATACAO_STRING;

		}

		return returnValue;
	}
}
