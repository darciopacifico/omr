package br.com.dlp.jazzomr.poc;

import ij.IJ;
import ij.ImagePlus;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ImageConverter;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import br.com.dlp.framework.dao.JazzDetachedCriteria;
import br.com.dlp.framework.gof.strategy.Strategy;
import br.com.dlp.framework.vo.ISortable;
import br.com.dlp.jazzomr.application.result.CriterionResultVO;
import br.com.dlp.jazzomr.event.ParticipationVO;
import br.com.dlp.jazzomr.exam.ExamOMRMetadataVO;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.exam.coordinate.CriterionCoordinateVO;
import br.com.dlp.jazzomr.exam.coordinate.IOMRMarkable;
import br.com.dlp.jazzomr.exceptions.JazzOMRException;
import br.com.dlp.jazzomr.exceptions.JazzOMRFixRotationException;
import br.com.dlp.jazzomr.exceptions.JazzOMRIdentityException;
import br.com.dlp.jazzomr.exceptions.RefParticleNotFoundException;
import br.com.dlp.jazzomr.omr.EParticleArea;
import br.com.dlp.jazzomr.omr.EParticlePosition;
import br.com.dlp.jazzomr.omr.ImageDocPK;
import br.com.dlp.jazzomr.omr.ImageDocVO;
import br.com.dlp.jazzomr.omr.ParticleTemplateVO;
import br.com.dlp.jazzomr.omr.ParticleVO;
import br.com.dlp.jazzomr.particle.counting.RefParticleFinder;
import br.com.dlp.jazzomr.particle.counting.ThreeRefParticleFinder;
import br.com.dlp.jazzomr.particle.counting.TwoRefParticleFinder;
import br.com.dlp.jazzomr.question.AbstractCriterionVO;
import br.com.dlp.jazzomr.question.AlternativeScore;
import br.com.dlp.jazzomr.question.AlternativeVO;
import br.com.dlp.jazzomr.question.DissertativeVO;
import br.com.dlp.jazzomr.question.QuestionVO;
import br.com.dlp.jazzomr.results.EProcessingState;
import br.com.dlp.jazzomr.results.ImageVO;
import br.com.dlp.jazzomr.results.PayloadVO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/**
 * 
 * @author darcio
 */
@Component
public class JazzOMRImageParser {

	private double maxImageArea = 2000*3000;
	private double raizMaxArea = Math.sqrt(maxImageArea);
	
	public static final String THRESHOLD_HUANG = "Huang";
	public static final String THRESHOLD_DEFAULT = "Default";

	public static final int CRITIC_FONT_SIZE = 8;

	public static final String IMG_EXT = "gif";

	public static final double BORDER_PARTICLE_RADIUS = 1.2;
	public static final int DEF_INTERPOLATION_TYPE = AffineTransformOp.TYPE_BICUBIC;
	public static final int MAX_CIRCULARITY = 1;
	public static final int MEDIDAS_PADRAO = Measurements.SHAPE_DESCRIPTORS + Measurements.CENTER_OF_MASS + Measurements.AREA + Measurements.PERIMETER
			+ Measurements.CENTROID;

	public Logger log = LoggerFactory.getLogger(this.getClass());
	private ImageDocLogger imageDocLogger = new ImageDocLogger();

	private double bulletMinAreaFactor = 0.07;
	private double bulletMaxAreaFactor = 0.95;
	private double bulletMinCirc = 0.32;//ajuste de sensibilidade de reconhecimento de marcas
	
	public static final double REFERENCE_BULLET_WIDTH = 25;

	@Autowired
	private HibernateTemplate hibernateTemplate;

	private List<Strategy<QRCodeReaderParam, String>> strategies;

	private Hashtable hints = new Hashtable();
	
	@Autowired
	@Qualifier("alternativeResultBullet")
	private Comparator<? super CriterionResultVO> altComparator;

	
	/**
	 * Cria DTO contendo os parametros para funcionamento do parseador OMR de imagens
	 * @return
	 */
	public ParticleAnalyzerParamDTO createParticleAnalyzerParams() {
		//tODO: PARAMETRIZAR EM BANCO DE DADOS
		List<ParticleTemplateVO> particleTemplates = getParticleTemplates();

		IdentityRegion identityRegion = new IdentityRegion(0.747,0.015,0.185,0.118);
		
		ParticleAnalyzerParamDTO analyzerParams = new ParticleAnalyzerParamDTO(particleTemplates);
		analyzerParams.setIdentityRegion(identityRegion);
		return analyzerParams;
	}

	/**
	 * Constroi modelo padrao de particulas que sera esperado pelo image parser
	 * @return
	 */
	protected static List<ParticleTemplateVO> getParticleTemplates() {
		//TODO: PARAMETRIZAR MODELOS DE PARTICULAS
		List<ParticleTemplateVO> particleTemplatesList = new ArrayList<ParticleTemplateVO>(2);
		
		particleTemplatesList.add(ParticleTemplateVO.getInstance(EParticleArea.PEQUENA,EParticlePosition.BOTTOM_LEFT));
		particleTemplatesList.add(ParticleTemplateVO.getInstance(EParticleArea.PEQUENA,EParticlePosition.TOP_RIGHT));
		
		return particleTemplatesList;
	}
	
	/**
	 * Prepara ImageDocVO que sera a carga do processamento de imagem.
	 * Grava o payload na base de dados, mesmo que ainda não identificado
	 * @param is
	 * @return
	 * @throws JazzOMRException
	 */
	public ImageDocVO prepareImageProcessing(InputStream is) throws JazzOMRException  {
		ImageDocVO imageDocVO = new ImageDocVO(is);
		
		createPayload(imageDocVO);
		
		return imageDocVO;
	}
	
	
	/**
	 * Prepara ImageDocVO que sera a carga do processamento de imagem.
	 * Grava o payload na base de dados, mesmo que ainda não identificado
	 * @param is
	 * @return
	 * @throws JazzOMRException
	 */
	public ImageDocVO prepareImageProcessing(BufferedImage bi) throws JazzOMRException  {
		ImageDocVO imageDocVO = new ImageDocVO(bi);
		
		createPayload(imageDocVO);
		
		return imageDocVO;
	}

	/**
	 * @param imageDocVO
	 */
	protected void createPayload(ImageDocVO imageDocVO) {
		PayloadVO payloadVO = new PayloadVO();
		payloadVO.setDescricao("Processamento de Imagens de Exames");
		//TODO: IMPLEMENTAR UM ALGORITMO DE HASH QUE SEJA COMUM NO JAVA E NOS SISTEMAS OPERACIONAIS WINDOWS E LINUX PARA FACILITAR DEPURACAO DE CODIGO E OUTROS PROCEDIMENTOS OPERACIONAIS
		payloadVO.setPreProcessImgHash("");
		payloadVO.setProcessingState(EProcessingState.CREATED);
		payloadVO.setSize(imageDocVO.getImgArea());
		
		//salva inicio do estado do processamento
		hibernateTemplate.save(payloadVO);

		imageDocVO.setPayloadVO(payloadVO);
	}
	
	/**
	 * Processa a imagem informada no construtor
	 * 
	 * @throws RefParticleNotFoundException
	 * @throws JazzOMRException
	 * @throws JazzOMRException
	 */
	public void parseImage(ImageDocVO imageDocVO) throws JazzOMRException {

		//recupera modelo de particulas do sistema
		ParticleAnalyzerParamDTO particleAnalyzerParamDTO = createParticleAnalyzerParams();
		
		long now = System.currentTimeMillis();

		//ajusta imagem inicial
		adjustImage(imageDocVO,particleAnalyzerParamDTO);
		
		// encontra as particulas candidatas a particulas de referencia
		// Analise de particulas pura e simples. padrao imagej
		findParticles(imageDocVO, particleAnalyzerParamDTO);

		// Determinar, dentre as particulas encontradas, quais sao as particulas de referencia
		findRefParticles(imageDocVO, particleAnalyzerParamDTO);

		// Ajusta rotacao da imagem original.
		// So e possivel ser executada quando se sabe a posicao das particulas de referencia
		fixImageRotation(imageDocVO, particleAnalyzerParamDTO);

		// Determina onde esta a regiao da imagem com os codigos de barra ou QRCode com a identificacao do documento.
		findIdentityRegion(imageDocVO, particleAnalyzerParamDTO);

		
		
		if(!imageDocVO.getParticipationVO().isProcessTerminated()){
		
			// Le os bullets dos criterios alternativos contidos na imagem
			List<CriterionResultVO> criterionResultVOs = readAlternativeAnswers(imageDocVO, particleAnalyzerParamDTO);
	
			saveResults(criterionResultVOs);
			
			
		}

		if (log.isDebugEnabled()) {
			log.debug("tempo total " + (System.currentTimeMillis() - now));
		}
		
		
	}


