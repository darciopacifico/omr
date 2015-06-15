/*
 * Data de Criacao 26/05/2005 16:14:31
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.test;

/**
 * Sr(a). Darcio L Pacifico, comente aqui a utilidade desta classe.
 * 
 * @author Darcio L Pacifico - 26/05/2005 16:14:31
 */
public class Test {
	public static void main(String[] args) {
		String returnValue = "Zuba.Lele";

		returnValue = returnValue.replaceAll("[.]{1}", "/");

		System.out.println(returnValue);
	}
}
