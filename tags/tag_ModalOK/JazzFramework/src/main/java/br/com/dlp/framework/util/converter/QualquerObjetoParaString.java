/*
 * Created on 29/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.util.converter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author PLDarcio
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class QualquerObjetoParaString extends AbstractConverter {

	public QualquerObjetoParaString(ResourceBundle resourceBundle) {
		super(resourceBundle);
	}

	private String getFormatoPadrao(Object objetoOrigem) {
		String retorno = "";

		ResourceBundle resourceBundle = getResourceBundle();

		if (retorno != null) {

			if (objetoOrigem instanceof String) {
				retorno = "";

			} else if (objetoOrigem instanceof Short) {
				retorno = resourceBundle.getString(PROPERTY_FORMATO_SHORT);

			} else if (objetoOrigem instanceof Integer) {
				retorno = resourceBundle.getString(PROPERTY_FORMATO_INTEGER);

			} else if (objetoOrigem instanceof Long) {
				retorno = resourceBundle.getString(PROPERTY_FORMATO_LONG);

			} else if (objetoOrigem instanceof Float) {
				retorno = resourceBundle.getString(PROPERTY_FORMATO_FLOAT);

			} else if (objetoOrigem instanceof Double) {
				retorno = resourceBundle.getString(PROPERTY_FORMATO_DOUBLE);

			}
		}
		return retorno;
	}

	public Object convert(Class arg0, Object valorOrigem) {

		try {
			String returnValue = null;

			if (valorOrigem instanceof String) {
				returnValue = (String) valorOrigem;

			} else if (valorOrigem instanceof Date) {
				returnValue = (String) (new DateParaStringConverter(getResourceBundle()).convert(arg0, valorOrigem));

			} else if (valorOrigem instanceof Number) {

				DecimalFormatSymbols decimalFormatSymbols = getDecimalFormatSymbols();

				DecimalFormat decimalFormat = new DecimalFormat(getFormatoPadrao(valorOrigem), decimalFormatSymbols);

				if (valorOrigem instanceof Short) {
					returnValue = decimalFormat.format(((Short) valorOrigem).shortValue());

				} else if (valorOrigem instanceof Integer) {
					returnValue = decimalFormat.format(((Integer) valorOrigem).intValue());

				} else if (valorOrigem instanceof Long) {
					returnValue = decimalFormat.format(((Long) valorOrigem).longValue());

				} else if (valorOrigem instanceof Float) {
					returnValue = decimalFormat.format(((Float) valorOrigem).floatValue());

				} else if (valorOrigem instanceof Double) {
					returnValue = decimalFormat.format(((Double) valorOrigem).doubleValue());

				}
			}
			return returnValue;

		} catch (Exception e) {
			logger.error("Erro ao tentar converter o objeto '" + valorOrigem + "'!", e);
			e.printStackTrace();
			throw new RuntimeException("Erro ao tentar converter o objeto '" + valorOrigem + "'!", e);

		}
	}

}