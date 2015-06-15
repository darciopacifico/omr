/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.exam;

import java.util.Date;
import java.util.List;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzomr.base.StatusEnum;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de DAO para o componente ExamVO
 *
 **/
public interface ExamDAO extends IJazzOMRDAO<ExamVO>{



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
	List<ExamVO> findExamVO(
String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status);

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
	Long countExamVO(
String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status);

	/**
	 * Pesquisa entidades do tipo ExamVO 
	 * @author darcio

	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de ExamVO
	 */
	List<ExamVO> findExamVO(
String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO);

	
}

