package br.com.dlp.jazzomr.jr.handler;

import java.awt.image.BufferedImage;
import java.util.Hashtable;

import br.com.dlp.jazzomr.jr.util.IQRCodeGenerator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * Classe utilitaria para uso com JasperReports. Fonte geradora de imagem de QRCode
 * @author darcio
 */
public class JazzQRCodeGenerator implements IQRCodeGenerator{
	private static final long serialVersionUID = 4565254883883489767L;
	
	protected static final int DEFAULT_WIDTH = 400;
	protected static final int DEFAULT_HEIGTH = 400;

	/* (non-Javadoc)
	 * @see br.com.dlp.jazzomr.export.IQRCodeGenerator#generateQRCode(java.lang.String, int, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public BufferedImage generateQRCode(String value, int w, int h){
		Writer writer = new MultiFormatWriter();
				
		@SuppressWarnings("rawtypes")
		Hashtable hints = new Hashtable();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		hints.put(EncodeHintType.CHARACTER_SET, "ISO-8859-1");
		
		BitMatrix matrix;
		try {
			matrix = writer.encode(value, BarcodeFormat.QR_CODE, w, h,hints);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao tentar criar bitMatrix para qrcode!",e);
		}
		
		BufferedImage buffImage = MatrixToImageWriter.toBufferedImage(matrix);
		
		return buffImage;
	}

	

	/* (non-Javadoc)
	 * @see br.com.dlp.jazzomr.export.IQRCodeGenerator#generateQRCode(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public BufferedImage generateQRCode(String value){
		return generateQRCode(value, JazzQRCodeGenerator.DEFAULT_WIDTH, JazzQRCodeGenerator.DEFAULT_HEIGTH);
	}


}
