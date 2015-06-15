/**
 * 
 */
package br.com.dlp.jazzomr.exam;

import java.io.Serializable;

import javax.persistence.Entity;

/**
 * Meta dados do padrao OMR do exame.
 * Normalmente um conjunto destes objetos esta associado a um exame para que seja possivel processar as marcas omr reconhecidas
 * 
 * @author darcio
 *
 */
@Entity
public class ExamOMRMetadataVO implements Serializable {

	private static final long serialVersionUID = -3304427598526145905L;

	private String omrKey;
	private Double x;
	private Double y;
	
	public ExamOMRMetadataVO() {
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
