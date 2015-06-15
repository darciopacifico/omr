/**
 * Gerado por template generation
 * Diretiva de overwrite atual:
 * TemplateName:
 **/
package br.com.dlp.jazzomr.event;

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
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.dao.AbstractHibernateDAO;
import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.dao.JazzDetachedCriteria;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.vo.ISortable;
import br.com.dlp.jazzomr.application.result.CriterionResultVO;
import br.com.dlp.jazzomr.base.AbstractJazzOMRBusinessImpl;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.exam.ExamVariantVO;
import br.com.dlp.jazzomr.exam.QuestionnaireVO;
import br.com.dlp.jazzomr.exam.coordinate.CriterionCoordinateVO;
import br.com.dlp.jazzomr.person.PessoaVO;
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
@WebService
//@Transactional(propagation=Propagation.REQUIRED)
public class EventBusinessImpl extends AbstractJazzOMRBusinessImpl<EventVO> implements EventBusiness {

	private static final long serialVersionUID = -4018418907098545031L;

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private HibernateTemplate hibernateTemplate;

	
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
					"		select  "+
					"			{eve.*}, "+ 
					"			count(distinct ccoAlt.pk) as altTotais, /*alternativas totais*/  "+ 
					"			count(distinct creAlt.pk) as altResult, /*resultados totais de alternativas*/   "+
					"		 "+
					"			count(distinct ccoDiss.pk) as disTotais, /*dissertativas totais */  "+
					"			count(distinct creDiss.pk) as disResult, /*resultados analizados de dissertativas*/ "+
					"			count(distinct creDissDisp.pk) as disResDisp /*resultados não analizados de dissertativas*/ "+
					"		from tb_event eve "+ 
					"			inner join tb_participation par on eve.pk = par.fk_event "+
					"			inner join tb_exam_variant  exv on exv.pk = par.fk_examvariant "+
					"					 "+
						
					"			/*criterios alternativos*/ "+
					"			left join tb_criterion_coordinate ccoAlt on "+ 
					"				ccoAlt.fk_exam_variant = exv.pk and  "+
					"				ccoAlt.alternative_order is not null "+

					"			/*criterios dissertativos*/ "+
					"			left join tb_criterion_coordinate ccoDiss on "+ 
					"				ccoDiss.fk_exam_variant = exv.pk and  "+
					"				ccoDiss.alternative_order is null "+

						
					"			/*resultados alternativas */ "+
					"			left join tb_criterion_result creAlt on "+ 
					"				creAlt.fk_participation = par.pk and "+
					"				creAlt.fk_criterion_coordinate = ccoAlt.pk "+ 
					 
					"			/*resultados dissertativas */ "+
					"			left join tb_criterion_result creDiss on "+ 
					"				creDiss.fk_participation = par.pk and "+
					"				creDiss.fk_criterion_coordinate = ccoDiss.pk and "+
					"				creDiss.pontuacao is not null "+
					

					"			/*resultados dissertativas disponiveis */ "+
					"			left join tb_criterion_result creDissDisp on "+ 
					"					creDissDisp.fk_participation = par.pk and "+
					"					creDissDisp.fk_criterion_coordinate = ccoDiss.pk and "+
					"					creDissDisp.pontuacao is null					 "+
					
					"		where  "+
					"			eve.pk in ("+eventPks+") "+
					"			group by eve.pk "+order;

					
					SQLQuery sqlQuery = session.createSQLQuery(nativeQuery);
					sqlQuery.addEntity("eve",EventVO.class);

					sqlQuery.addScalar("altTotais");
					sqlQuery.addScalar("altResult");
					sqlQuery.addScalar("disTotais");
					sqlQuery.addScalar("disResult");
					sqlQuery.addScalar("disResDisp");
					
					
					List<Object[]> resultados = sqlQuery.list();

					List<EventVO> eventos = new ArrayList<EventVO>(resultados.size());					
					
