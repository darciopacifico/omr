/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.status;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.AbstractHibernateDAO;
import br.com.dlp.framework.dao.QueryOrder;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de DAO para o componente StatusVO
 *
 **/
@Component
public class StatusHibernateDAOImpl extends AbstractHibernateDAO<StatusVO> implements StatusDAO {

	private static final long serialVersionUID = 4188683927282245182L;

	/**
	 * Pesquisa entidades do tipo StatusVO 
	 * @author t_dpacifico
	 * @param titulo
 	 * @param descricao
	 * @returns Coleção de StatusVO
	 */
	public List<StatusVO> findStatusVO(String titulo, String descricao){
		
		DetachedCriteria criteria = createDetachedCriteria();
		
	 	eqRestriction(titulo, criteria, "titulo");
 	 	eqRestriction(descricao, criteria, "descricao");
		
		return getHibernateTemplate().findByCriteria(criteria);
		
	}

	/**
	 * Pesquisa entidades do tipo StatusVO 
	 * @author t_dpacifico
	 * @param titulo
 	 * @param descricao
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de StatusVO
	 */
	public List<StatusVO> findStatusVO(String titulo, String descricao, QueryOrder... queryOrders){
	
		DetachedCriteria criteria = createDetachedCriteria();
		
	 	eqRestriction(titulo, criteria, "titulo");
 	 	eqRestriction(descricao, criteria, "descricao");
		
		order(criteria, queryOrders);
		return getHibernateTemplate().findByCriteria(criteria);

	}
}

