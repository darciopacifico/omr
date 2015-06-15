package br.com.dlp.jazzomr.parser;

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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import br.com.dlp.framework.gof.strategy.Strategy;
import br.com.dlp.jazzomr.application.result.CriterionResultVO;
import br.com.dlp.jazzomr.application.result.QuestionResultVO;
import br.com.dlp.jazzomr.empresa.RelatorioVO;
import br.com.dlp.jazzomr.event.EventVO;
import br.com.dlp.jazzomr.event.ParticipationVO;
import br.com.dlp.jazzomr.exam.ExamOMRMetadataVO;
import br.com.dlp.jazzomr.exam.coordinate.OMRMark;
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
import br.com.dlp.jazzomr.question.AlternativeScore;
import br.com.dlp.jazzomr.question.QuestionVO;

/**
 * 
 * @author darcio
 */
@Component
public class JazzOMRImageParser {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private ImageDocLogger imageDocLogger = new ImageDocLogger();

	//private static final double MAX_IMAGE_AREA = 2000 * 3000;
	private static final double MAX_IMAGE_AREA = 400 * 600;
	private static final double RAIZ_MAX_AREA = Math.sqrt(MAX_IMAGE_AREA);
	
	private static final String THRESHOLD_HUANG = "Huang";
	private static final String THRESHOLD_DEFAULT = "Default";
	
	private static final int CRITIC_FONT_SIZE = 8;
	private static final String IMG_EXT = "gif";
	
	private static final int DEF_INTERPOLATION_TYPE = AffineTransformOp.TYPE_BICUBIC;
	private static final int MAX_CIRCULARITY = 1;
	private static final int MEDIDAS_PADRAO = Measurements.SHAPE_DESCRIPTORS + Measurements.CENTER_OF_MASS + Measurements.AREA + Measurements.PERIMETER + Measurements.CENTROID;

	private static final double bulletMinAreaFactor = 0.07;
	private static final double bulletMaxAreaFactor = 0.95;
	private static final double bulletMinCirc = 0.62;// ajuste de sensibilidade de reconhecimento de marcas


	private List<Strategy<QRCodeReaderParam, String>> strategies;

	@Autowired
	@Qualifier("alternativeResultBullet")
	public Comparator<? super CriterionResultVO> altComparator;

	/**
	 * Cria DTO contendo os parametros para funcionamento do parseador OMR de imagens
	 * 
	 * @return
	 */
	private ParticleAnalyzerParamDTO createParticleAnalyzerParams() {
		// tODO: PARAMETRIZAR EM BANCO DE DADOS
		List<ParticleTemplateVO> particleTemplates = getParticleTemplates();

		IdentityRegion identityRegion = new IdentityRegion(0.747, 0.015, 0.185, 0.118);

		ParticleAnalyzerParamDTO analyzerParams = new ParticleAnalyzerParamDTO(particleTemplates);
		analyzerParams.setIdentityRegion(identityRegion);
		return analyzerParams;
	}

	/**
	 * Constroi modelo padrao de particulas que sera esperado pelo image parser
	 * 
	 * @return
	 */
	private static List<ParticleTemplateVO> getParticleTemplates() {
		// TODO: PARAMETRIZAR MODELOS DE PARTICULAS
		List<ParticleTemplateVO> particleTemplatesList = new ArrayList<ParticleTemplateVO>(2);

		particleTemplatesList.add(ParticleTemplateVO.getInstance(EParticleArea.PEQUENA, EParticlePosition.BOTTOM_LEFT));
		particleTemplatesList.add(ParticleTemplateVO.getInstance(EParticleArea.PEQUENA, EParticlePosition.TOP_RIGHT));

		return particleTemplatesList;
	}
	
	
	/**
	 * @param eventVO
	 * @param images
	 * @return
	 * @throws JazzOMRException
	 */
	public List<QuestionResultVO> parseImages(EventVO eventVO, List<InputStream> images) throws JazzOMRException {
		List<QuestionResultVO> resultsTotal = new ArrayList<QuestionResultVO>();

		for (InputStream inputStream : images) {
			ImageDocVO docVO = new ImageDocVO(inputStream);
			List<QuestionResultVO> questions = parseImage(docVO,eventVO);
		
			resultsTotal.addAll(questions);
		}
		return resultsTotal;
	}
	

