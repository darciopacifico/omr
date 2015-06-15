/*
 * Created on 28/07/2005
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
public class ReplaceTestCase {

	public static void main(String[] args) {

		String vitima = ", asdasd, adfsdfsdf, sdfsdfs ,sdfsdf";

		vitima = vitima.replaceFirst("[,]", "");

		System.out.println("|" + vitima);

	}
}
