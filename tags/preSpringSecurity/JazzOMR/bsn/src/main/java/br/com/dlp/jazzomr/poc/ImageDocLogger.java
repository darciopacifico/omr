package br.com.dlp.jazzomr.poc;

import ij.measure.ResultsTable;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.jazzomr.application.result.CriterionResultVO;
import br.com.dlp.jazzomr.exceptions.JazzOMRException;
import br.com.dlp.jazzomr.omr.ImageDocPK;
import br.com.dlp.jazzomr.omr.ImageDocVO;
import br.com.dlp.jazzomr.omr.ParticleVO;

/**
 * @author darcio
 *
 */
public class ImageDocLogger {

	public static final Color COR_LOG_REF_PARTICLES = Color.MAGENTA;
	public static final Color COR_LOG_PARTICLES = Color.GREEN;
	public static final Color COR_LOG_CHECKED_CORRECT = Color.GREEN;
	public static final Color COR_LOG_CHECKED_WRONG = Color.RED;
	public static final Color COR_LOG_IDENTIFICADOR = Color.BLUE;
	public static final Color COR_ERRO_IDENTIFICADOR = Color.RED;
	
	public static final String DEF_IMG_TYPE = "gif";
	public static final float LOG_TRANSPARENCY = 0.5f;
	public static final int RAIO_REF_MARKS = 9;

	public static final int IMG_WIDTH_REF = 1000;
	
	public static final double BORDER_PARTICLE_RADIUS = 1.2;

	public static final float WIDTH_REF_MARKS = 3.0f;

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	private int fontSizeLargeMessages = 30;
	private BufferedImage bi;
	private Graphics2D graphics2D;
	private ImageDocVO imageDoc;
	private ParticleAnalyzerParamDTO particleAnalyzerParamDTO;
	private String fileSufix;
	private String logDescription;
	private float lastMessageYCoord=0;

	
	/**
	 * Loga estado do processamento da imagem
	 * 
	 * @param imageDoc
	 * @param fileSufix
	 * @param logDescription
	 * @param copyImage 
	 * @throws JazzOMRException
	 */
	public void logImageProcessing(ImageDocVO imageDoc, ParticleAnalyzerParamDTO particleAnalyzerParamDTO, String fileSufix, String logDescription, boolean copyImage) throws JazzOMRException {
		/*
		 */
		if (!log.isDebugEnabled()) {
			return;
		}

		BufferedImage bi = logImageDocVO(imageDoc, particleAnalyzerParamDTO, logDescription, copyImage);
		
		writeImageFileLog(imageDoc, fileSufix, logDescription, bi);
	}
	
	/**
	 * Loga estado do processamento da imagem
	 * 
	 * @param imageDoc
	 * @param fileSufix
	 * @param logDescription
	 * @param copyImage 
	 * @throws JazzOMRException
	 */
	public void logImageProcessing(ImageDocVO imageDoc, ParticleAnalyzerParamDTO particleAnalyzerParamDTO, String fileSufix, String logDescription) throws JazzOMRException {
		logImageProcessing(imageDoc, particleAnalyzerParamDTO, fileSufix, logDescription,true);
	}
	
	/**
	 * Loga estado do processamento da imagem
	 * 
	 * @param imageDoc
	 * @param fileSufix
	 * @param logDescription
	 * @param copyImage 
	 * @throws JazzOMRException
	 */
	public void openGraphics2D(ImageDocVO imageDoc, ParticleAnalyzerParamDTO particleAnalyzerParamDTO, String fileSufix, String logDescription, boolean copyImage) throws JazzOMRException {
		/*
		if (!log.isDebugEnabled()) {
			return;
		}
		*/
		
		this.imageDoc = imageDoc;
		this.particleAnalyzerParamDTO = particleAnalyzerParamDTO;
		this.fileSufix = fileSufix; 
		this.logDescription = logDescription;
		this.lastMessageYCoord=0;

		this.bi = logImageDocVO(imageDoc, particleAnalyzerParamDTO, logDescription, copyImage);
		
		
		
		int fontSize = getFontSize(this.bi.getWidth(), IMG_WIDTH_REF, fontSizeLargeMessages);
		
		this.graphics2D = prepareGraphics2DLog(this.bi,fontSize);
		
	}
	
	
	
