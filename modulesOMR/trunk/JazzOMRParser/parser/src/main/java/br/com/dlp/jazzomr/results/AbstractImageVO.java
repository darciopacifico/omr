package br.com.dlp.jazzomr.results;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import br.com.dlp.jazzomr.AbstractBaseVO;

public abstract class AbstractImageVO<PK extends Serializable> extends AbstractBaseVO<PK> {

	private static final long serialVersionUID = -2413624468791667420L;

	private BufferedImage image;
	private String titulo;

	public AbstractImageVO() {
		super();
	}

	public AbstractImageVO(PK pk) {
		super(pk);
	}


	/**
	 * @return the image
	 */
	@XmlElement(type=JazzImageTypeCreator.class)
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

}