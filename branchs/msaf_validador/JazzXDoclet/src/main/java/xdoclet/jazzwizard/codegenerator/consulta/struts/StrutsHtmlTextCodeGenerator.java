/*
 * Data de Criacao 05/06/2005 20:49:28
 * 
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.consulta.struts;

import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.codegenerator.base.AbstractStrutsCodeGenerator;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;

/**
 * Gera uma chamada à tag html:text do struts
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:49:28
 */
public class StrutsHtmlTextCodeGenerator extends AbstractStrutsCodeGenerator {

	/** Override do metodo getTag da superclasse */
	public String getTag() {
		return "bean:write";
	}

	/**
	 * bean:write nao possui disabled
	 */
	public String evaluateDisabled() throws Wj2eeException {
		return "";
	}

	/**
	 * Esta classe gera apenas bear:write, e este não pode possuir o atributo
	 * indexed
	 */
	public String evaluateIndexed() throws Wj2eeException {
		return "";
	}

	/**
	 * Preparo os atributos necessários para esta implementacao de
	 * ICodeGenerator
	 */
	public String generateAttributes() throws Wj2eeException {
		return generateAttributesReadOnly();
	}

	/**
	 * Preparo os atributos necessários para esta implementacao de
	 * ICodeGenerator
	 */
	public String generateAttributesReadOnly() throws Wj2eeException {
		// getDefaultAttributes().removeAll(getDefaultAttributes());
		getDefaultAttributes().add(ATT_NAME);
		getDefaultAttributes().add(ATT_PROPERTY);

		getDefaultAttributes().remove(ATT_STYLECLASS);
		getDefaultAttributes().remove(ATT_ALTKEY);
		getDefaultAttributes().remove(ATT_TITLEKEY);
		getDefaultAttributes().remove(ATT_READONLY);

		return super.generateAttributes();
	}

	public String getProperty() throws Wj2eeException {

		String tipoControle = getTipoControle();

		String property = super.getProperty();

		if (tipoControle.equals(TagHandlerUtil.TIPO_CONTROLE_CONSULTA)) {
			property = getPropertyConsulta();

		} else if (tipoControle.equals(TagHandlerUtil.TIPO_CONTROLE_INDEXADO)) {
			property = getPropertyIndexado();

		}

		return property;
	}

	protected String getPropertyIndexado() throws Wj2eeException {
		String propertyVO = TagHandlerUtil
				.denifirVariavelParaTipo(TagHandlerUtil.voClassName(class1));
		String propertyIndexed = method.getPropertyName();

		return propertyVO + "." + propertyIndexed;
	}

	protected String getPropertyConsulta() throws Wj2eeException {
		String propertyVOWrapper = TagHandlerUtil
				.denifirVariavelParaTipo(TagHandlerUtil
						.voWrapperClassName(class1));
		String propertyVO = TagHandlerUtil
				.denifirVariavelParaTipo(TagHandlerUtil.voClassName(class1));
		String propertyIndexed = method.getPropertyName();

		String returnValue;

		if (TagHandlerUtil.ifIsPropriedadeTratavelTipoVO(method)) {
			returnValue = propertyVOWrapper + "." + propertyVO + "."
					+ propertyIndexed;
		} else {
			returnValue = propertyVOWrapper + "." + propertyIndexed;
		}

		return returnValue;
	}

	public String generateCode() throws Wj2eeException {
		return super.generateCode() + "&nbsp;";
	}
}