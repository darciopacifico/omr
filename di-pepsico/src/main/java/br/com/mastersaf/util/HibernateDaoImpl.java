package br.com.mastersaf.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.jpa.support.JpaDaoSupport;

/**
 * Generic dao implementation for basic data operations with Hibernate
 * @author Rodrigo Rodrigues <rodrigorodriguesweb arroba gmail.com>
 */
public class HibernateDaoImpl extends JpaDaoSupport implements Dao {

  /**
   * Persist a object
   * @param transaction actual transaction 
   */
	public void create(Bean bean) throws Exception {
		getJpaTemplate().persist(bean);
  	}

	public void remove(Bean bean, Serializable serializable) throws Exception {
		getJpaTemplate().remove(getJpaTemplate().getReference(bean.getClass(), serializable));
	}
	
  /**
   * Remove a object from persistence
   * @param object
   * @param transaction
   * @throws Exception
   */
	public void remove(Bean bean) throws Exception {
		Session session = (Session) getJpaTemplate().getEntityManagerFactory().createEntityManager().getDelegate();
		session.delete(bean);
		session = null;
	}

  /**
   * Update the data of object
   * @param transaction actual transaction
   */
	public void save(Bean bean) throws Exception {
		getJpaTemplate().merge(bean);
	}
  
  /**
   * Return number of criteria
   * @param expression
   * @param transaction
   * @return
   * @throws Exception
   */
	@SuppressWarnings("unchecked")
	public Long getCount(CriteriaExpression expression) throws Exception {
		Session session = (Session) getJpaTemplate().getEntityManagerFactory().createEntityManager().getDelegate();
		Number count = null; 
		String instanceObject = expression.getObjectName();
		Criteria criteria = session.createCriteria(Class.forName(instanceObject));
	
		Collection filters = expression.getFilters();
		if(filters!=null){
			Iterator it = filters.iterator();
			while(it.hasNext()){
				Filter filter = (Filter)it.next();
				criteria.add(getResrictionFromCriteria(filter)); 
			}
		}
		count = (Number) criteria.setProjection(Projections.rowCount()).uniqueResult();
		session = null;
		return new Long(count.longValue());
	}
  
  /**
   * Get a collection object from a criteria expression
   * @param transaction actual transaction
   */
	@SuppressWarnings("unchecked")
	public List get(CriteriaExpression expression) throws Exception {
		Session session = (Session) getJpaTemplate().getEntityManagerFactory().createEntityManager().getDelegate();
		List list = null;
		String instanceObject = expression.getObjectName();
		Criteria criteria = session.createCriteria(Class.forName(instanceObject));
		
		Collection filters = expression.getFilters();
		if(filters!=null){
			Iterator it = filters.iterator();
			while(it.hasNext()){
				Filter filter = (Filter)it.next();
				criteria.add(getResrictionFromCriteria(filter)); 
			}
		}
		
		List orders = expression.getOrders();
		if(orders!=null){
			Iterator it = orders.iterator();
			while(it.hasNext()){
				Order order = (Order)it.next();
				if (order.getOperation().equals(OrderOperator.ASC)){
					criteria.addOrder(org.hibernate.criterion.Order.asc(order.getAttribute()));
				} else {
					criteria.addOrder(org.hibernate.criterion.Order.desc(order.getAttribute()));
				}
			}
		}			
		
		Page page = expression.getPage();
		if(page!=null){
			if(page.getFirstItem()>0)
				criteria.setFirstResult(page.getFirstItem().intValue());
			if(page.getMaxItens()>0)
				criteria.setMaxResults(page.getMaxItens().intValue());
		}			
		list = criteria.list();
		session = null;
		return list;
	}
  
	/**
	 * Get component Criteria of Hibernate
	 * @param filter
	 * @return
	 */
	private Criterion getResrictionFromCriteria(Filter filter) {
		Criterion simpleExpression = null;
	  	Object value = filter.getValue();
	  	String attribute = filter.getAttribute();
	  	FilterOperator operator = filter.getOperation();	
	  	if(operator!=null){
	    	if(operator.equals(FilterOperator.CONTAINS))
	    		simpleExpression = Restrictions.ilike(attribute,"%" + value + "%");
	    	else if(operator.equals(FilterOperator.END_WITH))
	    		simpleExpression = Restrictions.ilike(attribute,"%" + value );
	    	else if(operator.equals(FilterOperator.START_WITH))
	    		simpleExpression = Restrictions.ilike(attribute,value + "%" );
	    	else if(operator.equals(FilterOperator.EQUAL))
	        	simpleExpression = Restrictions.eq(attribute,value);
	    	else if(operator.equals(FilterOperator.GREATER_THAN))
	        	simpleExpression = Restrictions.gt(attribute,value);
	    	else if(operator.equals(FilterOperator.GREATER_THAN_OR_EQUAL))
	        	simpleExpression = Restrictions.ge(attribute,value);    	
	    	else if(operator.equals(FilterOperator.LESS_THAN))
	        	simpleExpression = Restrictions.lt(attribute,value);
	    	else if(operator.equals(FilterOperator.LESS_THAN_OR_EQUAL))
	        	simpleExpression = Restrictions.le(attribute,value);
	    	else if(operator.equals(FilterOperator.NOT_EQUAL))
	        	simpleExpression = Restrictions.ne(attribute,value);
	    	else if(operator.equals(FilterOperator.IS_NULL))
	    		simpleExpression = Restrictions.isNull(attribute);
	  	}	
	  	return simpleExpression;
	}

