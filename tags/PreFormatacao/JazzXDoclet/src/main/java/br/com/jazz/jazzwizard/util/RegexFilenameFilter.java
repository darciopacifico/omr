/**
 * 
 */
package br.com.jazz.jazzwizard.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Implementação de FilenameFilter para filtragem por regular expression 
 * @author darcio
 * 
 */
public class RegexFilenameFilter implements FilenameFilter {
	
	private String regex;
	
	/**
	 * Recebe a regex para filtro
	 * @param regex
	 */
	public RegexFilenameFilter(String regex) {
		super();
		this.regex = regex;
	}


	/**
	 * Implementação de accept utilizando regex informado no construtor
	 */
	public boolean accept(File dir, String name) {
		return name.matches(regex);
	}
}
