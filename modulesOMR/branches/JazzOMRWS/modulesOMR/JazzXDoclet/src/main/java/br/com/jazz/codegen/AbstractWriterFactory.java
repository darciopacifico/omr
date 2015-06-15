package br.com.jazz.codegen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.dlp.framework.i18n.JazzMessageFormat;
import br.com.jazz.codegen.ITemplateRules.FILE_EXISTING_ACTIONS;
import br.com.jazz.codegen.exception.OverwriteNotAllowedException;
import br.com.jazz.jazzwizard.exception.GeradorException;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * Fábrica abstrata de java.io.Writers, de acordo com necessidades dos templates e javaclasses informados como argumento pelo gerador de
 * arquivos.
 * 
 * Implementacao abstrata IWriterFactory. Ã‰ utilizado por IGeradorController para criar e controlar o ciclo de vida de instâncias de
 * java.io.Writer para geração de código fonte.
 * 
 * @author darcio Implementar com as regras de criação e verificação de arquivos e também com a lógica para perguntar ao usuários se o mesmo
 *         permitrá sobrescrita.
 */
public abstract class AbstractWriterFactory implements IWriterFactory {
	protected static JazzMessageFormat mf = new JazzMessageFormat("br.com.jazz.gerador.messages");
	protected Log log = LogFactory.getLog(AbstractGeradorController.class);
	
	/**
	 * Implementação simples de submissão de decisão de sobrescrita para o usuário. Utiliza entrada da resposta pelo console! Pode ser
	 * sobrescrita em outras implementações.
	 * 
	 */
	public FILE_EXISTING_ACTIONS askUserForOverwrite(final ITemplateRules templateRules, final String destName) {
		
		// printa opções para o usuário
		final Map<String, FILE_EXISTING_ACTIONS> mapAlternatives = new HashMap<String, FILE_EXISTING_ACTIONS>();
		mapAlternatives.put("3", FILE_EXISTING_ACTIONS.OVERWRITE);
		mapAlternatives.put("2", FILE_EXISTING_ACTIONS.NOT_OVERWRITE);
		mapAlternatives.put("1", FILE_EXISTING_ACTIONS.CREATE_TEMP_FILE);
		System.out.print("O que fazer com processamento de " + templateRules.getName() + "/" + destName + " " + mapAlternatives + "?:");
		
		// LÃª escolha do console
		final InputStreamReader reader = new InputStreamReader(System.in);
		final BufferedReader bufferedReader = new BufferedReader(reader);
		String escolha;
		try {
			escolha = bufferedReader.readLine();
		} catch (final IOException e) {
			this.log.warn(AbstractWriterFactory.mf.format("writer.invalidchoice"));
			return FILE_EXISTING_ACTIONS.CREATE_TEMP_FILE;
		}
		
		// analisa escolha: continua ou pergunda de novo
		if (mapAlternatives.containsKey(escolha)) {
			return mapAlternatives.get(escolha);
		} else {
			this.log.warn(AbstractWriterFactory.mf.format("writer.invalidchoice"));
			return this.askUserForOverwrite(templateRules, destName);
		}
		
	}
	
	/**
	 * Avalia qual deve ser a ação para arquivos existentes. Ex: sobrescrever, criar um arquivo temporário, não sobrescrever etc.
	 * 
	 * @param templateRules
	 * @param existingAction
	 * @return
	 * @throws GeradorException
	 */
	protected Writer configWriterByAction(final ITemplateRules templateRules, final FILE_EXISTING_ACTIONS existingAction)
			throws GeradorException {
		final String fileName = templateRules.getFileName();
		
		switch (existingAction) {
			case CREATE_TEMP_FILE:

				this.log.warn(AbstractWriterFactory.mf.format("writer.tempfile", fileName));
				// "Um arquivo sera criado em diretório temporário para permitir comparação posterior!");
				return this.createWriter(templateRules, true);
				
			case ASK_USER:
				this.log.warn(AbstractWriterFactory.mf.format("writer.userdecision"));
				// log.warn("A decisão de sobrescrita será delegada ao usuário!");
				
				final ITemplateRules.FILE_EXISTING_ACTIONS acaoUsuario = this.askUserForOverwrite(templateRules, "<JavaClasses>");
				
				// caso a acao do usuário seja submeter a decisao ao usuário (rs... loop infinito), lança exceção
				if (acaoUsuario == null || acaoUsuario.equals(FILE_EXISTING_ACTIONS.ASK_USER)) {
					// mf.format("A acao escolhida pelo usuário não pode ser nula ou novamente ASK_USER!")
					throw new GeradorException(AbstractWriterFactory.mf.format("writer.invalidaskuser", templateRules.getName(), fileName));
				}
				
				// resubmete a acao para este mesmo switch
				return this.configWriterByAction(templateRules, acaoUsuario);
				
			case NOT_OVERWRITE:
				final String message = AbstractWriterFactory.mf.format("writer.not_overwrite", fileName, templateRules.getName(), fileName);
				this.log.warn(message);
				throw new OverwriteNotAllowedException(message);
				
			case OVERWRITE:
				this.log.warn(AbstractWriterFactory.mf.format("writer.overwriting"));
				// log.warn("O arquivo será sobrescrito!");
				return this.createWriter(templateRules, false);
				
			default:
				this.log.error(AbstractWriterFactory.mf.format("writer.switchdefault"));
				return this.createWriter(templateRules, true);
		}
	}
	
