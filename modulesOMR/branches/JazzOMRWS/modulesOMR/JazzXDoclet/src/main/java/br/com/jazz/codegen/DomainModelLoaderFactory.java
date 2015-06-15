package br.com.jazz.codegen;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.JazzRenderer;
import br.com.jazz.jazzwizard.exception.GeradorException;
import br.com.jazz.jazzwizard.exception.RuntimeGeradorException;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.Searcher;
import com.thoughtworks.qdox.model.AbstractInheritableJavaEntity;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.BeanProperty;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.Type;

/**
 * Abstrai a localização e leitura do modelo de dominio que será utilizado na geração de código. FIXME: IMPLEMENTAR REGRA DE CACHE DE MODELO
 * DE DOMÃ­NIO SE NECESSáRIO
 * 
 * @author darcio
 * 
 */
/**
 * @author darcio
 *
 */
public class DomainModelLoaderFactory implements Serializable, IDomainModelLoaderFactory {
	protected static final String STR_DEFAULT = "DEFAULT";

	protected static final String ATTRIBUTE_RENDERER = "renderer";
	
	private static final Type TYPE_DATE = new Type("java.util.Date");
	
	private static final Type TYPE_BOOLEAN = new Type("java.lang.Boolean");
	
	
	private final static Logger log = LoggerFactory.getLogger(DomainModelLoaderFactory.class);
	protected static Pattern isJazzEnum = Pattern.compile("^.*(ComparisonOperator|Renderer|PropertyStereotype).*$");
	
	private static final long serialVersionUID = 2525529382538031403L;
	
	public static void main(final String[] args) {
		
		final String string = "omparisonOperatorRendere";
		
		log.debug(""+DomainModelLoaderFactory.isJazzEnum.matcher(string).matches());
		
	}
	
	private final Map<String, JavaClass> domain = new HashMap<String, JavaClass>(10);
	
	private final JavaDocBuilder javaDocBuilder = new JavaDocBuilder();
	
	private final String regexFilter;
	
	private final String[] sourceTress;
	
	/**
	 * Informar os diretórios onde estarão os fontes java do modelo de domÃ­nio
	 * 
	 * @param sourceTrees
	 *          diretórios onde estão os fontes do modelo de domÃ­nio
	 */
	public DomainModelLoaderFactory(final String... sourceTrees) {
		// regex que significa qqr coisa com ao menos um caracter
		this(".+", sourceTrees);
		/*
		 * javaDocBuilder.setErrorHandler(new JavaDocBuilder.ErrorHandler() {
		 * 
		 * public void handle(final ParseException parseException) { parseException.printStackTrace(); } });
		 */
	}
	
	/**
	 * Informar a expressao regular que filtra as classes dos diretórios e os diretórios fonte
	 * 
	 * @param regexFilter
	 *          expressao regular para filtrar classes
	 * @param sourceTrees
	 *          diretórios onde estão os fontes do modelo de domÃ­nio
	 */
	public DomainModelLoaderFactory(final String regexFilter, final String... sourceTrees) {
		this.regexFilter = regexFilter;
		sourceTress = sourceTrees;
	}
	
	/**
	 * Atribui os valores default para cada atributo das anotações. Aparentemente por um bug, o QDox não consegue reconhecer valores default
	 * quando o atributo não é informado.
	 * 
	 * @param annotation
	 * @param annotClass
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 */
	protected void attributeDefaultValues(final Annotation annotation,AbstractInheritableJavaEntity javaEntity) throws SecurityException, ClassNotFoundException {
		final Class annotClass = getAnnotClass(annotation);
		
		// Os métodos aqui, na verdade, são os atributos das anotações
		for (final Method attribute : annotClass.getMethods()) {
			
			final Object value = getNamedParameter(annotation, attribute);
			// checa se já possui um valor atribuido. Caso contrário devemos atribuir o valor default manualmente.
			if (value == null) {
				
				final Object defaultValue = getDefaultValue(attribute,javaEntity);
				
				if (defaultValue != null) {
					// possui valor default
					//log.debug("ATRIBUINDO VALOR DEFAULT "+javaEntity.getName()+ " ANNOT "+annotation.getType().getFullyQualifiedName()+" att "+attribute+" valor "+defaultValue);
					annotation.getNamedParameterMap().put(attribute.getName(), "" + defaultValue);
					
				}
			} else {
				
				if (value instanceof String) {
					
					String strValue = (String) value;
					//ajuste no valor, que veio de um enum. Transforma tudo em string
					final Matcher matcher = DomainModelLoaderFactory.isJazzEnum.matcher(strValue);
					if (matcher.matches()) {
						final String[] splittedValue = strValue.split("\\.");
						strValue = splittedValue[splittedValue.length - 1];
						annotation.getNamedParameterMap().put(attribute.getName(), strValue);
					}
					
				}
				
			}
			
		}
	}

