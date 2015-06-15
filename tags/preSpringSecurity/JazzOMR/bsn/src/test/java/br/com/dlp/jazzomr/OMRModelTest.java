/**
 * 
 */
package br.com.dlp.jazzomr;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
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

import javax.imageio.ImageIO;
import javax.sql.DataSource;
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
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import br.com.dlp.jazzomr.application.result.CriterionResultVO;
import br.com.dlp.jazzomr.event.EventVO;
import br.com.dlp.jazzomr.event.ParticipationVO;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.exam.ExamVariantVO;
import br.com.dlp.jazzomr.exam.QuestionnaireVO;
import br.com.dlp.jazzomr.exam.coordinate.CriterionCoordinateVO;
import br.com.dlp.jazzomr.exceptions.JazzOMRException;
import br.com.dlp.jazzomr.exceptions.JazzOMRParseException;
import br.com.dlp.jazzomr.jr.handler.JazzQRCodeGenerator;
import br.com.dlp.jazzomr.jr.util.IQRCodeGenerator;
import br.com.dlp.jazzomr.person.PessoaVO;
import br.com.dlp.jazzomr.poc.ExamInstanceXMLHandler;
import br.com.dlp.jazzomr.poc.JazzOMRImageParser;
import br.com.dlp.jazzomr.question.AbstractCriterionVO;
import br.com.dlp.jazzomr.question.AlternativeScore;
import br.com.dlp.jazzomr.question.AlternativeVO;
import br.com.dlp.jazzomr.question.QuestionVO;

@ContextConfiguration(locations = "/ApplicationContext.xml")
@Test
public class OMRModelTest extends AbstractTestNGSpringContextTests {

	public static final String JAZZOMR_PROPERTIES = "reportQueries.properties";
	public static final String REPORT_QUERY_EXAM_VARIANT = "reportQueryExamVariant";
	public static final String REPORT_QUERY_EVENT = "reportQueryEvent";
	private static final Logger log = LoggerFactory.getLogger(OMRModelTest.class);

	@Autowired
	@Qualifier("jazzOmrProperties")
	private Properties jazzOmrProperties;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private HibernateTemplate hibernateTemplate;


	@Test
	public void testParticipation() {
		ParticipationVO participationVO = hibernateTemplate.get(ParticipationVO.class, 10l);

		System.out.println("zubalele:" + participationVO);

		System.out.println("zubalele:" + participationVO);
	}


	private void printProvas(Set<EventVO> eventos) {

		long now = System.currentTimeMillis();
		JasperPrint jp = createJasperPrintE(eventos);

		byte[] pdfReport;
		try {
			pdfReport = exportToPDF(jp);
		} catch (JazzOMRJRException e) {
			throw new RuntimeException("erro", e);
		}

		printImages(jp);

		try {
			FileOutputStream fos = new FileOutputStream("prova.pdf");
			fos.write(pdfReport);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("erro", e);
		} catch (IOException e) {
			throw new RuntimeException("erro", e);
		}

		printEventos(eventos);
	}

