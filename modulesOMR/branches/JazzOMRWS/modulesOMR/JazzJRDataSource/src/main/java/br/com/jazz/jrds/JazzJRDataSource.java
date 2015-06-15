package br.com.jazz.jrds;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.design.JRDesignField;

import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.jazz.jrds.annotations.Navigable;
import br.com.jazz.jrds.exceptions.JazzJRDSRuntimeException;

/**
 * Implementação de JRDataSource, com capacidade de fazer uma introspeccao na colecao de dados informados, 
 * navegando recursivamente pelos campos marcados com a anotação Navigable.
 * 
 *  Esta recursao cria um produto cartesiano dos relacionamentos do modelo, que pode ser utilizado para 
 *  carregar dados em relatórios do tipo JasperReports
 *  
 * @author darcio
 *
 * @param <T>
 */
public class JazzJRDataSource<T extends Class<?>> implements JRDataSource {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	private JazzJRNavigableField deeperField;
	private List<JazzJRNavigableField> fields;
	private Map<String,JazzJRNavigableField> fieldsMap=new HashMap<String, JazzJRNavigableField>(4);
	
	private Map<String, Object> parameters;
	
	public JazzJRDataSource(Collection data, T class1, Map<String, Object> parameters)  {
		
		fields = new ArrayList<JazzJRNavigableField>();
		
		this.parameters = parameters;
		JazzJRNavigableField firstNavigable = new JazzJRNavigableField(data,parameters);
		introspectNavigation(fields, firstNavigable, class1, 1);
	}

	/**
	 * Realiza a instrospeccao recursiva no modelo de dados, descobrindo os relacionamentos entre as entidades nos pontos marcados como Navigable, e estabelece um
	 * uma maneira de iterar o produto cartesiano destes relacionamentos.
	 * 
	 * @param properties
	 * @param parentField
	 * @param class1
	 * @param depth
	 */
	private void introspectNavigation(List<JazzJRNavigableField> properties, JazzJRNavigableField parentField, Class class1, int depth) {

		BeanInfo bi = getBeanInfo(class1);

		PropertyDescriptor[] pds = bi.getPropertyDescriptors();

		for (PropertyDescriptor propDesc : pds) {

			if (isNavigable(propDesc)) {
				Type nextType;

				if (isParameterizedCollection(propDesc)) {// atributo de relacionamento "many"
					
					nextType = getCollGenericType(propDesc);
					depth++;// incrementa a profundidade do atributo na arvore de relacionamento

				} else if (isParameterizedMap(propDesc)) {// atributo de relacionamento "many"
						
						nextType = getMapGenericType(propDesc);
						depth++;// incrementa a profundidade do atributo na arvore de relacionamento

				} else{ // atributo de relacionamento "One"
					nextType = propDesc.getReadMethod().getGenericReturnType();
				}

				introspectNavigation(properties, getNextField(parentField, propDesc, depth + 1), (Class) nextType, depth);

			} else {

				JazzJRNavigableField field = getNextField(parentField, propDesc, depth);

				// seta o field mais profundo do relacionamento, para que seja acionado o next através dele
				setDeeperField(field);

				// leitura de folha de arvore
				properties.add(field);
				String fieldName = field.getName();
				
				//System.out.println(""+fieldName);
				
				if(fieldsMap.get(fieldName)!=null){
					log.warn("Atenção!! Existe mais de um campo '"+fieldName+"' declarado!");
				}
				
				fieldsMap.put(fieldName, field);

			}
		}
	}



	/**
	 * @param class1
	 * @return
	 */
	protected BeanInfo getBeanInfo(Class class1) {
		BeanInfo bi;
		try {
			bi = Introspector.getBeanInfo(class1);
		} catch (IntrospectionException e) {
			throw new JazzJRDSRuntimeException("Erro ao tentar fazer instrospeccao em "+class1,e);
		}
		return bi;
	}

	
	/**
	 * Marca o ponto mais profundo do modelo, para que se acione o "next" da iteracao do modelo. 
	 * @param nextTypeProperty
	 */
	protected void setDeeperField(JazzJRNavigableField nextTypeProperty) {
		if (this.deeperField == null || nextTypeProperty.getDepth() > this.deeperField.getDepth()) {
			this.deeperField = nextTypeProperty;
		}
	}

	/**
	 * Registra mais um campo encontrado no modelo de dados.
	 * @param parentField
	 * @param pd
	 * @param depth
	 * @return
	 */
	private JazzJRNavigableField getNextField(JazzJRNavigableField parentField, PropertyDescriptor pd, int depth) {
		JazzJRNavigableField field = new JazzJRNavigableField(parentField, pd, depth,this.parameters);
		return field;
	}

	
	/**
	 * Determina o tipo generico declarado numa colecao
	 * @param pd
	 * @return
	 */
	private Type getCollGenericType(PropertyDescriptor pd) {
		Type nextType = null;

		Type collType = pd.getReadMethod().getGenericReturnType();

		Type[] types = ((ParameterizedType) collType).getActualTypeArguments();

		if (types.length == 1) {

			nextType = types[0];

		} else if (types.length > 1) {
			throw new JazzJRDSRuntimeException("Nao é possivel processar um tipo generico com mais de uma definicao de type argumens. (" + pd + ")");
		} else if (types.length == 1) {
			throw new JazzJRDSRuntimeException("Nao é possivel processar um tipo definido como generico e sem nenhum type argument definido (" + pd + "). Utilize a anotacao @Navigable!");
		}
		return nextType;
	}
	
	
	
