/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.person;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.JazzDetachedCriteria;
import br.com.dlp.jazzomr.base.AbstractJazzOMRBusinessImpl;
import br.com.dlp.jazzomr.base.StatusEnum;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente PessoaVO
 *
 **/
@Component
@WebService
public class PessoaBusinessImpl extends AbstractJazzOMRBusinessImpl<PessoaVO> implements PessoaBusiness {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Autowired
	private PessoaDAO pessoaDAO;

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
		return pessoaDAO.findPessoaVO(
login, email, nome, telefone, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}

	
	@Override
	public PessoaVO findPessoa(Principal principal) {
		
		if(principal==null){
			return null;
		}
		
		
		JazzDetachedCriteria criteria = JazzDetachedCriteria.forClass(PessoaVO.class);
		String loginName = principal.getName();
		
		criteria.add(Restrictions.eq("login", loginName));
		
		List<PessoaVO> pessoas = hibernateTemplate.findByCriteria(criteria);
		
		PessoaVO pessoaVO = null;
		
		if(CollectionUtils.isNotEmpty(pessoas)){
			pessoaVO = pessoas.get(0);
		}
		
		if(pessoas.size()>1 ){
			log.warn("Comportamento anormal. Mais de um registro para o login '"+loginName+"' foi encontrado!");
		}
		
		return pessoaVO;
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
	@WebMethod
	public Long countPessoaVO(
String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		return pessoaDAO.countPessoaVO(
login, email, nome, telefone, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
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
	 * @param extraArgumentsDTO Paginacao e Ordenação de pesquisa
	 * @returns Coleção de PessoaVO
	 */
	public List<PessoaVO> findPessoaVO(
String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO){
		return pessoaDAO.findPessoaVO(
login, email, nome, telefone, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO);
	}
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<PessoaVO> getDao() {
		return pessoaDAO;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public PessoaVO newVO() {
		return new PessoaVO();
	}
	
}