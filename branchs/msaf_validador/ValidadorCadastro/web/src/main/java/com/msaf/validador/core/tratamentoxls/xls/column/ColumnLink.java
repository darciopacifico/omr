package com.msaf.validador.core.tratamentoxls.xls.column;

public class ColumnLink extends AbstractColumn {

	private String textoLink;

	public ColumnLink(String value) {
		super(value);
	}

	public ColumnLink setTextoLink(String textoLink) {
		this.textoLink = textoLink;
		return this;
	}

	public String getTextoLink() {
		return this.textoLink;
	}

}
