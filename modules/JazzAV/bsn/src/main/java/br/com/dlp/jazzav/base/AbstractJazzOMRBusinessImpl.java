package br.com.dlp.jazzav.base;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;

import br.com.dlp.framework.business.AbstractBusinessImpl;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.security.SecurityHelper;
import br.com.dlp.framework.vo.IBaseVO;
import br.com.dlp.framework.vo.ILoggableVO;
import br.com.dlp.jazzav.IOrganizationRestrictVO;
import br.com.dlp.jazzav.exam.IJazzOMRBusiness;
import br.com.dlp.jazzav.exam.IJazzOMRDAO;

/**
 * Classe especializacao de abstractBusinessImpl. Implementa findByPK com fetchProfile
 * @author darcio
 *
 * @param <B>
 */
public abstract class AbstractJazzOMRBusinessImpl<B extends IBaseVO<? extends Serializable>> extends AbstractBusinessImpl<B> implements IJazzOMRBusiness<B> {
	
	private static final long serialVersionUID = -8514100142210577366L;
	
	@Autowired   
	private HibernateTemplate hibernateTemplate;
	
	/**
	 * Implementacao de findByPK com fetch profile
	 */
	@Override
	public B findByPK(B bean, String... fetchProfileName) {
		IJazzOMRDAO<B> dao = (IJazzOMRDAO<B>) this.getDao();
		return dao.findByPK(bean, fetchProfileName);
	}

	/**
	 * @param voClass
	 * @return
	 */
	protected DetachedCriteria createDetachedCriteria(Class<? extends Object> voClass) {
		return JazzOMRDetachedCriteria.forClass(voClass);
	}
	
	/**
	 * 
	 */
	protected void logInsertUpdate(B iBaseVO) {
		/*
		if(iBaseVO!=null && IOrganizationRestrictVO.class.isAssignableFrom(iBaseVO.getClass())){
			String loginName = SecurityHelper.getInstance().getLoginName();
			
			
			EmpresaVO empresaUsuarioLogado = getEmpresaUsuarioLogado();
			
			if(empresaUsuarioLogado==null){
				throw new JazzRuntimeException("É obrigatório que o usuario logado esteja associado a uma empresa (empresa==null)!");
			}
			
			
			IOrganizationRestrictVO organizationRestrictVO = (IOrganizationRestrictVO) iBaseVO;
			EmpresaVO empresaVOLogging = organizationRestrictVO.getEmpresaVO();
			
			if(empresaVOLogging!=null){
				//já existe uma empresa associada a este registro. Vou ver se é a mesma do usuario logado!
				preventEnterpriseChange(empresaUsuarioLogado, empresaVOLogging);
			}else{
				//o registro é novo, por tanto, nao existe uma empresa associada. Vou associar a empresa do usuario logado!
				organizationRestrictVO.setEmpresaVO(empresaUsuarioLogado);
			}
			
			
			
		}
		*/
		
		
		if(iBaseVO!=null && iBaseVO instanceof ILoggableVO){
			ILoggableVO loggable = (ILoggableVO) iBaseVO;
			
			String loginName = SecurityHelper.getInstance().getLoginName();
			
			if(StringUtils.isNotBlank(loginName)){
				if(iBaseVO.isNew()){
					loggable.setCriadoPor(loginName);
				}
				loggable.setAlteradoPor(loginName);
			}
			

			Date dtInc = new Date();
			loggable.setDtAlt(dtInc);
		}		
	}

	/*
	 * @param empresaUsuarioLogado
	 * @param empresaVOLogging
	protected void preventEnterpriseChange(EmpresaVO empresaUsuarioLogado, EmpresaVO empresaVOLogging) {
		if(empresaVOLogging!=null && !empresaVOLogging.equals(empresaUsuarioLogado)){
			throw new JazzRuntimeException("É proibido que o usuario de uma empresa ("+empresaUsuarioLogado+") faça alterações em registros de outra empresa ("+empresaVOLogging+")!");
		}
	}
	 */

	

	/* (non-Javadoc)
	 * @see br.com.dlp.jazzav.base.IJazzOMRBusiness#getEmpresaUsuarioLogado()
	@Override
	public EmpresaVO getEmpresaUsuarioLogado() {
		
		String loginName = SecurityHelper.getInstance().getLoginName();
		
		String hqlQuery = 
				" select emp " +
				" from PessoaVO pes " +
				" inner join pes.empresaVO emp " +
				" where pes.login = :loginName ";
				
		List<EmpresaVO> empresas = hibernateTemplate.findByNamedParam(hqlQuery, "loginName", loginName);
		
		EmpresaVO empresaVO=null;
		if(CollectionUtils.isNotEmpty(empresas)){
			empresaVO = empresas.get(0);
		}
		
		return empresaVO;
	}
	 */
	

	
}
