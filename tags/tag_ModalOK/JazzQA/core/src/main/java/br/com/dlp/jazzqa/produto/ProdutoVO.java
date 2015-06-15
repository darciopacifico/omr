package br.com.dlp.jazzqa.produto;

import java.util.Date;

import javax.persistence.Entity;

import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.annotation.JazzProp.ComparisonOperator;

@Entity
@JazzClass(name = "Produto")
public class ProdutoVO extends AbstractBaseVO<Long> {
	private static final long serialVersionUID = -6336488651161953590L;

	private String dsAreaProduto;

	private String obs;

	private String nome;

	private Date dtInclusao;
	
	@JazzProp(name="Data Inclusão",comparison=ComparisonOperator.RANGE)
	public Date getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(Date dtInclusao) {
		this.dtInclusao = dtInclusao;
	}

	@JazzProp(name = "Observação")
	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	@JazzProp(name = "Nome",comparison=ComparisonOperator.LIKE)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@JazzProp(name = "Descrição")
	public String getDsAreaProduto() {
		return dsAreaProduto;
	}

	public void setDsAreaProduto(String dsAreaProduto) {
		this.dsAreaProduto = dsAreaProduto;
	}
}
