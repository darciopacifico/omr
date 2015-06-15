package br.com.dlp.framework.util.converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * IMPLEMENTAcao DE Converter PARA UTILIZAR O BeanUtilsBean
 */
public class DateParaStringConverter extends AbstractConverter {

	public DateParaStringConverter(ResourceBundle resourceBundle) {
		super(resourceBundle);
	}

	public Object convert(Class classToSetter, Object objectFromGetter) {
		Object returnObj = objectFromGetter;

		if (objectFromGetter instanceof Date && classToSetter.getName().equals(String.class.getName())) {

			String formato = getResourceBundle().getString(PROPERTY_FORMATO_DATA_VIEW);

			SimpleDateFormat sdf = new SimpleDateFormat(formato);
			returnObj = sdf.format(objectFromGetter);
		}

		return returnObj;
	}

}
