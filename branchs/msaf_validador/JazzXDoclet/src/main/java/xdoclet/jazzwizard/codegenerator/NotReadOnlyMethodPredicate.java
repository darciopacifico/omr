/*
 * Created on 27/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package xdoclet.jazzwizard.codegenerator;

import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.tagvalue.AtributosMetodoVO;
import xjavadoc.XMethod;

/**
 * @author Darcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NotReadOnlyMethodPredicate extends Wj2eeXMethodPredicate {

	public boolean evaluate(Object method) {

		boolean returnValue = super.evaluate(method);

		if (returnValue) {
			try {
				AtributosMetodoVO atributosMetodoVO = new AtributosMetodoVO(
						(XMethod) method);

				String strSomenteLeitura = atributosMetodoVO
						.getSomenteLeitura();
				boolean somenteLeitura = Boolean.valueOf(strSomenteLeitura)
						.booleanValue();
				if (somenteLeitura) {
					if (logger.isDebugEnabled()) {
						logger.debug("O metodo " + ((XMethod) method).getName()
								+ " e somente leitura!");
					}
					returnValue = false;
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("O metodo " + ((XMethod) method).getName()
								+ " !!NAO!! e somente leitura!");
					}
				}

			} catch (Wj2eeException e) {
				logger
						.error("Erro ao tentar avaliar o metodo " + method
								+ "!!");
				e.printStackTrace();
			}
		}

		return returnValue;
	}
}
