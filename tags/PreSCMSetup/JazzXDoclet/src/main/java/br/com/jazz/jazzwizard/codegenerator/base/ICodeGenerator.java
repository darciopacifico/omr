/*
 * Data de Criacao 05/06/2005 20:36:34
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package br.com.jazz.jazzwizard.codegenerator.base;

import xdoclet.XDocletException;
import xjavadoc.XJavaDoc;
import xjavadoc.XMethod;
import br.com.jazz.jazzwizard.Wj2eeException;

/**
 * Contrato de renderer para gerador de codigo
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:36:34
 */
public interface ICodeGenerator {
	public String generateCode() throws Wj2eeException, XDocletException;
	
	public void setControlSufix(String controlSufix);
	
	public void setMethod(XMethod method) throws Wj2eeException;
	
	public void setTipoControle(String tipoControle);
	
	public void setXJavaDoc(XJavaDoc javaDoc);
	
}
