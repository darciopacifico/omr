/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.tiporequisito;

import java.util.Date;
import java.util.List;

import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.QueryOrder;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de DAO para o componente TipoRequisitoVO
 *
 **/
public interface TipoRequisitoDAO extends IDAO<TipoRequisitoVO>{

	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO 
	 * @author dpacifico
	 * @param nome
 	 * @param descricao
	 * @returns Coleção de TipoRequisitoVO
	 */
	public List<TipoRequisitoVO> findTipoRequisitoVO(String nome, String descricao);

	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO 
	 * @author dpacifico
	 * @param nome
 	 * @param descricao
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de TipoRequisitoVO
	 */
	public List<TipoRequisitoVO> findTipoRequisitoVO(String nome, String descricao, QueryOrder... queryOrders);

	
}

