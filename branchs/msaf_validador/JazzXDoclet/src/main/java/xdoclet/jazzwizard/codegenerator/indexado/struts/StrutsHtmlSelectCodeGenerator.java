/*
 * Data de Criacao 05/06/2005 20:49:28
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.indexado.struts;

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

	/**
	 * Override do metodo generateCode da superclasse
	 * 
	 * @throws Wj2eeException
	 * @throws XDocletException
	 */
	public String generateCode() throws Wj2eeException {
		String propertyName = method.getPropertyName();

		setProperty(propertyName);

		/* NÃO EXISTE readonly num SELECT */
		getDefaultAttributes().remove(
				StrutsHtmlSelectCodeGenerator.ATT_READONLY);

		return super.generateCode();
	}

	/**
	 * Override do metodo generateTagBody da superclasse
	 * 
	 * @throws Wj2eeException
	 */
	public String generateTagBody() throws Wj2eeException {
		String tagBody = "";

		StringBuffer sb = new StringBuffer(tagBody);

		String property = method.getPropertyName();
		String classProperty = TagHandlerUtil.primeiraLetraMaiuscula(property);

		// XClass methodClass = TagHandlerUtil.classeDoMetodo(method,javaDoc);
		String entidade = TagHandlerUtil.primeiraLetraMinuscula(TagHandlerUtil
				.entidadeDaClasse(class1));
		String name = TagHandlerUtil.toSingular(entidade);

		sb.append("\n\t\t\t\t\t\t").append(
				"<logic:iterate id=\"" + property + "\" name=\"" + name
						+ "\" property=\"list" + classProperty
						+ "s\"  indexId=\"idDaVez\" >");
		sb.append("\n\t\t\t\t\t\t\t").append(
				"<html:option value=\"<%=\"\"+idDaVez%>\">");
		sb.append("\n\t\t\t\t\t\t\t\t").append(
				"<bean:write name=\"" + property + "\"/>");
		sb.append("\n\t\t\t\t\t\t\t").append("</html:option>");
		sb.append("\n\t\t\t\t\t\t").append("</logic:iterate>");

		tagBody = sb.toString();

		return tagBody;
	}

}