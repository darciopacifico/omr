/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.tiporequisito;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.AbstractHibernateDAO;
import br.com.dlp.framework.dao.QueryOrder;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de DAO para o componente TipoRequisitoVO
 *
 **/
@Component
public class TipoRequisitoHibernateDAOImpl extends AbstractHibernateDAO<TipoRequisitoVO> implements TipoRequisitoDAO {

	private static final long serialVersionUID = 4188683927282245182L;

	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO 
	 * @author dpacifico
	 * @param nome
 	 * @param descricao
	 * @returns Coleção de TipoRequisitoVO
	 */
	public List<TipoRequisitoVO> findTipoRequisitoVO(String nome, String descricao){
		
		DetachedCriteria criteria = createDetachedCriteria();
		
	 	eqRestriction(nome, criteria, "nome");
 		ilikeRestriction(descricao, criteria, "descricao");
		
		return getHibernateTemplate().findByCriteria(criteria);
		
	}

	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO 
	 * @author dpacifico
	 * @param nome
 	 * @param descricao
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de TipoRequisitoVO
	 */
	public List<TipoRequisitoVO> findTipoRequisitoVO(String nome, String descricao, QueryOrder... queryOrders){
	
		DetachedCriteria criteria = createDetachedCriteria();
		
	 	eqRestriction(nome, criteria, "nome");
 		ilikeRestriction(descricao, criteria, "descricao");
		
		order(criteria, queryOrders);
		return getHibernateTemplate().findByCriteria(criteria);

	}
}

