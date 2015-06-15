/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.pessoa;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.AbstractHibernateDAO;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzqa.projeto.enums.StatusEnum;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de DAO para o componente PessoaVO
 *
 **/
@Component
public class PessoaHibernateDAOImpl extends AbstractHibernateDAO<PessoaVO> implements PessoaDAO {
	
	private static final long serialVersionUID = 4188683927282245182L;
	
	/**
	 * Pesquisa entidades do tipo PessoaVO
	 * @author t_dpacifico
	 * @param login
	 * @param email
	 * @param nome
	 * @param telefone
	 * @param dtInc
	 * @param dtAlt
	 * @param status
	 * @returns Coleção de PessoaVO
	 */
	public List<PessoaVO> findPessoaVO(String login, String email, String nome, String telefone, Date dtInc, Date dtAlt, StatusEnum status){
		
		DetachedCriteria criteria = createDetachedCriteria();
		
		eqRestriction(login, criteria, "login");
		eqRestriction(email, criteria, "email");
		eqRestriction(nome, criteria, "nome");
		eqRestriction(telefone, criteria, "telefone");
		eqRestriction(dtInc, criteria, "dtInc");
		eqRestriction(dtAlt, criteria, "dtAlt");
		eqRestriction(status, criteria, "status");
		
		return getHibernateTemplate().findByCriteria(criteria);
		
	}
	
	/**
	 * Pesquisa entidades do tipo PessoaVO
	 * @author t_dpacifico
	 * @param login
	 * @param email
	 * @param nome
	 * @param telefone
	 * @param dtInc
	 * @param dtAlt
	 * @param status
	 * @param ExtraArgumentsDTO Ordenação de pesquisa
	 * @returns Coleção de PessoaVO
	 */
	public List<PessoaVO> findPessoaVO(String login, String email, String nome, String telefone, Date dtInc, Date dtAlt, StatusEnum status, ExtraArgumentsDTO metaArgument){
		
		DetachedCriteria criteria = createDetachedCriteria();
		
		eqRestriction(login, criteria, "login");
		eqRestriction(email, criteria, "email");
		eqRestriction(nome, criteria, "nome");
		eqRestriction(telefone, criteria, "telefone");
		eqRestriction(dtInc, criteria, "dtInc");
		eqRestriction(dtAlt, criteria, "dtAlt");
		eqRestriction(status, criteria, "status");
		
		order(criteria, metaArgument);
		return getHibernateTemplate().findByCriteria(criteria);
		
	}
}

