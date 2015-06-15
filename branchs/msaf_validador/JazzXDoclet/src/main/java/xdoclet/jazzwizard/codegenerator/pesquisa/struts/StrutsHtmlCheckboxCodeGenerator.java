/*
 * Data de Criacao 05/06/2005 20:49:28
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.pesquisa.struts;

import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;

/**
 * Gera uma chamada à tag html:text do struts
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:49:28
 */
public class StrutsHtmlCheckboxCodeGenerator
		extends
		xdoclet.jazzwizard.codegenerator.cadastro.struts.StrutsHtmlCheckboxCodeGenerator {

	public String getProperty() throws Wj2eeException {
		String property = super.getProperty();

		String voWrapperProperty = TagHandlerUtil
				.primeiraLetraMinuscula(TagHandlerUtil
						.voWrapperClassName(class1));

		return voWrapperProperty + "." + property;
	}
}
