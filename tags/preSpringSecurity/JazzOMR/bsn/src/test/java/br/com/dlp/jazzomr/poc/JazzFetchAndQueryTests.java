package br.com.dlp.jazzomr.poc;

import ij.measure.ResultsTable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctResultTransformer;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.dao.JazzDetachedCriteria;
import br.com.dlp.framework.gof.strategy.Strategy;
import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.dlp.jazzomr.application.result.CriterionResultVO;
import br.com.dlp.jazzomr.event.EventBusiness;
import br.com.dlp.jazzomr.event.EventDAO;
import br.com.dlp.jazzomr.event.EventVO;
import br.com.dlp.jazzomr.event.ParticipationVO;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.exam.ExamVariantVO;
import br.com.dlp.jazzomr.exam.QuestionnaireBusiness;
import br.com.dlp.jazzomr.exam.QuestionnaireVO;
import br.com.dlp.jazzomr.exceptions.JazzOMRException;
import br.com.dlp.jazzomr.exceptions.RefParticleNotFoundException;
import br.com.dlp.jazzomr.omr.ImageDocVO;
import br.com.dlp.jazzomr.question.AbstractCriterionVO;
import br.com.dlp.jazzomr.question.QuestionVO;
import br.com.dlp.jazzomr.results.CriterionDetailDTO;
import br.com.dlp.jazzomr.results.ImageVO;
import br.com.dlp.jazzomr.results.PayloadVO;

@ContextConfiguration(locations = "/ApplicationContext_testng.xml")
@Test
public class JazzFetchAndQueryTests extends AbstractTestNGSpringContextTests {
	public static Logger log = LoggerFactory.getLogger(JazzFetchAndQueryTests.class);
	
	
	@Autowired
	private EventDAO eventDAO;
	
	@Autowired
	private EventBusiness eventBusiness;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Autowired
	private JazzOMRImageParser jazzOMRImageParser;

	@Autowired
	@Qualifier("qrCodeReaderDirect")
	private Strategy<BufferedImage, String> qrCodeReader;

	@Autowired
	private QuestionnaireBusiness questionnaireBusiness;
	
	
	@Test
	public void testImgQuestion(){
		
		QuestionVO questionVO = new QuestionVO();
		questionVO.setPK(10l);
		
		QuestionVO questionVO2 = questionnaireBusiness.complementBean(questionVO,"questao_com_imagens");
		
		System.out.println(questionVO2.getDescription());
		
		List<ImageVO> images = questionVO2.getImageVOs();
		
		for (ImageVO imageVO : images) {
			System.out.println(imageVO.getImage());
		}
		
	} 
	
	
	@Test
	public void testEventosComResultados(){
		
		EventVO eventVO = new EventVO();
		eventVO.setPK(4l);
		
		List<EventVO> eventos = eventBusiness.findEventResults(eventVO);
		
		
		for (EventVO eventVORes : eventos) {
			
			System.out.println(eventVORes);
			
			List<ParticipationVO> parts = eventVORes.getParticipations();
			
			for (ParticipationVO participationVO : parts) {
				
				if(participationVO!=null){
					
					
				System.out.println(participationVO.getPK()+" - "+participationVO.getPessoaVO().getNome());
				
				List<CriterionResultVO> results = participationVO.getCriterionResults();
				
				for (CriterionResultVO criterionResultVO : results) {
					
					if(criterionResultVO!=null){
					
						System.out.println("pontos:"+criterionResultVO.getChecked());
					
					}
				}
				}
				
			}
			
		}
		
	}
	
	
	
	@Test
	public void testEventCounting(){
		
		List<EventVO> resultados = new ArrayList<EventVO>();
		EventVO event = new EventVO();
		event.setPK(4l);
		resultados.add(event );
		
		ExtraArgumentsDTO extraArgumentsDTO = new ExtraArgumentsDTO();
		
		List<EventVO> eventos = eventBusiness.findEvents(resultados, extraArgumentsDTO);
		
		for (EventVO eventVO : eventos) {
			System.out.println(eventVO);
		}
		
		System.out.println("totais de eventos: "+eventos.size());;
		
	}
	
	
	
