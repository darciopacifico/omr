/*
 * Data de Criacao 05/06/2005 20:49:28
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.cadastro.struts;

import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.codegenerator.base.AbstractStrutsCodeGenerator;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;
import br.com.dlp.framework.util.FrameworkAcheUtil;

/**
 * Gera uma chamada à tag html:text do struts
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:49:28
 */
public class StrutsHtmlTextAreaCodeGenerator extends
		AbstractStrutsCodeGenerator {
	public static final String PREFIX_TXT_COUNTER = "Counter";

	public static final int MUTIPLICADOR_CHARS = 3;

	public String generateCode() throws Wj2eeException {
		String code = super.generateCode();

		StringBuffer stringBuffer = new StringBuffer(code);
		appendSpecialControl(stringBuffer);

		return new String(stringBuffer);
	}

	protected void appendSpecialControl(StringBuffer stringBuffer)
			throws Wj2eeException {
		/*
		 * Caso o tipo da propriedade seja data, concateno o código de abertura
		 * de CALENDARIO
		 */

		String txtCounterName = getTxtCounterName();

		stringBuffer.append("\n\t\t\t" + "<div name='" + txtCounterName
				+ "' id='" + txtCounterName + "'></div>");

		// "<html:text property='"+txtCounterName+"' styleClass='cc0'
		// readonly='false' maxlength='5' size='5' value='"+maxLength+"' />"

	}

	/** Override do metodo getTag da superclasse */
	public String getTag() {
		return "html:textarea";
	}

	public String getOnkeyup() throws Wj2eeException {
		return getOnkeypress();
	}

	public String generateAttributes() throws Wj2eeException {
		getDefaultAttributes().remove(ATT_SIZE);
		getDefaultAttributes().remove(ATT_DISABLED);
		getDefaultAttributes().remove(ATT_READONLY);
		getDefaultAttributes().add(ATT_COLS);
		getDefaultAttributes().add(ATT_ROWS);
		getDefaultAttributes().add(ATT_ONKEYPRESS);
		getDefaultAttributes().add(ATT_ONKEYUP);

		String maxLength = getMaxlength();
		String txtCounterName = getTxtCounterName();

		setOnkeypress("textCounter(this," + txtCounterName + "," + maxLength
				+ ");");

		return super.generateAttributes();
	}

	protected String getTxtCounterName() {
		String propertyName = method.getPropertyName();

		String entidade = TagHandlerUtil
				.primeiraLetraMinuscula(atributosClasseVO.getEntidade());

		propertyName = TagHandlerUtil.primeiraLetraMaiuscula(propertyName);
		propertyName = entidade + "_" + PREFIX_TXT_COUNTER + propertyName;

		return propertyName;
	}

	public String getMaxlength() throws Wj2eeException {
		String maxlength = super.getMaxlength();

		if (FrameworkAcheUtil.isNullOrEmptyOrZero(maxlength)) {
			String strRows = getRows();
			String strCols = getCols();

			int rows = Integer.parseInt(strRows);
			int cols = Integer.parseInt(strCols);

			maxlength = (rows * cols * MUTIPLICADOR_CHARS) + "";
		}

		return maxlength;
	}
}
