package com.msaf.validador.consultaonline.cliente;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Entity
@XmlType
public class ClienteVO extends AbstractVO implements Serializable {
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "PK_GEN")
    @SequenceGenerator(name="PK_GEN", sequenceName="SEQUENCE_CLIENTE", allocationSize=1)
//    @Column
	@Column(name="IDCLIENTE")
	private long idCliente;

	@Column
	private String cnpj;

	@Column
	private String nome;

	private static final long serialVersionUID = 1L;

	public ClienteVO() { }

	public ClienteVO(String nome, String cnpj) {
		this.nome = nome;
		this.cnpj = cnpj;
	}

	public ClienteVO(Long idCliente) {
		this.idCliente = idCliente;
	}

	public ClienteVO(Long idCliente, String nome, String cnpj) {
		this.idCliente = idCliente;
		this.nome = nome;
		this.cnpj = cnpj;
	}

	public long getIdCliente() {
		return this.idCliente;
	}

	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	@XmlElement(nillable=false, required=true)
	public String getCnpj() {
		return this.cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}


}



