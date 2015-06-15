package br.com.dlp.framework.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.servicelocator.IServiceLocator;
import br.com.dlp.framework.servicelocator.ServiceLocatorException;
import br.com.dlp.framework.vo.ICadastroBaseVO;

public abstract class AbstractHibernateDAO extends AbstractDAO {

	protected Log logger = LogFactory.getLog(AbstractHibernateDAO.class);

	public ICadastroBaseVO inserir(ICadastroBaseVO baseVO)
			throws BaseTechnicalError {

		Session session = getHibernateSession();
		// Transaction transaction = session.beginTransaction();
		try {

			session.save(baseVO);

		} catch (HibernateException e) {

			throw new BaseTechnicalError("Erro ao tentar incluir registro!", e);

		} finally {

			closeSession(session);
			// transaction.commit();
		}

		return baseVO;
	}

	public ICadastroBaseVO atualizar(ICadastroBaseVO baseVO)
			throws DAOException, BaseTechnicalError {
		Session session = getHibernateSession();
		try {

			session.update(baseVO);

		} catch (HibernateException e) {
			throw new BaseTechnicalError("Erro ao tentar alterar registro!", e);

		} finally {
			closeSession(session);

		}

		return baseVO;
	}

	public void excluir(ICadastroBaseVO baseVO) throws DAOException,
			BaseTechnicalError {
		Session session = getHibernateSession();
		try {

			session.delete(baseVO);

		} catch (HibernateException e) {
			throw new BaseTechnicalError("Erro ao tentar excluir registro!", e);

		} finally {
			closeSession(session);

		}
	}

	/**
	 * 
	 */
	public List findAll() throws DAOException, BaseTechnicalError {
		return findAll(new QueryOrder[] {});
	}

	public List findAll(QueryOrder[] queryOrders) throws DAOException,
			BaseTechnicalError {
		Class voClass = getMappedClass();

		Session session = null;
		List list = new ArrayList();
		try {
			session = getHibernateSession();

			Criteria criteria = session.createCriteria(voClass);

			configureOrderCriteria(criteria, queryOrders);

			list = criteria.list();

		} catch (HibernateException e) {
			throw new DAOException(
					"Não foi possível executar o findAll. Classe " + voClass, e);
		} catch (ServiceLocatorException e) {
			throw new DAOException(e);
		} finally {
			closeSession(session);
		}
		return list;
	}

	public abstract Class getMappedClass();

	public Session getHibernateSession() throws BaseTechnicalError {
		IServiceLocator serviceLocator = getServiceLocator();
		Session session = null;
		try {
			session = serviceLocator.getHibernateSession();
		} catch (ServiceLocatorException e) {
			throw new BaseTechnicalError(
					"Não foi possível recuperar uma sessão Hibernate", e);
		}
		return session;
	}

	public void closeSession(Session session) throws BaseTechnicalError {
		IServiceLocator serviceLocator = getServiceLocator();
		serviceLocator.closeHibernateSession(session);
	}

	/**
	 * Método específico da implementação para Hibernate de IDAO para ordenação
	 */
	protected void configureOrderCriteria(Criteria criteria,
			QueryOrder queryOrder) {
		if (queryOrder == null) {
			return;
		}

		if (queryOrder.isAsc()) {
			criteria.addOrder(Order.asc(queryOrder.getCampo()));
		} else {
			criteria.addOrder(Order.desc(queryOrder.getCampo()));
		}
	}

	/**
	 * Método específico da implementação para Hibernate de IDAO para ordenação
	 */
	protected void configureOrderCriteria(Criteria criteria,
			QueryOrder[] queryOrders) {
		for (int i = 0; i < queryOrders.length; i++) {
			configureOrderCriteria(criteria, queryOrders[i]);
		}
	}

}