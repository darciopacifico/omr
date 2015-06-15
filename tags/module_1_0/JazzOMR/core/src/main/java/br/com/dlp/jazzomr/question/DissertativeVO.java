package br.com.dlp.jazzomr.question;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.jazz.codegen.annotation.JazzClass;


/**
 * 
 * @author darcio
 */
@Entity
@DiscriminatorValue("D")
@JazzClass(name="Critério Dissertativo", voMestre=QuestionVO.class)
public class DissertativeVO extends AbstractCriterionVO  {
	
	private static final long serialVersionUID = -2069013206921764093L;

	private Integer qtdLinhas=5;
	private Boolean required=true;

	private Integer maxPonto=1;
	
	
	/**
	 * default constructor
	 */
	public DissertativeVO() {
	}

	public DissertativeVO(String description) {
		this.description = description;
	}
	

	public Integer getQtdLinhas() {
		return qtdLinhas;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setQtdLinhas(Integer linhasDescricao) {
		this.qtdLinhas = linhasDescricao;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Integer getMaxPonto() {
		return maxPonto;
	}

	public void setMaxPonto(Integer maxPontos) {
		this.maxPonto = maxPontos;
	}


}
