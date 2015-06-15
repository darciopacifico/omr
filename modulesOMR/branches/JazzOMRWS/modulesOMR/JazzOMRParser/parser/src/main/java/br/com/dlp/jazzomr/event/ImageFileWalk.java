/**
 * 
 */
package br.com.dlp.jazzomr.event;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import br.com.dlp.framework.exception.JazzRuntimeException;

/**
 * Navega pela estrutura de um arquivo ZIP recursivamente ate encontrar as imagens contidas no ZIP, entao aciona o processamento das mesmas
 * 
 * @author darcio
 * 
 */
@Component("imageFileWalk")
public class ImageFileWalk extends AbstractFileWalk {

	/**
	  	 (java.lang.String) application/vnd.openxmlformats-officedocument.wordprocessingml.document
	 (java.lang.String) application/msword
	 (java.lang.String) application/pdf
	 (java.lang.String) application/x-tar
	 (java.lang.String) application/x-rar
	 (java.lang.String) image/png
	 (java.lang.String) image/gif
	 (java.lang.String) image/jpeg
	 (java.lang.String) zipedExams.zip
	 (java.lang.String) application/zip
	 */
	
	public ImageFileWalk() {
	}

	/**
	 * Processa os bytes de um arquivo compactado. Visita todos os arquivos contidos no ZIP, detecta o mimetype e aciona um novo IImageFileWalk, ate
	 * chegar aas folhas da estrutura, que são as imagens de exames
	 */
	@Override
	public void processFile(byte[] bytes, IImageParserVisitor visitor) {
		
		
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		BufferedImage image;
		
		try {
			image = ImageIO.read(bais);
		} catch (IOException e) {
			throw new JazzRuntimeException("Erro ao tentar ler stream de origem da imagem",e);
		}
		
		visitor.visit(image);
	}

}
