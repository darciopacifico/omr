/*
 * Data de Criacao 05/06/2005 20:49:28
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.cadastro.struts;

import xdoclet.XDocletException;
import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.codegenerator.base.AbstractStrutsCodeGenerator;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;

/**
 * Gera uma chamada à tag html:text do struts
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:49:28
 */
public class StrutsHtmlSelectCodeGenerator extends AbstractStrutsCodeGenerator {
	/**
	 * Override do metodo getTag da superclasse
	 * 
	 * @throws Wj2eeException
	 */
	public String getTag() throws Wj2eeException {
		String tag;

		tag = "html:select";

		return tag;
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
		return super.generateCode();
	}

	protected String optionPadrao() throws Wj2eeException {
		StringBuffer sb = new StringBuffer();

		sb.append(" <html:option value=\"-1\">");
		sb.append("		<bean:message key=\"pesquisa.padrao.labelNenhum\"/>");
		sb.append(" </html:option>");

		return new String(sb);
	}

	/**
	 * Override do metodo generateTagBody da superclasse
	 * 
	 * @throws Wj2eeException
	 */
	public String generateTagBody() throws Wj2eeException {
		String tagBody;

		tagBody = super.generateTagBody();

		StringBuffer sb = new StringBuffer(tagBody);

		String wrapperName = TagHandlerUtil
				.primeiraLetraMinuscula(TagHandlerUtil
						.voWrapperClassName(class1));

		String property = method.getPropertyName();
		String classProperty = TagHandlerUtil.primeiraLetraMaiuscula(property);

		/* Opção Padrão, "Todos", "Nenhum", "Branco" */
		String optionPadrao = optionPadrao();
		sb.append(optionPadrao);

		sb.append("\n\t\t\t\t").append(
				"<logic:iterate id=\"" + property
						+ "\" name=\"<%=Constants.BEAN_KEY%>\" property=\""
						+ wrapperName + ".list" + classProperty
						+ "s\" indexId=\"idDaVez\" >");
		sb.append("\n\t\t\t\t\t").append(
				"<html:option value=\"<%=\"\"+idDaVez%>\">");
		sb.append("\n\t\t\t\t\t\t").append(
				"<bean:write name=\"" + property + "\"/>");
		sb.append("\n\t\t\t\t\t").append("</html:option>");
		sb.append("\n\t\t\t\t").append("</logic:iterate>");
		sb.append("\n\t\t\t\t");

		tagBody = sb.toString();

		return tagBody;
	}
}