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
import com.thoughtworks.qdox.directorywalker.FileVisitor;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.BeanProperty;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.parser.ParseException;

/**
 * Abstrai a localização e leitura do modelo de dominio que será utilizado na geração de código. FIXME: IMPLEMENTAR REGRA DE CACHE
 * DE MODELO DE DOMíNIO SE NECESSáRIO
 * 
 * @author darcio
 * 
 */
public class DomainModelLoaderFactory implements Serializable, IDomainModelLoaderFactory {

	private static final long serialVersionUID = 2525529382538031403L;

	private JavaDocBuilder javaDocBuilder = new JavaDocBuilder();
	private String[] sourceTress;
	private String regexFilter;

	private Map<String, JavaClass> domain = new HashMap<String, JavaClass>(10);

	/**
	 * Informar os diretórios onde estarão os fontes java do modelo de domínio
	 * 
	 * @param sourceTrees
	 *          diretórios onde estão os fontes do modelo de domínio
	 */
	public DomainModelLoaderFactory(String... sourceTrees) {
		// regex que significa qqr coisa com ao menos um caracter
		this(".+", sourceTrees);
		this.javaDocBuilder.setErrorHandler(new JavaDocBuilder.ErrorHandler() {

			public void handle(ParseException parseException) {
				parseException.printStackTrace();
			}
		});

	}

	/**
	 * Informar a expressao regular que filtra as classes dos diretórios e os diretórios fonte
	 * 
	 * @param regexFilter
	 *          expressao regular para filtrar classes
	 * @param sourceTrees
	 *          diretórios onde estão os fontes do modelo de domínio
	 */
	public DomainModelLoaderFactory(String regexFilter, String... sourceTrees) {
		this.regexFilter = regexFilter;
		this.sourceTress = sourceTrees;
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
	public Map<String, JavaClass> listDomain(boolean forceRefresh) {
		if (isRefreshModel(forceRefresh)) {

			FileVisitor fileVisitor = new FileVisitor() {
				public void visitFile(File file) {
					System.out.println(file.getAbsolutePath());
				}
			};

			for (String sourceTree : this.sourceTress) {
				javaDocBuilder.addSourceTree(new File(sourceTree), fileVisitor);
			}

			Searcher searcher = getRegexSearcher(this.regexFilter);
			List<JavaClass> javaClasses = javaDocBuilder.search(searcher);

			for (JavaClass javaClass : javaClasses) {
				try {
					fixQDoxAnnotations(javaClass);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException("Erro ao tentar ajustar os valores default de annotations", e);
				}

				this.domain.put(javaClass.getFullyQualifiedName(), javaClass);
			}
		}

		return this.domain;
	}

	/**
	 * Atribui os valores default para cada atributo das anotações. Aparentemente, por um bug, o QDox não consegue reconhecer
	 * valores default quando o atributo não é informado.
	 * 
	 * @param javaClass
	 * @throws ClassNotFoundException
	 */
	protected void fixQDoxAnnotations(JavaClass javaClass) throws ClassNotFoundException {
		fixQDoxAnnotations(javaClass.getAnnotations());

		JavaMethod[] methods = javaClass.getMethods();

		for (JavaMethod javaMethod : methods) {
			fixQDoxAnnotations(javaMethod.getAnnotations());
		}
	}

	/**
	 * Atribui os valores default para cada atributo das anotações. Aparentemente, por um bug, o QDox não consegue reconhecer
	 * valores default quando o atributo não é informado.
	 * 
	 * @param annotations
	 * @throws ClassNotFoundException
	 */
	protected void fixQDoxAnnotations(Annotation[] annotations) throws ClassNotFoundException {
		for (Annotation annotation : annotations) {
			if (isJazzAnnotation(annotation)) {
				attributeDefaultValues(annotation);
			}
		}
	}

	protected static Pattern isJazzEnum = Pattern.compile("^.*(ComparisonOperator|Renderer).*$");

	public static void main(String[] args) {
		
		String string = "omparisonOperatorRendere";
		
		System.out.println(	isJazzEnum.matcher(string).matches()		);
		
	}
	
	/**
	 * Atribui os valores default para cada atributo das anotações. Aparentemente, por um bug, o QDox não consegue reconhecer
	 * valores default quando o atributo não é informado.
	 * 
	 * @param annotation
	 * @param annotClass
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 */
	protected void attributeDefaultValues(Annotation annotation) throws SecurityException, ClassNotFoundException {

		Class annotClass = (Class) getAnnotClass(annotation);

		// Os métodos aqui, na verdade, são os atributos das anotações
		for (Method attribute : annotClass.getMethods()) {

			Object value = getNamedParameter(annotation, attribute);
			// checa se já possui um valor atribuido. Caso contrário, infelizmente, devemos atribuir o valor default manualmente.
			if (value == null) {

				Object defaultValue = attribute.getDefaultValue();

				if (defaultValue != null) {
					// possui valor default
					annotation.getNamedParameterMap().put(attribute.getName(), ""+defaultValue);
				}
			}else{
				
				if(value instanceof String){
					
					String strValue = (String) value;
					
					Matcher matcher = isJazzEnum.matcher(strValue);
					if(matcher.matches()){
						String[] splittedValue =  strValue.split("\\.");
						strValue = splittedValue[splittedValue.length-1];
						annotation.getNamedParameterMap().put(attribute.getName(), strValue);
					}
					
					
				}
				
				
			}

		}
	}

	
	
	/**
	 * Retorna a Class (java reflection puro) para a anotation informada
	 * @param annotation
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected Class<?> getAnnotClass(Annotation annotation) throws ClassNotFoundException {
		return Class.forName(annotation.getType().getFullyQualifiedName());
	}

	/**
	 * Verifica se a anotação informada é do tipo de anotação do gerador de código Jazz (JazzClass e JazzProp)
	 * @param annotation
	 * @return
	 */
	protected boolean isJazzAnnotation(Annotation annotation) {
		return
		annotation.getType().getFullyQualifiedName().equals(JazzClass.class.getName()) ||
		annotation.getType().getFullyQualifiedName().equals(JazzProp.class.getName());
	}

	/**
	 * @param annotation
	 * @param attribute
	 * @return
	 */
	protected Object getNamedParameter(Annotation annotation, Method attribute) {
		return annotation.getNamedParameter(attribute.getName());
	}

	/**
	 * Determina se o meta modelo de domínio deverá ser lido
	 * 
	 * @param forceRefresh
	 * @return
	 */
	protected boolean isRefreshModel(boolean forceRefresh) {
		return forceRefresh || this.domain == null || this.domain.isEmpty();
	}

	/**
	 * Cria um searcher baseado em regex
	 * 
	 * @param regexFilter
	 * @return
	 */
	protected Searcher getRegexSearcher(final String regexFilter) {
		Searcher searcher = new Searcher() {
			public boolean eval(JavaClass cls) {
				String nome = cls.getName();
				return nome.matches(regexFilter);
				// return true;
			}
		};
		return searcher;
	}

	public Object getAnnotationValue(JavaClass javaClass, String annotName, String attribute) throws GeradorException{
		
		String className = javaClass.getFullyQualifiedName();
		
		Class clazz = forName(className);
		
		Class annClazz = forName(annotName);
		
		java.lang.annotation.Annotation annotation = clazz.getAnnotation(annClazz);
		
		Object value=getValue(annotation,attribute);
		
		return value;
		
	}


	public Object getAnnotationValue(BeanProperty beanProperty, String annotName, String attribute) throws GeradorException{
		
		JavaMethod method = beanProperty.getAccessor();
		if(method==null){
			method = beanProperty.getMutator();
		}
		
		return getAnnotationValue(method, annotName, attribute);
	}
	
	public Object getAnnotationValue(JavaMethod javaMethod, String annotName, String attribute) throws GeradorException{
		
		JavaClass javaClass = javaMethod.getParentClass();
		
		String className = javaClass.getFullyQualifiedName();
		
		Class clazz = forName(className);
		
		Method method = getMethod(javaMethod, className, clazz);
		
		Class annClazz = forName(annotName);
		
		java.lang.annotation.Annotation annotation = method.getAnnotation(annClazz);
		
		Object value=getValue(annotation,attribute);
		
		return value;
		
	}

	/**
	 * @param javaMethod
	 * @param className
	 * @param clazz
	 * @return 
	 * @throws GeradorException
	 */
	protected Method getMethod(JavaMethod javaMethod, String className, Class clazz) throws GeradorException {
		Method method;
		
		String name = javaMethod.getName();
		try {
			
			method = clazz.getMethod(name, getParams(javaMethod));
			
		} catch (SecurityException e) {
			throw new GeradorException("Erro ao tentar recuperar o metodo '"+name+"' da classe '"+className+"'!",e);
		} catch (NoSuchMethodException e) {
			throw new GeradorException("Metodo '"+name+"' da classe '"+className+"' nao encontrado! ",e);
		}
		return method;
	}

	protected Class[] getParams(JavaMethod javaMethod) throws GeradorException {
		JavaParameter[] params = javaMethod.getParameters();
		
		Class[] paramsReflect = new Class[params.length];
		int i=0;
		
		for (JavaParameter javaParameter : params) {
			Class clazz;
			try {
				clazz = forName(javaParameter.getType().getFullyQualifiedName());
				paramsReflect[i++] = clazz;
			
			} catch (GeradorException e) {
				throw new GeradorException("",e);
			}
		}
			
		return paramsReflect;
	}

	/**
	 * @param className
	 * @return
	 * @throws GeradorException 
	 * @throws ClassNotFoundException
	 */
	protected Class<?> forName(String className) throws GeradorException  {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new GeradorException("Classe '"+className+"' nao encontrada! Verifique se a mesma está no classpath!",e);
		}
	}