					for (Object[] arrayObj : resultados) {
						
						EventVO eventVO = (EventVO) arrayObj[0];
						
						double altTotais= ((BigInteger)arrayObj[1]).doubleValue();
						double altResult= ((BigInteger)arrayObj[2]).doubleValue();
						double disTotais= ((BigInteger)arrayObj[3]).doubleValue();
						double disResult= ((BigInteger)arrayObj[4]).doubleValue();
						double disResDisp= ((BigInteger)arrayObj[5]).doubleValue();
						
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
	 * Complementa particicipacoes
	 */
	@Override
	public ParticipationVO findParticipation(ParticipationVO selParticipationVO, String... fetchProfile) {
		
		@SuppressWarnings("unchecked")
		List<ParticipationVO> participationVOs = hibernateTemplate.findByNamedParam(
				" select distinct par " +
				"  from ParticipationVO par " +
				"  left join par.payloadVOs pay " +
				"  left join fetch pay.imageVOs " +
				"	where " + 
				"		par = :participation ",
		new String[] { "participation" }, new Object[] { selParticipationVO });

		
		ParticipationVO participationVO=null;
		if( CollectionUtils.isNotEmpty(participationVOs)){
			participationVO = participationVOs.get(0);
		}
		
		
		return participationVO;
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
				" select "+
				"             {cre.*}, "+
				"             {eve.*}, "+
				"             {par.*}, "+
				"             {exv.*}, "+
				"             {exa.*}, "+
				"             {que.*}, "+
				"             {cco.*}, "+
				"             {abc.*}, "+
				"             {pay.*} "+
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
				"         inner join "+
				"             tb_abstract_criterion abc  "+
				"                 on cco.fk_abstractcriterion=abc.pk  "+
				"         inner join "+
				"             tb_question que  "+
				"                 on cco.fk_question=que.pk  "+
				
				"         inner join "+ 
				"         		tb_payload pay on  "+
				"         			pay.fk_participation = par.pk and  "+
				"         			pay.page = cco.pagina  and	"+
				"								pay.pk = (select min(payMin.pk) from tb_payload payMin where payMin.fk_participation = par.pk and payMin.page = cco.pagina ) "+
				
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
				nativeQuery.addEntity("pay",PayloadVO.class);
				
				applyPageSettings(extraArgumentsDTO, nativeQuery);

				if(extraArgumentsDTO!=null && extraArgumentsDTO.getMaxResults()!=null){
					nativeQuery.setFirstResult(extraArgumentsDTO.getFirstResult());
				}
				
				if(extraArgumentsDTO!=null && extraArgumentsDTO.getMaxResults()!=null){
					nativeQuery.setMaxResults(extraArgumentsDTO.getMaxResults());
				} 
				
				
				List<Object[]> criterions = nativeQuery.list();
				
				List<CriterionDetailDTO> criterionDetailDTOs = new ArrayList<CriterionDetailDTO>(criterions.size());
				
				for (Object[] object : criterions) {
					
					CriterionResultVO criterionResultVO =  (CriterionResultVO) 								object[0];
					EventVO eventVO =  (EventVO) 																							object[1];
					ParticipationVO participationVO =  (ParticipationVO) 											object[2];
					ExamVariantVO examVariantVO =  (ExamVariantVO) 														object[3];
					ExamVO examVO =  (ExamVO) 																								object[4];
					QuestionVO questionVO =  (QuestionVO)																			object[5];
					CriterionCoordinateVO criterionCoordinateVO =  (CriterionCoordinateVO) 		object[6];
					AbstractCriterionVO	abstractCriterionVO =  (AbstractCriterionVO) 					object[7];
					PayloadVO payloadVO = (PayloadVO) 																				object[8];
					
					CriterionDetailDTO criterionDetailDTO = new CriterionDetailDTO(eventVO, participationVO, examVariantVO, examVO, questionVO, criterionCoordinateVO, abstractCriterionVO, criterionResultVO,payloadVO);
					
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
		JazzDetachedCriteria criteria = JazzDetachedCriteria.forClass(CriterionResultVO.class);
		
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
		StringBuffer orderHQL = new StringBuffer();

		Map<String, Boolean> orderMap = extraArgumentsDTO.getOrderMap();
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
	
	@Override
	public List<PayloadVO> getPaginasFaltantes(CriterionDetailDTO criterionDetailDTO, Integer pagina) {
		
		ParticipationVO par = criterionDetailDTO.getParticipationVO();

		Set<Integer> paginasFaltantes = new HashSet<Integer>();
		paginasFaltantes.add(pagina);
		
		@SuppressWarnings("unchecked")
		List<PayloadVO> payloads = hibernateTemplate.findByNamedParam(
				" select distinct pay " +
				"  from PayloadVO pay " +
				" inner join fetch pay.imageVOs " +
				"	where " +
				"		pay.participationVO = :par and " +
				"   pay.page in (:paginasFaltantes)  ",
		new String[] { "par", "paginasFaltantes"}, 
		new Object[] {  par,   paginasFaltantes});
		
		return payloads;
		
	}

	@Override
	public PayloadVO fetchPayload(PayloadVO payloadVO) {
		
		if(payloadVO==null){
			return null;
		}
		
		@SuppressWarnings("unchecked")
		List<PayloadVO> payloads = hibernateTemplate.findByNamedParam(
				" select distinct pay " +
				"  	from PayloadVO pay " +
				" inner join fetch pay.imageVOs " +
				"	where " +
				"		pay.PK = :payloadPK  ",
		new String[] { "payloadPK"}, 
		new Object[] {  payloadVO.getPK()});
		
		
		if(!CollectionUtils.isEmpty(payloads)){
			return payloads.get(0);
			
		}else{
			throw new JazzRuntimeException("Nao foi possivel completar o payload informado");
		}
		
	}

	/**
	 * Complementa os DTOs de resultados com os payloads de imagens
	 * @param participationVO 
	 * @param questions
	 */
	@Override
	public ParticipationVO complementPayloadsVO(ParticipationVO participationVO) {
	
		@SuppressWarnings("unchecked")
		List<PayloadVO> payloads = hibernateTemplate.findByNamedParam(
				" select pay " +
				"  from PayloadVO pay " +
				"  inner join fetch pay.imageVOs " +
				"	where " + 
				"		pay.participationVO = :participation ",
		new String[] { "participation" }, new Object[] { participationVO });
		
		participationVO.setPayloadVOs(payloads);
		
		return participationVO;
		
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

}