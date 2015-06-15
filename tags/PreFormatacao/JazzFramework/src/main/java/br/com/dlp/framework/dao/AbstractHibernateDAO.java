/**
 * 
 */
package br.com.dlp.framework.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * Implementação abstrata de DAOs para JPA
 * 
 * @author dpacifico
 * @param <P>
 * @param <P>
 * @param <B>
 * 
 */
public abstract class AbstractHibernateDAO<B extends IBaseVO<? extends Serializable>>
		extends HibernateDaoSupport implements IDAO<B> {

	private static final long serialVersionUID = 5730970152568135815L;

	public AbstractHibernateDAO() {
	}

	@Override
	public void delete(B baseVO) throws BaseBusinessException {
		getHibernateTemplate().delete(baseVO);
	}

	@Override
	public boolean exists(B baseVO) {
		Object entity = getHibernateTemplate()
				.get(getVoClass(), baseVO.getPK());

		return entity != null;
	}

	@Override
	public boolean exists(Serializable chave) {
		Object entity = getHibernateTemplate().get(getVoClass(), chave);

		return entity != null;
	}

	@Override
	public List<B> findAll() {
		DetachedCriteria criteria = DetachedCriteria.forClass(getVoClass());
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * Retorna a classe referente ao primeiro parameter type definido na
	 * implementação de AbstractHibernateDAO
	 * 
	 * @return
	 */
	protected Class getVoClass() {
		Type[] types = ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments();

		Class voClazz = (Class) types[0];
		return voClazz;
	}

	@Override
	public List<B> findAll(Integer firstResult, Integer maxResult) {
		DetachedCriteria criteria = DetachedCriteria.forClass(getVoClass());
		return getHibernateTemplate().findByCriteria(criteria, firstResult,
				maxResult);
	}

	@Override
	public B findByPK(B bean) {
		B entity = (B) getHibernateTemplate().get(getVoClass(), bean.getPK());

		return entity;
	}

	@Override
	public B findByPK(Serializable chave) {
		B entity = (B) getHibernateTemplate().get(getVoClass(), chave);

		return entity;
	}

	@Override
	public B saveOrUpdate(B baseVO) throws BaseBusinessException {
		getHibernateTemplate().saveOrUpdate(baseVO);
		return baseVO;
	}

	@Override
	public B update(B baseVO) throws BaseBusinessException {
		getHibernateTemplate().update(baseVO);
		return baseVO;
	}

	/**
	 * Apenas para garantir o autowiring e nao ter que declaras todos os daos no
	 * applicationContext.xml
	 * 
	 * @param hibernateTemplate2
	 */
	@Autowired
	public void setHibernateTemplateDlp(HibernateTemplate hibernateTemplate2) {
		super.setHibernateTemplate(hibernateTemplate2);
	}

	/**
	 * Apenas para garantir o autowiring e nao ter que declaras todos os daos no
	 * applicationContext.xml
	 * 
	 * @param sessionFactory2
	 */
	@Autowired
	public void setSessionFactoryDlp(SessionFactory sessionFactory2) {
		super.setSessionFactory(sessionFactory2);
	}

	/**
	 * Apply order in hibenate Criteria
	 * 
	 * @param criteria
	 * @param queryOrders
	 */
	protected void order(DetachedCriteria criteria, QueryOrder... queryOrders) {
		for (QueryOrder queryOrder : queryOrders) {
			if (queryOrder.isAsc()) {
				criteria.addOrder(Order.asc(queryOrder.getCampo()));
			} else {
				criteria.addOrder(Order.desc(queryOrder.getCampo()));
			}
		}
	}

	/**
	 * Cria um detachedCriteria a partir do primeiro parameter type do DAO
	 * concreto especializado. Ex: Em
	 * TipoRequisitoHibernateDAOImpl<TipoRequisitoVO> cria um DetatchedCriteria
	 * para TipoRequisitoVO
	 * 
	 * @return
	 */
	protected DetachedCriteria createDetachedCriteria() {
		return DetachedCriteria.forClass(getVoClass());
	}

	/**
	 * Adiciona uma expressao equals, caso o valor informado não seja null
	 * 
	 * @param value
	 * @param criteria
	 * @param propertyName
	 */
	protected void eqRestriction(Object value, DetachedCriteria criteria,
			String propertyName) {

		if (isAddEquals(value)) {
			criteria.add(Restrictions.eq(propertyName, value));
		}

	}

	/**
	 * Testa se o valor deve ser adicionado como critério de pesquisa
	 * 
	 * @param value
	 * @return
	 */
	protected boolean isAddEquals(Object value) {
		if (value instanceof String) {
			return !StringUtils.isBlank((String) value);
		} else {
			return value != null;
		}
	}

	/**
	 * Adiciona uma expressao ilike, caso o valor informado não seja null
	 * 
	 * @param value
	 * @param criteria
	 * @param propertyName
	 */
	protected void ilikeRestriction(String value, DetachedCriteria criteria, String propertyName) {
		if (isAddEquals(value)) {
			criteria.add(Restrictions.ilike(propertyName, value));
		}
	}

	/**
	 * Define uma restrição de faixa de valores num criteria. Se valorA e valorB
	 * forem null, nenhuma restrição será adicionada
	 * 
	 * @param value 
	 * @param criteria
	 * @param propertyName
	 */
	protected void rangeRestriction(Object valueA, Object valueB,
			DetachedCriteria criteria, String propertyName) {
		if (valueA != null && valueB != null) {
			criteria.add(Restrictions.between(propertyName, valueA, valueB));
		} else if (valueA != null) {
			criteria.add(Restrictions.ge(propertyName, valueA));
		} else if (valueB != null) {
			criteria.add(Restrictions.le(propertyName, valueB));
		}
	}

}
