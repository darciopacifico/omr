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

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.jazzomr.base.AbstractJazzOMRBusinessImpl;
import br.com.dlp.jazzomr.base.JazzOMRBusinessConsistencyException;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.empresa.RelatorioVO;

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

	

	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	/**
	 * 
	 */
	@Override
	public void includeUpdateValidations(ExamVO examVO) throws JazzBusinessException {
		
		if(!isNew(examVO)){
			String queryString = 
					" select distinct eve.PK" +
					" from 				EventVO eve " +
					" inner join 	eve.participations par " +
					" inner join 	par.examVariantVO exv " +
					" inner join 	exv.examVO exa " +
					" where exa = :examVO " +
					" order by eve.PK ";
			
			List<Long> eventPKs = hibernateTemplate.findByNamedParam(queryString, "examVO", examVO);
	
			if(CollectionUtils.isNotEmpty(eventPKs)){
				throw new JazzOMRBusinessConsistencyException("Este exame não pode ser alterado pois está sendo utilizado pelas agendas "+eventPKs+"!");
			}
		}
		
		super.includeUpdateValidations(examVO);
	}
	
	
	@Override
	public void deleteValidations(ExamVO examVO) throws JazzBusinessException {
		this.includeUpdateValidations(examVO);
		super.deleteValidations(examVO);
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