  /**
   * Get a object with some aggregate objects. The id of  object need be id
   * @param objectName Name of class to be selected  
   * @param id ID of the object
   * @param associations Name of attributes of aggregate objects to be return together with the main object
   * @param transaction actual transaction
   */
	@SuppressWarnings("unchecked")
	public Bean getByIdWithRelations(String objectName, Serializable id, String[] associations) throws Exception {
		Session session = (Session) getJpaTemplate().getEntityManagerFactory().createEntityManager().getDelegate();
		Object object = null;
		Criteria filter = session.createCriteria(Class.forName(objectName));
		
		if(id instanceof String)
			filter.add(Restrictions.eq("id",(String)id));
		else
			filter.add(Restrictions.eq("id",(Long)id));
		
		if(associations!=null && associations.length>0){
			switch (associations.length){
				case 1: 
					filter.setFetchMode(associations[0],FetchMode.JOIN);
					break;
				case 2:	
					filter.setFetchMode(associations[0],FetchMode.JOIN).setFetchMode(associations[1],FetchMode.JOIN);
					break;
				case 3:	
					filter.setFetchMode(associations[0],FetchMode.JOIN).setFetchMode(associations[1],FetchMode.JOIN).setFetchMode(associations[2],FetchMode.JOIN);
					break;
				case 4:	
					filter.setFetchMode(associations[0],FetchMode.JOIN).setFetchMode(associations[1],FetchMode.JOIN).setFetchMode(associations[2],FetchMode.JOIN).setFetchMode(associations[3],FetchMode.JOIN);
					break;
				case 5:	
					filter.setFetchMode(associations[0],FetchMode.JOIN).setFetchMode(associations[1],FetchMode.JOIN).setFetchMode(associations[2],FetchMode.JOIN).setFetchMode(associations[3],FetchMode.JOIN).setFetchMode(associations[4],FetchMode.JOIN);
					break;						
			}
		}
		List objects = filter.list();
		if(objects!=null && objects.size()>0){
			object = objects.get(0);
		}
		session = null;
		return (Bean) object;
	}
  
  /**
   * Get a list of object with some agregate objects. The id of  object need be id
   * @param objectName Name of class to be selected  
   * @param associations Name of attributes of agregate objects to be return together with the main object
   * @param transaction actual transaction
   * @throws Exception
   */
	@SuppressWarnings("unchecked")
	public List<Bean> getWithRelations(String objectName, String[] associations) throws Exception {
		Session session = (Session) getJpaTemplate().getEntityManagerFactory().createEntityManager().getDelegate();
		List<Bean> objects = null;
		Criteria filter = session.createCriteria(Class.forName(objectName));
		
		if(associations!=null && associations.length>0){
			if(associations.length == 1){
				filter.setFetchMode(associations[0], FetchMode.SELECT);
			} else {
				for (int i = 0; i < associations.length; i++) {
					filter.setFetchMode(associations[i], FetchMode.JOIN);
				}
			}
			/*switch (associations.length){
				case 1: 
					filter.setFetchMode(associations[0], FetchMode.SELECT);
					break;
				case 2:	
					filter.setFetchMode(associations[0], FetchMode.JOIN).setFetchMode(associations[1],FetchMode.JOIN);
					break;
				case 3:	
					filter.setFetchMode(associations[0], FetchMode.JOIN).setFetchMode(associations[1],FetchMode.JOIN).setFetchMode(associations[2],FetchMode.JOIN);
					break;
				case 4:	
					filter.setFetchMode(associations[0], FetchMode.JOIN).setFetchMode(associations[1],FetchMode.JOIN).setFetchMode(associations[2],FetchMode.JOIN).setFetchMode(associations[3],FetchMode.JOIN);
					break;
				case 5:	
					filter.setFetchMode(associations[0], FetchMode.JOIN).setFetchMode(associations[1],FetchMode.JOIN).setFetchMode(associations[2],FetchMode.JOIN).setFetchMode(associations[3],FetchMode.JOIN).setFetchMode(associations[4],FetchMode.JOIN);
					break;						
			}*/
		}
		objects = filter.list();
		session = null;
		return objects;
	}
  
  /**
   * Get a list of object from a HSQL criteria
   */
	@SuppressWarnings("unchecked")
	public List<Bean> getFromHql(String hsql) throws Exception {
		Session session = (Session) getJpaTemplate().getEntityManagerFactory().createEntityManager().getDelegate();
		List<Bean> result = session.createQuery(hsql).list();
		session = null;
		return result;
	}

  /**
   * Get a list of object from a SQL criteria
   */
	@SuppressWarnings("unchecked")
	public List<Bean> getFromSql(String sql) throws Exception {
		Session session = (Session) getJpaTemplate().getEntityManagerFactory().createEntityManager().getDelegate();
		List<Bean> result = null;
		result = session.createSQLQuery(sql).list();
		session = null;
		return result;
	}

	/**
	 * Return number of criteria
	 */
	public Long getCount(Class<? extends Bean> clazz) throws Exception {
		CriteriaExpression expression = new CriteriaExpression(clazz);
		return getCount(expression);
	}
	
	/**
	 * Return number of criteria.
	 */
	public Long getCount(Bean bean) throws Exception {
		CriteriaExpression expression = new CriteriaExpression(bean.getClass());
		expression.addFilters(UtilSS.getMapAttribute(bean), true);
		return getCount(expression);
	}
	  
	/**
	 * Return number of criteria.
	 */
	public Long getCountWithEquals(Bean bean) throws Exception {
		CriteriaExpression expression = new CriteriaExpression(bean.getClass());
		expression.addFilters(UtilSS.getMapAttribute(bean), false);
		return getCount(expression);
	}

	public Session getSession() {
		return (Session) getJpaTemplate().getEntityManagerFactory().createEntityManager().getDelegate();
	}
}