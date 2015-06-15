/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.tiporequisito;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.cxf.annotations.DataBinding;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.dao.QueryOrder;


/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente TipoRequisitoVO
 *
 **/
@DataBinding(org.apache.cxf.aegis.databinding.AegisDatabinding.class)
@WebService
public interface TipoRequisitoBusiness extends IBusiness<TipoRequisitoVO> {

	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO 
	 * @author t_dpacifico
	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param descricao
 	 * @param nome
	 * @returns Coleção de TipoRequisitoVO
	 */
	@WebMethod
	List<TipoRequisitoVO> findTipoRequisitoVO(Date dtInclusaoFrom, Date dtInclusaoTo, String descricao, String nome);

	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO 
	 * @author t_dpacifico
	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param descricao
 	 * @param nome
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de TipoRequisitoVO
	 */
	@WebMethod
	List<TipoRequisitoVO> findTipoRequisitoVOOrdered(Date dtInclusaoFrom, Date dtInclusaoTo, String descricao, String nome, QueryOrder... queryOrders);
	
}