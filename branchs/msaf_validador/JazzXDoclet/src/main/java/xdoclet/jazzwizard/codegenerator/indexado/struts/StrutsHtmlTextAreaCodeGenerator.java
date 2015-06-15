/*
 * Data de Criacao 05/06/2005 20:49:28
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.indexado.struts;

import java.util.Iterator;
import java.util.Set;

import xdoclet.jazzwizard.Wj2eeException;

/**
 * Gera uma chamada à tag html:text do struts
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:49:28
 */
public class StrutsHtmlTextAreaCodeGenerator
		extends
		xdoclet.jazzwizard.codegenerator.cadastro.struts.StrutsHtmlTextAreaCodeGenerator {

	public String generateAttributes() throws Wj2eeException {
		getDefaultAttributes().remove(ATT_SIZE);
		getDefaultAttributes().remove(ATT_DISABLED);
		getDefaultAttributes().remove(ATT_READONLY);
		getDefaultAttributes().add(ATT_COLS);
		getDefaultAttributes().add(ATT_ROWS);
		setRows("3");

		String sequenceOfAttributes = "";
		Set defaultAttributes = getDefaultAttributes();

		logger
				.debug("ITERANDO TODOS OS  ATRIBUTOS SELECIONADAS PARA ESTA TAG. VIDE defaultAttributes");
		Iterator iterator = defaultAttributes.iterator();

		while (iterator.hasNext()) {

			String attributeName = (String) iterator.next();
			logger.debug("PROCESSANDO O ATRIBUTO " + attributeName);

			String completeAttribute = generateAttribute(attributeName);
			sequenceOfAttributes += completeAttribute;

		}

		return sequenceOfAttributes;
	}

	protected void appendSpecialControl(StringBuffer stringBuffer)
			throws Wj2eeException {
	}

}
