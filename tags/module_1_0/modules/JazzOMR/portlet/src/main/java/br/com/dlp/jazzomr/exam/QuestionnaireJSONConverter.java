/**
 * 
 */
package br.com.dlp.jazzomr.exam;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.dlp.jazzomr.converter.AbstractJSONConverter;


/**
 * Converson json para pessoavo. Mera formalidade
 * @author darcio
 *
 */
public class QuestionnaireJSONConverter extends AbstractJSONConverter {

	public QuestionnaireJSONConverter() {
	}

	@Override
	protected Class<? extends Object> getClassType() {
		return QuestionnaireVO.class;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		QuestionnaireVO questionnaireVO = (QuestionnaireVO) value;
		
		questionnaireVO.setQuestions(null);
		questionnaireVO.setEmpresaVO(null);
				
		// TODO Auto-generated method stub
		return super.getAsString(context, component, questionnaireVO);
	}
	
}