	/**
	 * Processa a imagem informada no construtor
	 * 
	 * @param relatorioVO
	 * @return 
	 * 
	 * @throws RefParticleNotFoundException
	 * @throws JazzOMRException
	 * @throws JazzOMRException
	 */
	public List<QuestionResultVO> parseImage(ImageDocVO imageDocVO, EventVO eventVO) throws JazzOMRException {
		// recupera modelo de particulas do sistema
		ParticleAnalyzerParamDTO particleAnalyzerParamDTO = createParticleAnalyzerParams();

		// ajusta imagem inicial
		adjustImage(imageDocVO, particleAnalyzerParamDTO);

		// encontra as particulas candidatas a particulas de referencia
		// Analise de particulas pura e simples. padrao imagej
		findParticles(imageDocVO, particleAnalyzerParamDTO);

		// Determinar, dentre as particulas encontradas, quais sao as particulas de referencia
		findRefParticles(imageDocVO, particleAnalyzerParamDTO);

		// Ajusta rotacao da imagem original.
		// So e possivel ser executada quando se sabe a posicao das particulas de referencia
		fixImageRotation(imageDocVO, particleAnalyzerParamDTO);

		// Determina onde esta a regiao da imagem com os codigos de barra ou QRCode com a identificacao do documento.
		findIdentityRegion(imageDocVO, particleAnalyzerParamDTO, eventVO);

		//processa a imagem tratada e identificada previamente e gera colecao de resultados.
		List<QuestionResultVO> questionResults = processResults(imageDocVO, eventVO);

		imageDocLogger.closeGraphics2D();
		
		return questionResults;
		
	}

	/**
	 * @param imageDocVO
	 * @param eventVO
	 * @return
	 * @throws JazzOMRException
	 */
	private List<QuestionResultVO> processResults(ImageDocVO imageDocVO, EventVO eventVO) throws JazzOMRException {
		Long participationId = imageDocVO.getImageDocPK().getParticipation();
		ParticipationVO participationVO = eventVO.getParticipations().get(participationId);
		Integer examVarId = participationVO.getExamVariant();
		Integer page = imageDocVO.getImageDocPK().getPage();

		//localiza todas as questoes contidas na imagem enviada
		Set<QuestionVO> questions = eventVO.findQuestions(examVarId, page);

		//cria arraylist para conter resultados
		List<QuestionResultVO> questionResults = new ArrayList<QuestionResultVO>(questions.size());

		for (QuestionVO questionVO : questions) {

			// encontra a referencia de todas as marcas que podem estar na página para cada questao 
			Set<OMRMark> markCoordinates = eventVO.findOMRMarks(questionVO, page, examVarId);

			// recorta todas as marcas da imagem tratada
			Set<CriterionResultVO> critResults = recortarRespostas(imageDocVO, markCoordinates, eventVO, examVarId, questionVO, participationVO);

			//determina quais foram os bullets selecionados 
			determineSelectedBullets(critResults, imageDocVO, questionVO, eventVO);

			//cria o objeto de resultado para a questao da vez
			QuestionResultVO questionResultVO = createQuestionResult(questionVO, critResults, examVarId, participationVO, page);

			//adiciona questionResult à lista de resposta
			questionResults.add(questionResultVO);

		}
		return questionResults;
	}

	/**
	 * @param questionVO
	 * @param critResults
	 * @param examVarId
	 * @param participationVO.getPK() 
	 * @param page 
	 * @param examVarId2 
	 * @return
	 */
	private QuestionResultVO createQuestionResult(QuestionVO questionVO, Set<CriterionResultVO> critResults, Integer examVarId, ParticipationVO participationVO, Integer page) {
		QuestionResultVO questionResultVO = new QuestionResultVO();
		
		questionResultVO.setPage(page);
		questionResultVO.setParticipation(participationVO.getPK());
		questionResultVO.setPessoaPK(participationVO.getPessoaVO().getPK());
		
		questionResultVO.setQuestionPK(questionVO.getPK());
		questionResultVO.setCriterionResultVOs(critResults);
		questionResultVO.setExamVar(examVarId);
		return questionResultVO;
	}

	/**
	 * Analisa os resultados de analise de particulas das imagens e determina qual foi a resposta escolhida
	 * 
	 * @param questionResultVO
	 * @param examVO
	 * @param imageDocVO
	 * @param eventVO
	 * @throws JazzOMRException
	 */
	private void determineSelectedBullets(Set<CriterionResultVO> allCriterions, ImageDocVO imageDocVO, QuestionVO questionVO, EventVO eventVO) throws JazzOMRException {

		// cria nova colecao com as alternativas para preservar a original
		List<CriterionResultVO> setAlternatives = new ArrayList<CriterionResultVO>(allCriterions.size());
		setAlternatives.addAll(allCriterions);

		// filtra apenas os candidatos mais provaveis
		CollectionUtils.filter(setAlternatives, (Predicate) this.altComparator);

		// apenas formaliza a passagem da colecao para uma lista
		ArrayList<CriterionResultVO> criterions = new ArrayList<CriterionResultVO>(setAlternatives.size());
		criterions.addAll(setAlternatives);

		// ordena as alternativas de pela maior probabilidade de terem sido as escolhidas
		Collections.sort(criterions, this.altComparator);

		// recupera quantas sao as questoes que serao assinaladas. normalmente 1 apenas
		Integer corrects = correctOptions(questionVO, criterions);

		// recupera lista de repostas assinaladas
		List<CriterionResultVO> selectedAlternatives = criterions.subList(0, corrects);

		// marca como checked os mais provaveis candidatos
		for (CriterionResultVO criterionResultVO : selectedAlternatives) {
			criterionResultVO.setChecked(true);
		}

		// loga os bullets nao checados
		logUncheckedAlternatives(allCriterions, eventVO.getRelatorioVO(), imageDocVO);

	}