	/**
	 * Determina o valor padrao para o atributo informado.<br> 
	 * Normalmente trata os renderizadores, que tem valores padrao diferentes, de acordo com o tipo da propriedade.<br> 
	 * Ex: String=textfield, Date=calendar, Number=textfield que aceita apenas números<br>
	 * 
	 * @param attribute
	 * @param javaEntity 
	 * @return
	 */
	protected Object getDefaultValue(Method attribute, AbstractInheritableJavaEntity javaEntity) {
		
		Object defaultValue = attribute.getDefaultValue();;
		
		if(javaEntity instanceof com.thoughtworks.qdox.model.JavaMethod){
			JavaMethod javaMethod = (JavaMethod) javaEntity;
			
			Type propertyType = javaMethod.getPropertyType();
			
			if(attribute.getName().equals(DomainModelLoaderFactory.ATTRIBUTE_RENDERER) && propertyType.isA(DomainModelLoaderFactory.TYPE_DATE)){
				
				defaultValue = JazzRenderer.CALENDAR.toString();
				
			}
			
		}
		
		return defaultValue;
	}
	
	/**
	 * Atribui os valores default para cada atributo das anotações. Aparentemente, por um bug, o QDox não consegue reconhecer valores default
	 * quando o atributo não é informado.
	 * 
	 * @param annotations
	 * @throws ClassNotFoundException
	 */
	protected void fixQDoxAnnotations(final Annotation[] annotations, AbstractInheritableJavaEntity javaEntity) throws ClassNotFoundException {
		for (final Annotation annotation : annotations) {
			if (isJazzAnnotation(annotation)) {
				attributeDefaultValues(annotation,javaEntity);
			}
		}
	}
	
	/**
	 * Atribui os valores default para cada atributo das anotações. Aparentemente, por um bug, o QDox não consegue reconhecer valores default
	 * quando o atributo não é informado.
	 * 
	 * @param javaClass
	 * @throws ClassNotFoundException
	 */
	protected void fixQDoxAnnotations(final JavaClass javaClass) throws ClassNotFoundException {
		this.fixQDoxAnnotations(javaClass.getAnnotations(),javaClass);
		
		
		final JavaMethod[] methods = javaClass.getMethods();
		for (final JavaMethod javaMethod : methods) {
			this.fixQDoxAnnotations(javaMethod.getAnnotations(),javaMethod);
		}
		
		
	}
	
	/**
	 * @param className
	 * @return
	 * @throws GeradorException
	 * @throws ClassNotFoundException
	 */
	protected Class<?> forName(final String className) {
		try {
			return Class.forName(className);
		} catch (final ClassNotFoundException e) {
			throw new RuntimeGeradorException(
					"Classe '"
					+ className
					+ "' nao encontrada! Verifique se a mesma está no classpath! Se for uma annotation, verifique um alias foi criado para a mesma! ex: annotationAliases.put('JazzClass', 'br.com.jazz.codegen.annotation.JazzClass');",
					e);
		}
	}
	
	/**
	 * Testa se a propriedade se trata de um relacionamento com uma outra entidade do sistema.
	 * @param beanProperty
	 * @return
	 */
	protected boolean isEntityRelationXXtoOne(BeanProperty beanProperty) {
		return hasAnnotation(beanProperty.getType(), "br.com.jazz.codegen.annotation.JazzClass");
	}
	
