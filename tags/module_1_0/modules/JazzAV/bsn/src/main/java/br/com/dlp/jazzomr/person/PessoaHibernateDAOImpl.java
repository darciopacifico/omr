/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.person;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.exam.AbstractJazzOMRHibernateDAO;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de DAO para o componente PessoaVO
 *
 **/
@Component
public class PessoaHibernateDAOImpl extends AbstractJazzOMRHibernateDAO<PessoaVO> implements PessoaDAO {

	private static final long serialVersionUID = 4188683927282245182L;

	/**
	 * Pesquisa entidades do tipo PessoaVO 
	 * @author darcio

	 * @param login
 	 * @param email
 	 * @param nome
 	 * @param telefone
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de PessoaVO
	 */
	public List<PessoaVO> findPessoaVO(
String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		
		DetachedCriteria criteria = createDetachedCriteria();
		

	 	eqRestriction(login, criteria, "login");
 	 	eqRestriction(email, criteria, "email");
 	 	eqRestriction(nome, criteria, "nome");
 	 	eqRestriction(telefone, criteria, "telefone");
 		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
 		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
 	 	eqRestriction(status, criteria, "status");
		
		return findByCriteria(criteria);
		
	}
	
	/**
	 * Pesquisa entidades do tipo PessoaVO 
	 * @author darcio

	 * @param login
 	 * @param email
 	 * @param nome
 	 * @param telefone
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de PessoaVO
	 */
	public Long countPessoaVO(
String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		
		DetachedCriteria criteria = createDetachedCriteria();
		

		ilikeRestriction(login, criteria, "login");
		ilikeRestriction(email, criteria, "email");
		ilikeRestriction(nome, criteria, "nome");
		ilikeRestriction(telefone, criteria, "telefone");
 		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
 		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
 	 	eqRestriction(status, criteria, "status");
		
		criteria.setProjection(Projections.rowCount() );
		
		List list = findByCriteria(criteria);
		
		return (Long) list.get(0);
		
	}

	/**
	 * Pesquisa entidades do tipo PessoaVO 
	 * @author darcio

	 * @param login
 	 * @param email
 	 * @param nome
 	 * @param telefone
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de PessoaVO
	 */
	public List<PessoaVO> findPessoaVO(String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO){
	
		DetachedCriteria criteria = createDetachedCriteria();
		
	 	ilikeRestriction(login, criteria, "login");
	 	ilikeRestriction(email, criteria, "email");
	 	ilikeRestriction(nome, criteria, "nome");
	 	ilikeRestriction(telefone, criteria, "telefone");
 		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
 		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
 	 	eqRestriction(status, criteria, "status");
		
		order(criteria, extraArgumentsDTO);
		return findByCriteria(criteria, extraArgumentsDTO);

	}
}

