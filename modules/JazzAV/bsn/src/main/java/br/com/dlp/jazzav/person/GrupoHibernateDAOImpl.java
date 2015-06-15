/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzav.person;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzav.StatusEnum;
import br.com.dlp.jazzav.exam.AbstractJazzOMRHibernateDAO;
import br.com.dlp.jazzav.person.GrupoVO;

/**
 * Incluir arquivo com comentários
 *
 * Implementação de DAO para o componente GrupoVO
 *
 **/
@Component
public class GrupoHibernateDAOImpl extends AbstractJazzOMRHibernateDAO<GrupoVO> implements GrupoDAO {

	private static final long serialVersionUID = 4188683927282245182L;

	@Autowired
	private PessoaDAO pessoaDAO;
	
	/**
	 * Pesquisa entidades do tipo GrupoVO 
	 * @author darcio

	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de GrupoVO
	 */
	public List<GrupoVO> findGrupoVO(
			String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		
		DetachedCriteria criteria = createDetachedCriteria();
		
		ilikeRestriction(description, criteria, "description");
 		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
 		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
 	 	eqRestriction(status, criteria, "status");
		
		return findByCriteria(criteria);
		
	}
	
	
	
	/**
	 * Pesquisa entidades do tipo GrupoVO 
	 * @author darcio

	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @returns Coleção de GrupoVO
	 */
	public Long countGrupoVO(String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status){
		
		DetachedCriteria criteria = createDetachedCriteria();

		ilikeRestriction(description, criteria, "description");
 		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
 		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
 	 	eqRestriction(status, criteria, "status");
		
		criteria.setProjection(Projections.rowCount() );
		
		List list = findByCriteria(criteria);
		
		return (Long) list.get(0);
		
	}

	/**
	 * Pesquisa entidades do tipo GrupoVO 
	 * @author darcio

	 * @param description
 	 * @param dtIncFrom
 	 * @param dtIncTo
 	 * @param dtAltFrom
 	 * @param dtAltTo
 	 * @param status
	 * @param QueryOrder Ordenação de pesquisa
	 * @returns Coleção de GrupoVO
	 */
	public List<GrupoVO> findGrupoVO(String description, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO){
	
		DetachedCriteria criteria = createDetachedCriteria();

		ilikeRestriction(description, criteria, "description");
 		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
 		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
 	 	eqRestriction(status, criteria, "status");
		
		order(criteria, extraArgumentsDTO);
		return findByCriteria(criteria, extraArgumentsDTO);

	}
}

