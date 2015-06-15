package br.com.dlp.jazzomr.omr;

import ij.ImagePlus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.beanutils.BeanComparator;

import br.com.dlp.jazzomr.event.ParticipationVO;
import br.com.dlp.jazzomr.exceptions.JazzOMRException;

/**
 * Documento scaneado para OMR
 * 
 * @author darcio
 * 
 */
public class ImageDocVO {
	
	private BufferedImage bufferedImage;

	//private BufferedImage bufferedImageCopy;
	
	private ImagePlus imagePlus;
	
	private List<ParticleVO> particles;
	
	private ImageDocPK imageDocPK;


	private ParticipationVO participationVO;
	
	/**
	 * Cria VO de abstracao de imagem de documento para OMR
	 * @param is
	 * @throws JazzOMRException 
	 */
	public ImageDocVO(InputStream is) throws JazzOMRException {
		BufferedImage image = readBufferedImage(is);
		
		copyBufferedImage(image);
	}

	
	
	/**
	 * Cria VO de abstracao de imagem de documento para OMR
	 * @param is
	 * @throws JazzOMRException 
	 */

	public ImageDocVO(BufferedImage image) throws JazzOMRException {
		
		copyBufferedImage(image);
	}

	


	


	/**
	 * @param image
	 */
	protected void copyBufferedImage(BufferedImage image) {
		int imgType = image.getType();
		
		if(imgType==0){
			imgType = BufferedImage.TYPE_INT_RGB;
		}
		
		/*
		BufferedImage biCopy = new BufferedImage(image.getWidth(), image.getHeight(), imgType);
		biCopy.setData(image.getData());
		*/
		
		setBufferedImage(image);
		//setBufferedImageCopy(biCopy);
	}


	/**
	 * @param is
	 * @return
	 * @throws JazzOMRException
	 */
	protected BufferedImage readBufferedImage(InputStream is) throws JazzOMRException {
		if (is == null) {
			throw new NullPointerException("O inputstream informado nao pode ser nulo!");
		}
		
		BufferedImage image;
		try {
			image = ImageIO.read(is);
		} catch (IOException e) {
			throw new JazzOMRException("Erro ao tentar ler stream de origem da imagem",e);
		}
		return image;
	}
	
	
	public Integer getImgHeight() {
		return bufferedImage.getHeight();
	}
	
	public Integer getImgWidth() {
		return bufferedImage.getWidth();
	}
	
	public long getImgArea() {
		if(getImgHeight()==null || getImgWidth()==null){
			return 0;
		}
		
		return getImgHeight()*getImgWidth();
	}
	
	
	public ImagePlus getImagePlus() {
		return imagePlus;
	}
	
	public void setImagePlus(ImagePlus imp) {
		imagePlus = imp;
	}
	
	public List<ParticleVO> getParticles() {
		return this.particles;
	}
	
	public void setParticles(List<ParticleVO> particles) {
		this.particles = particles;
	}
	
	public ImageDocPK getImageDocPK() {
		return imageDocPK;
	}
	
	public void setImageDocPK(ImageDocPK identityRegionVO) {
		this.imageDocPK = identityRegionVO;
	}
	
	/**
	 * @return the bufferedImage
	 */
	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}
	
	/**
	 * @param bufferedImage the bufferedImage to set
	 */
	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
		
		ImagePlus imagePlus = new ImagePlus("correcaoOMR", bufferedImage);
		
		setImagePlus(imagePlus);
	}
	
	/**
	 * Determinar ponto inferior direito
	 * 
	 * @param imageDocVO
	 * @param params
	 * @return
	 */
	public java.awt.geom.Point2D.Double determineBottomRightCorner() {
			
		List<ParticleVO> particles = getParticles();

		BeanComparator xComparator = new BeanComparator("x");
		BeanComparator yComparator = new BeanComparator("y");

		ParticleVO particleX = Collections.max(particles, xComparator);
		ParticleVO particleY = Collections.max(particles, yComparator);

		double x = particleX.getX();
		double y = particleY.getY();

		Double bottomRightCorner = new java.awt.geom.Point2D.Double(x, y);

		return bottomRightCorner;
	}

	

	
	
	/**
	 * Determinar ponto superior esquerdo da figura
	 * 
	 * @param imageDocVO
	 * @return
	 */
	public java.awt.geom.Point2D.Double determineTopLeftCorner() {

		List<ParticleVO> particles = getParticles();

		BeanComparator xComparator = new BeanComparator("x");
		BeanComparator yComparator = new BeanComparator("y");

		ParticleVO x = Collections.min(particles, xComparator);
		ParticleVO y = Collections.min(particles, yComparator);

		Double topLeftCorner = new java.awt.geom.Point2D.Double(x.getX(), y.getY());

		return topLeftCorner;
	}

	public java.lang.Double getImgRefWidth(){
		
		Point2D.Double brCornerImg = determineBottomRightCorner();
		Point2D.Double tlCornerImg = determineTopLeftCorner();
		double imgRefWidth = brCornerImg.getX() - tlCornerImg.getX();
	
		return new java.lang.Double(imgRefWidth);
	}  
	
	public java.lang.Double getImgRefHeigth(){
		
		Point2D.Double brCornerImg = determineBottomRightCorner();
		Point2D.Double tlCornerImg = determineTopLeftCorner();
		java.lang.Double imgRefHeigth = brCornerImg.getY() - tlCornerImg.getY();
	
		return imgRefHeigth;
	}

	
	
	public Graphics2D getLogGraphics2D() {
		Graphics2D createGraphics = this.bufferedImage.createGraphics();
		createGraphics.setColor(Color.red);
		return createGraphics;
	}

	/**
	 * 
	 */
	public void setParticipationVO(ParticipationVO participationVO) {
		this.participationVO = participationVO;
	}

	/**
	 * 
	 * @return
	 */
	public ParticipationVO getParticipationVO() {
		return participationVO;
	}


	/*
	public BufferedImage getBufferedImageCopy() {
		return bufferedImageCopy;
	}


	public void setBufferedImageCopy(BufferedImage bufferedImageCopy) {
		this.bufferedImageCopy = bufferedImageCopy;
	} 
	*/ 
	
	
	
}