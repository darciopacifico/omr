package br.com.dlp.jazzqa.status;

import javax.persistence.Entity;

import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.annotation.JazzTextArea;
import br.com.jazz.codegen.enums.JazzRenderer;

@Entity
@JazzClass(name = "Status")
public class StatusVO extends AbstractBaseVO<Integer> {
	private static final long serialVersionUID = 6010367305784305940L;
	
	private String titulo;
	private String descricao;
	
	@JazzProp(name = "Nome", renderer = JazzRenderer.TEXT)
	public String getTitulo() {
		return titulo;
	}
	
	@JazzProp(name = "Descrição", renderer = JazzRenderer.TEXTAREA)
	@JazzTextArea(cols = 20, rows = 3)
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setTitulo(final String nome) {
		titulo = nome;
	}
	
}