	/**
	 * 
	 * @param criterionResultVOs
	 */
	private void saveResults(List<CriterionResultVO> criterionResultVOs) {
		
		
		for (CriterionResultVO criterionResultVO : criterionResultVOs) {
			
			JazzDetachedCriteria criteria = JazzDetachedCriteria.forClass(CriterionResultVO.class);
			
			criteria.add(Restrictions.eq("criterionCoordinateVO", criterionResultVO.getCriterionCoordinateVO()));
			criteria.add(Restrictions.eq("participationVO", criterionResultVO.getParticipationVO()  ));
			
			List<CriterionResultVO> resultVOs = hibernateTemplate.findByCriteria(criteria);
			
			if(CollectionUtils.isEmpty(resultVOs)){
				hibernateTemplate.saveOrUpdate(criterionResultVO);
			}
		}
	}
	/**
	 * Atualiza o payload associado a imagem com estado de sucesso (TERMINATED)
	 * @param imageDocVO
	 * @return 
	 */
	public void updatePayloadVO(ImageDocVO imageDocVO) {
		PayloadVO payloadVO = imageDocVO.getPayloadVO();
		
		//atribui o registro de participacao
		payloadVO.setParticipationVO(imageDocVO.getParticipationVO());
		payloadVO.setPage(imageDocVO.getImageDocPK().getPage());
		
		//atualiza ultima imagem processada
		ImageVO imageVO = new ImageVO();
		imageVO.setImage(imageDocVO.getBufferedImage());
		payloadVO.getImageVOs().clear();
		payloadVO.getImageVOs().add(imageVO);

		//atualiza estado do processamento
		payloadVO.setProcessingState(EProcessingState.TERMINATED);
		
		//atualiza mensagens (apaga stacktrace, caso exista)
		payloadVO.setMessageState("Success!");
		payloadVO.setFullMessageState(null);
		
		//atualiza payload
		hibernateTemplate.saveOrUpdate(payloadVO);
		
	}


	/**
	 * Atualiza PayloadVO com estado de 'exception'
	 * 
	 * @param imageDocVO
	 * @param e
	 * @return
	 */
	public void updatePayloadVO(ImageDocVO imageDocVO, JazzOMRException e) {
		PayloadVO payloadVO = imageDocVO.getPayloadVO();
		
		//atualiza ultima imagem processada
		ImageVO imageVO = new ImageVO();
		imageVO.setImage(imageDocVO.getBufferedImageCopy());
		payloadVO.getImageVOs().clear();
		payloadVO.getImageVOs().add(imageVO);
		
		//atualiza estado do processamento
		payloadVO.setProcessingState(EProcessingState.FAILED);
		
		//captura stackTrace
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		e.printStackTrace(printWriter);
		String fullMessage = writer.toString();
		payloadVO.setFullMessageState(fullMessage);

		//captura mensagem de erro simples
		payloadVO.setMessageState(e.getMessage());
		
		//atualiza payload
		hibernateTemplate.saveOrUpdate(payloadVO);
	}
	
	/**
	 * Le as respostas das questoes contidas na imagem
	 * 
	 * @param particleAnalyzerParamDTO
	 * 
	 * @param examInstance
	 * @param rotatedImage
	 * @return
	 * @throws JazzOMRException
	 */
	protected List<CriterionResultVO> readAlternativeAnswers(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO particleAnalyzerParamDTO) throws JazzOMRException {

		ImageDocPK imageDocPK = imageDocVO.getImageDocPK();

		// encontra as coordenadas das questoes
		Map<QuestionVO, List<CriterionCoordinateVO>> critCoords = findCriterionCoordinates(imageDocPK);

		List<CriterionResultVO> criterionResults = new ArrayList<CriterionResultVO>();
		
		// recupera o participationVO ligado aa imagem
		ParticipationVO participationVO = imageDocVO.getParticipationVO();

		//recupera o examvo ligar aa participacao
		ExamVO examVO = participationVO.getExamVariantVO().getExamVO();

		//******** abre imagem para log ********
		imageDocLogger.openGraphics2D(imageDocVO, particleAnalyzerParamDTO, "5_leituraQuestAlter", "Leitura Questoes e Alternativas", false);
		logPosicaoEsperada(imageDocVO, examVO);

		long time = System.currentTimeMillis();

		// itera questoes encontradas
		
		
		Set<QuestionVO> questionKeys =  critCoords.keySet();
		
		for (QuestionVO questionVO : questionKeys) {
			
			List<CriterionCoordinateVO> coordinateVOs = critCoords.get(questionVO);
			
			List<CriterionResultVO> critQuestResults = createCriterionResults(coordinateVOs,participationVO);
			readAlternatives(imageDocVO, examVO, critQuestResults,questionVO);
			
			criterionResults.addAll(critQuestResults);
		}

		if(log.isDebugEnabled()){
			log.debug("tempo proce imagens " + (System.currentTimeMillis() - time));
		}

		//******** fecha imagem para log ********
		imageDocLogger.closeGraphics2D();

		return criterionResults;

	}

	
	private List<CriterionResultVO> createCriterionResults(List<CriterionCoordinateVO> coordinateVOs, ParticipationVO participationVO) {
		List<CriterionResultVO> criterionResults = new ArrayList<CriterionResultVO>();
		
		for (CriterionCoordinateVO criterionCoordinateVO : coordinateVOs) {
			CriterionResultVO criterionResultVO = new CriterionResultVO();
			criterionResultVO.setCriterionCoordinateVO(criterionCoordinateVO);
			criterionResultVO.setParticipationVO(participationVO);
			criterionResults.add(criterionResultVO);
		}
		
		return criterionResults;
	}

	/**
	 * @param criterionCoordinateVO
	 * @return
	 */
	protected boolean isAlternativa(CriterionCoordinateVO criterionCoordinateVO) {
		return !isDissertativa(criterionCoordinateVO);
	}

	/**
	 * @param imageDocVO
	 * @param examVO
	 * @param questionResultVO
	 * @throws JazzOMRException
	 */
	protected void readAlternatives(ImageDocVO imageDocVO, ExamVO examVO, List<CriterionResultVO> critQuestResults,QuestionVO questionVO) throws JazzOMRException {
		readAlternativeBullets(imageDocVO, examVO, critQuestResults,questionVO);
		
		determineSelectedBullets(critQuestResults, imageDocVO, examVO,questionVO);
	}

	/**
	 * Analisa os resultados de analise de particulas das imagens e determina qual foi a resposta escolhida
	 * @param questionResultVO
	 * @param examVO 
	 * @param imageDocVO 
	 * @throws JazzOMRException 
	 */
	protected void determineSelectedBullets(List<CriterionResultVO> allCriterions, ImageDocVO imageDocVO, ExamVO examVO,QuestionVO questionVO) throws JazzOMRException {

		//cria nova colecao com as alternativas para preservar a original
		List<CriterionResultVO> setAlternatives = new ArrayList<CriterionResultVO>(allCriterions.size());
		setAlternatives.addAll(allCriterions);

		
		//filtra apenas os candidatos mais provaveis
		CollectionUtils.filter(setAlternatives, (Predicate) this.altComparator);

		
		//apenas formaliza a passagem da colecao para uma lista
		ArrayList<CriterionResultVO> criterions = new ArrayList<CriterionResultVO>(setAlternatives.size());
		criterions.addAll(setAlternatives);
		
		//ordena as alternativas de pela maior probabilidade de terem sido as escolhidas
		Collections.sort(criterions, this.altComparator);
				
		//recupera quantas sao as questoes que serao assinaladas. normalmente 1 apenas
		Integer corrects = correctOptions(questionVO,criterions);
		
		//recupera lista de repostas assinaladas
		List<CriterionResultVO> selectedAlternatives =  criterions.subList(0, corrects);

		//marca como checked os mais provaveis candidatos
		for (CriterionResultVO criterionResultVO : selectedAlternatives) {
			criterionResultVO.setChecked(true);
		}
		
		//loga os bullets nao checados
		logUncheckedAlternatives(allCriterions, examVO,imageDocVO);
		
	}

