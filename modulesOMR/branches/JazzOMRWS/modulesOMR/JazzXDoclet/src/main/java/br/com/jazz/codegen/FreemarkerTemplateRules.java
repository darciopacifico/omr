/**
 * 
 */
package br.com.jazz.codegen;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.dlp.framework.i18n.JazzMessageFormat;
import br.com.jazz.codegen.exception.FileWriterFactoryException;
import br.com.jazz.codegen.exception.TemplateRulesException;
import br.com.jazz.jazzwizard.exception.GeradorException;

import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.JavaClass;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Regras e propriedades referentes a um template do gerador. Ex: determina regras para execução de um template, contém mapa de propriedades
 * para uso dos templates <br>
 * <br>
 * Implementação para template engine Freemarker <br>
 * <br>
 * 
 * @author darcio
 * 
 */
public class FreemarkerTemplateRules implements Serializable, ITemplateRules {
	
	protected static JazzMessageFormat mf = new JazzMessageFormat("br.com.jazz.gerador.messages");
	
	/**
	 * Chave de propriedade para recuperar o nome do arquivo de configuração de destino
	 */
	public static final Object PROP_FILE_NAME_CONFIG = "fileNameConfig";
	
	/**
	 * Recupera regex do mapa de propriedades para determinar nome da entidade de domÃ­nio a partir do nome da classe java que servirá de
	 * insumo para geração de código. Exemplos: classe=PessoaVO.java => domÃ­nio=Pessoa; classe=ProdutoBean.java => domÃ­nio=Produto. <br>
	 * <br>
	 * O nome da entidade de domÃ­nio será igual ao grupo $1 da regex informada: Exemplo se classe=PessoaVO e domÃ­nio=Pessoa então
	 * Regex="(.*)(VO)", onde o primeiro par de parÃªnteses corresponde ao grupo $1, de acordo com a implementação de regex de Java.
	 */
	public static final String PROP_REGEX_DOMAIN_NAME_PATTERN = "fileNamePattern";
	
	/**
	 * Recupera a string de replacement que será usada para compor o nome do arquivo de destino resultado do processamento. Deve seguir o
	 * padrão de replacement, como no segundo argumento do método String.replaceAll(regex, replacement); <br>
	 * <br>
	 * 
	 * Exemplo para composição de nome de um Facade a partir do nome de uma entidade de domÃ­nio:
	 * 
	 * String regexFileNameReplacement = "$0Facade"; String dominio = "Pessoa";
	 * 
	 * String destFile = dominio.replaceAll(FileWriterFactory.REGEX_FULL_STRING,regexFileNameReplacement); destFile=="PessoaFacade"
	 * 
	 */
	public static final String PROP_REGEX_FILE_DEST_REPLACEMENT = "regexFileDestReplacement";
	
	/**
	 * Literal regex = full string
	 */
	public static final String REGEX_FULL_STRING = "(^.+$)";
	private static final long serialVersionUID = -3638497614861027489L;
	private Map<String, String> annotationRules = new HashMap<String, String>();
	
	private FILE_EXISTING_ACTIONS fileExistingAction;
	
	protected Log log = LogFactory.getLog(ITemplateRules.class);
	private String outputEncoding;
	private Map<String, String> properties = new HashMap<String, String>();
	private Map<String, String> regexRules = new HashMap<String, String>();
	
	private List<String> requiredAnnotationRules = new ArrayList<String>();
	private Template template;
	
	private String templateEncoding;
	
	/**
	 * Construtor simples de template rules.
	 * 
	 * @param template
	 */
	public FreemarkerTemplateRules() {
	}
	
