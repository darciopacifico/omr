/**
 * 
 */
package br.com.dlp.jazzomr.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang.math.NumberUtils;

import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * Converter super simples, para trabalhar com IBaseVOs de pk não natural.
 * 
 * @author darcio
 */
public class IBaseVOConverter implements Converter {

	protected static final String SEPARATOR = ":";

	
	public Object getAsObject(FacesContext context, UIComponent component, String strValue) {

		String[] strings = strValue.split(IBaseVOConverter.SEPARATOR);
		
		String className = strings[0];
		String strPK = strings[1];

		
		if(!NumberUtils.isDigits(strPK)){
			throw new JazzRuntimeException("O valor da chave não é numérico ("+strPK+")!");
		}
			
			
		long parseLong = Long.parseLong(strPK);
		
		Class clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new JazzRuntimeException("A classe '"+className+"' não foi encontrada!");
		}
		
		IBaseVO<Serializable> iBaseVO;
		try {
			iBaseVO = (IBaseVO<Serializable>) clazz.newInstance();
		} catch (InstantiationException e) {
			throw new JazzRuntimeException("Erro ao tentar instanciar a classe '"+className+"'!");
		} catch (IllegalAccessException e) {
			throw new JazzRuntimeException("Erro ao tentar instanciar a classe '"+className+"'!");
		}
		
		iBaseVO.setPK(parseLong);
		
		return iBaseVO;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
				
		IBaseVO<Serializable> iBaseVO = (IBaseVO<Serializable>) value;
		
		String className = iBaseVO.getClass().getName();
		
		String pk = iBaseVO.getPK()+""; 
		
		String strValue = className+IBaseVOConverter.SEPARATOR+pk;
		
		return strValue;
	}

}
