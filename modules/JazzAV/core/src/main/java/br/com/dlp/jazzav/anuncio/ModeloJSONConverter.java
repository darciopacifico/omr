/**
 * 
 */
package br.com.dlp.jazzav.anuncio;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import br.com.dlp.jazzav.converter.AbstractJSONConverter;

/**
 * Converson json para ModeloVO. Mera formalidade
 * @author darcio
 *
 */
public class ModeloJSONConverter extends AbstractJSONConverter {

	public ModeloJSONConverter() {
	}

	@Override
	protected Class<? extends Object> getClassType() {
		return ModeloAdesivoVO.class;
	}

	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		ModeloAdesivoVO modeloVO = (ModeloAdesivoVO) value;
		
		String asString = super.getAsString(context, component, modeloVO);
		return asString;
	}
	
}