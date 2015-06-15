/**
 * 
 */
package br.com.dlp.jazzomr.application.result;

import ij.measure.ResultsTable;

import java.awt.image.BufferedImage;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import br.com.dlp.jazzomr.base.AbstractEntityVO;
import br.com.dlp.jazzomr.event.ParticipationVO;
import br.com.dlp.jazzomr.exam.coordinate.CriterionCoordinateVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;

/**
 * @author darcio
 *
 */
@Entity
@JazzClass(name="Resultado de Criterio de Questao")
public class CriterionResultVO extends AbstractEntityVO<Long> {

	private static final long serialVersionUID = 1291184127243085273L;

	private CriterionCoordinateVO criterionCoordinateVO;
	
	private Boolean checked=false;

	private ParticipationVO participationVO;
	
	private String observacao;
	
	private Integer pontuacao;	
	
	
	@Transient
	private ResultsTable transientResultAnalysis;
	
	@Transient
	private BufferedImage transientImage;
	
	public CriterionResultVO() {}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}
	
	/**
	 * @return the participationVO
	 */
	@ManyToOne(fetch=FetchType.LAZY,optional=false)
	@JoinColumn(name="fk_participation")
	@JazzProp(ignore=true, name = "")
	public ParticipationVO getParticipationVO() {
		return participationVO;
	}

	/**
	 * @param participationVO the participationVO to set
	 */
	public void setParticipationVO(ParticipationVO participationVO) {
		this.participationVO = participationVO;
	}
	

	
	/**
	 * @return the criterionCoordinateVO
	 */
	@ManyToOne
	@JoinColumn(name="fk_criterion_coordinate")
	public CriterionCoordinateVO getCriterionCoordinateVO() {
		return criterionCoordinateVO;
	}

	/**
	 * @param criterionCoordinateVO the criterionCoordinateVO to set
	 */
	public void setCriterionCoordinateVO(CriterionCoordinateVO criterionCoordinateVO) {
		this.criterionCoordinateVO = criterionCoordinateVO;
	}
	
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
	 * @return the transientImage
	 */
	@Transient
	public BufferedImage getTransientImage() {
		return transientImage;
	}


	/**
	 * @param transientImage the transientImage to set
	 */
	public void setTransientImage(BufferedImage image) {
		this.transientImage = image;
	}


	@Lob
	public String getObservacao() {
		return observacao;
	}


	public Integer getPontuacao() {
		return pontuacao;
	}


	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}


	public void setPontuacao(Integer pontos) {
		this.pontuacao = pontos;
	}

	
	@Transient
	public Boolean isReviewed(){
		return (pontuacao!=null);
	}
	
	@Transient
	public Boolean isCriticized(){
		return !StringUtils.isBlank(observacao);
	}
	
}
