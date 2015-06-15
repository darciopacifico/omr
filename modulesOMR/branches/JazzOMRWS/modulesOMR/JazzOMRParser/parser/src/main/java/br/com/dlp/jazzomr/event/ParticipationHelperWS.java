package br.com.dlp.jazzomr.event;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.imageio.ImageIO;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.JRXmlExporterParameter;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import br.com.dlp.framework.exception.JazzBusinessException;
import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.jazzomr.JazzOMRJRException;
import br.com.dlp.jazzomr.empresa.RelatorioVO;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.exam.QuestionnaireVO;
import br.com.dlp.jazzomr.exceptions.JazzOMRException;
import br.com.dlp.jazzomr.jr.handler.JazzQRCodeGenerator;
import br.com.dlp.jazzomr.jr.util.IQRCodeGenerator;
import br.com.dlp.jazzomr.omr.OMRCoordinatesVO;
import br.com.dlp.jazzomr.parser.ExamInstanceXMLHandlerWS;
import br.com.dlp.jazzomr.person.PessoaVO;
import br.com.dlp.jazzomr.question.CriterionVO;
import br.com.dlp.jazzomr.question.QuestionVO;
import br.com.jazz.jrds.JazzJRDataSource;

/**
 * Gera pdfs de provas e registra gabarito de coordenadas
 * 
 * @author darcio
 * 
 */
@Component
public class ParticipationHelperWS {

	private static final Logger log = LoggerFactory.getLogger(ParticipationHelperWS.class);

	/**
	 * Construtor padrao, apenas formalidade do bean
	 */
	public ParticipationHelperWS() {
	}

	/**
	 * @param eventName 
	 * @param numeroDeVariacoes
	 * @param templateProvaJR TODO
	 * @param relatorioVO2 TODO
	 * @param grupos
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = JazzBusinessException.class)
	public EventVO processParticipations(String eventName, ExamVO examVO, List<PessoaVO> pessoas, Integer numeroDeVariacoes, JasperReport templateProvaJR) throws JazzBusinessException {

		// processa as coordenadas das questoes, alternativas e dissertativas
		EventVO eventVO = createEventVO(eventName,examVO, pessoas,numeroDeVariacoes);

		//Map<String, OMRCoordinatesVO> coordinateMap = createCoordinateMap(examVO,numeroDeVariacoes); 
				
		EventVO eventoReturn = fillExamVariantsCoordinates(eventVO, numeroDeVariacoes, templateProvaJR);
		
		
		return eventoReturn;
	}

	/**
	 * @param coordinateMap
	 * @param questionnaireVO
	 * @param questionVO
	 * @param criterionVO
	 * @return
	 */
	protected String getCoordinateKey(Map<String, OMRCoordinatesVO> coordinateMap, QuestionnaireVO questionnaireVO, QuestionVO questionVO, CriterionVO criterionVO, Integer examVar) {
		Long questionPK = questionnaireVO.getPK();
		Long pergPK = questionVO.getPK();
		Long critPK = criterionVO.getPK();
		
		
		if(questionPK==null || pergPK==null || critPK==null){
			throw new JazzRuntimeException("Os codigos de questionario, pergunta e criterio (alternativo/dissertativo)" +
					" devem ser informados! questionario="+questionPK+" pegunta="+pergPK+" criterio="+critPK+"!");
		}
		
		String coordinateKey = "aco_Key"+"-"+questionPK+"-"+pergPK+"-"+critPK+"-"+examVar;
		if(coordinateMap.containsKey(coordinateKey)){
			throw new JazzRuntimeException("O codigo de coordenada composto por questionario+pergunta+criterio deve ser unico."+
					"Codigo "+coordinateKey+" já foi definido!");
		}
		return coordinateKey;
	}


	/**
	 * 
	 * Registra coordenadas das questoes, alternativas e dissertativas dos exames selecionados.
	 * 
	 * Executa relatório e gera XML Jasper. Parseia este XML procurando pelos elementos que representam os bullets das alternativas e as áreas de resposta das questções dissertativas.
	 * @param eventVO TODO
	 * @param numeroDeVariacoes 
	 * @param templateProvaJR TODO
	 * @param coordinateMap 
	 * @param parentExamVO
	 * @param examVariants
	 * @return 
	 */
	private EventVO fillExamVariantsCoordinates(EventVO eventVO, Integer numeroDeVariacoes, JasperReport templateProvaJR) {
		
		// criar jasperprint e exportar para xml e pdf
		JasperPrint jp = createJasperPrint(eventVO,numeroDeVariacoes, templateProvaJR);
		byte[] xmlBytes = exportToXML(jp);

		DataHandler dhPDF = exportToDataHandlerPDF(jp);
		eventVO.setDataHandlerProva(dhPDF);
		
		
		

		logReport(jp, xmlBytes);
		
		// parsear xml resultado e popupar coordenadas
		SAXParser saxParser = createSAXParser();

		ExamInstanceXMLHandlerWS examInstanceXMLHandler = new ExamInstanceXMLHandlerWS(eventVO);

		ByteArrayInputStream xmlBais = new ByteArrayInputStream(xmlBytes);

		try {
			saxParser.parse(xmlBais, examInstanceXMLHandler);
		} catch (SAXException e) {
			throw new JazzRuntimeException("erro ao tentar parsear xml resultado", e);
		} catch (IOException e) {
			throw new JazzRuntimeException("erro ao tentar parsear xml resultado", e);
		}

		
		return eventVO;
	}

