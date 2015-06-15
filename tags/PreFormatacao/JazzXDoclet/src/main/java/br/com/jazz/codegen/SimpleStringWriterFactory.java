/**
 * 
 */
package br.com.jazz.codegen;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.dlp.framework.i18n.JazzMessageFormat;
import br.com.jazz.codegen.exception.OverwriteNotAllowedException;

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

	protected Log log = LogFactory.getLog(SimpleStringWriterFactory.class);
	protected static JazzMessageFormat mf = new JazzMessageFormat("br.com.jazz.gerador.messages");
	
	@Override
	protected Writer createWriter(ITemplateRules templateRules, JavaClass javaClass, boolean temporario) {
		return new StringWriter();
	}

	@Override
	protected Writer createWriter(ITemplateRules templateRules, boolean temporario) {
		return new StringWriter();
	}

	@Override
	protected boolean exists(ITemplateRules templateRules, JavaClass javaClass) {
		return false;
	}

	@Override
	protected boolean exists(ITemplateRules templateRules) {
		return false;
	}

	/* (non-Javadoc)
	 * @see br.com.jazz.codegen.IWriterFactory#getWriterConfig(br.com.jazz.codegen.ITemplateRules)
	 */
	public Writer getWriterConfig(ITemplateRules templateRules) throws OverwriteNotAllowedException {
		// TODO Auto-generated method stub
		return new StringWriter();
	}

	
	
	

	/**
	 * Finaliza writer utilizado na geração dos artefatos. Sobrescrever, caso necessite controlar esta acao
	 * 
	 * @param writer
	 */
	public void finalizeConfigFileWriter(Writer writer) {
		try {
			writer.flush();
			writer.close();
			System.out.println(writer.toString());
		} catch (IOException e) {
			this.log.warn("erro ao tentar fechar o writer!", e);
		}
	}

	/**
	 * Finaliza writer utilizado na geração dos artefatos. Sobrescrever, caso necessite controlar esta acao
	 * 
	 * @param writer
	 */
	public void finalizeFileWriter(Writer writer) {
		try {
			writer.flush();
			writer.close();
			System.out.println(writer.toString());
		} catch (IOException e) {
			this.log.warn("erro ao tentar fechar o writer!", e);
		}
	}
	
}
