/**
 * 
 */
package br.com.jazz.jazzwizard.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Implementação de FilenameFilter para filtragem por regular expression
 * 
 * @author darcio
 * 
 */
public class RegexFilenameFilter implements FilenameFilter {
	
	private final String regex;
	
	/**
	 * Recebe a regex para filtro
	 * 
	 * @param regex
	 */
	public RegexFilenameFilter(final String regex) {
		super();
		this.regex = regex;
	}
	
	/**
	 * Implementação de accept utilizando regex informado no construtor
	 */
	public boolean accept(final File dir, final String name) {
		return name.matches(this.regex);
	}
}
