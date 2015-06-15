/**
 * 
 */
package br.com.dlp.qrcode.decoder;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/**
 * @author t_dpacifico
 * 
 */
public class JazzQRCodeReader {
	public static Logger log = LoggerFactory.getLogger(JazzQRCodeReader.class);
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws FormatException
	 * @throws ChecksumException
	 * @throws NotFoundException
	 */
	public static void main(String[] args) throws IOException, NotFoundException, ChecksumException, FormatException {
		
		/*
		 * JazzQRCode.decode("1.gif"); JazzQRCode.decode("2.gif"); JazzQRCode.decode("13.jpg"); JazzQRCode.decode("qrCode.gif");
		 */
		long tempo = System.currentTimeMillis();
		
		//JazzQRCodeReader.decode("/home/darcio/lixo/qrCodeOK.jpg");
		//JazzQRCodeReader.decode("/home/darcio/lixo/naoLeuQRCode_novaDigit_soQr.jpg");
		JazzQRCodeReader.decode("/home/darcio/Downloads/lixo/NovoQRnaoLe.gif");
		//JazzQRCodeReader.decode("/home/darcio/lixo/tentativaLerQRCode.png");
		
		//[(8.5,75.0), (218.5,75.0)]
		log.debug((System.currentTimeMillis()-tempo)+"");
		
	}
	
	/**
	 * @param pathname
	 * @throws IOException
	 * @throws NotFoundException
	 * @throws ChecksumException
	 * @throws FormatException
	 */
	protected static void decode(String pathname) throws IOException, NotFoundException, ChecksumException, FormatException {
		log.debug(pathname);
	
		BufferedImage image = getImage(pathname);
		
		
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		
		HybridBinarizer binarizer = new HybridBinarizer(source);
		
		Hashtable hints = new Hashtable();
		hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		
	
		BinaryBitmap bitmap = new BinaryBitmap(binarizer);
	
		QRCodeReader reader = new QRCodeReader();
		
		
		long now = System.currentTimeMillis();
		Result result;
		try {
			result = reader.decode(bitmap);
			log.warn(result+"");
		} catch (Exception e) {
			log.warn(e.getMessage(),e);
		}
		System.out.println(System.currentTimeMillis()-now);
		
	}

	/**
	 * @param pathname
	 * @return
	 * @throws IOException
	 */
	protected static BufferedImage getImage(String pathname) throws IOException {
		BufferedImage img = ImageIO.read(new File(pathname));
		
		double maxArea = (416*383)/4;
		
		double area = img.getHeight()*img.getWidth();
		
		if(area>maxArea){
		
			AffineTransform affineTransform = AffineTransform.getScaleInstance(maxArea/area, maxArea/area);
			AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BICUBIC);
			img = affineTransformOp.filter(img, null);

		}
		
		return img;
	}
	
}
