/*
 * Created on 18/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author PLDarcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DataAche extends Date {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5010891090939887559L;

	private static String FORMATO_DATA = "d/MM/yyyy";

	public DataAche(String data) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_DATA);
		sdf.setLenient(false);

		Date date = sdf.parse(data);
		setTime(date.getTime());
	}

	public String toString() {
		String retorno = null;

		SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_DATA);
		retorno = sdf.format(this);

		return retorno;
	}

}
