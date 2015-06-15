/**
 * 
 */
package br.com.dlp.jazzomr.person;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.dlp.jazzomr.converter.AbstractJSONConverter;
import br.com.dlp.jazzomr.empresa.EmpresaVO;

/**
 * Converson json para pessoavo. Mera formalidade
 * @author darcio
 *
 */
public class EmpresaJSONConverter extends AbstractJSONConverter {

	public EmpresaJSONConverter() {
	}

	@Override
	protected Class<? extends Object> getClassType() {
		return EmpresaVO.class;
	}

	

	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if(value instanceof EmpresaVO){
			EmpresaVO empresaVO = (EmpresaVO) value;
			empresaVO.setLogos(null);
		}
		
		String jsonString = super.getAsString(context, component, value);
		
		return jsonString;
	}
	
	
}
