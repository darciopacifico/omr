package br.com.jazz.codegen;

import java.io.Writer;
import java.util.Map;

import br.com.jazz.codegen.exception.TemplateRulesException;
import br.com.jazz.jazzwizard.exception.GeradorException;

import com.thoughtworks.qdox.model.JavaClass;

public interface ITemplateRules {

	/**
	 * Acoes para o caso do arquivo que será gerado já existir
	 * 
	 * @author darcio
	 */
	public enum FILE_EXISTING_ACTIONS {
		OVERWRITE, ASK_USER, NOT_OVERWRITE, CREATE_TEMP_FILE
	};
	
	/**
	 * Determina se deverá ser gerado código para a classe informada
	 * 
	 * @param javaClass
	 * @return
	 */
	public abstract boolean isGenerate(JavaClass javaClass);

	/**
	 * Retorna nome do template
	 * @return
	 */
	public abstract String getName();

	/**
	 * Recupera o valor de uma propriedade XDoclet de um JavaClass
	 * 
	 * @param javaClass
	 * @param key
	 * @return null, caso a tag ou um atributo nao seja encontrado
	 */
	public abstract Object getAnnotationValue(JavaClass javaClass, String key);

	/**
	 * @return the fileExistingAction
	 */
	public abstract FILE_EXISTING_ACTIONS getFileExistingAction();

	/**
	 * @param fileExistingAction
	 *          the fileExistingAction to set
	 */
	public abstract void setFileExistingAction(FILE_EXISTING_ACTIONS fileExistingAction);

	/**
	 * @return the properties
	 */
	public abstract Map<String, String> getProperties();

	/**
	 * @param properties
	 *          the properties to set
	 */
	public abstract void setProperties(Map<String, String> map);

	/**
	 * @return the regexRules
	 */
	public abstract Map<String, String> getRegexRules();

	/**
	 * @param regexRules
	 *          the regexRules to set
	 */
	public abstract void setRegexRules(Map<String, String> regexRules);

	/**
	 * @return the docletRules
	 */
	public abstract Map<String, String> getAnnotationRules();

	/**
	 * @param annotationRules
	 *          the annotationRules to set
	 */
	public abstract void setAnnotationRules(Map<String, String> annotationRules);

	public abstract void process(Map<String, Object> map, Writer writer) throws TemplateRulesException;

	/**
	 * Recupera o nome da entidade de domínio
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @return
	 * @throws GeradorException
	 */
	public String getDomainName(JavaClass javaClass) throws GeradorException;

	
	
	

	/**
	 * Determina o nome do arquivo que será criado
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @param temp determina se o arquivo será para destino temporário
	 * @return
	 * @throws GeradorException 
	 */
	public abstract String getFileName(JavaClass javaClass) throws GeradorException;

	/**
	 * Determina o nome do arquivo que será criado
	 * 
	 * @param templateRules
	 * @param temp determina se o arquivo será para destino temporário
	 * @return
	 * @throws GeradorException 
	 */
	public abstract String getFileName() throws GeradorException;

	/**
	 * Implementação de IWriterFactory.getFileName(). Recupera como será o nome do arquivo gerado
	 * 
	 * @throws GeradorException
	 */
	public String getJavaComponentName(JavaClass javaClass) throws GeradorException;

	public String getTemplateEncoding();

	public void setTemplateEncoding(String encoding);

	public String getOutputEncoding();

	public void setOutputEncoding(String outputEncoding);
	
}


