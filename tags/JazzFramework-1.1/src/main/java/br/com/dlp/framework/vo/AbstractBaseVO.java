/*
 * Data de Criacao 26/05/2005 11:17:34
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package br.com.dlp.framework.vo;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import br.com.dlp.framework.exception.BaseError;
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
	
	private P pk;
	
	/**
	 * Para Obrigar e garantir o construtor vazio. Contrato JavaBeans
	 */
	public AbstractBaseVO() {
		
	}
	
	/**
	 * Construtor VO. Composição com IPK
	 * 
	 * @param pk
	 */
	public AbstractBaseVO(final P pk) {
		this.setPK(pk);
	}
	
	/**
	 * Equals padrao para baseVO. Compara hashes das chaves
	 **/
	@Override
	public boolean equals(final Object arg) {
		final P ppk = this.getPK();
		
		if (ppk != null && arg instanceof IBaseVO<?>) {
			final IBaseVO<?> baseVOarg = (IBaseVO<?>) arg;
			return ppk.equals(baseVOarg.getPK());
			
		}
		return false;
		
	}
	
	/**
	 * getter pk
	 * 
	 * @hibernate.id generator-class="native"
	 */
	@Id
	@TableGenerator(name = "tbsed", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JazzProp(name = "ID")
	public P getPK() {
		return this.pk;
	}
	
	/**
	 * Implementacao padrao de hashcode. Utiliza do hash da chave do objeto
	 */
	@Override
	public int hashCode() {
		if (this.pk != null) {
			return this.pk.hashCode();
		}
		
		return 0;
	}
	
	/**
	 * setter pk
	 * 
	 * @param ppk
	 */
	public void setPK(final P ppk) {
		if (ppk == null) {
			throw new BaseError("Nao e permitido atribuir uma implementacao de PK nula");
		}
		this.pk = ppk;
	}
	
	/**
	 * ToString padrao.
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
	
}
