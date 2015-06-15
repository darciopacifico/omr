/*
 * Created on 06/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author darcio
 */
public final class UtilFormatter {
	protected Log logger = LogFactory.getLog(UtilFormatter.class);

	private UtilFormatter() {
	}

	public static Long toLong(String string) {
		Long longg = null;

		if (string == null) {
			return null;
		}

		try {
			longg = Long.valueOf(string);
		} catch (Exception e) {
		} finally {
		}

		return longg;
	}

	public static Integer toInteger(String string) {
		Integer integer = null;

		if (string == null) {
			return null;
		}

		try {
			integer = Integer.valueOf(string);
		} catch (Exception e) {
		} finally {
		}

		return integer;
	}

	public static Date toDate(String strDate, String formato)
			throws UtilFormatterException {
		Date returnValue;

		SimpleDateFormat sdf = new SimpleDateFormat(formato);

		try {
			returnValue = sdf.parse(strDate);
		} catch (ParseException e) {
			throw new UtilFormatterException("Erro ao tentar parsear a string "
					+ strDate + " para java.util.Date utilizando o formato "
					+ formato, e);
		}

		return returnValue;
	}

}
