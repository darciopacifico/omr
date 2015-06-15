package com.msaf.validador.consultaonline;


/**
 * Enum que representa os Tipos de Documentos
 * @author ederson
 *
 */
public enum TipoDocumento {

	SINTEGRA_CNPJ("0")
	,SINTEGRA_IE("1")
	
	,RECEITA_CNPJ("0")
	,RECEITA_CPF("1");
	
	private String codigo;

	private TipoDocumento(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return this.codigo;
	}

}
