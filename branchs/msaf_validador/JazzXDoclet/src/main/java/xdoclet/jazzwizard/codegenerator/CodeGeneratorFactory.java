/*
 * Data de Criacao 05/06/2005 20:59:13
 * 
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.codegenerator.base.ICodeGenerator;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;
import xjavadoc.XJavaDoc;
import xjavadoc.XMethod;
import br.com.dlp.framework.util.properties.PropertiesFactory;
import br.com.dlp.framework.util.properties.PropertiesFactoryException;

/**
 * Factory para ICodeGenerator
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:59:13
 */
public class CodeGeneratorFactory {
	public static final String RENDERER_STRUTS_INDEXADO = "indexado";

	public static final String RENDERER_KEY_READONLY = "readonly";

	protected Log logger = LogFactory.getLog(CodeGeneratorFactory.class);

	private static CodeGeneratorFactory codeGeneratorFactory;

	private static Properties properties;

	/**
	 * Construtor privado
	 */
	private CodeGeneratorFactory() throws PropertiesFactoryException {
		PropertiesFactory propertiesFactory = PropertiesFactory.getInstance();
		properties = propertiesFactory
				.getProperties(CodeGeneratorFactory.class);
	}

	/**
	 * Construtor Singleton
	 * 
	 * @throws CodeGeneratorException
	 */
	public static CodeGeneratorFactory getInstance()
			throws CodeGeneratorException {
		if (codeGeneratorFactory == null) {
			try {
				codeGeneratorFactory = new CodeGeneratorFactory();

			} catch (PropertiesFactoryException e) {
				throw new CodeGeneratorException(e,
						"Erro ao tentar invocar construtor de CodeGeneratorFactory");

			}
		}
		return codeGeneratorFactory;
	}

	protected String getRendererClassName(XMethod method, String tipoControle)
			throws Wj2eeException {
		String rendererClassName;
		String rendererKey;
		if (TagHandlerUtil.somenteLeitura(method)
				&& (tipoControle.equals(TagHandlerUtil.TIPO_CONTROLE_CADASTRO)
						|| tipoControle
								.equals(TagHandlerUtil.TIPO_CONTROLE_DEFAULT) || tipoControle
						.equals(TagHandlerUtil.TIPO_CONTROLE_INDEXADO))) {

			rendererKey = RENDERER_KEY_READONLY;
		} else {
			rendererKey = TagHandlerUtil.renderer(method);
		}
		rendererClassName = getRendererClassName(method, tipoControle,
				rendererKey);

		return rendererClassName;
	}

	protected String getRendererClassName(XMethod method, String tipoControle,
			String rendererKey) throws Wj2eeException {

		/* VERIFICA SE PRECISO PEGAR O SUBTIPO DO RENDERER */
		if (tipoControle != null && !tipoControle.trim().equals("")) {
			rendererKey = tipoControle + "_" + rendererKey;
		}

		String rendererClassName = properties.getProperty(rendererKey);

		if (rendererClassName == null || rendererClassName.trim().equals("")) {
			throw new CodeGeneratorException(
					"Não foi possível encontrar um renderer para o rendererKey:"
							+ rendererKey
							+ "!!. Verifique o arquivo de properties"
							+ this.getClass().getPackage().getName()
							+ ".CodeGeneratorFactory.properties");

			// RESOLVI LANÇAR UMA EXCEÇÃO QDO NÃO ENCONTRAR O RENDERER
			// rendererClassName = DEFAULT_RENDERER;
		}

		return rendererClassName;
	}

	/**
	 * Constroi um ICodeGenerator
	 */
	public ICodeGenerator getCodeGenerator(XMethod method, String tipoControle,
			XJavaDoc javaDoc) throws Wj2eeException {

		return getCodeGenerator(method, tipoControle, null, javaDoc);
	}

	/**
	 * Constroi um ICodeGenerator
	 */
	public ICodeGenerator getCodeGenerator(XMethod method, String tipoControle,
			String renderer, XJavaDoc javaDoc) throws Wj2eeException {
		ICodeGenerator codeGenerator;
		logger.debug("Invocando o getCodeGenerator");
		String rendererClassName;
		if (renderer != null) {
			rendererClassName = getRendererClassName(method, tipoControle,
					renderer);
		} else {
			rendererClassName = getRendererClassName(method, tipoControle);
		}

		try {
			Class class1 = Class.forName(rendererClassName);
			Constructor constructor = class1.getConstructor(new Class[] {});
			Object object = constructor.newInstance(new Object[] {});
			codeGenerator = (ICodeGenerator) object;

			/* atribui o objeto de 'contexto' de XDoclet XJavaDoc */
			codeGenerator.setXJavaDoc(javaDoc);

			/* Atribui o XMethod ao ICodeGenerator */
			codeGenerator.setMethod(method);

			/* Atribui Tipo de Controle */
			codeGenerator.setTipoControle(tipoControle);

		} catch (ClassNotFoundException e) {
			throw new CodeGeneratorException(e, "A classe " + rendererClassName
					+ " referente ao renderer " + rendererClassName
					+ ", implementação de ICodeGenerator não foi encontrada ");

		} catch (SecurityException e) {
			throw new CodeGeneratorException(e,
					"Erro de Seguranca ao tentar criar a implementação de ICodeGenerator "
							+ rendererClassName);

		} catch (NoSuchMethodException e) {
			throw new CodeGeneratorException(e,
					"Erro ao tentar criar a implementação de ICodeGenerator "
							+ rendererClassName);

		} catch (IllegalArgumentException e) {
			throw new CodeGeneratorException(e,
					"Erro ao tentar criar a implementação de ICodeGenerator "
							+ rendererClassName);

		} catch (InstantiationException e) {
			throw new CodeGeneratorException(e,
					"Erro ao tentar criar a implementação de ICodeGenerator "
							+ rendererClassName);

		} catch (IllegalAccessException e) {
			throw new CodeGeneratorException(e,
					"Erro ao tentar criar a implementação de ICodeGenerator "
							+ rendererClassName);

		} catch (InvocationTargetException e) {
			throw new CodeGeneratorException(e,
					"Erro ao tentar criar a implementação de ICodeGenerator "
							+ rendererClassName);

		}

		return codeGenerator;
	}

}