	/**
	 * Loga estado do processamento da imagem
	 * 
	 * @param imageDoc
	 * @param fileSufix
	 * @param logDescription
	 * @param copyImage 
	 * @throws JazzOMRException
	 */
	public void openGraphics2D(ImageDocVO imageDoc, ParticleAnalyzerParamDTO particleAnalyzerParamDTO, String fileSufix, String logDescription) throws JazzOMRException {
		this.openGraphics2D(imageDoc, particleAnalyzerParamDTO, fileSufix, logDescription, true);
		
	}
	

	/**
	 * @param realWidth TODO
	 * @param referenceWidth TODO
	 * @param fontSize TODO
	 * @return
	 */
	public int getFontSize(int realWidth, int referenceWidth, int fontSize) {
		return (int) ((double)fontSize * ((double)realWidth/(double)referenceWidth));
	}


	/**
	 * @param imageDoc
	 * @param particleAnalyzerParamDTO
	 * @param logDescription
	 * @param imagePosition
	 * @return
	 */
	public BufferedImage logImageDocVO(ImageDocVO imageDoc, ParticleAnalyzerParamDTO particleAnalyzerParamDTO, String logDescription, boolean copyImg) {
		this.lastMessageYCoord=0;
		
		BufferedImage bi;
		if(copyImg){
			bi = copyBufferedImage(imageDoc.getBufferedImage());
		}else{
			bi = imageDoc.getBufferedImage();
		}
		
		int fontSize = getFontSize(bi.getWidth(), IMG_WIDTH_REF, fontSizeLargeMessages);
		
		Graphics2D graphics2d = prepareGraphics2DLog(bi,fontSize);
		
		drawLargeMsg(bi, graphics2d, logDescription);
		
		
		//backup state
		Composite origCompos = graphics2d.getComposite();
		Color original = graphics2d.getColor();
		Stroke origStroke = graphics2d.getStroke();
		
		logParticles(graphics2d, imageDoc.getParticles(), COR_LOG_PARTICLES);
		
		//rollback state
		graphics2d.setStroke(origStroke);
		graphics2d.setColor(original);
		graphics2d.setComposite(origCompos);
		
		logaIdentificador(imageDoc, particleAnalyzerParamDTO, graphics2d);
		return bi;
	}


	/**
	 * @param bi
	 * @return
	 */
	protected Graphics2D prepareGraphics2DLog(BufferedImage bi, int fontSize) {
		Graphics2D graphics2d = bi.createGraphics();

		// seta transparencia
		setTransparency(graphics2d, LOG_TRANSPARENCY);
		
		// seta cor da escrita
		graphics2d.setColor(Color.red);
		
		int widthReal = bi.getWidth();
		
		Font font = new Font("Arial", Font.BOLD, fontSize);
		graphics2d.setFont(font);
		
		return graphics2d;
	}
	
	/**
	 * @param biOrig
	 * @return
	 */
	protected BufferedImage copyBufferedImage(BufferedImage biOrig) {
		BufferedImage biDest = new BufferedImage(biOrig.getWidth(), biOrig.getHeight(), biOrig.getType());
		
		int h = biOrig.getHeight();
		int w = biOrig.getWidth();
		
		for(int x=0; x<w; x++){
			for(int y=0; y<h; y++){
				int rgb = biOrig.getRGB(x, y);
				biDest.setRGB(x, y, rgb);
			}
		}
		
		return biDest;
	}


