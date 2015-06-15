/**
 * 
 */
package br.com.jazz.codegen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;

import br.com.jazz.codegen.exception.DestDirCreationException;
import br.com.jazz.codegen.exception.FileWriterFactoryException;
import br.com.jazz.jazzwizard.exception.GeradorException;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * Fábrica de Writers para arquivos simples
 * 
 * @author darcio
 */
public class FileWriterFactory extends AbstractWriterFactory {

	/**
	 * Literal tmp apenas.
	 */
	public static final String LITERAL_STRING_TMP = "tmp";

	/**
	 * Regular expression para testar se o valor da propriedade PROP_BASE_DIR_SUFIX possui o coringa "{0}" para ser substituida por "tmp" na geração de arquivos
	 * temporários.
	 */
	public static final String REGEX_POSSUI_CORINGA_TMP = "^.*\\{0\\}.*$";

	/**
	 * Propriedade que retorna a regular expression para ser utilizada no replace do nome da package da class para a estrutura de diretórios de destino do arquivo
	 * que será gerado
	 */
	public static final String PROP_DEST_DIR_REGEX_DESTINATION = "destDirRegexDestination";

	/**
	 * Propriedade que retorna a regular expression destination para ser utilizada no replace do nome da package da class para a estrutura de diretórios de
	 * destino do arquivo que será gerado
	 */
	public static final String PROP_DEST_DIR_REGEX_FROM = "destDirRegexFrom";

	/**
	 * Propriedade para sufixo de diretório base especificado nas regras de template. É possível especificar em qual ponto do path será feita colocada a string
	 * "tmp" para criação de arquivos temporários. ex: <br>
	 * <br>
	 * 
	 * key="propBaseDirSufix" value="web/src{0}/main/webapp": se temporário ficará como web/srctmp/main/webapp<br>
	 * key="propBaseDirSufix" value="web/src/main/webapp{0}": se temporário ficará como web/src/main/webapptmp<br>
	 * 
	 */
	public static final String PROP_BASE_DIR_SUFIX = "propBaseDirSufix";


	/**
	 * Chave de propriedade para recuperar diretório de destino para arquivos de configuração
	 */
	public static final Object PROP_DEST_DIR_CONFIG = "destDirConfig";


	/**
	 * Literal regex replacement. Segundo argumento para String.replaceAll(someRegex,FileWriterFactory.REGEX_GROUP1_REPLACEMENT);
	 */
	public static final String REGEX_GROUP1_REPLACEMENT = "$1";

	/**
	 * Diretório base para criação de arquivos
	 */
	private File baseDir;

	/**
	 * Constroi um FileWriterFactory para trabalhar a partir de um diretório base onde os arquivos serão gerados
	 */
	public FileWriterFactory(File baseDir) {
		// filewriter.creatingFactory=Criando FileWriterFactory para gerar arquivos nos diretórios a seguir: baseDir{0} e tmpBaseDir{1}
		log.warn(mf.format("filewriter.creatingFactory", baseDir.getAbsolutePath()));

		this.baseDir = baseDir;
	}

	/**
	 * Constroi um FileWriterFactory para trabalhar a partir de um diretório base onde os arquivos serão gerados
	 */
	public FileWriterFactory(String baseDir) {
		this(new File(baseDir));
	}

	/**
	 * Objetivo final desta fábrica: Implementação de IWriterFactory.createWriter. Cria FileWriter que será o writer utilizado pelo engine do gerador
	 * 
	 * @param templateRules
	 *          contém as regras específicas do template que será processado
	 * @param javaClass
	 *          meta modelo da classe que servirá de insumo para processamento do template
	 * @param temporario
	 *          determina se o writer gerado deverá apontar para um destino definitivo ou temporário, caso o arquivo de destino já exista
	 * @throws GeradorException
	 */
	@Override
	protected Writer createWriter(ITemplateRules templateRules, JavaClass javaClass, boolean temporario) throws GeradorException {

		// recupera o destDir
		File destDir = getDestDir(templateRules, javaClass, temporario);
		// lanca exceção se não obtiver sucesso
		createDestDir(destDir);

		// recupera o destfile já criado
		File destFile = getDestFile(templateRules, javaClass, destDir);
		// lanca excecao se não obtiver sucesso
		createDestFile(destFile);

		// cria FileWriter
		Writer fileWriter = createFileWriter(destFile,templateRules);

		return fileWriter;
	}

