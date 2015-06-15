/**
 * 
 */
package br.com.dlp.jazzomr.exam;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.dlp.jazzomr.base.AbstractBelongsOrgVO;
import br.com.dlp.jazzomr.question.QuestionVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.ComparisonOperator;
import br.com.jazz.codegen.enums.JazzRenderer;

/**
 * @author darcio
 *
 */
@Entity
@JazzClass(name="Questionário",baseEntity=true)
/*
@FetchProfiles(value={
		
		@FetchProfile(name="questinario_com_questoes", fetchOverrides={
				@FetchProfile.FetchOverride(entity=QuestionVO.class	, association="criterionVOs", mode=FetchMode.JOIN)
		}),
		
})  
 */
public class QuestionnaireVO extends AbstractBelongsOrgVO<Long> {

	private static final long serialVersionUID = 8060092252080747133L;

	public QuestionnaireVO() {}

	private String description;
	
	public List<QuestionVO> questions = new ArrayList<QuestionVO>(10);

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}
	

	
	@JazzProp(name="Questões",renderer=JazzRenderer.SUBMODULE,listable=false, searchable=false)
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@Size(min=1, message="Adicione pelo menos uma questão!")
	@NotEmpty(message="Adicione pelo menos uma questão!")
	@Valid()
	@Fetch(FetchMode.SELECT)//coloquei este fetch pq o hibernate vai multiplicar os resultados, por nao se tratar de um set. Estúpido!
	@OrderBy("ordem")
	public List<QuestionVO> getQuestions() {
		return questions;
	}
	
	
	/**
	 * @return the description
	 */
	@JazzProp(size="150", name = "Descrição",renderer=JazzRenderer.TEXTAREA,sortable=true, comparison=ComparisonOperator.RANGE)
	@javax.persistence.Column(length=150)
	@Length(max=150, message="A descrição de um questionário deve possuir no máximo 150 caracteres!")
	@NotEmpty
	public String getDescription() {
		return description;
	}

	
	
	/**
	 * @param questions the questions to set
	 */
	public void setQuestions(List<QuestionVO> questions) {
		this.questions = questions;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
}
