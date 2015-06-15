package br.com.tsuite.tiporelacionamento;

import java.io.Serializable;

public class ModuloVO implements Serializable {

	private ProjetoVO projetoVO;
	
	public ProjetoVO getProjetoVO() {
		return projetoVO;
	}
	public void setProjetoVO(ProjetoVO projetoVO) {
		this.projetoVO = projetoVO;
	}
	private Long id;
	private String descricao;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	
	
}
