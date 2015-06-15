package br.com.dlp.jazzomr.qrcodereader;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import org.springframework.stereotype.Component;

import br.com.dlp.framework.gof.strategy.StrategyExecutionException;
import br.com.dlp.jazzomr.parser.IdentityRegion;
import br.com.dlp.jazzomr.parser.QRCodeReaderParam;

@Component("qrCodeReader180G")
public class QRCodeReader180GStrategy extends QRCodeReaderStrategy {

	protected static final int _180_DEGREE_TURN = 180;

	/**
	 * Construtor padrao
	 */
	public QRCodeReader180GStrategy() {
		super();
	}


	/**
	 * @param idRegionRel
	 * @param fullImage
	 * @return
	 */
	protected BufferedImage getIdentityImage(IdentityRegion idRegionRel, BufferedImage fullImage) {
		
		BufferedImage imgId = getIdentityImage180(idRegionRel, fullImage);
		
		return imgId;
	}

	@Override
	public String execute(QRCodeReaderParam payload) throws StrategyExecutionException {
		String result = super.execute(payload);
		
		BufferedImage fullImage = payload.getImageDocVO().getBufferedImage();
		
		AffineTransform _180Transform = create180DegreeTransform(fullImage.getWidth(), fullImage.getHeight());
		
		AffineTransformOp transformOp = new AffineTransformOp(_180Transform, null);
		
		fullImage = transformOp.filter(fullImage, null);
		
		payload.getImageDocVO().setBufferedImage(fullImage);
		
		return result;
	}
	

	/**
	 * @param idRegionRel
	 * @param fullImage
	 * @return
	 */
	protected BufferedImage getIdentityImage180(IdentityRegion idRegionRel, BufferedImage fullImage) {
		int imageWidth = fullImage.getWidth();
		int imageHeight = fullImage.getHeight();
		
		int x = (int) Math.floor(imageWidth * idRegionRel.getXrel());
		int y = (int) Math.floor(imageHeight * idRegionRel.getYrel());
		int w = (int) Math.floor(imageWidth * idRegionRel.getWrel());
		int h = (int) Math.floor(imageHeight * idRegionRel.getHrel());
		
		Point2D.Double pt2 = rotate180Point2(imageWidth, imageHeight, x, y, w, h);
		
		x = (int) Math.round(pt2.getX());
		y = (int) Math.round(pt2.getY());
		
		BufferedImage imgId1 = fullImage.getSubimage(x, y, w, h);
		BufferedImage imgId = imgId1;
		return imgId;
	}


	/**
	 * @param imageWidth
	 * @param imageHeight
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	protected Point2D.Double rotate180Point2(int imageWidth, int imageHeight, int x, int y, int w, int h) {
		AffineTransform affineTransform = create180DegreeTransform(imageWidth, imageHeight);
		Point2D.Double pt2 = new Point2D.Double(x + w, y + h);
		pt2 = (java.awt.geom.Point2D.Double) affineTransform.transform(pt2, pt2);
		return pt2;
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

	
	@Override
	public int getOrder() {
		return 20;
	}
}
