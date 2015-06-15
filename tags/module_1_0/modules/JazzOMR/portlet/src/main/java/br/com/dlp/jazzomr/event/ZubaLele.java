package br.com.dlp.jazzomr.event;

import javax.faces.context.FacesContext;

import org.primefaces.model.StreamedContent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Scope(value = "request")
@Component
public class ZubaLele {
	@Scope(value = "request")
	public StreamedContent streamExamImg() {
		
		String strPagina = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pagina");
		String strPartPK = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("partPK");
		
		
		if (strPagina!=null || strPartPK!=null) {
			
			System.out.println("OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK OK ");
		}
		
		return null;
	}
	
	
}
