package br.com.jazz.codegen.test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TemplateLoaderTest {
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws TemplateException
	 * @throws InterruptedException
	 */
	public static void main(final String[] args) throws IOException, TemplateException, InterruptedException {
		
		final Configuration configuration = new Configuration();
		
		System.out.println(new File("./").getAbsolutePath());
		
		configuration.setTemplateLoader(new ClassTemplateLoader(TemplateLoaderTest.class, "/fmTemplates"));
		// configuration.setTemplateLoader(new FileTemplateLoader(new File("src/main/resources/fmTemplates")));
		
		final StringWriter stringWriter = new StringWriter();
		
		// prova de conceito para caching de templates. Classloader é o melhor, pois funciona em tempo de desenvolvimento e produção, permitindo
		// salvar novo
		// template no quente
		
		final Template template = configuration.getTemplate("teste.ftl");
		while (true) {
			
			configuration.clearTemplateCache();
			
			template.process(new HashMap(), stringWriter);
			System.out.println(stringWriter.toString());
			
			Thread.sleep(2000);
			
		}
		// stringWriter.flush();
		// stringWriter.close();
		
	}
	
}
