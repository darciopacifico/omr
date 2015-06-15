package br.com.jazz.codegen;

import java.io.Writer;

import br.com.jazz.codegen.ITemplateRules.FILE_EXISTING_ACTIONS;
import br.com.jazz.jazzwizard.exception.GeradorException;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * Contrato da fábrica abstrata de writers para o gerador de código
 * @author darcio
 *
 */
public interface IWriterFactory {

	/**
	 * Fabrica uma implementação de java.io.Writer para onde será encaminhada a geração do arquivo
	 * @throws GeradorException 
	 */
	public abstract Writer getWriter(ITemplateRules templateRules, JavaClass javaClass) throws GeradorException;

	/**
	 * Fabrica uma implementação de java.io.Writer para onde será encaminhada a geração do arquivo
	 * @throws GeradorException 
	 */
	public abstract Writer getWriterConfig(ITemplateRules templateRules) throws GeradorException;

	/**
	 * Implementar pergunta ao usuário se é permitido sobrescrever o arquivo a ser gerado
	 * 
	 * @param templateRules
	 * @param domainName
	 * @return
	 */
	public abstract FILE_EXISTING_ACTIONS askUserForOverwrite(ITemplateRules templateRules, String domainName);

	
	
	/**
	 * Finaliza writer utilizado na geração dos artefatos. Sobrescrever, caso necessite controlar esta acao
	 * 
	 * @param writer
	 */
	public void finalizeConfigFileWriter(Writer writer);

	/**
	 * Finaliza writer utilizado na geração dos artefatos. Sobrescrever, caso necessite controlar esta acao
	 * 
	 * @param writer
	 */
	public void finalizeFileWriter(Writer writer);

	
	
}