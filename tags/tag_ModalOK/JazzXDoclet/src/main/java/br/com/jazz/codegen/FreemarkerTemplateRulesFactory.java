/**
 * 
 */
package br.com.jazz.codegen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import br.com.dlp.framework.i18n.JazzMessageFormat;
import br.com.jazz.codegen.exception.TemplateConfigurationException;
import br.com.jazz.codegen.test.ListTemplates;
import br.com.jazz.jazzwizard.exception.GeradorException;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * Abstração de configuração para freemarker templates
 * 
 * @author darcio
 * 
 */
public class FreemarkerTemplateRulesFactory extends Configuration implements ITemplateRulesFactory {
	protected Log log = LogFactory.getLog(FreemarkerTemplateRulesFactory.class);
	protected static JazzMessageFormat mf = new JazzMessageFormat("br.com.jazz.gerador.messages");
	
	// Contém lista dos templates disponíveis organizados por templatePath
	protected Map<String, List<String>> availableTemplates = new HashMap<String, List<String>>(2);

	private ApplicationContext applicationContext;

	protected static final String REGEX_ARQUIVO_EXTENSAO_FTL = "^.+\\.[fF][tT][lL]$";
	protected static final String REGEX_EXTRAIR_TXT_FTL = "\\.[fF][tT][lL]$";

	/**
	 * Constroi uma especialização de Configuration de freemarker, de acordo com as necessidades do framework gerador
	 * 
	 * @param templatePaths
	 * @throws GeradorException
	 */
	public FreemarkerTemplateRulesFactory(ApplicationContext applicationContext, String... templatePaths) throws GeradorException {
		super();
		
		this.applicationContext = applicationContext;
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("gc", "GeradorCodigoImport.ftl");
		this.setAutoImports(map);
		
		Collection<TemplateLoader> tls = new ArrayList<TemplateLoader>(2); // lista de tipos template loaders (interfaces)

		for (String path : templatePaths) {
			ClassTemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), path);

			this.listarTemplates(path);

			tls.add(templateLoader);
		}

		MultiTemplateLoader multiTemplateLoader = new MultiTemplateLoader(tls.toArray(new TemplateLoader[0]));
		this.setTemplateLoader(multiTemplateLoader);
		
	}

	/**
	 * Lista e armazena templates disponíveis no path informado. Armazena resultados num mapa this.availableTemplates(path, List)
	 * 
	 * @param path
	 * @throws GeradorException
	 */
	protected void listarTemplates(String path) throws GeradorException {
		try {
			List<String> templates = this.listDirectory(path, REGEX_ARQUIVO_EXTENSAO_FTL);
			this.availableTemplates.put(path, templates);
		} catch (IOException e) {
			throw new GeradorException("Erro ao tentar ler diretorio de templates!", e);
		}
	}

	/**
	 * Retorna array de strings representando a lista de arquivos contidos no diretorio
	 * 
	 * @param urlPath
	 *          TODO
	 * @return
	 * @throws IOException
	 */
	protected List<String> listDirectory(String urlPath, String regexName) throws IOException {
		List<String> fileNames = new ArrayList<String>(30);

		URL url = ListTemplates.class.getResource(urlPath);

		URLConnection connection = url.openConnection();

		Object obj = connection.getContent();

		InputStream inputStream = (InputStream) obj;

		InputStreamReader reader = new InputStreamReader(inputStream);

		BufferedReader bufferedReader = new BufferedReader(reader);

		String linha = bufferedReader.readLine();
		while (linha != null) {

			// testa se o arquivo lido bate com o padrao informado
			if (linha.matches(regexName)) {
				fileNames.add(linha);
			}

			linha = bufferedReader.readLine();
		}
		return fileNames;
	}

	/**
	 * @return the availableTemplates
	 */
	public Map<String, List<String>> getAvailableTemplates() {
		return availableTemplates;
	}

	/**
	 * 
	 */
	public ITemplateRules getFreemarkerTemplateRules(String templateName) throws GeradorException {
		FreemarkerTemplateRules templateRules = getTemplateRules(templateName);

		String templateEncoding = templateRules.getTemplateEncoding();
		
		Template template = getTemplateByBean(templateName,templateEncoding);
		
		templateRules.setTemplate(template);

		return templateRules;
	}

	public FreemarkerTemplateRules getTemplateRules(String templateName) throws TemplateConfigurationException {
		// Criar templaterules default ou por spring
		FreemarkerTemplateRules templateRules;

		//tira extensão freemarker, caso possua
		String beanName = getBeanName(templateName);
		if (this.getApplicationContext().containsBean(beanName)) {
			
			Object object = this.getApplicationContext().getBean(beanName);
			
			if(!(object instanceof FreemarkerTemplateRules)){
				//fmtemplateconfiguration.templateruletypeerror=A implementação de ITemplateRule referente ao bean {0}, configurado em ApplicationContext.xml não é do tipo FreemarkerTemplateRules!  
				throw new TemplateConfigurationException(mf.format("fmtemplateconfiguration.templateruletypeerror", beanName));
			}
			
			templateRules = (FreemarkerTemplateRules)object; 
		} else {
			templateRules = new FreemarkerTemplateRules();
		}
		return templateRules;
	}

	private Template getTemplateByBean(String templateName, String templateEncoding) throws GeradorException {
		//coloca extensao freemarker, caso não possua
		templateName = trataTemplateName(templateName);
		
		// Recuperar template
		Template template;
		try {
			template = getTemplate(templateName,templateEncoding);
		} catch (IOException e) {
			String message = mf.format("rulesFreemarker.templateNotFound", templateName);
			log.error(message);
			throw new GeradorException(message, e);
		}
		return template;
	}

	/**
	 * @param templateName
	 * @return
	 */
	protected String getBeanName(String templateName) {
		String beanName = templateName.replaceAll(REGEX_EXTRAIR_TXT_FTL, "");
		return beanName;
	}

	/**
	 * @param templateName
	 * @return
	 */
	protected String trataTemplateName(String templateName) {
		if(!templateName.matches(REGEX_ARQUIVO_EXTENSAO_FTL)){ //nao possui extensao ftl
			templateName = templateName + ".ftl";
		}
		return templateName;
	}

	/**
	 * @return the applicationContext
	 */
	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
