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
	public static final String USER_ENV_VAR_LIN = "USER";
	public static final String USER_ENV_VAR_WIN = "USERNAME";
	protected Log log = LogFactory.getLog(AbstractGeradorController.class);
	protected static JazzMessageFormat  mf = new JazzMessageFormat("br.com.jazz.gerador.messages");

	public static final String MAP_KEY_DOMAINS = "domains";
	public static final String MAP_KEY_RULES = "rules";
	public static final String MAP_KEY_DOMAIN = "domain";
	public static final String MAP_KEY_CLASSNAME = "className";
	public static final String MAP_KEY_ENV = "env";
	public static final String MAP_KEY_DOMAINNAME = "domainName";
	public static final String MAP_KEY_RULES_FACTORY = "rulesFactory";
	public static final String MAP_KEY_RULES_NAME = "rulesName";
	public static final String MAP_KEY_MODELLOADER = "modelLoader";

	// objeto de configuração do FreeMarker
	public ITemplateRulesFactory templateRulesFactory;

	// wrapper para leitura de metamodelo pelo QDox
	public IDomainModelLoaderFactory modelLoader;

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
	public AbstractGeradorController(ITemplateRulesFactory templateRulesFactory, IDomainModelLoaderFactory modelLoaderFactory, IWriterFactory writerFactory) throws GeradorException {
		
		this.templateRulesFactory = templateRulesFactory;
		this.modelLoader = modelLoaderFactory;
		this.writerFactory = writerFactory;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateFile(com.thoughtworks.qdox.model.JavaClass, br.com.jazz.codegen.ITemplateRules)
	 */
	public void generateFile(JavaClass javaClass, ITemplateRules... templateRuless) throws GeradorException {

		for (ITemplateRules templateRules : templateRuless) {

			if (templateRules.isGenerate(javaClass)) {

				// recupera o writer da implementação concreta
				Writer writer;
				try {
					writer = this.writerFactory.getWriter(templateRules, javaClass);
				} catch (OverwriteNotAllowedException e) {
					log.warn(mf.format("abstractcontroller.nothingtodo", javaClass.getName(), templateRules.getName()));
					return;
				}
				
				// cria map com a entidade única
				Map<String, Object> map = getProcessMap(javaClass, templateRules);

				templateRules.process(map, writer);

				// finaliza o writer da implementação concreta
				this.writerFactory.finalizeFileWriter(writer);
				
				log.warn(mf.format("abstractcontroller.successfully.generated",javaClass.getName(), templateRules.getName()));

				
			}
		}

	}

	/**
	 * Retorna o mapa de vari�veis para processamento de templates.
	 * @param javaClass
	 * @param templateRules
	 * @return
	 * @throws GeradorException
	 */
	protected Map<String, Object> getProcessMap(JavaClass javaClass, ITemplateRules templateRules) throws GeradorException {
		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, JavaClass> list = getDomainList();

		String className = getClassName(javaClass, templateRules);

		String domainName = templateRules.getDomainName(javaClass);
		
		map.put(MAP_KEY_CLASSNAME, 	className);
		map.put(MAP_KEY_DOMAINNAME, domainName);
		map.put(MAP_KEY_MODELLOADER, this.modelLoader);
		map.put(MAP_KEY_DOMAINS, 		list.values());
		map.put(MAP_KEY_DOMAIN, 		javaClass);
		map.put(MAP_KEY_RULES, 			templateRules);
		map.put(MAP_KEY_RULES_NAME, templateRules.getName());
		map.put(MAP_KEY_ENV, 				getSOEnvironmentVars());

		map.put(MAP_KEY_RULES_FACTORY, 				this.templateRulesFactory);
		
		//


		return map;
	}

	/**
	 * Retorna o mapa de vari�veis de ambiente. <br><br> 
	 * Corrige a diverg�ncia entre o nome da vari�vel de ambiente que guarda o nome do usu�rio da sessao. 
	 * @return
	 */
	protected Map<String, String> getSOEnvironmentVars() {
		Map<String, String> soEnv = System.getenv();
		Map<String, String> soEnvCopy = new HashMap<String, String>();

		soEnvCopy.putAll(soEnv);
		
		String userWin = soEnv.get(AbstractGeradorController.USER_ENV_VAR_WIN);
		String userLin = soEnv.get(AbstractGeradorController.USER_ENV_VAR_LIN);
		
		if(!StringUtils.isBlank(userWin)){
			//ESTOU NUM SO WIN
			soEnvCopy.put(AbstractGeradorController.USER_ENV_VAR_LIN, userWin);
		}else if(!StringUtils.isBlank(userLin)){
			//ESTOU NUM SO LINUX/UNIX
			soEnvCopy.put(AbstractGeradorController.USER_ENV_VAR_WIN, userLin);
		}
		
		return soEnvCopy;
	}

	/**
	 * Determina que será o classname do arquivo gerado. Se não for java, retorna o nome do arquivo sem extensão.
	 * 
	 * @param javaClass
	 * @param templateRules
	 * @return
	 * @throws FileWriterFactoryException
	 */
	protected String getClassName(JavaClass javaClass, ITemplateRules templateRules) throws FileWriterFactoryException {
		String className;

		try {
			if (javaClass == null) {
				className = templateRules.getFileName();
			} else {
				className = templateRules.getFileName(javaClass);
			}
		} catch (GeradorException e) {
			throw new FileWriterFactoryException("Erro: Não foi possivel determinar o nome do arquivo", e);
		}

		// Corrige o nome retirando a extensão
		if (className != null)
			className = className.split("\\.")[0];

		return className;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateFile(com.thoughtworks.qdox.model.JavaClass, br.com.jazz.codegen.ITemplateRules)
	 */
	public void generateFile(JavaClass javaClass, String... templateNames) throws GeradorException {

		if (javaClass == null) {
			throw new IllegalArgumentException("Error javaClass==null! Informe uma classe de entidade para geracao de código!", new NullPointerException("Error javaClass==null! Informe uma classe de entidade para geracao de código!"));
		}
		if (templateNames == null){
			throw new IllegalArgumentException("Error templateNames==null! Informe os nomes dos templates a serem processados!", new NullPointerException("Error templateNames==null!"));
		}

		for (String templateName : templateNames) {
			ITemplateRules templateRules = this.templateRulesFactory.getFreemarkerTemplateRules(templateName);
			this.generateFile(javaClass, templateRules);
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateFile(java.lang.String)
	 */
	public void generateFile(String... templateNames) throws GeradorException {
		for (String templateName : templateNames) {
			ITemplateRules templateRules = this.templateRulesFactory.getFreemarkerTemplateRules(templateName);
			this.generateFile(templateRules);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateFile(java.lang.String)
	 */
	public void generateFile(ITemplateRules... templateRuless) throws GeradorException {
		for (ITemplateRules templateRules : templateRuless) {
			Collection<JavaClass> javaClasses = getDomainList().values();
			for (JavaClass javaClass : javaClasses) {
				if (templateRules.isGenerate(javaClass)) {
					generateFile(javaClass, templateRules);
				}
			}
		}
	}

	/**
	 * Lista as entidades do modelo de domínio pesquisado
	 * 
	 * @return
	 */
	protected Map<String, JavaClass> getDomainList() {
		return this.modelLoader.listDomain();
	}

	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateConfigFile(java.lang.String)
	 */
	public void generateConfigFile(String templateName) throws GeradorException {
	
		ITemplateRules templateRules = this.templateRulesFactory.getFreemarkerTemplateRules(templateName);
		
		this.generateConfigFile(templateRules);
		
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateConfigFile(java.lang.String)
	 */
	public void generateConfigFile(String... templateNames) throws GeradorException {
		for (String templateName : templateNames) {
			generateConfigFile(templateName);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateConfigFile(java.lang.String)
	 */
	public void generateConfigFile() throws GeradorException {
		 Map<String, List<String>> map = this.templateRulesFactory.getAvailableTemplates();
		 List<String> templates = map.get("/br/com/jazz/gerador/templates/group");
		 generateConfigFile(templates);
	}
	
	/**
	 * Gera arquivos de configuracao a partir dos templates informados para todas as entidades identificadas. 
	 * @param templates
	 * @throws GeradorException
	 */
	public void generateConfigFile(List<String> templates) throws GeradorException {
		for (String template : templates) {
			generateConfigFile(template);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateConfigFile(java.lang.String)
	 */
	public void generateConfigFile(ITemplateRules... templateRules) throws GeradorException {
		for (ITemplateRules iTemplateRule : templateRules) {
			generateConfigFile(iTemplateRule);
		}
	}
	
		
		
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IGeradorController#generateConfigFile(java.lang.String)
	 */
	public void generateConfigFile(ITemplateRules templateRules) throws GeradorException {
			
		// recupera o writer padrão da implementação concreta
		Writer writer;
		try {
			writer = this.writerFactory.getWriterConfig(templateRules);
		} catch (OverwriteNotAllowedException e) {
			log.warn(mf.format("abstractcontroller.nothingtodo", "<JavaClasses>", templateRules.getName()));
			return;
		}

		Map<String, Object> map = getProcessMap(templateRules);

		templateRules.process(map, writer);

		// finaliza o writer da implementação concreta
		this.writerFactory.finalizeConfigFileWriter(writer);

		log.warn(mf.format("abstractcontroller.successfully.config.generated.",templateRules.getName()));
		
	}

	/**
	 * Recupera map de argumentos para gerador
	 * 
	 * @param templateRules
	 * @return
	 * @throws GeradorException
	 */
	protected Map<String, Object> getProcessMap(ITemplateRules templateRules) throws GeradorException {
		return this.getProcessMap(null, templateRules);
	}

	public ITemplateRulesFactory getTemplateRulesFactory() {
		return templateRulesFactory;
	}

	public void setTemplateRulesFactory(ITemplateRulesFactory templateRulesFactory) {
		this.templateRulesFactory = templateRulesFactory;
	}

	public IDomainModelLoaderFactory getModelLoader() {
		return modelLoader;
	}

	public void setModelLoader(IDomainModelLoaderFactory modelLoader) {
		this.modelLoader = modelLoader;
	}

	public IWriterFactory getWriterFactory() {
		return writerFactory;
	}

	public void setWriterFactory(IWriterFactory writerFactory) {
		this.writerFactory = writerFactory;
	}

}
