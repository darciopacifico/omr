/**
 * 
 */
package br.com.dlp.jazzav.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

//import br.com.dlp.jazzomr.base.IOrganizationRestrictVO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * TODO: IMPLEMENTAR UM CONVERTER GENERICO, PARAMETRIZAVEL E QUE IGNORE OS ATRIBUTOS COLECOES
 * 
 * Converter super simples, para trabalhar com IBaseVOs de pk não natural.
 * 
 * @author darcio
 *
 */
public abstract class AbstractJSONConverter implements Converter {

	protected static final String SEPARATOR = ":";

	GsonBuilder gsonBuilder = new GsonBuilder();
	
	
	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String strValue) {

		Gson gson = gsonBuilder.create();
		
		Object object = gson.fromJson(strValue, getClassType());
		
		return object;
	}
	

	/**
	 * Determina qual será a classe do objeto a ser montado
	 * @return
	 */
	protected abstract Class<? extends Object> getClassType();


	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		/*
		if(value!=null && value instanceof IOrganizationRestrictVO){
			IOrganizationRestrictVO orgRest  = (IOrganizationRestrictVO) value;
			orgRest.setEmpresaVO(null);
		}
		*/
		
		Gson gson = gsonBuilder.create();
		
		String strJson = gson.toJson(value);
		
		return strJson;
	}


}
