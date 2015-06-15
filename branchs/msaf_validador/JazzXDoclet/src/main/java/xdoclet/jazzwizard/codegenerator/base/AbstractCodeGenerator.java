/*
 * Data de Criacao 05/06/2005 20:40:37
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;
import xdoclet.jazzwizard.tagvalue.AtributosClasseVO;
import xdoclet.jazzwizard.tagvalue.AtributosMetodoVO;
import xjavadoc.XClass;
import xjavadoc.XJavaDoc;
import xjavadoc.XMethod;

/**
 * Implementação abstrata de ICodeGenerator
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:40:37
 */
public abstract class AbstractCodeGenerator implements ICodeGenerator {
	protected Log logger = LogFactory.getLog(AbstractCodeGenerator.class);

	protected XMethod method;

	protected XClass class1;

	protected XJavaDoc javaDoc;

	protected AtributosMetodoVO atributosMetodoVO;

	protected AtributosClasseVO atributosClasseVO;

	protected String tipoControle;

	protected String controlSufix;

	public AbstractCodeGenerator() {
	}

	/**
	 * Override do metodo setMethod da superclasse
	 * 
	 * @throws Wj2eeException
	 */
	public void setMethod(XMethod method) throws Wj2eeException {
		this.method = method;
		this.class1 = method.getContainingClass();

		atributosMetodoVO = new AtributosMetodoVO(method);
		atributosClasseVO = new AtributosClasseVO(this.class1);
	}

	/**
	 * Identifica qual a classe da propriedade.<br>
	 * Caso seja instanceOf Collection, tenta descobir pelo atributo 'classe'
	 * 
	 * @throws Wj2eeException
	 */
	protected XClass getClassOfProperty() throws Wj2eeException {

		return TagHandlerUtil.classeDoMetodo(method, javaDoc);
	}

	/** Override do metodo setXJavaDoc da superclasse */
	public void setXJavaDoc(XJavaDoc javaDoc) {

		this.javaDoc = javaDoc;

	}

	/**
	 * Gera uma String contendo uma quantidade qtd de caracteres TAB
	 */
	public static String tabs(int qtd) {

		String tabs = "";

		for (int i = 0; i < qtd; i++) {
			tabs += "\t";
		}

		return tabs;
	}

	public void setTipoControle(String tipoControle) {
		this.tipoControle = tipoControle;
	}

	public String getTipoControle() {
		return tipoControle;
	}

	protected String getPrimaryKey(XMethod method) {
		return "id";
	}

	/**
	 * @return Returns the controlSufix.
	 */
	public String getControlSufix() {
		return controlSufix;
	}

	/**
	 * @param controlSufix
	 *            The controlSufix to set.
	 */
	public void setControlSufix(String controlSufix) {
		this.controlSufix = controlSufix;
	}

}
