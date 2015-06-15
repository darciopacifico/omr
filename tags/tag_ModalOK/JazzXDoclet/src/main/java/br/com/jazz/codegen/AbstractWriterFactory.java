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
 * Fábrica abstrata de java.io.Writers, de acordo com necessidades dos templates e javaclasses informados como argumento pelo gerador de arquivos.
 * 
 * Implementacao abstrata IWriterFactory. É utilizado por IGeradorController para criar e controlar o ciclo de vida de instâncias de java.io.Writer para geração
 * de código fonte.
 * 
 * @author darcio
 *         Implementar com as regras de criação e verificação de arquivos e também com a lógica para perguntar ao usuários se o mesmo permitrá sobrescrita.
 */
public abstract class AbstractWriterFactory implements IWriterFactory {
	protected Log log = LogFactory.getLog(AbstractGeradorController.class);
	protected static JazzMessageFormat mf = new JazzMessageFormat("br.com.jazz.gerador.messages");
	
	/**
	 * Implementação de regras abstratas de geraçao de Writers, contemplando regras para de file.exists()=true 
	 * @throws GeradorException 
	 */
	public Writer getWriter(ITemplateRules templateRules, JavaClass javaClass) throws GeradorException {

		if (exists(templateRules, javaClass)) {
			logFileExists(templateRules, javaClass);

			FILE_EXISTING_ACTIONS existingAction =templateRules.getFileExistingAction(); 
			
			return createWriterByAction(templateRules, javaClass, existingAction);
		}

		log.warn(mf.format("writer.generatingfile")); 
		
		return createWriter(templateRules, javaClass, false);

	}
	

	/**
	 * Implementação abstrata de um controlador para criação de um java.io.Writer
	 */
	public Writer getWriterConfig(ITemplateRules templateRules) throws GeradorException {

		if (exists(templateRules)) {
			logFileExists(templateRules);

			FILE_EXISTING_ACTIONS existingAction =templateRules.getFileExistingAction(); 
			
			return configWriterByAction(templateRules, existingAction);
		}

		log.warn(mf.format("writer.generatingfile")); 
		
		return createWriter(templateRules, false);
		
	}
	

	/**
	 * Testa se o config file que será gerado para o template existe
	 * @param templateRules
	 * @return
	 * @throws GeradorException 
	 */
	protected abstract boolean exists(ITemplateRules templateRules) throws GeradorException;


