package br.com.jazz.codegen;

import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.dlp.framework.i18n.JazzMessageFormat;
import br.com.jazz.codegen.exception.FileWriterFactoryException;
import br.com.jazz.codegen.exception.OverwriteNotAllowedException;
import br.com.jazz.jazzwizard.exception.GeradorException;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * Classe Controller que orquestra a geracao de código. Por motivos de performance esta classe não é thread safe.
 * 
 * @author darcio
 */
public abstract class AbstractGeradorController implements IGeradorController {
	public static final String MAP_KEY_CLASSNAME = "className";
	public static final String MAP_KEY_DOMAIN = "domain";
	public static final String MAP_KEY_DOMAINNAME = "domainName";
	public static final String MAP_KEY_DOMAINS = "domains";
	
	public static final String MAP_KEY_ENV = "env";
	public static final String MAP_KEY_MODELLOADER = "modelLoader";
	public static final String MAP_KEY_RULES = "rules";
	
	public static final String MAP_KEY_WRITER_FACTORY = "writerFactory";
	
	public static final String MAP_KEY_RULES_FACTORY = "rulesFactory";
	public static final String MAP_KEY_RULES_NAME = "rulesName";
	public static final String MAP_KEY_ANNOTATION_ALIASES = "aliasdlp";
	protected static JazzMessageFormat mf = new JazzMessageFormat("br.com.jazz.gerador.messages");
	public static final String USER_ENV_VAR_LIN = "USER";
	public static final String USER_ENV_VAR_WIN = "USERNAME";
	protected Log log = LogFactory.getLog(AbstractGeradorController.class);
	
	// wrapper para leitura de metamodelo pelo QDox
	public IDomainModelLoaderFactory modelLoader;
	
	// objeto de configuração do FreeMarker
	public ITemplateRulesFactory templateRulesFactory;
	
	// Contém regras para criação de writers que servirá de saída para a geracao de codigo
	public IWriterFactory writerFactory;
	
