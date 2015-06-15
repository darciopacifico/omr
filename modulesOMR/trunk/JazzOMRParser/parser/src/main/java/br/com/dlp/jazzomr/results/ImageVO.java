/**
 * 
 */
package br.com.dlp.jazzomr.results;


import java.awt.image.BufferedImage;

/**
 * @author darcio
 *
 */
public class ImageVO extends AbstractImageVO<Long> {

	private static final long serialVersionUID = -8993993066421518092L;
	
	public ImageVO() {
	}

	public ImageVO(Long pk) {
		super(pk);
	}
	public ImageVO(Long pk, String tituloImg, BufferedImage img) {
		super(pk);
		setTitulo(tituloImg);
		setImage(img);
	}

	@Override
	public Long getPK() {
		return this.pk;
	}	
}
