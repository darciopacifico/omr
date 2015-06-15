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
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dlp.jazzomr.event.ParticipationVO;
import br.com.jazz.jrds.JazzJRNavigableField;
import br.com.jazz.jrds.NavigableVisitor;

/**
 * @author darciopa
 *
 */
public class ShuffleCriterionsVisitor implements NavigableVisitor {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Map<String, Object> parameters;
	private Map<String, List<CriterionVO>> mapExaVarCriterions = new HashMap<String, List<CriterionVO>>();
	
	/**
	 * Constroi visitor para gerenciar as versoes de examVariants
	 * @param qtdVersions
	 */
	public ShuffleCriterionsVisitor() {
		System.out.println("criando");
	}

	
	

	@Override
	public Iterator createIterator(JazzJRNavigableField jazzJRNavigableField, Collection sortedColl) {
		
		ParticipationVO participationVO = (ParticipationVO) jazzJRNavigableField.getParentField().getParentField().getParentField().getParentField().getValue();
		
		QuestionVO questionVO = (QuestionVO) jazzJRNavigableField.getParentField().getValue();
		
		Integer exVar = participationVO.getExamVariant();
		
		String chaveMapa = exVar+"-"+questionVO.getPK();
		List<CriterionVO> criterionsShuffled = mapExaVarCriterions.get(chaveMapa);
		

		if(criterionsShuffled==null){
			 Set<CriterionVO> originalCriterions = questionVO.getCriterionVOs();
		
			 criterionsShuffled = new ArrayList<CriterionVO>(originalCriterions);
			 
			 Collections.shuffle(criterionsShuffled);
			 
			 mapExaVarCriterions.put(chaveMapa, criterionsShuffled);
		}else{
			
			if(log.isDebugEnabled()){
				log.debug("Ja existe uma colecao de criterions randomizada para a variacao de exame "+chaveMapa);
			}
			
		}
		
		
		return criterionsShuffled.iterator();
		
	}



	public Map<String, Object> getParameters() {
		return parameters;
	}


	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}	
	
	

}
