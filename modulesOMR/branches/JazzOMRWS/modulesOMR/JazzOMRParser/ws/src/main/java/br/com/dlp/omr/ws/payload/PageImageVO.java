package br.com.dlp.omr.ws.payload;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import org.apache.cxf.aegis.type.java5.XmlElement;

import br.com.dlp.jazzomr.results.JazzImageTypeCreator;

/**
 * Apenas para conter imagens de ppaginas para correcao
 * @author darciopa
 *
 */
public class PageImageVO implements Serializable {

	private static final long serialVersionUID = -2989494148126986137L;
	
	private BufferedImage pageImage;

	@XmlElement(type=JazzImageTypeCreator.class)
	public BufferedImage getPageImage() {
		return pageImage;
	}

	public void setPageImage(BufferedImage pageImage) {
		this.pageImage = pageImage;
	}
	
}