	/**
	 * Loga imagens de bullets não checados. para depuração de codigo
	 * @param allAlternatives
	 * @param selectedAlternatives
	 * @param examVO 
	 * @param imageDocVO 
	 */
	public void logUncheckedAlternatives(List<CriterionResultVO> allAlternatives, ExamVO examVO, ImageDocVO imageDocVO) {
		for (CriterionResultVO alternativeVO : allAlternatives) {
			logCheckedOption(alternativeVO,examVO,imageDocVO);
			imageDocLogger.logBullet(alternativeVO);
		}
	}

	/**
	 * @param questionResultVO
	 * @param alternatives 
	 * @return
	 */
	protected Integer correctOptions(QuestionVO questionVO, ArrayList<CriterionResultVO> alternatives) {
		Integer correctAlternatives = questionVO.countCorrectScore();
		correctAlternatives = correctAlternatives==null?1:correctAlternatives;
		
		
		if(alternatives.size()<correctAlternatives){
			correctAlternatives=alternatives.size();
		}
		
		return correctAlternatives;
	}

	/**
	 * 
	 * @param imageDocPK
	 * @return
	 * @throws JazzOMRException
	 */
	protected ParticipationVO findParticipationVO(ImageDocPK imageDocPK) throws JazzOMRException {

		if (imageDocPK == null) {
			throw new IllegalArgumentException("ImageDocPK informada nao é válida (" + imageDocPK + ")");
		}

		@SuppressWarnings("unchecked")
		List<ParticipationVO> participations = hibernateTemplate.findByNamedParam(" select par " + " from ParticipationVO as par "
				+ "  inner join fetch par.examVariantVO as exv " + "  inner join fetch exv.examVO as exa " + "  inner join fetch exa.examOMRMetadataVO as exm "
				+ "	where	par.PK in (:participationPK) ",

		new String[] { "participationPK" }, new Object[] { imageDocPK.getParticipation() });

		ParticipationVO participationVO = null;

		if (participations.size() > 0) {
			participationVO = participations.get(0);
		} else {
			throw new JazzOMRParticipationNotFound("Participacao de codigo " + imageDocPK.getParticipation() + " nao foi encontrada!");
		}

		return participationVO;

	}

	/**
	 * Marca com um X vermelho a posicao esperada das particulas de referencia. Deve sobrepor exatamente as cruzetas verdes das particulas de referencia encontradas
	 * 
	 * @param imageDocVO
	 * @param examVO
	 * @param widthAdjFactor
	 * @param heigthAdjFactor
	 * @throws JazzOMRException
	 */
	protected void logPosicaoEsperada(ImageDocVO imageDocVO, ExamVO examVO) throws JazzOMRException {
		if (!this.imageDocLogger.isOpenedGraphics2D())
			return;

		// fator de ajuste: ajustar coordenadas de acordo com o tamanho da imagem
		double widthAdjFactor = calculateWidthAdjustFactor(imageDocVO, examVO);
		double heigthAdjFactor = calculateHeigthAdjustFactor(imageDocVO, examVO);

		imageDocLogger.setTransparency(1);

		Graphics2D graphics2d = imageDocLogger.getOpenedGraphics2D();

		Stroke origStroke = graphics2d.getStroke();

		// set log pattern
		imageDocLogger.setLogStroke(graphics2d, imageDocLogger.WIDTH_REF_MARKS);

		Map<String, ExamOMRMetadataVO> mapExamOMR = examVO.getExamOMRMetadataVO();
		Set<String> keys = mapExamOMR.keySet();

		for (String key : keys) {

			ExamOMRMetadataVO examOMRMetadataVO = mapExamOMR.get(key);

			Point2D.Double tlXMLReport = examVO.determineTopLeftCorner();

			Point2D.Double tlImagem = imageDocVO.determineTopLeftCorner();
			int d = 8;

			Double xXML = examOMRMetadataVO.getX();
			Double yXML = examOMRMetadataVO.getY();

			int x = adjustXValue(widthAdjFactor, tlXMLReport, tlImagem, xXML);
			int y = adjustYValue(heigthAdjFactor, tlXMLReport, tlImagem, yXML);

			graphics2d.drawLine(x - d, y - d, x + d, y + d);
			graphics2d.drawLine(x - d, y + d, x + d, y - d);
		}

		graphics2d.setStroke(origStroke);
	}

	/**
	 * Faz ajustes necessários na imagem inicial
	 * @param imageDocVO
	 * @param particleAnalyzerParamDTO
	 * @throws JazzOMRException 
	 */
	protected void adjustImage(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO particleAnalyzerParamDTO) throws JazzOMRException {
		
		if(imageDocVO==null || imageDocVO.getBufferedImage()==null){
			throw new JazzOMRException("A imagem informada nao e valida (null)!");
		}
		
		
		
		BufferedImage bi = imageDocVO.getBufferedImage();
		
		double imgArea = bi.getHeight()*bi.getWidth();


		if(imgArea>maxImageArea){
		
			double raizImgArea = Math.sqrt(imgArea);
			
			double adjustFactor = raizMaxArea/raizImgArea;
			
			AffineTransform scaleTransform = AffineTransform.getScaleInstance(adjustFactor, adjustFactor);
			
			AffineTransformOp affineTransformOp = new AffineTransformOp(scaleTransform, null);
			bi = affineTransformOp.filter(bi, null);
			
			imageDocVO.setBufferedImage(bi);
			
		}
		
		
		
		
		
		
	}


	
	/**
	 * @param heigthAdjFactor
	 * @param tlXMLReport
	 * @param tlImagem
	 * @param yXML
	 * @return
	 */
	protected int adjustYValue(double heigthAdjFactor, Point2D.Double tlXMLReport, Point2D.Double tlImagem, Double yXML) {
		double y = yXML;
		y = y - tlXMLReport.y;
		y = y / heigthAdjFactor;
		y = y + tlImagem.y;
		return new Double(Math.floor(y)).intValue();
	}

	/**
	 * @param heigthAdjFactor
	 * @param tlXMLReport
	 * @param tlImagem
	 * @param yXML
	 * @return
	 */
	protected int adjustYValue(double heigthAdjFactor, Point2D.Double tlXMLReport, Point2D.Double tlImagem, Integer yXML) {

		return adjustYValue(heigthAdjFactor, tlXMLReport, tlImagem, yXML.doubleValue());
	}

	/**
	 * @param widthAdjFactor
	 * @param tlXMLReport
	 * @param tlImagem
	 * @param xXML
	 * @return
	 */
	protected int adjustXValue(double widthAdjFactor, Point2D.Double tlXMLReport, Point2D.Double tlImagem, Double xXML) {
		double x = xXML;
		x = x - tlXMLReport.x;
		x = x / widthAdjFactor;
		x = x + tlImagem.x;
		return new Double(Math.floor(x)).intValue();
	}

	/**
	 * @param widthAdjFactor
	 * @param tlXMLReport
	 * @param tlImagem
	 * @param xXML
	 * @return
	 */
	protected int adjustXValue(double widthAdjFactor, Point2D.Double tlXMLReport, Point2D.Double tlImagem, Integer xXML) {
		return adjustXValue(widthAdjFactor, tlXMLReport, tlImagem, xXML.doubleValue());
	}

