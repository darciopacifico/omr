package br.com.dlp.jazzomr.parser;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import br.com.dlp.jazzomr.application.result.QuestionResultVO;
import br.com.dlp.jazzomr.event.EventVO;
import br.com.dlp.jazzomr.exceptions.JazzOMRRuntimeException;

public class JazzOMRParserCallable implements Callable<List<QuestionResultVO>> {
	
	private EventVO eventVO;
	private BufferedImage image;
	private JazzOMRImageParser parser;
	

	public JazzOMRParserCallable(EventVO eventVO, BufferedImage image,	JazzOMRImageParser parser) {
		super();
		this.eventVO = eventVO;
		this.image = image;
		this.parser = parser;
	}


	public JazzOMRParserCallable(EventVO eventVO, InputStream inputStream,	JazzOMRImageParser parser) {
		super();
		BufferedImage bImage;
		try {
			bImage = ImageIO.read(inputStream);
		} catch (IOException e) {
			throw new JazzOMRRuntimeException("Erro ao tentar ler imagem! "+e.getMessage(), e);
		}
		
		this.eventVO = eventVO;
		this.image = bImage;
		this.parser = parser;
		
	}


	@Override
	public List<QuestionResultVO> call() throws Exception {
		
		List<QuestionResultVO> results = parser.parseImage(image,eventVO);
		
		return results;
	}
	

}
