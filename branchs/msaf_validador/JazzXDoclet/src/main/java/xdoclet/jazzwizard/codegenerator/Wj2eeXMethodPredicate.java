/*
 * Data de Criacao 11/06/2005 10:42:58
 * 
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator;

import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;
import xjavadoc.XMethod;
import xjavadoc.XTag;

/**
 * Filtra apenas XMethods getters com a tag wj2ee
 * 
 * @author Darcio L Pacifico - 11/06/2005 10:42:58
 */
public class Wj2eeXMethodPredicate implements Predicate {
	protected Log logger = LogFactory.getLog(this.getClass());

	/** Override do metodo evaluate da superclasse */
	public boolean evaluate(Object arg0) {

		try {

			if (!(arg0 instanceof XMethod) || arg0 == null) {
				return false;
			}

			XMethod method = (XMethod) arg0;

			if (!method.isPropertyAccessor()) {
				return false;
			}

			XTag tag = method.getDoc().getTag(TagHandlerUtil.W2JEE_NAMESPACE);

			/* testa se a tag não é nula (se o método possui a tag wj2ee) */
			boolean returnValue = (tag != null);

			return returnValue;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}