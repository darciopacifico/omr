/*
 * Created on 28/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package xdoclet.jazzwizard.codegenerator;

import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.tagvalue.AtributosMetodoVO;
import xjavadoc.XMethod;

public class MetodosPesquisaveisPredicate extends Wj2eeXMethodPredicate {
	public boolean evaluate(Object method) {

		boolean returnValue = super.evaluate(method);

		if (returnValue) {
			try {
				AtributosMetodoVO atributosMetodoVO = new AtributosMetodoVO(
						(XMethod) method);

				String strPesquisavel = atributosMetodoVO.getPesquisavel();
				boolean pesquisavel = Boolean.valueOf(strPesquisavel)
						.booleanValue();
				if (!pesquisavel) {
					if (logger.isDebugEnabled()) {
						logger.debug("O metodo " + ((XMethod) method).getName()
								+ " !!NAO!! e pesquisavel!");
					}
					returnValue = false;
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("O metodo " + ((XMethod) method).getName()
								+ " e pesquisavel!");
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
