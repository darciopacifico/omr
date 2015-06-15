/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.tiporequisito;

import java.util.Date;
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
	 * @author t_dpacifico
	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param descricao
 	 * @param nome
	 * @returns Coleção de TipoRequisitoVO
	 */
	public List<TipoRequisitoVO> findTipoRequisitoVO(Date dtInclusaoFrom, Date dtInclusaoTo, String descricao, String nome){
		
		DetachedCriteria criteria = createDetachedCriteria();
		
		rangeRestriction(dtInclusaoFrom, dtInclusaoTo, criteria, "dtInclusao");
 		ilikeRestriction(descricao, criteria, "descricao");
 	 	eqRestriction(nome, criteria, "nome");
		
		return getHibernateTemplate().findByCriteria(criteria);
		
	}

	/**
	 * Pesquisa entidades do tipo TipoRequisitoVO 
	 * @author t_dpacifico
	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param descricao
 	 * @param nome
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de TipoRequisitoVO
	 */
	public List<TipoRequisitoVO> findTipoRequisitoVO(Date dtInclusaoFrom, Date dtInclusaoTo, String descricao, String nome, QueryOrder... queryOrders){
	
		DetachedCriteria criteria = createDetachedCriteria();
		
		rangeRestriction(dtInclusaoFrom, dtInclusaoTo, criteria, "dtInclusao");
 		ilikeRestriction(descricao, criteria, "descricao");
 	 	eqRestriction(nome, criteria, "nome");
		
		order(criteria, queryOrders);
		return getHibernateTemplate().findByCriteria(criteria);

	}
}

