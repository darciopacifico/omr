/*
 * Data de Criacao 05/06/2005 20:49:28
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.cadastro.struts;

import java.util.Collection;

import xdoclet.XDocletException;
import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.codegenerator.base.AbstractStrutsCodeGenerator;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;

/**
 * Gera uma chamada à tag html:text do struts
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:49:28
 */
public class StrutsHtmlRadioCodeGenerator extends AbstractStrutsCodeGenerator {
	/** Override do metodo getTag da superclasse */
	public String getTag() {
		return "html:radio";
	}

	/**
	 * Override do metodo generateCode da superclasse
	 * 
	 * @throws Wj2eeException
	 * @throws XDocletException
	 */
	public String generateCode() throws Wj2eeException {
		/* NÃO EXISTE readonly num SELECT. Se for assim não utilze html:select */
		getDefaultAttributes().remove(
				StrutsHtmlSelectCodeGenerator.ATT_READONLY);

		String id = TagHandlerUtil.propertyNameSemSufixoVO(method);
		String voWrapperClassName = TagHandlerUtil
				.primeiraLetraMinuscula(TagHandlerUtil
						.voWrapperClassName(class1));
		String list = "list" + method.getNameWithoutPrefix() + "s";

		String abreIterate = "<%idDaVez=0;%>\n\t\t\t<logic:iterate id='" + id
				+ "' name='<%=Constants.BEAN_KEY%>' property='"
				+ voWrapperClassName + "." + list + "'  >";

		/* devo gerar o atributo value='' */
		Collection defaultAttributes = getDefaultAttributes();
		defaultAttributes.add(ATT_VALUE);
		/* não vai styleClass em radio */
		defaultAttributes.remove(ATT_STYLECLASS);

		setValue("<%=\"\"+(idDaVez++)%>");
		String strRadio = super.generateCode();
		String fechaIterate = "</logic:iterate>";

		String strutsCode = "\n\t\t\t" + abreIterate + "\n\t\t\t\t" + strRadio
				+ "\n\t\t\t" + fechaIterate;

		return strutsCode;
	}

	/**
	 * Override do metodo generateTagBody da superclasse
	 * 
	 * @throws Wj2eeException
	 */
	public String generateTagBody() throws Wj2eeException {
		String tagBody;
		String propertyName = TagHandlerUtil.propertyNameSemSufixoVO(method);

		tagBody = "\t<bean:write name='" + propertyName + "'/>";
		return tagBody;
	}

}
