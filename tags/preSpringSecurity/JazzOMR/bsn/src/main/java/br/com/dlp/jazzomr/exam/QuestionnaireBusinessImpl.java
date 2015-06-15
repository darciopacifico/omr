/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.exam;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.jazzomr.base.AbstractJazzOMRBusinessImpl;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.question.QuestionVO;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente QuestionnaireVO
 *
 **/
@Component
@WebService
@Transactional(propagation=Propagation.REQUIRED)
public class QuestionnaireBusinessImpl extends AbstractJazzOMRBusinessImpl<QuestionnaireVO> implements QuestionnaireBusiness {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	private QuestionnaireDAO questionnaireDAO;

	/**
	 * Pesquisa entidades do tipo QuestionnaireVO 
	 * @author darcio

	 * @param questions
 	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de QuestionnaireVO
	 */
	public List<QuestionnaireVO> findQuestionnaireVO(
Set<QuestionVO> questions, String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		return questionnaireDAO.findQuestionnaireVO(
questions, description, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}


	/**
	 * Pesquisa entidades do tipo QuestionnaireVO 
	 * @author darcio

	 * @param questions
 	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de QuestionnaireVO
	 */
	@WebMethod
	public Long countQuestionnaireVO(
Set<QuestionVO> questions, String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		return questionnaireDAO.countQuestionnaireVO(
questions, description, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}

	/**
	 * Pesquisa entidades do tipo QuestionnaireVO 
	 * @author darcio

	 * @param questions
 	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @param extraArgumentsDTO Paginacao e Ordenação de pesquisa
	 * @returns Coleção de QuestionnaireVO
	 */
	public List<QuestionnaireVO> findQuestionnaireVO(
Set<QuestionVO> questions, String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO){
		return questionnaireDAO.findQuestionnaireVO(questions, description, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO);
	}
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<QuestionnaireVO> getDao() {
		return questionnaireDAO;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public QuestionnaireVO newVO() {
		return new QuestionnaireVO();
	}
	
}