/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.event;

import java.util.Date;
import java.util.List;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.exam.IJazzOMRDAO;
import br.com.dlp.jazzomr.results.PayloadVO;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de DAO para o componente EventVO
 *
 **/
public interface EventDAO extends IJazzOMRDAO<EventVO>{

	/**
	 * Pesquisa entidades do tipo EventVO 
	 * @author darcio

	 * @param description
 	 * @param dtFimFrom
 	 * @param dtFimTo
 	 * @param dtInicioFrom
 	 * @param dtInicioTo
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de EventVO
	 */
	List<EventVO> findEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status);

	/**
	 * Pesquisa entidades do tipo EventVO 
	 * @author darcio

	 * @param description
 	 * @param dtFimFrom
 	 * @param dtFimTo
 	 * @param dtInicioFrom
 	 * @param dtInicioTo
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de EventVO
	 */
	Long countEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status);

	/**
	 * Pesquisa entidades do tipo EventVO 
	 * @author darcio

	 * @param description
 	 * @param dtFimFrom
 	 * @param dtFimTo
 	 * @param dtInicioFrom
 	 * @param dtInicioTo
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de EventVO
	 */
	List<EventVO> findEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO);


	/**
	 * Pesquisa entidades do tipo EventVO 
	 * @author darcio

	 * @param description
 	 * @param dtFimFrom
 	 * @param dtFimTo
 	 * @param dtInicioFrom
 	 * @param dtInicioTo
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de EventVO
	 */
	List<EventVO> findEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO, String... fetchProfile);

	/**
	 * 
	 * @param eventVO
	 * @return
	 */
	boolean hasExamPDF(EventVO eventVO);

	List<PayloadVO> findPayloadVO(ExtraArgumentsDTO extraArgumentsDTO, String... fetchProfile);

	Long countNotProcessedPayloadVO();

	PayloadVO findPayloadByPK(PayloadVO selPayloadVO, String... fetchProfile);


	
}

