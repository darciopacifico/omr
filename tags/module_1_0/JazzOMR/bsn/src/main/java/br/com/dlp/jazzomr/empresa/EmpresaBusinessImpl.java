/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.empresa;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.JazzDetachedCriteria;
import br.com.dlp.framework.security.SecurityHelper;
import br.com.dlp.jazzomr.base.AbstractJazzOMRBusinessImpl;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.exam.ExamVO;

/**
 * Incluir arquivo com comentários
 *
 * Contrado de Business para o componente EmpresaVO
 *
 **/
@Component
@WebService
public class EmpresaBusinessImpl extends AbstractJazzOMRBusinessImpl<EmpresaVO> implements EmpresaBusiness {
	
	private static final long serialVersionUID = -4018418907098545031L;
	
	@Autowired
	private EmpresaDAO empresaDAO;

	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	
	
	
	
	
	/**
	 * Checa se o relatorio informado foi utilizado em algum exame
	 */
	@Override
	public boolean hasExams(RelatorioVO relatorioVO) {
		
		if(relatorioVO.getPK()==null){
			return false;
		}
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ExamVO.class);
		
		criteria.add(Restrictions.eq("relatorioVO", relatorioVO));
		
		List<ExamVO> exams = hibernateTemplate.findByCriteria(criteria);
		
		return CollectionUtils.isNotEmpty(exams);
	}
	
	
	@Override
	public void deleteRelatorio(EmpresaVO empresaVO, RelatorioVO relatorioVO) {
		if(!hasExams(relatorioVO)){
			
			empresaVO.getRelatorioVOs().remove(relatorioVO);
			
			hibernateTemplate.saveOrUpdate(empresaVO);
		}
	}
	
	
	@Override
	public EmpresaVO empresaUsuarioLogado() {
		
		String loginName = SecurityHelper.getInstance().getLoginName();
		
		EmpresaVO empresaVO = empresaUsuario(loginName);
		
		
		return empresaVO;
	}

	/**
	 * @param loginName
	 * @return
	 */
	@Override
	public EmpresaVO empresaUsuario(String loginName) {
		JazzDetachedCriteria criteria = JazzDetachedCriteria.forClass(EmpresaVO.class);
		
		EmpresaVO empresaVO=null;
		
		criteria.add(Restrictions.sqlRestriction("{alias}.pk = (select fk_empresa from tb_pessoa where login = ?) ", loginName, Hibernate.STRING));
		
		
		List<EmpresaVO> empresas = hibernateTemplate.findByCriteria(criteria);
		
		if(CollectionUtils.isNotEmpty(empresas)){
			
			empresaVO = empresas.get(0);
			
			//TODO resolver o fetch da empresa de uma maneira mais elegante
			//forcando fetch dos relacionamentos de empresa
			empresaVO.getLogos().size();
			List<RelatorioVO> relatorios = empresaVO.getRelatorioVOs();
			
			for (RelatorioVO relatorioVO : relatorios) {
				relatorioVO.getJrFileVOs().size();
			}
			
		}
		return empresaVO;
	}
	
	/**
	 * Recupera os relatorio de exame registrados para esta empresa.
	 */
	@Override
	public List<RelatorioVO> findRelatorios() {
		String loginName = SecurityHelper.getInstance().getLoginName();
		
		return findRelatorios(loginName);
	}


	/**
	 * @param loginName
	 * @return
	 */
	public List<RelatorioVO> findRelatorios(String loginName) {
		@SuppressWarnings("unchecked")
		List<RelatorioVO> relatorios = hibernateTemplate.findByNamedParam(
				" select distinct rpt  \n" +
				" from PessoaVO pes  \n" +
				"  inner join pes.empresaVO as emp \n" + 
				"  inner join emp.relatorioVOs as rpt \n" + 
				"	where	pes.login = :loginName \n",
				
		new String[] { "loginName" }, new Object[] { loginName });
		
		return relatorios;
	}
	
	
	/**
	 * Pesquisa entidades do tipo EmpresaVO 
	 * @author darcio

	 * @param nome
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de EmpresaVO
	 */
	public List<EmpresaVO> findEmpresaVO(
String nome, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		return empresaDAO.findEmpresaVO(
nome, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}


	/**
	 * Pesquisa entidades do tipo EmpresaVO 
	 * @author darcio

	 * @param nome
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de EmpresaVO
	 */
	@WebMethod
	public Long countEmpresaVO(
String nome, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		return empresaDAO.countEmpresaVO(
nome, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
	}

	/**
	 * Pesquisa entidades do tipo EmpresaVO 
	 * @author darcio

	 * @param nome
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @param extraArgumentsDTO Paginacao e Ordenação de pesquisa
	 * @returns Coleção de EmpresaVO
	 */
	public List<EmpresaVO> findEmpresaVO(
String nome, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO){
		return empresaDAO.findEmpresaVO(
nome, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO);
	}
	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<EmpresaVO> getDao() {
		return empresaDAO;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public EmpresaVO newVO() {
		return new EmpresaVO();
	}
	
	
	
}