/**
 * 
 */
package br.com.dlp.jazzomr.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author darcio
 *
 */
@Scope(value="session")
@Component
public class HibernateIBaseVOConverter extends IBaseVOConverter {
	

	public HibernateIBaseVOConverter() {
	}
	
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String strValue) {
		Object asObject = super.getAsObject(context, component, strValue);
		
		//HibernateTemplate hibernateTemplate = (HibernateTemplate) FacesContextUtils.getWebApplicationContext(context).getBean("hibernateTemplate");
				
		//hibernateTemplate.refresh(asObject);
		
		return asObject;
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String asString = super.getAsString(context, component, value);
		return asString;
	}
	

}
