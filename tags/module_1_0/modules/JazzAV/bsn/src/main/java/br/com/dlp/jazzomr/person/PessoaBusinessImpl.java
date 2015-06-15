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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.security.SecurityHelper;
import br.com.dlp.jazzomr.base.AbstractJazzOMRBusinessImpl;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.empresa.EmpresaVO;

/**
 * Incluir arquivo com comentários
 * 
 * Contrado de Business para o componente PessoaVO
 * 
 **/
@Component
public class PessoaBusinessImpl extends AbstractJazzOMRBusinessImpl<PessoaVO> implements PessoaBusiness {

	private static final long serialVersionUID = -4018418907098545031L;

	@Autowired
	private HibernateTemplate hibernateTemplate;


	/**
	 * 
	 */
	@Override
	public PessoaVO revogarAcesso(PessoaVO pessoaVO) throws JazzBusinessException {
		
		pessoaVO.setStatus(StatusEnum.INACTIVE);

		if(pessoaVO.getPK()!=null){
			pessoaVO = saveOrUpdate(pessoaVO);
		}
		
		return pessoaVO;
	}
	
	/**
	 * Checa se existe o login informado
	 */
	@Override
	public Boolean existsLogin(String login) {
		
		if(StringUtils.isBlank(login)){
			return false;
		}
		
		List<Long> pessoaPks = hibernateTemplate.findByNamedParam("select PK from PessoaVO where login = :login", "login", login);
		
		return CollectionUtils.isNotEmpty(pessoaPks);
	}
	
	/**
	 * 
	 */
	@Override
	public void includeUpdateValidations(PessoaVO pessoaVO) throws JazzBusinessException {
		
		/*
		String queryString = 
				" select 			eve.PK" +
				" from 				EventVO eve " +
				" inner join 	eve.participations par " +
				" inner join 	par.examVariantVO exv " +
				" inner join 	exv.examVO exa " +
				" where exa = :examVO ";
		
		List<Long> eventPKs = hibernateTemplate.findByNamedParam(queryString, "examVO", pessoaVO);

		if(CollectionUtils.isNotEmpty(eventPKs)){
			throw new JazzOMRBusinessConsistencyException("Este exame não pode ser alterado ou excluído porque está sendo utilizado pelos eventos a seguir "+eventPKs+"!");
		}
		*/
		super.includeUpdateValidations(pessoaVO);
	}
	
	
	/**
	 * 
	 */
	@Override
	public void deleteValidations(PessoaVO pessoaVO) throws JazzBusinessException {
		this.includeUpdateValidations(pessoaVO);
		super.deleteValidations(pessoaVO);
	}
	
	
	
	@Autowired
	private PessoaDAO pessoaDAO;

	/**
	 * Pesquisa entidades do tipo PessoaVO
	 * 
	 * @author darcio
	 * 
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
	public List<PessoaVO> findPessoaVO(String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status) {
		return pessoaDAO.findPessoaVO(login, email, nome, telefone, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}

	
	@Override
	public PessoaVO saveOrUpdate(PessoaVO baseVO) throws JazzBusinessException {
		PessoaVO pessoa = super.saveOrUpdate(baseVO);
		pessoa.setNovo(false);
		
		return pessoa;
	}

	
	@Override
	public PessoaVO update(PessoaVO iBaseVO) throws JazzBusinessException {
		PessoaVO pessoa = super.update(iBaseVO);
		pessoa.setNovo(false);
		return pessoa;
	}
	
	
	@Override
	protected void logInsertUpdate(PessoaVO pessoaVO) {

		if(pessoaVO!=null){

			EmpresaVO empresaUsuarioLogado = getEmpresaUsuarioLogado();
			
			EmpresaVO empresaVOLogging = pessoaVO.getEmpresaVO();
			
			if(empresaVOLogging!=null && empresaUsuarioLogado!=null){
				//Se não é um acesso anonimo e já existe uma empresa definida para o vo, entao tem que ser a mesma empresa
				preventEnterpriseChange(empresaUsuarioLogado, empresaVOLogging);
			}
			
			if(empresaUsuarioLogado!=null){
				pessoaVO.setEmpresaVO(empresaUsuarioLogado);
			}
			
		
			//loga alteracoes de pessoa
			String loginName = SecurityHelper.getInstance().getLoginName();

			if(StringUtils.isNotBlank(loginName)){
				if(StringUtils.isBlank(pessoaVO.getCriadoPor())){
					pessoaVO.setCriadoPor(loginName);
				}
				pessoaVO.setAlteradoPor(loginName);
			}
			
			pessoaVO.setDtAlt(new Date());
			
		}
	}
	
	/**
	 * Retorna o registro de pessoa para o Principal logado
	 */
	@Override
	public PessoaVO findPessoa(Principal principal) {

		String loginName = SecurityHelper.getInstance().getLoginName(principal);

		if(loginName==null){
			return null;
		}
		
		DetachedCriteria criteria = createDetachedCriteria(PessoaVO.class);

		criteria.add(Restrictions.eq("login", loginName));

		List<PessoaVO> pessoas = hibernateTemplate.findByCriteria(criteria);

		PessoaVO pessoaVO = null;

		if (CollectionUtils.isNotEmpty(pessoas)) {
			pessoaVO = pessoas.get(0);
		}

		return pessoaVO;
	}

	/**
	 * Pesquisa entidades do tipo PessoaVO
	 * 
	 * @author darcio
	 * 
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
	public Long countPessoaVO(String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status) {
		
		
		//Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		return pessoaDAO.countPessoaVO(login, email, nome, telefone, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}

	/**
	 * Pesquisa entidades do tipo PessoaVO
	 * 
	 * @author darcio
	 * 
	 * @param login
	 * @param email
	 * @param nome
	 * @param telefone
	 * @param dtIncFrom
	 * @param dtIncTo
	 * @param dtAltFrom
	 * @param dtAltTo
	 * @param status
	 * @param extraArgumentsDTO
	 *          Paginacao e Ordenação de pesquisa
	 * @returns Coleção de PessoaVO
	 */
	public List<PessoaVO> findPessoaVO(String login, String email, String nome, String telefone, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status,
			ExtraArgumentsDTO extraArgumentsDTO) {
		return pessoaDAO.findPessoaVO(login, email, nome, telefone, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<PessoaVO> getDao() {
		return pessoaDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public PessoaVO newVO() {
		return new PessoaVO();
	}

}