	/**
	 * @param imageDoc
	 * @param fileSufix
	 * @param logDescription
	 * @param bi
	 * @throws JazzOMRException
	 */
	protected void writeImageFileLog(ImageDocVO imageDoc, String fileSufix, String logDescription, BufferedImage bi) throws JazzOMRException {
		if(!log.isDebugEnabled()){
			return;
		}
		
		try {
			if(fileSufix!=null){
				fileSufix = "_"+fileSufix;
				
			}else{
				fileSufix = "";
			}
			
			File dirResult = new File("result/");
			dirResult.mkdirs();
			
			ImageIO.write(bi, DEF_IMG_TYPE, new File(dirResult, imageDoc.hashCode() + fileSufix+"."+DEF_IMG_TYPE));
		} catch (IOException e) {
			throw new JazzOMRException("erro ao tentar logar estado de processamento de imagem (" + logDescription + ")", e);
		}
	}


	/**
	 * Loga identificador da imagem
	 * 
	 * @param imageDoc
	 * @param graphics2d
	 * @param imagePosition
	 */
	protected void logaIdentificador(ImageDocVO imageDoc, ParticleAnalyzerParamDTO particleAnalyzerParamDTO, Graphics2D graphics2d) {

		Color corOrig = graphics2d.getColor();
		
		graphics2d.setColor(COR_LOG_IDENTIFICADOR);
		
		BufferedImage bi = imageDoc.getBufferedImage();

		IdentityRegion idRegionRel = particleAnalyzerParamDTO.getIdentityRegion();
		destacaLocalEsperado(graphics2d, bi, idRegionRel);
		
		
		ImageDocPK imageDocPK = imageDoc.getImageDocPK();
		if (imageDocPK == null) {
			drawLargeMsg(bi,graphics2d, "QRCode nao encontrado. Local Esperado em destaque!");
			return;
		}
		
		if (idRegionRel == null) {
			throw new NullPointerException("Nao foi informada a regiao da imagem que contem a identificacao do documento!");
		}
		
		drawLargeMsg(bi,graphics2d,"QRCode encontrado e lido com sucesso!!");
		
		graphics2d.setColor(corOrig);
	}

	/**
	 * @param bi 
	 * @param graphics2d
	 * @param xCenter
	 * @param yCenter
	 * @param msg
	 */
	protected void drawLargeMsg(BufferedImage bi, Graphics2D graphics2d, String msg) {
		
		float xCenter = bi.getWidth() / 10;
		float yCenter = bi.getHeight() / 32;
		
		this.lastMessageYCoord += yCenter; 
	
		System.out.println(graphics2d.getColor()+" "+graphics2d.getFont());
		
		graphics2d.drawString(msg, xCenter, this.lastMessageYCoord);
	}

	/**
	 * @param graphics2d
	 * @param bi
	 * @param idRegionRel
	 */
	protected void destacaLocalEsperado(Graphics2D graphics2d, BufferedImage bi, IdentityRegion idRegionRel) {
		int imageWidth = bi.getWidth();
		int imageHeight = bi.getHeight();

		

		//backup state
		Stroke origStroke = graphics2d.getStroke();

		//set log pattern
		setLogStroke(graphics2d, 3.0f);
		
		int x = (int) Math.floor(imageWidth * idRegionRel.getXrel());
		int y = (int) Math.floor(imageHeight * idRegionRel.getYrel());
		int w = (int) Math.floor(imageWidth * idRegionRel.getWrel());
		int h = (int) Math.floor(imageHeight * idRegionRel.getHrel());
		
		graphics2d.drawRect(x, y, w, h);
		
		graphics2d.setStroke(origStroke);
	}
	
