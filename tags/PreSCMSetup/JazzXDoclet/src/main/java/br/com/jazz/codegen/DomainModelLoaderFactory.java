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

import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.jazzwizard.exception.GeradorException;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.Searcher;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.BeanProperty;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;

/**
 * Abstrai a localização e leitura do modelo de dominio que será utilizado na geração de código. FIXME: IMPLEMENTAR REGRA DE CACHE DE MODELO
 * DE DOMíNIO SE NECESSáRIO
 * 
 * @author darcio
 * 
 */
public class DomainModelLoaderFactory implements Serializable, IDomainModelLoaderFactory {
	
	protected static Pattern isJazzEnum = Pattern.compile("^.*(ComparisonOperator|Renderer).*$");
	
	private static final long serialVersionUID = 2525529382538031403L;
	
	public static void main(final String[] args) {
		
		final String string = "omparisonOperatorRendere";
		
		System.out.println(DomainModelLoaderFactory.isJazzEnum.matcher(string).matches());
		
	}
	
	private final Map<String, JavaClass> domain = new HashMap<String, JavaClass>(10);
	
	private final JavaDocBuilder javaDocBuilder = new JavaDocBuilder();
	
	private final String regexFilter;
	
	private final String[] sourceTress;
	
	/**
	 * Informar os diretórios onde estarão os fontes java do modelo de domínio
	 * 
	 * @param sourceTrees
	 *          diretórios onde estão os fontes do modelo de domínio
	 */
	public DomainModelLoaderFactory(final String... sourceTrees) {
		// regex que significa qqr coisa com ao menos um caracter
		this(".+", sourceTrees);
		/*
		javaDocBuilder.setErrorHandler(new JavaDocBuilder.ErrorHandler() {
			
			public void handle(final ParseException parseException) {
				parseException.printStackTrace();
			}
		});
		 */
	}
	
	/**
	 * Informar a expressao regular que filtra as classes dos diretórios e os diretórios fonte
	 * 
	 * @param regexFilter
	 *          expressao regular para filtrar classes
	 * @param sourceTrees
	 *          diretórios onde estão os fontes do modelo de domínio
	 */
	public DomainModelLoaderFactory(final String regexFilter, final String... sourceTrees) {
		this.regexFilter = regexFilter;
		sourceTress = sourceTrees;
	}
	
	/**
	 * Atribui os valores default para cada atributo das anotações. Aparentemente, por um bug, o QDox não consegue reconhecer valores default
	 * quando o atributo não é informado.
	 * 
	 * @param annotation
	 * @param annotClass
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 */
	protected void attributeDefaultValues(final Annotation annotation) throws SecurityException, ClassNotFoundException {
		
		final Class annotClass = getAnnotClass(annotation);
		
		// Os métodos aqui, na verdade, são os atributos das anotações
		for (final Method attribute : annotClass.getMethods()) {
			
			final Object value = getNamedParameter(annotation, attribute);
			// checa se já possui um valor atribuido. Caso contrário, infelizmente, devemos atribuir o valor default manualmente.
			if (value == null) {
				
				final Object defaultValue = attribute.getDefaultValue();
				
				if (defaultValue != null) {
					// possui valor default
					annotation.getNamedParameterMap().put(attribute.getName(), "" + defaultValue);
				}
			} else {
				
				if (value instanceof String) {
					
					String strValue = (String) value;
					
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
	 * Atribui os valores default para cada atributo das anotações. Aparentemente, por um bug, o QDox não consegue reconhecer valores default
	 * quando o atributo não é informado.
	 * 
	 * @param annotations
	 * @throws ClassNotFoundException
	 */
	protected void fixQDoxAnnotations(final Annotation[] annotations) throws ClassNotFoundException {
		for (final Annotation annotation : annotations) {
			if (isJazzAnnotation(annotation)) {
				attributeDefaultValues(annotation);
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
		this.fixQDoxAnnotations(javaClass.getAnnotations() );
		
		final JavaMethod[] methods = javaClass.getMethods();
		
		for (final JavaMethod javaMethod : methods) {
			this.fixQDoxAnnotations(javaMethod.getAnnotations());
		}
	}
	
	/**
	 * @param className
	 * @return
	 * @throws GeradorException
	 * @throws ClassNotFoundException
	 */
	protected Class<?> forName(final String className) throws GeradorException {
		try {
			return Class.forName(className);
		} catch (final ClassNotFoundException e) {
			throw new GeradorException("Classe '" + className + "' nao encontrada! Verifique se a mesma está no classpath! Se for uma annotation, verifique um alias foi criado para a mesma! ex: annotationAliases.put('JazzClass', 'br.com.jazz.codegen.annotation.JazzClass');", e);
		}
	}
	
	public Object getAnnotationValue(final BeanProperty beanProperty, final String annotName, final String attribute) throws GeradorException {
		
		JavaMethod method = beanProperty.getAccessor();
		if (method == null) {
			method = beanProperty.getMutator();
		}
		
		return this.getAnnotationValue(method, annotName, attribute);
	}
	
	public Object getAnnotationValue(final JavaClass javaClass, final String annotName, final String attribute) throws GeradorException {
		
		final String className = javaClass.getFullyQualifiedName();
		
		final Class clazz = getAnnotationClass(className);
		
		final Class annClazz = getAnnotationClass(annotName);
		
		final java.lang.annotation.Annotation annotation = clazz.getAnnotation(annClazz);
		
		final Object value = getValue(annotation, attribute);
		
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
		
		final Object value = getValue(annotation, attribute);
		
		return value;
		
	}
	
	/**
	 * Recupera uma java.lang.Class de uma anotation a partir do seu nome completo ou apelido
	 * @param className
	 * @return
	 * @throws GeradorException
	 */
	protected Class<?> getAnnotationClass(final String className) throws GeradorException {
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
				
			} catch (final GeradorException e) {
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
	 * 
	 * @param annotation
	 * @param attribute
	 * @return
	 * @throws GeradorException
	 */
	protected Object getValue(final java.lang.annotation.Annotation annotation, final String attribute) throws GeradorException {
		
		if (annotation == null || StringUtils.isBlank(attribute)) {
			return "";
		}
		
		Method method;
		try {
			method = annotation.getClass().getMethod(attribute, new Class[] {});
		} catch (final SecurityException e) {
			throw new GeradorException("Erro ao tentar acessar o atributo '" + attribute + "' da anotacao '" + annotation + "' !", e);
		} catch (final NoSuchMethodException e) {
			throw new GeradorException("Atributo '" + attribute + "' da anotacao '" + annotation + "' não encontrado!", e);
		}
		
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
	 * Verifica se a anotação informada é do tipo de anotação do gerador de código Jazz (JazzClass e JazzProp)
	 * 
	 * @param annotation
	 * @return
	 */
	protected boolean isJazzAnnotation(final Annotation annotation) {
		return annotation.getType().getFullyQualifiedName().equals(JazzClass.class.getName())
		|| annotation.getType().getFullyQualifiedName().equals(JazzProp.class.getName());
	}
	
	/**
	 * Determina se o meta modelo de domínio deverá ser lido
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
	public Map<String, JavaClass> listDomain() {
		return this.listDomain(false);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.jazz.codegen.IDomainModelLoaderFactory#listDomain(boolean)
	 */
	public Map<String, JavaClass> listDomain(final boolean forceRefresh) {
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
