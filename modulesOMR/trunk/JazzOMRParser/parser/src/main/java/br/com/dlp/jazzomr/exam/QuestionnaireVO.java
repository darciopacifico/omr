/**
 * 
 */
package br.com.dlp.jazzomr.exam;


import java.util.ArrayList;
import java.util.List;

import br.com.dlp.jazzomr.AbstractBaseVO;
import br.com.dlp.jazzomr.question.QuestionVO;
import br.com.dlp.jazzomr.question.ShuffleQuestionsVisitor;
import br.com.jazz.jrds.annotations.Navigable;

/**
 * @author darcio
 *
 */
public class QuestionnaireVO extends AbstractBaseVO<Long> {

	private static final long serialVersionUID = 8060092252080747133L;

	public QuestionnaireVO() {}

	private String description;
	
	private List<QuestionVO> questions = new ArrayList<QuestionVO>(10);

	
	@Override
	public Long getPK() {
		return this.pk;
	}
	
	@Override
	public void setPK(Long ppk) {
		// TODO Auto-generated method stub
		super.setPK(ppk);
	}

	
	@Navigable(alias="perg",visitorClass=ShuffleQuestionsVisitor.class)
	public List<QuestionVO> getQuestions() {
		return questions;
	}
	
	
	/**
	 * @return the description
	 */
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
