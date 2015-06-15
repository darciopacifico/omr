/**
 * 
 */
package br.com.dlp.jazzomr.application.result;


import ij.measure.ResultsTable;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import br.com.dlp.jazzomr.exam.coordinate.OMRMark;
import br.com.dlp.jazzomr.results.JazzImageTypeCreator;

/**
 * @author darcio
 */
public class CriterionResultVO implements Serializable {

	private static final long serialVersionUID = 1291184127243085273L;

	@XmlTransient
	@Transient
	private ResultsTable transientResultAnalysis;
	private Boolean checked=false;
	private BufferedImage croppedImage;
	
	
	private String chave; 
	private String criterion;
	private String critType;
	private Long pergunta;
	
	
	private OMRMark omrMark;

	
	public CriterionResultVO() {}
	
	/**
	 * @return the checked
	 */
	public Boolean getChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	/**
	 * @return the transientResultAnalysis
	 */
	@XmlTransient
	@Transient
	public ResultsTable getTransientResultAnalysis() {
		return transientResultAnalysis;
	}

	/**
	 * @param transientResultAnalysis the transientResultAnalysis to set
	 */
	public void setTransientResultAnalysis(ResultsTable resultAnalysis) {
		this.transientResultAnalysis = resultAnalysis;
	}

	/**
	 * @return the croppedImage
	 */
	
	@XmlElement(type=JazzImageTypeCreator.class)
	@XmlTransient
	public BufferedImage getCroppedImage() {
		return croppedImage;
	}


	/**
	 * @param croppedImage the croppedImage to set
	 */
	public void setCroppedImage(BufferedImage image) {
		this.croppedImage = image;
	}
	
	public String toString() {
			String val = ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
			return val;
	}


	public String getCritType() {
		return critType;
	}

	public void setCritType(String critType) {
		this.critType = critType;
	}

	@Transient
	@XmlTransient
	public OMRMark getOmrMark() {
		return omrMark;
	}


	public void setOmrMark(OMRMark omrMark) {
		this.omrMark = omrMark;
	}

	public String getChave() {
		return chave;
	}

	public String getCriterion() {
		return criterion;
	}

	public Long getPergunta() {
		return pergunta;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public void setCriterion(String criterion) {
		this.criterion = criterion;
	}

	public void setPergunta(Long pergunta) {
		this.pergunta = pergunta;
	}


}