	@Test
	public void testFetchParticipationPayload(){
		
		ParticipationVO participationVO = new ParticipationVO();
		participationVO.setPK(3l);
		
		participationVO =  eventBusiness.findParticipation(participationVO, "participacao_com_payloadsImagens");
		
		
		System.out.println(participationVO);
	}	
	
	
	@Test
	public void testFetchParticipation(){
	
		
		EventVO eventVO = new EventVO();
		eventVO.setPK(4l);
		
		ExtraArgumentsDTO argumentsDTO = new ExtraArgumentsDTO();
		
		argumentsDTO.setFirstResult(17);
		argumentsDTO.setMaxResults(3);
		
		argumentsDTO.getOrderMap().put("que.PK", true);
		argumentsDTO.getOrderMap().put("par.PK", true);
		argumentsDTO.getOrderMap().put("abc.PK", true);
		
		List<CriterionDetailDTO> questionResutls = eventBusiness.findCriterionDetails(eventVO, argumentsDTO);
		
		Long count = eventBusiness.countCriterionDetails(eventVO);
		System.out.println(count);
		
		for (CriterionDetailDTO criterionDetailDTO : questionResutls) {
			
			System.out.println(criterionDetailDTO.getCriterionVO().getDescription()+"  "+criterionDetailDTO.getCriterionResultVO());
			
		}
		System.out.println("##resultados encontrados #: "+questionResutls.size());
		
	}
	
	
	
	
	@Test
	public void testGetMax(){

		JazzDetachedCriteria criteria = JazzDetachedCriteria.forClass(EventVO.class);
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		criteria.enableFetchProfile("event_participations");
		
		List<EventVO> list = hibernateTemplate.findByCriteria(criteria);
		
		for (EventVO eventVO : list) {
			
			Collection<ParticipationVO> parts = eventVO.getParticipations();
			
			Object min = AbstractBaseVO.getMin("dtInc", parts);

			Object max = AbstractBaseVO.getMax("dtInc", parts);
			
			System.out.println(" min "+min);
			System.out.println(" max "+max);
			
		}
		
		
	}
	
	
	@Test
	public void testFetchEvent(){
		
		JazzDetachedCriteria criteria = JazzDetachedCriteria.forClass(EventVO.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		criteria.enableFetchProfile("event_participations");
		
		List<EventVO> eventos = hibernateTemplate.findByCriteria(criteria);
		
		for (EventVO eventVO : eventos) {
			
			System.out.println(eventVO);
			
			Collection<ParticipationVO> participationVOs = eventVO.getParticipations();
			
			for (ParticipationVO participationVO : participationVOs) {
				
				System.out.println(" "+participationVO);
				if(participationVO!=null){
					
				
					Collection<PayloadVO> payloads = participationVO.getPayloadVOs();
					
					for (PayloadVO payloadVO : payloads) {
						System.out.println(" \t "+payloadVO);
					}
					/*
					 */
				}
				
			}
			
		}
		
		
		
	}
	
	
	
	@Test
	public void testFetchExamQtn(){
		
		JazzDetachedCriteria criteria = JazzDetachedCriteria.forClass(ExamVO.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		criteria.enableFetchProfile("exame_com_questionarios");
		
		List<ExamVO> exams = hibernateTemplate.findByCriteria(criteria);
		
		for (ExamVO examVO : exams) {
			
			System.out.println(examVO);
			
			List<QuestionnaireVO> qtns = examVO.getQuestionnaires();
			
			for (QuestionnaireVO questionnaireVO : qtns) {
				
				System.out.println(questionnaireVO);
				
			}
			
		}
		
		
		
	}
	
	@Test
	public void testFetchMode(){

		JazzDetachedCriteria criteria = JazzDetachedCriteria.forClass(QuestionnaireVO.class);
		
		//criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		ResultTransformer rt = DistinctRootEntityResultTransformer.INSTANCE;
		criteria.setResultTransformer(rt);
		
		criteria.add(Restrictions.eq("PK", 1l));

		criteria.enableFetchProfile("questinario_com_questoes");
		criteria.enableFetchProfile("questao_com_alternativas");
		
		List<QuestionnaireVO> questinnaires = hibernateTemplate.findByCriteria(criteria);
		
		
		//criteria.disableFetchProfile("questinario_com_questoes");
		//questinnaires = hibernateTemplate.findByCriteria(criteria);
		
		for (QuestionnaireVO questionnaireVO : questinnaires) {
			
			System.out.println("\t "+questionnaireVO.getDescription());
			
			Collection<QuestionVO> questions = questionnaireVO.getQuestions();
			for (QuestionVO questionVO : questions) {
				
				System.out.println("\t\t "+questionVO.getDescription());
				Set<AbstractCriterionVO> criterions = questionVO.getCriterionVOs();
				
				for (AbstractCriterionVO alternativeVO : criterions) {
					String x = alternativeVO==null?"":alternativeVO.getDescription();
					
					System.out.println("\t\t\t "+alternativeVO+" "+x);
					
				}
				
			}
			
			System.out.println(questionnaireVO);
		}		
		
	}
	
	
	
	@Test
	public void testFetchMode2(){

		JazzDetachedCriteria criteria = JazzDetachedCriteria.forClass(QuestionVO.class);
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		//criteria.add(Restrictions.eq("PK", 1l));

		criteria.enableFetchProfile("questao_com_alternativas");
		List<QuestionVO> questions = hibernateTemplate.findByCriteria(criteria);
		
		
			
			for (QuestionVO questionVO : questions) {
				
				System.out.println("\t\t "+questionVO.getDescription());
				Set<AbstractCriterionVO> alternatives = questionVO.getCriterionVOs();
				
				for (AbstractCriterionVO criterionVO : alternatives) {
					
					String x = criterionVO==null?"":criterionVO.getDescription();
					
					System.out.println("\t\t\t "+criterionVO+" "+x);
					
				}
				
			}
			
		
	}


	/**
	 * @param fetchProfile
	 */
	protected void enableFetchProfile(final String fetchProfile) {
		hibernateTemplate.execute(new HibernateCallback<QuestionnaireVO>() {
			@Override
			public QuestionnaireVO doInHibernate(Session session) throws HibernateException, SQLException {
				session.enableFetchProfile(fetchProfile);
				return null;
			}
		});
	}
	
	
	public void testQRCodeReader() throws Exception{
		
		BufferedImage img = ImageIO.read(new File("erroLeituraImgOtima.png"));
		
		String result = qrCodeReader.execute(img);
		
		System.out.println(result);
		
	}
	
	
	public void testIsChecked() throws IOException{
		

		for(int i=2; i<=16;i++){
			
			String fileName = "a_"+i;

			BufferedImage bi = ImageIO.read(new File(fileName));
						
			System.out.print(fileName+" \t");
			jazzOMRImageParser.particleAnaylis(bi);

		}

		
		
	}


	

	protected Double getValue(ResultsTable rt, COLUMNS_PARTICLE column, int row) {

		if (rt.columnExists(column.col)) {
			return rt.getValueAsDouble(column.col, row);
		} else {
			return null;
		}

	}

	
	public void testFindParicipartion() {
		
		@SuppressWarnings("unchecked")
		List<ParticipationVO> participations = hibernateTemplate.findByNamedParam(
				" select par " + 
				" from ParticipationVO as par " + 
				"  inner join fetch par.examVariantVO as exv " + 
				"  inner join fetch exv.exam as exa " + 
				"  inner join fetch exa.examOMRMetadataVO as exm " + 
				"	where	par.PK in (:participationPK) ",

		new String[] { "participationPK" }, new Object[] { 13l });

		ParticipationVO participationVO = participations.get(0);
		
		System.out.println("part="+participationVO);
		
		
		
	}
	
	
	
	@Test
	public void testQueryAlternativeCoordinate(){
		
		Long participationPK = 17l;
		Integer pagina = new Integer(2);

		@SuppressWarnings("unchecked")
		List aCoordinates = hibernateTemplate.findByNamedParam(
				" select distinct qco" +
				"  from ParticipationVO par " +
				"	inner join par.examVariantVO exv "+
				"	inner join exv.questionCoordinates qco " +
				"	inner join fetch qco.questionVO que " +
				"	inner join fetch qco.alternativeCoordinates aco " +
				"	inner join fetch aco.alternativeVO alt " +
				"	where " + 
				"		par.PK in (:participationPK) and " + 
				"		( aco.pagina = :pagina or qco.pagina = :pagina  ) " + 
				" order by aco.alternativeOrder , qco.questionOrder ",
		new String[] { "participationPK", "pagina" }, new Object[] { participationPK, pagina  });

		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		for (Object obj : aCoordinates) {
			System.out.println(obj);
		}
	}

	
	@Test
	public void testQueryExam(){
		
		Long participationPK = 1l;

		@SuppressWarnings("unchecked")
		List<ExamVariantVO> examVars = hibernateTemplate.findByNamedParam(
				" select exv " + 
				" from ParticipationVO as par " +
				"  inner join par.examVariantVO as exv " + 
				"	where	par.PK in (:participationPK) ",

		new String[] { "participationPK"}, new Object[] { participationPK});

		
		System.out.println();
		System.out.println();
		
		for (ExamVariantVO examVariantVO : examVars) {
			
			System.out.println(examVariantVO.getPK()+" "+examVariantVO.getExamVO().getPK());
		}
	}

	///
	
	
	@Test
	public void testDirectory() throws FileNotFoundException, RefParticleNotFoundException, JazzOMRException{
	
		String strDir = "/home/darcio/workspace/modules/JazzOMR/bsn/digitalizado/";
		File dir = new File(strDir);
		dir.mkdirs();
		
		String[] files = dir.list();
		
		for (String filePath : files) {
			
			System.out.println(filePath);
			long now = System.currentTimeMillis();
			
			String pathname = strDir+filePath;
			
			System.out.println("#######################################################################");
			System.out.println("###: "+pathname);
			
			File file = new File(pathname);
			if(file.isFile()){
				parse(file);
			}
			
			System.out.println(pathname + " - " +( System.currentTimeMillis()-now));
			
		}
		
	}

	/**
	 * @param file
	 * @throws FileNotFoundException
	 * @throws JazzOMRException
	 * @throws RefParticleNotFoundException
	 */
	protected void parse(File file) throws FileNotFoundException, JazzOMRException {
		InputStream is = new FileInputStream(file);
					
		ImageDocVO imageDocVO = jazzOMRImageParser.prepareImageProcessing(is);
	
		try {
			jazzOMRImageParser.parseImage(imageDocVO);

			//sucesso
			jazzOMRImageParser.updatePayloadVO(imageDocVO);
			
		} catch (JazzOMRException e) {
			//erro
			jazzOMRImageParser.updatePayloadVO(imageDocVO, e);
			
		}
		
		
	}



	
	
	
	
}