	/**
	 * Avalia qual deve ser a ação para arquivos existentes. Ex: sobrescrever, criar um arquivo temporário, não sobrescrever etc.
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @param existingAction
	 * @return
	 * @throws GeradorException 
	 */
	protected Writer createWriterByAction(ITemplateRules templateRules, JavaClass javaClass, FILE_EXISTING_ACTIONS existingAction) throws GeradorException {
		switch (existingAction) {
		case CREATE_TEMP_FILE:
			String tempFileName = templateRules.getFileName(javaClass);
			
			log.warn(mf.format("writer.tempfile",tempFileName));
			//"Um arquivo sera criado em diretório temporário para permitir comparação posterior!");
			return createWriter(templateRules, javaClass, true);

		case ASK_USER:
			log.warn(mf.format("writer.userdecision"));
			//log.warn("A decisão de sobrescrita será delegada ao usuário!");
			
			ITemplateRules.FILE_EXISTING_ACTIONS acaoUsuario = askUserForOverwrite(templateRules, javaClass.getName());
			
			//caso a acao do usuário seja submeter a decisao ao usuário (rs... loop infinito), lança exceção
			if(acaoUsuario==null || acaoUsuario.equals(FILE_EXISTING_ACTIONS.ASK_USER)){
				//mf.format("A acao escolhida pelo usuário não pode ser nula ou novamente ASK_USER!")
				throw new GeradorException(mf.format("writer.invalidaskuser",templateRules.getName(),javaClass.getName()));
			}
			
			//resubmete a acao para este mesmo switch
			return createWriterByAction(templateRules, javaClass, acaoUsuario); 
			

		case NOT_OVERWRITE:
			String fileName = templateRules.getFileName(javaClass);
			String message = mf.format("writer.not_overwrite", fileName, templateRules.getName(), javaClass.getName());
			log.warn(message);
			throw new OverwriteNotAllowedException(message);

		case OVERWRITE:
			log.warn(mf.format("writer.overwriting"));
			//log.warn("O arquivo será sobrescrito!");
			return createWriter(templateRules, javaClass, false);

		default:
			log.error(mf.format("writer.switchdefault"));
			return createWriter(templateRules, javaClass, true);
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
	protected Writer configWriterByAction(ITemplateRules templateRules, FILE_EXISTING_ACTIONS existingAction) throws GeradorException {
		String fileName = templateRules.getFileName();

		switch (existingAction) {
		case CREATE_TEMP_FILE:
			
			log.warn(mf.format("writer.tempfile",fileName));
			//"Um arquivo sera criado em diretório temporário para permitir comparação posterior!");
			return createWriter(templateRules, true);

		case ASK_USER:
			log.warn(mf.format("writer.userdecision"));
			//log.warn("A decisão de sobrescrita será delegada ao usuário!");
			
			ITemplateRules.FILE_EXISTING_ACTIONS acaoUsuario = askUserForOverwrite(templateRules, "<JavaClasses>");
			
			//caso a acao do usuário seja submeter a decisao ao usuário (rs... loop infinito), lança exceção
			if(acaoUsuario==null || acaoUsuario.equals(FILE_EXISTING_ACTIONS.ASK_USER)){
				//mf.format("A acao escolhida pelo usuário não pode ser nula ou novamente ASK_USER!")
				throw new GeradorException(mf.format("writer.invalidaskuser",templateRules.getName(),fileName));
			}
			
			//resubmete a acao para este mesmo switch
			return configWriterByAction(templateRules, acaoUsuario); 
			

		case NOT_OVERWRITE:
			String message = mf.format("writer.not_overwrite", fileName, templateRules.getName(), fileName);
			log.warn(message);
			throw new OverwriteNotAllowedException(message);

		case OVERWRITE:
			log.warn(mf.format("writer.overwriting"));
			//log.warn("O arquivo será sobrescrito!");
			return createWriter(templateRules, false);

		default:
			log.error(mf.format("writer.switchdefault"));
			return createWriter(templateRules, true);
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
	protected Writer writerByAction(ITemplateRules templateRules, String domainName, FILE_EXISTING_ACTIONS existingAction) throws GeradorException {
		switch (existingAction) {
		case CREATE_TEMP_FILE:
			String tempFileName = templateRules.getFileName();
			
			log.warn(mf.format("writer.tempfile",tempFileName));
			//"Um arquivo sera criado em diretório temporário para permitir comparação posterior!");
			return createWriter(templateRules, true);

		case ASK_USER:
			log.warn(mf.format("writer.userdecision"));
			//log.warn("A decisão de sobrescrita será delegada ao usuário!");
			
			ITemplateRules.FILE_EXISTING_ACTIONS acaoUsuario = askUserForOverwrite(templateRules,domainName);
			
			//caso a acao do usuário seja submeter a decisao ao usuário (rs... loop infinito), lança exceção
			if(acaoUsuario==null || acaoUsuario.equals(FILE_EXISTING_ACTIONS.ASK_USER)){
				//mf.format("A acao escolhida pelo usuário não pode ser nula ou novamente ASK_USER!")
				throw new GeradorException(mf.format("writer.invalidaskuser",templateRules.getName()));
			}
			
			//resubmete a acao para este mesmo switch
			return writerByAction(templateRules, domainName, acaoUsuario); 
			

		case NOT_OVERWRITE:
			String fileName = templateRules.getFileName();
			String message = mf.format("writer.not_overwrite", fileName, templateRules.getName());
			log.warn(message);
			throw new OverwriteNotAllowedException(message);

		case OVERWRITE:
			log.warn(mf.format("writer.overwriting"));
			//log.warn("O arquivo será sobrescrito!");
			return createWriter(templateRules, false);

		default:
			log.error(mf.format("writer.switchdefault"));
			return createWriter(templateRules, true);
		}
	}

	
	
	/**
	 *  Cria um writer para a mídia onde o arquivo será enviado (ex: FileWriter, StringWriter)
	 * @param templateRules
	 * @param temporario
	 * @return
	 */
	protected abstract Writer createWriter(ITemplateRules templateRules, boolean temporario) throws GeradorException;

	/**
	 *  Cria um writer para a mídia onde o arquivo será enviado (ex: FileWriter, StringWriter)
	 * @param templateRules
	 * @return
	 */
	protected Writer createWriter(ITemplateRules templateRules) throws GeradorException{
	 return this.createWriter(templateRules,false);
	}


	/**
	 * Apenas loga mensagem de erro para caso arquivo existe e não seja permitido sobrescrever o arquivo
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @throws GeradorException 
	 */
	protected void logFileExists(ITemplateRules templateRules, JavaClass javaClass) throws GeradorException {
		String fileName = templateRules.getFileName(javaClass);
		log.warn(mf.format("writer.fileexists", fileName, templateRules.getName(), javaClass.getName()));
	}

	

	/**
	 * Apenas loga mensagem de erro para caso arquivo existe e não seja permitido sobrescrever o arquivo
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @throws GeradorException 
	 */
	protected void logFileExists(ITemplateRules templateRules) throws GeradorException {
		String fileName = templateRules.getFileName();
		
		//writer.fileconfigexists=O arquivo de configuracao {0}, referente ao template {1}, já existe!
		log.warn(mf.format("writer.fileconfigexists", fileName, templateRules.getName()));
	}

	/**
	 * Cria um writer para a mídia onde o arquivo será enviado (ex: FileWriter, StringWriter)
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @param temporario
	 * @return
	 */
	protected abstract Writer createWriter(ITemplateRules templateRules, JavaClass javaClass, boolean temporario)throws GeradorException;


	/**
	 * Cria um writer para a mídia onde o arquivo será enviado (ex: FileWriter, StringWriter)
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @param temporario
	 * @return
	 */
	protected Writer createWriter(ITemplateRules templateRules, JavaClass javaClass) throws GeradorException{
		return this.createWriter(templateRules, javaClass, false);
	}

	/**
	 * Verifica se o arquivo existe na mídia onde será criado
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @return
	 * @throws GeradorException 
	 */
	protected abstract boolean exists(ITemplateRules templateRules, JavaClass javaClass) throws GeradorException;


	/**
	 * Implementação simples de submissão de decisão de sobrescrita para o usuário. Utiliza entrada da resposta pelo console! 
	 * Pode ser sobrescrita em outras implementações.
	 * 
	 */
	public FILE_EXISTING_ACTIONS askUserForOverwrite(ITemplateRules templateRules, String destName) {
		
		//printa opções para o usuário
		Map<String, FILE_EXISTING_ACTIONS> mapAlternatives = new HashMap<String, FILE_EXISTING_ACTIONS>();
		mapAlternatives.put("3", FILE_EXISTING_ACTIONS.OVERWRITE);
		mapAlternatives.put("2", FILE_EXISTING_ACTIONS.NOT_OVERWRITE);
		mapAlternatives.put("1", FILE_EXISTING_ACTIONS.CREATE_TEMP_FILE);
		System.out.print("O que fazer com processamento de "+templateRules.getName()+"/"+destName+" "+mapAlternatives+"?:");
		
	
		//Lê escolha do console
		InputStreamReader reader = new InputStreamReader(System.in);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String escolha;
		try {
			 escolha = bufferedReader.readLine();
		} catch (IOException e) {
			log.warn(mf.format("writer.invalidchoice"));
			return FILE_EXISTING_ACTIONS.CREATE_TEMP_FILE;
		}
		
		//analisa escolha: continua ou pergunda de novo
		if(mapAlternatives.containsKey(escolha)){
			return mapAlternatives.get(escolha);
		}else{
			log.warn(mf.format("writer.invalidchoice"));
			return askUserForOverwrite(templateRules, destName);
		}
		
	}

}