	/**
	 * @param jp
	 */
	protected void printImages(JasperPrint jp) {
		try {

			int pageCount = jp.getPages().size();

			for (int i = 0; i < pageCount; i++) {

				File file = new File("result/pagina_" + (i + 1) + "."+ JazzOMRImageParser.IMG_EXT);

				JRGraphics2DExporter graphics2dExporter = new JRGraphics2DExporter();
				BufferedImage bi = new BufferedImage(600, 800, BufferedImage.TYPE_4BYTE_ABGR);
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

	private JasperPrint createJasperPrintE(Set<EventVO> eventos) {

		HashMap<String, Object> parameters = mountParametersEventReport(eventos);

		JasperReport jasperReport;
		try {
			jasperReport = getJasperReport();
		} catch (JazzOMRException e) {
			throw new RuntimeException("erro", e);

		}

		JasperPrint jasperPrint;
		try {
			jasperPrint = fillReport(parameters, jasperReport);
		} catch (JazzOMRException e) {
			throw new RuntimeException("erro", e);
		}

		return jasperPrint;

	}

	/**
	 * @param eventos
	 * @return
	 */
	protected HashMap<String, Object> mountParametersEventReport(Set<EventVO> eventos) {
		List<Long> eventosKeys = new ArrayList<Long>(eventos.size());

		for (EventVO evento : eventos) {
			eventosKeys.add(evento.getPK());
		}

		BufferedImage bi;
		try {
			bi = loadImage("/reports/logo-cliente.png");
		} catch (JazzOMRException e) {
			throw new RuntimeException("erro", e);
		}

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		IQRCodeGenerator qrCodeGen = new JazzQRCodeGenerator();

		String reportQuery;
		try {
			reportQuery = getReportQuery(eventosKeys, " eve.pk ", OMRModelTest.REPORT_QUERY_EVENT);
		} catch (JazzOMRException e1) {
			throw new RuntimeException("erro", e1);
		}

		parameters.put("qrCodeGen", qrCodeGen);
		parameters.put("imgLogoEscola", bi);
		parameters.put("pReportQuery", reportQuery);
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
	 * @param eventos
	 */
	protected void printEventos(Set<EventVO> eventos) {
		for (EventVO eventVO : eventos) {

			for (ParticipationVO participationVO : eventVO.getParticipations()) {

				ExamVariantVO examVariantVO = participationVO.getExamVariantVO();
				CriterionCoordinateVO questionCoordinateVO = (CriterionCoordinateVO) examVariantVO.getCriterionCoordinates().toArray()[1];
				QuestionVO questionVO = questionCoordinateVO.getQuestionVO();
				System.out.println(questionVO.getDescription() + " " + questionCoordinateVO.getQuestionOrder() + " " + examVariantVO.getPK());

			}

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
	protected void logExportedFiles(byte[] pdfBytes, byte[] xmlBytes, String pdfName, String xmlName) throws JazzOMRException {
		
		
		
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
			throw new JazzOMRException("erro ao tentar montar relatorio resultado do XML!", e);
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
	protected void writeToFile(byte[] bytes, String name) throws JazzOMRException {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(name);
			fos.write(bytes);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			throw new JazzOMRException("erro ao tentar logar resultado de exportacao de relatorio!", e);
		} catch (IOException e) {
			throw new JazzOMRException("erro ao tentar logar resultado de exportacao de relatorio!", e);
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

	private void registrarParticipacoes(Set<EventVO> eventos, Set<PessoaVO> pessoas, Set<ExamVariantVO> examVariants) {

		Iterator<ExamVariantVO> itExVar = IteratorUtils.loopingIterator(examVariants);

		for (EventVO eventVO : eventos) {
			for (PessoaVO pessoaVO : pessoas) {
				ParticipationVO participationVO = new ParticipationVO();

				participationVO.setPessoaVO(pessoaVO);

				ExamVariantVO nowExamVariant = itExVar.next();
				participationVO.setExamVariantVO(nowExamVariant);

				eventVO.getParticipations().add(participationVO);
			}
		}
	}

	private Set<PessoaVO> montarParticipantes() {

		Set<PessoaVO> pessoaVOs = new HashSet<PessoaVO>();

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

	private Set<EventVO> montarEventos() {

		Set<EventVO> eventVOs = new HashSet<EventVO>();

		EventVO eventVO = new EventVO();
		eventVO.setDescription("Exame final do 2. semestre. Turma 2");
		eventVO.setDtInicio(new Date());
		eventVO.setDtFim(new Date());
		eventVOs.add(eventVO);

		return eventVOs;
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
	protected SAXParser createSAXParser() throws JazzOMRJRException {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = null;
		try {
			saxParser = saxParserFactory.newSAXParser();
		} catch (ParserConfigurationException e) {
			throw new JazzOMRJRException("erro ao tentar criar um sax parser", e);
		} catch (SAXException e) {
			throw new JazzOMRJRException("erro ao tentar criar um sax parser", e);
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
	protected JasperPrint createJasperPrint(Set<ExamVariantVO> examVariants) throws JazzOMRException {

		List<Long> examVariantKeys = new ArrayList<Long>(examVariants.size());
		for (ExamVariantVO examVariantVO : examVariants) {
			examVariantKeys.add(examVariantVO.getPK());
		}

		HashMap<String, Object> parameters = getParameters(examVariantKeys);

		JasperReport jasperReport = getJasperReport();

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
	protected JasperPrint fillReport(HashMap<String, Object> parameters, JasperReport jasperReport) throws JazzOMRException {
		JasperPrint jasperPrint;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

		} catch (SQLException e) {
			throw new JazzOMRJRException("Erro ao tentar acessar recurso jdbc!", e);
		} catch (JRException e) {
			throw new JazzOMRJRException("Erro ao preencher relatorio", e);
		} finally {
			close(connection);
		}
		return jasperPrint;
	}

	/**
	 * @return
	 * @throws JazzOMRException
	 * @throws JRException
	 */
	protected JasperReport getJasperReport() throws JazzOMRException {
		InputStream jasperCompiled = this.getClass().getResourceAsStream("/reports/ExamReport.jasper");

		JasperReport jasperReport;
		try {
			jasperReport = (JasperReport) JRLoader.loadObject(jasperCompiled);
		} catch (JRException e) {
			throw new JazzOMRParseException("Erro ao tentar carregar objeto de relatorio!", e);
		}
		return jasperReport;
	}

	/**
	 * @param examVariantPKs
	 * @return
	 * @throws JazzOMRException
	 * @throws IOException
	 */
	protected HashMap<String, Object> getParameters(Collection<Long> examVariantPKs) throws JazzOMRException {
		BufferedImage bi = loadImage("/reports/logo-cliente.png");

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		IQRCodeGenerator qrCodeGen = new JazzQRCodeGenerator();

		String reportQuery = getReportQuery(examVariantPKs, "exv.pk", OMRModelTest.REPORT_QUERY_EXAM_VARIANT);

		parameters.put("qrCodeGen", qrCodeGen);
		parameters.put("imgLogoEscola", bi);
		parameters.put("pReportQuery", reportQuery);
		parameters.put("examInstancePKs", examVariantPKs);

		return parameters;
	}

	private String getReportQuery(Collection<Long> examVariantPKs, String chave, String queryPropertie) throws JazzOMRException {

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
	protected byte[] exportToPDF(JasperPrint jp) throws JazzOMRJRException {

		ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();

		JRPdfExporter exporter = new JRPdfExporter();

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baosPDF);

		try {
			exporter.exportReport();
		} catch (JRException e) {
			throw new JazzOMRJRException("Erro ao tentar exportar relatorio!", e);
		}

		byte[] pdfBytes = null;
		try {
			baosPDF.flush();
			pdfBytes = baosPDF.toByteArray();
			baosPDF.close();

		} catch (IOException e) {
			throw new JazzOMRJRException("Erro ao tentar exportar relatorio!", e);
		}

		return pdfBytes;
	}

	/**
	 * @param jp
	 * @return
	 * @throws JazzOMRJRException
	 * @throws JRException
	 */
	protected byte[] exportToXML(JasperPrint jp) throws JazzOMRJRException {

		ByteArrayOutputStream baosXML = new ByteArrayOutputStream();

		JRXmlExporter exporter = new JRXmlExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baosXML);
		exporter.setParameter(JRXmlExporterParameter.IS_EMBEDDING_IMAGES, Boolean.FALSE);
		try {
			exporter.exportReport();
		} catch (JRException e1) {
			throw new JazzOMRJRException("Erro ao tentar exportar relatorio", e1);
		}

		byte[] xmlBytes = null;
		try {
			baosXML.flush();
			xmlBytes = baosXML.toByteArray();
			baosXML.close();
		} catch (IOException e) {
			throw new JazzOMRJRException("Erro ao tentar exportar relatorio", e);
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
	protected BufferedImage loadImage(String imageFile) throws JazzOMRException {
		InputStream is = OMRModelTest.class.getResourceAsStream(imageFile);

		BufferedImage bi;
		try {
			bi = ImageIO.read(is);
		} catch (IOException e) {
			throw new JazzOMRParseException("Erro ao tentar carregar imagem (" + imageFile + ")!", e);
		}
		return bi;
	}

	Map<Long, PessoaVO> pessoasMapGambi = new HashMap<Long, PessoaVO>();

}
