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
@Table(name="bairro")
public class BairroVO implements Serializable{

	private static final long serialVersionUID = -5339330090136092308L;

	private Integer PK;
  private CidadeVO cidadeVO;
  private String descricao;

  
	/*
  `bairro_codigo` int(11) NOT NULL DEFAULT '0',
  */
  @Column(name="bairro_codigo")
  @Id
  public Integer getPK() {
		return PK;
	}

	/*
  `cidade_codigo` int(11) DEFAULT NULL,
  */
  @ManyToOne(fetch=FetchType.EAGER)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name="cidade_codigo")
	public CidadeVO getCidadeVO() {
		return cidadeVO;
	}

	/*
  `bairro_descricao` varchar(72) DEFAULT NULL,
  */
  @Column(name="bairro_descricao")
	public String getDescricao() {
		return descricao;
	}

	
	
	
	public void setPK(Integer pK) {
		PK = pK;
	}

	public void setCidadeVO(CidadeVO cidadeVO) {
		this.cidadeVO = cidadeVO;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

  
  
	
}
