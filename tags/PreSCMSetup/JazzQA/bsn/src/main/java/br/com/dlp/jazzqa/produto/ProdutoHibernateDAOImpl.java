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
	 * @author t_dpacifico
	 * @param dsAreaProduto
 	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param nome
 	 * @param obs
	 * @returns Coleção de ProdutoVO
	 */
	public List<ProdutoVO> findProdutoVO(String dsAreaProduto, Date dtInclusaoFrom, Date dtInclusaoTo, String nome, String obs){
		
		DetachedCriteria criteria = createDetachedCriteria();
		
	 	eqRestriction(dsAreaProduto, criteria, "dsAreaProduto");
 		rangeRestriction(dtInclusaoFrom, dtInclusaoTo, criteria, "dtInclusao");
 		ilikeRestriction(nome, criteria, "nome");
 	 	eqRestriction(obs, criteria, "obs");
		
		return getHibernateTemplate().findByCriteria(criteria);
		
	}

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
	public List<ProdutoVO> findProdutoVO(String dsAreaProduto, Date dtInclusaoFrom, Date dtInclusaoTo, String nome, String obs, QueryOrder... queryOrders){
	
		DetachedCriteria criteria = createDetachedCriteria();
		
	 	eqRestriction(dsAreaProduto, criteria, "dsAreaProduto");
 		rangeRestriction(dtInclusaoFrom, dtInclusaoTo, criteria, "dtInclusao");
 		ilikeRestriction(nome, criteria, "nome");
 	 	eqRestriction(obs, criteria, "obs");
		
		order(criteria, queryOrders);
		return getHibernateTemplate().findByCriteria(criteria);

	}
}

