/*
 * Data de Criacao 11/06/2005 12:29:15
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.test;

import xjavadoc.XClass;
import xjavadoc.XJavaDoc;


/**
 * Sr(a). Darcio L Pacifico comente aqui a utilidade desta classe.
 * 
 * @author Darcio L Pacifico - 11/06/2005 12:29:15
 */
public class TestGetXClass {

	public static void main(String[] args) {

		XJavaDoc javaDoc = new XJavaDoc();

		XClass class1 = javaDoc
				.getXClass("xdoclet.jazzwizard.tagvalue.AtributosClasseVO");
		System.out.println(class1.getMethods().size());

		class1 = javaDoc
				.getXClass("xdoclet/jazzwizard/tagvalue/AtributosClasseVO");
		System.out.println(class1.getMethods().size());

		class1 = javaDoc
				.getXClass("xdoclet.jazzwizard.tagvalue.AtributosClasseVO.class");
		System.out.println(class1.getMethods().size());

		class1 = javaDoc
				.getXClass("xdoclet.jazzwizard.tagvalue.AtributosClasseVO.java");
		System.out.println(class1.getMethods().size());

		class1 = javaDoc
				.getXClass("src/xdoclet/jazzwizard/tagvalue/AtributosClasseVO.java");
		System.out.println(class1.getMethods().size());

		class1 = javaDoc
				.getXClass("xdoclet.jazzwizard.tagvalue.AtributosClasseVO");
		System.out.println(class1.getMethods().size());

	}
}
