/**
 * 
 */
package br.com.dlp.jazzomr.person;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.dlp.jazzomr.converter.AbstractJSONConverter;
import br.com.dlp.jazzomr.empresa.RelatorioVO;

/**
 * Converson json para pessoavo. Mera formalidade
 * @author darcio
 *
 */
public class RelatorioJSONConverter extends AbstractJSONConverter {

	
	public RelatorioJSONConverter() {
	}

	@Override
	protected Class<? extends Object> getClassType() {
		return RelatorioVO.class;
	}

	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if(value instanceof RelatorioVO){
			RelatorioVO relatorioVO = (RelatorioVO) value;
			relatorioVO.setExamOMRMetadataVO(null);
			relatorioVO.setJrFileVOs(null);
		}
		
		String jsonString = super.getAsString(context, component, value);
		
		return jsonString;
	}
	
	
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String strValue) {
		return super.getAsObject(context, component, strValue);
	}
	
	
}
