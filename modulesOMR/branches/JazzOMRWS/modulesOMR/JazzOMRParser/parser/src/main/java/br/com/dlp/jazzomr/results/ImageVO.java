/**
 * 
 */
package br.com.dlp.jazzomr.results;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author darcio
 *
 */
@Entity
public class ImageVO extends AbstractImageVO<Long> {

	private static final long serialVersionUID = -8993993066421518092L;
	
	public ImageVO() {
	}

	public ImageVO(Long pk) {
		super(pk);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}	
	
}
