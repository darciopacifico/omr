/**
 * 
 */
package br.com.dlp.jazzomr.question;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dlp.jazzomr.event.ParticipationVO;
import br.com.dlp.jazzomr.exam.QuestionnaireVO;
import br.com.jazz.jrds.JazzJRNavigableField;
import br.com.jazz.jrds.NavigableVisitor;

/**
 * @author darciopa
 *
 */
public class ShuffleQuestionsVisitor implements NavigableVisitor {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Map<String, Object> parameters;
	private Map<String, List<QuestionVO>> mapExaVarQuestions = new HashMap<String, List<QuestionVO>>();
	
	/**
	 * Constroi visitor para gerenciar as versoes de examVariants
	 * @param qtdVersions
	 */
	public ShuffleQuestionsVisitor() {
		System.out.println("criando");
	}

	
	

	@Override
	public Iterator createIterator(JazzJRNavigableField jazzJRNavigableField, Collection sortedColl) {
		
		ParticipationVO participationVO = (ParticipationVO) jazzJRNavigableField.getParentField().getParentField().getParentField().getValue();
		
		QuestionnaireVO questionnaireVO = (QuestionnaireVO) jazzJRNavigableField.getParentField().getValue();
		
		Integer exVar = participationVO.getExamVariant();
		
		String chaveMapa = exVar+"-"+questionnaireVO.getPK();
		List<QuestionVO> questionsShuffled = mapExaVarQuestions.get(chaveMapa);
		

		if(questionsShuffled==null){
			 List<QuestionVO> originalQuestions = questionnaireVO.getQuestions();
		
			 questionsShuffled = new ArrayList<QuestionVO>(originalQuestions);
			 
			 Collections.shuffle(questionsShuffled);
			 
			 mapExaVarQuestions.put(chaveMapa, questionsShuffled);
		}else{
			
			if(log.isDebugEnabled()){
				log.debug("Ja existe uma colecao randomizada de questoes para a variacao de exame "+chaveMapa);
			}
			
		}
		
		
		return questionsShuffled.iterator();
		
	}



	public Map<String, Object> getParameters() {
		return parameters;
	}


	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}	
	
	

}
