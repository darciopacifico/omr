/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.event;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.AbstractHibernateDAO;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.vo.ISortable;
import br.com.dlp.jazzomr.application.result.CriterionResultVO;
import br.com.dlp.jazzomr.base.AbstractJazzOMRBusinessImpl;
import br.com.dlp.jazzomr.base.JazzOMRBusinessConsistencyException;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.empresa.EmpresaVO;
import br.com.dlp.jazzomr.empresa.RelatorioVO;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.exam.ExamVariantVO;
import br.com.dlp.jazzomr.exam.QuestionnaireVO;
import br.com.dlp.jazzomr.exam.coordinate.CriterionCoordinateVO;
import br.com.dlp.jazzomr.question.AbstractCriterionVO;
import br.com.dlp.jazzomr.question.QuestionVO;
import br.com.dlp.jazzomr.results.CriterionDetailDTO;
import br.com.dlp.jazzomr.results.PayloadVO;

/**
 * Incluir arquivo com comentários
 * 
 * Contrado de Business para o componente EventVO
 * 
 **/
@Component
public class EventBusinessImpl extends AbstractJazzOMRBusinessImpl<EventVO> implements EventBusiness {

	private static final long serialVersionUID = -4018418907098545031L;

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private HibernateTemplate hibernateTemplate;

	
	@Override
	public List<EventVO> findEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom,
			Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO, Boolean hasParts, String... fetchProfile) {
		return eventDAO.findEventVO(description, dtFimFrom, dtFimTo, dtInicioFrom, dtInicioTo, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO, hasParts, fetchProfile);
	}
	
	/**
	 * 
	 */
	@Override
	public void includeUpdateValidations(EventVO eventVO) throws JazzBusinessException {

		/*
		if(!isNew(eventVO)){
			String queryString = 
					" select 			eve.PK" +
					" from 				EventVO eve " +
					" inner join 	eve.participations par " +
					" inner join 	par.examVariantVO exv " +
					" inner join 	exv.examVO exa " +
					" where exa = :examVO ";
			
			List<Long> eventPKs = hibernateTemplate.findByNamedParam(queryString, "examVO", eventVO);
		
			if(CollectionUtils.isNotEmpty(eventPKs)){
				throw new JazzOMRBusinessConsistencyException("Este exame não pode ser alterado ou excluído porque está sendo utilizado pelos eventos a seguir "+eventPKs+"!");
			}
		}
		*/
		
		super.includeUpdateValidations(eventVO);
	}
	
	
	/**
	 * 
	 */
	@Override
	public void deleteValidations(EventVO eventVO) throws JazzBusinessException {
		this.includeUpdateValidations(eventVO);
		super.deleteValidations(eventVO);
	}
	

	@Override
	public List<Integer> fetchPaginas(final CriterionCoordinateVO criterionCoordinateVO) {

		HibernateCallback<List<Integer>> hibernateCallback = new HibernateCallback<List<Integer>>() {
			@Override
			public List<Integer> doInHibernate(Session session) throws HibernateException, SQLException {
				
				
				String nativeQuery = " select paginas as pagina from tj_criterion_coordinate_pages where fk_criterioncoordinate = :criCoordPK order by paginas";						
				
				SQLQuery sqlQuery = session.createSQLQuery(nativeQuery);
				
				sqlQuery.setLong("criCoordPK", criterionCoordinateVO.getPK());

				sqlQuery.addScalar("pagina");
				
				List<Integer> resultados = sqlQuery.list();

				return resultados;				
				
			}
		};

		List<Integer> paginas = hibernateTemplate.executeFind(hibernateCallback);
		
		return paginas;
	}
	
	
	@Override
	public List<RelatorioVO> findDistinctReports(EventVO eventVO) {
		
		@SuppressWarnings("unchecked")
		List<RelatorioVO> relatoriosVOs = hibernateTemplate.findByNamedParam(
				" select distinct rpt " +
				"  from EventVO 									eve " +
				"  left join eve.participations 	par " +
				"  left join par.examVariantVO 		exv " +
				"  left join exv.examVO 					exa " +
				"  left join exa.relatorioVO 		rpt " +
				"	where " + 
				"		eve = :eventVO ",		new String[] { "eventVO" }, new Object[] { eventVO });

		
		return relatoriosVOs;
	}


	
	@Override
	public List<EventVO> findEventResults(final EventVO eventVO) {
		
		@SuppressWarnings("unchecked")
		
		List<EventVO> eventosComResultados = hibernateTemplate.executeFind(new HibernateCallback<List<EventVO>>() {
			
			@Override
			public List<EventVO> doInHibernate(Session session) throws HibernateException, SQLException {
								
				String nativeQuery="" +
				" select  "+ 
				" {eve.*}, "+
				" {cre.*}, "+
				" {par.*}, "+
				" {exv.*}, "+
				" {exa.*}, "+
				" {que.*}, "+
				" {cco.*}, "+
				" {abc.*}, "+
				" {pay.*}, "+
				" {pes.*} "+
				"  "+
				" from "+ 
				" tb_event eve "+  
				" inner join  "+
				" 	tb_participation par  on eve.pk=par.fk_event "+  
				" inner join  "+
				" 	tb_pessoa pes  on pes.pk=par.fk_pessoa "+  
				" inner join  "+
				" 	tb_exam_variant exv  	 on par.fk_examvariant=exv.pk "+  
				" inner join  "+
				" 	tb_exam exa   on exv.fk_exam=exa.pk "+  
				" inner join  "+
				" 	tb_criterion_coordinate cco   on exv.pk=cco.fk_exam_variant "+  
				" inner join  "+
				" 	tb_abstract_criterion abc   on cco.fk_abstractcriterion=abc.pk "+  
				" inner join  "+
				" 	tb_question que   on cco.fk_question=que.pk "+
				
				" left outer join  "+
				" 	tb_criterion_result cre 	on "+  
				" 		cre.fk_participation=par.pk and "+  
				"			cre.fk_criterion_coordinate = cco.pk "+ 
				"  "+
				" left outer join "+
				" 	tb_payload pay on pay.pk = (select max(pk) from tb_payload payl where  payl.fk_participation = par.pk and payl.page = cco.pagina ) "+        
				"  "+
				" where "+ 
				" eve.pk 			=:eventPk " +
				" order by exv.pk, par.pk, cco.question_order, abc.crit_type, cco.alternative_order, abc.pk " +
				" ";				
				
				
				
				
				SQLQuery sqlQuery = session.createSQLQuery(nativeQuery);
				
				
				sqlQuery.addEntity("eve",EventVO.class).
						addJoin("par", "eve.participations").
						addJoin("pes", "par.pessoaVO").
						addJoin("cre", "par.criterionResults").
						addJoin("exv", "par.examVariantVO").
						addJoin("exa", "exv.examVO").
						addJoin("cco", "cre.criterionCoordinateVO").
						addJoin("que", "cco.questionVO").
						addJoin("abc", "que.criterionVOs").
						/*addJoin("abc", "cco.abstractCriterionVO").*/
						addJoin("pay", "par.payloadVOs");
				
				sqlQuery.setParameter("eventPk",eventVO.getPK());
				
				
				List<Object[]> arrayObjs = (List<Object[]>) sqlQuery.list();
				
				Set<EventVO> eventos = new HashSet<EventVO>(200);
				
				for (Object[] object : arrayObjs) {
					EventVO eventVO = distinctEvent(session, object);
					eventos.add(eventVO);
				}
				
				return new ArrayList<EventVO>(eventos);
			}

			
			
			
			
			/**
			 * @param session
			 * @param object
			 * @return
			 */
			protected EventVO distinctEvent(Session session, Object[] object) {
				EventVO eventVO =  (EventVO) 																							object[0];
				ParticipationVO participationVO =  (ParticipationVO) 											object[1];
				ExamVariantVO examVariantVO =  (ExamVariantVO) 														object[4];
				ExamVO examVO =  (ExamVO) 																								object[5];
				ISortable questionVO =  (ISortable)																			object[7];
				PayloadVO payloadVO = (PayloadVO) 																				object[9];				
				
				session.evict(eventVO );
				session.evict(participationVO);
				session.evict(examVariantVO );
				session.evict(examVO );
				session.evict(questionVO);
				session.evict(payloadVO);
				
				AbstractHibernateDAO.distinctResults(participationVO,"criterionResults");
				AbstractHibernateDAO.distinctResults(participationVO,"payloadVOs");
				
				AbstractHibernateDAO.distinctResults(examVariantVO,"criterionCoordinates");
				AbstractHibernateDAO.distinctResults(questionVO,"criterionVOs");
				AbstractHibernateDAO.distinctResults(eventVO,"participations");
				
				
				List<ParticipationVO> parts = eventVO.getParticipations();
				for (ParticipationVO part : parts) {
					AbstractHibernateDAO.distinctResults(part,"criterionResults");
					//System.out.println(part.getCriterionResults());
				}
				
				return eventVO;
			}
			
		});
		
		return eventosComResultados;
	}
	
	
	@Override
	public boolean hasExamPDF(EventVO eventVO) {
		return eventDAO.hasExamPDF(eventVO);
	}

	@Override
	public PayloadVO findPayloadByPK(PayloadVO selPayloadVO, String... fetchProfile) {
		return eventDAO.findPayloadByPK(selPayloadVO, fetchProfile);

	}

	@Override
	public List<EventVO> findEvents(final List<EventVO> resultados,final ExtraArgumentsDTO extraArgumentsDTO) {

		
		@SuppressWarnings("unchecked")
		List<EventVO> eventos = hibernateTemplate.executeFind(new HibernateCallback<List<EventVO>>() {
				@Override
				public List<EventVO> doInHibernate(Session session) throws HibernateException, SQLException {
					String order = parseHQLOrder(extraArgumentsDTO);
					
					String eventPks = "0";
					for (EventVO eventVO : resultados) {
						eventPks=eventPks+","+eventVO.getPK();
					}
					
					String nativeQuery =

					"	select distinct "+
					"	{eveCt.*},   "+
					"	sum(altTotais) altTotais 	, /*alternativas totais 						28 */ "+  			
					"	sum(altResult) altResult 	, /*resultados totais de alternativas          	56 */ "+   		 			
					"	sum(disTotais) disTotais 	, /*dissertativas totais */  			 "+
					"	sum(disResult) disResult 	, /*resultados analizados de dissertativas*/ "+			
					"	sum(disResDisp) disResDisp 	/*resultados não analizados de dissertativas*/ "+ 		

					"	from ( "+

					"	select  			eve.*, "+ 			
					"	count(DISTINCT ccoAlt.pk) as altTotais, /*alternativas totais 						28 */ "+  			
					"	count(DISTINCT creAlt.pk) as altResult, /*resultados totais de alternativas          	56 */ "+   		 			
					"	count(DISTINCT ccoDiss.pk) as disTotais, /*dissertativas totais */  			 "+
					"	count(DISTINCT creDiss.pk) as disResult, /*resultados analizados de dissertativas*/ "+ 			
					"	count(DISTINCT creDissDisp.pk) as disResDisp /*resultados não analizados de dissertativas*/ "+ 		

					"	from tb_event eve "+ 			
					"	inner join tb_participation par on eve.pk = par.fk_event "+ 			
					"	inner join tb_exam_variant  exv on exv.pk = par.fk_examvariant "+ 					 			

					"	/*criterios alternativos*/ "+ 			
					"	left join tb_criterion_coordinate ccoAlt on "+ 		
					"		ccoAlt.fk_exam_variant = exv.pk and  				 "+
					"		ccoAlt.alternative_order is not null "+

					"	/*criterios dissertativos*/  "+ 			
					"	left join tb_criterion_coordinate ccoDiss on "+ 				
					"		ccoDiss.fk_exam_variant = exv.pk and  				 "+
					"		ccoDiss.alternative_order is null 			 "+

					"/*resultados alternativas */ "+ 			
					"	left join tb_criterion_result creAlt on "+ 				
					"	  creAlt.fk_participation = par.pk and 		 "+		
					"		creAlt.fk_criterion_coordinate = ccoAlt.pk  "+			
					
					"	/*resultados dissertativas */ 			 "+
					"	left join tb_criterion_result creDiss on "+ 				
					"		creDiss.fk_participation = par.pk and 		 "+		
					"		creDiss.fk_criterion_coordinate = ccoDiss.pk and "+ 				
					"		creDiss.pontuacao is not null 			 "+
					
					"	/*resultados dissertativas disponiveis */ "+ 			
					"	left join tb_criterion_result creDissDisp on "+ 					
					"		creDissDisp.fk_participation = par.pk and    "+
					"		creDissDisp.fk_criterion_coordinate = ccoDiss.pk and "+ 					
					"		creDissDisp.pontuacao is null		" +

					"		 and 	" +
					"			exists (	/* testa se a imagem da resposta foi carregada no sistema */ " +
					"				select 1 from tj_criterion_coordinate_pages tjp_b	" +
					"				inner join tb_criterion_coordinate cco_b on cco_b.pk = tjp_b.fk_criterioncoordinate	" +
					"				inner join tb_payload pay_b on pay_b.page = tjp_b.paginas	" +
					"				where 	cco_b.pk 				= ccoDiss.pk and	" +
					"						pay_b.fk_participation 	= par.pk	" +
					"			)						" +
					
					"	where eve.pk in ("+eventPks+")  "+
					"	group by eve.pk , par.pk "+
					" "+
					"		) eveCt group by eveCt.pk ";						
						
						
					
					SQLQuery sqlQuery = session.createSQLQuery(nativeQuery);
					sqlQuery.addEntity("eveCt",EventVO.class);

					sqlQuery.addScalar("altTotais");
					sqlQuery.addScalar("altResult");
					sqlQuery.addScalar("disTotais");
					sqlQuery.addScalar("disResult");
					sqlQuery.addScalar("disResDisp");
					
					
					List<Object[]> resultados = sqlQuery.list();

					List<EventVO> eventos = new ArrayList<EventVO>(resultados.size());					
					
					for (Object[] arrayObj : resultados) {
						
						EventVO eventVO = (EventVO) arrayObj[0];
						
						double altTotais= ((BigDecimal)arrayObj[1]).doubleValue();
						double altResult= ((BigDecimal)arrayObj[2]).doubleValue();
						double disTotais= ((BigDecimal)arrayObj[3]).doubleValue();
						double disResult= ((BigDecimal)arrayObj[4]).doubleValue();
						double disResDisp= ((BigDecimal)arrayObj[5]).doubleValue();
						
						eventVO.setAltResult(altResult);
						eventVO.setAltTotais(altTotais);
						eventVO.setDisResult(disResult);
						eventVO.setDisTotais(disTotais);
						eventVO.setDisResDisp(disResDisp);
						
						eventos.add(eventVO);
						
					}
					
					return eventos;
				}
		});
		
		

		return eventos;
	}
	
	
	
	@Override
	public CriterionResultVO saveOrUpdate(CriterionResultVO criterionResultVO) {
		
		hibernateTemplate.saveOrUpdate(criterionResultVO);
		
		return criterionResultVO;
	}
	
	
	/**
	 * Monta lista de detalhes de questoes da participacao escolhida
	 */
	@Override
	public List<CriterionDetailDTO> findCriterionDetails(final EventVO eventVO,  final ExtraArgumentsDTO extraArgumentsDTO) {
		
		if(eventVO==null){
			return new ArrayList<CriterionDetailDTO>();
		}
		
		
		@SuppressWarnings("unchecked")
		List<CriterionDetailDTO> criterions = hibernateTemplate.executeFind(new HibernateCallback<List<CriterionDetailDTO>>(){
			@Override
			public List<CriterionDetailDTO> doInHibernate(Session session) throws HibernateException, SQLException {
				final String hqlOrder = parseHQLOrder(extraArgumentsDTO);
				
				String strNativeQuery = 
				" select distinct "+
				"             {cre.*}, "+
				"             {eve.*}, "+
				"             {par.*}, "+
				"             {exv.*}, "+
				"             {exa.*}, "+
				"             {que.*}, "+
				"             {cco.*}, "+
				"             {abc.*} "+
				"         from "+
				"             tb_event eve  "+
				"         inner join "+
				"             tb_participation par  "+
				"                 on eve.pk=par.fk_event  "+
				"         inner join "+
				"             tb_pessoa pes  "+
				"                 on pes.pk=par.fk_pessoa  "+
				"         inner join "+
				"             tb_exam_variant exv  "+
				"                 on par.fk_examvariant=exv.pk  "+
				"         inner join "+
				"             tb_exam exa  "+
				"                 on exv.fk_exam=exa.pk  "+
				"         inner join "+
				"             tb_criterion_coordinate cco  "+
				"                 on exv.pk=cco.fk_exam_variant  "+
				
				" 				inner join 	/* limita a exibicao para apenas os criterions que ja possuem imagem carregada. nao e retornado */	" +
				"							tj_criterion_coordinate_pages ccp	" +
				"								on ccp.fk_criterioncoordinate = cco.pk	" +
				
				" 				inner join 	/* limita a exibicao para apenas os criterions que ja possuem imagem carregada. nao e retornado */	" +
				"							tb_payload pay " +
				"								on pay.fk_participation = par.pk and pay.page = ccp.paginas	" +
				
				"         inner join  "+
				"             tb_abstract_criterion abc  "+
				"                 on cco.fk_abstractcriterion=abc.pk  "+
				"         inner join "+
				"             tb_question que  "+
				"                 on cco.fk_question=que.pk  "+
				
				
				"         left outer join "+
				"             tb_criterion_result cre 	on " +
				"										cre.fk_participation=par.pk and " +
				"										cre.fk_criterion_coordinate = cco.pk  "+
				"         where "+
				"             eve.pk 		in ("+eventVO.getPK()+") and "+ 
				"             abc.crit_type	= 'D' "+
				"         " + hqlOrder;
				
				SQLQuery nativeQuery = session.createSQLQuery(strNativeQuery);
				
				nativeQuery.addEntity("cre",CriterionResultVO.class);
				nativeQuery.addEntity("eve",EventVO.class);
				nativeQuery.addEntity("par",ParticipationVO.class);
				nativeQuery.addEntity("exv",ExamVariantVO.class);
				nativeQuery.addEntity("exa",ExamVO.class);
				nativeQuery.addEntity("que",QuestionVO.class);
				nativeQuery.addEntity("cco",CriterionCoordinateVO.class);
				nativeQuery.addEntity("abc",AbstractCriterionVO.class);
				//nativeQuery.addEntity("pay",PayloadVO.class); retirado join com payload. sera carregado sob demanda
				
				applyPageSettings(extraArgumentsDTO, nativeQuery);

				if(extraArgumentsDTO!=null && extraArgumentsDTO.getMaxResults()!=null){
					nativeQuery.setFirstResult(extraArgumentsDTO.getFirstResult());
				}
				
				if(extraArgumentsDTO!=null && extraArgumentsDTO.getMaxResults()!=null){
					nativeQuery.setMaxResults(extraArgumentsDTO.getMaxResults());
				} 
				
				
				List<Object[]> criterions = nativeQuery.list();

				List<CriterionDetailDTO > criterionDetailDTOs = new ArrayList<CriterionDetailDTO>(criterions.size());
				
				for (Object[] object : criterions) {
					
					CriterionResultVO criterionResultVO =  (CriterionResultVO) 								object[0];
					EventVO eventVO =  (EventVO) 																							object[1];
					ParticipationVO participationVO =  (ParticipationVO) 											object[2];
					ExamVariantVO examVariantVO =  (ExamVariantVO) 														object[3];
					ExamVO examVO =  (ExamVO) 																								object[4];
					QuestionVO questionVO =  (QuestionVO)																			object[5];
					CriterionCoordinateVO criterionCoordinateVO =  (CriterionCoordinateVO) 		object[6];
					AbstractCriterionVO	abstractCriterionVO =  (AbstractCriterionVO) 					object[7];
					//PayloadVO payloadVO = (PayloadVO) 																				object[8]; retirado join com payload será carregado sob demanda
					
					AbstractHibernateDAO.distinctResults(participationVO, "criterionResults");
					
					CriterionDetailDTO criterionDetailDTO = new CriterionDetailDTO(eventVO, participationVO, examVariantVO, examVO, questionVO, criterionCoordinateVO, abstractCriterionVO, criterionResultVO);
					criterionDetailDTOs.add(criterionDetailDTO);
					
				}
				
				

				return criterionDetailDTOs;
			}

			
			
			
			/**
			 * @param extraArgumentsDTO
			 * @param query
			 */
			protected void applyPageSettings(final ExtraArgumentsDTO extraArgumentsDTO, Query query) {
				if(extraArgumentsDTO.getFirstResult()!=null)
					query.setFirstResult(extraArgumentsDTO.getFirstResult());
				
				if(extraArgumentsDTO.getMaxResults()!=null)
					query.setMaxResults(extraArgumentsDTO.getMaxResults());
			}
			
		} );
		
		return criterions;
	}
	
	
	@Override
	public CriterionResultVO findQuestionResult(CriterionResultVO criterionResultVO) {
		Class<CriterionResultVO> voClass = CriterionResultVO.class;
		
		DetachedCriteria criteria = createDetachedCriteria(voClass);
		
		criteria.add(Restrictions.eq("PK", criterionResultVO.getPK()));
	
		@SuppressWarnings("unchecked")
		List<CriterionResultVO> beans = hibernateTemplate.findByCriteria(criteria);
		
		CriterionResultVO beanReturn = null;
		if(beans.size()>0){
			beanReturn = beans.get(0);
		}
		
		return beanReturn;		
	}


	@Override
	public void saveQuestionResultVO(CriterionResultVO criterionResultVO) {
		hibernateTemplate.saveOrUpdate(criterionResultVO);
	}
	
	
	/**
	 * Monta lista de detalhes de questoes da participacao escolhida
	 */
	@Override
	public Long countCriterionDetails(final EventVO eventVO) {
		
		if(eventVO==null){
			return 0l;
		}
		
		
		@SuppressWarnings("unchecked")
		Long count = hibernateTemplate.execute(new HibernateCallback<Long>(){
			
			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				
				String strNativeQuery = 
					"          select count(1)  "+
					"                  from  "+
					"                      tb_event eve   "+
					"                  inner join  "+
					"                      tb_participation par   "+
					"                          on eve.pk=par.fk_event   "+
					"                  inner join  "+
					"                      tb_exam_variant exv   "+
					"                          on par.fk_examvariant=exv.pk   "+
					"                  inner join  "+
					"                      tb_exam exa   "+
					"                          on exv.fk_exam=exa.pk   "+
					"                  inner join  "+
					"                      tb_criterion_coordinate cco   "+
					"                          on cco.fk_exam_variant=exv.pk "+
					"                  inner join  "+
					"                      tb_abstract_criterion abc   "+
					"                          on cco.fk_abstractcriterion=abc.pk   "+
					"                  inner join  "+
					"                      tb_question que   "+
					"                          on cco.fk_question=que.pk   "+
					"                  inner join  "+
					"                      tb_criterion_result cre 	on par.pk=cre.fk_participation 	and cre.fk_criterion_coordinate = cco.pk  "+
					"                  where  "+
					"                      eve.pk 		in ("+eventVO.getPK()+") and   "+
					"                      abc.crit_type	= 'D' ";
				
				SQLQuery nativeQuery = session.createSQLQuery(strNativeQuery);
				
				
				BigInteger obj = (BigInteger) nativeQuery.uniqueResult();
				
				
				return obj.longValue();
			}
			
			
			
			
		});
		
		return count;
		
	}	
	

	/**
	 * @param extraArgumentsDTO
	 * @return
	 */
	protected String parseHQLOrder(ExtraArgumentsDTO extraArgumentsDTO) {

		
		if(extraArgumentsDTO==null){
			return "";
		}
		
		Map<String, Boolean> orderMap2 = extraArgumentsDTO.getOrderMap();

		if(orderMap2 == null || orderMap2.isEmpty()){
			return "";
		}
		
		
		
		StringBuffer orderHQL = new StringBuffer();
		Map<String, Boolean> orderMap = orderMap2;
		Set<String> keys = orderMap.keySet();

		String virgula=" order by ";
		for (String key : keys) {
			
			
			String ascDesc = orderMap.get(key)?" asc ":" desc ";
			
			orderHQL.append(" " + virgula + " " + key + " " + ascDesc);
			
			virgula=",";
		} 
		
		String hqlOrder = orderHQL.toString();
		return hqlOrder;
	}
	


	/**
	 * Complementa com o VO de questionnario
	 * 
	 * @param questions
	 */
	protected void complementQuestionnaireVO(List<CriterionDetailDTO> questions) {

		for (final CriterionDetailDTO criterionDetailDTO : questions) {

			QuestionnaireVO questionnaireVO = hibernateTemplate.execute(new HibernateCallback<QuestionnaireVO>() {
				@Override
				public QuestionnaireVO doInHibernate(Session session) throws HibernateException, SQLException {

					String nativeQuery = 
							" select {questionnaire.*} from tj_examvo_questionnaires eq " + 
							" 	inner join tj_questionnairevo_questions qq on "							+ 
							" 		qq.fk_questionnaire = eq.fk_questionnaires_questionnaire " + 
							" 	inner join tb_questionnaire questionnaire  on " + 
							" 		questionnaire.pk = qq.fk_questionnaire "							+ 
							" where  " + 
							" 	eq.fk_exam = :exam and " + 
							" 	qq.fk_questions_question = :question";

					SQLQuery sqlQuery = session.createSQLQuery(nativeQuery);

					sqlQuery.addEntity("questionnaire", QuestionnaireVO.class);
					sqlQuery.setParameter("exam", criterionDetailDTO.getExamVO().getPK());
					sqlQuery.setParameter("question", criterionDetailDTO.getQuestionVO().getPK());

					List<QuestionnaireVO> questionnaires = sqlQuery.list();

					if (CollectionUtils.isNotEmpty(questionnaires)) {
						return questionnaires.get(0);
					} else {
						return null;
					}
				}
			});

			criterionDetailDTO.setQuestionnaireVO(questionnaireVO);

		}
	}

	/**
	 * @param participationVO
	 * @return
	 */
	protected List<CriterionDetailDTO> findResults(ParticipationVO participationVO) {
		/*
		 * private QuestionResultVO questionResultVO; private PayloadVO payloadVO; private QuestionnaireVO questionnaireVO;
		 */

		@SuppressWarnings("unchecked")
		List<CriterionDetailDTO> questionResults = hibernateTemplate.findByNamedParam(
				" select distinct new br.com.dlp.jazzomr.results.CriterionDetailDTO" + 
				"		( qre) 					"	+ 
				"  from ParticipationVO par " + 
				"  inner join  par.questionResults qre " + 
				"  inner join  qre.criterionResultVOs cre " +
				"	where " + 
				"		par.PK in (:participationPK) " +
				"   ", 
				new String[] { "participationPK" }, new Object[] { participationVO.getPK() });
		return questionResults;
	}

	/**
	 * @param questionResults1
	 * @return
	 */
	protected Map<QuestionVO, CriterionDetailDTO> mapQuestionResults(List<CriterionDetailDTO> questionResults1) {
		Map<QuestionVO, CriterionDetailDTO> mapResults = new HashMap<QuestionVO, CriterionDetailDTO>(questionResults1.size());
		for (CriterionDetailDTO criterionDetailDTO : questionResults1) {
			mapResults.put(criterionDetailDTO.getQuestionVO(), criterionDetailDTO);
		}
		return mapResults;
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
		return eventDAO.findEventVO(description, dtFimFrom, dtFimTo, dtInicioFrom, dtInicioTo, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
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
	@WebMethod
	public Long countEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo,
			StatusEnum status) {
		return eventDAO.countEventVO(description, dtFimFrom, dtFimTo, dtInicioFrom, dtInicioTo, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status);
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
	@Override
	@WebMethod
	public Long countEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom, Date dtAltTo,
			StatusEnum status, Boolean hasParts) {
		return eventDAO.countEventVO(description, dtFimFrom, dtFimTo, dtInicioFrom, dtInicioTo, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status,hasParts);
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
	 * @param extraArgumentsDTO
	 *          Paginacao e Ordenação de pesquisa
	 * @returns Coleção de EventVO
	 */
	public List<EventVO> findEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom,
			Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO, String... fetchProfile) {
		return eventDAO.findEventVO(description, dtFimFrom, dtFimTo, dtInicioFrom, dtInicioTo, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO, fetchProfile);
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
	 * @param extraArgumentsDTO
	 *          Paginacao e Ordenação de pesquisa
	 * @returns Coleção de EventVO
	 */
	public List<EventVO> findEventVO(String description, Date dtFimFrom, Date dtFimTo, Date dtInicioFrom, Date dtInicioTo, Date dtIncFrom, Date dtIncTo, Date dtAltFrom,
			Date dtAltTo, StatusEnum status, ExtraArgumentsDTO extraArgumentsDTO) {
		return findEventVO(description, dtFimFrom, dtFimTo, dtInicioFrom, dtInicioTo, dtIncFrom, dtIncTo, dtAltFrom, dtAltTo, status, extraArgumentsDTO, (String) null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.dlp.jazzomr.event.EventBusiness#findPayloadVO(br.com.dlp.framework.dao.ExtraArgumentsDTO, java.lang.String[])
	 */
	@Override
	public List<PayloadVO> findPayloadVO(ExtraArgumentsDTO extraArgumentsDTO, String... fetchProfile) {

		// TODO Auto-generated method stub
		return eventDAO.findPayloadVO(extraArgumentsDTO, fetchProfile);
	}

	@Override
	public Long countNotProcessedPayloadVO() {
		return eventDAO.countNotProcessedPayloadVO();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#getDao()
	 */
	@Override
	protected IDAO<EventVO> getDao() {
		return eventDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.dlp.framework.business.AbstractBusinessImpl#newVO()
	 */
	@Override
	public EventVO newVO() {
		return new EventVO();
	}
	


	
	
	/**
	 * Carrega o payload da participação/página especificados
	 */
	@Override
	public PayloadVO findPayloadVO(Long participationPK, Integer pagina) {

		EmpresaVO empresaVO = getEmpresaUsuarioLogado();
		
		@SuppressWarnings("unchecked")
		List<PayloadVO> payloads = hibernateTemplate.findByNamedParam(
				" select distinct pay " +
				"  	from PayloadVO pay " +
				" inner join fetch pay.imageVOs img " +
				"	where " +
				"		pay.participationVO.PK 	= :participationPK and " +
				"		pay.page 								= :pagina and " +
				"   pay.empresaVO					 	= :empresaVO" +
				"",
		new String[] { "participationPK","pagina","empresaVO"}, 
		new Object[] {  participationPK , pagina , empresaVO});
		

		if(CollectionUtils.isNotEmpty(payloads)){
			return payloads.get(0);
			
		}else{
			return null;
		}
		
		
	}
	

}