	/**
	 * Cria um writer para a mÃ­dia onde o arquivo será enviado (ex: FileWriter, StringWriter)
	 * 
	 * @param templateRules
	 * @return
	 */
	protected Writer createWriter(final ITemplateRules templateRules) throws GeradorException {
		return this.createWriter(templateRules, false);
	}
	
	/**
	 * Cria um writer para a mÃ­dia onde o arquivo será enviado (ex: FileWriter, StringWriter)
	 * 
	 * @param templateRules
	 * @param temporario
	 * @return
	 */
	protected abstract Writer createWriter(ITemplateRules templateRules, boolean temporario) throws GeradorException;
	
	/**
	 * Cria um writer para a mÃ­dia onde o arquivo será enviado (ex: FileWriter, StringWriter)
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @param temporario
	 * @return
	 */
	protected Writer createWriter(final ITemplateRules templateRules, final JavaClass javaClass) throws GeradorException {
		return this.createWriter(templateRules, javaClass, false);
	}
	
	/**
	 * Cria um writer para a mÃ­dia onde o arquivo será enviado (ex: FileWriter, StringWriter)
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @param temporario
	 * @return
	 */
	protected abstract Writer createWriter(ITemplateRules templateRules, JavaClass javaClass, boolean temporario) throws GeradorException;
	
	/**
	 * Avalia qual deve ser a ação para arquivos existentes. Ex: sobrescrever, criar um arquivo temporário, não sobrescrever etc.
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @param existingAction
	 * @return
	 * @throws GeradorException
	 */
	protected Writer createWriterByAction(final ITemplateRules templateRules, final JavaClass javaClass,
			final FILE_EXISTING_ACTIONS existingAction) throws GeradorException {
		switch (existingAction) {
			case CREATE_TEMP_FILE:
				final String tempFileName = templateRules.getFileName(javaClass);
				
				this.log.warn(AbstractWriterFactory.mf.format("writer.tempfile", tempFileName));
				// "Um arquivo sera criado em diretório temporário para permitir comparação posterior!");
				return this.createWriter(templateRules, javaClass, true);
				
			case ASK_USER:
				this.log.warn(AbstractWriterFactory.mf.format("writer.userdecision"));
				// log.warn("A decisão de sobrescrita será delegada ao usuário!");
				
				final ITemplateRules.FILE_EXISTING_ACTIONS acaoUsuario = this.askUserForOverwrite(templateRules, javaClass.getName());
				
				// caso a acao do usuário seja submeter a decisao ao usuário (rs... loop infinito), lança exceção
				if (acaoUsuario == null || acaoUsuario.equals(FILE_EXISTING_ACTIONS.ASK_USER)) {
					// mf.format("A acao escolhida pelo usuário não pode ser nula ou novamente ASK_USER!")
					throw new GeradorException(AbstractWriterFactory.mf.format("writer.invalidaskuser", templateRules.getName(), javaClass.getName()));
				}
				
				// resubmete a acao para este mesmo switch
				return this.createWriterByAction(templateRules, javaClass, acaoUsuario);
				
			case NOT_OVERWRITE:
				final String fileName = templateRules.getFileName(javaClass);
				final String message = AbstractWriterFactory.mf.format("writer.not_overwrite", fileName, templateRules.getName(), javaClass
						.getName());
				this.log.warn(message);
				throw new OverwriteNotAllowedException(message);
				
			case OVERWRITE:
				this.log.warn(AbstractWriterFactory.mf.format("writer.overwriting"));
				// log.warn("O arquivo será sobrescrito!");
				return this.createWriter(templateRules, javaClass, false);
				
			default:
				this.log.error(AbstractWriterFactory.mf.format("writer.switchdefault"));
				return this.createWriter(templateRules, javaClass, true);
		}
	}
	
	/**
	 * Testa se o config file que será gerado para o template existe
	 * 
	 * @param templateRules
	 * @return
	 * @throws GeradorException
	 */
	protected abstract boolean exists(ITemplateRules templateRules) throws GeradorException;
	
	/**
	 * Verifica se o arquivo existe na mÃ­dia onde será criado
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @return
	 * @throws GeradorException
	 */
	protected abstract boolean exists(ITemplateRules templateRules, JavaClass javaClass) throws GeradorException;
	
