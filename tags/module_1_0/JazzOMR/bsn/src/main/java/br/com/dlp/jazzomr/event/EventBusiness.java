/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.event;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.cxf.annotations.DataBinding;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzomr.application.result.CriterionResultVO;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.empresa.RelatorioVO;
import br.com.dlp.jazzomr.exam.IJazzOMRBusiness;
import br.com.dlp.jazzomr.exam.coordinate.CriterionCoordinateVO;
import br.com.dlp.jazzomr.results.CriterionDetailDTO;
import br.com.dlp.jazzomr.results.PayloadVO;


/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente EventVO
 *
 **/
@DataBinding(org.apache.cxf.aegis.databinding.AegisDatabinding.class)
@WebService
public interface EventBusiness extends IJazzOMRBusiness<EventVO> {

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
	@WebMethod
	List<EventVO> findEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status);

	
	
	List<EventVO> findEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom,
			Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO, Boolean hasParts, String... fetchProfile) ;

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
	@WebMethod
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
	@WebMethod
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
	@WebMethod
	List<EventVO> findEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO, String... fetchProfile);

	
	


	/**
	 * Pesquisa as cargas de processamento do sistema. Normalmente as pendentes, para exibicao na tela de gerenciamento 
	 * @author darcio

	 * @returns Coleção de EventVO
	 */
	@WebMethod
	List<PayloadVO> findPayloadVO(ExtraArgumentsDTO extraArgumentsDTO, String... fetchProfile);


	@WebMethod(exclude=true)
	boolean hasExamPDF(EventVO eventVO);


	Long countNotProcessedPayloadVO();


	PayloadVO findPayloadByPK(PayloadVO selPayloadVO, String... fetchProfile);


	List<CriterionDetailDTO> findCriterionDetails(EventVO eventVO, ExtraArgumentsDTO extraArgumentsDTO);


	Long countCriterionDetails(final EventVO eventVO); 

	@WebMethod(exclude = true)
	CriterionResultVO saveOrUpdate(CriterionResultVO criterionResultVO);


	CriterionResultVO findQuestionResult(CriterionResultVO criterionResultVO);


	void saveQuestionResultVO(CriterionResultVO criterionResultVO);


	List<EventVO> findEvents(List<EventVO> resultados,ExtraArgumentsDTO extraArgumentsDTO);

	
	List<EventVO> findEventResults(EventVO eventVO);


	List<RelatorioVO> findDistinctReports(EventVO eventVO);



	PayloadVO findPayloadVO(Long participationPK, Integer pagina);


	List<Integer> fetchPaginas(CriterionCoordinateVO criterionCoordinateVO);



	public abstract Long countEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, Boolean hasParts);
	
}