	/**
	 * testa se a classe informada possui a anotação
	 * @throws GeradorException
	 */
	protected boolean hasAnnotation(Type type, String strAnnotation) {
		final java.lang.annotation.Annotation annotation = getAnnotation(type.getJavaClass(), strAnnotation);
		
		return annotation!=null;
	}
	
	
	@Override
	public Boolean getBooleanValue(final BeanProperty beanProperty, final String annotName, final String attribute) throws GeradorException {
		Object object = getAnnotationValue(beanProperty, annotName, attribute);
		
		Boolean boolValue=booleanValue(object);
		
		return boolValue;
	}
	
	@Override
	public Boolean getBooleanValue(final JavaClass javaClass, final String annotName, final String attribute) throws GeradorException {
		Object object = getAnnotationValue(javaClass, annotName, attribute);
		Boolean boolValue=booleanValue(object);
		
		return boolValue;
	}
	
	@Override
	public Boolean getBooleanValue(final JavaMethod javaMethod, final String annotName, final String attribute) throws GeradorException {
		Object object = getAnnotationValue(javaMethod, annotName, attribute);
		Boolean boolValue=booleanValue(object);
		
		return boolValue;
	}
	
	
	/**
	 * Converte qualquer objeto para um boleano.<br>
	 * 1=true, y=true, s=true, e o Boolean.TRUE=true e true
	 * @param object
	 * @return
	 */
	private Boolean booleanValue(Object object) {
		
		if(object==null){
			return false;
		}
		
		return
		Boolean.TRUE.equals(object) ||
		new Integer(1).equals(object) ||
		"true".equalsIgnoreCase(object.toString()) ||
		"y".equalsIgnoreCase(object.toString()) ||
		"s".equalsIgnoreCase(object.toString());
	}
	
	
	
	
	public Object getAnnotationValue(final BeanProperty beanProperty, final String annotName, final String attribute) throws GeradorException {
		
		JavaMethod method = beanProperty.getAccessor();
		if (method == null) {
			method = beanProperty.getMutator();
		}
		
		return this.getAnnotationValue(method, annotName, attribute);
	}
	
	public Object getAnnotationValue(final JavaClass javaClass, final String annotName, final String attribute) throws GeradorException {
		
		final java.lang.annotation.Annotation annotation = getAnnotation(javaClass, annotName);
		
		final Object value = getAnnotationAttributeValue(annotation, attribute);
		
		return value;
		
	}
	
	/**
	 * Recuopera o valor de um atributo de uma anotacao
	 */
	public Object getAnnotationValue(final JavaMethod javaMethod, final String annotName, final String attribute) throws GeradorException {
		
		final JavaClass javaClass = javaMethod.getParentClass();
		
		final String className = javaClass.getFullyQualifiedName();
		
		final Class clazz = getAnnotationClass(className);
		
		final Method method = getMethod(javaMethod, className, clazz);
		
		final Class annClazz = getAnnotationClass(annotName);
		
		final java.lang.annotation.Annotation annotation = method.getAnnotation(annClazz);
		
		final Object value = getValue(javaMethod, annotation, attribute);
		
		return value;
		
	}
	
	
	
	/**
	 * @param javaClass
	 * @param annotName
	 * @return
	 * @throws GeradorException
	 */
	public java.lang.annotation.Annotation getAnnotation(final JavaClass javaClass, final String annotName)  {
		final String className = javaClass.getFullyQualifiedName();
		
		final Class clazz = getAnnotationClass(className);
		
		final Class annClazz = getAnnotationClass(annotName);
		
		final java.lang.annotation.Annotation annotation = clazz.getAnnotation(annClazz);
		return annotation;
	}
	
	/**
	 * Recupera uma java.lang.Class de uma anotation a partir do seu nome completo ou apelido
	 * 
	 * @param className
	 * @return
	 * @throws GeradorException
	 */
	protected Class<?> getAnnotationClass(final String className) {
		return forName(className);
	}
	
