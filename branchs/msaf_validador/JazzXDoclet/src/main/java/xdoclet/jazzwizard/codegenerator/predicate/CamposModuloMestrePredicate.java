/*
 * Data de Criacao 11/06/2005 10:42:58
 * 
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.predicate;

import org.apache.commons.collections.Predicate;

import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;
import xdoclet.jazzwizard.tagvalue.AtributosMetodoVO;
import xjavadoc.XMethod;
import xjavadoc.XTag;

/**
 * Filtra apenas XMethods getters com a tag wj2ee que deverão aparecer na
 * listagem dos VOs escravos do modulo mestre Ex: <br>
 * Campos da listagem de itens de pedido do modulo mestre
 * 
 * @author Darcio L Pacifico - 11/06/2005 10:42:58
 */
public class CamposModuloMestrePredicate implements Predicate {

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

					if (tag != null) {
						AtributosMetodoVO atributosMetodoVO = new AtributosMetodoVO(
								method);
						String listavel = atributosMetodoVO.getListavel();

						returnValue = Boolean.valueOf(listavel).booleanValue();

					} else {
						returnValue = false;
					}
				}
			}

			return returnValue;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}