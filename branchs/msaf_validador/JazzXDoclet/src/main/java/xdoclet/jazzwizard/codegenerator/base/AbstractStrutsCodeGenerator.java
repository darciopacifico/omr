/*
 * Data de Criacao 05/06/2005 20:40:37
 * 
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.codegenerator.CodeGeneratorException;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;
import xjavadoc.XClass;
import br.com.dlp.framework.util.FrameworkAcheUtil;

/**
 * Implementação abstrata de ICodeGenerator
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:40:37
 */
public abstract class AbstractStrutsCodeGenerator extends AbstractCodeGenerator {
	public static final String ATT_ACCESSKEY = "accessKey";

	public static final String ATT_ALT = "alt";

	public static final String ATT_ALTKEY = "altKey";

	public static final String ATT_BUNDLE = "bundle";

	public static final String ATT_DISABLED = "disabled";

	public static final String ATT_ERRORKEY = "errorKey";

	public static final String ATT_ERRORSTYLE = "errorStyle";

	public static final String ATT_ERRORSTYLECLASS = "errorStyleClass";

	public static final String ATT_ERRORSTYLEID = "errorStyleId";

	public static final String ATT_INDEXED = "indexed";

	public static final String ATT_MAXLENGTH = "maxlength";

	/* TEXTARE ONLY */
	public static final String ATT_ROWS = "rows";

	public static final String ATT_COLS = "cols";

	public static final String ATT_NAME = "name";

	public static final String ATT_ONBLUR = "onblur";

	public static final String ATT_ONCHANGE = "onchange";

	public static final String ATT_ONCLICK = "onclick";

	public static final String ATT_ONDBLCLICK = "ondblclick";

	public static final String ATT_ONFOCUS = "onfocus";

	public static final String ATT_ONKEYDOWN = "onkeydown";

	public static final String ATT_ONKEYPRESS = "onkeypress";

	public static final String ATT_ONKEYUP = "onkeyup";

	public static final String ATT_ONMOUSEDOWN = "onmousedown";

	public static final String ATT_ONMOUSEMOVE = "onmousemove";

	public static final String ATT_ONMOUSEOUT = "onmouseout";

	public static final String ATT_ONMOUSEOVER = "onmouseover";

	public static final String ATT_ONMOUSEUP = "onmouseup";

	public static final String ATT_PROPERTY = "property";

	public static final String ATT_READONLY = "readonly";

	public static final String ATT_SIZE = "size";

	public static final String ATT_STYLE = "style";

	public static final String ATT_STYLECLASS = "styleClass";

	public static final String ATT_STYLEID = "styleId";

	public static final String ATT_TABINDEX = "tabindex";

	public static final String ATT_TITLE = "title";

	public static final String ATT_TITLEKEY = "titleKey";

	public static final String ATT_VALUE = "value";

	public static final String ATT_IGNORE = "ignore";

	public static final String ATT_FORMAT_KEY = "formatKey";

	private String indexed = "false";

	private String ignore = "";

	private String formatKey = "";

	private String name = "<%=Constants.BEAN_KEY%>";

	private String property = "";

	private String styleClass = "cc0";

	private String readonly;

	private String onblur = "";

	private String onchange = "";

	private String onclick = "";

	private String ondblclick = "";

	private String onfocus = "";

	private String onkeydown = "";

	private String onkeypress = "";

	private String onkeyup = "";

	private String onmousedown = "";

	private String onmousemove = "";

	private String onmouseout = "";

	private String onmouseover = "";

	private String rows = "";

	private String onmouseup = "";

	private String bundle;

	private String errorKey;

	private String errorStyle;

	private String errorStyleClass;

	private String errorStyleId;

	private String size;

	private String style;

	private String styleId;

	private String tabindex;

	private String value;

	private String accessKey;

	private Set defaultAttributes = new LinkedHashSet(6);

	/* TEXTAREA ONLY */
	private String cols;

	/**
	 * 
	 */
	public AbstractStrutsCodeGenerator() {
		super();

		defaultAttributes.add(ATT_PROPERTY);
		defaultAttributes.add(ATT_STYLECLASS);
		defaultAttributes.add(ATT_ALTKEY);
		defaultAttributes.add(ATT_TITLEKEY);
		defaultAttributes.add(ATT_READONLY);
	}

