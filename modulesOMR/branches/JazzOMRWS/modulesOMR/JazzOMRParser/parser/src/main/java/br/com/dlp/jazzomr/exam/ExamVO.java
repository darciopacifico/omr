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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.jrds.annotations.Navigable;

/**
 * @author darcio
 */
@Entity
@JazzClass(name = "Exame")
public class ExamVO extends AbstractBaseVO<Long>  {

	private static final long serialVersionUID = 7124967832372109142L;
	
	private String description;
	private List<QuestionnaireVO> questionnaires = new ArrayList<QuestionnaireVO>(10);
	
	/**
	 * Construtor padrao
	 */
	public ExamVO() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}
	
	@Override
	public void setPK(Long ppk) {
		// TODO Auto-generated method stub
		super.setPK(ppk);
	}
	
	/**
	 * @return the questionnaires
	 */
	@JazzProp(name = "Questionários",searchable=false,listable=false)
	@ManyToMany(cascade = CascadeType.DETACH, fetch=FetchType.EAGER)
	@NotEmpty(message="Um exame deve possuir ao menos um questionário selecionado!")
	@Fetch(FetchMode.SELECT)
	@Navigable(alias="qnr")
	public List<QuestionnaireVO> getQuestionnaires() {
		return questionnaires;
	}
	
	/**
	 * @return the description
	 */
	@JazzProp(name="Descrição")
	@NotEmpty
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param questionnaires
	 *          the questionnaires to set
	 */
	public void setQuestionnaires(List<QuestionnaireVO> questionnaires) {
		this.questionnaires = questionnaires;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
