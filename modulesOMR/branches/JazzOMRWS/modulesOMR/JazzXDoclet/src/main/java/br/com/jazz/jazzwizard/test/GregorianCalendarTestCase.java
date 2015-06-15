/*
 * Created on 30/07/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.jazz.jazzwizard.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Darcio
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class GregorianCalendarTestCase {
	
	public static void main(final String[] args) {
		
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			final Date date = sdf.parse("20/11/2005");
			
			final GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTime(date);
			
			final Date date2 = new GregorianCalendar(gregorianCalendar.get(Calendar.YEAR), gregorianCalendar.get(Calendar.MONTH),
					gregorianCalendar.get(Calendar.DAY_OF_MONTH)).getTime();
			
			System.out.println(sdf.format(date));
			System.out.println(sdf.format(date2));
			
		} catch (final ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