	/**
	 * Retorna a Class (java reflection puro) para a anotation informada
	 * 
	 * @param annotation
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected Class<?> getAnnotClass(final Annotation annotation) throws ClassNotFoundException {
		return Class.forName(annotation.getType().getFullyQualifiedName());
	}
	
	/**
	 * @param javaMethod
	 * @param className
	 * @param clazz
	 * @return
	 * @throws GeradorException
	 */
	protected Method getMethod(final JavaMethod javaMethod, final String className, final Class clazz) throws GeradorException {
		Method method;
		
		final String name = javaMethod.getName();
		try {
			
			method = clazz.getMethod(name, getParams(javaMethod));
			
		} catch (final SecurityException e) {
			throw new GeradorException("Erro ao tentar recuperar o metodo '" + name + "' da classe '" + className + "'!", e);
		} catch (final NoSuchMethodException e) {
			throw new GeradorException("Metodo '" + name + "' da classe '" + className + "' nao encontrado! ", e);
		}
		return method;
	}
	
	/**
	 * @param annotation
	 * @param attribute
	 * @return
	 */
	protected Object getNamedParameter(final Annotation annotation, final Method attribute) {
		return annotation.getNamedParameter(attribute.getName());
	}
	
	protected Class[] getParams(final JavaMethod javaMethod) throws GeradorException {
		final JavaParameter[] params = javaMethod.getParameters();
		
		final Class[] paramsReflect = new Class[params.length];
		int i = 0;
		
		for (final JavaParameter javaParameter : params) {
			Class clazz;
			try {
				clazz = forName(javaParameter.getType().getValue());
				paramsReflect[i++] = clazz;
				
			} catch (final RuntimeGeradorException e) {
				throw new GeradorException("", e);
			}
		}
		
		return paramsReflect;
	}
	
	/**
	 * Cria um searcher baseado em regex
	 * 
	 * @param regexFilter
	 * @return
	 */
	protected Searcher getRegexSearcher(final String regexFilter) {
		final Searcher searcher = new Searcher() {
			public boolean eval(final JavaClass cls) {
				final String nome = cls.getName();
				return nome.matches(regexFilter);
				// return true;
			}
		};
		return searcher;
	}
	
	/**
	 * TODO: IMPLEMENTAR DEFAULT VALUE INTELIGENTE PARA OUTROS ATRIBUTOS DE JAZZPROP
	 * @param javaMethod 
	 * @param annotation
	 * @param attribute
	 * @return
	 * @throws GeradorException
	 */
	protected Object getValue(JavaMethod javaMethod, final java.lang.annotation.Annotation annotation, final String attribute) throws GeradorException {
		
		Object value = getAnnotationAttributeValue(annotation, attribute);
		
		Type propertyType = javaMethod.getPropertyType();
		
		if(isDefaultValue(value) && isJazzRendererAttribute(annotation, attribute) ){
				
			value = getRendererDefaultValue(value, propertyType);
			
		}
		
		return value;
	}

	/**
	 * TODO Implementar o retorno de default value para todos os tipos interessantes
	 * @param value
	 * @param propertyType
	 * @return
	 */
	protected Object getRendererDefaultValue(Object value, Type propertyType) {

		if (propertyType.isA(TYPE_DATE)) {
			value = JazzRenderer.CALENDAR;
			
		}else if(propertyType.isA(TYPE_BOOLEAN)) {
			value = JazzRenderer.CHECKBOX;
			
		}
		
		return value;
	}

	/**
	 * Testa se a anotacao é jazzprop e o atributo é renderer
	 * @param annotation
	 * @param annAttribute
	 * @param expAttribute TODO
	 * @return
	 */
	protected boolean isJazzRendererAttribute(final java.lang.annotation.Annotation annotation, final String annAttribute) {
		return (annotation instanceof JazzProp) && annAttribute.equals( DomainModelLoaderFactory.ATTRIBUTE_RENDERER);
	}

	/**
	 * @param value
	 * @return
	 */
	protected boolean isDefaultValue(Object value) {
		
		value = value==null?"":value+"";
		
		return value.equals(DomainModelLoaderFactory.STR_DEFAULT);
	}
	


	/**
	 * @param annotation
	 * @param attribute
	 * @return
	 * @throws GeradorException
	 */
	protected Object getAnnotationAttributeValue(final java.lang.annotation.Annotation annotation, final String attribute) throws GeradorException {
		Object value = "";
		if (annotation == null || StringUtils.isBlank(attribute)) {
			return value;
		}
		
		Method annotationMethod = getAnnotationMethod(annotation, attribute);
		
		value = invokeMethod(annotation, attribute, annotationMethod);
		return value;
	}

