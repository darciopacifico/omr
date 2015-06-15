/*
 * Data de Criacao 05/06/2005 20:49:28
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.pesquisa.struts;

import xdoclet.XDocletException;
import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;

/**
 * Gera uma chamada à tag html:text do struts
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:49:28
 */
public class StrutsHtmlSelectCodeGenerator
		extends
		xdoclet.jazzwizard.codegenerator.cadastro.struts.StrutsHtmlSelectCodeGenerator {

	/** Override do metodo getTag da superclasse */
	public String getTag() {
		return "html:select";
	}

	/**
	 * Override do metodo generateCode da superclasse
	 * 
	 * @throws Wj2eeException
	 * @throws XDocletException
	 */
	public String generateCode() throws Wj2eeException {
		String propertyName = method.getPropertyName();

		propertyName += "." + getPrimaryKey(method);

		setProperty(propertyName);

		/* NÃO EXISTE readonly num SELECT */
		getDefaultAttributes().remove(
				StrutsHtmlSelectCodeGenerator.ATT_READONLY);

		return super.generateCode();
	}

	protected String optionPadrao() throws Wj2eeException {
		return " <html:option value=\"-1\">"
				+ "		<bean:message key=\"pesquisa.padrao.labelTodos\"/>"
				+ " </html:option>";

	}

	public String getProperty() throws Wj2eeException {
		String property = super.getProperty();

		String voWrapperProperty = TagHandlerUtil
				.primeiraLetraMinuscula(TagHandlerUtil
						.voWrapperClassName(class1));

		return voWrapperProperty + "." + property;
	}
}