	/**
	 * @param imageDocVO
	 * @param questionCoordinateVO
	 * @param examVO
	 * @param questionResult TODO
	 * @return
	 * @throws JazzOMRException
	 */
	protected void  readAlternativeBullets(ImageDocVO imageDocVO, ExamVO examVO, List<CriterionResultVO> critQuestResults, ISortable questionVO ) throws JazzOMRException {

		for (CriterionResultVO criterionResultVO : critQuestResults) {
			
			if(isAlternative(criterionResultVO) ){
				BufferedImage imgAlternativeAnswer = cropOMRMark(imageDocVO, examVO, criterionResultVO, imageDocVO.getBufferedImage());
				ResultsTable resultsTable = particleAnaylis(imgAlternativeAnswer);
				criterionResultVO.setTransientResultAnalysis(resultsTable);
				criterionResultVO.setTransientImage(imgAlternativeAnswer);
			} 
		
		}
		//este metodo nao determina quais bullets foram checados.
		
	}

	/**
	 * @param criterionResultVO
	 * @return
	 */
	protected boolean isAlternative(CriterionResultVO criterionResultVO) {
		return criterionResultVO.getCriterionCoordinateVO().getAbstractCriterionVO() instanceof AlternativeVO;
	}

	/**
	 * @param altCoord
	 * @param widthAdjFactor
	 * @param heigthAdjFactor
	 * @param tlCornerExam
	 * @param tlCornerImag
	 * @throws JazzOMRException
	 */
	protected void logCheckedOption(CriterionResultVO criterionResultVO,ExamVO examVO, ImageDocVO imageDocVO) {

		CriterionCoordinateVO altCoord = criterionResultVO.getCriterionCoordinateVO();
		
		if(/*!log.isDebugEnabled() || */!imageDocLogger.isOpenedGraphics2D()){
			return;
		}
		
		Point2D.Double tlCornerExam = examVO.determineTopLeftCorner();
		Point2D.Double tlCornerImag = imageDocVO.determineTopLeftCorner();

		double widthAdjFactor = calculateWidthAdjustFactor(imageDocVO, examVO);
		double heigthAdjFactor = calculateHeigthAdjustFactor(imageDocVO, examVO);
		
		
		
		Graphics2D graphics2d = imageDocLogger.getOpenedGraphics2D();

		Color colorOrig = graphics2d.getColor();// backup color
		Font fontOrig = graphics2d.getFont();

		// aplica cor do log de acordo com score da alernativa CORRECT/WRONG
		setLogColor(altCoord, graphics2d);

		IOMRMarkable omrMark = altCoord;

		double qcRefW = omrMark.getW() / widthAdjFactor;
		double qcRefH = omrMark.getH() / heigthAdjFactor;

		int intW = new Double(Math.floor(qcRefW)).intValue();
		int intH = new Double(Math.floor(qcRefH)).intValue();
		int intX = adjustXValue(widthAdjFactor, tlCornerExam, tlCornerImag, omrMark.getX());
		int intY = adjustYValue(heigthAdjFactor, tlCornerExam, tlCornerImag, omrMark.getY());

		intX = (int) (intX - (intW * 2.4));
		intX = intX <= 1 ? 1 : intX;

		String criticText = getCriticText(altCoord);

		setCriticFont(widthAdjFactor, graphics2d);

		int criticX = (int) (intX - (1.5*(double)intW));
		criticX = criticX <= 1 ? 1 : criticX;

		graphics2d.drawString(criticText, criticX, (int) (intY + (intH * 0.93)));

		if(BooleanUtils.isTrue(criterionResultVO.getChecked())){
			/*Desenha a seta apenas na alternativa escolhida*/
			drawSetaCritic(graphics2d, intW, intH, intX, intY);
		}

		graphics2d.setColor(colorOrig);// rollback color
		graphics2d.setFont(fontOrig);
	}

	/**
	 * @param graphics2d
	 * @param intW
	 * @param intH
	 * @param intX
	 * @param intY
	 */
	protected void drawSetaCritic(Graphics2D graphics2d, int intW, int intH, int intX, int intY) {
		intX=intX-(intW/2);
		
		graphics2d.fillPolygon(new int[] { intX, intX + intW, intX + intW + (intW / 2), intX + intW, intX }, new int[] { intY, intY, intY + (intH / 2), intY + intH,
				intY + intH }, 5);
	}

	/**
	 * @param widthAdjFactor
	 * @param graphics2d
	 */
	protected void setCriticFont(double widthAdjFactor, Graphics2D graphics2d) {
		int fontSize = (int) ((double) JazzOMRImageParser.CRITIC_FONT_SIZE / widthAdjFactor);
		Font font = new Font("Arial", Font.BOLD, fontSize);
		graphics2d.setFont(font);
	}

	/**
	 * @param criterionCoordinateVO
	 * @return
	 */
	protected String getCriticText(CriterionCoordinateVO criterionCoordinateVO) {

		AbstractCriterionVO abstractCriterionVO = criterionCoordinateVO.getAbstractCriterionVO();
		
		
		if(abstractCriterionVO instanceof AlternativeVO){//se for um criterio alternativo...
			
			AlternativeVO alternativeVO = (AlternativeVO) abstractCriterionVO;
		
			AlternativeScore score = alternativeVO.getScore();
	
			if (score != null && score.equals(AlternativeScore.CORRECT)) {
				return "C";
			} else if (score == null || score.equals(AlternativeScore.WRONG)) {
				return "E";
			} else {
				return "?";
			}
		}else{
			return "?";
		}
	}

	/**
	 * @param omrMark
	 * @param graphics2d
	 */
	protected void setLogColor(IOMRMarkable omrMark, Graphics2D graphics2d) {

		if (omrMark instanceof CriterionCoordinateVO) {

			CriterionCoordinateVO altCoordVO = (CriterionCoordinateVO) omrMark;

			AbstractCriterionVO abstractCriterionVO = altCoordVO.getAbstractCriterionVO();
			
			if(abstractCriterionVO instanceof AlternativeVO){
				
				AlternativeVO alternativeVO = (AlternativeVO) abstractCriterionVO;
				AlternativeScore score = alternativeVO.getScore();
	
				if (score != null && score.equals(AlternativeScore.CORRECT)) {
					graphics2d.setColor(ImageDocLogger.COR_LOG_CHECKED_CORRECT);
					
				} else if (score == null || score.equals(AlternativeScore.WRONG)) {
					graphics2d.setColor(ImageDocLogger.COR_LOG_CHECKED_WRONG);
					
				}
			}
		}
	}

	/**
	 * Realiza uma analise de particulas na imagem do bullet. Estes resultados serao analizados juntamente com as outras imagens de bullets de alternativas. 
	 * @param bi
	 * @return
	 */
	protected ResultsTable particleAnaylis(BufferedImage bi) {

		// wrapper de imagem do ImageJ para analise
		ImagePlus ip = new ImagePlus("bullet", bi);

		// prepara imagem para analise
		contrastImageThresholds(ip, JazzOMRImageParser.THRESHOLD_HUANG);

		// cria tabela de resultados
		ResultsTable rt = new ResultsTable();

		// determina parametros de analise
		double area = bi.getHeight() * bi.getWidth();
		double minArea = area * bulletMinAreaFactor;
		double maxArea = area * bulletMaxAreaFactor;

		/*
		private double bulletMinAreaFactor = 0.10;
		private double bulletMaxAreaFactor = 0.95;
		private double bulletMinCirc = 0.30;
		*/
		// cria analizador de particulas para bullets
		ParticleAnalyzer jpa = new ParticleAnalyzer(0, Measurements.AREA+Measurements.CIRCULARITY, rt, minArea, maxArea, bulletMinCirc, JazzOMRImageParser.MAX_CIRCULARITY);

		// executa analise
		jpa.analyze(ip);

		// testa se foram encotradas particulas
		//boolean checked = rt.getCounter() > 0;
		
		//imageDocLogger.logBullet(checked,bi,rt);
		//if(log.isDebugEnabled()){
		//}
		
		return rt;

	}

	/**
	 * Cria um objeto de resultado de questão e atribui referencia aa coordenada 
	 * @param criterionCoordinateVO
	 * @param participationVO
	 * @return
	 */
	protected CriterionResultVO createCriterionResult(CriterionCoordinateVO criterionCoordinateVO, ParticipationVO participationVO) {
		CriterionResultVO criterionResultVO = new CriterionResultVO();
		criterionResultVO.setCriterionCoordinateVO(criterionCoordinateVO);
		criterionResultVO.setParticipationVO(participationVO);
		return criterionResultVO;
	}

