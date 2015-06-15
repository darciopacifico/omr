/**********************************************************************
 * Copyright (C) - 2009 MasterSaf
 * 
 * Projeto: Validador
 * Arquivo: DAOGenericoHibernate.java
 * @date 11/02/2009
 * @author Everton Amaral - SBR
 *  
 ***********************************************************************/
package com.msaf.validador.integration.hibernate.base;


import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.msaf.validador.integration.util.Paginacao;

/**
 * Class DAOGenericoHibernate.
 */
public abstract class DAOGenericoHibernate<T, ID extends Serializable> implements IDAOGenerico<T, ID> {

	/** persistent class. */
	private Class<T> persistentClass;

	@PersistenceContext
	private EntityManager entityManager;

	private JpaTemplate jpaTemplate; 

	
	private String persistenceUnitName = "persistenciaProject";
	
	
	
	public T findById(ID id) {
		return findById(id, false);
	}

	/**
	 * Instancia um(a) novo(a) dAO generico hibernate.
	 */
	@SuppressWarnings("unchecked")
	public DAOGenericoHibernate() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	protected void addPaginacaoRestriction(Query query, Paginacao paginacao) {
		if(paginacao == null) return;
		query.setFirstResult(paginacao.getInicio());
		query.setMaxResults(paginacao.getLimite());
	}

	/**
	 * Configura session factory.
	 * 
	 * @param sessionFactory
	 *            o novo(a) session factory
	 */
	public void setSessionFactory(EntityManager entityManager) {
		this.setEntityManager(entityManager);
		this.setJpaTemplate(new JpaTemplate(entityManager));		
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<T> findAll() {
		
	     return (List<T>) getJpaTemplate().execute(new JpaCallback() {

	    	    public Object doInJpa(EntityManager entityManager) {

	    	        Query query = entityManager.createQuery("Select e from "

	    	        + getPersistentClass().getSimpleName() + " e ");

	    	        return query.getResultList();
	    	    }

	    	    }); 
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public T findById(ID id, boolean lock) {
		
		T entity = (T) this.getJpaTemplate().find(getPersistentClass(), id);
		return entity;
	}

	/**
	 * Retorna persistent class.
	 * 
	 * @return persistent class
	 */
	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	@Override
	public void delete(T entity) {
		getJpaTemplate().remove(entity);
		
	}

	@Override
	public void merge(T entity) {
		getJpaTemplate().merge(entity);
		
	}

	@Override
	public void update(T entity) {
		getJpaTemplate().merge(entity);
	}
	

	@Transactional(readOnly = false)
	public T makePersistent(T entity) {
		getJpaTemplate().persist(entity); 
		return entity;
	}
	
	
	public EntityManager getEntityManager() {
		/*
		if(entityManager==null){
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(getPersistenceUnitName());
			entityManager = entityManagerFactory.createEntityManager();
			
		}*/
		
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
		
	}

	public JpaTemplate getJpaTemplate() {
		if(jpaTemplate==null){
			EntityManager entityManager = getEntityManager();
			jpaTemplate = new JpaTemplate(entityManager);
		}
		return jpaTemplate; 
	}

	public void setJpaTemplate(JpaTemplate jpaTemplate) {
		this.jpaTemplate = jpaTemplate;
	}

	public void setPersistenceUnitName(String persistenceUnitName) {
		this.persistenceUnitName = persistenceUnitName;
	}

	public String getPersistenceUnitName() {
		return persistenceUnitName;
	}
}
