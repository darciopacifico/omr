package br.com.dlp.jazzomr.qrcodereader;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import org.springframework.stereotype.Component;

import br.com.dlp.jazzomr.parser.IdentityRegion;

@Component("qrCodeReaderScaled")
public class QRCodeReaderScaleStrategy extends QRCodeReaderStrategy {

	double maxArea = (416*383)/3;
	
	public QRCodeReaderScaleStrategy() {
		super();
	}

	
	@Override
	protected BufferedImage getIdentityImage(IdentityRegion idRegionRel, BufferedImage fullImage) {
		BufferedImage identityImage = super.getIdentityImage(idRegionRel, fullImage);
		
		identityImage = scaleToMaxArea(identityImage);
		
		return identityImage;
	}
	

	/**
	 * @param payload
	 * @return
	 */
	protected BufferedImage scaleToMaxArea(BufferedImage payload) {
		
		double area = payload.getHeight()*payload.getWidth();
		
		if(area>maxArea){
		
			AffineTransform affineTransform = AffineTransform.getScaleInstance(maxArea/area, maxArea/area);
			AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BICUBIC);
			payload = affineTransformOp.filter(payload, null);

		}
		return payload;
	}
	
	
	/**
	 * @return the maxArea
	 */
	public double getMaxArea() {
		return maxArea;
	}

	/**
	 * @param maxArea the maxArea to set
	 */
	public void setMaxArea(double maxArea) {
		this.maxArea = maxArea;
	}
	
	@Override
	public int getOrder() {
		return 30;
	}	
}
