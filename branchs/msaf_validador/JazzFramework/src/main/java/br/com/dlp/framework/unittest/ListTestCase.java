/*
 * Created on 18/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.unittest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pldarcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ListTestCase {

	public static void main(String[] args) {

		List list = new ArrayList();

		list.add("a");// 0
		list.add("b");// 1
		list.add("c");// 2
		list.add("d");// 3
		list.add("e");// 4
		list.add("f");// 5
		list.add("g");// 6
		list.add("h");// 7

		List subList = new ArrayList(list.subList(1, 5));

		System.out.println(subList);

	}
}
