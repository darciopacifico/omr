package br.com.dlp.framework.vo;

import java.io.Serializable;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlType;

import br.com.jazz.codegen.annotation.JazzProp;

/**
 * Interface para implementação de contrato básico de entidade de negócio.
 * 
 * @author dpacifico
 * 
 * @param <PK>
 */
@XmlType
public interface IBaseVO<PK extends Serializable> extends Serializable {
	/**
	 * @return
	 */
	PK getPK();
	
	/**
	 * 
	 */
	void setPK(PK pk);
	
	
	/**
	 * 
	 * @return
	 */
	@Transient
	@JazzProp(ignore=true, name = "novo")
	boolean isNew();
	
}
