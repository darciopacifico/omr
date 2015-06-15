/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.produto;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.business.AbstractBusinessImpl;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.QueryOrder;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente ProdutoVO
 *
 **/
@Component
public class ProdutoBusinessImpl extends AbstractBusinessImpl<ProdutoVO> implements ProdutoBusiness {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	protected ProdutoDAO produtoDAO;

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
	public List<ProdutoVO> findProdutoVO(Date dtInclusaoFrom, Date dtInclusaoTo, String obs, String nome, String dsAreaProduto){
		return produtoDAO.findProdutoVO(dtInclusaoFrom, dtInclusaoTo, obs, nome, dsAreaProduto);
	}

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
	public List<ProdutoVO> findProdutoVO(Date dtInclusaoFrom, Date dtInclusaoTo, String obs, String nome, String dsAreaProduto, QueryOrder... queryOrders){
		return produtoDAO.findProdutoVO(dtInclusaoFrom, dtInclusaoTo, obs, nome, dsAreaProduto, queryOrders);
	}
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<ProdutoVO> getDao() {
		return produtoDAO;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public ProdutoVO newVO() {
		return new ProdutoVO();
	}
	
}