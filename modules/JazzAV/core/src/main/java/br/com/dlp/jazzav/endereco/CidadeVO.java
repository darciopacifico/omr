package br.com.dlp.jazzav.endereco;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="cidade")
public class CidadeVO  implements Serializable{

	private static final long serialVersionUID = -14289894568268793L;
	
	private Integer PK;
	private EstadoVO estadoVO;
	private String descricao;
	private String cep;

	
	@Id
	@Column(name="cidade_codigo")
	public Integer getPK() {
		return PK;
	}

	//`` int(11) DEFAULT NULL,
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="uf_codigo")
	@Fetch(FetchMode.JOIN)
	public EstadoVO getEstadoVO() {
		return estadoVO;
	}

	@Column(name="cidade_descricao")
	public String getDescricao() {
		return descricao;
	}

	@Column(name="cidade_cep")
	public String getCep() {
		return cep;
	}


	
	
	public void setPK(Integer pK) {
		PK = pK;
	}

	public void setEstadoVO(EstadoVO estadoVO) {
		this.estadoVO = estadoVO;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}
	
	
	
	
	
}
