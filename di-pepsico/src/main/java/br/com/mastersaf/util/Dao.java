/**
 * 
 */
package br.com.mastersaf.util;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

/**
 * Interface generic for Objects mappings
 * @author Rodrigo Rodrigues <rodrigorodriguesweb arroba gmail.com>
 */
public interface Dao extends Serializable {
	/**
	 * Persist a object
	 * @param object
	 * @throws Exception
	 */
	public void create(Bean bean) throws Exception;
	
	/**
	 * Remove a object from persistence
	 * @param object
	 * @throws Exception
	 */
	public void remove(Bean bean) throws Exception;
	
	/**
	 * Remove a object from persistence
	 * @param bean
	 * @param serializable
	 * @throws Exception
	 */
	public void remove(Bean bean, Serializable serializable) throws Exception;
	
	/**
	 * Update the data of object
	 * @param object
	 * @throws Exception
	 */
	public void save(Bean bean) throws Exception;
	
	/**
	 * Return number of criteria.
	 * @param expression
	 * @return
	 * @throws Exception
	 */
	public Long getCount(CriteriaExpression expression) throws Exception;
	
	/**
	 * Return number of criteria.
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public Long getCount(Class<? extends Bean> clazz) throws Exception;
	
	/**
	 * Return number of criteria.
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long getCount(Bean bean) throws Exception;
	
	/**
	 * Return number of criteria.
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long getCountWithEquals(Bean bean) throws Exception;
	
	/**
	 * Get a collection object from a criteria expression
	 * @param expression
	 * @return
	 * @throws Exception
	 */
	public List<Bean> get(CriteriaExpression expression) throws Exception;

	  /**
	   * Get a object with some agregate objects
	   * @param objectName Name of class to be selected  
	   * @param id ID of the object
	   * @param associations Name of attributes of agregate objects to be return together with the main object
	   * @param transaction actual transaction
	   */
	public Bean getByIdWithRelations(String objectName, Serializable id, String[] associations) throws Exception;

	  /**
	   * Get a list of object with some agregate objects. The id of  object need be id
	   * @param objectName Name of class to be selected  
	   * @param associations Name of attributes of agregate objects to be return together with the main object
	   * @param transaction actual transaction
	   * @throws Exception
	   */
	public List<Bean> getWithRelations(String objectName, String[] associations) throws Exception;
	
	/**
	 * Get a list of object from a HSQL criteria
	 * @param hsql
	 * @return
	 * @throws Exception
	 */
	public List<Bean> getFromHql(String hsql) throws Exception;

	/**
	 * Get a list of object from a SQL criteria
	 * @param hsql
	 * @return
	 * @throws Exception
	 */
	public List<Bean> getFromSql(String sql) throws Exception;
	
	public Session getSession();
}