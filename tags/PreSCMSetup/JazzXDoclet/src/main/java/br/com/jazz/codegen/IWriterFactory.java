package br.com.jazz.codegen;

import java.io.File;
import java.io.Writer;

import br.com.jazz.codegen.ITemplateRules.FILE_EXISTING_ACTIONS;
import br.com.jazz.jazzwizard.exception.GeradorException;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * Contrato da fábrica abstrata de writers para o gerador de código
 * 
 * @author darcio
 * 
 */
public interface IWriterFactory {
	
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
	
	/**
	 * Fabrica uma implementação de java.io.Writer para onde será encaminhada a geração do arquivo
	 * 
	 * @throws GeradorException
	 */
	public abstract Writer getWriter(ITemplateRules templateRules, JavaClass javaClass) throws GeradorException;
	
	/**
	 * Fabrica uma implementação de java.io.Writer para onde será encaminhada a geração do arquivo
	 * 
	 * @throws GeradorException
	 */
	public abstract Writer getWriterConfig(ITemplateRules templateRules) throws GeradorException;
	
	/**
	 * Coloca um sufixo no final de baseDir, caso tenha sido especificado. Exemplo src/main/java; src/main/webapp, onde "java" e "webapp" são
	 * sufixos.
	 * 
	 * @param baseDir2
	 * @param templateRules
	 * @return
	 * @throws GeradorException
	 */
	public File getBaseDirSufix(final File baseDir, final ITemplateRules templateRules) throws GeradorException;
	
	/**
	 * Recupera como será a estrutura de diretório que será gerada, de acordo com a package de javaClass. Este path não contém o nome do
	 * arquivo no final da String.
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @return
	 * @throws GeradorException
	 */
	public String getDirRelativePath(final ITemplateRules templateRules, final JavaClass javaClass) throws GeradorException;
	
}
