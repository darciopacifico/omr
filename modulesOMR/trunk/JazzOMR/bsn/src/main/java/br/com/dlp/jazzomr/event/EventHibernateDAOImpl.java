/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.event;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.jazzomr.base.JazzOMRDetachedCriteria;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.exam.AbstractJazzOMRHibernateDAO;
import br.com.dlp.jazzomr.results.EProcessingState;
import br.com.dlp.jazzomr.results.PayloadVO;

/**
 * Incluir arquivo com comentários
 * 
 * Implementação de DAO para o componente EventVO
 * 
 **/
@Component
public class EventHibernateDAOImpl extends AbstractJazzOMRHibernateDAO<EventVO> implements EventDAO {

	private static final long serialVersionUID = 4188683927282245182L;

	@Override
	public List<PayloadVO> findPayloadVO(ExtraArgumentsDTO extraArgumentsDTO, String... fetchProfile) {

		Class<PayloadVO> clazz = PayloadVO.class;
		JazzOMRDetachedCriteria criteria = createDetachedCriteria(clazz);

		criteria.enableFetchProfile(fetchProfile);

		criteria.add(Restrictions.not(Restrictions.in("processingState", new EProcessingState[] { EProcessingState.TERMINATED })));

		return getHibernateTemplate().findByCriteria(criteria, extraArgumentsDTO.getFirstResult(), extraArgumentsDTO.getMaxResults());
	}

	@Override
	public PayloadVO findPayloadByPK(PayloadVO selPayloadVO, String... fetchProfile) {

		JazzOMRDetachedCriteria criteria = createDetachedCriteria(PayloadVO.class);
		criteria.add(Restrictions.eq("PK", selPayloadVO.getPK()));

		enableFetchProfiles(criteria, fetchProfile);

		List<PayloadVO> beans = getHibernateTemplate().findByCriteria(criteria);

		PayloadVO beanReturn = null;
		if (beans.size() > 0) {
			beanReturn = beans.get(0);
		}

		return beanReturn;
	}

	@Override
	public Long countNotProcessedPayloadVO() {

		DetachedCriteria criteria = createDetachedCriteria(PayloadVO.class);

		criteria.add(Restrictions.not(Restrictions.in("processingState", new EProcessingState[] { EProcessingState.TERMINATED })));

		criteria.setProjection(Projections.rowCount());

		List list = findByCriteria(criteria);

		return (Long) list.get(0);

	}

	@Override
	public boolean hasExamPDF(EventVO eventVO) {

		if (eventVO == null)
			return false;

		HibernateTemplate hibernateTemplate = getHibernateTemplate();

		@SuppressWarnings("unchecked")
		List<Long> pksArquivo = hibernateTemplate.findByNamedParam(" select distinct arq.PK " + " from EventVO as eve " + "  inner join eve.arquivoProvaVOs arq "
				+ "	where	eve.PK = :eventPK and arq.status = 1  ",

		new String[] { "eventPK" }, new Object[] { eventVO.getPK() });

		return pksArquivo.size() > 0;

	}

