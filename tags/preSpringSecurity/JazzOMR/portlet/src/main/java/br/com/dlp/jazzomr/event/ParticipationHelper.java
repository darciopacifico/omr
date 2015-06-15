package br.com.dlp.jazzomr.event;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.sql.DataSource;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.JRXmlExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRPrintXmlLoader;

import org.apache.commons.collections.IteratorUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import br.com.dlp.framework.dao.ExtraArgumentsDTO;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.vo.IBaseVO;
import br.com.dlp.jazzomr.JazzOMRJRException;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.exam.ExamBusiness;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.exam.ExamVariantVO;
import br.com.dlp.jazzomr.exam.QuestionnaireVO;
import br.com.dlp.jazzomr.exam.coordinate.CriterionCoordinateVO;
import br.com.dlp.jazzomr.exceptions.JazzOMRException;
import br.com.dlp.jazzomr.jr.handler.JazzQRCodeGenerator;
import br.com.dlp.jazzomr.jr.util.IQRCodeGenerator;
import br.com.dlp.jazzomr.person.GrupoBusiness;
import br.com.dlp.jazzomr.person.GrupoVO;
import br.com.dlp.jazzomr.person.PessoaBusiness;
import br.com.dlp.jazzomr.person.PessoaVO;
import br.com.dlp.jazzomr.poc.ExamInstanceXMLHandler;
import br.com.dlp.jazzomr.poc.JazzOMRImageParser;
import br.com.dlp.jazzomr.question.AbstractCriterionVO;
import br.com.dlp.jazzomr.question.AlternativeScore;
import br.com.dlp.jazzomr.question.AlternativeVO;
import br.com.dlp.jazzomr.question.QuestionVO;

/**
 * Auxilia EventJSFBean na operacionalização da montagem de participações de eventos. Permite selecionar participantes por pesquisas de grupos ou de pessoas
 * @author darcio
 *
 */
@Scope(value="session")
@Component
public class ParticipationHelper {
	
	protected static final String JAZZOMR_PROPERTIES = "reportQueries.properties";
	protected static final String REPORT_QUERY_EXAM_VARIANT = "reportQueryExamVariant";
	protected static final String REPORT_QUERY_EVENT = "reportQueryEvent";
	private static final Logger log = LoggerFactory.getLogger(ParticipationHelper.class);
	
	protected static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

	@Autowired
	@Qualifier("jazzOmrProperties")
	private Properties jazzOmrProperties;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	private String examDescription;
	
	private String groupDescription;
	private String pessoaLogin;
	private String pessoaEmail;
	private String pessoaNome;
	private String pessoaTelefone;
	
	private List<GrupoVO> gruposSelecionados = new ArrayList<GrupoVO>();
	private List<PessoaVO> pessoasSelecionadas = new ArrayList<PessoaVO>();
	private List<ExamVO> examSelecionados = new ArrayList<ExamVO>();
	
	
	private Integer progressCount=-1;
	
	/**
	 * Modelo de variantes de exames
	 */
	private Integer maxQuestions = 10;
	private Integer maxAlternatives = 5;
	private Integer numeroDeVariacoes = 4;
	
	/**
	 * Maximo de resultados nos list shuttles
	 */
	private Integer maxResults = 130;
	
	@Autowired
	private PessoaBusiness pessoaBusiness;

	@Autowired
	private EventBusiness eventBusiness;

	@Autowired
	private ExamBusiness examBusiness;

	@Autowired
	private GrupoBusiness grupoBusiness;

	/**
	 * Construtor padrao, apenas formalidade do bean
	 */
	public ParticipationHelper() {}
	