	/**
	 * Loga imagens de bullets não checados. para depuração de codigo
	 * 
	 * @param allAlternatives
	 * @param selectedAlternatives
	 * @param examVO
	 * @param imageDocVO
	 */
	private void logUncheckedAlternatives(Collection<CriterionResultVO> allAlternatives, RelatorioVO relatorioVO, ImageDocVO imageDocVO) {
		for (CriterionResultVO alternativeVO : allAlternatives) {
			logCheckedOption(alternativeVO,relatorioVO,imageDocVO);
			imageDocLogger.logBullet(alternativeVO);
		}
	}

	/**
	 * @param questionResultVO
	 * @param alternatives
	 * @return
	 */
	private Integer correctOptions(QuestionVO questionVO, ArrayList<CriterionResultVO> alternatives) {
		Integer correctAlternatives = questionVO.countCorrectScore();
		correctAlternatives = correctAlternatives == null ? 1 : correctAlternatives;

		if (alternatives.size() < correctAlternatives) {
			correctAlternatives = alternatives.size();
		}

		return correctAlternatives;
	}

	/**
	 * 
	 * @param imageDocPK
	 * @param eventVO
	 * @return
	 * @throws JazzOMRException
	 */
	private ParticipationVO findParticipationVO(ImageDocPK imageDocPK, EventVO eventVO) throws JazzOMRException {

		if (imageDocPK == null || imageDocPK.getParticipation() == null) {
			throw new IllegalArgumentException("Erro ao tentar ler numero de participacao contido em ImageDocPK (null)");
		}

		ParticipationVO participationVO = getParticipacao(imageDocPK, eventVO);

		return participationVO;

	}

