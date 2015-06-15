/**
 * 
 */
package br.com.dlp.jazzomr.exam;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.dlp.jazzav.converter.AbstractJSONConverter;
import br.com.dlp.jazzav.produto.OpcionalVO;

/**
 * Converson json para OpcionalVO. Mera formalidade
 * @author darcio
 *
 */
public class OpcionalJSONConverter extends AbstractJSONConverter {

	public OpcionalJSONConverter() {
	}

	@Override
	protected Class<? extends Object> getClassType() {
		return OpcionalVO.class;
	}

	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		OpcionalVO examVO = (OpcionalVO) value;
		
		String asString = super.getAsString(context, component, examVO);
		return asString;
	}
	
}