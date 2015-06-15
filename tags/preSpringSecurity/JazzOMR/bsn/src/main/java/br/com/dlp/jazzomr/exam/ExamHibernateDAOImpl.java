/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.exam;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzomr.base.StatusEnum;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de DAO para o componente ExamVO
 *
 **/
@Component
public class ExamHibernateDAOImpl extends AbstractJazzOMRHibernateDAO<ExamVO> implements ExamDAO {

	private static final long serialVersionUID = 4188683927282245182L;

	/**
	 * Pesquisa entidades do tipo ExamVO 
	 * @author darcio

	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de ExamVO
	 */
	public List<ExamVO> findExamVO(
String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		
		DetachedCriteria criteria = createDetachedCriteria();
		
		
		ilikeRestriction(description, criteria, "description");
 		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
 		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
 	 	eqRestriction(status, criteria, "status");
		
		return getHibernateTemplate().findByCriteria(criteria);
		
	}
	
	/**
	 * Pesquisa entidades do tipo ExamVO 
	 * @author darcio

	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de ExamVO
	 */
	public Long countExamVO(String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		
		DetachedCriteria criteria = createDetachedCriteria();
		

		ilikeRestriction(description, criteria, "description");
 		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
 		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
 	 	eqRestriction(status, criteria, "status");
		
		criteria.setProjection(Projections.rowCount() );
		
		List list = getHibernateTemplate().findByCriteria(criteria);
		
		return (Long) list.get(0);
		
	}

	/**
	 * Pesquisa entidades do tipo ExamVO 
	 * @author darcio

	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de ExamVO
	 */
	public List<ExamVO> findExamVO(
String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO){
	
		DetachedCriteria criteria = createDetachedCriteria();
		
		ilikeRestriction(description, criteria, "description");
 		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
 		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
 	 	eqRestriction(status, criteria, "status");
		
		order(criteria, extraArgumentsDTO);
		return getHibernateTemplate().findByCriteria(criteria, extraArgumentsDTO.getFirstResult(), extraArgumentsDTO.getMaxResults());

	}
}

