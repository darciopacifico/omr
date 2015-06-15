/**********************************************************************
 * Copyright (C) - 2009 MasterSaf
 * 
 * Projeto: Validador
 * Arquivo: IDAOGenerico.java
 * @date 11/02/2009
 * @author Everton Amaral - SBR
 *  
 ***********************************************************************/

package com.msaf.validador.integration.hibernate.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

// TODO: Auto-generated Javadoc
/**
 * Interface IDAOGenerico.
 */
public interface IDAOGenerico<T, ID extends Serializable> {
	
	/**
	 * Find all.
	 * 
	 * @return the list< t>
	 */
	List<T> findAll();

	
	/**
	 * Merge.
	 * 
	 * @param entity the entity
	 */
	void merge(T entity);
	
	/**
	 * Update.
	 * 
	 * @param entity the entity
	 */
	void update(T entity);

	
	/**
	 * Clear.,
	 * 
	 * @param entity the entity
	 */
	void delete(T entity);
	
	

	/**
	 * Make persistent.
	 * 
	 * @param entity the entity
	 * 
	 * @return the t
	 */
	T makePersistent(T entity);


	public T findById(ID id);


	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public T findById(ID id, boolean lock);
	
	
	
	
}
