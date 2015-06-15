/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.exam;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.question.QuestionVO;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de DAO para o componente QuestionnaireVO
 *
 **/
public interface QuestionnaireDAO extends IJazzOMRDAO<QuestionnaireVO>{

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
	List<QuestionnaireVO> findQuestionnaireVO(
Set<QuestionVO> questions, String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO);

	
}

