/*
 * Data de Criacao 10/06/2005 14:43:49
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package br.com.jazz.jazzwizard.test;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Sr(a). Darcio L Pacifico, comente aqui a utilidade desta classe.
 * 
 * @author Darcio L Pacifico - 10/06/2005 14:43:49
 * 
 * 
 */
public class SetTestCase {

	public static void main(String[] args) {
		Set set = new LinkedHashSet();

		set.add("b");
		set.add("b");
		set.add("b");
		set.add("b");
		set.add("b");
		set.add("a");
		set.add("aa");
		set.add("d");
		set.add("e");
		set.add("f");

		set.remove("aa");

		set.add("aa");
		set.add("aa");
		set.add("aa");

		Iterator iterator = set.iterator();
		System.out.println("LISTA ATUAL=========>");
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}

	}
}