	/**
	 * Implementação de generateCode()
	 */
	public String generateCode() throws Wj2eeException {

		// TESTO SE O CONTROLE ATUAL É UM CONTROLE PARA UMA TELA DE PESQUISA
		if (tipoControle != null
				&& tipoControle.equals(TagHandlerUtil.TIPO_CONTROLE_PESQUISA)) {

			// SE FOR, ENTÃO O NOME DA PROPRIEDADE DESTE SERÁ O RESULTADO DE
			// TagHandlerUtil.nomeAtributoParaPesquisa(method)

			String property = TagHandlerUtil.nomeAtributoParaPesquisa(method,
					getControlSufix());
			setProperty(property);
			setReadonly("false");

		}

		String tag = getTag();
		String generateTagOpening = "<" + tag + " ";
		String generateAttributes = generateAttributes();
		String generateInTagClosing; // inicialização depende de uma condicao
		String generateTagBody = generateTagBody();
		String generateTagClosing; // inicialização depende de uma condicao
		// "\n"

		if (generateTagBody == null || generateTagBody.equals("")) {
			generateInTagClosing = "/>";
			generateTagClosing = "";

		} else {
			generateInTagClosing = ">\n";
			generateTagClosing = "\n\t\t\t</" + tag + ">";

		}

		StringBuffer generateCodeBuffer = new StringBuffer();

		/* <html:select property="statusSelectedId" styleClass="cc0"> */
		generateCodeBuffer.append(generateTagOpening);
		generateCodeBuffer.append(generateAttributes);
		generateCodeBuffer.append(generateInTagClosing);

		/* <html:options collection='statusList' /> */
		generateCodeBuffer.append("\t\t\t\t" + generateTagBody);

		/* </html:select> */
		generateCodeBuffer.append(generateTagClosing);

		return generateCodeBuffer.toString();
	}

	/**
	 * Gera a string que representa a tag <br>
	 * Ex: "html:text"
	 * 
	 * @throws Wj2eeException
	 */
	public abstract String getTag() throws Wj2eeException;

	/**
	 * Processa o corpo da tag
	 * 
	 * @throws Wj2eeException
	 */
	public String generateTagBody() throws Wj2eeException {
		return "";
	}

