/*
 * Created on 10/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package xdoclet.jazzwizard.codegenerator.predicate;

import org.apache.commons.collections.Predicate;

import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;
import xdoclet.jazzwizard.tagvalue.AtributosClasseVO;
import xdoclet.jazzwizard.tagvalue.AtributosMetodoVO;
import xjavadoc.XMethod;
import xjavadoc.XTag;

/**
 * @author PLDarcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class MetodosModuloEscravo implements Predicate {

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
						String renderer = atributosMetodoVO.getRenderer();

						returnValue = renderer
								.equals(AtributosClasseVO.RENDERER_MODULOESCRAVO)
								|| renderer
										.equals(AtributosClasseVO.RENDERER_INDEXADO);

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
