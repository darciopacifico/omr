package br.com.dlp.jazzav.adesivo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import br.com.dlp.jazzav.AbstractEntityVO;
import br.com.jazz.codegen.annotation.JazzProp;

/**
 * Armazena campo do adesivo com par de chave/valor
 * @author darcio
 *
 */
@Entity
public class CampoAdesivoVO extends AbstractEntityVO<Long> {

	private static final long serialVersionUID = -8215816229731360535L;
	
	private String chave;
	private String valor;
	

	public CampoAdesivoVO() {
		// TODO Auto-generated constructor stub
	}
	
	public CampoAdesivoVO(String chave){
		this.chave=chave;
	}

	public CampoAdesivoVO(String chave, String valor){
		this.chave=chave;
		this.valor=valor;
	}

	@Id
	@JazzProp(name="PK" ,tip="Chave de Modelo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}

	

	public String getChave() {
		return chave;
	}

	public String getValor() {
		return valor;
	}




	public void setValor(String valor) {
		this.valor = valor;
	}


	public void setChave(String chave) {
		this.chave = chave;
	}


	
}
