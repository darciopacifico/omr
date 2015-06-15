package br.com.dlp.jazzomr.question;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import org.apache.commons.lang.BooleanUtils;

import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.JazzRenderer;


/**
 * 
 * @author darcio
 */
@Entity
@DiscriminatorValue("A")
@JazzClass(name="Alternativa", voMestre=QuestionVO.class)
public class AlternativeVO extends AbstractCriterionVO  {
	
	private static final long serialVersionUID = -2069013206921764093L;

	//TODO: TESTAR SE ISTO NAO VAI DEIXA NADA INCONSISTENTE
	private AlternativeScore score = AlternativeScore.WRONG;
	
	/**
	 * default constructor
	 */
	public AlternativeVO() {
	}

	/**
	 * @param pk
	 * @param resumo
	 */
	public AlternativeVO(String description) {
		this.description = description;
	}

	/**
	 * @param descricao
	 * @param score
	 */
	public AlternativeVO(String descricao, AlternativeScore score) {
		super();
		this.description = descricao;
		this.score = score;
	}

	/**
	 * @return the score
	 */
	@Enumerated(EnumType.ORDINAL)
	@JazzProp(name="Score",renderer=JazzRenderer.RADIO)
	public AlternativeScore getScore() {
		return score;
	}

	@Transient
	public Boolean getCorrect(){
		return AlternativeScore.CORRECT.equals(score);
	}
	
	@Transient	
	public void setCorrect(Boolean correct){
		
		if(BooleanUtils.isTrue(correct)){
			setScore(AlternativeScore.CORRECT);
		}else{
			setScore(AlternativeScore.WRONG);
		}
		
	}
	
	
	/**
	 * @param score
	 *          the score to set
	 */
	public void setScore(AlternativeScore score) {
		this.score = score;
	}

}
