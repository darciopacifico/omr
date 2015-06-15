/**
 * 
 */
package br.com.dlp.jazzomr.exam;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.dlp.jazzomr.converter.AbstractJSONConverter;

/**
 * Converson json para ExamVO. Mera formalidade
 * @author darcio
 *
 */
public class ExamJSONConverter extends AbstractJSONConverter {

	public ExamJSONConverter() {
	}

	@Override
	protected Class<? extends Object> getClassType() {
		return ExamVO.class;
	}

	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		ExamVO examVO = (ExamVO) value;
		
		examVO.setExamOMRMetadataVO(null);
		examVO.setQuestionnaires(null);
		
		return super.getAsString(context, component, examVO);
	}
	
}