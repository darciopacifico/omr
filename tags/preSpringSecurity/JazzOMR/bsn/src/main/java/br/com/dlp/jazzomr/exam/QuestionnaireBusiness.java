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

import org.apache.cxf.annotations.DataBinding;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.question.QuestionVO;


/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente QuestionnaireVO
 *
 **/
@DataBinding(org.apache.cxf.aegis.databinding.AegisDatabinding.class)
@WebService
@Transactional(propagation=Propagation.REQUIRED)
public interface QuestionnaireBusiness extends IJazzOMRBusiness<QuestionnaireVO> {






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
	List<QuestionnaireVO> findQuestionnaireVO(
Set<QuestionVO> questions, String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status);


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
	Long countQuestionnaireVO(
Set<QuestionVO> questions, String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status);


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
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de QuestionnaireVO
	 */
	@WebMethod
	List<QuestionnaireVO> findQuestionnaireVO(
Set<QuestionVO> questions, String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO);


	
}