package br.com.mastersaf.util;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Service;


/**
 * Generic service implementation
 * @author Rodrigo Rodrigues <rodrigorodriguesweb arroba gmail.com>
 */
@Service("genericBo")
public class GenericBoImpl implements GenericBo {
	
	private Dao genericDao;

	public void setGenericDao(Dao genericDao) {
		this.genericDao = genericDao;
	}
	
	public Dao getDao() {
		return genericDao;
	}

	/**
	 * Persist a object
	 * @param object
	 * @throws Exception
	 */
	public void create(Bean bean) throws Exception {
		emptyObject(bean);
		createDynamicObject(bean);
		genericDao.create(bean);
	}

	/**
	 * Verify if relationship object's exist case no then try to insert relationship object's.
	 * @param object
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void createDynamicObject(Object object) throws Exception {
		if(UtilSS.verifyObject(object)){
			Method[] methods = object.getClass().getMethods();
			String methodName = null;
			Object objectReturn = null;
			List existObject = null;
			for(int i = 0; i < methods.length; i++){
				Method method = methods[i];
				methodName = method.getName();
				if(!methodName.equals("getClass") && methodName.startsWith("get")){
					objectReturn = method.invoke(object, new Object[]{});
					if(objectReturn instanceof Bean){
						existObject = get((Bean) objectReturn);
						if(existObject == null || existObject.isEmpty()){
							genericDao.create((Bean) objectReturn);
						}
					}
				}
			}
		}
	}

	/**
	 * Get a List using name of Class with Criteria
	 */
	public List<Bean> get(Class<? extends Bean> clazz) throws Exception {
		emptyObject(clazz);
		CriteriaExpression expression = new CriteriaExpression(clazz);
		return genericDao.get(expression);
	}

	/**
	 * Get a List using Object with Criteria
	 */
	public List<Bean> get(Bean bean) throws Exception {
		emptyObject(bean);
		CriteriaExpression expression = new CriteriaExpression(bean.getClass());
		expression.addFilters(UtilSS.getMapAttribute(bean), true);
		return genericDao.get(expression);
	}

	/**
	 * Remove a object from persistence
	 * @param object
	 * @throws Exception
	 */
	public void remove(Bean bean, Serializable object) throws Exception {
		emptyObject(bean);
		emptyObject(object);
		genericDao.remove(bean, object);
	}

	/**
	 * Update the data of object
	 * @param object
	 * @throws Exception
	 */
	public void save(Bean bean) throws Exception {
		emptyObject(bean);
		genericDao.save(bean);
	}

	/**
	 * Get a collection object from a criteria expression
	 * @param expression
	 * @return
	 * @throws Exception
	 */
	public List<Bean> get(CriteriaExpression expression) throws Exception {
		emptyObject(expression);
		return genericDao.get(expression);
	}

	/**
	 * Get a object with some agregate objects
	 * @param objectName
	 * @param id
	 * @param associations
	 * @return
	 * @throws Exception
	 */
	public Bean getByIdWithRelations(String objectName, Serializable id, String[] associations) throws Exception {
		emptyObject(objectName);
		emptyObject(id);
		emptyObject(associations);
		return genericDao.getByIdWithRelations(objectName, id, associations);
	}

	/**
	 * Return number of criteria
	 * @param expression
	 * @return
	 * @throws Exception
	 */
	public Long getCount(CriteriaExpression expression) throws Exception {
		emptyObject(expression);
		return genericDao.getCount(expression);
	}

	/**
	 * Return number of criteria
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public Long getCount(Class<? extends Bean> clazz) throws Exception {
		emptyObject(clazz);
		return genericDao.getCount(clazz);
	}

	/**
	 * Return number of criteria
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long getCount(Bean bean) throws Exception {
		emptyObject(bean);
		return genericDao.getCount(bean);
	}

	/**
	 * Return number of criteria
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Long getCountWithEquals(Bean bean) throws Exception {
		emptyObject(bean);
		return genericDao.getCountWithEquals(bean);
	}

	/**
	 * Get a list of object from a HSQL criteria
	 * @param hsql
	 * @return
	 * @throws Exception
	 */
	public List<Bean> getFromHql(String hsql) throws Exception {
		emptyObject(hsql);
		return genericDao.getFromHql(hsql);
	}

	/**
	 * Get a list of object from a SQL criteria
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<Bean> getFromSql(String sql) throws Exception {
		emptyObject(sql);
		return genericDao.getFromSql(sql);
	}

	/**
	 * Get a List of object with Relationships.
	 * @param objectName
	 * @param associations
	 * @return
	 * @throws Exception
	 */
	public List<Bean> getWithRelations(String objectName, String[] associations) throws Exception {
		emptyObject(objectName);
		emptyObject(associations);
		return genericDao.getWithRelations(objectName, associations);
	}

	/**
	 * Remove a object from persistence
	 * @param object
	 * @throws Exception
	 */
	public void remove(Bean bean) throws Exception {
		emptyObject(bean);
		genericDao.remove(bean);
	}

	/**
	 * Get a List using Object with Criteria where property's is equals
	 */
	public List<Bean> getWithEquals(Bean bean) throws Exception {
		emptyObject(bean);
		CriteriaExpression expression = new CriteriaExpression(bean.getClass());
		expression.addFilters(UtilSS.getMapAttribute(bean), false);
		return genericDao.get(expression);
	}

	/**
	 * Remove a List of Objects
	 * @param list
	 * @throws Exception
	 */
	public void remove(List<Bean> list) throws Exception {
		emptyObject(list);
		for(Bean bean : list){
			UtilSS.setObject(bean);
			remove(bean);
		}
	}

	/**
	 * Remove a List of Objects using with parameters property id of Object, property must by Serializable.
	 */
	public void remove(Bean bean, List<? extends Serializable> list) throws Exception {
		emptyObject(list);
		for (Serializable serializable : list) {
			remove(bean, serializable);
		}
	}

	/**
	 * Verify if object is null
	 * @param object
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void emptyObject(Object object) throws Exception {
		boolean exception = false;
		if(object == null){
			exception = true;
		} else if(object instanceof Collection){
			if( ((Collection) object).isEmpty() ){
				exception = true;
			}
		} else if(object instanceof String){
			if( ((String) object).trim().length() <= 0 ){
				exception = true;
			}
		} else if(object instanceof String[]){
			if( ((String[]) object).length <= 0 ){
				exception = true;
			}
		}
		
		if(exception){
			throw new Exception("Object is null.");
		}
	}

	public Session getSession() {
		return genericDao.getSession();
	}

}