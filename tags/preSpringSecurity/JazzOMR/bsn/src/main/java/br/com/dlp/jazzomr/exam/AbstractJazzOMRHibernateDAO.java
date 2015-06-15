package br.com.dlp.jazzomr.exam;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Restrictions;

import br.com.dlp.framework.dao.AbstractHibernateDAO;
import br.com.dlp.framework.dao.JazzDetachedCriteria;
import br.com.dlp.framework.vo.IBaseVO;

public abstract class AbstractJazzOMRHibernateDAO <B extends IBaseVO<? extends Serializable>> extends AbstractHibernateDAO<B> implements IJazzOMRDAO<B> {

	private static final long serialVersionUID = 8318685984270582118L;

	public AbstractJazzOMRHibernateDAO() {
		super();
	}
	

	/**
	 * Sobrecarga de findByPK com suporte ao uso de fetchProfile
	 * @param bean
	 * @param fetchProfileName
	 * @return
	 */
	@Override
	public B findByPK(B bean, String... fetchProfileNames) {
		
		JazzDetachedCriteria criteria = JazzDetachedCriteria.forClass(getVoClass());
		criteria.add(Restrictions.eq("PK", bean.getPK()));
	
		enableFetchProfiles(criteria, fetchProfileNames);
		
		List<B> beans = getHibernateTemplate().findByCriteria(criteria);
		
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
	protected void enableFetchProfiles(JazzDetachedCriteria criteria, String... fetchProfileNames) {
		if(fetchProfileNames!=null && fetchProfileNames.length>0){
			for (String fetchProfileName : fetchProfileNames) {
				criteria.enableFetchProfile(fetchProfileName);
			}
		}
	}
	
	
}