	/**
	 * 
	 * @param examVO
	 * @param omrMark
	 * @param bufferedImage TODO
	 * @param imageDocPK
	 * @return
	 * @throws JazzOMRException
	 */
	public BufferedImage cropOMRMark(ImageDocVO imageDocVO, ExamVO examVO, CriterionResultVO criterionResultVO , BufferedImage bufferedImage) throws JazzOMRException {
		IOMRMarkable omrMark = criterionResultVO.getCriterionCoordinateVO();

		Point2D.Double tlCornerExam = examVO.determineTopLeftCorner();
		Point2D.Double tlCornerImag = imageDocVO.determineTopLeftCorner();

		// calculos dos fatores de ajuste das coordenadas armazenadas para efetuar os cortes na imagem
		double widthAdjFactor = calculateWidthAdjustFactor(imageDocVO, examVO);
		double heigthAdjFactor = calculateHeigthAdjustFactor(imageDocVO, examVO);

		
		BufferedImage imageQuestion = cropOMRMark(bufferedImage, omrMark, tlCornerExam, tlCornerImag, widthAdjFactor, heigthAdjFactor);

		return imageQuestion;
	}

	/**
	 * @param imageDocVO
	 * @param omrMark
	 * @param tlCornerExam
	 * @param tlCornerImag
	 * @param widthAdjFactor
	 * @param heigthAdjFactor
	 * @return
	 * @throws JazzOMRException
	 */
	protected BufferedImage cropOMRMark(BufferedImage bufferedImage, IOMRMarkable omrMark, Point2D.Double tlCornerExam, Point2D.Double tlCornerImag, double widthAdjFactor,
			double heigthAdjFactor) throws JazzOMRException {
		

		// calcula a posição (x,y) e tamanho dos cortes (w,h)
		// lembrando que as coordenadas da questão nao é relativa aos discos pretos de referencia
		// o trecho de calculo "omrMark.getX() - tlCornerExam.x" determina as coordenadas a partir dos discos

		
		double qcRefW = omrMark.getW() / widthAdjFactor;
		int intX = adjustXValue(widthAdjFactor, tlCornerExam, tlCornerImag, omrMark.getX());
		int intW = new Double(Math.floor(qcRefW)).intValue();
		int xBorder = ((int) (qcRefW));
		intX = intX - xBorder;
		intW = intW + (2 * xBorder);
		

		double qcRefH = omrMark.getH() / heigthAdjFactor;
		final int intYOrig = adjustYValue(heigthAdjFactor, tlCornerExam, tlCornerImag, omrMark.getY());
		final int intHOrig = new Double(Math.floor(qcRefH)).intValue();
		
		int yBorder = ((int) (qcRefH / 3.5d));
		int intY = intYOrig - yBorder;
		int intH = intHOrig + (2 * yBorder);
		
		int wImagem = bufferedImage.getWidth();
		
		if(isDissertativa(omrMark)){
			//se for dissertativo é toda a largura da imagem
			intX=0;
			intW=wImagem;
			
			intY = intYOrig;
			intH = intHOrig;
			
		}else{
			//se for alternativa é apenas o bullet
			int wMax = intW + intX;
			if(wMax>wImagem){
				intW = wImagem - intX; 			
			}
			if(intX<0){
				intX=0;
			}
		}
		
		BufferedImage imageQuestion = bufferedImage.getSubimage(intX, intY, intW, intH);

		// loga posicao dos cortes na imagem
		//TODO: CONSERTAR ESTE LOG OU DAR UMA OUTRA SOLUCAO DE VISUALIZACAO
		logImageCropPosition(omrMark, intW, intH, intX, intY);

			
		return imageQuestion;
	}

	/**
	 * @param omrMark
	 * @return
	 */
	protected Boolean isDissertativa(IOMRMarkable omrMark) {
		Boolean isDissertative = false;
		
		if(omrMark instanceof CriterionCoordinateVO){
			CriterionCoordinateVO critCoord = (CriterionCoordinateVO) omrMark;
			AbstractCriterionVO abstractCriterionVO =  critCoord.getAbstractCriterionVO();
			
			isDissertative = (abstractCriterionVO!=null && abstractCriterionVO instanceof DissertativeVO);
			
		}
		return isDissertative;
	}

	/**
	 * @param omrMark
	 * @param intW
	 * @param intH
	 * @param intX
	 * @param intY
	 * @throws JazzOMRException
	 */
	protected void logImageCropPosition(IOMRMarkable omrMark, int intW, int intH, int intX, int intY) throws JazzOMRException {
		if (this.imageDocLogger.isOpenedGraphics2D()) {

			Graphics2D graphics2d = this.imageDocLogger.getOpenedGraphics2D();
			Color cOrig = graphics2d.getColor();
			Stroke origStroke = graphics2d.getStroke();
			Composite compositeOrig = graphics2d.getComposite();
			
			
			ImageDocLogger.setLogStroke(graphics2d, 0.01f);
			ImageDocLogger.setTransparency(graphics2d, 0.1f);

			// determina a cor do log, de acordo com o score de omrMark (questao ou alternativa)
			setLogColor(omrMark, graphics2d);

			if(isDissertativa(omrMark)){
				graphics2d.drawRect(intX, intY, intW, intH);
				
			}else{
				//para evitar confusoes nao vou logar a regiao do bullet
				//graphics2d.drawRect(intX-2, intY, 3, intH);
			}
			

			/*
			 * DESATIVADO, ATE SEGUNDA ORDEM rs... drawOMRMarkPK(omrMark, intW, intH, intX, intY, graphics2d);
			 */

			graphics2d.setStroke(origStroke);
			graphics2d.setColor(cOrig);
			graphics2d.setComposite(compositeOrig);

		}
	}

	/**
	 * @param omrMark
	 * @param intW
	 * @param intH
	 * @param intX
	 * @param intY
	 * @param graphics2d
	 */
	protected void drawOMRMarkPK(IOMRMarkable omrMark, int intW, int intH, int intX, int intY, Graphics2D graphics2d) {

		Font origFont = graphics2d.getFont();// FONT BACKUP

		int fontSize = (int) (18 * ((double) intW / REFERENCE_BULLET_WIDTH));
		Font font = new Font("Arial", Font.PLAIN, fontSize);

		int xPosition = intX - (intW * 5);
		int yPosition = (int) (intY + (intH * 0.8));
		xPosition = xPosition <= 1 ? 1 : xPosition;// protege contra resultados menores que 1

		graphics2d.setFont(font);

		CriterionCoordinateVO criterionCoordinateVO = (CriterionCoordinateVO) omrMark;

		graphics2d.drawString(criterionCoordinateVO.getAbstractCriterionVO().getDescription() + "", xPosition, yPosition);

		graphics2d.setFont(origFont);// FONT ROLLBACK
	}

	/**
	 * @param imageDocVO
	 * @param examVO
	 * @return
	 */
	protected double calculateHeigthAdjustFactor(ImageDocVO imageDocVO, ExamVO examVO) {

		// busca tamanhos de referencia para o padrao do exame, a partir dos discos pretos nas quinas do relatorio
		Double examHeigth = examVO.getExamRefHeigth();

		// busca tamanhos de referencia encontrados na imagem, a partir dos discos pretos nas quinas da imagem
		Double imgHeigth = imageDocVO.getImgRefHeigth();

		// determina um coeficiente de ajuste das coordenadas
		// este coeficiente será utilizado para ajustar os cortes que serão feitos na imagem
		double heigthAdjFactor = examHeigth / imgHeigth;
		return heigthAdjFactor;
	}

	/**
	 * @param imageDocVO
	 * @param examVO
	 * @return
	 */
	protected double calculateWidthAdjustFactor(ImageDocVO imageDocVO, ExamVO examVO) {

		// busca tamanhos de referencia para o padrao do exame, a partir dos discos pretos nas quinas do relatorio
		Double examWidth = examVO.getExamRefWidth();

		// busca tamanhos de referencia encontrados na imagem, a partir dos discos pretos nas quinas da imagem
		Double imgWidth = imageDocVO.getImgRefWidth();

		// determina um coeficiente de ajuste das coordenadas
		// este coeficiente será utilizado para ajustar os cortes que serão feitos na imagem
		double widthAdjFactor = examWidth / imgWidth;
		return widthAdjFactor;
	}

