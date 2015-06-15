package br.com.dlp.jazzomr.qrcodereader;

import java.awt.image.BufferedImage;

import org.springframework.stereotype.Component;

import br.com.dlp.jazzomr.parser.IdentityRegion;

@Component("qrCodeReader180Scaled")
public class QRCodeReader180ScaleStrategy extends QRCodeReader180GStrategy {

	double maxArea = (416*383)/4;
	
	public QRCodeReader180ScaleStrategy() {
		super();
	}
	
	
	/**
	 * @param idRegionRel
	 * @param fullImage
	 * @return
	 */
	protected BufferedImage getIdentityImage(IdentityRegion idRegionRel, BufferedImage fullImage) {
		
		BufferedImage imgId1 = getIdentityImage180(idRegionRel, fullImage);
		
		QRCodeReaderScaleStrategy scaleStrategy = new QRCodeReaderScaleStrategy();
		
		imgId1 = scaleStrategy.scaleToMaxArea(imgId1);
		
		return imgId1;
	}

	
	@Override
	public int getOrder() {
		return 40;
	}	
}
