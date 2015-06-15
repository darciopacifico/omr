/*
 * Data de Criacao 11/06/2005 14:40:28
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.test;

import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;

/**
 * Sr(a). Darcio L Pacifico comente aqui a utilidade desta classe.
 * 
 * @author Darcio L Pacifico - 11/06/2005 14:40:28
 */
public class TesteSingularPlural {

	public static void main(String[] args) {

		String toSingular = TagHandlerUtil.toSingular("darciorrsssssrrrssss");
		System.out.println(toSingular);

		String toPlural = TagHandlerUtil.toPlural("darciossssssrrr");
		System.out.println(toPlural);

	}
}
