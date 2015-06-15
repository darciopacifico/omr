package br.com.jazz.jrds;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.design.JRDesignField;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.jazz.jrds.annotations.Navigable;
import br.com.jazz.jrds.exceptions.JazzJRDSRuntimeException;

/**
 * Implementação de JRField, a partir de um modelo de estrutura de dados em lista de JavaBeans (POJOs). 
 * 
 * Este objeto é criado a partir da introspecção dos relacionamentos de uma estrutura de dados, passados como argumento
 * na construção de br.com.jazz.jrds.JazzJRDataSource<T>. Esta introspecção é feita recursivamente...
 *   
 * @author darcio
 *
 */
/**
 * @author darcio
 * 
 */
public class JazzJRNavigableField extends JRDesignField implements JRField {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	private String name;

	private Navigable navigable;

	private NavigableVisitor visitor;

	private Iterator iterator;
	private Collection rootValue;
	private Object iteratorValue;
	private Map<String, Object> parameters;

	boolean hasNext = false;
	private int depth;

	private JazzJRNavigableField parentField;

	private PropertyDescriptor propertyDescriptor;

	/**
	 * Construtor que tem como argumento o elemento raiz da estrutura de dados em árvore.
	 * 
	 * @param rootValue
	 */
	public JazzJRNavigableField(Collection rootValue, Map<String, Object> parameters) {
		this.hasNext = CollectionUtils.isNotEmpty(rootValue);
		this.rootValue = rootValue;
		this.parameters = parameters;
	}

	/**
	 * Construtor para subelementos da arvore de campos
	 * 
	 * @param parentField
	 * @param propertyDescriptor
	 * @param depth
	 */
	public JazzJRNavigableField(JazzJRNavigableField parentField, PropertyDescriptor pd, int depth,Map<String, Object> parameters) {
		super();

		this.navigable = pd.getReadMethod().getAnnotation(Navigable.class);
		this.name = getName(parentField, pd);
		this.parentField = parentField;
		this.propertyDescriptor = pd;
		this.depth = depth;
		this.parameters = parameters;
	}

	/**
	 * Retorna o valor do campo, recuperandos, caso necessário, os valores dos pais recursivamente.
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Object getValue() {
		Object returnVal;

		if (parentField == null || ((isCollection() || isMap()) && isNavigable(propertyDescriptor))) {
			returnVal = getIteratorValue();

			if (log.isDebugEnabled()) {
				if (propertyDescriptor != null) {
					log.debug("propriedade " + propertyDescriptor.getName() + " É colecao???");
				} else {
					log.debug("propertyDescriptor==null");
				}
			}

		} else {
			returnVal = getSimpleValue();

			if (log.isDebugEnabled()) {
				log.error("propriedade " + propertyDescriptor.getName() + " NAO é colecao???");
			}
		}

		return returnVal;

	}

	/**
	 * Testa se a propriedade é navegável
	 * 
	 * @param pd
	 * @return
	 */
	private boolean isNavigable(PropertyDescriptor pd) {
		// Navigable navigable = pd.getReadMethod().getAnnotation(Navigable.class);
		return navigable != null && navigable.navigable();
	}

	/**
	 * Recupera o valor deste campo, considerando q este seja um campo não collection
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private Object getSimpleValue() {
		Object obj = parentField.getValue();

		if (obj == null) {
			return null;
		}

		Object result;
		Method readMethod = propertyDescriptor.getReadMethod();
		try {
			result = readMethod.invoke(obj);
		} catch (Exception e) {
			log.error("Erro ao tentar recuperar o valor do metodo " + readMethod + " do objeto " + obj, e);
			return null;
		}

		return result;
	}

	/**
	 * Recupera o valor da posição (estado) de um iterator de uma coleção
	 * 
	 * @return
	 */
	public Object getIteratorValue() {
		if (this.iterator == null) {
			Collection coll = getCollection();

			createIterator(coll);

			if (this.iterator.hasNext()) {
				this.iteratorValue = this.iterator.next();
			}
		}

		return this.iteratorValue;
	}