	private ParticipationVO getParticipacao(ImageDocPK imageDocPK, EventVO eventVO) {

		return eventVO.getParticipations().get(imageDocPK.getParticipation());
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
	private void logPosicaoEsperada(ImageDocVO imageDocVO, RelatorioVO relatorioVO) throws JazzOMRException {
		if (!this.imageDocLogger.isOpenedGraphics2D()) {
			return;
		}

		// fator de ajuste: ajustar coordenadas de acordo com o tamanho da imagem
		double widthAdjFactor = calculateWidthAdjustFactor(imageDocVO, relatorioVO);
		double heigthAdjFactor = calculateHeigthAdjustFactor(imageDocVO, relatorioVO);

		imageDocLogger.setTransparency(1);

		Graphics2D graphics2d = imageDocLogger.getOpenedGraphics2D();

		Stroke origStroke = graphics2d.getStroke();

		// set log pattern
		ImageDocLogger.setLogStroke(graphics2d, ImageDocLogger.WIDTH_REF_MARKS);

		Map<String, ExamOMRMetadataVO> mapExamOMR = relatorioVO.getExamOMRMetadataVO();
		Set<String> keys = mapExamOMR.keySet();

		for (String key : keys) {

			ExamOMRMetadataVO examOMRMetadataVO = mapExamOMR.get(key);

			Point2D.Double tlXMLReport = relatorioVO.determineTopLeftCorner();

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
	 * 
	 * @param imageDocVO
	 * @param particleAnalyzerParamDTO
	 * @throws JazzOMRException
	 */
	private void adjustImage(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO particleAnalyzerParamDTO) throws JazzOMRException {
		// tratamento de null basico
		if (imageDocVO == null || imageDocVO.getBufferedImage() == null) {
			throw new JazzOMRException("A imagem informada nao e valida (null)!");
		}

		// recupera área da imagem
		BufferedImage bi = imageDocVO.getBufferedImage();
		double imgArea = bi.getHeight() * bi.getWidth();

		// testa se a imagem eh maior que o maximo necessario
		if (imgArea > MAX_IMAGE_AREA) {
			// reduz imagem para melhorar performance de processamento
			reduzTamanhoImagem(imageDocVO, bi, imgArea);
		}
	}

	/**
	 * Reduz imagem para melhorar performance de processamento. Não é necessário que uma imagem tenha area maior que MAX_IMAGE_AREA
	 * 
	 * @param imageDocVO
	 * @param bi
	 * @param imgArea
	 */
	private void reduzTamanhoImagem(ImageDocVO imageDocVO, BufferedImage bi, double imgArea) {
		double raizImgArea = Math.sqrt(imgArea);

		double adjustFactor = RAIZ_MAX_AREA / raizImgArea;

		AffineTransform scaleTransform = AffineTransform.getScaleInstance(adjustFactor, adjustFactor);

		AffineTransformOp affineTransformOp = new AffineTransformOp(scaleTransform, null);
		bi = affineTransformOp.filter(bi, null);

		imageDocVO.setBufferedImage(bi);
	}

	/**
	 * @param heigthAdjFactor
	 * @param tlXMLReport
	 * @param tlImagem
	 * @param yXML
	 * @return
	 */
	private int adjustYValue(double heigthAdjFactor, Point2D.Double tlXMLReport, Point2D.Double tlImagem, Double yXML) {
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
	private int adjustYValue(double heigthAdjFactor, Point2D.Double tlXMLReport, Point2D.Double tlImagem, Integer yXML) {

		return adjustYValue(heigthAdjFactor, tlXMLReport, tlImagem, yXML.doubleValue());
	}

	/**
	 * @param widthAdjFactor
	 * @param tlXMLReport
	 * @param tlImagem
	 * @param xXML
	 * @return
	 */
	private int adjustXValue(double widthAdjFactor, Point2D.Double tlXMLReport, Point2D.Double tlImagem, Double xXML) {
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
	private int adjustXValue(double widthAdjFactor, Point2D.Double tlXMLReport, Point2D.Double tlImagem, Integer xXML) {
		return adjustXValue(widthAdjFactor, tlXMLReport, tlImagem, xXML.doubleValue());
	}

	/**
	 * @param imageDocVO
	 * @param eventVO
	 * @param participationVO 
	 * @param questionCoordinateVO
	 * @param examVO
	 * @param questionResult
	 * @return
	 * @return
	 * @throws JazzOMRException
	 */
	private Set<CriterionResultVO> recortarRespostas(ImageDocVO imageDocVO, Set<OMRMark> omrMarks, EventVO eventVO, Integer examVar, QuestionVO questionVO, ParticipationVO participationVO) throws JazzOMRException {

		Set<CriterionResultVO> critQuestResults = new HashSet<CriterionResultVO>(omrMarks.size());

		for (OMRMark omrMark : omrMarks) {

			BufferedImage imgAnswer = cropOMRMark(imageDocVO, eventVO.getRelatorioVO(), omrMark, imageDocVO.getBufferedImage());

			ResultsTable resultsTable = particleAnaylis(imgAnswer);

			CriterionResultVO critResVO = new CriterionResultVO();

			critResVO.setChave(omrMark.getChave());
			critResVO.setCriterion(omrMark.getCriterion());
			critResVO.setCritType(omrMark.getCritType());
			
			critResVO.setOmrMark(omrMark);
			
			critResVO.setPergunta(questionVO.getPK());
			
			critResVO.setTransientResultAnalysis(resultsTable);
			critResVO.setCroppedImage(imgAnswer);

			critQuestResults.add(critResVO);

		}

		return critQuestResults;

	}

	/**
	 * @param graphics2d
	 * @param intW
	 * @param intH
	 * @param intX
	 * @param intY
	 */
	private void drawSetaCritic(Graphics2D graphics2d, int intW, int intH, int intX, int intY) {
		intX = intX - (intW / 2);
		graphics2d.fillPolygon(new int[] { intX, intX + intW, intX + intW + (intW / 2), intX + intW, intX }, new int[] { intY, intY, intY + (intH / 2), intY + intH, intY + intH }, 5);
	}

	/**
	 * @param widthAdjFactor
	 * @param graphics2d
	 */
	private void setCriticFont(double widthAdjFactor, Graphics2D graphics2d) {
		int fontSize = (int) ((double) JazzOMRImageParser.CRITIC_FONT_SIZE / widthAdjFactor);
		Font font = new Font("Arial", Font.BOLD, fontSize);
		graphics2d.setFont(font);
	}

	/**
	 * @param omrMark
	 * @param graphics2d
	 */
	private void setLogColor(OMRMark omrMark, Graphics2D graphics2d) {

		if ("A".equals(omrMark.getCritType())) {

			Object score = AlternativeScore.CORRECT;
			if (score != null && score.equals(AlternativeScore.CORRECT)) {
				graphics2d.setColor(ImageDocLogger.COR_LOG_CHECKED_CORRECT);

			} else if (score == null || score.equals(AlternativeScore.WRONG)) {
				graphics2d.setColor(ImageDocLogger.COR_LOG_CHECKED_WRONG);

			}
		}
	}

	/**
	 * Realiza uma analise de particulas na imagem do bullet. Estes resultados serao analizados juntamente com as outras imagens de bullets de alternativas.
	 * 
	 * @param bi
	 * @return
	 */
	private ResultsTable particleAnaylis(BufferedImage bi) {

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
		 * private double bulletMinAreaFactor = 0.10; private double bulletMaxAreaFactor = 0.95; private double bulletMinCirc = 0.30;
		 */
		// cria analizador de particulas para bullets
		ParticleAnalyzer jpa = new ParticleAnalyzer(0, Measurements.AREA + Measurements.CIRCULARITY, rt, minArea, maxArea, bulletMinCirc, JazzOMRImageParser.MAX_CIRCULARITY);

		// executa analise
		jpa.analyze(ip);

		// testa se foram encotradas particulas
		boolean checked = rt.getCounter() > 0;

		if (log.isDebugEnabled()) {
			imageDocLogger.logBullet(checked, bi, rt);
		}

		return rt;

	}

	/**
	 * 
	 * @param examVO
	 * @param omrMark
	 * @param bufferedImage
	 * @param imageDocPK
	 * @return
	 * @throws JazzOMRException
	 */
	private BufferedImage cropOMRMark(ImageDocVO imageDocVO, RelatorioVO relatorioVO, OMRMark omrMark, BufferedImage bufferedImage) throws JazzOMRException {

		Point2D.Double tlCornerExam = relatorioVO.determineTopLeftCorner();
		Point2D.Double tlCornerImag = imageDocVO.determineTopLeftCorner();

		// calculos dos fatores de ajuste das coordenadas armazenadas para efetuar os cortes na imagem
		double widthAdjFactor = calculateWidthAdjustFactor(imageDocVO, relatorioVO);
		double heigthAdjFactor = calculateHeigthAdjustFactor(imageDocVO, relatorioVO);

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
	private BufferedImage cropOMRMark(BufferedImage bufferedImage, OMRMark omrMark, Point2D.Double tlCornerExam, Point2D.Double tlCornerImag, double widthAdjFactor, double heigthAdjFactor) throws JazzOMRException {

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

		if (isDissertativa(omrMark)) {
			// se for dissertativo é toda a largura da imagem
			intX = 0;
			intW = wImagem;

			intY = intYOrig;
			intH = intHOrig;

		} else {
			// se for alternativa é apenas o bullet
			int wMax = intW + intX;
			if (wMax > wImagem) {
				intW = wImagem - intX;
			}
			if (intX < 0) {
				intX = 0;
			}
		}

		BufferedImage imageQuestion = bufferedImage.getSubimage(intX, intY, intW, intH);
		// BufferedImage imageQuestion = bufferedImage.getSubimage(3, intY, wImagem-10, intH);

		// loga posicao dos cortes na imagem
		logImageCropPosition(omrMark, intW, intH, intX, intY);
		// logImageCropPosition(omrMark, wImagem, intH, 0, intY);

		return imageQuestion;
	}

	/**
	 * @param omrMark
	 * @return
	 */
	private Boolean isDissertativa(OMRMark omrMark) {
		return omrMark != null && "D".equals(omrMark.getCritType());
	}

	/**
	 * @param omrMark
	 * @param intW
	 * @param intH
	 * @param intX
	 * @param intY
	 * @throws JazzOMRException
	 */
	private void logImageCropPosition(OMRMark omrMark, int intW, int intH, int intX, int intY) throws JazzOMRException {
		if (this.imageDocLogger.isOpenedGraphics2D()) {
		}

		Graphics2D graphics2d = this.imageDocLogger.getOpenedGraphics2D();
		Color cOrig = graphics2d.getColor();
		Stroke origStroke = graphics2d.getStroke();
		Composite compositeOrig = graphics2d.getComposite();

		ImageDocLogger.setLogStroke(graphics2d, 0.5f);
		ImageDocLogger.setTransparency(graphics2d, 0.5f);

		// determina a cor do log, de acordo com o score de omrMark (questao ou alternativa)
		// setLogColor(omrMark, graphics2d);

		if (isDissertativa(omrMark)) {
			graphics2d.drawRect(intX, intY, intW, intH);
		} else { // para evitar confusoes nao vou logar a regiao do bullet
			graphics2d.drawRect(intX - 2, intY, 3, intH);
		}

		/*
		 * graphics2d.setStroke(origStroke); graphics2d.setColor(cOrig); graphics2d.setComposite(compositeOrig);
		 */
	}

	/**
	 * @param imageDocVO
	 * @param examVO
	 * @return
	 */
	private double calculateHeigthAdjustFactor(ImageDocVO imageDocVO, RelatorioVO relatorioVO) {
		// busca tamanhos de referencia para o padrao do exame, a partir dos discos pretos nas quinas do relatorio
		Double examHeigth = relatorioVO.getExamRefHeigth();

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
	private double calculateWidthAdjustFactor(ImageDocVO imageDocVO, RelatorioVO relatorioVO) {

		// busca tamanhos de referencia para o padrao do exame, a partir dos discos pretos nas quinas do relatorio
		Double examWidth = relatorioVO.getExamRefWidth();

		// busca tamanhos de referencia encontrados na imagem, a partir dos discos pretos nas quinas da imagem
		Double imgWidth = imageDocVO.getImgRefWidth();

		// determina um coeficiente de ajuste das coordenadas
		// este coeficiente será utilizado para ajustar os cortes que serão feitos na imagem
		double widthAdjFactor = examWidth / imgWidth;
		return widthAdjFactor;
	}

	/**
	 * Ajusta rotacao da imagem original. so e possivel ser executada quando se sabe a posicao das particulas de referencia
	 * 
	 * @param refParticles
	 * @param params
	 * @throws JazzOMRException
	 */
	private void fixImageRotation(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO params) throws JazzOMRException {
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
	private double anguloAjuste(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO params) throws JazzOMRFixRotationException {
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
	private void applyImageTransformations(ImageDocVO imageDocVO, AffineTransform rotationTransform, ParticleAnalyzerParamDTO params) {

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
	private BufferedImage applyImageTransformation(ImageDocVO imageDocVO, AffineTransform translation) {
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
	private BufferedImage recortaSobrasDaImagem(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO params, BufferedImage buffImgDest) {
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
	private AffineTransform determineTranslation(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO params) {
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
	private Point2D.Double determineTopLeftCornerDest(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO params) {

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
	private void applyParticleTransformations(ImageDocVO imageDocVO, AffineTransform affineTransform) {
		List<ParticleVO> particles = imageDocVO.getParticles();

		applyParticleTransformations(affineTransform, particles);
	}

	/**
	 * Aplica as transformacoes 2D na colecao de particulas informada
	 * 
	 * @param affineTransform
	 * @param refParticles
	 */
	private void applyParticleTransformations(AffineTransform affineTransform, List<ParticleVO> refParticles) {
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
	private AffineTransform prepareRotateTransformations(double anguloRadians) {
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
	private double anguloHipotenusa(List<ParticleVO> angularRef) {
		ParticleVO particle1 = angularRef.get(0);
		ParticleVO particle2 = angularRef.get(1);

		double x1 = particle1.getX();
		double x2 = particle2.getX();

		double y1 = particle1.getY();
		double y2 = particle2.getY();

		double deltaX = x1 - x2;
		double deltaY = y1 - y2;

		double tangente = deltaY / deltaX;

		double arcoTangente = Math.atan(tangente);

		return arcoTangente;
	}

	/**
	 * A partir da lista de particulas de referencia, filtra as particulas que sao referencias do angulo (rotacao) da imagem
	 * 
	 * @param refParticles
	 * @return
	 * @throws JazzOMRFixRotationException
	 */
	private List<ParticleVO> findAngularRefParticles(List<ParticleVO> refParticles) throws JazzOMRFixRotationException {
		if (refParticles.size() < 2) {
			throw new JazzOMRFixRotationException("Duas, e apenas duas, particulas de referÃªncia angular devem ser informadas para ajuste da rotacao da imagem !");
		}

		List<ParticleVO> angularReference = new ArrayList<ParticleVO>(2);

		for (ParticleVO particleVO : refParticles) {
			if (particleVO.isAngularReference()) {
				angularReference.add(particleVO);
			}
		}

		if (angularReference.size() != 2) {
			throw new JazzOMRFixRotationException("Duas, e apenas duas, particulas de referÃªncia angular devem ser informadas para ajuste da rotacao da imagem !");
		}

		return angularReference;
	}

	/**
	 * Determina onde esta a regiao da imagem com os codigos de barra ou QRCode com a identificacao do documento.
	 * 
	 * @param particleAnalyzerParamDTO
	 * @param eventVO
	 * 
	 * @param rotatedImage
	 * @param refDots
	 * @return
	 * @throws JazzOMRException
	 * @throws JazzOMRException
	 */
	private void findIdentityRegion(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO particleAnalyzerParamDTO, EventVO eventVO) throws JazzOMRException {

		ImageDocPK imageDocPK;

		// TODO IMPLEMENTAR ESTE MESMO ALGORITMO EM STRATEGY E CHAIN OF RESPONSABILITY

		openGraphics2D(imageDocVO, particleAnalyzerParamDTO);
		try {
			// executa diversas tentativas de encontrar o QRCode identificador da imagem
			imageDocPK = getImageDocPK(imageDocVO, particleAnalyzerParamDTO);
		} catch (JazzOMRIdentityException e) {

			/*
			if (log.isDebugEnabled()) {
				// loga imagem onde deveria estar o qrcode
			}
			*/

			throw new JazzOMRIdentityException("Nao foi possivel encontrar o QRCode identificador da imagem mesmo depois de duas tentativas em HEAD_UP e UPSIDE_DOWN", e);

		}

		// atribui identificador da imagem
		imageDocVO.setImageDocPK(imageDocPK);

		// pesquisa e atribui registro de participacao referente aa imagem
		ParticipationVO participationVO = findParticipationVO(imageDocPK, eventVO);
		imageDocVO.setParticipationVO(participationVO);
		Integer examVar = participationVO.getExamVariant();

		// loga estato do processamento da imagem
		imageDocLogger.logImageProcessing(imageDocVO, particleAnalyzerParamDTO, "4-idRegionFinded", "Identificador QRCode Encontrado. ExamVar=" + examVar);

	}

	private void openGraphics2D(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO particleAnalyzerParamDTO) throws JazzOMRException {
		imageDocLogger.openGraphics2D(imageDocVO, particleAnalyzerParamDTO, "XX-erroImgID_HEAD_UP", null);
		imageDocLogger.logaIdentificador(imageDocVO, particleAnalyzerParamDTO, imageDocLogger.getOpenedGraphics2D());
		// imageDocLogger.closeGraphics2D();
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
	private ImageDocPK getImageDocPK(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO particleAnalyzerParamDTO) throws JazzOMRException {

		QRCodeReaderParam parameterObject = new QRCodeReaderParam(imageDocVO, particleAnalyzerParamDTO);

		String strResult = readQRCode(parameterObject);

		// cria identificador da imagem a partir do codigo lido
		ImageDocPK imageDocPK = new ImageDocPK(strResult);

		if (log.isDebugEnabled()) {
			log.debug("Resultado leitura codigo de barras: " + strResult);
		}

		return imageDocPK;
	}

	/**
	 * Executa leitura do qrCDode contido na imagem. Utiliza diversas estrategias ate conseguir ler identificacao da imagem com sucesso
	 * 
	 * @param identityRegion
	 * @return
	 * @throws JazzOMRException
	 */
	private String readQRCode(QRCodeReaderParam qrCodeReaderParam) throws JazzOMRException {

		// recupera estrategias de leitura de qrcode: qrCodeReaderDirect qrCodeReader180G qrCodeReaderScaled qrCodeReader180Scaled
		List<Strategy<QRCodeReaderParam, String>> strategies = getStrategies();
		String result = null;
		Exception lastError = null;

		// itera a sequencia de estrategias de leitura do qrCode (normal, 180g, escalado, 180g escalado ....)
		for (Strategy<QRCodeReaderParam, String> strategy : strategies) {
			try {
				result = strategy.execute(qrCodeReaderParam);

				if (isValidResult(result)) {
					break;
				}

			} catch (Exception e) {
				lastError = e;
				log.warn("Erro ao tentar interpretar imagem!", e);
			}
		}

		if (result == null) {
			// nao houve resultado satisfatorio
			throw new JazzOMRException("Erro ao tentar ler qrcode", lastError);
		}

		return result;
	}

	/**
	 * @param result
	 * @return
	 */
	private boolean isValidResult(String result) {
		return !StringUtils.isBlank(result);
	}



	/**
	 * Determinar pontos de referÃªncia para processamento das imagens com base na tabela de resultados de análise de partÃ­culas do imagej
	 * 
	 * @param totalParticles
	 * @param particleAnalyzerParamDTO
	 *          TODO
	 * @return
	 * @throws JazzOMRException
	 */
	private void findRefParticles(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO particleAnalyzerParamDTO) throws JazzOMRException {
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
	private RefParticleFinder createRefParticleFinder(ParticleAnalyzerParamDTO particleAnalyzerParamDTO) throws RefParticleNotFoundException {

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
	 * @param imageDocVO.getImp()
	 * @return
	 * @throws RefParticleNotFoundException
	 * @throws JazzOMRException
	 * @throws IOException
	 */
	private void findParticles(ImageDocVO imageDocVO, ParticleAnalyzerParamDTO analyzerParams) throws JazzOMRException {

		imageDocLogger.logImageProcessing(imageDocVO, analyzerParams, "0-imagemOriginal", "Imagem Original");

		// destaca limites da imagem
		contrastImageThresholds(imageDocVO.getImagePlus(), JazzOMRImageParser.THRESHOLD_DEFAULT);

		// executa a analise de particulas
		List<ParticleVO> particles = findParticles(imageDocVO, 0, JazzOMRImageParser.MEDIDAS_PADRAO, analyzerParams);

		if (CollectionUtils.isEmpty(particles)) {
			throw new RefParticleNotFoundException("Não foram encontradas particulas de marcação na imagem informada");
		}

		imageDocVO.setParticles(particles);

		imageDocLogger.logImageProcessing(imageDocVO, analyzerParams, "1-particulasEncontradas", "Particulas Encontradas");

	}

	/**
	 * @param imageDoc
	 * @param fileSufix
	 * @param logDescription
	 * @param bi
	 * @throws JazzOMRException
	 */
	private void writeImageFileLog(ImageDocVO imageDoc, String fileSufix, String logDescription, BufferedImage bi) throws JazzOMRException {
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
	 * @param thresMethod
	 */
	private void contrastImageThresholds(ImagePlus imp, String thresMethod) {
		ImageConverter converter = new ImageConverter(imp);
		converter.convertToGray8();

		IJ.setAutoThreshold(imp, thresMethod);
	}

	/**
	 * Lista partÃ­culas encontradas na imagem de acordo com o template de particulas informado
	 * 
	 * @param imp
	 * @param options
	 * @param medidas
	 * @param parameterObject
	 *          TODO
	 * @return Measurements.SHAPE_DESCRIPTORS + Measurements.CENTER_OF_MASS + Measurements.AREA + Measurements.PERIMETER + Measurements.CENTROID;
	 */
	private List<ParticleVO> findParticles(ImageDocVO data, int options, int medidas, ParticleAnalyzerParamDTO analyzerParams) {
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
	private List<ParticleVO> fillParticleVOs(ImageDocVO data, ResultsTable rt) {
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
	private Double getValue(ResultsTable rt, COLUMNS_PARTICLE column, int row) {

		if (rt.columnExists(column.col)) {
			return rt.getValueAsDouble(column.col, row);
		} else {
			return null;
		}

	}

	/**
	 * @param strategies
	 *          the strategies to set
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

	/**
	 * @param altCoord
	 * @param widthAdjFactor
	 * @param heigthAdjFactor
	 * @param tlCornerExam
	 * @param tlCornerImag
	 * @throws JazzOMRException
	 */
	private void logCheckedOption(CriterionResultVO criterionResultVO, RelatorioVO relatorioVO, ImageDocVO imageDocVO) {

		if (!imageDocLogger.isOpenedGraphics2D() || !criterionResultVO.getChecked()) {
			return;
		}

		Point2D.Double tlCornerExam = relatorioVO.determineTopLeftCorner();
		Point2D.Double tlCornerImag = imageDocVO.determineTopLeftCorner();

		double widthAdjFactor = calculateWidthAdjustFactor(imageDocVO, relatorioVO);
		double heigthAdjFactor = calculateHeigthAdjustFactor(imageDocVO, relatorioVO);

		Graphics2D graphics2d = imageDocLogger.getOpenedGraphics2D();

		Color colorOrig = graphics2d.getColor();// backup color Font fontOrig = graphics2d.getFont();

		// aplica cor do log de acordo com score da alernativa CORRECT/WRONG setLogColor(altCoord, graphics2d);

		OMRMark omrMark = criterionResultVO.getOmrMark();

		double qcRefW = omrMark.getW() / widthAdjFactor;
		double qcRefH = omrMark.getH() / heigthAdjFactor;

		int intW = new Double(Math.floor(qcRefW)).intValue();
		int intH = new Double(Math.floor(qcRefH)).intValue();
		int intX = adjustXValue(widthAdjFactor, tlCornerExam, tlCornerImag, omrMark.getX());
		int intY = adjustYValue(heigthAdjFactor, tlCornerExam, tlCornerImag,

		omrMark.getY());

		intX = (int) (intX - (intW * 2.4));
		intX = intX <= 1 ? 1 : intX;


		setCriticFont(widthAdjFactor, graphics2d);

		int criticX = (int) (intX - (1.5 * (double) intW));
		criticX = criticX <= 1 ? 1 : criticX;

		graphics2d.drawString(criterionResultVO.getCritType(), criticX, (int) (intY + (intH * 0.93)));

		drawSetaCritic(graphics2d, intW, intH, intX, intY);
		
		graphics2d.setColor(colorOrig); 
		//graphics2d.setFont(fontOrig);
	}

}
