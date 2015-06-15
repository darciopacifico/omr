/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.status;

import java.util.List;

import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.QueryOrder;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de DAO para o componente StatusVO
 *
 **/
public interface StatusDAO extends IDAO<StatusVO>{

	/**
	 * Pesquisa entidades do tipo StatusVO 
	 * @author t_dpacifico
	 * @param titulo
 	 * @param descricao
	 * @returns Coleção de StatusVO
	 */
	List<StatusVO> findStatusVO(String titulo, String descricao);

	/**
	 * Pesquisa entidades do tipo StatusVO 
	 * @author t_dpacifico
	 * @param titulo
 	 * @param descricao
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de StatusVO
	 */
	List<StatusVO> findStatusVO(String titulo, String descricao, QueryOrder... queryOrders);

	
}