	/**
	 * Constroi o controller do gerador de código
	 * 
	 * @param applicationContext
	 * @param templateRulesFactory
	 * @param domainModelLoader
	 * @param writerFactory
	 * @throws GeradorException
	 */
	public AbstractGeradorController(final ITemplateRulesFactory templateRulesFactory, final IDomainModelLoaderFactory modelLoaderFactory,
			final IWriterFactory writerFactory) throws GeradorException {
		
		this.templateRulesFactory = templateRulesFactory;
		modelLoader = modelLoaderFactory;
		this.writerFactory = writerFactory;
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateConfigFile(java.lang.String)
	 */
	public void generateConfigFile() throws GeradorException {
		final Map<String, List<String>> map = templateRulesFactory.getAvailableTemplates();
		final List<String> templates = map.get("/br/com/jazz/gerador/templates/group");
		this.generateConfigFile(templates);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateConfigFile(java.lang.String)
	 */
	public void generateConfigFile(final ITemplateRules... templateRules) throws GeradorException {
		for (final ITemplateRules iTemplateRule : templateRules) {
			this.generateConfigFile(iTemplateRule);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateConfigFile(java.lang.String)
	 */
	public void generateConfigFile(final ITemplateRules templateRules) throws GeradorException {
		
		// recupera o writer padrão da implementação concreta
		Writer writer;
		try {
			writer = writerFactory.getWriterConfig(templateRules);
		} catch (final OverwriteNotAllowedException e) {
			log.warn(AbstractGeradorController.mf.format("abstractcontroller.nothingtodo", "<JavaClasses>", templateRules.getName()));
			return;
		}
		
		final Map<String, Object> map = this.getProcessMap(templateRules, writerFactory);
		
		templateRules.process(map, writer);
		
		// finaliza o writer da implementação concreta
		writerFactory.finalizeConfigFileWriter(writer);
		
		log.warn(AbstractGeradorController.mf.format("abstractcontroller.successfully.config.generated", templateRules.getName()));
		
	}
	
	/**
	 * Gera arquivos de configuracao a partir dos templates informados para todas as entidades identificadas.
	 * 
	 * @param templates
	 * @throws GeradorException
	 */
	public void generateConfigFile(final List<String> templates) throws GeradorException {
		for (final String template : templates) {
			this.generateConfigFile(template);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateConfigFile(java.lang.String)
	 */
	public void generateConfigFile(final String templateName) throws GeradorException {
		
		final ITemplateRules templateRules = templateRulesFactory.getTemplateRules(templateName);
		
		this.generateConfigFile(templateRules);
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateConfigFile(java.lang.String)
	 */
	public void generateConfigFile(final String... templateNames) throws GeradorException {
		for (final String templateName : templateNames) {
			this.generateConfigFile(templateName);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateFile(java.lang.String)
	 */
	public void generateFile(final ITemplateRules... templateRuless) throws GeradorException {
		for (final ITemplateRules templateRules : templateRuless) {
			final Collection<JavaClass> javaClasses = getDomainList().values();
			for (final JavaClass javaClass : javaClasses) {
				if (templateRules.isGenerate(javaClass)) {
					this.generateFile(javaClass, templateRules);
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateFile(com.thoughtworks.qdox.model.JavaClass, br.com.jazz.codegen.ITemplateRules)
	 */
	public void generateFile(final JavaClass javaClass, final ITemplateRules... templateRuless) throws GeradorException {
		
		for (final ITemplateRules templateRules : templateRuless) {
			
			if (templateRules.isGenerate(javaClass)) {
				
				// recupera o writer da implementação concreta
				Writer writer;
				try {
					writer = writerFactory.getWriter(templateRules, javaClass);
				} catch (final OverwriteNotAllowedException e) {
					log.warn(AbstractGeradorController.mf.format("abstractcontroller.nothingtodo", javaClass.getName(), templateRules.getName()));
					return;
				}
				
				// cria map com a entidade única
				final Map<String, Object> map = this.getProcessMap(javaClass, templateRules, writerFactory);
				
				templateRules.process(map, writer);
				
				// finaliza o writer da implementação concreta
				writerFactory.finalizeFileWriter(writer);
				
				log.warn(AbstractGeradorController.mf.format("abstractcontroller.successfully.generated", javaClass.getName(), templateRules
						.getName()));
				
			}
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateFile(com.thoughtworks.qdox.model.JavaClass, br.com.jazz.codegen.ITemplateRules)
	 */
	public void generateFile(final JavaClass javaClass, final String... templateNames) throws GeradorException {
		
		if (javaClass == null) {
			throw new IllegalArgumentException("Error javaClass==null! Informe uma classe de entidade para geracao de código!",
					new NullPointerException("Error javaClass==null! Informe uma classe de entidade para geracao de código!"));
		}
		if (templateNames == null) {
			throw new IllegalArgumentException("Error templateNames==null! Informe os nomes dos templates a serem processados!",
					new NullPointerException("Error templateNames==null!"));
		}
		
		for (final String templateName : templateNames) {
			final ITemplateRules templateRules = templateRulesFactory.getTemplateRules(templateName);
			this.generateFile(javaClass, templateRules);
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateFile(com.thoughtworks.qdox.model.JavaClass, br.com.jazz.codegen.ITemplateRules)
	 */
	public void generateFile(final JavaClass[] javaClasses, final String... templateNames) throws GeradorException {
		for (final JavaClass javaClass : javaClasses) {
			this.generateFile(javaClass, templateNames);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateFile(java.lang.String)
	 */
	public void generateFile(final String... templateNames) throws GeradorException {
		for (final String templateName : templateNames) {
			final ITemplateRules templateRules = templateRulesFactory.getTemplateRules(templateName);
			this.generateFile(templateRules);
		}
	}
	
	/**
	 * Determina que será o classname do arquivo gerado. Se não for java, retorna o nome do arquivo sem extensão.
	 * 
	 * @param javaClass
	 * @param templateRules
	 * @return
	 * @throws FileWriterFactoryException
	 */
	protected String getClassName(final JavaClass javaClass, final ITemplateRules templateRules) throws FileWriterFactoryException {
		String className;
		
		try {
			if (javaClass == null) {
				className = templateRules.getFileName();
			} else {
				className = templateRules.getFileName(javaClass);
			}
		} catch (final GeradorException e) {
			throw new FileWriterFactoryException("Erro: Não foi possivel determinar o nome do arquivo", e);
		}
		
		// Corrige o nome retirando a extensão
		if (className != null) {
			className = className.split("\\.")[0];
		}
		
		return className;
	}
	
	/**
	 * Lista as entidades do modelo de domínio pesquisado
	 * 
	 * @return
	 */
	protected Map<String, JavaClass> getDomainList() {
		return modelLoader.getDomainMap();
	}
	
	public IDomainModelLoaderFactory getModelLoader() {
		return modelLoader;
	}
	
	/**
	 * Recupera map de argumentos para gerador
	 * 
	 * @param templateRules
	 * @param writerFactory2
	 * @return
	 * @throws GeradorException
	 */
	protected Map<String, Object> getProcessMap(final ITemplateRules templateRules, IWriterFactory writerFactory) throws GeradorException {
		return this.getProcessMap(null, templateRules, writerFactory);
	}
	
	/**
	 * Retorna o mapa de vari�veis para processamento de templates.
	 * 
	 * @param javaClass
	 * @param templateRules
	 * @return
	 * @throws GeradorException
	 */
	protected Map<String, Object> getProcessMap(final JavaClass javaClass, final ITemplateRules templateRules, IWriterFactory writerFactory)
			throws GeradorException {
		final Map<String, Object> map = new HashMap<String, Object>();
		
		final Map<String, JavaClass> list = getDomainList();
		
		final String className = getClassName(javaClass, templateRules);
		
		final String domainName = templateRules.getDomainName(javaClass);
		
		map.put(AbstractGeradorController.MAP_KEY_CLASSNAME, className);
		map.put(AbstractGeradorController.MAP_KEY_DOMAINNAME, domainName);
		map.put(AbstractGeradorController.MAP_KEY_MODELLOADER, modelLoader);
		map.put(AbstractGeradorController.MAP_KEY_DOMAINS, list.values());
		map.put(AbstractGeradorController.MAP_KEY_DOMAIN, javaClass);
		map.put(AbstractGeradorController.MAP_KEY_WRITER_FACTORY, writerFactory);
		
		map.put(AbstractGeradorController.MAP_KEY_RULES, templateRules);
		map.put(AbstractGeradorController.MAP_KEY_RULES_NAME, templateRules.getName());
		map.put(AbstractGeradorController.MAP_KEY_ENV, getSOEnvironmentVars());
		
		map.put(AbstractGeradorController.MAP_KEY_RULES_FACTORY, templateRulesFactory);
		
		map.put(AbstractGeradorController.MAP_KEY_ANNOTATION_ALIASES, getAnnotationAliases());
		//
		
		return map;
	}
	
	/**
	 * Recupera um mapa contendo os apelidos das annotations
	 * 
	 * @return
	 */
	protected abstract Map<String, String> getAnnotationAliases();
	
	/**
	 * Retorna o mapa de vari�veis de ambiente. <br>
	 * <br>
	 * Corrige a diverg�ncia entre o nome da vari�vel de ambiente que guarda o nome do usu�rio da sessao.
	 * 
	 * @return
	 */
	protected Map<String, String> getSOEnvironmentVars() {
		final Map<String, String> soEnv = System.getenv();
		final Map<String, String> soEnvCopy = new HashMap<String, String>();
		
		soEnvCopy.putAll(soEnv);
		
		final String userWin = soEnv.get(AbstractGeradorController.USER_ENV_VAR_WIN);
		final String userLin = soEnv.get(AbstractGeradorController.USER_ENV_VAR_LIN);
		
		if (!StringUtils.isBlank(userWin)) {
			// ESTOU NUM SO WIN
			soEnvCopy.put(AbstractGeradorController.USER_ENV_VAR_LIN, userWin);
		} else if (!StringUtils.isBlank(userLin)) {
			// ESTOU NUM SO LINUX/UNIX
			soEnvCopy.put(AbstractGeradorController.USER_ENV_VAR_WIN, userLin);
		}
		
		return soEnvCopy;
	}
	
	public ITemplateRulesFactory getTemplateRulesFactory() {
		return templateRulesFactory;
	}
	
	public IWriterFactory getWriterFactory() {
		return writerFactory;
	}
	
	public void setModelLoader(final IDomainModelLoaderFactory modelLoader) {
		this.modelLoader = modelLoader;
	}
	
	public void setTemplateRulesFactory(final ITemplateRulesFactory templateRulesFactory) {
		this.templateRulesFactory = templateRulesFactory;
	}
	
	public void setWriterFactory(final IWriterFactory writerFactory) {
		this.writerFactory = writerFactory;
	}
	
}