	public Object getActualValue() {
		return this.iteratorValue;
	}

	/**
	 * 
	 * @param coll
	 */
	protected void createIterator(Collection coll) {

		if(this.navigable==null){
			this.iterator = coll.iterator();
			return;
		}
		
		Class visitorClass = this.navigable.visitorClass();
		
		if(visitorClass!=null && !Navigable.class.equals(visitorClass)){
			
			if(this.visitor==null){
				try {
					this.visitor = (NavigableVisitor) visitorClass.newInstance();
				} catch (Exception e) {
					throw new JazzJRDSRuntimeException("erro ao tentar instanciar visitor "+visitorClass, e);
				}
			}

			this.iterator = this.visitor.createIterator(this, coll);
			
			
		}else{
			this.iterator = coll.iterator();
		}
		
		

	}


	/**
	 * 
	 * @return
	 */
	private Iterator getCachedIterator() {
		if (this.iterator == null) {
			Collection coll = getCollection();

			createIterator(coll);

		}
		return this.iterator;
	}

	/**
	 * Recupera o valor deste campo como uma coleção
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private Collection getCollection() {

		if (parentField == null && (rootValue instanceof Collection || rootValue instanceof Map)) {

			if (rootValue instanceof Map) {
				Collection values = ((Map) rootValue).values();
				return values;

			} else {
				return rootValue;
			}

		} else {

			Object parentVal = parentField.getValue();
			Method readMethod = null;
			Object object = null;
			try {
				readMethod = propertyDescriptor.getReadMethod();
				object = readMethod.invoke(parentVal);

				if (object instanceof Collection) {
					return (Collection) object;

				} else if (object instanceof Map) {
					Collection mapValues = ((Map) object).values();
					return mapValues;

				} else {
					throw new JazzJRDSRuntimeException("Erro! O valor retornado por " + readMethod + " nao é colecao ou mapa!");
				}

			} catch (Exception e) {

				log.error("propertpropertyDescriptor.getName = " + propertyDescriptor.getName());
				log.error("readMethod=" + readMethod);
				log.error("parentVal=" + parentVal);
				log.error("parentVal.class=" + parentVal.getClass());

				throw new JazzJRDSRuntimeException("Erro ao tentar recuperar valor do campo!", e);
				// return new ArrayList();
			}

		}

	}

	/**
	 * Testa se o campo atual é do tipo colecao
	 * 
	 * @return
	 */
	private boolean isCollection() {

		if (parentField == null) {
			return true;

		}

		Method readMethod = propertyDescriptor.getReadMethod();

		if (readMethod == null) {
			throw new JazzJRDSRuntimeException("ReadMethod ==null. nao e possivel tratar uma propriedade que nao possui readMethod");
		}

		return Collection.class.isAssignableFrom(readMethod.getReturnType());

	}

	/**
	 * Testa se o campo atual é do tipo colecao
	 * 
	 * @return
	 */
	private boolean isMap() {

		if (parentField == null) {
			return false;
		}

		Method readMethod = propertyDescriptor.getReadMethod();

		if (readMethod == null) {
			throw new JazzJRDSRuntimeException("ReadMethod ==null. nao e possivel tratar uma propriedade que nao possui readMethod");
		}

		return Map.class.isAssignableFrom(readMethod.getReturnType());

	}

	/**
	 * Recupera o nome deste campo
	 * 
	 * @param parentField
	 * @param propertyDescriptor
	 * @return
	 */
	protected String getName(JazzJRNavigableField parentField, PropertyDescriptor pd) {

		// Navigable navigable = pd.getReadMethod().getAnnotation(Navigable.class);
		if (navigable != null && navigable.alias().length() > 0) {
			return navigable.alias();
		}

		if (parentField != null && parentField.getName() != null) {
			name = parentField.getName() + "." + pd.getName();
		} else {
			name = pd.getName();
		}
		return name;
	}