	/**
	 * Determina o tipo generico declarado numa colecao
	 * @param pd
	 * @return
	 */
	private Type getMapGenericType(PropertyDescriptor pd) {
		Type nextType = null;

		Type collType = pd.getReadMethod().getGenericReturnType();

		Type[] types = ((ParameterizedType) collType).getActualTypeArguments();

		if (types.length == 2) {

			nextType = types[1];

		} else if (types.length > 2) {
			throw new JazzJRDSRuntimeException("Nao é possivel processar um tipo mapa generico com mais de duas definicões de type argumens. (" + pd + ")");
		} else if (types.length <2) {
			throw new JazzJRDSRuntimeException("Nao é possivel processar um tipo definido como mapa generico com menos de dois type arguments (" + pd + "). Utilize a anotacao @Navigable!");
		}
		return nextType;
	}

	
	/**
	 * Determina se na marcaçao de navigable houve a determinacao forcada do proximo tipo
	 * @param pd
	 * @return
	 */
	private boolean isForceNextType(PropertyDescriptor pd) {
		Navigable navigable = pd.getReadMethod().getAnnotation(Navigable.class);

		return (navigable != null && navigable.nextType() != Navigable.class);

	}

	
	/**
	 * Testa se a propriedade é uma coleção generica
	 * @param pd
	 * @return
	 */
	private boolean isParameterizedCollection(PropertyDescriptor pd) {
		Class<?> returnType = pd.getReadMethod().getReturnType();
		Type type = pd.getReadMethod().getGenericReturnType();

		return Collection.class.isAssignableFrom(returnType) && type instanceof ParameterizedType;
	}
	
	
	/**
	 * Testa se a propriedade é uma coleção generica
	 * @param pd
	 * @return
	 */
	private boolean isParameterizedMap(PropertyDescriptor pd) {
		Class<?> returnType = pd.getReadMethod().getReturnType();
		Type type = pd.getReadMethod().getGenericReturnType();

		return Map.class.isAssignableFrom(returnType)&& type instanceof ParameterizedType;
	}

	
	/**
	 * testa se a propriedade é navegável
	 * @param pd
	 * @return
	 */
	private boolean isNavigable(PropertyDescriptor pd) {
		Navigable navigable = pd.getReadMethod().getAnnotation(Navigable.class);
		return navigable != null && navigable.navigable();
	}

	
	/**
	 * Recupera todos os campos descobertos no relacionamento
	 * @return
	 */
	public JRField[] getFields(){
		
		JRField[] keyFields = new JRDesignField[this.fields.size()];
		
		int i=0;
		for (JazzJRNavigableField jrField : this.fields) {
			
			String name = jrField.getName();
			JRDesignField jrDesignField = new JRDesignField();
			jrDesignField.setName(name);
			jrDesignField.setDescription(jrField.getDescription());
			
			Class<?> valueClass = jrField.getValueClass();
			valueClass = ClassUtils.primitiveToWrapper(valueClass);
			
			
			
			jrDesignField.setValueClass(valueClass);
			jrDesignField.setValueClassName(valueClass.getName());
			
			keyFields[i] = jrDesignField;
			
			
			//System.out.println("<field name=\""+name+"\" class=\""+valueClass.getName()+"\"/>");
			
			//<field name="exv_pk" class="java.lang.Long"/>
			
			i++;
		}
		
		return keyFields;
	}
	

	/**
	 * Move o estado do DS para o próximo registro, caso haja.
	 */
	public boolean hasNext() throws JRException {
		return this.deeperField.hasNext();
	}
	
	/**
	 * Move o estado do DS para o próximo registro, caso haja.
	 */
	public boolean next() throws JRException {
		return this.deeperField.next();
	}
	
	/**
	 * Recupera o valor do campo em questão..
	 */
	public Object getFieldValue(JRField jrField) throws JRException {
		
		String nome = getName(jrField);
		
		JazzJRNavigableField myField;
		try{
			myField = this.fieldsMap.get(nome); 
		}catch(Exception e){
			log.error("Erro ao tentar recuperar o valor do campo "+nome,e);
			return null;
		}
		 
		
		if(myField!=null){
			Object value =null;
			
			try{
				value = myField.getValue();
			}catch(Exception e){
				log.error("Erro ao tentar recuperar o valor do campo "+nome,e);
				return null;
			}
			
			return value; 
		}else{
			
			//log.error("O campo '"+nome+"' nao foi encontrado! É provavel que tenha ocorrido algum erro ao tentar acessar o seu valor. Verifique o Log!");
			return null;
		}
	}


	/**
	 * @param jrField
	 * @return
	 */
	protected String getName(JRField jrField) {
		String nome=null;
		if(jrField!=null){
			nome= jrField.getName();
		}
		return nome;
	}
	
	
}