	/**
	 * Pesquisa entidades do tipo EventVO
	 * 
	 * @author darcio
	 * @param description
	 * @param dtFimFrom
	 * @param dtFimTo
	 * @param dtInicioFrom
	 * @param dtInicioTo
	 * @param dtIncFrom
	 * @param dtIncTo
	 * @param dtAltFrom
	 * @param dtAltTo
	 * @param status
	 * @returns Coleção de EventVO
	 */
	public List<EventVO> findEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom,
			Date dtAltTo, StatusEnum status) {

		DetachedCriteria criteria = createDetachedCriteria();

		ilikeRestriction(description, criteria, "description");
		rangeRestriction(dtFimFrom, dtFimTo, criteria, "dtFim");
		rangeRestriction(dtInicioFrom, dtInicioTo, criteria, "dtInicio");
		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
		eqRestriction(status, criteria, "status");

		return findByCriteria(criteria);

	}

	/**
	 * Pesquisa entidades do tipo EventVO
	 * 
	 * @author darcio
	 * 
	 * @param description
	 * @param dtFimFrom
	 * @param dtFimTo
	 * @param dtInicioFrom
	 * @param dtInicioTo
	 * @param dtIncFrom
	 * @param dtIncTo
	 * @param dtAltFrom
	 * @param dtAltTo
	 * @param status
	 * @returns Coleção de EventVO
	 */
	public Long countEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo,
			StatusEnum status) {

		return countEventVO(description, dtFimFrom, dtFimTo, dtInicioFrom, dtInicioTo, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, null);

	}

	/**
	 * Pesquisa entidades do tipo EventVO
	 * 
	 * @author darcio
	 * 
	 * @param description
	 * @param dtFimFrom
	 * @param dtFimTo
	 * @param dtInicioFrom
	 * @param dtInicioTo
	 * @param dtIncFrom
	 * @param dtIncTo
	 * @param dtAltFrom
	 * @param dtAltTo
	 * @param status
	 * @returns Coleção de EventVO
	 */
	public Long countEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo,
			StatusEnum status, Boolean hasParts) {

		DetachedCriteria criteria = createDetachedCriteria();

		if (BooleanUtils.isTrue(hasParts)) {
			criteria.add(Restrictions.sqlRestriction(" exists (select 1 from tb_participation where fk_event = {alias}.PK) "));
		}

		ilikeRestriction(description, criteria, "description");
		rangeRestriction(dtFimFrom, dtFimTo, criteria, "dtFim");
		rangeRestriction(dtInicioFrom, dtInicioTo, criteria, "dtInicio");
		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
		eqRestriction(status, criteria, "status");

		criteria.setProjection(Projections.rowCount());

		List list = findByCriteria(criteria);

		return (Long) list.get(0);

	}

	/**
	 * Pesquisa entidades do tipo EventVO
	 * 
	 * @author darcio
	 * 
	 * @param description
	 * @param dtFimFrom
	 * @param dtFimTo
	 * @param dtInicioFrom
	 * @param dtInicioTo
	 * @param dtIncFrom
	 * @param dtIncTo
	 * @param dtAltFrom
	 * @param dtAltTo
	 * @param status
	 * @param QueryOrder
	 *          Ordenação de pesquisa
	 * @returns Coleção de EventVO
	 */
	public List<EventVO> findEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom,
			Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO) {

		return findEventVO(description, dtFimFrom, dtFimTo, dtInicioFrom, dtInicioTo, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO, (String) null);

	}

	/**
	 * Pesquisa entidades do tipo EventVO
	 * 
	 * @author darcio
	 * 
	 * @param description
	 * @param dtFimFrom
	 * @param dtFimTo
	 * @param dtInicioFrom
	 * @param dtInicioTo
	 * @param dtIncFrom
	 * @param dtIncTo
	 * @param dtAltFrom
	 * @param dtAltTo
	 * @param status
	 * @param QueryOrder
	 *          Ordenação de pesquisa
	 * @returns Coleção de EventVO
	 */
	@Override
	public List<EventVO> findEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom,
			Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO, Boolean hasParts, String... fetchProfile) {

		JazzOMRDetachedCriteria criteria = createDetachedCriteria();

		// habilita fetch profiles
		criteria.enableFetchProfile(fetchProfile);

		if (BooleanUtils.isTrue(hasParts)) {
			criteria.add(Restrictions.sqlRestriction(" exists (select 1 from tb_participation where fk_event = {alias}.PK) "));
		}

		ilikeRestriction(description, criteria, "description");
		rangeRestriction(dtFimFrom, dtFimTo, criteria, "dtFim");
		rangeRestriction(dtInicioFrom, dtInicioTo, criteria, "dtInicio");
		rangeRestriction(dtIncFrom, dtIncTo, criteria, "dtInc");
		rangeRestriction(dtAltFrom, dtAltTo, criteria, "dtAlt");
		eqRestriction(status, criteria, "status");

		order(criteria, extraArgumentsDTO);
		return findByCriteria(criteria, extraArgumentsDTO);

	}

	/**
	 * Pesquisa entidades do tipo EventVO
	 * 
	 * @author darcio
	 * 
	 * @param description
	 * @param dtFimFrom
	 * @param dtFimTo
	 * @param dtInicioFrom
	 * @param dtInicioTo
	 * @param dtIncFrom
	 * @param dtIncTo
	 * @param dtAltFrom
	 * @param dtAltTo
	 * @param status
	 * @param QueryOrder
	 *          Ordenação de pesquisa
	 * @returns Coleção de EventVO
	 */
	public List<EventVO> findEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom,
			Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO, String... fetchProfile) {
		return this.findEventVO(description, dtFimFrom, dtFimTo, dtInicioFrom, dtInicioTo, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO, null, fetchProfile);
	}

}