	/**
	 * @param annotation
	 * @param attribute
	 * @param method
	 * @return
	 * @throws GeradorException
	 */
	protected Object invokeMethod(final java.lang.annotation.Annotation annotation, final String attribute, Method method) throws GeradorException {
		Object value;
		try {
			value = method.invoke(annotation);
		} catch (final IllegalArgumentException e) {
			throw new GeradorException("Erro ao tentar acessar o atributo '" + attribute + "' da anotacao '" + annotation + "' !", e);
			
		} catch (final IllegalAccessException e) {
			throw new GeradorException("Erro ao tentar acessar o atributo '" + attribute + "' da anotacao '" + annotation + "' !", e);
			
		} catch (final InvocationTargetException e) {
			throw new GeradorException("Erro ao tentar acessar o atributo '" + attribute + "' da anotacao '" + annotation + "' !", e);
			
		}
		return value;
	}

	/**
	 * @param annotation
	 * @param attribute
	 * @return
	 * @throws GeradorException
	 */
	protected Method getAnnotationMethod(final java.lang.annotation.Annotation annotation, final String attribute) throws GeradorException {
		Method method;
		try {
			method = annotation.getClass().getMethod(attribute, new Class[] {});
		} catch (final SecurityException e) {
			throw new GeradorException("Erro ao tentar acessar o atributo '" + attribute + "' da anotacao '" + annotation + "' !", e);
		} catch (final NoSuchMethodException e) {
			throw new GeradorException("Atributo '" + attribute + "' da anotacao '" + annotation + "' não encontrado!", e);
		}
		return method;
	}
	
	
	/**
	 * Verifica se a anotação informada é do tipo de anotação do gerador de código Jazz (JazzClass e JazzProp)
	 * 
	 * @param annotation
	 * @return
	 */
	protected boolean isJazzAnnotation(final Annotation annotation) {
		return isJazzClassAnnotation(annotation) || isJazzPropAnnotation(annotation);
	}
	
	/**
	 * @param annotation
	 * @return
	 */
	protected boolean isJazzPropAnnotation(final Annotation annotation) {
		String name = JazzProp.class.getName();
		return isSameAnnotation(annotation, name);
	}
	
	/**
	 * @param annotation
	 * @param name
	 * @return
	 */
	protected boolean isSameAnnotation(final Annotation annotation, String name) {
		String fullyQualifiedName = annotation.getType().getFullyQualifiedName();
		return fullyQualifiedName.equals(name);
	}
	
	/**
	 * @param annotation
	 * @return
	 */
	protected boolean isJazzClassAnnotation(final Annotation annotation) {
		String name = JazzClass.class.getName();
		return isSameAnnotation(annotation, name);
	}
	
	/**
	 * Determina se o meta modelo de domÃ­nio deverá ser lido
	 * 
	 * @param forceRefresh
	 * @return
	 */
	protected boolean isRefreshModel(final boolean forceRefresh) {
		return forceRefresh || domain == null || domain.isEmpty();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IDomainModelLoaderFactory#listDomain()
	 */
	public Map<String, JavaClass> getDomainMap() {
		return this.getDomainMap(false);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IDomainModelLoaderFactory#listDomain(boolean)
	 */
	public Map<String, JavaClass> getDomainMap(final boolean forceRefresh) {
		if (isRefreshModel(forceRefresh)) {
			
			for (final String sourceTree : sourceTress) {
				javaDocBuilder.addSourceTree(new File(sourceTree));
			}
			
			final Searcher searcher = getRegexSearcher(regexFilter);
			final List<JavaClass> javaClasses = javaDocBuilder.search(searcher);
			
			for (final JavaClass javaClass : javaClasses) {
				try {
					this.fixQDoxAnnotations(javaClass);
				} catch (final ClassNotFoundException e) {
					throw new RuntimeException("Erro ao tentar ajustar os valores default de annotations", e);
				}
				
				domain.put(javaClass.getFullyQualifiedName(), javaClass);
			}
		}
		
		return domain;
	}
	
}
