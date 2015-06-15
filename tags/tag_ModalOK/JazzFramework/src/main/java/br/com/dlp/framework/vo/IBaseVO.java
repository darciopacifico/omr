package br.com.dlp.framework.vo;

import java.io.Serializable;


/**
 * Interface para implementação de contrato básico de entidade de negócio. 
 * 
 * @author dpacifico
 *
 * @param <PK>
 */
public interface IBaseVO<PK extends Serializable> extends Serializable {
	/**
	 * @return
	 */
	public PK getPK();
	
	/**
	 * 
	 */
	public void setPK(PK pk);
}
