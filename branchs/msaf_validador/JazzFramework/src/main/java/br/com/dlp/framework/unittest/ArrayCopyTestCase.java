/*
 * Created on 31/07/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.unittest;

/**
 * @author Darcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ArrayCopyTestCase {

	public static void main(String[] args) {

		String objects[] = new String[2];

		String newObjects[] = new String[3];

		String realLabel = "Darsdasdcio";

		objects[0] = "Lopes";
		objects[1] = "Pacifico";

		newObjects[0] = realLabel;
		for (int i = 0; i < objects.length; i++) {
			newObjects[i + 1] = objects[i];
		}

		for (int i = 0; i < newObjects.length; i++) {
			System.out.println(newObjects[i]);
		}

	}
}
