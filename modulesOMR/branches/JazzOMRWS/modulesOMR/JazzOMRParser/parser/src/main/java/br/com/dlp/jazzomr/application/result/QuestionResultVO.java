package  br.com.dlp.jazzomr.application.result;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
/**
 * 
 * @author darciopa
 */
public class QuestionResultVO implements Serializable {

	private static final long serialVersionUID = 5790125472473421892L;

	private Long questionPK;
	private Integer examVar; 
	private Integer page;
	private Long participation;
	private Long pessoaPK;
	
	
	private Set<CriterionResultVO> criterionResultVOs = new HashSet<CriterionResultVO>();
	private Float score;

	
	public Long getQuestionPK() {
		return questionPK;
	}
	public Integer getExamVar() {
		return examVar;
	}
	public Set<CriterionResultVO> getCriterionResultVOs() {
		return criterionResultVOs;
	}
	public Float getScore() {
		return score;
	}
	public void setQuestionPK(Long questionPK) {
		this.questionPK = questionPK;
	}
	public void setExamVar(Integer examVar) {
		this.examVar = examVar;
	}
	public void setCriterionResultVOs(Set<CriterionResultVO> criterionResultVOs) {
		this.criterionResultVOs = criterionResultVOs;
	}
	public void setScore(Float score) {
		this.score = score;
	}
	public Integer getPage() {
		return page;
	}
	public Long getParticipation() {
		return participation;
	}
	public Long getPessoaPK() {
		return pessoaPK;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public void setParticipation(Long participation) {
		this.participation = participation;
	}
	public void setPessoaPK(Long pessoa) {
		this.pessoaPK = pessoa;
	}
	
	
	
	
	

	
	
}
