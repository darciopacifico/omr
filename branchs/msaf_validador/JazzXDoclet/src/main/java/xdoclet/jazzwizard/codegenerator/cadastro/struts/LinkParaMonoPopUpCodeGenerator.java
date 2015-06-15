/*
 * Data de Criacao 05/06/2005 20:49:28
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.cadastro.struts;

import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.codegenerator.base.AbstractStrutsCodeGenerator;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;
import xjavadoc.XClass;

/**
 * Gera uma chamada à tag html:text do struts
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:49:28
 */
public class LinkParaMonoPopUpCodeGenerator extends AbstractStrutsCodeGenerator {
	private String tag;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	/* não permito gerar corpo para esta tag */
	public String generateTagBody() {
		return "";
	}

	public String generateCode() throws Wj2eeException {

		/** *********************** */
		/** * GERANDO BEAN WRITE ** */
		/* não quero gerar estes atributos */
		getDefaultAttributes().remove(ATT_STYLECLASS);
		getDefaultAttributes().remove(ATT_ALTKEY);
		getDefaultAttributes().remove(ATT_TITLEKEY);
		getDefaultAttributes().remove(ATT_READONLY);

		/* preciso de name */
		getDefaultAttributes().add(ATT_NAME);

		/* name terá o seguinte valor */
		setName("<%=Constants.BEAN_KEY%>");

		setTag("bean:write");

		String voWrapperProperty = TagHandlerUtil
				.primeiraLetraMinuscula(TagHandlerUtil
						.voWrapperClassName(class1));
		String voClassProperty = TagHandlerUtil
				.primeiraLetraMinuscula(TagHandlerUtil.voClassName(class1));

		XClass methodPropertyClass = method.getPropertyType().getType();
		String voMethodProperty = TagHandlerUtil
				.primeiraLetraMinuscula(TagHandlerUtil
						.voClassName(methodPropertyClass));

		setProperty(voWrapperProperty + "." + voClassProperty + "."
				+ voMethodProperty);

		String beanWrite = super.generateCode();

		String larguraCampo = atributosMetodoVO.getLarguraCampo();
		beanWrite = "<input type=\"text\" size=\"" + larguraCampo
				+ "\" class=\"cc0\" value=\"" + beanWrite.trim()
				+ "\" readonly=\"readonly\" disabled=\"disabled\"  >";

		/** * GERANDO BEAN WRITE ** */
		/** *********************** */

		/** *********************** */
		/** * GERANDO POPUP LINK ** */
		setTag("html:button");
		getDefaultAttributes().remove(ATT_NAME);

		getDefaultAttributes().add(ATT_ONCLICK);
		getDefaultAttributes().add(ATT_STYLECLASS);
		getDefaultAttributes().add(ATT_VALUE);
		getDefaultAttributes().add(ATT_PROPERTY);

		setStyleClass("bb1");
		setValue("...");
		setProperty("abrirPopUp");

		String actionClassName = TagHandlerUtil
				.actionClassName(class1, javaDoc);
		String serviceCallPopUp = TagHandlerUtil.callServiceParaPopUp(method,
				javaDoc);
		String constanteServiceCallPopUp = TagHandlerUtil
				.constanteParaServico(serviceCallPopUp);

		setOnclick("<%= \"invocaServico('\"+" + actionClassName + "."
				+ constanteServiceCallPopUp + "+\"'); travarBotao(this);\"%>");

		String linkPopUp = super.generateCode();
		/** * GERANDO POPUP LINK ** */
		/** *********************** */

		return beanWrite + "\n\t\t\t" + linkPopUp;
	}
}
