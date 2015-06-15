package br.com.dlp.jazzav.anuncio;

/**
 * Tipos de combustivel
 * @author darcio
 *
 */
public enum CombustivelEnum {

	FLEX("Flex(A/G)", "Flex"), 
	GASOLINA("Gasolina", "Gasol"), 
	ALCOOL("Etanol(\"Álcool\")","Álcool"), 
	DIESEL("Diesel","Dies"), 
	KIT_GAS("GNV"), 
	ELETRICO("Elétrico","Elét."), 
	ELETRICO_HIBRIDO("Híbrido (Elétrico+Combust)","Híbrido")
	; 
	
	String desc;
	String sigla;
	
	private CombustivelEnum(String desc, String sigla) {
		this.desc = desc;
		this.sigla = sigla;
	}
	
	private CombustivelEnum(String desc) {
		this(desc, desc);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	

	@Override
	public String toString() {
		return getSigla();
	}


}