	/**
	 * Implementação de regras abstratas de geraçao de Writers, contemplando regras para de file.exists()=true
	 * 
	 * @throws GeradorException
	 */
	public Writer getWriter(final ITemplateRules templateRules, final JavaClass javaClass) throws GeradorException {
		
		if (this.exists(templateRules, javaClass)) {
			this.logFileExists(templateRules, javaClass);
			
			final FILE_EXISTING_ACTIONS existingAction = templateRules.getFileExistingAction();
			
			return this.createWriterByAction(templateRules, javaClass, existingAction);
		}
		
		this.log.warn(AbstractWriterFactory.mf.format("writer.generatingfile"));
		
		return this.createWriter(templateRules, javaClass, false);
		
	}
	
	/**
	 * Implementação abstrata de um controlador para criação de um java.io.Writer
	 */
	public Writer getWriterConfig(final ITemplateRules templateRules) throws GeradorException {
		
		if (this.exists(templateRules)) {
			this.logFileExists(templateRules);
			
			final FILE_EXISTING_ACTIONS existingAction = templateRules.getFileExistingAction();
			
			return this.configWriterByAction(templateRules, existingAction);
		}
		
		this.log.warn(AbstractWriterFactory.mf.format("writer.generatingfile"));
		
		return this.createWriter(templateRules, false);
		
	}
	
	/**
	 * Apenas loga mensagem de erro para caso arquivo existe e não seja permitido sobrescrever o arquivo
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @throws GeradorException
	 */
	protected void logFileExists(final ITemplateRules templateRules) throws GeradorException {
		final String fileName = templateRules.getFileName();
		
		// writer.fileconfigexists=O arquivo de configuracao {0}, referente ao template {1}, já existe!
		this.log.warn(AbstractWriterFactory.mf.format("writer.fileconfigexists", fileName, templateRules.getName()));
	}
	
	/**
	 * Apenas loga mensagem de erro para caso arquivo existe e não seja permitido sobrescrever o arquivo
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @throws GeradorException
	 */
	protected void logFileExists(final ITemplateRules templateRules, final JavaClass javaClass) throws GeradorException {
		final String fileName = templateRules.getFileName(javaClass);
		this.log.warn(AbstractWriterFactory.mf.format("writer.fileexists", fileName, templateRules.getName(), javaClass.getName()));
	}
	
	/**
	 * Avalia qual deve ser a ação para arquivos existentes. Ex: sobrescrever, criar um arquivo temporário, não sobrescrever etc.
	 * 
	 * @param templateRules
	 * @param existingAction
	 * @return
	 * @throws GeradorException
	 */
	protected Writer writerByAction(final ITemplateRules templateRules, final String domainName, final FILE_EXISTING_ACTIONS existingAction)
			throws GeradorException {
		switch (existingAction) {
			case CREATE_TEMP_FILE:
				final String tempFileName = templateRules.getFileName();
				
				this.log.warn(AbstractWriterFactory.mf.format("writer.tempfile", tempFileName));
				// "Um arquivo sera criado em diretório temporário para permitir comparação posterior!");
				return this.createWriter(templateRules, true);
				
			case ASK_USER:
				this.log.warn(AbstractWriterFactory.mf.format("writer.userdecision"));
				// log.warn("A decisão de sobrescrita será delegada ao usuário!");
				
				final ITemplateRules.FILE_EXISTING_ACTIONS acaoUsuario = this.askUserForOverwrite(templateRules, domainName);
				
				// caso a acao do usuário seja submeter a decisao ao usuário (rs... loop infinito), lança exceção
				if (acaoUsuario == null || acaoUsuario.equals(FILE_EXISTING_ACTIONS.ASK_USER)) {
					// mf.format("A acao escolhida pelo usuário não pode ser nula ou novamente ASK_USER!")
					throw new GeradorException(AbstractWriterFactory.mf.format("writer.invalidaskuser", templateRules.getName()));
				}
				
				// resubmete a acao para este mesmo switch
				return this.writerByAction(templateRules, domainName, acaoUsuario);
				
			case NOT_OVERWRITE:
				final String fileName = templateRules.getFileName();
				final String message = AbstractWriterFactory.mf.format("writer.not_overwrite", fileName, templateRules.getName());
				this.log.warn(message);
				throw new OverwriteNotAllowedException(message);
				
			case OVERWRITE:
				this.log.warn(AbstractWriterFactory.mf.format("writer.overwriting"));
				// log.warn("O arquivo será sobrescrito!");
				return this.createWriter(templateRules, false);
				
			default:
				this.log.error(AbstractWriterFactory.mf.format("writer.switchdefault"));
				return this.createWriter(templateRules, true);
		}
	}
	
}
