/**
 * 
 */
package br.com.mastersaf.util;

import java.io.Serializable;
import java.util.List;


/**
 * @author RRSANTOS
 *
 */
public interface GenericBo extends Dao {
	/**
	 * Get a List using Object with Criteria
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public List<Bean> get(Bean bean) throws Exception;
	
	/**
	 * Get a List using name of Class with Criteria
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public List<Bean> get(Class<? extends Bean> clazz) throws Exception;
	
	/**
	 * Get a List using Object with Criteria where property's is equals
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public List<Bean> getWithEquals(Bean bean) throws Exception;
	
	/**
	 * Remove a List of Objects
	 * @param list
	 * @throws Exception
	 */
	public void remove(List<Bean> list) throws Exception;
	
	public void remove(Bean bean, List<? extends Serializable> list) throws Exception;
}
