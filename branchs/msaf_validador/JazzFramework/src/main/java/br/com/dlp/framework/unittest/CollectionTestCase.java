/*
 * Created on 28/07/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.unittest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author PLDarcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class CollectionTestCase {

	public static void main(String[] args) {

		Collection collection = new ArrayList();

		collection.add("1");
		collection.add("2");
		collection.add("3");
		collection.add("4");
		collection.add("5");
		collection.add("6");
		collection.add("7");
		collection.add("8");
		collection.add("9");

		Iterator iterator = collection.iterator();

		while (iterator.hasNext()) {

			System.out.println(iterator.next());

		}

	}
}
