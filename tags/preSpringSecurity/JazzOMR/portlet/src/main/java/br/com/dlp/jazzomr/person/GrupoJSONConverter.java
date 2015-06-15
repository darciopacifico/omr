/**
 * 
 */
package br.com.dlp.jazzomr.person;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.dlp.jazzomr.converter.AbstractJSONConverter;

/**
 * Converson json para pessoavo. Mera formalidade
 * @author darcio
 *
 */
public class GrupoJSONConverter extends AbstractJSONConverter {

	public GrupoJSONConverter() {
	}

	@Override
	protected Class<? extends Object> getClassType() {
		return GrupoVO.class;
	}

	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if(value instanceof GrupoVO){
			GrupoVO grupoVO = (GrupoVO) value;
			grupoVO.setPessoas(null);
		}
		
		return super.getAsString(context, component, value);
	}
	
	
}
