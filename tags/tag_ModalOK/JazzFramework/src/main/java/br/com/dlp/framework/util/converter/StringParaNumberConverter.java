/*
 * Created on 29/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.util.converter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ResourceBundle;

/**
 * @author PLDarcio
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class StringParaNumberConverter extends AbstractConverter {

	/**
	 * Constroi um StringParaNumberConverter baseado em configuracoes internacionalizadas
	 */
	public StringParaNumberConverter(ResourceBundle resourceBundle) {
		super(resourceBundle);
	}

	public Object convert(Class classeDoSetter, Object objetoOrigem) {
		if (!(objetoOrigem instanceof String)) {
			return objetoOrigem;
		}

		/*
		 * recupera o padrao de casas decimais e separadores de milhar do sistema, independentemente das configuracoes regionais do SO
		 */
		DecimalFormatSymbols decimalFormatSymbols = getDecimalFormatSymbols();

		Object returnValue = objetoOrigem;
		String valorDeOrigem = (String) objetoOrigem;

		try {
			if (valorDeOrigem.trim().equals("")) {
				returnValue = null;

			} else if (classeDoSetter.getName().equals(Short.class.getName())) {
				Number number = getNumber(valorDeOrigem, PROPERTY_FORMATO_SHORT, decimalFormatSymbols);
				returnValue = new Short(number.shortValue());

			} else if (classeDoSetter.getName().equals(Integer.class.getName())) {
				Number number = getNumber(valorDeOrigem, PROPERTY_FORMATO_INTEGER, decimalFormatSymbols);
				returnValue = new Integer(number.intValue());

			} else if (classeDoSetter.getName().equals(Long.class.getName())) {
				Number number = getNumber(valorDeOrigem, PROPERTY_FORMATO_LONG, decimalFormatSymbols);
				returnValue = new Long(number.longValue());

			} else if (classeDoSetter.getName().equals(Float.class.getName())) {
				Number number = getNumber(valorDeOrigem, PROPERTY_FORMATO_FLOAT, decimalFormatSymbols);
				returnValue = new Float(number.floatValue());

			} else if (classeDoSetter.getName().equals(Double.class.getName())) {
				Number number = getNumber(valorDeOrigem, PROPERTY_FORMATO_DOUBLE, decimalFormatSymbols);
				returnValue = new Double(number.doubleValue());

			}

		} catch (ParseException e) {
			returnValue = null;
			logger.error("Nao FOI POSSIVEL CONVERTER  DO VALOR " + objetoOrigem + " PARA O TIPO " + classeDoSetter.getName());
		}

		return returnValue;
	}

	/**
	 */
	protected Number getNumber(String valorDeOrigem, String propertyFormato, DecimalFormatSymbols decimalFormatSymbols) throws ParseException {
		String formato = getResourceBundle().getString(propertyFormato);
		DecimalFormat decimalFormat = new DecimalFormat(formato, decimalFormatSymbols);
		Number number = decimalFormat.parse(valorDeOrigem);
		return number;
	}

}