	/**
	 * Gera e retorna a sequencia de atributos em pares no formato 'chave=valor'
	 * selecionados pelo metodo getDefaultAttributes
	 */
	public String generateAttributes() throws Wj2eeException {
		String sequenceOfAttributes = "";
		defaultAttributes = getDefaultAttributes();

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

	/**
	 * Retorna o conjunto " atributo='valor' " para atributos de tags do struts
	 */
	protected String generateAttribute(String attributeName)
			throws CodeGeneratorException {

		String EVALUATE_PREFIX = "evaluate";

		String firstLetter = attributeName.substring(0, 1).toUpperCase();
		String remainingOfWord = attributeName.substring(1);

		String completeMethodName = EVALUATE_PREFIX + firstLetter
				+ remainingOfWord;

		Method method;
		String processedAttribute;
		try {

			method = this.getClass().getMethod(completeMethodName,
					new Class[] {});
			processedAttribute = (String) method.invoke(this, new Object[] {});

		} catch (SecurityException e) {
			throw new CodeGeneratorException(e,
					"Erro ao tentar avaliar o atributo " + attributeName + "");

		} catch (NoSuchMethodException e) {
			throw new CodeGeneratorException(e,
					"Erro ao tentar avaliar o atributo " + attributeName + "");

		} catch (IllegalArgumentException e) {
			throw new CodeGeneratorException(e,
					"Erro ao tentar avaliar o atributo " + attributeName + "");

		} catch (IllegalAccessException e) {
			throw new CodeGeneratorException(e,
					"Erro ao tentar avaliar o atributo " + attributeName + "");

		} catch (InvocationTargetException e) {
			throw new CodeGeneratorException(e,
					"Erro ao tentar avaliar o atributo " + attributeName + "");

		}

		logger.debug("...RESULTADO: " + attributeName + " ==>> "
				+ processedAttribute);
		return processedAttribute;

	}

	/**
	 * Seleciona quais os atributos padrão para a tag corrente.
	 */
	public Set getDefaultAttributes() {
		return defaultAttributes;
	}

	/**
	 * Retornar os atributos padrão para a tag corrente.
	 */
	public void setDefaultAttributes(Set defaultAttributes) {
		this.defaultAttributes = defaultAttributes;
	}

	/**
	 * Metodo Setter do atributo indexed.
	 */
	public void setIndexed(String indexed) {
		this.indexed = indexed;
	}

	/**
	 * Metodo Getter do atributo accessKey.
	 */
	public String getAccessKey() {
		return accessKey;
	}

	/**
	 * Metodo Setter do atributo accessKey.
	 */
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getAlt() throws Wj2eeException {
		String dica = TagHandlerUtil.dicaValueProperty(this.method);
		return dica;
	}

	/**
	 * Metodo Getter do atributo onblur.
	 */
	public String getOnblur() {
		return onblur;
	}

	/**
	 * Metodo Getter do atributo onchange.
	 */
	public String getOnchange() {
		return onchange;
	}

	/**
	 * Metodo Getter do atributo onclick.
	 */
	public String getOnclick() {
		return onclick;
	}

	/**
	 * Metodo Getter do atributo bundle.
	 */
	public String getBundle() {
		return bundle;
	}

	/**
	 * Metodo Getter do atributo errorKey.
	 */
	public String getErrorKey() {
		return errorKey;
	}

	/**
	 * Metodo Getter do atributo errorStyle.
	 */
	public String getErrorStyle() {
		return errorStyle;
	}

	/**
	 * Metodo Getter do atributo errorStyleClass.
	 */
	public String getErrorStyleClass() {
		return errorStyleClass;
	}

	/**
	 * Metodo Getter do atributo errorStyleId.
	 */
	public String getErrorStyleId() {
		return errorStyleId;
	}

	/**
	 * Metodo Getter do atributo readonly.
	 */
	public String getReadonly() {
		if (readonly == null || readonly.trim().equals("")) {
			readonly = atributosMetodoVO.getSomenteLeitura();
		}

		return readonly;
	}

	/**
	 * Metodo Getter do atributo size.
	 */
	public String getSize() {
		if (size == null || size.trim().equals("")) {
			size = atributosMetodoVO.getLarguraCampo();
		}
		return size;
	}

	/**
	 * Metodo Getter do atributo style.
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * Metodo Getter do atributo styleClass.
	 */
	public String getStyleClass() {
		return styleClass;
	}

	/**
	 * Metodo Getter do atributo styleId.
	 */
	public String getStyleId() {
		return styleId;
	}

	/**
	 * Metodo Getter do atributo tabindex.
	 */
	public String getTabindex() {
		return tabindex;
	}

	/**
	 * Metodo Setter do atributo bundle.
	 */
	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	/**
	 * Metodo Setter do atributo errorKey.
	 */
	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

	/**
	 * Metodo Setter do atributo errorStyle.
	 */
	public void setErrorStyle(String errorStyle) {
		this.errorStyle = errorStyle;
	}

	/**
	 * Metodo Setter do atributo errorStyleClass.
	 */
	public void setErrorStyleClass(String errorStyleClass) {
		this.errorStyleClass = errorStyleClass;
	}

	/**
	 * Metodo Setter do atributo errorStyleId.
	 */
	public void setErrorStyleId(String errorStyleId) {
		this.errorStyleId = errorStyleId;
	}

	/**
	 * Metodo Setter do atributo readonly.
	 */
	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	/**
	 * Metodo Setter do atributo size.
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * Metodo Setter do atributo style.
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * Metodo Setter do atributo styleClass.
	 */
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	/**
	 * Metodo Setter do atributo styleId.
	 */
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	/**
	 * Metodo Setter do atributo tabindex.
	 */
	public void setTabindex(String tabindex) {
		this.tabindex = tabindex;
	}

	/**
	 * Metodo Getter do atributo ondblclick.
	 */
	public String getOndblclick() {
		return ondblclick;
	}

	/**
	 * Metodo Getter do atributo onfocus.
	 */
	public String getOnfocus() {
		return onfocus;
	}

	/**
	 * Metodo Getter do atributo onkeydown.
	 */
	public String getOnkeydown() {
		return onkeydown;
	}

	/**
	 * Metodo Getter do atributo onkeypress.
	 * 
	 * @throws Wj2eeException
	 */
	public String getOnkeypress() throws Wj2eeException {
		return onkeypress;
	}

	/**
	 * Metodo Getter do atributo onkeyup.
	 * 
	 * @throws Wj2eeException
	 */
	public String getOnkeyup() throws Wj2eeException {
		return onkeyup;
	}

	/**
	 * Metodo Getter do atributo onmousedown.
	 */
	public String getOnmousedown() {
		return onmousedown;
	}

	/**
	 * Metodo Getter do atributo onmousemove.
	 */
	public String getOnmousemove() {
		return onmousemove;
	}

	/**
	 * Metodo Getter do atributo onmouseout.
	 */
	public String getOnmouseout() {
		return onmouseout;
	}

	/**
	 * Metodo Getter do atributo onmouseover.
	 */
	public String getOnmouseover() {
		return onmouseover;
	}

	/**
	 * Metodo Getter do atributo onmouseup.
	 */
	public String getOnmouseup() {
		return onmouseup;
	}

	/**
	 * Metodo Setter do atributo onblur.
	 */
	public void setOnblur(String onblur) {
		this.onblur = onblur;
	}

	/**
	 * Metodo Setter do atributo onchange.
	 */
	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	/**
	 * Metodo Setter do atributo onclick.
	 */
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	/**
	 * Metodo Setter do atributo ondblclick.
	 */
	public void setOndblclick(String ondblclick) {
		this.ondblclick = ondblclick;
	}

	/**
	 * Metodo Setter do atributo onfocus.
	 */
	public void setOnfocus(String onfocus) {
		this.onfocus = onfocus;
	}

	/**
	 * Metodo Setter do atributo onkeydown.
	 */
	public void setOnkeydown(String onkeydown) {
		this.onkeydown = onkeydown;
	}

	/**
	 * Metodo Setter do atributo onkeypress.
	 */
	public void setOnkeypress(String onkeypress) {
		this.onkeypress = onkeypress;
	}

	/**
	 * Metodo Setter do atributo onkeyup.
	 */
	public void setOnkeyup(String onkeyup) {
		this.onkeyup = onkeyup;
	}

	/**
	 * Metodo Setter do atributo onmousedown.
	 */
	public void setOnmousedown(String onmousedown) {
		this.onmousedown = onmousedown;
	}

	/**
	 * Metodo Setter do atributo onmousemove.
	 */
	public void setOnmousemove(String onmousemove) {
		this.onmousemove = onmousemove;
	}

	/**
	 * Metodo Setter do atributo onmouseout.
	 */
	public void setOnmouseout(String onmouseout) {
		this.onmouseout = onmouseout;
	}

	/**
	 * Metodo Setter do atributo onmouseover.
	 */
	public void setOnmouseover(String onmouseover) {
		this.onmouseover = onmouseover;
	}

	/**
	 * Metodo Setter do atributo onmouseup.
	 */
	public void setOnmouseup(String onmouseup) {
		this.onmouseup = onmouseup;
	}

	public String getAltKey() throws Wj2eeException {
		String dicaKey = TagHandlerUtil.dicaKeyProperty(this.method);
		return dicaKey;
	}

	public String getDisabled() throws Wj2eeException {
		boolean disabled = TagHandlerUtil.somenteLeitura(this.method);
		return "" + disabled;
	}

	public String getIndexed() throws Wj2eeException {
		return indexed;
	}

	public String getMaxlength() throws Wj2eeException {
		String maxlength = atributosMetodoVO.getTamanhoMaximo();
		return maxlength;
	}

	/**
	 * Metodo Setter do atributo name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getName() throws Wj2eeException {
		return name;
	}

	public String getProperty() throws Wj2eeException {

		if (property == null || property.trim().equals("")) {
			property = method.getPropertyName();
			String tipoControle = getTipoControle();

			if (tipoControle.equals(TagHandlerUtil.TIPO_CONTROLE_CADASTRO)) {
				String voWrapperClassName = TagHandlerUtil
						.voWrapperClassName(class1);
				String voWrapperPropertyName = TagHandlerUtil
						.denifirVariavelParaTipo(voWrapperClassName);

				if (TagHandlerUtil.ifIsPropriedadeTratavelTipoVO(method)
						&& TagHandlerUtil.somenteLeitura(method)) {
					property = voWrapperPropertyName + ".baseVO." + property;

				} else {
					property = voWrapperPropertyName + "." + property;

				}

			} else if (tipoControle
					.equals(TagHandlerUtil.TIPO_CONTROLE_DEFAULT)) {
				String voWrapperClassName = TagHandlerUtil
						.voWrapperClassName(class1);
				String voWrapperPropertyName = TagHandlerUtil
						.denifirVariavelParaTipo(voWrapperClassName);

				if (TagHandlerUtil.ifIsPropriedadeTratavelTipoVO(method)
						&& TagHandlerUtil.somenteLeitura(method)) {
					property = voWrapperPropertyName + ".baseVO." + property;

				} else {
					property = voWrapperPropertyName + "." + property;

				}

			} else if (tipoControle
					.equals(TagHandlerUtil.TIPO_CONTROLE_INDEXADO)) {
				/* por enquanto, deixa como está */

			} else if (tipoControle
					.equals(TagHandlerUtil.TIPO_CONTROLE_PESQUISA)) {
				/* por enquanto, deixa como está */

			}
		}

		return property;
	}

	/** Metodo Setter do atributo property. */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * Metodo Getter do atributo value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Metodo Setter do atributo value.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public String getTitle() throws Wj2eeException {
		String title = TagHandlerUtil.dicaValueProperty(method);
		return title;
	}

	public String getTitleKey() throws Wj2eeException {
		String titleKey = TagHandlerUtil.dicaKeyProperty(method);
		return titleKey;
	}

	public String evaluateAccesskey() throws Wj2eeException {
		return ATT_ACCESSKEY + "='" + getAccessKey() + "' ";
	}

	public String evaluateIgnore() throws Wj2eeException {
		return ATT_IGNORE + "='" + getIgnore() + "' ";
	}

	public String evaluateFormatKey() throws Wj2eeException {
		String formatKey = getFormatKey();
		String evaluate;

		if (!FrameworkAcheUtil.isNullOrEmpty(formatKey)) {
			evaluate = ATT_FORMAT_KEY + "='" + formatKey + "' ";
		} else {
			evaluate = "";
		}

		return evaluate;
	}

	public String evaluateAlt() throws Wj2eeException {
		return ATT_ALT + "='" + getAlt() + "' ";
	}

	public String evaluateAltKey() throws Wj2eeException {
		return ATT_ALTKEY + "='" + getAltKey() + "' ";
	}

	public String evaluateBundle() throws Wj2eeException {
		return ATT_BUNDLE + "='" + getBundle() + "' ";
	}

	public String evaluateDisabled() throws Wj2eeException {
		return ATT_DISABLED + "='" + getDisabled() + "' ";
	}

	public String evaluateErrorKey() throws Wj2eeException {
		return ATT_ERRORKEY + "='" + getErrorKey() + "' ";
	}

	public String evaluateErrorStyle() throws Wj2eeException {
		return ATT_ERRORSTYLE + "='" + getErrorStyle() + "' ";
	}

	public String evaluateErrorstyleclass() throws Wj2eeException {
		return ATT_ERRORSTYLECLASS + "='" + getErrorStyleClass() + "' ";
	}

	public String evaluateErrorStyleId() throws Wj2eeException {
		return ATT_ERRORSTYLEID + "='" + getErrorStyleId() + "' ";
	}

	public String evaluateIndexed() throws Wj2eeException {
		return ATT_INDEXED + "='" + getIndexed() + "' ";
	}

	public String evaluateMaxlength() throws Wj2eeException {
		return ATT_MAXLENGTH + "='" + getMaxlength() + "' ";
	}

	public String evaluateName() throws Wj2eeException {
		return ATT_NAME + "='" + getName() + "' ";
	}

	public String evaluateOnblur() throws Wj2eeException {
		return ATT_ONBLUR + "='" + getOnblur() + "' ";
	}

	public String evaluateOnchange() throws Wj2eeException {
		return ATT_ONCHANGE + "='" + getOnchange() + "' ";
	}

	public String evaluateOnclick() throws Wj2eeException {
		return ATT_ONCLICK + "='" + getOnclick() + "' ";
	}

	public String evaluateOndblclick() throws Wj2eeException {
		return ATT_ONDBLCLICK + "='" + getOndblclick() + "' ";
	}

	public String evaluateOnfocus() throws Wj2eeException {
		return ATT_ONFOCUS + "='" + getOnfocus() + "' ";
	}

	public String evaluateOnkeydown() throws Wj2eeException {
		return ATT_ONKEYDOWN + "='" + getOnkeydown() + "' ";
	}

	public String evaluateOnkeypress() throws Wj2eeException {
		return ATT_ONKEYPRESS + "='" + getOnkeypress() + "' ";
	}

	public String evaluateOnkeyup() throws Wj2eeException {
		return ATT_ONKEYUP + "='" + getOnkeyup() + "' ";
	}

	public String evaluateOnmousedown() throws Wj2eeException {
		return ATT_ONMOUSEDOWN + "='" + getOnmousedown() + "' ";
	}

	public String evaluateOnmousemove() throws Wj2eeException {
		return ATT_ONMOUSEMOVE + "='" + getOnmousemove() + "' ";
	}

	public String evaluateOnmouseout() throws Wj2eeException {
		return ATT_ONMOUSEOUT + "='" + getOnmouseout() + "' ";
	}

	public String evaluateOnmouseover() throws Wj2eeException {
		return ATT_ONMOUSEOVER + "='" + getOnmouseover() + "' ";
	}

	public String evaluateOnmouseup() throws Wj2eeException {
		return ATT_ONMOUSEUP + "='" + getOnmouseup() + "' ";
	}

	public String evaluateProperty() throws Wj2eeException {
		return ATT_PROPERTY + "='" + getProperty() + "' ";
	}

	public String evaluateReadonly() throws Wj2eeException {
		return ATT_READONLY + "='" + getReadonly() + "' ";
	}

	public String evaluateSize() throws Wj2eeException {
		return ATT_SIZE + "='" + getSize() + "' ";
	}

	public String evaluateStyle() throws Wj2eeException {
		return ATT_STYLE + "='" + getStyle() + "' ";
	}

	public String evaluateStyleClass() throws Wj2eeException {
		return ATT_STYLECLASS + "='" + getStyleClass() + "' ";
	}

	public String evaluateStyleId() throws Wj2eeException {
		return ATT_STYLEID + "='" + getStyleId() + "' ";
	}

	public String evaluateTabindex() throws Wj2eeException {
		return ATT_TABINDEX + "='" + getTabindex() + "' ";
	}

	public String evaluateTitle() throws Wj2eeException {
		return ATT_TITLE + "=\"" + getTitle() + "\" ";
	}

	public String evaluateTitleKey() throws Wj2eeException {
		return ATT_TITLEKEY + "=\"" + getTitleKey() + "\" ";
	}

	public String evaluateValue() throws Wj2eeException {
		return ATT_VALUE + "='" + getValue() + "' ";
	}

	public String evaluateRows() throws Wj2eeException {
		return ATT_ROWS + "='" + getRows() + "' ";
	}

	public String evaluateCols() throws Wj2eeException {
		return ATT_COLS + "='" + getCols() + "' ";
	}

	/**
	 * @return Returns the cols.
	 */
	public String getCols() {
		if (this.cols == null || this.cols.trim().equals("")) {
			String larguraCampo = atributosMetodoVO.getLarguraCampo();
			this.cols = larguraCampo;
		}

		return cols;
	}

	/**
	 * @param cols
	 *            The cols to set.
	 */
	public void setCols(String cols) {
		this.cols = cols;
	}

	/**
	 * @return Returns the rows.
	 */
	public String getRows() {
		if (FrameworkAcheUtil.isNullOrEmptyOrZero(rows)) {
			rows = atributosMetodoVO.getRows();
		}
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	/**
	 * @return Returns the ignore.
	 */
	public String getIgnore() {
		return ignore;
	}

	/**
	 * @param ignore
	 *            The ignore to set.
	 */
	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}

	/**
	 * @return Returns the formatKey.
	 */
	public String getFormatKey() {

		if (FrameworkAcheUtil.isNullOrEmpty(this.formatKey)) {
			XClass propertyType = method.getPropertyType().getType();
			if (TagHandlerUtil.isInstanceOf(propertyType,
					TagHandlerUtil.CLASS_NAME_DATE)) {
				this.formatKey = TagHandlerUtil.FORMATKEY_DATA;

			} else if (TagHandlerUtil.isInstanceOf(propertyType, Short.class
					.getName())) {
				this.formatKey = TagHandlerUtil.FORMATKEY_SHORT;

			} else if (TagHandlerUtil.isInstanceOf(propertyType, Integer.class
					.getName())) {
				this.formatKey = TagHandlerUtil.FORMATKEY_INTEGER;

			} else if (TagHandlerUtil.isInstanceOf(propertyType, Long.class
					.getName())) {
				this.formatKey = TagHandlerUtil.FORMATKEY_LONG;

			} else if (TagHandlerUtil.isInstanceOf(propertyType, Float.class
					.getName())) {
				this.formatKey = TagHandlerUtil.FORMATKEY_FLOAT_STRUTS;

			} else if (TagHandlerUtil.isInstanceOf(propertyType, Double.class
					.getName())) {
				this.formatKey = TagHandlerUtil.FORMATKEY_DOUBLE_STRUTS;

			}
		}

		return this.formatKey;
	}

	/**
	 * @param formatKey
	 *            The formatKey to set.
	 */
	public void setFormatKey(String formatKey) {
		this.formatKey = formatKey;
	}
}