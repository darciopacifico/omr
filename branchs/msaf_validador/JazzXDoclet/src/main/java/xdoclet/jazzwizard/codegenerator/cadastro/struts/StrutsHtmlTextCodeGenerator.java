/*
 * Data de Criacao 05/06/2005 20:49:28
 * 
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.cadastro.struts;

import java.math.BigDecimal;

import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.codegenerator.base.AbstractStrutsCodeGenerator;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;
import xjavadoc.XClass;

/**
 * Gera uma chamada à tag html:text do struts
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:49:28
 */
public class StrutsHtmlTextCodeGenerator extends AbstractStrutsCodeGenerator {

	/** Override do metodo getTag da superclasse */
	public String getTag() {
		String tag;

		if (atributosMetodoVO.getSomenteLeitura().equals("true")) {
			tag = "bean:write";

		} else {
			tag = "html:text";
		}

		return tag;
	}

	/**
	 * Preparo os atributos necessários para esta implementacao de
	 * ICodeGenerator
	 */
	public String generateAttributes() throws Wj2eeException {
		if (atributosMetodoVO.getSomenteLeitura().equals("true")) {
			return generateAttributesReadOnly();

		} else {
			return generateAttributesNotReadOnly();

		}
	}

	/**
	 * Preparo os atributos necessários para esta implementacao de
	 * ICodeGenerator
	 */
	public String generateAttributesReadOnly() throws Wj2eeException {
		getDefaultAttributes().removeAll(getDefaultAttributes());
		getDefaultAttributes().add(ATT_NAME);
		getDefaultAttributes().add(ATT_PROPERTY);

		return super.generateAttributes();
	}

	/**
	 * Preparo os atributos necessários para esta implementacao de
	 * ICodeGenerator
	 */
	public String generateAttributesNotReadOnly() throws Wj2eeException {
		getDefaultAttributes().add(ATT_SIZE);

		String tamanhoMaximo = atributosMetodoVO.getTamanhoMaximo();
		if (tamanhoMaximo != null && !tamanhoMaximo.equals("")) {
			getDefaultAttributes().add(ATT_MAXLENGTH);
		}

		String onkeypress = getOnkeypress();
		String onblur = getOnblur();
		String onchange = getOnchange();

		XClass classeDoMetodo = TagHandlerUtil.classeDoMetodo(method, javaDoc);
		if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_DATE)) {
			onkeypress += generateScriptFormataData(classeDoMetodo);
			onblur += generateScriptValidaData(classeDoMetodo);

			setOnkeypress(onkeypress);
			setOnblur(onblur);

			getDefaultAttributes().add(ATT_ONKEYPRESS);
			getDefaultAttributes().add(ATT_ONBLUR);

		} else if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_NUMBER)) {
			onkeypress += generateScriptValidaNumero(classeDoMetodo);
			onchange += generateScriptFormataNumero(classeDoMetodo);

			setOnkeypress(onkeypress);
			setOnchange(onchange);

			getDefaultAttributes().add(ATT_ONKEYPRESS);
			getDefaultAttributes().add(ATT_ONCHANGE);

		}

		return super.generateAttributes();
	}

	protected String generateScriptValidaData(XClass class1) {
		return "validaData(this.value, 5)";
	}

	protected String generateScriptValidaNumero(XClass class1) {
		String returnValue;
		if (class1.isA(Float.class.getName())
				|| class1.isA(BigDecimal.class.getName())
				|| class1.isA(Double.class.getName())) {
			returnValue = "onlyDec();";
		} else {
			returnValue = "onlyNum();";
		}

		return returnValue;
	}

	protected String generateScriptFormataData(XClass class1) {
		return "formataData(this.name);";
	}

	protected String generateScriptFormataNumero(XClass class1) {
		String returnValue;
		if (class1.isA(Float.class.getName())
				|| class1.isA(BigDecimal.class.getName())
				|| class1.isA(Double.class.getName())) {
			returnValue = "formataNum(this.name);";
		} else {
			returnValue = "";
		}

		return returnValue;
	}

	public String generateCode() throws Wj2eeException {
		String code = super.generateCode();

		StringBuffer stringBuffer = new StringBuffer(code);
		appendSpecialControl(stringBuffer);

		if (atributosMetodoVO.getSomenteLeitura().equals("true")) {
			stringBuffer.append("&nbsp;");
		}

		return new String(stringBuffer);
	}

	protected void appendSpecialControl(StringBuffer stringBuffer)
			throws Wj2eeException {

		if (method.getReturnType().getType().isA("java.util.Date")
				&& !TagHandlerUtil.somenteLeitura(method)) {
			/*
			 * Caso o tipo da propriedade seja data, concateno o código de
			 * abertura de CALENDARIO
			 */

			XClass classe = method.getContainingClass();

			String formName = TagHandlerUtil.actionFormNameMestre(classe,
					javaDoc);
			String property = getProperty();

			stringBuffer.append("\n");
			stringBuffer.append("\t\t\t <a href=\"javascript: void(0);\" \n ");
			stringBuffer
					.append("\t\t\t\t onmouseover=\"if (timeoutId) clearTimeout(timeoutId); window.status='Calendário';return true;\" \n ");
			stringBuffer
					.append("\t\t\t\t onmouseout=\"if (timeoutDelay) calendarTimeout();window.status='';\"  \n ");
			stringBuffer
					.append("\t\t\t\t onclick='g_Calendar.show(event,\""
							+ formName
							+ ".elements[\\\""
							+ property
							+ "\\\"]\", false, \"dd/mm/yyyy\", \"\",\"\"); return false;'> \n ");
			stringBuffer
					.append("\t\t\t\t <img src='../assets/calendario/calendar.gif' style='vertical-align:-30%' name='imgCalendar' border='0' alt=''></a>  \n ");

		}
	}

}