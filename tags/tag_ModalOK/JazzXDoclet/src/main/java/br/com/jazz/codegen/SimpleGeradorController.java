/**
 * 
 */
package br.com.jazz.codegen;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.jazzwizard.exception.GeradorException;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * Implementação simples de AbstractGeradorController para testes e debug
 * 
 * @author darcio
 * 
 */
public class SimpleGeradorController extends AbstractGeradorController {

	public SimpleGeradorController(ITemplateRulesFactory templateRulesFactory, IDomainModelLoaderFactory modelLoaderFactory, IWriterFactory writerFactory) throws GeradorException {
		super(templateRulesFactory, modelLoaderFactory, writerFactory);
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
	public static void main(String[] args) throws GeradorException, IOException, SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		ITemplateRulesFactory templateRulesFactory = createTemplateRulesFactory();
		IDomainModelLoaderFactory domainModelLoaderFactory = createDomainLoaderFactory();
		IWriterFactory writerFactory = createWriterFactory();

		IGeradorController controller = new SimpleGeradorController(templateRulesFactory,domainModelLoaderFactory,writerFactory);
		

		Map<String, JavaClass> javaClasses = controller.getModelLoader().listDomain();

		
		Object value = controller.getModelLoader().getAnnotationValue(javaClasses.get("br.com.dlp.jazzqa.produto.ProdutoVO"), JazzClass.class.getName(), "name");

		System.out.println(value);
		
		
		//gera todos os arquivos de configuracao
		controller.generateConfigFile();

		/*
		 */
		controller.generateFile(javaClasses
				.get("br.com.dlp.jazzqa.tiporequisito.TipoRequisitoVO"),
				"Business.ftl", 
				"DomainBundle.ftl",
				"BusinessImpl.ftl", 
				"HibernateDAOImpl.ftl",
				"RichFacesJSP.ftl", 
				"JSFBean.ftl", 
				"DAO.ftl");
		
		controller.generateFile(
				javaClasses.get("br.com.dlp.jazzqa.produto.ProdutoVO"),
				"Business.ftl", 
				"DomainBundle.ftl",
				"BusinessImpl.ftl", 
				"HibernateDAOImpl.ftl",
				"RichFacesJSP.ftl", 
				"JSFBean.ftl", 
				"DAO.ftl");

		/*
		 */
		controller.generateFile(javaClasses
				.get("br.com.dlp.jazzqa.usuariojazz.UsuarioJazzVO"),
				"Business.ftl", 
				"DomainBundle.ftl",
				"BusinessImpl.ftl",
				"HibernateDAOImpl.ftl", 
				"RichFacesJSP.ftl",
				"JSFBean.ftl", 
				"DAO.ftl");
	}

	/**
	 * Cria implementação de ITemplateRulesFactory
	 * @return
	 * @throws BeansException
	 * @throws GeradorException
	 */
	public static ITemplateRulesFactory createTemplateRulesFactory() throws BeansException, GeradorException {
		// Configuração do spring para o configurador de templates
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/br/com/jazz/gerador/templates/ApplicationContext.xml");
		// responsável por encontrar e listar os templates disponíveis
		ITemplateRulesFactory templateConfiguration = new FreemarkerTemplateRulesFactory(applicationContext, 
				"/br/com/jazz/gerador/templates/imports",
				"/br/com/jazz/gerador/templates/group",
				"/br/com/jazz/gerador/templates/single");
		
		showAvailableTemplates(templateConfiguration);
		
		return templateConfiguration;
	}

	/**
	 * Cria uma implementação de IDomainModelLoaderFactory
	 * @return
	 */
	public static IDomainModelLoaderFactory createDomainLoaderFactory() {
		// Responsável por carregar o modelo de domínio para geração.
		// Se for necessário trabalhar com tags annotation de supertipos das classes de dominio,
		// o diretório fonte desdes deve ser incluído
		IDomainModelLoaderFactory domainModelLoader = new DomainModelLoaderFactory("^.+VO$", new String[] { "../../JazzQA/core/src/main/java",
				"../../JazzFramework/src/main/java", });
		
		showAvailableClasses(domainModelLoader);
		
		return domainModelLoader;
	}

	/**
	 * @return 
	 * @return
	 */
	public static FileWriterFactory createWriterFactory() {
		return new FileWriterFactory("./../");
	}

	/**
	 * Lista templates disponíveis
	 * @param templateConfiguration
	 */
	protected static void showAvailableTemplates(ITemplateRulesFactory templateConfiguration) {
		// lista todos os templates disponíveis
		Map<String, List<String>> map = templateConfiguration.getAvailableTemplates();
	
		System.out.println("************************** TEMPLATES DISPONIVEIS **************************************");
		System.out.println(("templates group \n" + map.get("/br/com/jazz/gerador/templates/group")).replaceAll(",", "\n"));
		System.out.println(("  "));
		System.out.println(("templates single \n" + map.get("/br/com/jazz/gerador/templates/single")).replaceAll(",", "\n"));
		System.out.println("***************************************************************************************");
	}

	/**
	 * Apenas lista classes disponíveis 
	 * @param domainModelLoader
	 */
	protected static void showAvailableClasses(IDomainModelLoaderFactory domainModelLoader) {
		// lista todos as entidades de dominio disponíveis
		Map<String, JavaClass> javaClasses = domainModelLoader.listDomain();
	
		System.out.println("************************** CLASSES DISPONIVEIS **************************************");
		System.out.println(("" + javaClasses.keySet()).replaceAll(",", "\n"));
		System.out.println("***************************************************************************************");
	}

}