	/**
	 * Restorna o conjunto de questoes
	 * 
	 * @param imageDocPK
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Map<QuestionVO, List<CriterionCoordinateVO>> findCriterionCoordinates(ImageDocPK imageDocPK) {

		Integer pagina = imageDocPK.getPage();
		Long participationPK = imageDocPK.getParticipation();

		@SuppressWarnings("unchecked")
		List<CriterionCoordinateVO> criterionCoordinates = hibernateTemplate.findByNamedParam(
				" select distinct cco" +
				"  from ParticipationVO par " +
				"	inner join par.examVariantVO exv "+
				"	inner join exv.criterionCoordinates cco " +
				"	inner join fetch cco.questionVO que " +
				"	inner join fetch cco.abstractCriterionVO alt" +
				"	where " +
				"		 " + 
				"		par.PK in (:participationPK) and " + 
				"		cco.pagina = :pagina " +
				"" +
				" order by cco.alternativeOrder , cco.questionOrder ",
		new String[] { "participationPK", "pagina" }, new Object[] { participationPK, pagina });

		Map<QuestionVO, List<CriterionCoordinateVO>> mapCoord = new HashMap<QuestionVO, List<CriterionCoordinateVO>>(criterionCoordinates.size());
		
		
		for (CriterionCoordinateVO criterionCoordinateVO : criterionCoordinates) {
			List<CriterionCoordinateVO> critCoors = getListFromMap(mapCoord, criterionCoordinateVO);
			critCoors.add(criterionCoordinateVO);
		}
		
		
		return mapCoord;
	}

	/**
	 * @param mapCoord
	 * @param criterionCoordinateVO
	 * @return
	 */
	protected List<CriterionCoordinateVO> getListFromMap(Map<QuestionVO, List<CriterionCoordinateVO>> mapCoord, CriterionCoordinateVO criterionCoordinateVO) {
		QuestionVO questionVO = criterionCoordinateVO.getQuestionVO();
		
		List<CriterionCoordinateVO> critCoors = mapCoord.get(questionVO);
		
		if(critCoors==null){
			critCoors= new ArrayList<CriterionCoordinateVO>(7);
			mapCoord.put(questionVO, critCoors);
		}
		return critCoors;
	}

	/**
	 * Ajusta rotacao da imagem original. so e possivel ser executada quando se sabe a posicao das particulas de referencia
	 * 
	 * @param refParticles
	 * @param params
	 * @throws JazzOMRException
	 */
	protected void fixImageRotation(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO params) throws JazzOMRException {
		// determina o angulo de ajuste da rotacao da imagem. Angulo atual menos o angulo esperado
		double anguloAjuste = anguloAjuste(imageDocVO, params);

		// cria um affine transform com o angulo de ajuste
		AffineTransform affineRotateTransform = prepareRotateTransformations(anguloAjuste);

		// aplica as transformacoes nos pontos primeiro
		applyParticleTransformations(imageDocVO, affineRotateTransform);

		// de posse na nova posicacao dos pontos, aplica as transformacoes necessarias na imagem
		applyImageTransformations(imageDocVO, affineRotateTransform, params);

		// Apenas loga processamento da imagem. Faz uma copia da imagem no estado atual
		imageDocLogger.logImageProcessing(imageDocVO, params, "3-FixedRotation", "Rotacao da Imagem Corrigida");

	}

	/**
	 * Determina o angulo de ajuste, para que a imagem scaneada
	 * 
	 * @param imageDocVO
	 * @param params
	 * @return
	 * @throws JazzOMRFixRotationException
	 */
	protected double anguloAjuste(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO params) throws JazzOMRFixRotationException {
		// encontra particulas que sao referencia do angulo/rotacao da imagem
		List<ParticleVO> angularRef = findAngularRefParticles(imageDocVO.getParticles());
		imageDocVO.setParticles(angularRef);

		// angulo esperado vindo do ParticleAnalyzerParamDTO
		double refParticleAngleParam = params.getParticlesAngle();

		// angulo atual das particulas de referencia
		double refParticleAngle = anguloHipotenusa(angularRef);

		// angulo para de ajuste
		double anguloAjuste = refParticleAngleParam - refParticleAngle;
		return anguloAjuste;
	}

	/**
	 * Aplica as transformacoes na imagem scaneada
	 * 
	 * @param imageDocVO
	 * @param rotationTransform
	 * @param params
	 */
	protected void applyImageTransformations(ImageDocVO imageDocVO, AffineTransform rotationTransform, ParticleAnalyzerParamDTO params) {

		// calcula a translacao para trazer a quina superior esquerda do documento até a borda da imagem
		AffineTransform translation = determineTranslation(imageDocVO, params);

		// aplica as translacao nos pontos primeiro
		applyParticleTransformations(imageDocVO, translation);

		// soma a translacao aa rotacao da imagem
		translation.concatenate(rotationTransform);

		// aplica a concatenacao da translacao e rotacao aa imagem
		BufferedImage buffImgDest = applyImageTransformation(imageDocVO, translation);

		// recorta as sobras inferiores da imagem
		buffImgDest = recortaSobrasDaImagem(imageDocVO, params, buffImgDest);

		// atualiza o buffered image
		imageDocVO.setBufferedImage(buffImgDest);
	}

	/**
	 * @param imageDocVO
	 * @param translation
	 * @return
	 */
	protected BufferedImage applyImageTransformation(ImageDocVO imageDocVO, AffineTransform translation) {
		AffineTransformOp affOp = new AffineTransformOp(translation, JazzOMRImageParser.DEF_INTERPOLATION_TYPE);
		BufferedImage buffImgDest = affOp.filter(imageDocVO.getBufferedImage(), null);
		return buffImgDest;
	}

	/**
	 * Determina o limite inferior direito util da imagem e recorta as sobras.
	 * 
	 * @param imageDocVO
	 * @param params
	 * @param buffImgDest
	 * @return
	 */
	protected BufferedImage recortaSobrasDaImagem(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO params, BufferedImage buffImgDest) {
		Point2D.Double bottRight = imageDocVO.determineBottomRightCorner();

		// raio medio das particulas. é adicionado aos calculos de corner para nao cortar as quinas da imagem no meio das marcas de referencia
		double avgParticleRadius = params.getAvgParticleRadius(imageDocVO.getParticles());

		int xLimit = new Long(Math.round(bottRight.x + avgParticleRadius)).intValue();
		int yLimit = new Long(Math.round(bottRight.y + avgParticleRadius)).intValue();

		// protege conta possiveis erros de calculo
		if (xLimit > buffImgDest.getWidth()) {
			xLimit = buffImgDest.getWidth();
		}

		// protege conta possiveis erros de calculo
		if (yLimit > buffImgDest.getHeight()) {
			yLimit = buffImgDest.getHeight();
		}

		buffImgDest = buffImgDest.getSubimage(0, 0, xLimit, yLimit);
		return buffImgDest;
	}

	/**
	 * Determina translacao que alinhara a area util registrada na imagem ao canto superior esquerdo da imagem
	 * 
	 * @param imageDocVO
	 * @param params
	 * @return
	 */
	protected AffineTransform determineTranslation(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO params) {
		Point2D.Double particleTopLeft = imageDocVO.determineTopLeftCorner();
		Point2D.Double pointTopLeftDest = determineTopLeftCornerDest(imageDocVO, params);

		// faz translacao da figura
		AffineTransform translation = new AffineTransform();
		translation.translate(pointTopLeftDest.x - particleTopLeft.x, pointTopLeftDest.y - particleTopLeft.y);
		return translation;
	}

	/**
	 * Calcula o ponto da imagem que devera corresponder ao ponto 0,0 da imagem.
	 * 
	 * Usado no calculo da translacao da imagem e das particulas
	 * 
	 * @param imageDocVO
	 * @param params
	 * @return
	 */
	protected Point2D.Double determineTopLeftCornerDest(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO params) {

		double particleRadius = params.getAvgParticleRadius(imageDocVO.getParticles());

		Point2D.Double ptDest = new Point2D.Double(particleRadius, particleRadius);

		return ptDest;
	}

