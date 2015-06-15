/**
 * 
 */
package br.com.dlp.jazzomr.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlMimeType;

import org.hibernate.validator.constraints.NotEmpty;

import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.dlp.jazzomr.empresa.RelatorioVO;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.exam.QuestionnaireVO;
import br.com.dlp.jazzomr.exam.coordinate.OMRMark;
import br.com.dlp.jazzomr.question.QuestionVO;
import br.com.jazz.jrds.annotations.Navigable;

/**
 * @author darcio
 *
 */
public class EventVO extends AbstractBaseVO<Long> {

	private static final long serialVersionUID = -4019759324155704476L;
  
	private String description;
	private Map<Long,ParticipationVO> participations = new HashMap<Long,ParticipationVO>(80);
	
	private ExamVO examVO; 
	
	private Map<String,Set<String>> mapPerguntasByExamVarPage = new HashMap<String,Set<String>>();
	private Map<String,Set<OMRMark>> mapOMRMarksByPergExamVarPage  = new HashMap<String, Set<OMRMark>>();
	private RelatorioVO relatorioVO = new RelatorioVO();
	
	private DataHandler dataHandlerProva; 
	
	@XmlMimeType("application/pdf")
	public DataHandler getDataHandlerProva() {
		
		return dataHandlerProva;
	}

	public void setDataHandlerProva(DataHandler dataHandlerProva) {
		this.dataHandlerProva = dataHandlerProva;
	}

	
	public EventVO() {
	}
	
	public Long getPK() {
		return this.pk;
	}

	@Override
	public void setPK(Long ppk) {
		// TODO Auto-generated method stub
		super.setPK(ppk);
	}
	

	public Set<OMRMark> findOMRMarks(QuestionVO questionVO, Integer page, Integer examVarId) {
		Set<OMRMark> omrMarks = this.getMapOMRMarksByPergExamVarPage().get(questionVO.getPK()+"-"+examVarId+"-"+page);
		return omrMarks;
	}
	
	/**
	 * 
	 * @param examVarId
	 * @param page
	 * @param participationId
	 * @param eventVO
	 * @return
	 */
	public Set<QuestionVO> findQuestions(Integer examVarId, Integer page) {
		
		Set<String> setQuestions = this.getMapPerguntasByExamVarPage().get(examVarId+"-"+page);

		Set<QuestionVO> questionsReturn = new HashSet<QuestionVO>();
		
		for (String strQuestionPk : setQuestions) {
			
			Long questionPk = new Long(strQuestionPk);
			
			List<QuestionnaireVO> questionnaires = getExamVO().getQuestionnaires();
			for (QuestionnaireVO questionnaireVO : questionnaires) {
				
				List<QuestionVO> questions = questionnaireVO.getQuestions();
				
				for (QuestionVO questionVO : questions) {
					
					if(questionVO.getPK().equals(questionPk)){
						questionsReturn.add(questionVO);
					}
				}
			}
		}
		return questionsReturn;
	}
	
	/**
	 * @return the participations
	 */
	@Navigable(alias="par")
	public Map<Long,ParticipationVO> getParticipations() {
		return participations;
	}
	
	/**
	 * @return the description
	 */
	@NotEmpty(message="Por favor, preencha a descrição do evento.")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public void setParticipations(Map<Long, ParticipationVO> participations) {
		this.participations = participations;
	}

	public RelatorioVO getRelatorioVO() {
		return relatorioVO;
	}

	public void setRelatorioVO(RelatorioVO relatorioVO) {
		this.relatorioVO = relatorioVO;
	}

	public Map<String, Set<String>> getMapPerguntasByExamVarPage() {
		return mapPerguntasByExamVarPage;
	}

	public Map<String, Set<OMRMark>> getMapOMRMarksByPergExamVarPage() {
		return mapOMRMarksByPergExamVarPage;
	}

	public void setMapPerguntasByExamVarPage(Map<String,Set<String>> mapPerguntasByExamVarPage) {
		this.mapPerguntasByExamVarPage = mapPerguntasByExamVarPage;
	}

	public void setMapOMRMarksByPergExamVarPage(Map<String, Set<OMRMark>> mapOMRMarksByPergExamVarPage) {
		this.mapOMRMarksByPergExamVarPage = mapOMRMarksByPergExamVarPage;
	}

	public ExamVO getExamVO() {
		return examVO;
	}

	public void setExamVO(ExamVO examVO) {
		this.examVO = examVO;
	}
	
}
