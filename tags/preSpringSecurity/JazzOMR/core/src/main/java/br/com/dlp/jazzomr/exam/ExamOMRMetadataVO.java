/**
 * 
 */
package br.com.dlp.jazzomr.exam;

import javax.persistence.Entity;

import br.com.dlp.jazzomr.base.AbstractEntityVO;
import br.com.jazz.codegen.annotation.JazzClass;

/**
 * Meta dados do padrao OMR do exame.
 * Normalmente um conjunto destes objetos esta associado a um exame para que seja possivel processar as marcas omr reconhecidas
 * 
 * @author darcio
 *
 */
@Entity
@JazzClass(name="ExamOMRMetadata",voMestre=ExamVO.class)
public class ExamOMRMetadataVO extends AbstractEntityVO {

	private static final long serialVersionUID = -3304427598526145905L;

	private String omrKey;
	private Double x;
	private Double y;
	
	public ExamOMRMetadataVO() {
	}

	public ExamOMRMetadataVO(Long pk) {
		super(pk);
	}

	
	/**
	 * @return the omrKey
	 */
	public String getOmrKey() {
		return omrKey;
	}

	/**
	 * @return the x
	 */
	public Double getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public Double getY() {
		return y;
	}

	/**
	 * @param omrKey the omrKey to set
	 */
	public void setOmrKey(String omrKey) {
		this.omrKey = omrKey;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(Double x) {
		this.x = x;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(Double y) {
		this.y = y;
	}

	
	
}