	/**
	 * Aplica as transformacoes 2D no conjunto de particulas encontradas. O conjunto allParticles contem o subset de particulas de referencia (refParticlesSubSet)
	 * 
	 * @param imageDocVO
	 * @param affineTransform
	 */
	protected void applyParticleTransformations(ImageDocVO imageDocVO, AffineTransform affineTransform) {
		List<ParticleVO> particles = imageDocVO.getParticles();

		applyParticleTransformations(affineTransform, particles);
	}

	/**
	 * Aplica as transformacoes 2D na colecao de particulas informada
	 * 
	 * @param affineTransform
	 * @param refParticles
	 */
	protected void applyParticleTransformations(AffineTransform affineTransform, List<ParticleVO> refParticles) {
		for (ParticleVO particleVO : refParticles) {
			if (log.isDebugEnabled()) {
				log.debug("Origem  X=" + particleVO.getX() + " Y=" + particleVO.getY());
			}

			// aplica das mudancas 2D acumuladas no affinetransform: rotacao, translacao, escala etc
			affineTransform.transform(particleVO, particleVO);

			if (log.isDebugEnabled()) {
				log.debug("Destino X=" + particleVO.getX() + " Y=" + particleVO.getY() + "\n\n");
			}

		}
	}

	/**
	 * Determina as transformacoes 2D a serem realizadas a partir do angulo informado (rotacao, translacao e crop).
	 * 
	 * @param anguloRadians
	 * @return
	 */
	protected AffineTransform prepareRotateTransformations(double anguloRadians) {
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.rotate(anguloRadians);

		return affineTransform;
	}

	/**
	 * Retorna o Angulo formado pela hipotenusa formada pelas particulas 1 e 2
	 * 
	 * @param angularRef
	 * @return
	 */
	protected double anguloHipotenusa(List<ParticleVO> angularRef) {
		ParticleVO particle1 = angularRef.get(0);
		ParticleVO particle2 = angularRef.get(1);

		double x1 = particle1.getX();
		double x2 = particle2.getX();

		double y1 = particle1.getY();
		double y2 = particle2.getY();

		double deltaX = x1 - x2;
		double deltaY = y1 - y2;

		double tangente = deltaY / deltaX;

		double arcotangente = Math.atan(tangente);

		return arcotangente;
	}

	/**
	 * A partir da lista de particulas de referencia, filtra as particulas que sao referencias do angulo (rotacao) da imagem
	 * 
	 * @param refParticles
	 * @return
	 * @throws JazzOMRFixRotationException
	 */
	protected List<ParticleVO> findAngularRefParticles(List<ParticleVO> refParticles) throws JazzOMRFixRotationException {
		if (refParticles.size() < 2) {
			throw new JazzOMRFixRotationException("Duas, e apenas duas, particulas de referência angular devem ser informadas para ajuste da rotacao da imagem !");
		}

		List<ParticleVO> angularReference = new ArrayList<ParticleVO>(2);

		for (ParticleVO particleVO : refParticles) {
			if (particleVO.isAngularReference()) {
				angularReference.add(particleVO);
			}
		}

		if (angularReference.size() != 2) {
			throw new JazzOMRFixRotationException("Duas, e apenas duas, particulas de referência angular devem ser informadas para ajuste da rotacao da imagem !");
		}

		return angularReference;
	}

	/**
	 * Determina onde esta a regiao da imagem com os codigos de barra ou QRCode com a identificacao do documento.
	 * 
	 * @param particleAnalyzerParamDTO
	 * 
	 * @param rotatedImage
	 * @param refDots
	 * @return
	 * @throws JazzOMRException
	 * @throws JazzOMRException
	 */
	protected void findIdentityRegion(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO particleAnalyzerParamDTO) throws JazzOMRException {

		ImageDocPK imageDocPK;

		// TODO IMPLEMENTAR ESTE MESMO ALGORITMO EM STRATEGY E CHAIN OF RESPONSABILITY

		try {
			// executa diversas tentativas de encontrar o QRCode identificador da imagem
			imageDocPK = getImageDocPK(imageDocVO, particleAnalyzerParamDTO);
		} catch (JazzOMRIdentityException e) {

			if (log.isDebugEnabled()) {
				//loga imagem onde deveria estar o qrcode
				imageDocLogger.openGraphics2D(imageDocVO, particleAnalyzerParamDTO, "XX-erroImgID_HEAD_UP", "Erro ao tentar ler QRCode");
				imageDocLogger.logaIdentificador(imageDocVO, particleAnalyzerParamDTO, imageDocLogger.getOpenedGraphics2D());
				imageDocLogger.closeGraphics2D();
			}

			throw new JazzOMRIdentityException("Nao foi possivel encontrar o QRCode identificador da imagem mesmo depois de duas tentativas em HEAD_UP e UPSIDE_DOWN", e);

		}

		//atribui identificador da imagem
		imageDocVO.setImageDocPK(imageDocPK);
		
		//pesquisa e atribui registro de participacao referente aa imagem
		ParticipationVO participationVO = findParticipationVO(imageDocPK);
		imageDocVO.setParticipationVO(participationVO);

		//loga estato do processamento da imagem
		imageDocLogger.logImageProcessing(imageDocVO, particleAnalyzerParamDTO, "4-idRegionFinded", "Identificador QRCode Encontrado");

	}

	/**
	 * Detecta na imagem o qrCode de identificacao documento/pagina e cria o objeto chave ImageDocPK
	 * 
	 * @param imageDocVO
	 * @param particleAnalyzerParamDTO
	 * @param imagePosition
	 * @return
	 * @throws JazzOMRException
	 */
	protected ImageDocPK getImageDocPK(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO particleAnalyzerParamDTO) throws JazzOMRException {
		
		QRCodeReaderParam parameterObject = new QRCodeReaderParam(imageDocVO, particleAnalyzerParamDTO);

		String strResult = readQRCode(parameterObject);

		//cria identificador da imagem a partir do codigo lido
		ImageDocPK imageDocPK = new ImageDocPK(strResult);

		if (log.isDebugEnabled()) {
			log.debug("Resultado leitura codigo de barras: " + strResult);
		}

		return imageDocPK;
	}



	/**
	 * Executa leitura do qrCDode contido na imagem.
	 * Utiliza diversas estrategias ate conseguir ler identificacao da imagem com sucesso
	 * @param identityRegion
	 * @return
	 * @throws JazzOMRException 
	 */
	protected String readQRCode(QRCodeReaderParam qrCodeReaderParam) throws JazzOMRException {
		
		//recupera estrategias de leitura de qrcode: qrCodeReaderDirect qrCodeReader180G qrCodeReaderScaled qrCodeReader180Scaled
		List<Strategy<QRCodeReaderParam, String>> strategies = getStrategies();
		String result=null;
		Exception lastError=null;

		//itera a sequencia de estrategias de leitura do qrCode (normal, 180g, escalado, 180g escalado ....)
		for (Strategy<QRCodeReaderParam, String> strategy : strategies) {
			try {
				result = strategy.execute(qrCodeReaderParam);
				
				if(isValidResult(result)){
					break;
				}
				
			} catch (Exception e) {
				lastError = e;
				log.warn("Erro ao tentar interpretar imagem!",e);
			}
		} 
		
		if(result==null){
			//nao houve resultado satisfatorio
			throw new JazzOMRException("Erro ao tentar ler qrcode",lastError);
		}
		
		return result;
	}

	/**
	 * @param result
	 * @return
	 */
	protected boolean isValidResult(String result) {
		return !StringUtils.isBlank(result);
	}

	/**
	 * @param identityRegion
	 * @return
	 */
	protected BinaryBitmap prepareBinaryMap(BufferedImage identityRegion) {
		LuminanceSource source = new BufferedImageLuminanceSource(identityRegion);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		return bitmap;
	}

	/**
	 * @return
	 */
	protected Reader getReader() {
		return new QRCodeReader();
	}

	/**
	 * Cria um AffineTransform para rotacao de 180 graus
	 * 
	 * @param imageWidth
	 * @param imageHeight
	 * @return
	 */
	protected AffineTransform create180DegreeTransform(int imageWidth, int imageHeight) {
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.rotate(Math.toRadians(180), imageWidth / 2, imageHeight / 2);
		return affineTransform;
	}

