/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.produto;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.AbstractHibernateDAO;
import br.com.dlp.framework.dao.QueryOrder;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de DAO para o componente ProdutoVO
 *
 **/
@Component
public class ProdutoHibernateDAOImpl extends AbstractHibernateDAO<ProdutoVO> implements ProdutoDAO {

	private static final long serialVersionUID = 4188683927282245182L;

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
		
		DetachedCriteria criteria = createDetachedCriteria();
		
		rangeRestriction(dtInclusaoFrom, dtInclusaoTo, criteria, "dtInclusao");
 	 	eqRestriction(obs, criteria, "obs");
 		ilikeRestriction(nome, criteria, "nome");
 	 	eqRestriction(dsAreaProduto, criteria, "dsAreaProduto");
		
		return getHibernateTemplate().findByCriteria(criteria);
		
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
	
		DetachedCriteria criteria = createDetachedCriteria();
		
		rangeRestriction(dtInclusaoFrom, dtInclusaoTo, criteria, "dtInclusao");
 	 	eqRestriction(obs, criteria, "obs");
 		ilikeRestriction(nome, criteria, "nome");
 	 	eqRestriction(dsAreaProduto, criteria, "dsAreaProduto");
		
		order(criteria, queryOrders);
		return getHibernateTemplate().findByCriteria(criteria);

	}
}

