/**
 * 
 */
package br.com.dlp.framework.jsf;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * O JSF não consegue entender corretamente generics. O método getPK de IBaseVO deve retornar PK onde PK extends Serializable.
 * O VO de pessoa (PessoaVO extends AbstractBelongsOrgVO<String>) define PK como String, mas o JSF não consegue interpretar o retorno do método getPK de PessoaVO como String e sim como Serializable. 
 * 
 * Desta forma, o JSF se perde ao tentar encontrar um converter padrao para Serializable, que não existe, e dá um erro de tentativa de conversão.
 * 
 * Como solução, esta implementação de converter super simples deve ser forçada nos trechos de componentes JSF que referenciam PessoaVO.getPK, que é chave da entidade PessoaVO
 * mas é uma String definida que deve ser definida pelo próprio usuário e, sendo assim, passÃ­vel de validação. 
 * 
 * @author darcio
 *
 */
public class GenericErrorSerializableConverter implements Converter {
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if(value==null){
			return null;

		}else{
			return value.toString();
			
		}
	}
}