	/**
	 * Navega os iterators recursivamente para a próxima posição.
	 * 
	 * Este método é o que dá o efeito de produto cartesiano nos relacionamentos das estruturas de dados.
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public boolean next() {

		if (!hasNext()) {
			return false;
		}

		if (isCollection() || isMap()) {

			this.hasNext = this.getCachedIterator().hasNext();

			if (this.hasNext) {
				this.iteratorValue = this.iterator.next();

			} else if (this.parentField != null) {
				this.hasNext = this.parentField.next();

				if (this.hasNext) {
					this.iterator = null;
				}

			}

		} else {
			this.hasNext = this.parentField.next();
		}
		return this.hasNext;
	}

	/**
	 * Testa se há um proximo registro na estrutura de forma recursiva.
	 * 
	 * @return
	 */
	public boolean hasNext() {

		if (this.hasNext) {
			return true;

		} else if (this.parentField != null) {
			return this.parentField.hasNext();

		} else {
			return false;
		}

	}

	/**
	 * Recupera o descritor de propriedade referente a este campo na estrutura de dados. Serve para acionar os métodos acessores da propriedade, determinar o tipo da propriedade etc...
	 * 
	 * @return
	 */
	public PropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.JRField#getValueClass()
	 */
	@Override
	public Class<?> getValueClass() {
		if (isNavigable(this.propertyDescriptor) && isCollection() ) {
			
			return getCollectionGenericReturnType();
			
		} else if (isNavigable(this.propertyDescriptor) && isMap()) {
			
			return getMapGenericReturnType();
			
		} else {
			
			
			return propertyDescriptor.getPropertyType();
		}
	}

	/**
	 * Retorna o tipo genérico que está contido na colecao generics
	 * 
	 * @return
	 */
	public Class<?> getCollectionGenericReturnType() {
		Type collType = propertyDescriptor.getReadMethod().getGenericReturnType();

		Type[] types = ((ParameterizedType) collType).getActualTypeArguments();

		Type nextType = null;

		if (types.length == 1) {

			nextType = types[0];

		} else if (types.length > 1) {
			throw new JazzJRDSRuntimeException("Nao é possivel processar um tipo generico com mais de uma definicao de type argumens. (" + propertyDescriptor + ")");
		} else if (types.length < 1) {
			throw new JazzJRDSRuntimeException("Nao é possivel processar um tipo definido como generico e sem nenhum type argument definido (" + propertyDescriptor + "). Utilize a anotacao @Navigable!");
		}

		return (Class<?>) nextType;
	}


	/**
	 * Retorna o tipo genérico que está contido na colecao generics
	 * 
	 * @return
	 */
	public Class<?> getMapGenericReturnType() {
		Type collType = propertyDescriptor.getReadMethod().getGenericReturnType();

		Type[] types = ((ParameterizedType) collType).getActualTypeArguments();

		Type nextType = null;

		if (types.length == 2) {

			nextType = types[1];

		} else if (types.length > 2) {
			throw new JazzJRDSRuntimeException("Nao é possivel processar um tipo mapa generico com mais de duas definicoes de type argumens. (" + propertyDescriptor + ")");
		} else if (types.length < 2) {
			throw new JazzJRDSRuntimeException("Nao é possivel processar um tipo definido como generico com menos de duas definicoes(" + propertyDescriptor + "). Utilize a anotacao @Navigable!");
		}

		return (Class<?>) nextType;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.JRField#getValueClassName()
	 */
	@Override
	public String getValueClassName() {
		return getValueClass().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.JRField#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public JazzJRNavigableField getParentField() {
		return parentField;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param parentField
	 */
	public void setParentField(JazzJRNavigableField parentField) {
		this.parentField = parentField;
	}

	/**
	 * @param propertyDescriptor
	 */
	public void setPropertyDescriptor(PropertyDescriptor pd) {
		this.propertyDescriptor = pd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Retorna a profundidade deste campo na estrutura de dados.
	 * 
	 * @return
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * @param depth
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

}
