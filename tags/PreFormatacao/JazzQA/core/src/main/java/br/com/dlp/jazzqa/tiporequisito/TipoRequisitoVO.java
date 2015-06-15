package br.com.dlp.jazzqa.tiporequisito;

import javax.persistence.Entity;

import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.annotation.JazzProp.ComparisonOperator;

@Entity
@JazzClass(name = "Tipo de Requisito")
public class TipoRequisitoVO extends AbstractBaseVO<Long> {

	private static final long serialVersionUID = 8541646026789968351L;

	
	public TipoRequisitoVO() {
		// TODO Auto-generated constructor stub
	}
	
	public TipoRequisitoVO(Long pk) {
		setPK(pk);
	}
	
	private String descricao;
	private String nome;

	@JazzProp(name = "Nome", searchable = true)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@JazzProp(name = "Descrição", searchable = true, comparison = ComparisonOperator.LIKE)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
