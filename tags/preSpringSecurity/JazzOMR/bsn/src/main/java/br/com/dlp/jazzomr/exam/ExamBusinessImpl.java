/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.exam;

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
 * Contrado de Business para o componente ExamVO
 *
 **/
@Component
@WebService
public class ExamBusinessImpl extends AbstractJazzOMRBusinessImpl<ExamVO> implements ExamBusiness {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	private ExamDAO examDAO;

	/**
	 * Pesquisa entidades do tipo ExamVO 
	 * @author darcio

	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de ExamVO
	 */
	public List<ExamVO> findExamVO(
String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		return examDAO.findExamVO(
description, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}


	/**
	 * Pesquisa entidades do tipo ExamVO 
	 * @author darcio

	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de ExamVO
	 */
	@WebMethod
	public Long countExamVO(
String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		return examDAO.countExamVO(
description, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}

	/**
	 * Pesquisa entidades do tipo ExamVO 
	 * @author darcio

	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @param extraArgumentsDTO Paginacao e Ordenação de pesquisa
	 * @returns Coleção de ExamVO
	 */
	public List<ExamVO> findExamVO(
String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO){
		return examDAO.findExamVO(
description, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO);
	}
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<ExamVO> getDao() {
		return examDAO;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public ExamVO newVO() {
		return new ExamVO();
	}
	
}