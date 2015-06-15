/*
 * Data de Criacao 11/06/2005 10:36:02
 * 
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator;

import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.tagvalue.AtributosMetodoVO;
import xjavadoc.XMethod;

/**
 * Comparator para XMethod.<br>
 * Compara (ordena) de acordo com o atributo 'ordem' da tag wj2ee
 * 
 * @author Darcio L Pacifico - 11/06/2005 10:36:02
 */
public class Wj2eeXMethodComparator implements Comparator {
	protected Log logger = LogFactory.getLog(Wj2eeXMethodComparator.class);

	public int compare(Object method1, Object method2) {

		if (method1 == null || method2 == null) {
			return 0;
		}

		XMethod xMethod1 = (XMethod) method1;
		XMethod xMethod2 = (XMethod) method2;

		if (!(xMethod1.isPropertyAccessor() || xMethod1.isPropertyMutator())
				|| !(xMethod2.isPropertyAccessor() || xMethod2
						.isPropertyMutator())) {
			return 0;
		}

		try {

			AtributosMetodoVO atributosMetodoVO1 = new AtributosMetodoVO(
					xMethod1);
			AtributosMetodoVO atributosMetodoVO2 = new AtributosMetodoVO(
					xMethod2);

			int ordem1 = Integer.parseInt(atributosMetodoVO1.getOrdem());
			int ordem2 = Integer.parseInt(atributosMetodoVO2.getOrdem());

			return ordem1 - ordem2;

		} catch (Wj2eeException e) {
			logger
					.error("Erro ao tentar construir objetos AtributosMetodoVO. "
							+ "Este comparator não pode lancar exceções! Retornarei zero!!! ");
			return 0;

		}
	}
}