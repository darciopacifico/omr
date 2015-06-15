package br.com.jazz.codegen;

import java.util.List;
import java.util.Map;

import br.com.jazz.jazzwizard.exception.GeradorException;

/**
 * Contrato para f�brica abstrata de ITemplateRules
 * 
 * @author darcio
 * 
 */
public interface ITemplateRulesFactory {
	
	/**
	 * @return the availableTemplates
	 */
	public Map<String, List<String>> getAvailableTemplates();
	
	/**
	 * Retorna abstra��o de templates
	 * 
	 * @param templateName
	 * @return
	 * @throws GeradorException
	 */
	public ITemplateRules getTemplateRules(String templateName) throws GeradorException;
	
}
