package br.com.dlp.jazzqa.produto;

import java.util.Date;

import javax.persistence.Entity;

import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.annotation.JazzProp.ComparisonOperator;
import br.com.jazz.codegen.annotation.JazzProp.Renderer;

@Entity
@JazzClass(name = "Produto")
public class ProdutoVO extends AbstractBaseVO<Long> {
	private static final long serialVersionUID = -6336488651161953590L;
	
	private String dsAreaProduto;
	
	private Date dtInclusao;
	
	private String nome;
	
	private String obs;
	
	@JazzProp(name = "Descrição")
	public String getDsAreaProduto() {
		return this.dsAreaProduto;
	}
	
	@JazzProp(name = "Data Inclusão", comparison = ComparisonOperator.RANGE, renderer=Renderer.CALENDAR)
	public Date getDtInclusao() {
		return this.dtInclusao;
	}
	
	@JazzProp(name = "Nome", comparison = ComparisonOperator.LIKE)
	public String getNome() {
		return this.nome;
	}
	
	@JazzProp(name = "Observação")
	public String getObs() {
		return this.obs;
	}
	
	public void setDsAreaProduto(final String dsAreaProduto) {
		this.dsAreaProduto = dsAreaProduto;
	}
	
	public void setDtInclusao(final Date dtInclusao) {
		this.dtInclusao = dtInclusao;
	}
	
	public void setNome(final String nome) {
		this.nome = nome;
	}
	
	public void setObs(final String obs) {
		this.obs = obs;
	}
}
