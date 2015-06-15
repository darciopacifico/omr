/**
 * 
 */
package br.com.jazz.codegen;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.jazz.jazzwizard.exception.GeradorException;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * Implementação simples de AbstractGeradorController para testes e debug
 * 
 * @author darcio
 * 
 */
public class SimpleGeradorController extends AbstractGeradorController {
	
	/**
	 * Cria uma implementação de IDomainModelLoaderFactory
	 * 
	 * @return
	 */
	public static IDomainModelLoaderFactory createDomainLoaderFactory() {
		// Responsável por carregar o modelo de domínio para geração.
		// Se for necessário trabalhar com tags annotation de supertipos das classes de dominio,
		// o diretório fonte desdes deve ser incluído
		final IDomainModelLoaderFactory domainModelLoader = new DomainModelLoaderFactory("^.+VO$", new String[] {
				"../../JazzQA/core/src/main/java", "../../JazzFramework/src/main/java", });
		
		SimpleGeradorController.showAvailableClasses(domainModelLoader);
		
		return domainModelLoader;
	}
	
	/**
	 * Cria implementação de ITemplateRulesFactory
	 * 
	 * @return
	 * @throws BeansException
	 * @throws GeradorException
	 */
	public static ITemplateRulesFactory createTemplateRulesFactory() throws BeansException, GeradorException {
		// Configuração do spring para o configurador de templates
		final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
		"/br/com/jazz/gerador/templates/ApplicationContext.xml");
		// responsável por encontrar e listar os templates disponíveis
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
		
		final ITemplateRulesFactory templateRulesFactory = SimpleGeradorController.createTemplateRulesFactory();
		final IDomainModelLoaderFactory domainModelLoaderFactory = SimpleGeradorController.createDomainLoaderFactory();
		final IWriterFactory writerFactory = SimpleGeradorController.createWriterFactory();
		
		final IGeradorController controller = new SimpleGeradorController(templateRulesFactory, domainModelLoaderFactory, writerFactory);
		
		Map<String, JavaClass> javaClasses = controller.getModelLoader().listDomain();
		
		// gera todos os arquivos de configuracao
		List<String> templates = new ArrayList<String>();
		templates.add("ConfigMenu");
		templates.add("faces-config");
		controller.generateConfigFile(templates);
		
		controller.generateFile(new JavaClass[] {
				javaClasses.get("br.com.dlp.jazzqa.tiporequisito.TipoRequisitoVO"),
				javaClasses.get("br.com.dlp.jazzqa.produto.ProdutoVO"),
				javaClasses.get("br.com.dlp.jazzqa.status.StatusVO"),
				javaClasses.get("br.com.dlp.jazzqa.usuariojazz.UsuarioJazzVO") },
				"Business.ftl",
				"DomainBundle.ftl",
				"BusinessImpl.ftl",
				"HibernateDAOImpl.ftl",
				"RichFacesJSP.ftl",
				"JSFBean.ftl",
				"DAO.ftl"
				
		);
		
	}
	
	/**
	 * Apenas lista classes disponíveis
	 * 
	 * @param domainModelLoader
	 */
	protected static void showAvailableClasses(final IDomainModelLoaderFactory domainModelLoader) {
		// lista todos as entidades de dominio disponíveis
		final Map<String, JavaClass> javaClasses = domainModelLoader.listDomain();
		
		System.out.println("************************** CLASSES DISPONIVEIS **************************************");
		System.out.println(("" + javaClasses.keySet()).replaceAll(",", "\n"));
		System.out.println("***************************************************************************************");
	}
	
	/**
	 * Lista templates disponíveis
	 * 
	 * @param templateConfiguration
	 */
	protected static void showAvailableTemplates(final ITemplateRulesFactory templateConfiguration) {
		// lista todos os templates disponíveis
		final Map<String, List<String>> map = templateConfiguration.getAvailableTemplates();
		
		System.out.println("************************** TEMPLATES DISPONIVEIS **************************************");
		System.out.println(("templates group \n" + map.get("/br/com/jazz/gerador/templates/group")).replaceAll(",", "\n"));
		System.out.println("  ");
		System.out.println(("templates single \n" + map.get("/br/com/jazz/gerador/templates/single")).replaceAll(",", "\n"));
		System.out.println("***************************************************************************************");
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
