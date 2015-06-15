package br.com.poc;

import java.io.Serializable;

public class PessoaVO implements Serializable {

	private Long id;
	private String nome;
	
	public PessoaVO() {
		// TODO Auto-generated constructor stub
	}
	
	public PessoaVO(Long id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}
	public Long getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	
	
}
