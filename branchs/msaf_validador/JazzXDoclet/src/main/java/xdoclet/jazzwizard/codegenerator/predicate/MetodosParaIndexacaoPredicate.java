/*
 * Data de Criacao 11/06/2005 10:42:58
 * 
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.predicate;

import org.apache.commons.collections.Predicate;

import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;
import xjavadoc.XMethod;
import xjavadoc.XTag;

/**
 * Filtra apenas XMethods getters com a tag wj2ee que deverão aparecer no grid
 * indexado
 * 
 * @author Darcio L Pacifico - 11/06/2005 10:42:58
 */
public class MetodosParaIndexacaoPredicate implements Predicate {

	/** Override do metodo evaluate da superclasse */
	public boolean evaluate(Object arg0) {
		boolean returnValue;

		try {
			/* testa se posso fazer o cast com seguranca */
			if (arg0 == null || !(arg0 instanceof XMethod)) {
				returnValue = false;
			} else {

				XMethod method = (XMethod) arg0;

				if (!method.isPropertyAccessor()) {
					returnValue = false;
				} else {
					XTag tag = method.getDoc().getTag(
							TagHandlerUtil.W2JEE_NAMESPACE);
					/*
					 * testa se a tag não é nula (se o método possui a tag
					 * wj2ee)
					 */
					returnValue = (tag != null);
				}
			}

			return returnValue;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}