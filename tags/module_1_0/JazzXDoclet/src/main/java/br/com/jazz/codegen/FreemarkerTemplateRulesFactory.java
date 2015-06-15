/**
 * 
 */
package br.com.jazz.codegen;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
	protected static JazzMessageFormat mf = new JazzMessageFormat("br.com.jazz.gerador.messages");
	protected static final String REGEX_ARQUIVO_EXTENSAO_FTL = "^.+\\.[fF][tT][lL]$";
	
	protected static final String REGEX_EXTRAIR_TXT_FTL = "\\.[fF][tT][lL]$";
	
	private final ApplicationContext applicationContext;
	
	// Contém lista dos templates disponíveis organizados por templatePath
	protected Map<String, List<String>> availableTemplates = new HashMap<String, List<String>>(2);
	protected Log log = LogFactory.getLog(FreemarkerTemplateRulesFactory.class);
	
	/**
	 * Constroi uma especialização de Configuration de freemarker, de acordo com as necessidades do framework gerador
	 * 
	 * @param templatePaths
	 * @throws GeradorException
	 */
	public FreemarkerTemplateRulesFactory(final ApplicationContext applicationContext, final String... templatePaths) throws GeradorException {
		super();
		
		this.applicationContext = applicationContext;
		
		final Map<String, String> map = new HashMap<String, String>();
		map.put("gc", "GeradorCodigoImport.ftl");
		setAutoImports(map);
		
		final Collection<TemplateLoader> tls = new ArrayList<TemplateLoader>(2); // lista de tipos template loaders (interfaces)
		
		for (final String path : templatePaths) {
			final ClassTemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), path);
			
			listarTemplates(path);
			
			tls.add(templateLoader);
		}
		
		final MultiTemplateLoader multiTemplateLoader = new MultiTemplateLoader(tls.toArray(new TemplateLoader[0]));
		setTemplateLoader(multiTemplateLoader);
		
	}
	
	/**
	 * @return the applicationContext
	 */
	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	/**
	 * @return the availableTemplates
	 */
	public Map<String, List<String>> getAvailableTemplates() {
		return availableTemplates;
	}
	
	/**
	 * @param templateName
	 * @return
	 */
	protected String getBeanName(final String templateName) {
		final String beanName = templateName.replaceAll(FreemarkerTemplateRulesFactory.REGEX_EXTRAIR_TXT_FTL, "");
		return beanName;
	}
	
	/**
	 * 
	 */
	public ITemplateRules getTemplateRules(final String templateName) throws GeradorException {
		final FreemarkerTemplateRules templateRules = getFreeMarkerTemplateRules(templateName);
		
		final String templateEncoding = templateRules.getTemplateEncoding();
		
		final Template template = getTemplateByBean(templateName, templateEncoding);
		
		templateRules.setTemplate(template);
		
		return templateRules;
	}
	
	private Template getTemplateByBean(String templateName, final String templateEncoding) throws GeradorException {
		// coloca extensao freemarker, caso não possua
		templateName = trataTemplateName(templateName);
		
		// Recuperar template
		Template template;
		try {
			template = this.getTemplate(templateName, templateEncoding);
		} catch (final FileNotFoundException e) {
			final String message = FreemarkerTemplateRulesFactory.mf.format("rulesFreemarker.templateNotFound", templateName);
			log.warn(message);
			template = null;
		} catch (final IOException e) {
			final String message = FreemarkerTemplateRulesFactory.mf.format("rulesFreemarker.templateNotFound", templateName);
			log.error(message);
			throw new GeradorException(message, e);
		}
		return template;
	}
	
	public FreemarkerTemplateRules getFreeMarkerTemplateRules(final String templateName) throws TemplateConfigurationException {
		// Criar templaterules default ou por spring
		FreemarkerTemplateRules templateRules;
		
		// tira extensão freemarker, caso possua
		final String beanName = getBeanName(templateName);
		if (getApplicationContext().containsBean(beanName)) {
			
			final Object object = getApplicationContext().getBean(beanName);
			
			if (!(object instanceof FreemarkerTemplateRules)) {
				// fmtemplateconfiguration.templateruletypeerror=A implementação de ITemplateRule referente ao bean {0}, configurado em
				// ApplicationContext.xml não é do tipo FreemarkerTemplateRules!
				throw new TemplateConfigurationException(FreemarkerTemplateRulesFactory.mf.format("fmtemplateconfiguration.templateruletypeerror",
						beanName));
			}
			
			templateRules = (FreemarkerTemplateRules) object;
		} else {
			templateRules = new FreemarkerTemplateRules();
		}
		return templateRules;
	}
	
	/**
	 * Lista e armazena templates disponíveis no path informado. Armazena resultados num mapa this.availableTemplates(path, List)
	 * 
	 * @param path
	 * @throws GeradorException
	 */
	protected void listarTemplates(final String path) throws GeradorException {
		try {
			final List<String> templates = listDirectory(path, FreemarkerTemplateRulesFactory.REGEX_ARQUIVO_EXTENSAO_FTL);
			availableTemplates.put(path, templates);
		} catch (final IOException e) {
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
	protected List<String> listDirectory(final String urlPath, final String regexName) throws IOException {
		final List<String> fileNames = new ArrayList<String>(30);
		
		final URL url = ListTemplates.class.getResource(urlPath);
		
		final URLConnection connection = url.openConnection();
		
		final Object obj = connection.getContent();
		
		final InputStream inputStream = (InputStream) obj;
		
		final InputStreamReader reader = new InputStreamReader(inputStream);
		
		final BufferedReader bufferedReader = new BufferedReader(reader);
		
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
	 * @param templateName
	 * @return
	 */
	protected String trataTemplateName(String templateName) {
		if (!templateName.matches(FreemarkerTemplateRulesFactory.REGEX_ARQUIVO_EXTENSAO_FTL)) { // nao possui extensao ftl
			templateName = templateName + ".ftl";
		}
		return templateName;
	}
	
}
