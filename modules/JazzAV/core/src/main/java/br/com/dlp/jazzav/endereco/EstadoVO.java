package br.com.dlp.jazzav.endereco;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="uf")
public class EstadoVO  implements Serializable{

	private static final long serialVersionUID = 7629578330665452765L;

	private Integer PK;
	private String sigla;
	private String descricao;


	@Id
	@Column(name="uf_codigo")
	public Integer getPK() {
		return PK;
	}

	@Column(name="uf_sigla")
	public String getSigla() {
		return sigla;
	}

	@Column(name="uf_descricao")
	public String getDescricao() {
		return descricao;
	}


	
	
	public void setPK(Integer pK) {
		PK = pK;
	}


	public void setSigla(String sigla) {
		this.sigla = sigla;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	
}
