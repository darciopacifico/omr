/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.empresa;


import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.JazzDetachedCriteria;
import br.com.dlp.framework.security.SecurityHelper;
import br.com.dlp.jazzomr.base.AbstractJazzOMRBusinessImpl;
import br.com.dlp.jazzomr.base.StatusEnum;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente EmpresaVO
 *
//@WebService
 **/
@Component
public class EmpresaBusinessImpl extends AbstractJazzOMRBusinessImpl<EmpresaVO> implements EmpresaBusiness {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	private EmpresaDAO empresaDAO;

	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Override
	public EmpresaVO empresaUsuarioLogado() {
		
		String loginName = SecurityHelper.getInstance().getLoginName();
		
		EmpresaVO empresaVO = empresaUsuario(loginName);
		
		
		return empresaVO;
	}

	/**
	 * @param loginName
	 * @return
	 */
	@Override
	public EmpresaVO empresaUsuario(String loginName) {
		JazzDetachedCriteria criteria = JazzDetachedCriteria.forClass(EmpresaVO.class);
		
		EmpresaVO empresaVO=null;
		
		criteria.add(Restrictions.sqlRestriction("{alias}.pk = (select fk_empresa from tb_pessoa where login = ?) ", loginName, Hibernate.STRING));
		
		
		List<EmpresaVO> empresas = hibernateTemplate.findByCriteria(criteria);
		
		if(CollectionUtils.isNotEmpty(empresas)){
			
			empresaVO = empresas.get(0);
		
		}
		return empresaVO;
	}

	
	/**
	 * Pesquisa entidades do tipo EmpresaVO 
	 * @author darcio

	 * @param nome
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de EmpresaVO
	 */
	public List<EmpresaVO> findEmpresaVO(
String nome, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		return empresaDAO.findEmpresaVO(
nome, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}


	/**
	 * Pesquisa entidades do tipo EmpresaVO 
	 * @author darcio

	 * @param nome
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de EmpresaVO
	 */
	public Long countEmpresaVO(
String nome, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		return empresaDAO.countEmpresaVO(
nome, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}

	/**
	 * Pesquisa entidades do tipo EmpresaVO 
	 * @author darcio

	 * @param nome
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @param extraArgumentsDTO Paginacao e Ordenação de pesquisa
	 * @returns Coleção de EmpresaVO
	 */
	public List<EmpresaVO> findEmpresaVO(
String nome, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO){
		return empresaDAO.findEmpresaVO(
nome, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO);
	}
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<EmpresaVO> getDao() {
		return empresaDAO;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public EmpresaVO newVO() {
		return new EmpresaVO();
	}
	
	
	
}