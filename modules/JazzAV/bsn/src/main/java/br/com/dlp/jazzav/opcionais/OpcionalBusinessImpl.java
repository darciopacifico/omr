package br.com.dlp.jazzav.opcionais;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.jazzav.StatusEnum;
import br.com.dlp.jazzav.base.AbstractJazzOMRBusinessImpl;
import br.com.dlp.jazzav.produto.OpcionalVO;

/**
 * 
 * @author darcio
 *
 */
@Component
public class OpcionalBusinessImpl extends AbstractJazzOMRBusinessImpl<OpcionalVO> implements OpcionalBusiness{
	
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	private OpcionalDAO opcionalDAO;

	@Autowired
	private HibernateTemplate hibernateTemplate;
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<OpcionalVO> getDao() {
		return opcionalDAO;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public OpcionalVO newVO() {
		return new OpcionalVO();
	}
	
	
	@Override
	public List<OpcionalVO> findAll() {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OpcionalVO.class);
		
		criteria.add(Restrictions.not(Restrictions.eq("status", StatusEnum.INACTIVE)));
		
		List<OpcionalVO> opcionais = hibernateTemplate.findByCriteria(criteria);
		
		return opcionais;
	}
	


}
