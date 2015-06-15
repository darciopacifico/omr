package br.com.dlp.jazzomr.exam;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.com.dlp.framework.dao.AbstractHibernateDAO;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.vo.IBaseVO;
import br.com.dlp.jazzomr.base.JazzOMRDetachedCriteria;

public abstract class AbstractJazzOMRHibernateDAO <B extends IBaseVO<? extends Serializable>> extends AbstractHibernateDAO<B> implements IJazzOMRDAO<B> {

	private static final long serialVersionUID = 8318685984270582118L;

	public AbstractJazzOMRHibernateDAO() {
		super();
	}
	
	
	/**
	 * @param voClass
	 * @return
	 */
	protected JazzOMRDetachedCriteria createDetachedCriteria(Class voClass) {
		JazzOMRDetachedCriteria forClass = JazzOMRDetachedCriteria.forClass(voClass);
		
		return forClass;
	}
	
	
	/**
	 * Cria um detachedCriteria a partir do primeiro parameter type do DAO concreto especializado. Ex: Em
	 * TipoRequisitoHibernateDAOImpl<TipoRequisitoVO> cria um DetatchedCriteria para TipoRequisitoVO
	 * 
	 * @return
	 */
	protected JazzOMRDetachedCriteria createDetachedCriteria() { // NOSONAR
		Class voClass = this.getVoClass();
		return createDetachedCriteria(voClass);
	}
	
	
	@Override
	public List<B> findAllNotIn(List<B> notIn, String... fetchProfiles) {
		// TODO Auto-generated method stub
		return super.findAllNotIn(notIn, fetchProfiles);
	}
	
	
	@Override
	public List<B> findAll(ExtraArgumentsDTO extraArgumentsDTO) {
		// TODO Auto-generated method stub
		final DetachedCriteria criteria = createDetachedCriteria();
		
		return findByCriteria(criteria, extraArgumentsDTO);
	}	

	/**
	 * Sobrecarga de findByPK com suporte ao uso de fetchProfile
	 * @param bean
	 * @param fetchProfileName
	 * @return
	 */
	@Override
	public B findByPK(B bean, String... fetchProfileNames) {
		
		JazzOMRDetachedCriteria criteria =  createDetachedCriteria();
		criteria.add(Restrictions.eq("PK", bean.getPK()));
	
		enableFetchProfiles(criteria, fetchProfileNames);
		
		List<B> beans = findByCriteria(criteria);
		
		B beanReturn = null;
		if(beans.size()>0){
			beanReturn = beans.get(0);
		}
		
		return beanReturn;
		
	}

	/**
	 * Caso tenha sido informado um array de fetchProfiles...
	 * @param criteria
	 * @param fetchProfileNames
	 */
	protected void enableFetchProfiles(JazzOMRDetachedCriteria criteria, String... fetchProfileNames) {
		if(fetchProfileNames!=null && fetchProfileNames.length>0){
			for (String fetchProfileName : fetchProfileNames) {
				criteria.enableFetchProfile(fetchProfileName);
			}
		}
	}

	

}