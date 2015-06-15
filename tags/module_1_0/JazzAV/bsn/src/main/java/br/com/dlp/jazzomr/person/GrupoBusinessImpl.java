/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.person;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.jazzomr.base.AbstractJazzOMRBusinessImpl;
import br.com.dlp.jazzomr.base.IOrganizationRestrictVO;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.empresa.EmpresaVO;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente GrupoVO
 *
//@WebService
 **/
@Component
public class GrupoBusinessImpl extends AbstractJazzOMRBusinessImpl<GrupoVO> implements GrupoBusiness {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	private GrupoDAO grupoDAO;

	/**
	 * Pesquisa entidades do tipo GrupoVO 
	 * @author darcio

	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de GrupoVO
	 */
	public List<GrupoVO> findGrupoVO(
String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		return grupoDAO.findGrupoVO(
description, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}


	
	@Override
	protected void logInsertUpdate(GrupoVO iBaseVO) {

		
		if(iBaseVO!=null && IOrganizationRestrictVO.class.isAssignableFrom(iBaseVO.getClass())){

			IOrganizationRestrictVO iOrganizationRestrictVO = iBaseVO; 
			
			EmpresaVO empresaUsuarioLogado = getEmpresaUsuarioLogado();
			
			IOrganizationRestrictVO organizationRestrictVO = (IOrganizationRestrictVO) iBaseVO;
			EmpresaVO empresaVOLogging = organizationRestrictVO.getEmpresaVO();
			
			if(empresaVOLogging!=null && empresaUsuarioLogado!=null){
				preventEnterpriseChange(empresaUsuarioLogado, empresaVOLogging);
			}
			
			iOrganizationRestrictVO.setEmpresaVO(empresaUsuarioLogado);
		}
	}
	
	
	/**
	 * Pesquisa entidades do tipo GrupoVO 
	 * @author darcio

	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de GrupoVO
	 */
	public Long countGrupoVO(
String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		return grupoDAO.countGrupoVO(
description, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}

	/**
	 * Pesquisa entidades do tipo GrupoVO 
	 * @author darcio

	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @param extraArgumentsDTO Paginacao e Ordenação de pesquisa
	 * @returns Coleção de GrupoVO
	 */
	public List<GrupoVO> findGrupoVO(
String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO){
		return grupoDAO.findGrupoVO(
description, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO);
	}
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<GrupoVO> getDao() {
		return grupoDAO;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public GrupoVO newVO() {
		return new GrupoVO();
	}
	
}