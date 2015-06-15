package br.com.dlp.framework.business;


import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.JazzDetachedCriteria;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.security.SecurityHelper;
import br.com.dlp.framework.vo.IBaseVO;
import br.com.dlp.framework.vo.ILoggableVO;

/**
 * Generic abstract business object implementation.
 * 
 * @author dpacifico
 * @version 1.0
 * @updated 19-mai-2010 16:25:32
 */
@Transactional
public abstract class AbstractBusinessImpl<B extends IBaseVO<? extends Serializable>> implements IBusiness<B> {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final long serialVersionUID = -5087585943211384211L;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	/**
	 * Método helper genérico para complementar atributos de entidades, conforme fetchProfiles informados.
	 */
	@Override
	public <V extends br.com.dlp.framework.vo.IBaseVO<? extends Serializable>> V complementBean(V valueObject, String... fetchProfile) {
		
		JazzDetachedCriteria criteria = JazzDetachedCriteria.forClass(valueObject.getClass());
		
		criteria.add(Restrictions.eq("PK", valueObject.getPK()));
		
		criteria.enableFetchProfile(fetchProfile);
		
	  List<V> vos = hibernateTemplate.findByCriteria(criteria);
	  
	  
	  
	  
	  if(CollectionUtils.isNotEmpty(vos)){
	  	V vo = vos.get(0);
	  	return vo;
	  }
		
		return null;
	}
	
	
	/**
	 * Excluir generico.
	 */
	public void delete(final B iBaseVO) throws JazzBusinessException {
		
		this.deleteValidations(iBaseVO);
		
		this.getDao().delete(iBaseVO);
		
	}

	
	public List<B> findAllNotIn(List<B> notIn, String... fetchProfiles) {
		return getDao().findAllNotIn(notIn, fetchProfiles);
	}
	
	/**
	 * Implemente aqui as validaAAes de negAcio do seu modulo. As exceptions de validacao de negAcio devem ser acumuladas num
	 * JazzBusinessException e depois este deve ser lancado.
	 */
	public void deleteValidations(final B baseVO) throws JazzBusinessException {
		if(log.isDebugEnabled())
		log.debug("Nao ha validacao para exclusao de registro nesta classe");
	}
	
	/**
	 * Generic exists using bean.
	 */
	public boolean existsByBean(final B bean) {
		return this.getDao().exists(bean);
	}
	
	/**
	 * Generic exists.
	 */
	public boolean exists(final Serializable pk) {
		return this.getDao().exists(pk);
	}
	
	/**
	 * Find all generic.
	 */
	public List<B> findAll() {
		return this.getDao().findAll();
	}
	
	
	/**
	 * Procura por texto livre
	 * @see br.com.dlp.framework.business.IBusiness#findByTextSearch(java.lang.String)
	 */
	@Override
	public List<B> findByTextSearch(String textSearch) {
		// TODO Auto-generated method stub
		return this.getDao().findByTextSearch(textSearch);
	}
	
	/**
	 * Find all generic.
	 */
	public List<B> findAll(ExtraArgumentsDTO extraArgumentsDTO) {
		return this.getDao().findAll(extraArgumentsDTO);
	}
	
	/**
	 * Generic findByPK using Bean.
	 */
	public B findByBeanPK(final B bean) {
		return this.getDao().findByPK(bean);
		
	}
	
	/**
	 * Generic findByPK using Bean.
	 */
	public List<B> findBeansByBeans(Collection<B> beans, String... fetchProfiles) {
		return this.getDao().findBeansByBeans(beans, fetchProfiles);
	}
	
	/**
	 * Generic findByPK.
	 */
	public B findByPK(final Serializable chave) {
		
		return this.getDao().findByPK(chave);
		
	}
	
	/**
	 * Retorna o DAO da implementação concreta do módulo.
	 * 
	 * @return the dao
	 */
	protected abstract IDAO<B> getDao();
	
	/**
	 * Implemente aqui as validaAAes de negAcio referentes aos metodos de inclusao e alteracao do seu modulo. As exceptions de validacao de
	 * negAcio devem ser acumuladas num JazzBusinessException e depois este deve ser lancado.
	 */
	@Override
	public void includeUpdateValidations(final B baseVO) throws JazzBusinessException {
		if(log.isDebugEnabled())
		log.debug("Nao ha validacao para inclusao/atualizacao de registro nesta classe");
	}
	
	/**
	 * Implemente aqui as validaAAes de negAcio do seu modulo. As exceptions de validacao de negAcio devem ser acumuladas num
	 * JazzBusinessException e depois este deve ser lancado.
	 */
	@Override
	public void includeValidations(final B baseVO) throws JazzBusinessException {
		if(log.isDebugEnabled())
		log.debug("Nao ha validacao para inclusao de registro nesta classe");
	}
	
	/**
	 * Salva, se ainda não existe, ou altera um registro.
	 * 
	 * @param baseVO
	 * @return
	 * @throws JazzBusinessException
	 */
	public B saveOrUpdate(B baseVO) throws JazzBusinessException {
		
		logInsertUpdate(baseVO);

		this.includeUpdateValidations(baseVO);
		this.includeValidations(baseVO);
		
		IDAO<B> dao = this.getDao();
		
		B savedBaseVO = dao.saveOrUpdate(baseVO);
				
		savedBaseVO = dao.findByPK(savedBaseVO);
		
		return savedBaseVO;
	};
	
	/**
	 * Alterar generico.
	 */
	public B update(B iBaseVO) throws JazzBusinessException {
		logInsertUpdate(iBaseVO);

		this.includeUpdateValidations(iBaseVO);
		this.updateValidations(iBaseVO);
		
		return this.getDao().update(iBaseVO);
	}


	/**
	 * @param iBaseVO
	 */
	protected void logInsertUpdate(B iBaseVO) {
		
		if(iBaseVO!=null && iBaseVO instanceof ILoggableVO){
			ILoggableVO loggable = (ILoggableVO) iBaseVO;
			
			String loginName = SecurityHelper.getInstance().getLoginName();
			
			Date dtInc = new Date();
			
			if(iBaseVO.isNew()){
				loggable.setCriadoPor(loginName);
				loggable.setDtInc(dtInc);
			}
			
			loggable.setAlteradoPor(loginName);
			loggable.setDtAlt(dtInc);
		}
	};
	
	/**
	 * Implemente aqui as validaAAes de negAcio do seu modulo. As exceptions de validacao de negAcio devem ser acumuladas num
	 * JazzBusinessException e depois este deve ser lancado.
	 */
	@Override
	public void updateValidations(final B baseVO) throws JazzBusinessException {
		if(log.isDebugEnabled())
		log.debug("Nao ha validacao para atualizacao de registro nesta classe");
	}


	/**
	 * Testa se vo é novo
	 * @param questionnaireVO
	 * @return
	 */
	protected boolean isNew(B baseVO) {
		return baseVO!=null && baseVO.getPK()==null;
	}
	

	
}
