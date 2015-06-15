/*
 * Created on 04/07/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.unittest;

/**
 * @author PLDarcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class SplitTesteCase {

	public static void main(String[] args) {

		String string[] = "enderecos,statusVO,sdfsdf".split("[/,]{1}");

		System.out.println(string);

	}
}