	/**
	 * @return
	 * @throws JazzOMRExamInstanceParseException
	 */
	private FileInputStream getFakeXMLSample() throws JazzOMRExamInstanceParseException {
		FileInputStream fis;
		try {
			fis = new FileInputStream("/home/darcio/workspace/modules/JazzOMRJRElements/ellipseJRElement.xml");
		} catch (FileNotFoundException e) {
			throw new JazzOMRExamInstanceParseException("Erro ao tentar criar parser de XML", e);
		}
		return fis;
	}

	/**
	 * @return
	 * @throws JazzOMRExamInstanceParseException
	 */
	protected SAXParser createSAXParser() throws JazzOMRExamInstanceParseException {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = saxParserFactory.newSAXParser();
		} catch (ParserConfigurationException e) {
			throw new JazzOMRExamInstanceParseException("Erro ao tentar criar parser de XML", e);
		} catch (SAXException e) {
			throw new JazzOMRExamInstanceParseException("Erro ao tentar criar parser de XML", e);
		}
		return parser;
	}

	/**
	 * Determinar pontos de referência para processamento das imagens com base na tabela de resultados de análise de partículas do imagej
	 * 
	 * @param totalParticles
	 * @param particleAnalyzerParamDTO
	 *          TODO
	 * @return
	 * @throws JazzOMRException
	 */
	protected void findRefParticles(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO particleAnalyzerParamDTO) throws JazzOMRException {
		RefParticleFinder refParticleFinder = createRefParticleFinder(particleAnalyzerParamDTO);

		List<ParticleVO> refParticleSubSet = refParticleFinder.executeFind(imageDocVO.getParticles(), particleAnalyzerParamDTO);

		imageDocVO.setParticles(refParticleSubSet);

	}

	/**
	 * Fabrica Apurador de particulas de acordo com o modelo informado
	 * 
	 * @param particleAnalyzerParamDTO
	 * @return
	 * @throws RefParticleNotFoundException
	 */
	protected RefParticleFinder createRefParticleFinder(ParticleAnalyzerParamDTO particleAnalyzerParamDTO) throws RefParticleNotFoundException {

		int countParticles = particleAnalyzerParamDTO.getCount();

		switch (countParticles) {
		case 3:
			return new ThreeRefParticleFinder();
		case 2:
			return new TwoRefParticleFinder();

		default:
			throw new RefParticleNotFoundException("Nao foi possivel encontrar um analizador de particulas para o (count=" + countParticles + ")");
		}

	}

	/**
	 * @param particleTemplates
	 *          TODO
	 * @param data
	 *          .getImp()
	 * @return
	 * @throws RefParticleNotFoundException
	 * @throws JazzOMRException
	 * @throws IOException
	 */
	protected void findParticles(ImageDocVO data, ParticleAnalyzerParamDTO analyzerParams) throws JazzOMRException {

		imageDocLogger.logImageProcessing(data, analyzerParams, "0-imagemOriginal", "Imagem Original");

		// destaca limites da imagem
		contrastImageThresholds(data.getImagePlus(), JazzOMRImageParser.THRESHOLD_DEFAULT);

		// executa a analise de particulas
		List<ParticleVO> particles = findParticles(data, 0, JazzOMRImageParser.MEDIDAS_PADRAO, analyzerParams);

		if (CollectionUtils.isEmpty(particles)) {
			throw new RefParticleNotFoundException("Não foram encontradas particulas de marcação na imagem informada");
		}

		data.setParticles(particles);

		imageDocLogger.logImageProcessing(data, analyzerParams, "1-particulasEncontradas", "Particulas Encontradas");

	}

	/**
	 * @param imageDoc
	 * @param fileSufix
	 * @param logDescription
	 * @param bi
	 * @throws JazzOMRException
	 */
	protected void writeImageFileLog(ImageDocVO imageDoc, String fileSufix, String logDescription, BufferedImage bi) throws JazzOMRException {
		try {
			if (fileSufix != null) {
				fileSufix = "_" + fileSufix;

			} else {
				fileSufix = "";
			}

			ImageIO.write(bi, IMG_EXT, new File("result/" + imageDoc.hashCode() + fileSufix));
		} catch (IOException e) {
			throw new JazzOMRException("erro ao tentar logar estado de processamento de imagem (" + logDescription + ")", e);
		}
	}

	/**
	 * Contrasta a imagem destacando todos os shapes para a analise de particulas
	 * 
	 * @param imp
	 * @param thresMethod TODO
	 */
	protected void contrastImageThresholds(ImagePlus imp, String thresMethod) {
		ImageConverter converter = new ImageConverter(imp);
		converter.convertToGray8();
		
		IJ.setAutoThreshold(imp, thresMethod);
	}

	/**
	 * Lista partículas encontradas na imagem de acordo com o template de particulas informado
	 * 
	 * @param imp
	 * @param options
	 * @param medidas
	 * @param parameterObject
	 *          TODO
	 * @return
	 */
	protected List<ParticleVO> findParticles(ImageDocVO data, int options, int medidas, ParticleAnalyzerParamDTO analyzerParams) {
		// int measurements=0;
		ResultsTable rt = new ResultsTable();

		double minArea = analyzerParams.getMinArea(data.getImgArea());
		double maxArea = analyzerParams.getMaxArea(data.getImgArea());
		double minCirc = analyzerParams.getMinCirc();

		ParticleAnalyzer jpa = new ParticleAnalyzer(options, medidas, rt, minArea, maxArea, minCirc, JazzOMRImageParser.MAX_CIRCULARITY);

		// executa analise
		jpa.analyze(data.getImagePlus());

		// itera resultados e preenche VOs
		List<ParticleVO> particles = fillParticleVOs(data, rt);

		return particles;
	}

	/**
	 * Apenas faz o trabalho bracal de preencher a lista de VOs
	 * 
	 * @param rt
	 * @return
	 */
	protected List<ParticleVO> fillParticleVOs(ImageDocVO data, ResultsTable rt) {
		int rowCount = rt.getCounter();
		List<ParticleVO> particles = new ArrayList<ParticleVO>(rowCount);

		if (rowCount > 0) {

			for (int row = 0; row < rowCount; row++) {

				ParticleVO particleVO = new ParticleVO();

				particleVO.setArea(getValue(rt, COLUMNS_PARTICLE.AREA, row));
				particleVO.setPerimeter(getValue(rt, COLUMNS_PARTICLE.PERIM, row));
				particleVO.setSolidity(getValue(rt, COLUMNS_PARTICLE.SOLIDITY, row));

				particleVO.setX(getValue(rt, COLUMNS_PARTICLE.X, row));
				particleVO.setY(getValue(rt, COLUMNS_PARTICLE.Y, row));

				particleVO.setCircularity(getValue(rt, COLUMNS_PARTICLE.CIRC, row));

				particles.add(particleVO);
			}

		}
		return particles;
	}

	/**
	 * Checa se coluna existe nos resultados. Se sim, retorna valor.
	 * 
	 * Evita erros
	 * 
	 * @param rt
	 * @param column
	 * @param row
	 * @return
	 */
	protected Double getValue(ResultsTable rt, COLUMNS_PARTICLE column, int row) {

		if (rt.columnExists(column.col)) {
			return rt.getValueAsDouble(column.col, row);
		} else {
			return null;
		}

	}

	/**
	 * Hints padrao de processamento de imagem
	 * 
	 * @return the hints
	 */
	public Hashtable getHints() {
		// hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		return hints;
	}

	/**
	 * @param hints
	 *          the hints to set
	 */
	public void setHints(Hashtable hints) {
		this.hints = hints;
	}


	/**
	 * @param strategies the strategies to set
	 */
	@SuppressWarnings("unchecked")
	@Autowired
	public void setStrategies(List<Strategy<QRCodeReaderParam, String>> strategies) {
		
		BeanComparator orderComparator = new BeanComparator("order");
		
		Collections.sort(strategies, orderComparator);
		
		this.strategies = strategies;
	}
	

	/**
	 * @return the strategies
	 */
	public List<Strategy<QRCodeReaderParam, String>> getStrategies() {
		return strategies;
	}
}
