package com.msaf.validador.core.tratamentoxls.xls.column;

import com.msaf.validador.core.tratamentoxls.xls.Column;

public abstract class AbstractColumn implements Column {

	public AbstractColumn(String value) {
		this.value = value;
	}
	
	private String value;

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}
	
}
