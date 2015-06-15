package br.com.dlp.jazzav.anuncio;

/**
 * Tipos de cambio
 * @author darcio
 *
 */
public enum CambioEnum {

	MANUAL("Manual", "Man"), AUTOMATICA("Automático", "Aut."), CVT("CVT");
	
	String desc;
	String sigla;
	
	private CambioEnum(String desc, String sigla) {
		this.desc = desc;
		this.sigla = sigla;
	}
	
	private CambioEnum(String desc) {
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
