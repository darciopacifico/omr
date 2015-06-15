package br.com.dlp.framework.util.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class StringParaDataConverter extends AbstractConverter {

	public StringParaDataConverter(ResourceBundle resourceBundle) {
		super(resourceBundle);
	}

	public Object convert(Class converterPara, Object valorOriginal) {

		String formatoData = getResourceBundle().getString(
				PROPERTY_FORMATO_DATA_VIEW);

		try {

			SimpleDateFormat sdf = new SimpleDateFormat(formatoData);

			Date date = sdf.parse(valorOriginal + "");

			return date;
		} catch (ParseException e) {
			logger.warn("Não foi possível converter o valor=" + valorOriginal
					+ " para Data");
			return null;
		}
	}

}