	/**
	 * Cria fileWriter para o arquivo de destino da geracao de código
	 * 
	 * @param destFile
	 * @param templateRules 
	 * @return
	 * @throws FileWriterFactoryException
	 */
	protected Writer createFileWriter(File destFile, ITemplateRules templateRules) throws FileWriterFactoryException {
		// cria FileWriter
		String outputEncoding=templateRules.getOutputEncoding();
		
		Writer w;
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(destFile);
			
			Charset charset = Charset.forName(outputEncoding);
			w = new OutputStreamWriter(fileOutputStream,charset);
		} catch (IOException e) {
			throw new FileWriterFactoryException(mf.format("filewriter.creatingWriterError", destFile), e);
		}
		return w;
	}

	/**
	 * Cria arquivo de destino para geração de código
	 * 
	 * @param destFile
	 * @throws FileWriterFactoryException
	 *           caso o arquivo não seja gerado
	 */
	protected void createDestFile(File destFile) throws FileWriterFactoryException {
		try {
			destFile.createNewFile();
		} catch (IOException e) {
			// filewriter.destFileCreationError=Erro ao tentar criar arquivo de destino {0} para geracao de codigo!
			throw new FileWriterFactoryException(mf.format("filewriter.destFileCreationError", destFile.toString()), e);
		}
	}

	/**
	 * Cria diretório de destino
	 * 
	 * @param destDir
	 */
	protected void createDestDir(File destDir) throws DestDirCreationException {
		// cria diretorio e checa se existe
		boolean dirCriados = destDir.mkdirs();
		boolean exists = destDir.exists();

		// loga sucesso ou erro na criação dos diretórios
		if (exists) {
			if (dirCriados) {
				// filewriter.baseGenDirSuccess=O diretório {0} foi criado com sucesso!
				log.debug(mf.format("filewriter.baseGenDirSuccess", destDir.toString()));

			} else {
				// filewriter.baseGenDirAlreadyExists=O diretório {0} já existe!
				log.debug(mf.format("filewriter.baseGenDirAlreadyExists", destDir.toString()));

			}
		} else {
			// filewriter.errorBaseGenNotCreater=Nao foi possivel criar o diretorio {0} dentro de {1}!
			String msgError = mf.format("filewriter.errorBaseGenNotCreater", destDir.toString());
			log.error(msgError);

			throw new DestDirCreationException(msgError);
		}
	}

	/**
	 * Recupera dest file para a geração de código. Não cria o arquivo
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @param baseGenDir
	 * @return
	 * @throws GeradorException
	 */
	protected File getDestFile(ITemplateRules templateRules, JavaClass javaClass, File destDir) throws GeradorException {
		// recupera fileName
		String fileName = templateRules.getFileName(javaClass);

		File destFile = new File(destDir, fileName);

		return destFile;
	}

	/**
	 * Recupera diretório de destino para o código que será gerado. Não cria diretório no disco
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @param temporario
	 * @return
	 * @throws GeradorException
	 */
	protected File getDestDir(ITemplateRules templateRules, JavaClass javaClass, boolean temporario) throws GeradorException {
		// Caminho do diretório base
		File baseDir = getBaseDir();

		// Coloca um sufixo no final de baseDir, caso tenha sido especificado. Exemplo src/main/java; src/main/webapp, onde "java" e "webapp" são sufixos.
		File baseDirSufix = getBaseDirSufix(baseDir, templateRules, temporario);

		// compoe caminho completo do diretório do arquivo
		String dirRelativePath = getDirRelativePath(templateRules, javaClass);
		File baseGenDir = new File(baseDirSufix, dirRelativePath);

		return baseGenDir;
	}

	/**
	 * Coloca um sufixo no final de baseDir, caso tenha sido especificado. Exemplo src/main/java; src/main/webapp, onde "java" e "webapp" são sufixos.
	 * 
	 * @param baseDir2
	 * @param templateRules
	 * @param temporario
	 * @return
	 * @throws GeradorException
	 */
	protected File getBaseDirSufix(File baseDir, ITemplateRules templateRules, boolean temporario) throws GeradorException {
		File retFile = baseDir;

		String baseDirSufix = templateRules.getProperties().get(PROP_BASE_DIR_SUFIX);

		if (!StringUtils.isEmpty(baseDirSufix)) {

			if (!baseDirSufix.matches(REGEX_POSSUI_CORINGA_TMP)) {//deve possuir {0} na string
				// filewriter.tmpreplacenotspecified=O coringa de substituição para sufixo 'tmp' não foi especificado para o bean {1} na propriedade {0} no map
				// 'properties'. Exemplo: web/src\{\0\}/main/webapp
				throw new FileWriterFactoryException(mf.format("filewriter.tmpreplacenotspecified", FileWriterFactory.PROP_BASE_DIR_SUFIX, templateRules.getName()));
			}

			if (temporario) {
				baseDirSufix = MessageFormat.format(baseDirSufix, LITERAL_STRING_TMP); //se temporário, substitui {0} por tmp
			} else {
				baseDirSufix = MessageFormat.format(baseDirSufix, StringUtils.EMPTY); //se não temporário, substitui por empty
			}
			retFile = new File(baseDir, baseDirSufix);

			// filewriter.basedirsuffixadded=O sufixo {0} foi adicionado ao baseDir {1}
			log.debug(mf.format("filewriter.basedirsuffixadded", baseDirSufix, baseDir.getAbsolutePath()));

		} else {
			// filewriter.sufixnotspecified=ATENCAO!! A propriedade {0} nao foi especificada no bean do template {1}! Desta forma não será possível determinar como
			// deve ficar o diretório temporário para geração de código!
			throw new FileWriterFactoryException(mf.format("filewriter.sufixnotspecified", FileWriterFactory.PROP_BASE_DIR_SUFIX, templateRules.getName()));
		}

		return retFile;
	}

	/**
	 * Recupera como será a estrutura de diretório que será gerada, de acordo com a package de javaClass Este path não contém o nome do arquivo no final da String
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @return
	 * @throws GeradorException
	 */
	protected String getDirRelativePath(ITemplateRules templateRules, JavaClass javaClass) throws GeradorException {
		String packageName = javaClass.getPackage().getName();

		String destDirRegexFrom = templateRules.getProperties().get(PROP_DEST_DIR_REGEX_FROM);
		String destDirRegexDestination = templateRules.getProperties().get(PROP_DEST_DIR_REGEX_DESTINATION);

		if (StringUtils.isEmpty(destDirRegexFrom) && StringUtils.isEmpty(destDirRegexDestination)) {
			// filewriter.destDirRegexNotFound=As propriedades {0} e {1} não foram especificadas para o template {2} no map properties. Verifique o arquivo de
			// configuração ApplicationContext.xml! Sem estas propriedades não é possível determinar o diretório de destino.
			throw new FileWriterFactoryException(mf.format("filewriter.destDirRegexNotFound", PROP_DEST_DIR_REGEX_FROM, PROP_DEST_DIR_REGEX_DESTINATION,
					templateRules.getName()));
		}

		String filePath = packageName.replaceAll(destDirRegexFrom, destDirRegexDestination);

		return filePath;
	}

	/**
	 * Implementação de IWriterFacotry.exists para FileWriterFactory. Verifica se o arquivo de destino já existe
	 * 
	 * @throws GeradorException
	 */
	@Override
	protected boolean exists(ITemplateRules templateRules) throws GeradorException {

		// recupera dest dir
		File destDir = getDestDir(templateRules, false);

		// recupera destFile
		File destFile = getDestFile(templateRules, destDir);

		boolean exists = destFile.exists();

		return exists;
	}

	/**
	 * Implementação de IWriterFacotry.exists para FileWriterFactory. Verifica se o arquivo de destino já existe
	 * 
	 * @throws GeradorException
	 */
	@Override
	protected boolean exists(ITemplateRules templateRules, JavaClass javaClass) throws GeradorException {

		// recupera dest dir
		File destDir = getDestDir(templateRules, javaClass, false);

		// recupera destFile
		File destFile = getDestFile(templateRules, javaClass, destDir);

		boolean exists = destFile.exists();

		return exists;
	}


	/**
	 * Implementação de IWriterFactory.getWriterConfig. Recupera Writer para arquivo de configuração
	 * 
	 */
	protected Writer createWriter(ITemplateRules templateRules, boolean temporario) throws GeradorException {

		// recupera diretorio de destino para arquivo de configuracao
		File destDirConfig = getDestDir(templateRules, temporario);

		// Cria diretório de destino
		createDestDir(destDirConfig);

		// recupera arquivo de destino
		File fileConfig = getDestFile(templateRules, destDirConfig);

		// cria arquivo de destino
		createDestFile(fileConfig);

		// cria writer para diretório de configuração
		Writer fileWriterConfig = createFileWriter(fileConfig, templateRules);

		return fileWriterConfig;
	}

	/**
	 * Recupera destFile para arquivo de configuracao
	 * 
	 * @param templateRules
	 * @param destDirConfig
	 * @return
	 * @throws GeradorException
	 */
	protected File getDestFile(ITemplateRules templateRules, File destDirConfig) throws GeradorException {
		String fileNameConfig = templateRules.getFileName();

		File fileConfig = new File(destDirConfig, fileNameConfig);
		return fileConfig;
	}

	/**
	 * Recupera dest dir para arquivo de configuração
	 * 
	 * @param templateRules
	 * @return
	 * @throws GeradorException
	 */
	protected File getDestDir(ITemplateRules templateRules, boolean temporario) throws GeradorException {

		// Caminho completo do arquivo que será gerado. Será relativo ao diretório base
		File baseDir = getBaseDir();

		// Coloca um sufixo no final de baseDir, caso tenha sido especificado. Exemplo src/main/java; src/main/webapp, onde "java" e "webapp" são sufixos.
		File baseDirSufix = getBaseDirSufix(baseDir, templateRules, temporario);

		// compoe caminho completo do diretório do arquivo
		String destDirPathConfig = templateRules.getProperties().get(FileWriterFactory.PROP_DEST_DIR_CONFIG);

		if (StringUtils.isEmpty(destDirPathConfig)) {
			// filewriter.destDirConfigNotSpecified=O diretório de destino para {0} não especificado em {1}! Verifique as configurações em ApplicationContext.xml!
			throw new FileWriterFactoryException(mf.format("filewriter.destDirConfigNotSpecified", templateRules.getName(), PROP_DEST_DIR_CONFIG));
		}

		File destDirConfig = new File(baseDirSufix, destDirPathConfig);
		return destDirConfig;
	}

	/**
	 * Recupera o diretório base padrão ou para arquivos temporários
	 * 
	 * @return the baseDir
	 */
	protected File getBaseDir() {
		return this.baseDir;
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
		} catch (IOException e) {
			this.log.warn("erro ao tentar fechar o writer!", e);
		}
	}

}
