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
import org.hibernate.annotations.Index;

@Entity
@Table(name="endereco")
public class LogradouroVO implements Serializable {

	private static final long serialVersionUID = 1734466427773347141L;

	private Integer PK;
	private BairroVO bairroVO;
	private String cep; /* ` varchar(9) DEFAULT NULL, */
	private String logradouro; /* ` varchar(72) DEFAULT NULL, */
	private String complemento; /* ` varchar(72) DEFAULT NULL, */
	
	/**
	 * `endereco_codigo` int(11) NOT NULL DEFAULT '0', PK
	 * 
	 * @return
	 */
	@Id
	@Column(name="endereco_codigo")
	public Integer getPK() {
		return PK;
	}

	/**
	 * `bairro_codigo` int(11) DEFAULT NULL, bairroVO
	 * 
	 * @return
	 */
	@ManyToOne(fetch=FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name="bairro_codigo")
	public BairroVO getBairroVO() {
		return bairroVO;
	}

	/**
	 * `endereco_cep` varchar(9) DEFAULT NULL, cep
	 * 
	 * @return
	 */
	@Column(name="endereco_cep")
	@Index(name="index_cep")
	public String getCep() {
		return cep;
	}

	/**
	 * `endereco_logradouro` varchar(72) DEFAULT NULL, logradouro
	 * 
	 * @return
	 */
	@Column(name="endereco_logradouro")
	public String getLogradouro() {
		return logradouro;
	}

	
	
	/**
	 * `` varchar(72) DEFAULT NULL, complemento
	 * 
	 * @return
	 */
	@Column(name="endereco_complemento")
	public String getComplemento() {
		return complemento;
	}

	public void setPK(Integer endereco_codigo) {
		this.PK = endereco_codigo;
	}

	/*
	 */
	public void setBairroVO(BairroVO bairro_codigo) {
		this.bairroVO = bairro_codigo;
	}
	
	public void setCep(String endereco_cep) {
		this.cep = endereco_cep;
	}

	public void setLogradouro(String endereco_logradouro) {
		this.logradouro = endereco_logradouro;
	}

	public void setComplemento(String endereco_complemento) {
		this.complemento = endereco_complemento;
	}

}
