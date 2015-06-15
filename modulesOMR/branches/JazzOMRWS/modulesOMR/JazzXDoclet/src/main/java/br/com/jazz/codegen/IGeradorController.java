package br.com.jazz.codegen;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.BeansException;

import br.com.jazz.jazzwizard.exception.GeradorException;

import com.thoughtworks.qdox.model.JavaClass;

import freemarker.template.TemplateException;

/**
 * Contrato para controller para gera��o de c�digo. Principal componente para gera��o de c�digo. Orquestra chamadas aos demais
 * 
 * @author darcio
 */
public interface IGeradorController {
	
	public void generateConfigFile() throws GeradorException;
	
	/**
	 * Processa gera��o de c�digo para o conjunto de JavaClass do modelo de domínio, por exemplo, gerar arquivo de configura��o
	 * faces-config.xml
	 * 
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void generateConfigFile(ITemplateRules templateRules) throws GeradorException;
	
	public void generateConfigFile(ITemplateRules... templateRules) throws GeradorException;
	
	/**
	 * Gera arquivos de configuracao a partir dos templates informados para todas as entidades identificadas.
	 * 
	 * @param templates
	 * @throws GeradorException
	 */
	public void generateConfigFile(List<String> templates) throws GeradorException;
	
	/**
	 * Processa gera��o de c�digo para o conjunto de JavaClass do modelo de domínio, por exemplo, gerar arquivo de configura��o
	 * faces-config.xml
	 * 
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void generateConfigFile(String templateName) throws GeradorException;
	
	public void generateConfigFile(String... templateNames) throws GeradorException;
	
	/**
	 * Processa gera��o de c�digo para o template informado para cada uma das classes do modelo de domínio
	 * 
	 * @throws GeradorException
	 * 
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void generateFile(ITemplateRules... templateRules) throws GeradorException;
	
	/**
	 * Processa gera��o de c�digo para uma �nica JavaClass identificada no modelo. Por exemplo, gerar p�gina JSP de uma entidade de domínio
	 * 
	 * @param templateRules
	 * @throws GeradorException
	 * 
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void generateFile(JavaClass javaClass, ITemplateRules... templateRules) throws GeradorException;
	
	/**
	 * Processa gera��o de c�digo para uma �nica JavaClass identificada no modelo. Por exemplo, gerar p�gina JSP de uma entidade de domínio
	 * 
	 * @param templateName
	 * @param javaClass
	 * @throws GeradorException
	 * 
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void generateFile(JavaClass javaClass, String... templateName) throws GeradorException;
	
	/**
	 * Processa gera��o de c�digo para uma �nica JavaClass identificada no modelo. Por exemplo, gerar p�gina JSP de uma entidade de domínio
	 * 
	 * @param templateName
	 * @param javaClass
	 * @throws GeradorException
	 * 
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void generateFile(JavaClass[] javaClass, String... templateName) throws GeradorException;
	
	/**
	 * Processa gera��o de c�digo para o template informado para todas as classes do modelo de domínio
	 * 
	 * @throws GeradorException
	 * 
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void generateFile(String... templateName) throws GeradorException;
	
	public IDomainModelLoaderFactory getModelLoader();
	
	public ITemplateRulesFactory getTemplateRulesFactory() throws BeansException, GeradorException;
	
	public IWriterFactory getWriterFactory();
	
}