	/**
	 * @param graphics2d
	 * @param transparency
	 */
	public static void setTransparency(Graphics2D graphics2d, float transparency) {
		AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency);
		graphics2d.setComposite(composite);
	}
	
	
	/**
	 * @param graphics2d
	 * @param transparency
	 * @throws JazzOMRException 
	 */
	protected void setTransparency(float transparency) throws JazzOMRException {
		if(isOpenedGraphics2D())
		setTransparency(getOpenedGraphics2D(),transparency);
	}
	
	
	

	/**
	 * @param graphics2d
	 * @param particles
	 * @param logPartColor
	 */
	protected void logParticles(Graphics2D graphics2d, Collection<ParticleVO> particles, Color logPartColor) {
		if (particles == null)
			return;


		
		//set log pattern
		setTransparency(graphics2d, ImageDocLogger.LOG_TRANSPARENCY);
		graphics2d.setColor(logPartColor);
		setLogStroke(graphics2d,WIDTH_REF_MARKS); 

		
		//do log stuff
		for (ParticleVO particleVO : particles) {
			int px = (int) particleVO.getX();
			int py = (int) particleVO.getY();

			int r = ImageDocLogger.RAIO_REF_MARKS;
			graphics2d.drawLine(px - r, py, px + r, py);
			graphics2d.drawLine(px, py - r, px, py + r);
		}
	}

	/**
	 * @param graphics2d
	 * @param f
	 */
	public static void setLogStroke(Graphics2D graphics2d, float f) {
		
		BasicStroke bs = new BasicStroke(f); 
		
		graphics2d.setStroke(bs);
	}


	
	public Graphics2D getOpenedGraphics2D()  {
		
		
		if(this.graphics2D==null){
			throw new JazzRuntimeException("O graphics 2d nao foi aberto!");
		}
		
		return this.graphics2D;
		
	}
	
	/**
	 * @return
	 * @throws JazzOMRException
	 */
	public boolean isOpenedGraphics2D() {
		return this.graphics2D!=null;
	}
	
	public void closeGraphics2D() throws JazzOMRException{
		/*
		if(!log.isDebugEnabled()){
			return;
		}
		*/
		
		if(this.graphics2D==null){
			log.debug("O graphics 2d nao foi aberto!");
		}
		
		
		
		writeImageFileLog(this.imageDoc, this.fileSufix, this.logDescription, this.bi);
		
		this.graphics2D=null;
		this.imageDoc = null; 
		this.fileSufix = null; 
		this.logDescription = null; 
		this.bi = null;
	}
	
	/**
	 * Log image bullet
	 * @param alternativeResultVO
	 * @param imgAlternativeAnswer
	 * @param rt 
	 */
	public void logBullet(Boolean checked, BufferedImage imgAlternativeAnswer, ResultsTable rt) {
		if(!log.isDebugEnabled() || imgAlternativeAnswer==null || rt==null ){
			return;
		}
		
		File dirBulletLog = new File("result/bulletLog/");
		
		dirBulletLog.mkdirs();

		double imgArea = imgAlternativeAnswer.getHeight()*imgAlternativeAnswer.getWidth();
		
		int circ=0;
		int area=0;
		int count = rt.getCounter();
		for(int i=0; i<count; i++){

			circ = (int) (getValue(rt, i, COLUMNS_PARTICLE.CIRC)*100);
			area = (int) ((getValue(rt, i, COLUMNS_PARTICLE.AREA)/imgArea)*100);
		}
		
		
		File bulletImg = new File(dirBulletLog,"blt_"+checked+"_"+System.currentTimeMillis()+"_"+circ+"_"+area+"."+this.DEF_IMG_TYPE);
		
		try {
			ImageIO.write(imgAlternativeAnswer, DEF_IMG_TYPE, bulletImg);
		} catch (IOException e) {
			log.error("Erro ao tentar gravar bullet de opcao!",e);
		}
		
	}
	/**
	 * @param rt
	 * @param circ
	 * @param i
	 * @param column
	 * @return
	 */
	protected double getValue(ResultsTable rt, int i, COLUMNS_PARTICLE column) {
		double circ=0;
		if (rt.columnExists(column.col)) {
			circ = rt.getValueAsDouble(column.col, i);
		}
		return circ;
	}

	/**
	 * 
	 * @param criterionResultVO
	 */
	public void logBullet(CriterionResultVO criterionResultVO) {
		if(!log.isDebugEnabled()){
			return;
		}
		logBullet(criterionResultVO.getChecked(), criterionResultVO.getTransientImage(), criterionResultVO.getTransientResultAnalysis());
	}

	
}
