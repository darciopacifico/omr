/*
 * Data de Criacao 26/05/2005 11:17:34
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package br.com.dlp.framework.vo;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;

import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.jazz.codegen.annotation.JazzProp;

/**
 * Implementação básica de entidades de negócio
 * 
 * @author dpacifico
 * 
 * @param <P>
 */
@MappedSuperclass
@XmlType
public abstract class AbstractBaseVO<P extends Serializable> implements IBaseVO<P> {
	
	private static final long serialVersionUID = -8962470685172952645L;
	
	protected P pk;
	
	/**
	 * Para Obrigar e garantir o construtor vazio. Contrato JavaBeans
	 */
	public AbstractBaseVO() {
		// intencionalmente em branco
	}
	
	/**
	 * Construtor VO. Composição com IPK
	 * 
	 * @param pk
	 */
	@SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
	public AbstractBaseVO(final P pk) {
		this.setPK(pk);
	}
	
	/**
	 * Equals padrao para baseVO. Compara hashes das chaves
	 **/
	@Override
	public boolean equals(final Object arg) {
		
		if(arg==this){
			return true;
		}
		
		final P ppk = this.getPK();
		
		if (ppk != null && arg instanceof IBaseVO<?>) {
			final IBaseVO<?> baseVOarg = (IBaseVO<?>) arg;
			return ppk.equals(baseVOarg.getPK());
			
		}
		return false;
	}
	
	/**
	 * Implementacao padrao de hashcode. Utiliza do hash da chave do objeto
	 */
	@Override
	public int hashCode() {
		if (this.pk != null) {
			return this.pk.hashCode();
		}
		return super.hashCode();
	}
	
	
	/**
	 * setter pk
	 * 
	 * @param ppk
	 */
	public void setPK(final P ppk) {
		this.pk = ppk;
	}
	

	/**
	 * Recupera propriedade informada
	 * @param <B>
	 * @param <C>
	 * @param propertyName
	 * @param bs
	 * @return
	 */
	public static <B extends Object> Object getMin(String propertyName, Collection<B> bs, boolean inverse) {
		Object maxPropValue = null;
		
		if(!CollectionUtils.isEmpty(bs)){
			
			Comparator<B> comparator = new BeanComparator(propertyName);
			
			//apenas para evitar erros de concorrencia
			List<B> localList = new ArrayList<B>(bs.size());
			localList.addAll(bs);
			
			B b;
			
			if(!inverse){
				b=Collections.min(localList, comparator);
			}else{
				b=Collections.max(localList, comparator);
			}
					
			try {
				maxPropValue = PropertyUtils.getProperty(b, propertyName);
			} catch (IllegalAccessException e) {
				throw new JazzRuntimeException("Erro ao tentar acessar propriedade de bean!",e);
			} catch (InvocationTargetException e) {
				throw new JazzRuntimeException("Erro ao tentar acessar propriedade de bean!",e);
			} catch (NoSuchMethodException e) {
				throw new JazzRuntimeException("Erro ao tentar acessar propriedade de bean!",e);
			}
			
		}
		
		return maxPropValue;
	}
	
	
	

	/**
	 * Recupera propriedade informada
	 * @param <B>
	 * @param <C>
	 * @param propertyName
	 * @param bs
	 * @return
	 */
	public static <B extends Object> Object getMax(String propertyName, Collection<B> bs) {
		return getMin(propertyName, bs,true);
	}
	
	

	/**
	 * Recupera propriedade informada
	 * @param <B>
	 * @param <C>
	 * @param propertyName
	 * @param bs
	 * @return
	 */
	public static <B extends Object> Object getMin(String propertyName, Collection<B> bs) {
		return getMin(propertyName, bs,false);
	}
	
	/**
	 * ToString padrao.
	 */
	@Override
	public String toString() {
		String string = this.getClass().getSimpleName()+" pk="+this.getPK();
		return string;
	}
	
	/**
	 * 
	 */
	@Transient
	@Override
	@JazzProp(ignore=true, name = "novo")
	public boolean isNew(){
		return getPK()==null;
	}
	
	
}
