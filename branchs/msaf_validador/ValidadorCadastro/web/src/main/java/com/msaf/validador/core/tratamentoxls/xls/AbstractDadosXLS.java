package com.msaf.validador.core.tratamentoxls.xls;

import java.util.ResourceBundle;

import com.msaf.validador.consultaonline.TipoValidacao;
import com.msaf.validador.core.tratamentoxls.xls.column.ColumnString;

public abstract class AbstractDadosXLS implements DadosXLS {

	private static ResourceBundle bundle = ResourceBundle.getBundle("com.msaf.validador.xls");
	
	private TipoValidacao tipoValidacao;
	
	public AbstractDadosXLS(TipoValidacao tipoValidacao){
		this.tipoValidacao = tipoValidacao;
	}
	
	protected String getLabel(String key) {
		return bundle.getString(key);
	}
	
	protected Column writeValue(String value){
		value = value == null? "": value;
		
		return new ColumnString(value);
	}

	@Override
	public TipoValidacao getTipoValidacao(){
		return this.tipoValidacao;
	}
}
