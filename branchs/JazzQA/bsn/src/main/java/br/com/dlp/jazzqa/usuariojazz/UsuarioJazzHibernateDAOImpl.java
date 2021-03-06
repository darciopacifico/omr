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
import br.com.dlp.framework.dao.ExtraArgumentsDTO;

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
	 * @author t_dpacifico
	 * @param altura
 	 * @param login
 	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param nome
	 * @returns Coleção de UsuarioJazzVO
	 */
	public List<UsuarioJazzVO> findUsuarioJazzVO(double altura, String login, Date dtInclusaoFrom, Date dtInclusaoTo, String nome){
		
		DetachedCriteria criteria = createDetachedCriteria();
		
	 	eqRestriction(altura, criteria, "altura");
 	 	eqRestriction(login, criteria, "login");
 		rangeRestriction(dtInclusaoFrom, dtInclusaoTo, criteria, "dtInclusao");
 	 	eqRestriction(nome, criteria, "nome");
		
		return getHibernateTemplate().findByCriteria(criteria);
		
	}

	/**
	 * Pesquisa entidades do tipo UsuarioJazzVO 
	 * @author t_dpacifico
	 * @param altura
 	 * @param login
 	 * @param dtInclusaoFrom
 	 * @param dtInclusaoTo
 	 * @param nome
	 * @param ExtraArgumentsDTO Ordenação de pesquisa
	 * @returns Coleção de UsuarioJazzVO
	 */
	public List<UsuarioJazzVO> findUsuarioJazzVO(double altura, String login, Date dtInclusaoFrom, Date dtInclusaoTo, String nome, ExtraArgumentsDTO  metaArgument){
	
		DetachedCriteria criteria = createDetachedCriteria();
		
	 	eqRestriction(altura, criteria, "altura");
 	 	eqRestriction(login, criteria, "login");
 		rangeRestriction(dtInclusaoFrom, dtInclusaoTo, criteria, "dtInclusao");
 	 	eqRestriction(nome, criteria, "nome");
		
		order(criteria, metaArgument);
		return getHibernateTemplate().findByCriteria(criteria);

	}
}

