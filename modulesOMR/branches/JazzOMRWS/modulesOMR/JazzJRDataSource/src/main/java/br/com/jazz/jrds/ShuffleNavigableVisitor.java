/**
 * 
 */
package br.com.jazz.jrds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;
import org.jfree.util.Log;

/**
 * @author darciopa
 *
 */
public class ShuffleNavigableVisitor implements NavigableVisitor {

	public static final String PARAM_EXAM_VARS = "examVariants";
	
	private Map<String, Object> parameters;
	private Map<Integer, Iterator> shuffledIterators = new HashMap<Integer, Iterator>();
	
	/**
	 * Constroi visitor para gerenciar as versoes de examVariants
	 * @param qtdVersions
	 */
	public ShuffleNavigableVisitor() {
		
	}

	
	

	@Override
	public Iterator createIterator(JazzJRNavigableField jazzJRNavigableField, Collection sortedColl) {
		Integer examVars = getParamExamVars();
		if(examVars==null || examVars<1){
			Log.error("O parametro '"+PARAM_EXAM_VARS+"' nao foi informado ou nao e valido ("+examVars+") ");
			Log.error("Prosseguindo com iterator padrao sem randomização ");
			return sortedColl.iterator();
		}
		
		
		Integer parentHashCode = jazzJRNavigableField.getParentField().getValue().hashCode();
		
		Iterator shuffledColls = this.shuffledIterators.get(parentHashCode);
		
		if(shuffledColls==null){
			
			
			shuffledColls = createCircularIterator(sortedColl, examVars);
			this.shuffledIterators.put(parentHashCode, shuffledColls);
		}else{
			//System.out.println("@@@@@@@@@@@ Shuff do cache "+parentHashCode+" @@@@@@@@@@@");
		}
			
		Collection shuffledColl = (Collection) shuffledColls.next();
		
		return shuffledColl.iterator();
		
	}




	protected Integer getParamExamVars(){
		return (Integer) this.parameters.get(PARAM_EXAM_VARS);
	}


	protected Iterator createCircularIterator(Collection coll, Integer qtdVersions) {
		
			List shuffledCollections = new ArrayList();
			
			for(int i=1; i<=qtdVersions; i++){
				List list = shuffleCollection(coll);
				shuffledCollections.add(list);
			}
			
			Iterator circularIterator = IteratorUtils.loopingIterator(shuffledCollections);
			
			return circularIterator;
			
	}




	protected List shuffleCollection(Collection coll) {
		List list = new ArrayList(coll.size());
		list.addAll(coll);
		Collections.shuffle(list);
		return list;
	}




	public Map<String, Object> getParameters() {
		return parameters;
	}




	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	
	
	
}
