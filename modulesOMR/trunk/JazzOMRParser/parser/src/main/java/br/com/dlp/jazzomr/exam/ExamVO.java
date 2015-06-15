/**
 * 
 */
package br.com.dlp.jazzomr.exam;

import java.util.ArrayList;
import java.util.List;

import br.com.dlp.jazzomr.AbstractBaseVO;
import br.com.jazz.jrds.annotations.Navigable;

/**
 * @author darcio
 */
public class ExamVO extends AbstractBaseVO<Long>  {

	private static final long serialVersionUID = 7124967832372109142L;
	
	private String description;
	private List<QuestionnaireVO> questionnaires = new ArrayList<QuestionnaireVO>(10);
	
	/**
	 * Construtor padrao
	 */
	public ExamVO() {
	}

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
	@Navigable(alias="qnr")
	public List<QuestionnaireVO> getQuestionnaires() {
		return questionnaires;
	}
	
	/**
	 * @return the description
	 */
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
