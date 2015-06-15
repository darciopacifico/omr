/**
 * 
 */
package br.com.dlp.qrcode.decoder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dlp.jazzomr.poc.JazzOMRImageParser;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * @author t_dpacifico
 *
 */
public class JazzQRCodeWriter {
	public static Logger log = LoggerFactory.getLogger(JazzQRCodeWriter.class);
	/**
	 * @param args
	 * @throws WriterException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws WriterException, FileNotFoundException, IOException {
		Writer writer = new MultiFormatWriter();
		
		Hashtable hints = new Hashtable();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		hints.put(EncodeHintType.CHARACTER_SET, "ISO-8859-1");
		
		
		long now = System.currentTimeMillis();
		
		
		//String contents = gson.toJson(docPK);
		String contents = "12345-TESTE123";
		
		
		log.debug(contents.length()+"");
		
		BitMatrix matrix = writer.encode(
				contents
				, BarcodeFormat.QR_CODE, 350, 350,hints);
		
		
		
		
		BufferedImage buffImage = MatrixToImageWriter.toBufferedImage(matrix);

		
		
		ImageIO.write(buffImage, JazzOMRImageParser.IMG_EXT, new File("qrCodePerfeito.png"));
	}
	
	
	
	
}
