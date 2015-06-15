package br.com.dlp.framework.util.converter;

import org.apache.commons.beanutils.Converter;

public class BooleanParaStringConverterxx implements Converter {

	public Object convert(Class converterPara, Object valorOriginal) {
		Object returnValue = valorOriginal;

		if (valorOriginal != null && converterPara.getName().equals(Boolean.class.getName())) {
			try {

				returnValue = Boolean.valueOf("" + valorOriginal);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
				
		return returnValue;

	}
}
