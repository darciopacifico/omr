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
public class PessoaJSONConverter extends AbstractJSONConverter {

	public PessoaJSONConverter() {
	}

	@Override
	protected Class<? extends Object> getClassType() {
		return PessoaVO.class;
	}


	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if(value instanceof PessoaVO){
			PessoaVO pessoaVO = (PessoaVO) value;
			pessoaVO.setAuthorities(null);
			pessoaVO.setEmpresaVO(null);
		}

		return super.getAsString(context, component, value);
	}

}
