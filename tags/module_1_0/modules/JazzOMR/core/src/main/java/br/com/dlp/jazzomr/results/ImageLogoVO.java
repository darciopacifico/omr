/**
 * 
 */
package br.com.dlp.jazzomr.results;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import br.com.jazz.codegen.annotation.JazzClass;

/**
 * @author darcio
 *
 */
@Entity
@JazzClass(voMestre=PayloadVO.class,name="Imagem")
public class ImageLogoVO extends AbstractImageVO<Long> {
	
	private static final long serialVersionUID = 890343809472845847L;

	public ImageLogoVO() {
	}

	public ImageLogoVO(Long pk) {
		super(pk);
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}
	
}
