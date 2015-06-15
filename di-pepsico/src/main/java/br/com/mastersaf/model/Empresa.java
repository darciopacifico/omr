package br.com.mastersaf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.mastersaf.util.Bean;
import br.com.mastersaf.util.Util;

@Entity
@Table(name = "CTRL_EMPRESA")
public class Empresa implements Bean {
	
	private static final long serialVersionUID = 4783974349809551429L;

	@Id
	@Column(name="ID_EMPRESA", updatable = false, insertable = false)
	private Long idEmpresa; 

	@Column(name="NM_FANTASIA", updatable = false, insertable = false)
	private String nome;

//	@SuppressWarnings("unused")
//	@Transient
//	private String descricao;
	
	
	
	public Empresa() {}
	
	public Empresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

//	public String getDescricao() {
//		if(Util.isEmpty(nome)){
//			return idEmpresa.toString();   			
//		} 
//		return descricao = idEmpresa +" - "+nome;
//	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	

}
