/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.produto;

import java.util.Date;
import java.util.List;

import br.com.dlp.framework.business.IBusiness;
import br.com.dlp.framework.dao.QueryOrder;


/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente ProdutoVO
 *
 **/
public interface ProdutoBusiness extends IBusiness<ProdutoVO> {

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