/**
 * 
 */
package br.com.dlp.framework.dao;


import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.exception.JazzRuntimeException;
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
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final long serialVersionUID = 5730970152568135815L;
	
	public AbstractHibernateDAO() {
		// intencionalmente em branco
	}
	
	/**
	 * Cria um detachedCriteria a partir do primeiro parameter type do DAO concreto especializado. Ex: Em
	 * TipoRequisitoHibernateDAOImpl<TipoRequisitoVO> cria um DetatchedCriteria para TipoRequisitoVO
	 * 
	 * @return
	 */
	protected JazzDetachedCriteria createDetachedCriteria() { // NOSONAR
		return JazzDetachedCriteria.forClass(this.getVoClass());
	}
	
	@Override
	public void delete(final B baseVO) throws JazzBusinessException {
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
	public List<B> findByTextSearch(String textSearch){
		return null;
	}
	
	@Override
	public List<B> findAll() {
		return findAll(null);
	}
	
	@Override
	public List<B> findAll(ExtraArgumentsDTO extraArgumentsDTO) {
		
		final DetachedCriteria criteria = DetachedCriteria.forClass(this.getVoClass());
		
		return findByCriteria(criteria, extraArgumentsDTO);
		
	}
	
	
	@Override
	public List<B> findAllNotIn(List<B> notIn, String... fetchProfiles) {
		
		JazzDetachedCriteria criteria = createDetachedCriteria();


		if(notIn!=null && notIn.size()>0){
			List<Serializable> pks = new ArrayList<Serializable>(notIn.size());
			
			for (B b : notIn) {
				pks.add(b.getPK());			
			}
			
			criteria.add(Restrictions.not(Restrictions.in("PK", pks)));
		}
		
		List<B> list = findByCriteria(criteria);
		
		return list;
	}	
	
	
	/**
	 * Executa consulta do criteria informado mais os metaArgumentos de ordenação e paginação informados
	 * @param criteria
	 * @param extraArgumentsDTO
	 * @return
	 * @throws DataAccessException
	 */
	protected List<B> findByCriteria(final DetachedCriteria criteria, ExtraArgumentsDTO extraArgumentsDTO) {
		order(criteria, extraArgumentsDTO);
		
		if(extraArgumentsDTO!=null && extraArgumentsDTO.hasPageArguments()){
			return getHibernateTemplate().findByCriteria(criteria, extraArgumentsDTO.getFirstResult(), extraArgumentsDTO.getMaxResults());
			
		}else{
			return getHibernateTemplate().findByCriteria(criteria);
			
		}
	}
	
	
	/**
	 * Executa consult apenas por criteria
	 * @param criteria
	 * @return
	 * @throws DataAccessException
	 */
	protected List<B> findByCriteria(final DetachedCriteria criteria) throws DataAccessException {
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	@Override
	public B findByPK(final B bean) {
		return (B) getHibernateTemplate().get(this.getVoClass(), bean.getPK());
	}


	/* (non-Javadoc)
	 * @see br.com.dlp.framework.dao.IDAO#findBeansByBeans(java.util.Collection, java.lang.String[])
	 */
	@Override
	public List<B> findBeansByBeans(Collection<B> iBaseVOs, String... fetchProfiles) {

		if(CollectionUtils.isEmpty(iBaseVOs)){
			//mera protecao do fluxo do codigo
			return new ArrayList<B>(0);
		}

		
		JazzDetachedCriteria criteria = createDetachedCriteria();
		
		//copia array de pks
		List<Serializable> pks = new ArrayList<Serializable>(iBaseVOs.size());
		for (B iBaseVO : iBaseVOs) {
			pks.add(iBaseVO.getPK());
		}
		
		//prepara fetch profiles
		criteria.enableFetchProfile(fetchProfiles);
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		criteria.add(Restrictions.in("PK", pks));
		
		//fire
		List<B> results = getHibernateTemplate().findByCriteria(criteria);
		
		return results;
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
			//adiciona condicao se o value for informado
			return value != null;
		}
	}
	
	/**
	 * Apply order in hibenate Criteria
	 * 
	 * @param criteria
	 * @param metaArgument
	 */
	protected void order(final DetachedCriteria criteria, final ExtraArgumentsDTO metaArgument) {
		if(metaArgument!=null && metaArgument.hasOrderArguments()) {
			
			Map<String, Boolean> orderMap = metaArgument.getOrderMap();
			
			Set<String> fields = orderMap.keySet();
			for (String field : fields) {
				
				if (orderMap.get(field)) {
					criteria.addOrder(Order.asc(field));
				} else {
					criteria.addOrder(Order.desc(field));
				}
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
	public B saveOrUpdate(final B baseVO) throws JazzBusinessException {
		
		getHibernateTemplate().contains(baseVO);
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
	public B update(final B baseVO) throws JazzBusinessException {
		getHibernateTemplate().update(baseVO);
		return baseVO;
	}

	/**
	 * 
	 * @param someEntity
	 * @param string
	 */
	public static void distinctResults(Object someEntity, String collProperty) {
		
		if(someEntity==null || collProperty==null){
			return;
		}
		
		Object obj;
		try {
			obj = PropertyUtils.getProperty(someEntity, collProperty);
		} catch (IllegalAccessException e) {
			throw new JazzRuntimeException("Erro ao tentar acessar a propriedade "+collProperty+ " do objeto "+someEntity,e);
		} catch (InvocationTargetException e) {
			throw new JazzRuntimeException("Erro ao tentar acessar a propriedade "+collProperty+ " do objeto "+someEntity,e);
		} catch (NoSuchMethodException e) {
			throw new JazzRuntimeException("Erro ao tentar acessar a propriedade "+collProperty+ " do objeto "+someEntity,e);
		}
		
		if(obj!=null && obj instanceof Collection){
			
			Collection coll = (Collection) obj;
			
			Set distinctRers = new HashSet(coll);
			
			distinctRers.remove(null);
			
			
			Collection newCollection; 
			if(coll instanceof Set){
				newCollection = new HashSet(distinctRers);
			}else{
				newCollection = new ArrayList(distinctRers);
			}
			
			
			try {
				PropertyUtils.setProperty(someEntity,collProperty,newCollection);
			} catch (IllegalAccessException e) {
				throw new JazzRuntimeException("Erro ao tentar acessar a propriedade "+collProperty+ " do objeto "+someEntity,e);
			} catch (InvocationTargetException e) {
				throw new JazzRuntimeException("Erro ao tentar acessar a propriedade "+collProperty+ " do objeto "+someEntity,e);
			} catch (NoSuchMethodException e) {
				throw new JazzRuntimeException("Erro ao tentar acessar a propriedade "+collProperty+ " do objeto "+someEntity,e);
			}
			
			
			
		}
		
		
		
	}
	
}
