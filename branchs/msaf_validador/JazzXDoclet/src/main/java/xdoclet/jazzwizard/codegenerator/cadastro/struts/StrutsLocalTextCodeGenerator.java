/*
 * Data de Criacao 05/06/2005 20:49:28
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.cadastro.struts;

import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.codegenerator.base.AbstractStrutsCodeGenerator;

/**
 * Gera uma chamada à tag html:text do struts
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:49:28
 */
public class StrutsLocalTextCodeGenerator extends AbstractStrutsCodeGenerator {

	/** Override do metodo generateCode da superclasse */
	public String generateCode() throws Wj2eeException {
		return this.getClass().getName();
	}

	public String getTag() {
		return "html:text";
	}
}
