/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.usuariojazz;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.AbstractHibernateDAO;
import br.com.dlp.framework.dao.QueryOrder;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de DAO para o componente UsuarioJazzVO
 *
 **/
@Component
public class UsuarioJazzHibernateDAOImpl extends AbstractHibernateDAO<UsuarioJazzVO> implements UsuarioJazzDAO {

	private static final long serialVersionUID = 4188683927282245182L;

	/**
	 * Pesquisa entidades do tipo UsuarioJazzVO 
	 * @author dpacifico
	 * @param nome
 	 * @param login
	 * @returns Coleção de UsuarioJazzVO
	 */
	public List<UsuarioJazzVO> findUsuarioJazzVO(String nome, String login){
		
		DetachedCriteria criteria = createDetachedCriteria();
		
	 	eqRestriction(nome, criteria, "nome");
 	 	eqRestriction(login, criteria, "login");
		
		return getHibernateTemplate().findByCriteria(criteria);
		
	}

	/**
	 * Pesquisa entidades do tipo UsuarioJazzVO 
	 * @author dpacifico
	 * @param nome
 	 * @param login
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de UsuarioJazzVO
	 */
	public List<UsuarioJazzVO> findUsuarioJazzVO(String nome, String login, QueryOrder... queryOrders){
	
		DetachedCriteria criteria = createDetachedCriteria();
		
	 	eqRestriction(nome, criteria, "nome");
 	 	eqRestriction(login, criteria, "login");
		
		order(criteria, queryOrders);
		return getHibernateTemplate().findByCriteria(criteria);

	}
}

