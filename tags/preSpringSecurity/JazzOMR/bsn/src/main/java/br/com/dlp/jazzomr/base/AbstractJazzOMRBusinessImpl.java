package br.com.dlp.jazzomr.base;

import java.io.Serializable;

import br.com.dlp.framework.business.AbstractBusinessImpl;
import br.com.dlp.framework.vo.IBaseVO;
import br.com.dlp.jazzomr.exam.IJazzOMRBusiness;
import br.com.dlp.jazzomr.exam.IJazzOMRDAO;

/**
 * Classe especializacao de abstractBusinessImpl. Implementa findByPK com fetchProfile
 * @author darcio
 *
 * @param <B>
 */
public abstract class AbstractJazzOMRBusinessImpl<B extends IBaseVO<? extends Serializable>> extends AbstractBusinessImpl<B> implements IJazzOMRBusiness<B> {
	
	private static final long serialVersionUID = -8514100142210577366L;
	
	/**
	 * Implementacao de findByPK com fetch profile
	 */
	@Override
	public B findByPK(B bean, String... fetchProfileName) {
		IJazzOMRDAO<B> dao = (IJazzOMRDAO<B>) this.getDao();
		return dao.findByPK(bean, fetchProfileName);
	}
	
	
	
}
