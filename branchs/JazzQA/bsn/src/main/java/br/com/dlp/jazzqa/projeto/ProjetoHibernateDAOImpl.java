/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzqa.projeto;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.AbstractHibernateDAO;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzqa.projeto.enums.StatusEnum;
import br.com.dlp.jazzqa.projeto.enums.TipoProjetoEnum;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de DAO para o componente ProjetoVO
 *
 **/
@Component
public class ProjetoHibernateDAOImpl extends AbstractHibernateDAO<ProjetoVO> implements ProjetoDAO {

	private static final long serialVersionUID = 4188683927282245182L;

	/**
	 * Pesquisa entidades do tipo ProjetoVO 
	 * @author t_dpacifico
	 * @param tipoProjeto
 	 * @param dtInc
 	 * @param dtAlt
 	 * @param status
	 * @returns Coleção de ProjetoVO
	 */
	public List<ProjetoVO> findProjetoVO(TipoProjetoEnum tipoProjeto, Date dtInc, Date dtAlt, StatusEnum status){
		
		DetachedCriteria criteria = createDetachedCriteria();
		
	 	eqRestriction(tipoProjeto, criteria, "tipoProjeto");
 	 	eqRestriction(dtInc, criteria, "dtInc");
 	 	eqRestriction(dtAlt, criteria, "dtAlt");
 	 	eqRestriction(status, criteria, "status");
		
		return getHibernateTemplate().findByCriteria(criteria);
		
	}

	/**
	 * Pesquisa entidades do tipo ProjetoVO 
	 * @author t_dpacifico
	 * @param tipoProjeto
 	 * @param dtInc
 	 * @param dtAlt
 	 * @param status
	 * @param ExtraArgumentsDTO Ordenação de pesquisa
	 * @returns Coleção de ProjetoVO
	 */
	public List<ProjetoVO> findProjetoVO(TipoProjetoEnum tipoProjeto, Date dtInc, Date dtAlt, StatusEnum status, ExtraArgumentsDTO  metaArgument){
	
		DetachedCriteria criteria = createDetachedCriteria();
		
	 	eqRestriction(tipoProjeto, criteria, "tipoProjeto");
 	 	eqRestriction(dtInc, criteria, "dtInc");
 	 	eqRestriction(dtAlt, criteria, "dtAlt");
 	 	eqRestriction(status, criteria, "status");
		
		order(criteria, metaArgument);
		return getHibernateTemplate().findByCriteria(criteria);

	}
}

