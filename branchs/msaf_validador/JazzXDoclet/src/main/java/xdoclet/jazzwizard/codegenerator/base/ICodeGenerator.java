/*
 * Data de Criacao 05/06/2005 20:36:34
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.base;

import xdoclet.XDocletException;
import xdoclet.jazzwizard.Wj2eeException;
import xjavadoc.XJavaDoc;
import xjavadoc.XMethod;

/**
 * Contrato de renderer para gerador de código
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:36:34
 */
public interface ICodeGenerator {
	public String generateCode() throws Wj2eeException, XDocletException;

	public void setMethod(XMethod method) throws Wj2eeException;

	public void setXJavaDoc(XJavaDoc javaDoc);

	public void setTipoControle(String tipoControle);

	public void setControlSufix(String controlSufix);

}
