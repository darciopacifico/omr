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
public class ReadonlyCodeGenerator extends AbstractStrutsCodeGenerator {

	/** Override do metodo getTag da superclasse */
	public String getTag() {
		String tag;

		tag = "bean:write";

		return tag;
	}

	/**
	 * Preparo os atributos necessários para esta implementacao de
	 * ICodeGenerator
	 */
	public String generateAttributes() throws Wj2eeException {

		getDefaultAttributes().clear();
		getDefaultAttributes().add(ATT_NAME);
		getDefaultAttributes().add(ATT_PROPERTY);

		return super.generateAttributes();
	}

}