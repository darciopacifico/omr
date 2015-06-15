package br.com.dlp.jazzav.results;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import br.com.dlp.jazzav.AbstractEntityVO;

@MappedSuperclass
public abstract class AbstractImageVO<PK extends Serializable> extends AbstractEntityVO<PK> {

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
	@Lob
	@org.hibernate.annotations.Type(type = "br.com.dlp.jazzav.JazzImageType")
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