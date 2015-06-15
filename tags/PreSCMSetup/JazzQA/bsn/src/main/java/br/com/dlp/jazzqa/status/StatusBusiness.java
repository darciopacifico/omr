/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.status;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.cxf.annotations.DataBinding;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.dao.QueryOrder;


/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente StatusVO
 *
 **/
@DataBinding(org.apache.cxf.aegis.databinding.AegisDatabinding.class)
@WebService
public interface StatusBusiness extends IBusiness<StatusVO> {

	/**
	 * Pesquisa entidades do tipo StatusVO 
	 * @author t_dpacifico
	 * @param titulo
 	 * @param descricao
	 * @returns Coleção de StatusVO
	 */
	@WebMethod
	List<StatusVO> findStatusVO(String titulo, String descricao);

	/**
	 * Pesquisa entidades do tipo StatusVO 
	 * @author t_dpacifico
	 * @param titulo
 	 * @param descricao
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de StatusVO
	 */
	@WebMethod
	List<StatusVO> findStatusVOOrdered(String titulo, String descricao, QueryOrder... queryOrders);
	
}