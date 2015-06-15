/**
 * 
 */
package br.com.dlp.jazzomr.results;

import java.awt.image.BufferedImage;

import javax.persistence.Entity;
import javax.persistence.Lob;

import br.com.dlp.jazzomr.base.AbstractEntityVO;
import br.com.jazz.codegen.annotation.JazzClass;

/**
 * @author darcio
 *
 */
@Entity
@JazzClass(voMestre=PayloadVO.class,name="Imagem")
public class ImageVO extends AbstractEntityVO {

	private static final long serialVersionUID = -8993993066421518092L;
	
	private BufferedImage image;
	
	private String titulo;
	
	public ImageVO() {
	}

	public ImageVO(Long pk) {
		super(pk);
	}

	/**
	 * @return the image
	 */
	@Lob
	@org.hibernate.annotations.Type(type="br.com.dlp.jazzomr.JazzImageType")
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
