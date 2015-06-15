/**
 * 
 */
package br.com.jazz.codegen;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.jazz.jazzwizard.exception.GeradorException;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * ImplementaÁ„o simples de AbstractGeradorController para testes e debug
 * 
 * @author darcio
 * TODO: permitir marcar vo com fileExistingAction
 */
public class SimpleGeradorController extends AbstractGeradorController {
private static final Logger log = LoggerFactory.getLogger(SimpleGeradorController.class);
	/**
	 * Cria uma implementaÁ„o de IDomainModelLoaderFactory
	 * 
	 * @return
	 */
	public static IDomainModelLoaderFactory createDomainLoaderFactory() {
		// Respons·vel por carregar o modelo de dom√≠nio para geraÁ„o.
		// Se for necess·rio trabalhar com tags annotation de supertipos das classes de dominio,
		// o diretÛrio fonte desdes deve ser inclu√≠do
		final IDomainModelLoaderFactory domainModelLoader = new DomainModelLoaderFactory("^.+VO$", new String[] {
				"../../JazzOMR/core/src/main/java", "../../JazzFramework/src/main/java", });
		
		SimpleGeradorController.showAvailableClasses(domainModelLoader);
		
		return domainModelLoader;
	}
	
	/**
	 * Cria implementaÁ„o de ITemplateRulesFactory
	 * 
	 * @return
	 * @throws BeansException
	 * @throws GeradorException
	 */
	public static ITemplateRulesFactory createTemplateRulesFactory() throws BeansException, GeradorException {
		// ConfiguraÁ„o do spring para o configurador de templates
		final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
		"/br/com/jazz/gerador/templates/ApplicationContext.xml");
		// respons·vel por encontrar e listar os templates dispon√≠veis
		final ITemplateRulesFactory templateConfiguration = new FreemarkerTemplateRulesFactory(applicationContext,
				"/br/com/jazz/gerador/templates/imports", "/br/com/jazz/gerador/templates/group", "/br/com/jazz/gerador/templates/single");
		
		SimpleGeradorController.showAvailableTemplates(templateConfiguration);
		
		return templateConfiguration;
	}
	
	/**
	 * @return
	 * @return
	 */
	public static FileWriterFactory createWriterFactory() {
		return new FileWriterFactory("./../");
	}
	
	/**
	 * 
	 * @param args
	 * @throws GeradorException
	 * @throws IOException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void main(final String[] args) throws GeradorException, IOException, SecurityException, IllegalArgumentException,
	ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		
		final ITemplateRulesFactory 			templateRulesFactory = SimpleGeradorController.createTemplateRulesFactory();
		final IDomainModelLoaderFactory 		domainModelLoaderFactory = SimpleGeradorController.createDomainLoaderFactory();
		final IWriterFactory 					writerFactory = SimpleGeradorController.createWriterFactory();
		final IGeradorController 				controller = new SimpleGeradorController(templateRulesFactory, domainModelLoaderFactory, writerFactory);
		
		
		controller.getModelLoader().getDomainMap();
		
		// gera todos os arquivos de configuracao
		/*
		List<String> templates = new ArrayList<String>(); templates.add("ConfigMenu"); templates.add("faces-config");
		controller.generateConfigFile(templates);
		 */
		Map<String, JavaClass> javaClasses = domainModelLoaderFactory.getDomainMap();
		
		
		
		controller.generateFile(
				new JavaClass[] {
						javaClasses.get("br.com.dlp.jazzomr.empresa.EmpresaVO")
				}
				, "Business.ftl"
				, "BusinessImpl.ftl"
				, "HibernateDAOImpl.ftl"
				, "JSFBean.ftl"
				, "DomainBundle.ftl"
				, "RichFacesJSP.ftl"
				, "DAO.ftl"
				, "JSFBean.ftl"
				, "RichFacesJSP.ftl"
		);
		
		/*
		controller.generateFile(
				new JavaClass[] {
						javaClasses.get("br.com.dlp.jazzomr.tiporequisito.TipoRequisitoVO")
				}
				,"Business.ftl"
				,"DomainBundle.ftl"
				,"BusinessImpl.ftl"
				,"HibernateDAOImpl.ftl"
				,"RichFacesJSP.ftl"
				,"JSFBean.ftl"
				,"DAO.ftl"
		);
		 */
		
		//controller.generateConfigFile();
		
	}
	
	/**
	 * Apenas lista classes dispon√≠veis
	 * 
	 * @param domainModelLoader
	 */
	protected static void showAvailableClasses(final IDomainModelLoaderFactory domainModelLoader) {
		// lista todos as entidades de dominio dispon√≠veis
		final Map<String, JavaClass> javaClasses = domainModelLoader.getDomainMap();
		
		log.debug("************************** CLASSES DISPONIVEIS **************************************");
		log.debug(("" + javaClasses.keySet()).replaceAll(",", "\n"));
		log.debug("***************************************************************************************");
	}
	
	/**
	 * Lista templates dispon√≠veis
	 * 
	 * @param templateConfiguration
	 */
	protected static void showAvailableTemplates(final ITemplateRulesFactory templateConfiguration) {
		// lista todos os templates dispon√≠veis
		final Map<String, List<String>> map = templateConfiguration.getAvailableTemplates();
		
		log.debug("************************** TEMPLATES DISPONIVEIS **************************************");
		log.debug(("templates group \n" + map.get("/br/com/jazz/gerador/templates/group")).replaceAll(",", "\n"));
		log.debug("  ");
		log.debug(("templates single \n" + map.get("/br/com/jazz/gerador/templates/single")).replaceAll(",", "\n"));
		log.debug("***************************************************************************************");
	}
	
	public SimpleGeradorController(final ITemplateRulesFactory templateRulesFactory, final IDomainModelLoaderFactory modelLoaderFactory,
			final IWriterFactory writerFactory) throws GeradorException {
		super(templateRulesFactory, modelLoaderFactory, writerFactory);
	}
	
	@Override
	protected Map<String, String> getAnnotationAliases() {
		Map<String, String> annotationAliases = new HashMap<String, String>();
		
		annotationAliases.put("JazzClass", "br.com.jazz.codegen.annotation.JazzClass");
		annotationAliases.put("JazzProp", "br.com.jazz.codegen.annotation.JazzProp");
		annotationAliases.put("JazzTextArea", "br.com.jazz.codegen.annotation.JazzTextArea");
		
		return annotationAliases;
	}
}
