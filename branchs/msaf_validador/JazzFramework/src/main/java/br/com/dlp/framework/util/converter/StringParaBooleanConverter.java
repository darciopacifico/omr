package br.com.dlp.framework.util.converter;

import org.apache.commons.beanutils.Converter;

public class StringParaBooleanConverter implements Converter {

	public Object convert(Class converterPara, Object valorOriginal) {

		String returnValue = valorOriginal + "";

		return Boolean.valueOf(returnValue);
	}

}
