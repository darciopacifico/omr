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
	 * @author dpacifico
	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param obs
 	 * @param nome
 	 * @param dsAreaProduto
	 * @returns Coleção de ProdutoVO
	 */
	public List<ProdutoVO> findProdutoVO(Date dtInclusaoFrom, Date dtInclusaoTo, String obs, String nome, String dsAreaProduto);

	/**
	 * Pesquisa entidades do tipo ProdutoVO 
	 * @author dpacifico
	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param obs
 	 * @param nome
 	 * @param dsAreaProduto
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de ProdutoVO
	 */
	public List<ProdutoVO> findProdutoVO(Date dtInclusaoFrom, Date dtInclusaoTo, String obs, String nome, String dsAreaProduto, QueryOrder... queryOrders);

	
}

