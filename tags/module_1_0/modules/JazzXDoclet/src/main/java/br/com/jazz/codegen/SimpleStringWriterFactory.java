/**
 * 
 */
package br.com.jazz.codegen;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.dlp.framework.i18n.JazzMessageFormat;
import br.com.jazz.codegen.exception.OverwriteNotAllowedException;
import br.com.jazz.jazzwizard.exception.GeradorException;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * Implementacao simplesde IWriterFactory que cria fileWriters
 * 
 * É utilizado por IGeradorController para criar e controlar o ciclo de vida de instancias de java.io.Writer para geração de código
 * 
 * @author darcio
 * 
 */
public class SimpleStringWriterFactory extends AbstractWriterFactory {
	
	protected static JazzMessageFormat mf = new JazzMessageFormat("br.com.jazz.gerador.messages");
	protected Log log = LogFactory.getLog(SimpleStringWriterFactory.class);
	
	@Override
	protected Writer createWriter(final ITemplateRules templateRules, final boolean temporario) {
		return new StringWriter();
	}
	
	@Override
	protected Writer createWriter(final ITemplateRules templateRules, final JavaClass javaClass, final boolean temporario) {
		return new StringWriter();
	}
	
	@Override
	protected boolean exists(final ITemplateRules templateRules) {
		return false;
	}
	
	@Override
	protected boolean exists(final ITemplateRules templateRules, final JavaClass javaClass) {
		return false;
	}
	
	/**
	 * Finaliza writer utilizado na geração dos artefatos. Sobrescrever, caso necessite controlar esta acao
	 * 
	 * @param writer
	 */
	public void finalizeConfigFileWriter(final Writer writer) {
		try {
			writer.flush();
			writer.close();
			System.out.println(writer.toString());
		} catch (final IOException e) {
			log.warn("erro ao tentar fechar o writer!", e);
		}
	}
	
	/**
	 * Finaliza writer utilizado na geração dos artefatos. Sobrescrever, caso necessite controlar esta acao
	 * 
	 * @param writer
	 */
	public void finalizeFileWriter(final Writer writer) {
		try {
			writer.flush();
			writer.close();
			System.out.println(writer.toString());
		} catch (final IOException e) {
			log.warn("erro ao tentar fechar o writer!", e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IWriterFactory#getWriterConfig(br.com.jazz.codegen.ITemplateRules)
	 */
	@Override
	public Writer getWriterConfig(final ITemplateRules templateRules) throws OverwriteNotAllowedException {
		// TODO Auto-generated method stub
		return new StringWriter();
	}
	
	@Override
	public File getBaseDirSufix(File baseDir, ITemplateRules templateRules) throws GeradorException {
		// TODO Auto-generated method stub
		log.error("getBaseDirSufix NAO APLICADO NESTA IMPLEMENTACAO DE WRITER FACTORY ");
		throw new GeradorException("getBaseDirSufix NAO APLICADO NESTA IMPLEMENTACAO DE WRITER FACTORY ");
	}
	
	@Override
	public String getDirRelativePath(ITemplateRules templateRules, JavaClass javaClass) throws GeradorException {
		// TODO Auto-generated method stub
		return "getDirRelativePath NAO APLICADO NESTA IMPLEMENTACAO DE WRITER FACTORY ";
	}
	
}
