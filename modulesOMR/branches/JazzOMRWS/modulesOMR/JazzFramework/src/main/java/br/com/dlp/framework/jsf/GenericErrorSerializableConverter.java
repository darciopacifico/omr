/**
 * 
 */
package br.com.dlp.framework.jsf;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * O JSF n�o consegue entender corretamente generics. O m�todo getPK de IBaseVO deve retornar PK onde PK extends Serializable.
 * O VO de pessoa (PessoaVO extends AbstractBelongsOrgVO<String>) define PK como String, mas o JSF n�o consegue interpretar o retorno do m�todo getPK de PessoaVO como String e sim como Serializable. 
 * 
 * Desta forma, o JSF se perde ao tentar encontrar um converter padrao para Serializable, que n�o existe, e d� um erro de tentativa de convers�o.
 * 
 * Como solu��o, esta implementa��o de converter super simples deve ser for�ada nos trechos de componentes JSF que referenciam PessoaVO.getPK, que � chave da entidade PessoaVO
 * mas � uma String definida que deve ser definida pelo pr�prio usu�rio e, sendo assim, passível de valida��o. 
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
