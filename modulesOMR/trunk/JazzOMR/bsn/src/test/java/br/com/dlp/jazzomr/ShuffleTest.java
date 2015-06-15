package br.com.dlp.jazzomr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ShuffleTest {

	public static void main(String[] args) {
		
		List<Date> dates = new ArrayList<Date>();
		
		dates.add(new Date(1980,11,20));
		dates.add(new Date(2001,9,11));
		dates.add(new Date());
		dates.add(new Date(1945,06,14));

		
		
		Collections.shuffle(dates);
		printDates(dates);
		
		
		Collections.shuffle(dates);
		printDates(dates);
		
		Collections.shuffle(dates);
		printDates(dates);
		
		Collections.shuffle(dates);
		printDates(dates);
		
		
	}

	/**
	 * @param dates
	 */
	protected static void printDates(List<Date> dates) {
		for (Date date : dates) {
			System.out.println(date);
		}
		
		System.out.println();
		System.out.println();
	}

}
