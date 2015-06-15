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
public class StrutsHtmlRadioCodeGenerator
		extends
		xdoclet.jazzwizard.codegenerator.cadastro.struts.StrutsHtmlRadioCodeGenerator {

	public String generateCode() throws Wj2eeException {
		StringBuffer generatedCode = new StringBuffer(super.generateCode());

		String property = TagHandlerUtil.primeiraLetraMinuscula(TagHandlerUtil
				.voWrapperClassName(class1));
		property += "." + TagHandlerUtil.nomeAtributoParaPesquisa(method, "");

		generatedCode.append("\n\t\t\t<html:radio property='" + property
				+ "' value='-1'>");
		generatedCode
				.append("\n\t\t\t\t<bean:message key='pesquisa.padrao.labelTodos'/>");
		generatedCode.append("\n\t\t\t</html:radio>");

		return new String(generatedCode);
	}

	public String getProperty() throws Wj2eeException {
		String property = super.getProperty();

		String voWrapperProperty = TagHandlerUtil
				.primeiraLetraMinuscula(TagHandlerUtil
						.voWrapperClassName(class1));

		return voWrapperProperty + "." + property;
	}
}
