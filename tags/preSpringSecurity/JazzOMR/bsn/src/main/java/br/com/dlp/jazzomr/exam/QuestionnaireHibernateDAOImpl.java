/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.exam;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.question.QuestionVO;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de DAO para o componente QuestionnaireVO
 *
 **/
@Component
@Transactional(propagation=Propagation.REQUIRED)
public class QuestionnaireHibernateDAOImpl extends AbstractJazzOMRHibernateDAO<QuestionnaireVO> implements QuestionnaireDAO {

	private static final long serialVersionUID = 4188683927282245182L;

	/**
	 * Pesquisa entidades do tipo QuestionnaireVO 
	 * @author darcio

	 * @param questions
 	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de QuestionnaireVO
	 */
	public List<QuestionnaireVO> findQuestionnaireVO(Set<QuestionVO> questions, String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		
		DetachedCriteria criteria = createDetachedCriteria();

	 	eqRestriction(questions, criteria, "questions");
 	 	eqRestriction(description, criteria, "description");
 		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
 		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
 	 	eqRestriction(status, criteria, "status");
		
 	 	criteria.setFetchMode("questions.alternatives", FetchMode.JOIN);

 	 	return getHibernateTemplate().findByCriteria(criteria);
		
	}
	
	/**
	 * Pesquisa entidades do tipo QuestionnaireVO 
	 * @author darcio

	 * @param questions
 	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de QuestionnaireVO
	 */
	public Long countQuestionnaireVO(
Set<QuestionVO> questions, String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		
		DetachedCriteria criteria = createDetachedCriteria();
		

	 	eqRestriction(questions, criteria, "questions");
 	 	eqRestriction(description, criteria, "description");
 		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
 		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
 	 	eqRestriction(status, criteria, "status");
		
		criteria.setProjection(Projections.rowCount() );
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		
		return (Long) list.get(0);
		
	}

	/**
	 * Pesquisa entidades do tipo QuestionnaireVO 
	 * @author darcio

	 * @param questions
 	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de QuestionnaireVO
	 */
	public List<QuestionnaireVO> findQuestionnaireVO(
Set<QuestionVO> questions, String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO){
	
		DetachedCriteria criteria = createDetachedCriteria();
		

	 	eqRestriction(questions, criteria, "questions");
	 	ilikeRestriction(description, criteria, "description");
 		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
 		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
 	 	eqRestriction(status, criteria, "status");
		
		order(criteria, extraArgumentsDTO);
		
		Integer firstResult = extraArgumentsDTO.getFirstResult();
		Integer maxResults = extraArgumentsDTO.getMaxResults();
		
		//ajustando estrategia de fetch
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List resutls = getHibernateTemplate().findByCriteria(criteria,firstResult,maxResults);
		
		return resutls;

	}
}

