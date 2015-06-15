package br.com.dlp.jazzav.anuncio;

/**
 * Cilindrada Enum
 * @author darcio
 */
public enum CilindradaEnum {

	_1_0,
	_1_4,
	_1_5,
	_1_6,
	_1_7,
	_1_8,
	_2_0,
	_2_2,
	_2_4,
	_2_5,
	_2_8,
	_3_0,
	_3_2,
	_4_0,
	_4_1,
	_4_3,
	_5_0;
	
	private String toString; 
	
	/**
	 * Exibe enum de cilindrada no formato padrão
	 */
	public String toString() {
		if(toString==null){
			toString = super.toString();
			toString = toString.substring(1).replace("_", ".");
		}
		
		return toString;
		
	};
	
}
