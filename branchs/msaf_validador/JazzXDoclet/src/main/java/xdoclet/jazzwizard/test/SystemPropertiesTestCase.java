/*
 * Created on 11/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package xdoclet.jazzwizard.test;

/**
 * @author PLDarcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class SystemPropertiesTestCase {

	public static void main(String[] args) {

		String tempDir = System.getProperty("java.io.tmpdir");
		System.out.println(tempDir);

	}
}