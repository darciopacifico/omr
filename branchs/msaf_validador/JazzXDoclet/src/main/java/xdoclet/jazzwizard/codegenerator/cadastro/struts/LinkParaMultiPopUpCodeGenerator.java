/*
 * Data de Criacao 05/06/2005 20:49:28
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.cadastro.struts;

import xdoclet.jazzwizard.codegenerator.base.AbstractStrutsCodeGenerator;

/**
 * Gera uma chamada à tag html:text do struts
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:49:28
 */
public class LinkParaMultiPopUpCodeGenerator extends
		AbstractStrutsCodeGenerator {
	/** Override do metodo getTag da superclasse */
	public String getTag() {
		return "html:text";
	}

}
