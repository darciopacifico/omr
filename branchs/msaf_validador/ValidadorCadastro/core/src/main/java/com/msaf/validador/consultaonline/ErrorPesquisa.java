package com.msaf.validador.consultaonline;

/**
 * C�digo com os erros de pesquisa
 * @author ederson
 *
 */
public enum ErrorPesquisa {

	DIGITOR_VERIFICADOR_INVALIDO(20001,"Digito Verificador inv�lido");
	
	private long codigo;
	
	private String descricao;
	
	private ErrorPesquisa(long id, String descricao) {
		this.codigo = id;
		this.descricao = descricao;
	}
	
	public long getCodigo(){
		return this.codigo;
	}

	public String getDescricao(){
		return this.descricao;
	}
}
