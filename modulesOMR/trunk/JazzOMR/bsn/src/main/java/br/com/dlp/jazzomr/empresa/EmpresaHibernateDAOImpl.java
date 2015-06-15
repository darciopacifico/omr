/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.empresa;

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
 * Implementação de DAO para o componente EmpresaVO
 * 
 **/
@Component
public class EmpresaHibernateDAOImpl extends AbstractJazzOMRHibernateDAO<EmpresaVO> implements EmpresaDAO {

	private static final long serialVersionUID = 4188683927282245182L;

	/**
	 * Pesquisa entidades do tipo EmpresaVO
	 * 
	 * @author darcio
	 * 
	 * @param nome
	 * @param dtIncFrom
	 * @param dtIncTo
	 * @param dtAltFrom
	 * @param dtAltTo
	 * @param status
	 * @returns Coleção de EmpresaVO
	 */
	public List<EmpresaVO> findEmpresaVO(String nome, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status) {

		
		
		DetachedCriteria criteria = createDetachedCriteria();

		eqRestriction(nome, criteria, "nome");
		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
		eqRestriction(status, criteria, "status");

		return findByCriteria(criteria);

	}

	/**
	 * Pesquisa entidades do tipo EmpresaVO
	 * 
	 * @author darcio
	 * 
	 * @param nome
	 * @param dtIncFrom
	 * @param dtIncTo
	 * @param dtAltFrom
	 * @param dtAltTo
	 * @param status
	 * @returns Coleção de EmpresaVO
	 */
	public Long countEmpresaVO(String nome, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status) {

		DetachedCriteria criteria = createDetachedCriteria();

		eqRestriction(nome, criteria, "nome");
		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
		eqRestriction(status, criteria, "status");

		criteria.setProjection(Projections.rowCount());

		List list = findByCriteria(criteria);

		return (Long) list.get(0);

	}

	/**
	 * Pesquisa entidades do tipo EmpresaVO
	 * 
	 * @author darcio
	 * 
	 * @param nome
	 * @param dtIncFrom
	 * @param dtIncTo
	 * @param dtAltFrom
	 * @param dtAltTo
	 * @param status
	 * @param QueryOrder
	 *          Ordenação de pesquisa
	 * @returns Coleção de EmpresaVO
	 */
	public List<EmpresaVO> findEmpresaVO(String nome, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO) {

		DetachedCriteria criteria = createDetachedCriteria();

		eqRestriction(nome, criteria, "nome");
		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
		eqRestriction(status, criteria, "status");

		order(criteria, extraArgumentsDTO);
		return findByCriteria(criteria, extraArgumentsDTO);

	}
}
