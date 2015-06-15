/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.produto;

import java.util.Date;
import java.util.List;

import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.QueryOrder;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de DAO para o componente ProdutoVO
 *
 **/
public interface ProdutoDAO extends IDAO<ProdutoVO>{

	/**
	 * Pesquisa entidades do tipo ProdutoVO 
	 * @author t_dpacifico
	 * @param dsAreaProduto
 	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param nome
 	 * @param obs
	 * @returns Coleção de ProdutoVO
	 */
	List<ProdutoVO> findProdutoVO(String dsAreaProduto, Date dtInclusaoFrom, Date dtInclusaoTo, String nome, String obs);

	/**
	 * Pesquisa entidades do tipo ProdutoVO 
	 * @author t_dpacifico
	 * @param dsAreaProduto
 	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param nome
 	 * @param obs
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de ProdutoVO
	 */
	List<ProdutoVO> findProdutoVO(String dsAreaProduto, Date dtInclusaoFrom, Date dtInclusaoTo, String nome, String obs, QueryOrder... queryOrders);

	
}

