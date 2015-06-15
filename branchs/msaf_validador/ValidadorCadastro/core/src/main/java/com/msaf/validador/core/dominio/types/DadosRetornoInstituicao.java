package com.msaf.validador.core.dominio.types;

public enum DadosRetornoInstituicao {
	 Id(0)
	,RazaoSocial(1)
	,Logradouro(2)
	,Numero(3)
	,Complemento(4)
	,Bairro(5)
	,CEP(6)
	,Município(7)
	,Estado(8)
	,CNPJ(9)
	,IE(10);
	
	
	DadosRetornoInstituicao(int row){
		 this.value = row;
	 }
	 private final int value;
	 
	 public int getValue() {
		return value;
	}
	 
	 
}
