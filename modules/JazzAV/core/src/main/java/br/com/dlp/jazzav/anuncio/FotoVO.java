package br.com.dlp.jazzav.anuncio;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.dlp.jazzav.results.AbstractImageVO;


/**
 * Fotos do anuncio
 * @author darcio
 */
@Entity
public class FotoVO extends AbstractImageVO<Long> {

	private static final long serialVersionUID = -8606170799947570785L;

	public FotoVO() {
	}

	public FotoVO(Long pk) {
		super(pk);
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}
	
	
}
