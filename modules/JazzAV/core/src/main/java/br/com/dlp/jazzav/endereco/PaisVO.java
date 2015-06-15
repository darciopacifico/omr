package br.com.dlp.jazzav.endereco;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author darcio
 */
@Entity
@Table(name="paises")
public class PaisVO  implements Serializable{

	private static final long serialVersionUID = 3464642650603013209L;

	private String iso;
	private String iso3;
	private Integer numCode;
	private String nome;

	@Id
	@Column(name="iso")
	public String getIso() {
		return iso;
	}
	
	@Column(name="iso3")
	public String getIso3() {
		return iso3;
	}
	
	@Column(name="numcode")
	public Integer getNumCode() {
		return numCode;
	}
	
	@Column(name="nome")
	public String getNome() {
		return nome;
	}
	
	public void setIso(String iso) {
		this.iso = iso;
	}
	public void setIso3(String iso3) {
		this.iso3 = iso3;
	}
	public void setNumCode(Integer numCode) {
		this.numCode = numCode;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
