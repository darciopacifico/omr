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

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.jazzomr.base.AbstractJazzOMRBusinessImpl;
import br.com.dlp.jazzomr.base.StatusEnum;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente GrupoVO
 *
 **/
@Component
@WebService
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
	@WebMethod
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