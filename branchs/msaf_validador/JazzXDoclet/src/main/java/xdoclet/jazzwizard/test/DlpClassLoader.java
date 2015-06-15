/*
 * Created on 16/06/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package xdoclet.jazzwizard.test;

import sun.reflect.Reflection;

/**
 * @author PLDarcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DlpClassLoader {
	public static void main(String args[]) {

		class Teste {
			public void teste() {
				Class class1 = Reflection.getCallerClass(1);
				System.out.println(class1.getName());
			}
		}

		new Teste().teste();
	}
}
