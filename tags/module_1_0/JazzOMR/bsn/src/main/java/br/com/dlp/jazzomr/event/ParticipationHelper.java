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
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

import org.apache.commons.collections.CollectionUtils;
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
import br.com.dlp.framework.dao.JazzDetachedCriteria;
import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.vo.IBaseVO;
import br.com.dlp.framework.vo.ISortable;
import br.com.dlp.jazzomr.JazzOMRJRException;
import br.com.dlp.jazzomr.base.StatusEnum;
import br.com.dlp.jazzomr.empresa.RelatorioVO;
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
import br.com.dlp.jazzomr.question.AlternativeVO;
import br.com.dlp.jazzomr.question.QuestionVO;
import br.com.dlp.jazzomr.results.ImageLogoVO;
import br.com.dlp.jazzomr.results.JRFileVO;

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
				
			//processa cada um dos exames selecionados separadamente
			for (ExamVO examVO : this.getExamSelecionados()) {
				
				//apenas para atualizar exame
				examVO = examBusiness.findByPK(examVO.getPK());
				
				List<ExamVariantVO> examVariants = criarVariacoesExame(examVO, numeroDeVariacoes);
				saveIBaseVOAll(examVariants);
	
				hibernateTemplate.flush();
				
				//processa as coordenadas das variacoes do 
				fillCoordinates(examVO.getRelatorioVO(),examVariants);
				saveIBaseVOAll(examVariants);
				saveIBaseVO(examVO);
	
				/* MONTAR PARTICIPACOES EM EXAMES */
				
				registrarParticipacoes(eventVO, groupPessoas(), examVariants);
	
			}
			saveIBaseVO(eventVO);
			hibernateTemplate.flush();
			
		}
		
		Integer newParts = this.getExamSelecionados().size() * groupPessoas().size();
		
		addMessage(FacesMessage.SEVERITY_INFO, "As participações foram salvas com sucesso!", "");
		
		resetHelper();
		setExamSelecionados(new ArrayList<ExamVO>(0));
		setGruposSelecionados(new ArrayList<GrupoVO>(0));
		setPessoasSelecionadas(new ArrayList<PessoaVO>(0));

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
	
	/**
	 * @param eventVO
	 * @param relatorioVO 
	 * @return
	 */
	protected byte[] createPDFBytes(EventVO eventVO, RelatorioVO relatorioVO) {
		JasperPrint jp = createJasperPrintE(relatorioVO,eventVO);
		
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

	protected JasperPrint createJasperPrintE(RelatorioVO relatorioVO, EventVO... eventos) {

		HashMap<String, Object> parameters = mountParametersEventReport(relatorioVO,eventos);

		JasperPrint jasperPrint = createJasperPrint(relatorioVO, parameters);
		
		return jasperPrint;

	}

	/**
	 * @param relatorioVO 
	 * @param eventos
	 * @return
	 */
	protected HashMap<String, Object> mountParametersEventReport(RelatorioVO relatorioVO, EventVO... eventos) {
		List<Long> eventosKeys = new ArrayList<Long>(eventos.length);

		for (EventVO evento : eventos) {
			eventosKeys.add(evento.getPK());
		}
		
		ImageLogoVO imageLogoVO = pessoaBusiness.getLogoEmpresa();
		
		BufferedImage bi; 
		if(imageLogoVO!=null && imageLogoVO.getImage()!=null){
			bi = imageLogoVO.getImage();
		}else{
			bi= loadImage("/reports/logo-cliente.png");
		}
		
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		IQRCodeGenerator qrCodeGen = new JazzQRCodeGenerator();

		String reportQuery = getReportQuery(relatorioVO,eventosKeys, " eve.pk ", REPORT_QUERY_EVENT);

		parameters.put("qrCodeGen", qrCodeGen);
		parameters.put("imgLogoEscola", bi);
		parameters.put("pReportQuery", reportQuery);
		parameters.put("SUBREPORT_DIR", "/reports/");
		
		
		return parameters;
	}

	/**
	 * Cria query de relatorio de provas, considerando um único layout
	 * @param relatorioVO
	 * @param eventosKeys
	 * @param chave
	 * @param reportQueryEvent
	 * @return
	 */
	protected String getReportQuery(RelatorioVO relatorioVO, List<Long> eventosKeys, String chave, String reportQueryEvent) {
		
		String queryReport = jazzOmrProperties.getProperty(reportQueryEvent);
		
		StringBuffer buffer = new StringBuffer();
		
		String virgula = " ";
		
		for (Long examPK : eventosKeys) {
			buffer.append(virgula + examPK);
			virgula = ", ";
		}
		
		String sqlReportRestriction=" and exa.fk_relatorio = "+relatorioVO.getPK()+" ";
		String whereClause = " where " + chave + " in ( " + buffer.toString() + " ) "+sqlReportRestriction;
		
		queryReport = MessageFormat.format(queryReport, whereClause);
		
		return queryReport;
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
		
		//para não viciar a distribuição das variantes sempre na mesma ordem
		Collections.shuffle(variants);
		
		return variants;
	}


	/**
	 * @param questoesPorTema
	 * @param alternativaPorQuestao
	 * @param randomizar
	 * @param examVariantVO
	 * @param questionnaires
	 */
	protected void fillQuestionCoordinates(boolean randomizar, ExamVariantVO examVariantVO, List<QuestionnaireVO> questionnaires) {
		
		for (QuestionnaireVO questionnaireVO : questionnaires) {

			//as questoes já vêm ordenadas do questionário
			Iterator<QuestionVO> itQuestions = createOrderedIterator(questionnaireVO.getQuestions(), randomizar);

			int ordemQuestao = 1;

			while (itQuestions.hasNext()) {

				QuestionVO questionVO = itQuestions.next();

				List<CriterionCoordinateVO> criterionCoordinateVOs = fillAlternativeCoordinates(questionVO, randomizar, ordemQuestao++);

				examVariantVO.getCriterionCoordinates().addAll(criterionCoordinateVOs);
				
			}
			
		}
	}

	/**
	 * Registra coordenadas das questoes, alternativas e dissertativas dos exames selecionados.
	 * 
	 * Gera XML de relatório Jasper e parseia este XML procurando pelos elementos que representam os bullets das alternativas e as áreas de resposta das questões dissertativas.
	 *  
	 * @param parentExamVO
	 * @param examVariants
	 */
	protected void fillCoordinates(RelatorioVO relatorioVO, List<ExamVariantVO> examVariants) {
		
	
		//cria mapa com os vos que receberão as coordenadas dos criterios alternativos e dissertativos
		Map<Long, CriterionCoordinateVO> mapAlternativeCoords = fillReferenceMaps(examVariants);
		
		// OK criar jasperprint e exportar para xml e pdf
		JasperPrint jp = createJasperPrint(relatorioVO,examVariants);
		byte[] xmlBytes = exportToXML(jp);
				
		
		if(log.isDebugEnabled()){
			byte[] pdfBytes = exportToPDF(jp);
			logExportedFiles(pdfBytes, xmlBytes, "pdfResult.pdf", "pdfResult.xml");
		}
	
		
		// parsear xml resultado e popupar coordenadas
		SAXParser saxParser = createSAXParser();
		ExamInstanceXMLHandler examInstanceXMLHandler = new ExamInstanceXMLHandler(relatorioVO,mapAlternativeCoords);
	
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
	protected Map<Long, CriterionCoordinateVO>  fillReferenceMaps(List<ExamVariantVO> examVariants) {
		
		Map<Long, CriterionCoordinateVO> mapAlternativeCoords = new HashMap<Long, CriterionCoordinateVO>(500);
		
		for (ExamVariantVO examVariantVO : examVariants) {
			
			List<CriterionCoordinateVO> alternatives = examVariantVO.getCriterionCoordinates();
			for (CriterionCoordinateVO criterionCoordinateVO : alternatives) {
				mapAlternativeCoords.put(criterionCoordinateVO.getPK(), criterionCoordinateVO);
			}
			
		}
		
		return mapAlternativeCoords;
		
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

		boolean criticarDuplicidade = false;
		
		//looping iterator, para distribuição das variações de exame
		Iterator<ExamVariantVO> itExVar = IteratorUtils.loopingIterator(examVariants);

		for (PessoaVO pessoaVO : pessoas) {
			ExamVariantVO nowExamVariant = itExVar.next();
			
			if(pessoaExameNaoRegistrado(eventVO, pessoaVO, nowExamVariant)){
				
				ParticipationVO participationVO = new ParticipationVO();
				participationVO.setPessoaVO(pessoaVO);
				participationVO.setExamVariantVO(nowExamVariant);
				eventVO.getParticipations().add(participationVO);
			}
		}
		
	}

	
	/**
	 * 
	 * @param eventVO
	 * @param pessoaVO
	 * @param nowExamVariant
	 * @return
	 */
	private boolean pessoaExameNaoRegistrado(EventVO eventVO, PessoaVO pessoaVO, ExamVariantVO nowExamVariant) {
		
				
		List<ParticipationVO> participations = eventVO.getParticipations();
		
		for (ParticipationVO participationVO : participations) {
			
			PessoaVO evePessoa = participationVO.getPessoaVO();
			ExamVariantVO eveExamVariantVO = participationVO.getExamVariantVO();

			ExamVO eveExamVO = eveExamVariantVO.getExamVO();
			ExamVO examVO = nowExamVariant.getExamVO();
			
			if(evePessoa.getPK().equals(pessoaVO.getPK()) && 
				 eveExamVO.getPK().equals(examVO.getPK()) ){
				
				return false;
				
			}
		}
		
		return true;
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
	 * Cria um iterator para um conjunto de abstractCriterionVOs. Se shuffle = false, ordena os criterions pela ordem utilizada no cadastro
	 * @param criterionVOs
	 * @param shuffle
	 * @return
	 */
	protected Iterator<AbstractCriterionVO> createOrderedIterator(Set<AbstractCriterionVO> criterionVOs, boolean shuffle) {

		List<AbstractCriterionVO> criterionsList = new ArrayList<AbstractCriterionVO>(criterionVOs);
		if(!shuffle){
			Collections.sort(criterionsList,ISortable.sortableComparator);
		}

		return createOrderedIterator(criterionsList, shuffle);
	}

	/**
	 * @param <T>
	 * @param itens
	 * @return
	 */
	protected <T> Iterator<T> createOrderedIterator(List<T> itens, boolean shuffle) {

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
	protected <T> Iterator<T> createOrderedIterator(List<T> alternatives) {
		return createOrderedIterator(alternatives, true);
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
	 * Cria jasperpring a partir do modelo de relatorios informado e das variantes de exame
	 * 
	 * @param relatorioVO Conjunto de relatórios jasper que será utilizado na criação do exame
	 * @param examVariants Variantes do mesmo exame. Cada variante possui diferentes ordens de questões e alternativas
	 * @return
	 */
	protected JasperPrint createJasperPrint(RelatorioVO relatorioVO, List<ExamVariantVO> examVariants){

		HashMap<String, Object> parameters = getParameters(examVariants);

		JasperPrint jasperPrint = createJasperPrint(relatorioVO, parameters);

		return jasperPrint;
	}

	/**
	 * @param examVariants
	 * @return
	 */
	protected HashMap<String, Object> getParameters(List<ExamVariantVO> examVariants) {
		List<Long> examVariantKeys = new ArrayList<Long>(examVariants.size());
		for (ExamVariantVO examVariantVO : examVariants) {
			examVariantKeys.add(examVariantVO.getPK());
		}
		BufferedImage bi = loadImage("/reports/logo-cliente.png");
		
		HashMap<String, Object> parameters1 = new HashMap<String, Object>();
		
		IQRCodeGenerator qrCodeGen = new JazzQRCodeGenerator();
		
		String reportQuery = getReportQuery(examVariantKeys, "exv.pk", REPORT_QUERY_EXAM_VARIANT);
		
		parameters1.put("qrCodeGen", qrCodeGen);
		parameters1.put("imgLogoEscola", bi);
		parameters1.put("pReportQuery", reportQuery);
		parameters1.put("examInstancePKs", examVariantKeys);
		parameters1.put("SUBREPORT_DIR", "/reports/");

		//monta mapa de parametros, com as queries, variáveis p/ filtro etc
		HashMap<String, Object> parameters = parameters1;
		return parameters;
	}

	/**
	 * @param relatorioVO
	 * @param parameters
	 * @return
	 */
	protected JasperPrint createJasperPrint(RelatorioVO relatorioVO, HashMap<String, Object> parameters) {
		//preenche mapa de relatorios. Pode conter subReports, então será carregado no mapa de parâmetros
		Map<String, JasperReport> reportMap = fillReportMap(relatorioVO);
		
		//recupera relatório principal. Se não encontrar lança exceção runtime
		JasperReport jasperReport = getJasperReportPrincipal(relatorioVO, reportMap);

		//integra demais subrelatorios ao mapa de parametros que sera utilizado na chamada do relatorio principal.
		parameters.putAll(reportMap);
		
		//executa relatório
		JasperPrint jasperPrint = fillReport(parameters, jasperReport);
		return jasperPrint;
	}

	/**
	 * @param relatorioVO
	 * @return
	 */
	protected Map<String, JasperReport> fillReportMap(RelatorioVO relatorioVO) {
		
		Map<String, JasperReport> reportMapDirect = new HashMap<String, JasperReport>(2);
		
		JasperReport examReport = loadJasperReport(this.getClass().getResourceAsStream("/reports/ExamReport.jasper"));
		JasperReport questionImgRpt = loadJasperReport(this.getClass().getResourceAsStream("/reports/questionImgRpt.jasper"));
		
		reportMapDirect.put("principal", examReport);
		reportMapDirect.put("questionImgRpt", questionImgRpt);
		
		/*
		RelatorioVO relatorioWithJRs = getRelatorioFetchJRFiles(relatorioVO);
		List<JRFileVO> jrFiles = relatorioWithJRs.getJrFileVOs();
		
		Map<String, JasperReport> reportMap = new HashMap<String, JasperReport>(jrFiles.size());
		
		for (JRFileVO jrFileVO : jrFiles) {
			String nomeRef = jrFileVO.getNome();
			JasperReport jasperReport = getJasperReport(jrFileVO);
			reportMap.put(nomeRef, jasperReport);
		}
		return reportMap;
		*/
		
		return reportMapDirect;
		
	}

	/**
	 * Pesquisa novamente os dados do relatorio informado, mas forçando o fetch dos arquivos jr associados ao mesmo.
	 * @param relatorioVO
	 * @return
	 */
	protected RelatorioVO getRelatorioFetchJRFiles(RelatorioVO relatorioVO) {
		JazzDetachedCriteria criteria = JazzDetachedCriteria.forClass(RelatorioVO.class);
		criteria.add(Restrictions.eq("PK", relatorioVO.getPK()));
		criteria.enableFetchProfile("relatorio_com_jasper");
		List<RelatorioVO> relatorios = hibernateTemplate.findByCriteria(criteria);
		
		RelatorioVO relatorioVO2 = null;
		if(CollectionUtils.isNotEmpty(relatorios)){
			relatorioVO2 = relatorios.get(0);
		}else{
			throw new JazzRuntimeException("O relatório de código '"+relatorioVO.getPK()+"' não foi encontrado!");
		}
		return relatorioVO2;
	}

	/**
	 * @param relatorioVO
	 * @param reportMap
	 * @return
	 */
	protected JasperReport getJasperReportPrincipal(RelatorioVO relatorioVO, Map<String, JasperReport> reportMap) {
		JasperReport jasperReport = reportMap.remove(JRFileVO.RELATORIO_PRINCIPAL);
		
		if(jasperReport==null){
			throw new JazzRuntimeException("O Layout "+relatorioVO.getPK()+" ('"+relatorioVO.getDescription()+"') é inválido! Pelo menos um dos arquivos Jasper deve ser rotulado como 'principal'!");
		}
		return jasperReport;
	}

	
	/**
	 * Carrega relatório jasper contido em JrFileVO
	 * @param jrFileVO
	 * @return
	 */
	protected JasperReport getJasperReport(JRFileVO jrFileVO) {
		
		byte[] reportBytes = jrFileVO.getJasperReport();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(reportBytes);
		
		JasperReport jasperReport = loadJasperReport(bais);
		
		return jasperReport;
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
	 * @param reportName TODO
	 * @return
	 * @throws JazzOMRException
	 * @throws JRException
	 */
	protected JasperReport getJasperReport(String reportName)  {
		InputStream jasperCompiled = this.getClass().getResourceAsStream(reportName);

		return loadJasperReport(jasperCompiled);
	}

	/**
	 * @param jasperCompiled
	 * @return
	 */
	protected JasperReport loadJasperReport(InputStream jasperCompiled) {
		JasperReport jasperReport;
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(jasperCompiled);
		} catch (JRException e) {
			throw new JazzRuntimeException("Erro ao tentar carregar objeto de relatorio!", e);
		}
		return jasperReport;
	}

	/**
	 * 
	 * @param pks
	 * @param chave
	 * @param queryPropertie
	 * @return
	 */
	protected String getReportQuery(Collection<Long> pks, String chave, String queryPropertie)  {

		String queryReport = jazzOmrProperties.getProperty(queryPropertie);

		StringBuffer buffer = new StringBuffer();

		String virgula = " ";

		for (Long examPK : pks) {
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

	Map<String, PessoaVO> pessoasMapGambi = new HashMap<String, PessoaVO>();


	
	
	
	
	
	
	
	
	
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