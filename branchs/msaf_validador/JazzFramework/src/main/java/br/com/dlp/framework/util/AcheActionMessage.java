package br.com.dlp.framework.util;

import java.util.ResourceBundle;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

public class AcheActionMessage extends ActionMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9060075042701462904L;

	/**
	 * Descobre o valor de label key e incrementa como primeiro item do array de
	 * objetos
	 */
	private static Object[] descobreLabelKey(String labelKey, Object objects[],
			MessageResources messageResources, ResourceBundle resourceBundle) {

		String[] strings = transformaEmString(objects, resourceBundle);

		String realLabel = messageResources.getMessage(labelKey);
		Object[] returnObjects = new Object[strings.length + 1];
		returnObjects[0] = realLabel;
		for (int i = 0; i < strings.length; i++) {
			returnObjects[i + 1] = strings[i];
		}

		return returnObjects;

	}

	/**
	 * Converte um array quaisquer objetos para um array de Strings de acordo
	 * com o padrao internacionalizado de conversao do framework (Utilizando os
	 * converters)
	 */
	public static String[] transformaEmString(Object[] objects,
			ResourceBundle resourceBundle) {
		String[] strings = new String[objects.length];

		ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
		FrameworkAcheUtil.configureConvertUtilsBean(convertUtilsBean,
				resourceBundle);

		for (int i = 0; i < objects.length; i++) {
			strings[i] = convertUtilsBean.convert(objects[i]);
		}

		return strings;
	}

	/**
	 * Descobre labelKey
	 */
	private static Object descobreLabelKey(String labelKey,
			MessageResources messageResources) {
		String realLabel = messageResources.getMessage(labelKey);
		return realLabel;
	}

	public AcheActionMessage(String messageKey, String labelKey,
			MessageResources messageResources) {
		super(messageKey, descobreLabelKey(labelKey, messageResources));
	}

	public AcheActionMessage(String messageKey, String labelKey,
			Object objects[], MessageResources messageResources,
			ResourceBundle resourceBundle) {
		super(messageKey, descobreLabelKey(labelKey, objects, messageResources,
				resourceBundle));
	}

	public AcheActionMessage(String messageKey, String labelKey, Object object,
			MessageResources messageResources, ResourceBundle resourceBundle) {
		super(messageKey, descobreLabelKey(labelKey, new Object[] { object },
				messageResources, resourceBundle));
	}

	public AcheActionMessage(String messageKey, String labelKey,
			Object object1, Object object2, MessageResources messageResources,
			ResourceBundle resourceBundle) {
		super(messageKey, descobreLabelKey(labelKey, new Object[] { object1,
				object2 }, messageResources, resourceBundle));
	}

	public AcheActionMessage(String messageKey, String labelKey,
			Object object1, Object object2, Object object3,
			MessageResources messageResources, ResourceBundle resourceBundle) {
		super(messageKey, descobreLabelKey(labelKey, new Object[] { object1,
				object2, object3 }, messageResources, resourceBundle));
	}

}