	/**
	 * 
	 * @param jp
	 * @return
	 */
	private DataHandler exportToDataHandlerPDF(JasperPrint jp) {
		ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();

		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baosPDF);
				
		try {
			exporter.exportReport();
		} catch (JRException e1) {
			throw new JazzRuntimeException("Erro ao tentar exportar relatorio", e1);
		}

		byte[] xmlBytes = null;
		try {
			baosPDF.flush();
			xmlBytes = baosPDF.toByteArray();
			baosPDF.close();
		} catch (IOException e) {
			throw new JazzRuntimeException("Erro ao tentar exportar relatorio", e);
		}

		DataSource source = new ByteArrayDataSource(xmlBytes, "application/pdf");
		DataHandler dataHandler = new DataHandler(source);
		
		return dataHandler;
	}

	protected void logReport(JasperPrint jp, byte[] xmlBytes) {
		try {
			
			FileOutputStream fos = new FileOutputStream("provas.xml");
			fos.write(xmlBytes);
			fos.flush();
			fos.close();
			JasperExportManager.exportReportToPdfFile(jp, "provas.pdf");
		} catch (JRException e1) {
			log.error("Erro ao tentar gerar pdf",e1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	/**
	 * @return
	 * @throws JazzOMRJRException
	 */
	private SAXParser createSAXParser() {
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
	 * Monta mapa de parametros para JasperReports
	 * @param numeroDeVariacoes 
	 * 
	 * @param examVariants
	 * @return
	 */
	private HashMap<String, Object> getJRParameters(Integer numeroDeVariacoes) {

		IQRCodeGenerator qrCodeGen = new JazzQRCodeGenerator();

		BufferedImage logoImage = loadImage("/reports/logo-cliente.png");

		// monta mapa de parametros, com as queries, variáveis p/ filtro etc
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("qrCodeGen", qrCodeGen);
		params.put("examVariants", numeroDeVariacoes);
		params.put("imgLogoEscola", logoImage);
		params.put("SUBREPORT_DIR", "/reports/");

		return params;
	}

	/**
	 * @param eventVO TODO
	 * @param numeroDeVariacoes 
	 * @param templateProvaJR TODO
	 * @param parameters
	 * @param relatorioVO
	 * @param examVariants
	 * @return
	 */
	private JasperPrint createJasperPrint(EventVO eventVO, Integer numeroDeVariacoes, JasperReport templateProvaJR) {
		JasperPrint jasperPrint;
		try {

			
			Collection eventos = new ArrayList();
			eventos.add(eventVO);
			
			Map<String, Object> dsParameters = new HashMap<String, Object>();
			
			JazzJRDataSource jrDataSourceProvider = new JazzJRDataSource(eventos, EventVO.class,dsParameters);

			HashMap<String, Object> parameters = getJRParameters(numeroDeVariacoes);

			jasperPrint = JasperFillManager.fillReport(templateProvaJR, parameters, jrDataSourceProvider);

		} catch (JRException e) {
			throw new JazzRuntimeException("Erro ao preencher relatorio", e);
		}

		
		return jasperPrint;
	}

	/**
	 * @param eventName 
	 * @param exam
	 * @param pessoas
	 * @param numeroDeVariacoes 
	 * @return
	 */
	protected EventVO createEventVO(String eventName, ExamVO exam, Collection<PessoaVO> pessoas, Integer numeroDeVariacoes) {

		EventVO eventVO = new EventVO();
		eventVO.setDescription(eventName);
		eventVO.setExamVO(exam);
		
		Long ppk = 1l;
		for (PessoaVO pessoaVO : pessoas) {
			ParticipationVO participationVO = new ParticipationVO();
			participationVO.setExamVO(exam);
			participationVO.setPessoaVO(pessoaVO);
			participationVO.setPK(ppk++);
			
			eventVO.getParticipations().put(participationVO.getPK(),participationVO);
			
			Long exvar = (ppk%numeroDeVariacoes)+1;
			participationVO.setExamVariant(exvar.intValue());
			/*
			*/
		}
		
		return eventVO;
	}





	/**
	 * @param jp
	 * @return
	 * @throws JazzOMRJRException
	 * @throws JRException
	 */
	private byte[] exportToXML(JasperPrint jp) {

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
	private BufferedImage loadImage(String imageFile) {
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
	 * @param examsSels
	 * @param lNumeroParticipacoes
	 * @return
	 */
	private boolean getIsPeopleAndExamSelecteds(ExamVO exam, List<PessoaVO> pessoas) {

		return CollectionUtils.isNotEmpty(pessoas) && exam != null;
	}

}