	/**
	 * Adiciona mensagem de diálogo para o usuário
	 * 
	 * @param severityInfo
	 * @param summary
	 * @param detail
	 */
	protected void addMessage(Severity severityInfo, String summary, String detail) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(severityInfo, summary, detail));
	}
	

	
	/**
	 * 
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=JazzBusinessException.class)
	public String processParticipations(EventVO eventVO) throws JazzBusinessException{
		
		if(!getIsEventVOSaved(eventVO) || !getIsPeopleAndExamSelecteds() ){
			addMessage(FacesMessage.SEVERITY_INFO, "Para registrar participações selecione ao menos uma pessoa/grupo e um exame", "");
			return "";
		}
		
		
		//tenta salvar event vo. Caso haja algum constraint violation, critica e sai fora...
		if(saveIBaseVO(eventVO)){
				
			for (ExamVO examVO : this.getExamSelecionados()) {
				
				//apenas para atualizar exame
				examVO = examBusiness.findByPK(examVO, "exame_com_questionarios");
				
				List<ExamVariantVO> examVariants = criarVariacoesExame(examVO, numeroDeVariacoes);
				saveIBaseVOAll(examVariants);
	
				hibernateTemplate.flush();
				
				fillCoordinates(examVO,examVariants);
				saveIBaseVOAll(examVariants);
				saveIBaseVO(examVO);
	
				/* MONTAR PARTICIPACOES EM EXAMES */
				
				registrarParticipacoes(eventVO, groupPessoas(), examVariants);
	
			}
			saveIBaseVO(eventVO);
			hibernateTemplate.flush();
			
		}
		
		Integer newParts = this.getExamSelecionados().size() * groupPessoas().size();
		
		addMessage(FacesMessage.SEVERITY_INFO, "Foram registradas "+newParts+" novas participações!", "");
		
		
		return "";
		
		
	}
	
	/**
	 * Testa condições para que o processamento de participações seja permitido
	 * 
	 * @param eventVO
	 * @return
	 */
	public Boolean getIsEventVOSaved(EventVO eventVO){
		
		boolean hasErrors = hasValidationErrors(eventVO);
			
		boolean allow = eventVO!=null && eventVO.getPK()!=null;
		
		return allow && !hasErrors;
		
	}
	

	/**
	 * @param eventVO
	 * @return
	 */
	protected boolean hasValidationErrors(EventVO eventVO) {
		
		if(eventVO==null)
			return false;
		
		Validator validator = validatorFactory.getValidator();
		
		Set<ConstraintViolation<EventVO>> constraints = validator.validate(eventVO);
		
		boolean hasErrors = constraints!=null && constraints.size()>0;
		return hasErrors;
	}

	/**
	 * @param eventVO
	 * @return success
	 * @throws JazzBusinessException
	 */
	protected Boolean saveIBaseVO(IBaseVO<? extends Serializable> eventVO) throws JazzBusinessException {
		try {
			hibernateTemplate.setFlushMode(HibernateTemplate.FLUSH_ALWAYS);
			hibernateTemplate.saveOrUpdate(eventVO);
			hibernateTemplate.flush();
			return true;
		} catch (ConstraintViolationException e) {
			parseViolations(e);
			return false;
		}
	}
	
	
	/**
	 * @param eventVO
	 * @throws JazzBusinessException
	 */
	protected void saveIBaseVOAll(Collection baseVOs) throws JazzBusinessException {
		try {
			hibernateTemplate.setFlushMode(HibernateTemplate.FLUSH_ALWAYS);
			hibernateTemplate.saveOrUpdateAll(baseVOs);
			hibernateTemplate.flush();
		} catch (ConstraintViolationException e) {
			parseViolations(e);
			throw new JazzOMRException("Erro ao tentar salvar entidade!",e);
		}
	}
	
	
	
	
	
	
	




	@Test
	public void testOMRModel() throws JazzOMRException {

		/* MOMENTO PLANEJAMENTO ACADEMICO */
		Set<QuestionVO> questoesG = montarQuestoesGeografia();
		hibernateTemplate.saveOrUpdateAll(questoesG);

		Set<QuestionVO> questoesF = montarQuestoesFisica();
		hibernateTemplate.saveOrUpdateAll(questoesF);

		Set<QuestionnaireVO> questionarios = montarQuestionarios(questoesG, questoesF);

		ExamVO examVO = montarExame(questionarios);
		hibernateTemplate.saveOrUpdate(examVO);

		/* OPCAO POR VARIACOES DE EXAMES */
		int numeroDeVariacoes = 4;

		List<ExamVariantVO> examVariants = criarVariacoesExame(examVO, numeroDeVariacoes);
		hibernateTemplate.saveOrUpdateAll(examVariants);

		fillCoordinates(examVO,examVariants);
		hibernateTemplate.saveOrUpdateAll(examVariants);
		hibernateTemplate.saveOrUpdate(examVO);
		
		/* MONTAR PARTICIPACOES EM EXAMES */
		EventVO evento = montarEventos();
		List<PessoaVO> pessoas = montarParticipantes();
		registrarParticipacoes(evento, pessoas, examVariants);

		hibernateTemplate.saveOrUpdate(evento);

	}

	

	/**
	 * @param eventVO
	 * @return
	 */
	protected byte[] createPDFBytes(EventVO eventVO) {
		JasperPrint jp = createJasperPrintE(eventVO);

		
		logPrintImages(jp,eventVO);
		
		
		byte[] pdfReport = exportToPDF(jp);
		return pdfReport;
	}


	/**
	 * @param pdfReport
	 * @param localFileName
	 */
	protected void printToFile(byte[] pdfReport, String localFileName) {
		try {
			FileOutputStream fos = new FileOutputStream(localFileName);
			fos.write(pdfReport);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("erro", e);
		} catch (IOException e) {
			throw new RuntimeException("erro", e);
		}
	}

	/**
	 * Cria arquivos de imagens das provas. Utilizado exclusivamente em depuracao de codigo
	 * @param jp
	 * @param eventVO 
	 */
	protected static void logPrintImages(JasperPrint jp, EventVO eventVO) {
		if(!log.isDebugEnabled()){
			return;
		}
		
		try {

			int pageCount = jp.getPages().size();

			for (int i = 0; i < pageCount; i++) {

				File file = new File("result/evento_"+eventVO.getPK()+"_pg_" + (i + 1) + "."+ JazzOMRImageParser.IMG_EXT);
				
				file.mkdirs();

				JRGraphics2DExporter graphics2dExporter = new JRGraphics2DExporter();
				BufferedImage bi = new BufferedImage(600, 900, BufferedImage.TYPE_4BYTE_ABGR);
				Graphics graphics = bi.createGraphics();
				graphics2dExporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
				graphics2dExporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, graphics);
				graphics2dExporter.setParameter(JRGraphics2DExporterParameter.PAGE_INDEX, i);
				graphics2dExporter.exportReport();

				ImageIO.write(bi, JazzOMRImageParser.IMG_EXT, file);
			}

		} catch (JRException e1) {
			throw new RuntimeException("erro", e1);
		} catch (IOException e) {
			throw new RuntimeException("erro", e);
		}
	}

	protected JasperPrint createJasperPrintE(EventVO... eventos) {

		HashMap<String, Object> parameters = mountParametersEventReport(eventos);

		JasperReport jasperReport = getJasperReport("/reports/ExamReport.jasper");

		JasperPrint jasperPrint = fillReport(parameters, jasperReport);

		return jasperPrint;

	}

	/**
	 * @param eventos
	 * @return
	 */
	protected HashMap<String, Object> mountParametersEventReport(EventVO... eventos) {
		List<Long> eventosKeys = new ArrayList<Long>(eventos.length);

		for (EventVO evento : eventos) {
			eventosKeys.add(evento.getPK());
		}

		BufferedImage bi;
			bi = loadImage("/reports/logo-cliente.png");

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		IQRCodeGenerator qrCodeGen = new JazzQRCodeGenerator();

		String reportQuery = getReportQuery(eventosKeys, " eve.pk ", REPORT_QUERY_EVENT);

		parameters.put("qrCodeGen", qrCodeGen);
		parameters.put("imgLogoEscola", bi);
		parameters.put("pReportQuery", reportQuery);
		parameters.put("SUBREPORT_DIR", "/reports/");
		
		
		return parameters;
	}

	@Test(enabled = false)
	public void testeRandom() {

		DetachedCriteria criteria = DetachedCriteria.forClass(QuestionVO.class);

		criteria.add(Restrictions.sqlRestriction(" 1=1 order by rand() "));

		List<QuestionVO> questions = hibernateTemplate.findByCriteria(criteria);

		for (QuestionVO questionVO : questions) {
			System.out.println(questionVO.getDescription());
		}

	}

	/**
	 * @param exams
	 * @param maxQuestions
	 * @param maxAlternatives
	 * @param numeroDeVariacoes
	 * @return
	 */
	protected List<ExamVariantVO> criarVariacoesExame(ExamVO examVO, int numeroDeVariacoes) {
		
		boolean randomizar = numeroDeVariacoes>1;
		
		List<ExamVariantVO> variants = new ArrayList<ExamVariantVO>();

		for (int i = 0; i < numeroDeVariacoes; i++) {

			ExamVariantVO examVariantVO = new ExamVariantVO();
			examVariantVO.setExamVO(examVO);
			
			List<QuestionnaireVO> questionnaires = examVO.getQuestionnaires();
			fillQuestionCoordinates(randomizar, examVariantVO, questionnaires);
			variants.add(examVariantVO);

		}
		
		//para não viciar a distribuição das variantes
		Collections.shuffle(variants);
		
		return variants;
	}


	/**
	 * @param questoesPorTema
	 * @param alternativaPorQuestao
	 * @param randomizar
	 * @param examVariantVO1
	 * @param questionnaires
	 */
	protected void fillQuestionCoordinates(boolean randomizar, ExamVariantVO examVariantVO1, List<QuestionnaireVO> questionnaires) {
		
		for (QuestionnaireVO questionnaireVO : questionnaires) {

			Iterator<QuestionVO> itQuestions = createOrderedIterator(questionnaireVO.getQuestions(), randomizar);

			int ordemQuestao = 1;

			while (itQuestions.hasNext()) {

				QuestionVO questionVO = itQuestions.next();

				List<CriterionCoordinateVO> criterionCoordinateVOs = fillAlternativeCoordinates(questionVO, randomizar, ordemQuestao++);

				examVariantVO1.getCriterionCoordinates().addAll(criterionCoordinateVOs);
				
			}
			
		}
	}

	/**
	 * 
	 * @param parentExamVO
	 * @param examVariants
	 */
	private void fillCoordinates(ExamVO parentExamVO, List<ExamVariantVO> examVariants) {

		
			Map<Long, CriterionCoordinateVO> mapAlternativeCoords = new HashMap<Long, CriterionCoordinateVO>(500);
		
			fillReferenceMaps(parentExamVO,examVariants, mapAlternativeCoords);
		
			// OK criar jasperprint e exportar para xml e pdf
			JasperPrint jp = createJasperPrint(examVariants);
			byte[] xmlBytes = exportToXML(jp);
			
		
			if(log.isDebugEnabled()){
				byte[] pdfBytes = exportToPDF(jp);
				logExportedFiles(pdfBytes, xmlBytes, "pdfResult.pdf", "pdfResult.xml");
			}
		
			// parsear xml resultado e popupar coordenadas
			SAXParser saxParser = createSAXParser();
			ExamInstanceXMLHandler examInstanceXMLHandler = new ExamInstanceXMLHandler(parentExamVO,mapAlternativeCoords);
		
			ByteArrayInputStream xmlBais = new ByteArrayInputStream(xmlBytes);
		
			try {
				saxParser.parse(xmlBais, examInstanceXMLHandler);
			} catch (SAXException e) {
				throw new JazzRuntimeException("erro ao tentar parsear xml resultado", e);
			} catch (IOException e) {
				throw new JazzRuntimeException("erro ao tentar parsear xml resultado", e);
			}
		

	}

	/**
	 * @param mapExamVars
	 * @param examVariantVO
	 * @return
	 */
	protected Set<ExamVariantVO> getExamVariantSet(Map<Long, Set<ExamVariantVO>> mapExamVars, ExamVariantVO examVariantVO) {
		Long examPK = examVariantVO.getExamVO().getPK();

		Set<ExamVariantVO> examVars = mapExamVars.get(examPK);
		if (examVars == null) {
			examVars = new HashSet<ExamVariantVO>();
			mapExamVars.put(examPK, examVars);
		}
		return examVars;
	}

	/** TODO REVISAR O NOME DESTE METODO. ESTA CONFUSO O SIGNIFICADO
	 * Separa os mapas de coordenadas de questoes e alternativas. Adicionalmente, valida se as variacoes de exame pertencem a um mesmo exame pai 
	 * @param examVariants
	 * @param mapQuestionCoords
	 * @param mapAlternativeCoords
	 * @throws JazzOMRException 
	 */
	protected void fillReferenceMaps(ExamVO parentExamVO, List<ExamVariantVO> examVariants, Map<Long, CriterionCoordinateVO> mapAlternativeCoords) {
		for (ExamVariantVO examVariantVO : examVariants) {
			
			if(!isParent(parentExamVO, examVariantVO)){
				String message = "Nao e permitido processar em conjunto variacoes de exames que nao tenham um mesmo ExameVO de origem! Por favor, separe as variantes de exam por exame de origem a cada chamada!";
				log.error(message);
				throw new JazzRuntimeException(message);
			}
			
			List<CriterionCoordinateVO> alternatives = examVariantVO.getCriterionCoordinates();
			for (CriterionCoordinateVO criterionCoordinateVO : alternatives) {
				mapAlternativeCoords.put(criterionCoordinateVO.getPK(), criterionCoordinateVO);
			}
			
		}
	}

	/**
	 * @param parentExamVO
	 * @param examVariantVO
	 * @return
	 */
	protected boolean isParent(ExamVO parentExamVO, ExamVariantVO examVariantVO) {
		return examVariantVO.getExamVO().getPK().equals(parentExamVO.getPK());
	}

	/**
	 * @param pdfBytes
	 * @param xmlBytes
	 * @param pdfName
	 * @param xmlName
	 * @throws JazzOMRException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void logExportedFiles(byte[] pdfBytes, byte[] xmlBytes, String pdfName, String xmlName) {
		
		if(!log.isDebugEnabled())
			return;
		
		byte[] pdfFromXML;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(xmlBytes);
			JasperPrint jasperPrint = JRPrintXmlLoader.load(bais);
			JRPdfExporter pdfExporter = new JRPdfExporter();
			
			ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
			pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			pdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baosPDF);
			pdfExporter.exportReport();
			
			pdfFromXML = baosPDF.toByteArray();
			
		} catch (JRException e) {
			throw new JazzRuntimeException("erro ao tentar montar relatorio resultado do XML!", e);
		}

		writeToFile(pdfFromXML, "fromXML_"+pdfName);

		writeToFile(pdfBytes, pdfName);

		writeToFile(xmlBytes, xmlName);
	}




	/**
	 * @param bytes
	 * @param name
	 * @throws JazzOMRException
	 */
	protected void writeToFile(byte[] bytes, String name) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(name);
			fos.write(bytes);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			throw new JazzRuntimeException("erro ao tentar logar resultado de exportacao de relatorio!", e);
		} catch (IOException e) {
			throw new JazzRuntimeException("erro ao tentar logar resultado de exportacao de relatorio!", e);
		}
	}

	/**
	 * @param examVariants
	 * @return
	 */
	protected Set<Long> createExamVariantPKSet(Set<ExamVariantVO> examVariants) {
		Set<Long> examVariationsPKs = new HashSet<Long>();
		for (ExamVariantVO variacao : examVariants) {
			examVariationsPKs.add(variacao.getPK());
		}
		return examVariationsPKs;
	}

	
	/**
	 * Cria as participações por pessoa, atribuindo a cada pessoa uma variante do exame contida na coleção examVariants
	 * 
	 * @param eventVO
	 * @param pessoas
	 * @param examVariants
	 */
	private void registrarParticipacoes(EventVO eventVO, Collection<PessoaVO> pessoas, List<ExamVariantVO> examVariants) {

		
		
		//looping iterator, para distribuição das variações de exame
		Iterator<ExamVariantVO> itExVar = IteratorUtils.loopingIterator(examVariants);

		for (PessoaVO pessoaVO : pessoas) {
			ParticipationVO participationVO = new ParticipationVO();

			participationVO.setPessoaVO(pessoaVO);

			ExamVariantVO nowExamVariant = itExVar.next();
			participationVO.setExamVariantVO(nowExamVariant);

			eventVO.getParticipations().add(participationVO);
		}
	}

	
	/**
	 * 
	 * @return
	 */
	private List<PessoaVO> montarParticipantes() {

		List<PessoaVO> pessoaVOs = new ArrayList<PessoaVO>();

		pessoaVOs.add(criaPessoa(1l, "Luiz"));
		pessoaVOs.add(criaPessoa(2l, "Antonio"));
		pessoaVOs.add(criaPessoa(3l, "Joao"));
		pessoaVOs.add(criaPessoa(4l, "Marcio"));
		pessoaVOs.add(criaPessoa(5l, "Adriana"));
		pessoaVOs.add(criaPessoa(6l, "Valeria"));
		pessoaVOs.add(criaPessoa(7l, "Joana"));
		pessoaVOs.add(criaPessoa(8l, "Jeane"));
		pessoaVOs.add(criaPessoa(9l, "Carlos"));
		pessoaVOs.add(criaPessoa(10l, "Daniela"));
		pessoaVOs.add(criaPessoa(11l, "Darcio"));
		pessoaVOs.add(criaPessoa(12l, "Maria"));
		pessoaVOs.add(criaPessoa(13l, "Paulo"));
		pessoaVOs.add(criaPessoa(14l, "Pedro"));
		pessoaVOs.add(criaPessoa(15l, "Matheus"));
		pessoaVOs.add(criaPessoa(16l, "Leandro"));

		return pessoaVOs;
	}

	private EventVO montarEventos() {


		EventVO eventVO = new EventVO();
		eventVO.setDescription("Exame final do 2. semestre. Turma 2");
		eventVO.setDtInicio(new Date());
		eventVO.setDtFim(new Date());

		return eventVO;
	}

	/**
	 * @param questionVO
	 * @param questionCoordinateVO
	 * @param alternativaPorQuestao
	 * @return 
	 */
	protected List<CriterionCoordinateVO> fillAlternativeCoordinates(QuestionVO questionVO, boolean shuffle, Integer ordemQuestao) {
		List<CriterionCoordinateVO> criterionCoordinateVOs = new ArrayList<CriterionCoordinateVO>();

		int ordemAlternativa = 1;

		Iterator<AbstractCriterionVO> itCriterions = createOrderedIterator(questionVO.getCriterionVOs(), shuffle);
		
		while (itCriterions.hasNext() ) {
			
			AbstractCriterionVO criterionVO = itCriterions.next();

			CriterionCoordinateVO criterionCoordinateVO = new CriterionCoordinateVO();
			criterionCoordinateVO.setAbstractCriterionVO(criterionVO);
			criterionCoordinateVO.setQuestionVO(questionVO);
			criterionCoordinateVO.setQuestionOrder(ordemQuestao);
			
			if(criterionVO instanceof AlternativeVO){
				// se for alternativa, aplica controle de ordem
				criterionCoordinateVO.setAlternativeOrder(ordemAlternativa++);
			}

			criterionCoordinateVOs.add(criterionCoordinateVO);
		}
		
		return criterionCoordinateVOs;
		
	}

	/**
	 * @param <T>
	 * @param itens
	 * @return
	 */
	protected <T> Iterator<T> createOrderedIterator(Collection<T> itens, boolean shuffle) {

		if (shuffle) {
			List<T> itemList = new ArrayList<T>(itens);
			Collections.shuffle(itemList);
			itens = itemList;
		}

		Iterator<T> itAlternative = itens.iterator();
		return itAlternative;
	}

	/**
	 * @param <T>
	 * @param alternatives
	 * @return
	 */
	protected <T> Iterator<T> createOrderedIterator(Collection<T> alternatives) {
		return createOrderedIterator(alternatives, true);
	}

	private ExamVO montarExame(Set<QuestionnaireVO> questionarios) {

		ExamVO examVO = new ExamVO();
		examVO.setDescription("Exame modelo");

		examVO.getQuestionnaires().addAll(questionarios);

		return examVO;
	}

	/**
	 * 
	 * @param questoesG
	 * @param questoesF
	 * @return
	 */
	private Set<QuestionnaireVO> montarQuestionarios(Set<QuestionVO> questoesG, Set<QuestionVO> questoesF) {

		QuestionnaireVO questionnaireF = new QuestionnaireVO();
		questionnaireF.setDescription("Questionário de Física");

		addQuestion(questoesF, questionnaireF);

		QuestionnaireVO questionnaireG = new QuestionnaireVO();
		questionnaireG.setDescription("Questionário de Geografia");

		addQuestion(questoesG, questionnaireG);

		hibernateTemplate.saveOrUpdate(questionnaireF);
		hibernateTemplate.saveOrUpdate(questionnaireG);

		Set<QuestionnaireVO> questionnaireVOs = new HashSet<QuestionnaireVO>(2);

		questionnaireVOs.add(questionnaireG);
		questionnaireVOs.add(questionnaireF);

		return questionnaireVOs;
	}

	/**
	 * @param questoesF
	 * @param questionnaireF
	 */
	protected void addQuestion(Set<QuestionVO> questoesF, QuestionnaireVO questionnaireF) {
		for (QuestionVO questionVO : questoesF) {
			questionnaireF.getQuestions().add(questionVO);
		}
	}

	private Set<QuestionVO> montarQuestoesGeografia() {

		Set<QuestionVO> questoes = new HashSet<QuestionVO>();

		QuestionVO questionVO1 = new QuestionVO("Quantos países fazem fronteira com Portugal?");
		QuestionVO questionVO2 = new QuestionVO("Qual é a capital da Alemanha?");
		QuestionVO questionVO3 = new QuestionVO("Qual é o país mais ao sul das Américas?");
		QuestionVO questionVO4 = new QuestionVO("Em qual país se fala a língua Mandarim?");
		QuestionVO questionVO5 = new QuestionVO("Quais são as duas principais línguas faladas no Canadá ?");

		questionVO1.getCriterionVOs().add(new AlternativeVO("1", AlternativeScore.CORRECT));
		questionVO1.getCriterionVOs().add(new AlternativeVO("2"));
		questionVO1.getCriterionVOs().add(new AlternativeVO("3"));
		questionVO1.getCriterionVOs().add(new AlternativeVO("4"));
		questionVO1.getCriterionVOs().add(new AlternativeVO("5"));

		questionVO2.getCriterionVOs().add(new AlternativeVO("Dresdem"));
		questionVO2.getCriterionVOs().add(new AlternativeVO("Budapeste"));
		questionVO2.getCriterionVOs().add(new AlternativeVO("Berlim", AlternativeScore.CORRECT));
		questionVO2.getCriterionVOs().add(new AlternativeVO("Stuttgart"));
		questionVO2.getCriterionVOs().add(new AlternativeVO("Munich"));

		questionVO3.getCriterionVOs().add(new AlternativeVO("Uruguai"));
		questionVO3.getCriterionVOs().add(new AlternativeVO("Brasil"));
		questionVO3.getCriterionVOs().add(new AlternativeVO("Chile", AlternativeScore.CORRECT));
		questionVO3.getCriterionVOs().add(new AlternativeVO("Argentina"));
		questionVO3.getCriterionVOs().add(new AlternativeVO("Peru"));

		questionVO4.getCriterionVOs().add(new AlternativeVO("Marrocos"));
		questionVO4.getCriterionVOs().add(new AlternativeVO("Malásia"));
		questionVO4.getCriterionVOs().add(new AlternativeVO("China", AlternativeScore.CORRECT));
		questionVO4.getCriterionVOs().add(new AlternativeVO("Vietnam"));
		questionVO4.getCriterionVOs().add(new AlternativeVO("México"));

		questionVO5.getCriterionVOs().add(new AlternativeVO("Inglês e Espanhol"));
		questionVO5.getCriterionVOs().add(new AlternativeVO("Espanhol e Armênio"));
		questionVO5.getCriterionVOs().add(new AlternativeVO("Francês e Alemão"));
		questionVO5.getCriterionVOs().add(new AlternativeVO("Espanhol e Italiano"));
		questionVO5.getCriterionVOs().add(new AlternativeVO("Francês e Inglês", AlternativeScore.CORRECT));

		questoes.add(questionVO1);
		questoes.add(questionVO2);
		questoes.add(questionVO3);
		questoes.add(questionVO4);
		questoes.add(questionVO5);

		return questoes;

	}

	private Set<QuestionVO> montarQuestoesFisica() {

		Set<QuestionVO> questoes = new HashSet<QuestionVO>();

		QuestionVO questionVO1 = new QuestionVO("Qual é a velocidade da luz no vácuo?");
		QuestionVO questionVO2 = new QuestionVO("Qual é a aceleração da gravidade?");
		QuestionVO questionVO3 = new QuestionVO("Qual o nome da primeira teoria da relatividade Einstein?");
		QuestionVO questionVO4 = new QuestionVO("Qual é a fórmula da teoria da relatividade especial?");
		QuestionVO questionVO5 = new QuestionVO("Qual é a velocidade do som no ar?");

		questionVO1.getCriterionVOs().add(new AlternativeVO("~300.000Km/s", AlternativeScore.CORRECT));
		questionVO1.getCriterionVOs().add(new AlternativeVO("~1200Km/2"));
		questionVO1.getCriterionVOs().add(new AlternativeVO("~1.000.000Km/s"));
		questionVO1.getCriterionVOs().add(new AlternativeVO("~20.000Km/h"));
		questionVO1.getCriterionVOs().add(new AlternativeVO("~10.000m/s"));

		questionVO2.getCriterionVOs().add(new AlternativeVO("~9m/s2", AlternativeScore.CORRECT));
		questionVO2.getCriterionVOs().add(new AlternativeVO("~10m/s2"));
		questionVO2.getCriterionVOs().add(new AlternativeVO("~4m/s2"));
		questionVO2.getCriterionVOs().add(new AlternativeVO("~3Km/h"));
		questionVO2.getCriterionVOs().add(new AlternativeVO("~10Km/h"));

		questionVO3.getCriterionVOs().add(new AlternativeVO("Teoria da Relatividade Geral"));
		questionVO3.getCriterionVOs().add(new AlternativeVO("Teoria da Relatividade Especial", AlternativeScore.CORRECT));
		questionVO3.getCriterionVOs().add(new AlternativeVO("Teoria de Tudo"));
		questionVO3.getCriterionVOs().add(new AlternativeVO("Teoria da Aceleracao dos Corpos"));
		questionVO3.getCriterionVOs().add(new AlternativeVO("Teoria da Energia Limpa"));

		questionVO4.getCriterionVOs().add(new AlternativeVO("E=m.c2", AlternativeScore.CORRECT));
		questionVO4.getCriterionVOs().add(new AlternativeVO("f(x)=x2y"));
		questionVO4.getCriterionVOs().add(new AlternativeVO("R=r/e3"));
		questionVO4.getCriterionVOs().add(new AlternativeVO("A=v/c"));
		questionVO4.getCriterionVOs().add(new AlternativeVO("m=w.c4"));

		questionVO5.getCriterionVOs().add(new AlternativeVO("~340m/s", AlternativeScore.CORRECT));
		questionVO5.getCriterionVOs().add(new AlternativeVO("~1040m/s"));
		questionVO5.getCriterionVOs().add(new AlternativeVO("~140m/s"));
		questionVO5.getCriterionVOs().add(new AlternativeVO("~3000m/s"));
		questionVO5.getCriterionVOs().add(new AlternativeVO("~10.000m/s"));

		questoes.add(questionVO1);
		questoes.add(questionVO2);
		questoes.add(questionVO3);
		questoes.add(questionVO4);
		questoes.add(questionVO5);

		return questoes;

	}

	/**
	 * @return
	 * @throws JazzOMRJRException
	 */
	protected SAXParser createSAXParser() {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = null;
		try {
			saxParser = saxParserFactory.newSAXParser();
		} catch (ParserConfigurationException e) {
			throw new JazzRuntimeException("erro ao tentar criar um sax parser", e);
		} catch (SAXException e) {
			throw new JazzRuntimeException("erro ao tentar criar um sax parser", e);
		}
		return saxParser;
	}

	/**
	 * @param examVariants
	 * @return
	 * @throws JazzOMRException
	 * @throws IOException
	 * @throws JRException
	 * @throws JazzOMRJRException
	 */
	protected JasperPrint createJasperPrint(List<ExamVariantVO> examVariants){

		List<Long> examVariantKeys = new ArrayList<Long>(examVariants.size());
		for (ExamVariantVO examVariantVO : examVariants) {
			examVariantKeys.add(examVariantVO.getPK());
		}

		HashMap<String, Object> parameters = getParameters(examVariantKeys);

		JasperReport jasperReport = getJasperReport("/reports/ExamReport.jasper");

		JasperPrint jasperPrint = fillReport(parameters, jasperReport);

		return jasperPrint;
	}

	/**
	 * @param parameters
	 * @param jasperReport
	 * @return
	 * @throws JRException
	 * @throws JazzOMRJRException
	 */
	protected JasperPrint fillReport(Map<String, Object> parameters, JasperReport jasperReport) {
		JasperPrint jasperPrint;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

			//logQuery(parameters, connection);
			
		} catch (SQLException e) {
			throw new JazzRuntimeException("Erro ao tentar acessar recurso jdbc!", e);
		} catch (JRException e) {
			throw new JazzRuntimeException("Erro ao preencher relatorio", e);
		} finally {
			close(connection);
		}
		return jasperPrint;
	}


	/**
	 * @param parameters
	 * @param connection
	 * @throws SQLException
	 */
	protected void logQuery(HashMap<String, Object> parameters, Connection connection) throws SQLException {
		Statement st = connection.createStatement();
		String strQuery = (String) parameters.get("pReportQuery");
		
		boolean achouAlgo = false;
		
		boolean ok = st.execute(strQuery);
		
		if(ok){
			ResultSet rs = st.getResultSet();
			
			while(rs.next()){
				achouAlgo=true;
				String strResumo = rs.getString("e_description");
				System.out.println(strResumo);
			}
			
		}
		
		if(achouAlgo){
			System.out.println("###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU");
			System.out.println("###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU");
			System.out.println("###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU");
			System.out.println("###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU");
			System.out.println("###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU");
			System.out.println("###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU");
			System.out.println("###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU###ACHOU");
		}
		
	}

	/**
	 * @param reportName TODO
	 * @return
	 * @throws JazzOMRException
	 * @throws JRException
	 */
	protected JasperReport getJasperReport(String reportName)  {
		InputStream jasperCompiled = this.getClass().getResourceAsStream(reportName);

		JasperReport jasperReport;
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(jasperCompiled);
		} catch (JRException e) {
			throw new JazzRuntimeException("Erro ao tentar carregar objeto de relatorio!", e);
		}
		return jasperReport;
	}

	/**
	 * @param examVariantPKs
	 * @return
	 * @throws JazzOMRException
	 * @throws IOException
	 */
	protected HashMap<String, Object> getParameters(Collection<Long> examVariantPKs){
		BufferedImage bi = loadImage("/reports/logo-cliente.png");

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		IQRCodeGenerator qrCodeGen = new JazzQRCodeGenerator();

		String reportQuery = getReportQuery(examVariantPKs, "exv.pk", REPORT_QUERY_EXAM_VARIANT);

		parameters.put("qrCodeGen", qrCodeGen);
		parameters.put("imgLogoEscola", bi);
		parameters.put("pReportQuery", reportQuery);
		parameters.put("examInstancePKs", examVariantPKs);
		parameters.put("SUBREPORT_DIR", "/reports/");
		

		return parameters;
	}

	private String getReportQuery(Collection<Long> examVariantPKs, String chave, String queryPropertie)  {

		String queryReport = jazzOmrProperties.getProperty(queryPropertie);

		StringBuffer buffer = new StringBuffer();

		String virgula = " ";

		for (Long examPK : examVariantPKs) {
			buffer.append(virgula + examPK);
			virgula = ", ";
		}
		String whereClause = " where " + chave + " in ( " + buffer.toString() + " ) ";

		queryReport = MessageFormat.format(queryReport, whereClause);

		return queryReport;
	}

	/**
	 * @param connection
	 */
	protected void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				log.warn("Erro ao tentar fechar conexao", e);
			}
		}
	}

	/**
	 * @param i
	 * @param nome
	 * @return
	 */
	protected PessoaVO criaPessoa(Long i, String nome) {

		PessoaVO pessoaVO = pessoasMapGambi.get(i);

		if (pessoaVO == null) {
			pessoaVO = hibernateTemplate.get(PessoaVO.class, i);
			if (pessoaVO != null)
				pessoasMapGambi.put(i, pessoaVO);
		}

		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
			pessoaVO.setNome(nome);
			pessoaVO.setLogin(nome);
			hibernateTemplate.saveOrUpdate(pessoaVO);
			pessoasMapGambi.put(pessoaVO.getPK(), pessoaVO);
		}

		return pessoaVO;
	}

	/**
	 * @param jp
	 * @return
	 * @throws JazzOMRJRException
	 * @throws JRException
	 */
	protected byte[] exportToPDF(JasperPrint jp) {

		ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();

		JRPdfExporter exporter = new JRPdfExporter();

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baosPDF);

		try {
			exporter.exportReport();
		} catch (JRException e) {
			throw new JazzRuntimeException("Erro ao tentar exportar relatorio!", e);
		}

		byte[] pdfBytes = null;
		try {
			baosPDF.flush();
			pdfBytes = baosPDF.toByteArray();
			baosPDF.close();

		} catch (IOException e) {
			throw new JazzRuntimeException("Erro ao tentar exportar relatorio!", e);
		}

		return pdfBytes;
	}

	/**
	 * @param jp
	 * @return
	 * @throws JazzOMRJRException
	 * @throws JRException
	 */
	protected byte[] exportToXML(JasperPrint jp)  {

		ByteArrayOutputStream baosXML = new ByteArrayOutputStream();

		JRXmlExporter exporter = new JRXmlExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baosXML);
		exporter.setParameter(JRXmlExporterParameter.IS_EMBEDDING_IMAGES, Boolean.FALSE);
		try {
			exporter.exportReport();
		} catch (JRException e1) {
			throw new JazzRuntimeException("Erro ao tentar exportar relatorio", e1);
		}

		byte[] xmlBytes = null;
		try {
			baosXML.flush();
			xmlBytes = baosXML.toByteArray();
			baosXML.close();
		} catch (IOException e) {
			throw new JazzRuntimeException("Erro ao tentar exportar relatorio", e);
		}

		return xmlBytes;
	}

	/**
	 * @param imageFile
	 *          TODO
	 * @return
	 * @throws JazzOMRException
	 * @throws IOException
	 */
	protected BufferedImage loadImage(String imageFile) {
		InputStream is = this.getClass().getResourceAsStream(imageFile);

		BufferedImage bi;
		try {
			bi = ImageIO.read(is);
		} catch (IOException e) {
			throw new JazzRuntimeException("Erro ao tentar carregar imagem (" + imageFile + ")!", e);
		}
		return bi;
	}

	Map<Long, PessoaVO> pessoasMapGambi = new HashMap<Long, PessoaVO>();


	
	
	
	
	
	
	
	
	
	/**
	 * 
	 */
	public void resetHelper(){
		
		setProgressCount(-1);
		
	}
	

	/**
	 * @param examsSels
	 * @param lNumeroParticipacoes
	 * @return
	 */
	public boolean getIsPeopleAndExamSelecteds() {
		
		Integer examsSels = getExamSelecionados().size();
		
		Integer nPpessoas = groupPessoas().size();
		
		return examsSels>=1 && nPpessoas>=1;
	}
	
	


	/**
	 * @param examsSels
	 * @param lNumeroParticipacoes
	 * @return
	 */
	public boolean getHasExamPDF(EventVO eventVO) {
		
		return eventBusiness.hasExamPDF(eventVO);
		
	}
	

	/**
	 * @return
	 */
	protected Set<PessoaVO> groupPessoas() {
		Set<PessoaVO> pessoasTotais = new HashSet<PessoaVO>();
		pessoasTotais.addAll(getPessoasSelecionadas());
		List<GrupoVO> gruposSelecionados = getGruposSelecionados();
		
		for (GrupoVO grupoVO : gruposSelecionados) {
			
			grupoVO = grupoBusiness.findByPK(grupoVO, "grupo_com_pessoas");
			
			pessoasTotais.addAll(grupoVO.getPessoas());
		}
		return pessoasTotais;
	}
	
	
	
	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public String getPessoaLogin() {
		return pessoaLogin;
	}

	public void setPessoaLogin(String pessoaLogin) {
		this.pessoaLogin = pessoaLogin;
	}

	public String getPessoaEmail() {
		return pessoaEmail;
	}

	public void setPessoaEmail(String pessoaEmail) {
		this.pessoaEmail = pessoaEmail;
	}

	public String getPessoaNome() {
		return pessoaNome;
	}

	public void setPessoaNome(String pessoaNome) {
		this.pessoaNome = pessoaNome;
	}

	public String getPessoaTelefone() {
		return pessoaTelefone;
	}

	public void setPessoaTelefone(String pessoaTelefone) {
		this.pessoaTelefone = pessoaTelefone;
	}



	/**
	 * @return the maxResults
	 */
	public Integer getMaxResults() {
		return maxResults;
	}


	/**
	 * @param maxResults the maxResults to set
	 */
	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}


	/**
	 * @return the gruposSelecionados
	 */
	public List<GrupoVO> getGruposSelecionados() {
		return gruposSelecionados;
	}


	/**
	 * @return the pessoasSelecionadas
	 */
	public List<PessoaVO> getPessoasSelecionadas() {
		return pessoasSelecionadas;
	}

	public String actionPesquisa(){
		
		
		return "";
	}
	
	/**
	 * @return the gruposDisponiveis
	 */
	public List<GrupoVO> getGruposDisponiveis() {
		
		ExtraArgumentsDTO argumentsDTO = new ExtraArgumentsDTO();
		argumentsDTO.setFirstResult(0);
		argumentsDTO.setMaxResults(maxResults);
		//TODO ORDENAR COMBOS
		List<GrupoVO> gruposEncontrados = grupoBusiness.findGrupoVO(groupDescription, null, null, null, null, StatusEnum.ACTIVE,argumentsDTO);
		gruposEncontrados.removeAll(getGruposSelecionados());
		
		return gruposEncontrados;
	}

	public void setGruposDisponiveis(List<GrupoVO> grupos) {}
	
	
	/**
	 * @return the pessoasDisponiveis
	 */
	public List<PessoaVO> getPessoasDisponiveis() {
		ExtraArgumentsDTO argumentsDTO = new ExtraArgumentsDTO();
		argumentsDTO.setFirstResult(0);
		argumentsDTO.setMaxResults(maxResults);
		
		List<PessoaVO> pessoasEncontradas = pessoaBusiness.findPessoaVO(pessoaLogin, pessoaEmail, pessoaNome, pessoaTelefone, null, null, null, null, StatusEnum.ACTIVE, argumentsDTO);
		pessoasEncontradas.removeAll(getPessoasSelecionadas());
		
		return pessoasEncontradas;
		
	}

	public void setPessoasDisponiveis(List<PessoaVO> pessoas) {}
	

	/**
	 * @param gruposSelecionados the gruposSelecionados to set
	 */
	public void setGruposSelecionados(List<GrupoVO> gruposSelecionados) {
		this.gruposSelecionados = gruposSelecionados;
	}


	/**
	 * @param pessoasSelecionadas the pessoasSelecionadas to set
	 */
	public void setPessoasSelecionadas(List<PessoaVO> pessoasSelecionadas) {
		this.pessoasSelecionadas = pessoasSelecionadas;
	}

	/**
	 * @return the maxQuestions
	 */
	public Integer getMaxQuestions() {
		return maxQuestions;
	}

	/**
	 * @return the maxAlternatives
	 */
	public Integer getMaxAlternatives() {
		return maxAlternatives;
	}

	/**
	 * @return the numeroDeVariacoes
	 */
	public Integer getNumeroDeVariacoes() {
		return numeroDeVariacoes;
	}

	/**
	 * @param maxQuestions the maxQuestions to set
	 */
	public void setMaxQuestions(Integer maxQuestions) {
		this.maxQuestions = maxQuestions;
	}

	/**
	 * @param maxAlternatives the maxAlternatives to set
	 */
	public void setMaxAlternatives(Integer maxAlternatives) {
		this.maxAlternatives = maxAlternatives;
	}

	/**
	 * @param numeroDeVariacoes the numeroDeVariacoes to set
	 */
	public void setNumeroDeVariacoes(Integer numeroDeVariacoes) {
		this.numeroDeVariacoes = numeroDeVariacoes;
	}


	/**
	 * @return the examDisponiveis
	 */
	public List<ExamVO> getExamDisponiveis() {
		ExtraArgumentsDTO argumentsDTO = new ExtraArgumentsDTO();
		argumentsDTO.setFirstResult(0);
		argumentsDTO.setMaxResults(maxResults);
		
		List<ExamVO> examsEncontrados = examBusiness.findExamVO(examDescription, null, null, null, null, StatusEnum.ACTIVE,argumentsDTO);
		examsEncontrados.removeAll(getExamSelecionados());
		
		return examsEncontrados;
		
	}

	public void setExamDisponiveis(List<ExamVO> exms) {
		
	}
	/**
	 * @return the examSelecionados
	 */
	public List<ExamVO> getExamSelecionados() {
		return examSelecionados;
	}

	/**
	 * @param examSelecionados the examSelecionados to set
	 */
	public void setExamSelecionados(List<ExamVO> examSelecionados) {
		this.examSelecionados = examSelecionados;
	}


	/**
	 * @return the examDescription
	 */
	public String getExamDescription() {
		return examDescription;
	}


	/**
	 * @param examDescription the examDescription to set
	 */
	public void setExamDescription(String examDescription) {
		this.examDescription = examDescription;
	}

	/**
	 * @return the progressCount
	 */
	public Integer getProgressCount() {
		return progressCount;
	}


	/**
	 * @param progressCount the progressCount to set
	 */
	public void setProgressCount(Integer progressCount) {
		this.progressCount = progressCount;
	}

	/**
	 * Converte as exceções apontadas em validações automáticas em mensagens
	 * padrão JSF
	 * 
	 * @param e
	 */
	protected void parseViolations(ConstraintViolationException e) {
		FacesContext context = FacesContext.getCurrentInstance();
		
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		
		for (ConstraintViolation<?> constraintViolation : violations) {
			context.addMessage(getClientID(constraintViolation), createFacesMessage(constraintViolation));
		}
		
	}
	
	
	/**
	 * Determina o clientId para o constraintviolation capturado
	 * @param constraintViolation
	 * @return
	 */
	protected String getClientID(ConstraintViolation<?> constraintViolation) {
		return getFrmManutencao()+constraintViolation.getPropertyPath().toString();
	}
	
	/**
	 * Retorna o nome do formulário com os campos de manutenção
	 * @return
	 */
	protected String getFrmManutencao() {
		return "frmManutencao:";
	}
	
	/**
	 * Cria uma FacesMessage a partir de uma constraintviolation
	 * @param constraintViolation
	 * @return
	 */
	protected FacesMessage createFacesMessage(ConstraintViolation<?> constraintViolation) {
		return new FacesMessage(constraintViolation.getMessage());
	}
}