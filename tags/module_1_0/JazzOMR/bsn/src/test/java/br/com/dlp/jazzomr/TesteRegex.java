package br.com.dlp.jazzomr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TesteRegex {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String OMR_MARK_REGEX_KEY = "OMRMark-(.+)-(.+)";
		
		Pattern omr_mark_pattern = Pattern.compile(OMR_MARK_REGEX_KEY);
		
		Matcher matcher = omr_mark_pattern.matcher("OMRMark-top-right");
		
		System.out.println(matcher.matches());
		
	}

}
