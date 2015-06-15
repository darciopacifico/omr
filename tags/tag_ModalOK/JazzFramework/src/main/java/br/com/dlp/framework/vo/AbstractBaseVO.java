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
	public AbstractBaseVO(P pk) {
		setPK(pk);
	}

	/**
	 * getter pk
	 * 
	 * @hibernate.id generator-class="native"
	 */
	@Id
	@TableGenerator(name = "tbsed", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.IDENTITY )
	@JazzProp(name="ID")
	public P getPK() {
		return this.pk;
	}

	/**
	 * setter pk
	 * 
	 * @param pk
	 */
	public void setPK(P pk) {
		if (pk == null) {
			throw new BaseError("Nao e permitido atribuir uma implementacao de PK nula");
		}
		this.pk = pk;
	}

	/**
	 * Equals padrao para baseVO. Compara hashes das chaves
	 **/
	public boolean equals(Object arg) {
		P pk = getPK();

		if (arg != null && pk != null && (arg instanceof IBaseVO<?>)) {
			IBaseVO<?> baseVOarg = (IBaseVO<?>) arg;
			return pk.equals(baseVOarg.getPK());

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

		return 0;
	}

	/**
	 * ToString padrao.
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}