	/**
	 * Construtor simples de template rules. Recebe template Freemarker
	 * 
	 * @param template
	 */
	public FreemarkerTemplateRules(final Template template) {
		this.template = template;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.ITemplateRules#getDocletRules()
	 */
	public Map<String, String> getAnnotationRules() {
		return this.annotationRules;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.ITemplateRules#getDocletValue(com.thoughtworks.qdox.model.JavaClass, java.lang.String)
	 */
	public Object getAnnotationValue(final JavaClass javaClass, final String key) {
		
		final String[] parts = key.split("\\.");
		
		if (parts.length > 0) {
			
			final Annotation[] annotations = javaClass.getAnnotations();
			Annotation annotation = null;
			for (final Annotation annotDaVez : annotations) {
				if (annotDaVez.getType().getJavaClass().getName().equals(parts[0])) {
					annotation = annotDaVez;
				}
			}
			
			if (annotation != null) {
				
				if (parts.length <= 1) {
					// retorna o valor da tag
					return annotation.getParameterValue();
				} else {
					// retorna o valor do atributo da tab
					return annotation.getNamedParameter(parts[1]);
					// return docletTag.getNamedParameterMap().get(parts[1]);
				}
				
			} else {
				return null;
			}
		} else {
			return null;
			
		}
		
	}
	
	/**
	 * Recupera o nome da entidade de domÃ­nio
	 * 
	 * @param templateRules
	 * @param javaClass
	 * @return
	 * @throws GeradorException
	 */
	public String getDomainName(final JavaClass javaClass) throws GeradorException {
		
		if (javaClass != null) {
			
			String domainName;
			final String fileNamePattern = this.getProperties().get(FreemarkerTemplateRules.PROP_REGEX_DOMAIN_NAME_PATTERN);
			
			if (StringUtils.isEmpty(fileNamePattern)) {
				// filewriter.regexDomainNamePatternNotSpecified=A regex para determinar o nome do domÃ­nio ({0}) não foi espeficicada nas
				// configurações do gerador.
				// Verifique o arquivo ApplicationContext.xml!
				throw new TemplateRulesException(FreemarkerTemplateRules.mf.format("filewriter.regexDomainNamePatternNotSpecified",
						FreemarkerTemplateRules.PROP_REGEX_DOMAIN_NAME_PATTERN, javaClass.getName()));
			}
			
			final String className = javaClass.getName();
			
			domainName = className.replaceAll(fileNamePattern, FileWriterFactory.REGEX_GROUP1_REPLACEMENT);
			return domainName;
			
		} else {
			return null;
			
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.ITemplateRules#getFileExistingAction()
	 */
	public FILE_EXISTING_ACTIONS getFileExistingAction() {
		return this.fileExistingAction;
	}
	
	/**
	 * Implementação de IWriterFactory.getFileName(). Recupera como será o nome do arquivo gerado
	 * 
	 * @throws GeradorException
	 */
	public String getFileName() throws GeradorException {
		
		final String fileName = this.getProperties().get(FreemarkerTemplateRules.PROP_FILE_NAME_CONFIG);
		
		if (StringUtils.isEmpty(fileName)) {
			// filewriter.fileNameConfigNotSpecified=O nome do arquivo a ser gerado para {0} não foi especificado na propriedade {1}. Verifique o
			// arquivo de
			// configurações ApplicationContext.xml!
			throw new FileWriterFactoryException(FreemarkerTemplateRules.mf.format("filewriter.fileNameConfigNotSpecified", this.getName(),
					FreemarkerTemplateRules.PROP_FILE_NAME_CONFIG));
		}
		
		return fileName;
	}
	
	/**
	 * Implementação de IWriterFactory.getFileName(). Recupera como será o nome do arquivo gerado
	 * 
	 * @throws GeradorException
	 */
	public String getFileName(final JavaClass javaClass) throws GeradorException {
		
		final String domainName = this.getDomainName(javaClass);
		
		final String regexDestReplacement = this.getProperties().get(FreemarkerTemplateRules.PROP_REGEX_FILE_DEST_REPLACEMENT);
		if (StringUtils.isEmpty(regexDestReplacement)) {
			// filewriter.destfileNotSpecified=Nao é possivel determinar o fileName para {0}/{1} por que a propriedade {2} nao foi especificada!
			throw new FileWriterFactoryException(FreemarkerTemplateRules.mf.format("filewriter.destfileNotSpecified", this.getName(), javaClass
					.getName(), FreemarkerTemplateRules.PROP_REGEX_FILE_DEST_REPLACEMENT));
		}
		
		final String fileName = domainName.replaceAll(FreemarkerTemplateRules.REGEX_FULL_STRING, regexDestReplacement);
		
		return fileName;
	}
	
	/**
	 * Implementação de IWriterFactory.getFileName(). Recupera como será o nome do arquivo gerado
	 * 
	 * @throws GeradorException
	 */
	@Deprecated
	public String getJavaComponentGenericName(final JavaClass javaClass) throws GeradorException {
		
		throw new NullPointerException("METODO DESABILITADO!!! NãO ESTá MAIS SENDO UTILIZADO");
		// String componentName = getJavaComponentGenericName(javaClass);
		
		// return componentName;
	}
	
	/**
	 * Implementação de IWriterFactory.getFileName(). Recupera como será o nome do arquivo gerado
	 * 
	 * @throws GeradorException
	 */
	public String getJavaComponentName(final JavaClass javaClass) throws GeradorException {
		
		final String fileName = this.getFileName(javaClass);
		
		// retira extensão do nome do arquivo
		final String componentName = fileName.replaceAll("\\..*", "");
		
		return componentName;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.ITemplateRules#getName()
	 */
	public String getName() {
		return this.template.getName();
	}
	
	public String getOutputEncoding() {
		return this.outputEncoding;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.ITemplateRules#getProperties()
	 */
	public Map<String, String> getProperties() {
		return this.properties;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.ITemplateRules#getRegexRules()
	 */
	public Map<String, String> getRegexRules() {
		return this.regexRules;
	}
	
	/**
	 * @return the requiredAnnotationRules
	 */
	public List<String> getRequiredAnnotationRules() {
		return this.requiredAnnotationRules;
	}
	
	protected Template getTemplate() {
		return this.template;
	}
	
	public String getTemplateEncoding() {
		return this.templateEncoding;
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.ITemplateRules#isGenerate(com.thoughtworks.qdox.model.JavaClass)
	 */
	public boolean isGenerate(final JavaClass javaClass) {
		
		if (!this.isGenerateRegex(javaClass)) {
			return false;
		}
		
		if (!this.isGenerateAnnotationRules(javaClass)) {
			return false;
		}
		
		return true;
		
	}
	
	/**
	 * Verifica se as propriedades especificadas no map annotationRules batem com as tags e atributos de Annotation de javaClass.
	 * 
	 * @param javaClass
	 * @return
	 */
	protected boolean isGenerateAnnotationRules(final JavaClass javaClass) {
		final Set<String> keys = this.annotationRules.keySet();
		// itera todas as chaves
		for (final String keyAnnotation : keys) {
			
			Object jcAnnotationValue;
			try {
				jcAnnotationValue = this.getAnnotationValue(javaClass, keyAnnotation);
			} catch (final Exception e) {
				// se a propriedade não existe sai do for
				break;
			}
			
			final String annotationRule = this.annotationRules.get(keyAnnotation);
			
			if (jcAnnotationValue == null && this.requiredAnnotationRules.contains(keyAnnotation)) {
				// rules.annotationmapNotFound=A regra XDoclet obrigatória {0}, especificada para o template {1}, não foi encontrada na classe {2}.
				// O template {1} não será
				// rodado para a classe {2}!
				this.log.warn(FreemarkerTemplateRules.mf.format("rules.annotationmapNotFound", keyAnnotation, this.template.getName(), javaClass
						.getName()));
				return false;
			}
			
			// se o valor encontrado é diferente do valor esperado não roda do template
			if (jcAnnotationValue != null && !("" + jcAnnotationValue).matches(annotationRule)) {
				
				this.log.warn(FreemarkerTemplateRules.mf.format("rules.docletmap", this.template.getName(), javaClass.getName(), keyAnnotation,
						jcAnnotationValue, annotationRule));
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Verifica se as propriedades especificadas no map regexRules batem com as propriedades da JavaClass informada.
	 * 
	 * @param javaClass
	 * @return
	 */
	protected boolean isGenerateRegex(final JavaClass javaClass) {
		final Set<String> keys = this.regexRules.keySet();
		// itera todas as chaves
		for (final String keyProp : keys) {
			
			String jcPropValue;
			try {
				jcPropValue = BeanUtilsBean.getInstance().getProperty(javaClass, keyProp);
			} catch (final Exception e) {
				// se a propriedade não existe sai do for
				break;
			}
			
			final String regexRule = this.regexRules.get(keyProp);
			
			// se o valor encontrado é diferente do valor esperado não roda do template
			if (jcPropValue != null && !jcPropValue.matches(regexRule)) {
				this.log.warn(FreemarkerTemplateRules.mf.format("rules.regexmap", this.template.getName(), javaClass.getName(), keyProp,
						jcPropValue, regexRule));
				return false;
			}
		}
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.ITemplateRules#process(java.util.Map, java.io.Writer)
	 */
	public void process(final Map<String, Object> map, final Writer writer) throws TemplateRulesException {
		try {
			this.template.process(map, writer);
			
		} catch (final TemplateException e) {
			// templaterules.processerror=Erro ao tentar processar o template {0}!
			throw new TemplateRulesException(FreemarkerTemplateRules.mf.format("templaterules.processerror", this.getName()), e);
			
		} catch (final IOException e) {
			
			throw new TemplateRulesException(FreemarkerTemplateRules.mf.format("templaterules.processerror", this.getName()), e);
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.ITemplateRules#setDocletRules(java.util.Map)
	 */
	public void setAnnotationRules(final Map<String, String> annotationRules) {
		this.annotationRules = annotationRules;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.ITemplateRules#setFileExistingAction(br.com.jazz.codegen.ITemplateRules.FILE_EXISTING_ACTIONS)
	 */
	public void setFileExistingAction(final FILE_EXISTING_ACTIONS fileExistingAction) {
		this.fileExistingAction = fileExistingAction;
	}
	
	public void setOutputEncoding(final String outputEncoding) {
		this.outputEncoding = outputEncoding;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.ITemplateRules#setProperties(java.util.Map)
	 */
	public void setProperties(final Map<String, String> map) {
		this.properties = map;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.ITemplateRules#setRegexRules(java.util.Map)
	 */
	public void setRegexRules(final Map<String, String> regexRules) {
		this.regexRules = regexRules;
	}
	
	/**
	 * @param requiredAnnotationRules
	 *          the requiredAnnotationRules to set
	 */
	public void setRequiredAnnotationRules(final List<String> requiredAnnotationRules) {
		this.requiredAnnotationRules = requiredAnnotationRules;
	}
	
	protected void setTemplate(final Template template) {
		this.template = template;
	}
	
	public void setTemplateEncoding(final String templateEncoding) {
		this.templateEncoding = templateEncoding;
	}
}
