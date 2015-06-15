/**
 * 
 */
package br.com.dlp.jazzomr.jr.util;

import java.util.ArrayList;

/**
 * Dummy collection for JasperReports default value in collections parameters
 * @author darcio
 */
public class CollectionDefaulParameter extends ArrayList {

	private static final long serialVersionUID = 4547705166086481199L;

	public static void main(String[] args) {
		System.out.println(
		new br.com.dlp.jazzomr.jr.util.CollectionDefaulParameter(1l,2l,3l)
		); 
	}
	
	/**
	 * Construtor padrao
	 * @param objects
	 */
	public CollectionDefaulParameter(long l1, long l2, long l3 ) {
		add(l1);
		add(l2);
		add(l3);
	}
	
}
