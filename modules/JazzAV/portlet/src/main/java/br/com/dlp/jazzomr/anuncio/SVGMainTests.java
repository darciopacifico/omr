package br.com.dlp.jazzomr.anuncio;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

public class SVGMainTests {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws TranscoderException 
	 */
	public static void main(String[] args) throws IOException, TranscoderException {
		
		InputStream resourceAsStream = SVGMainTests.class.getResourceAsStream("/br/com/dlp/jazzav/modeloanuncio/teste.svg");
		
		InputStream fis = resourceAsStream;
		
		FileOutputStream fos = new FileOutputStream("teste.png");
		
		Transcoder transcoder = new PNGTranscoder(); 
		
		Map hintMap = new HashMap();
		
		hintMap.put(PNGTranscoder.KEY_BACKGROUND_COLOR,Color.BLACK);
		
		
		
		
		transcoder.setTranscodingHints(hintMap);
		
		
		
		TranscoderInput in = new TranscoderInput(fis);
		TranscoderOutput out = new TranscoderOutput(fos);
		
		transcoder.transcode(in, out);
		
		fos.flush();
		fos.close();
		
		/*
		AffineTransform xform = new AffineTransform();
		
		xform.rotate(Math.toRadians(30));
		
		AffineTransformOp op = new AffineTransformOp(xform, AffineTransformOp.TYPE_BICUBIC);
		*/
		
	}
}