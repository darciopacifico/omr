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
public abstract class AbstractHibernateDAO<B extends IBaseVO<? extends Serializable>> extends HibernateDaoSupport implements IDAO<B> {
	
	private static final long serialVersionUID = 5730970152568135815L;
	
	public AbstractHibernateDAO() {
	}
	
	/**
	 * Cria um detachedCriteria a partir do primeiro parameter type do DAO concreto especializado. Ex: Em
	 * TipoRequisitoHibernateDAOImpl<TipoRequisitoVO> cria um DetatchedCriteria para TipoRequisitoVO
	 * 
	 * 
	 * 
	 * @return
	 */
	protected DetachedCriteria createDetachedCriteria() { // NOSONAR
		return DetachedCriteria.forClass(this.getVoClass());
	}
	
	@Override
	public void delete(final B baseVO) throws BaseBusinessException {
		getHibernateTemplate().delete(baseVO);
	}
	
	/**
	 * Adiciona uma expressao equals, caso o valor informado não seja null
	 * 
	 * @param value
	 * @param criteria
	 * @param propertyName
	 */
	protected void eqRestriction(final Object value, final DetachedCriteria criteria, final String propertyName) {
		
		if (this.isAddEquals(value)) {
			criteria.add(Restrictions.eq(propertyName, value));
		}
		
	}
	
	@Override
	public boolean exists(final B baseVO) {
		final Object entity = getHibernateTemplate().get(this.getVoClass(), baseVO.getPK());
		
		return entity != null;
	}
	
	@Override
	public boolean exists(final Serializable chave) {
		final Object entity = getHibernateTemplate().get(this.getVoClass(), chave);
		
		return entity != null;
	}
	
	@Override
	public List<B> findAll() {
		final DetachedCriteria criteria = DetachedCriteria.forClass(this.getVoClass());
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	@Override
	public List<B> findAll(final Integer firstResult, final Integer maxResult) {
		final DetachedCriteria criteria = DetachedCriteria.forClass(this.getVoClass());
		return getHibernateTemplate().findByCriteria(criteria, firstResult, maxResult);
	}
	
	@Override
	public B findByPK(final B bean) {
		return (B) getHibernateTemplate().get(this.getVoClass(), bean.getPK());
	}
	
	@Override
	public B findByPK(final Serializable chave) {
		return (B) getHibernateTemplate().get(this.getVoClass(), chave);
	}
	
	/**
	 * Retorna a classe referente ao primeiro parameter type definido na implementação de AbstractHibernateDAO
	 * 
	 * @return
	 */
	protected Class getVoClass() {
		final Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		
		return (Class) types[0];
	}
	
	/**
	 * Adiciona uma expressao ilike, caso o valor informado não seja null
	 * 
	 * @param value
	 * @param criteria
	 * @param propertyName
	 */
	protected void ilikeRestriction(final String value, final DetachedCriteria criteria, final String propertyName) {
		if (this.isAddEquals(value)) {
			criteria.add(Restrictions.ilike(propertyName, value));
		}
	}
	
	/**
	 * Testa se o valor deve ser adicionado como critério de pesquisa
	 * 
	 * @param value
	 * @return
	 */
	protected boolean isAddEquals(final Object value) {
		if (value instanceof String) {
			return !StringUtils.isBlank((String) value);
		} else {
			return value != null;
		}
	}
	
	/**
	 * Apply order in hibenate Criteria
	 * 
	 * @param criteria
	 * @param queryOrders
	 */
	protected void order(final DetachedCriteria criteria, final QueryOrder... queryOrders) {
		for (final QueryOrder queryOrder : queryOrders) {
			if (queryOrder.isAsc()) {
				criteria.addOrder(Order.asc(queryOrder.getCampo()));
			} else {
				criteria.addOrder(Order.desc(queryOrder.getCampo()));
			}
		}
	}
	
	/**
	 * Define uma restrição de faixa de valores num criteria. Se valorA e valorB forem null, nenhuma restrição será adicionada
	 * 
	 * @param value
	 * @param criteria
	 * @param propertyName
	 */
	protected void rangeRestriction(final Object valueA, final Object valueB, final DetachedCriteria criteria, final String propertyName) {
		if (valueA != null && valueB != null) {
			criteria.add(Restrictions.between(propertyName, valueA, valueB));
		} else if (valueA != null) {
			criteria.add(Restrictions.ge(propertyName, valueA));
		} else if (valueB != null) {
			criteria.add(Restrictions.le(propertyName, valueB));
		}
	}
	
	@Override
	public B saveOrUpdate(final B baseVO) throws BaseBusinessException {
		getHibernateTemplate().saveOrUpdate(baseVO);
		return baseVO;
	}
	
	/**
	 * Apenas para garantir o autowiring e nao ter que declaras todos os daos no applicationContext.xml
	 * 
	 * @param hibernateTemplate2
	 */
	@Autowired
	public void setHibernateTemplateDlp(final HibernateTemplate hibernateTemplate2) {
		super.setHibernateTemplate(hibernateTemplate2);
	}
	
	/**
	 * Apenas para garantir o autowiring e nao ter que declaras todos os daos no applicationContext.xml
	 * 
	 * @param sessionFactory2
	 */
	@Autowired
	public void setSessionFactoryDlp(final SessionFactory sessionFactory2) {
		super.setSessionFactory(sessionFactory2);
	}
	
	@Override
	public B update(final B baseVO) throws BaseBusinessException {
		getHibernateTemplate().update(baseVO);
		return baseVO;
	}
	
}