	/**
	 * 
	 * @param annotation
	 * @param attribute
	 * @return
	 * @throws GeradorException
	 */
	protected Object getValue(java.lang.annotation.Annotation annotation, String attribute) throws GeradorException{
		
		if(annotation==null || StringUtils.isBlank(attribute)){
			return "";
		}
		
		Method method;
		try {
			method = annotation.getClass().getMethod(attribute, new Class[]{});
		} catch (SecurityException e) {
			throw new GeradorException("Erro ao tentar acessar o atributo '"+attribute+"' da anotacao '"+annotation+"' !",e);
		} catch (NoSuchMethodException e) {
			throw new GeradorException("Atributo '"+attribute+"' da anotacao '"+annotation+"' não encontrado!",e);
		}
		
		
		Object value;
		try {
			value = method.invoke(annotation);
		} catch (IllegalArgumentException e) {
			throw new GeradorException("Erro ao tentar acessar o atributo '"+attribute+"' da anotacao '"+annotation+"' !",e);

		} catch (IllegalAccessException e) {
			throw new GeradorException("Erro ao tentar acessar o atributo '"+attribute+"' da anotacao '"+annotation+"' !",e);
		
		} catch (InvocationTargetException e) {
			throw new GeradorException("Erro ao tentar acessar o atributo '"+attribute+"' da anotacao '"+annotation+"' !",e);

		}
		
		return value;
	}

}
