package br.com.dlp.jazzqa.tiporequisito;

import java.util.Date;

import javax.persistence.Entity;

import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;

@Entity
@JazzClass(name="VO Dummy" , description="Apenas um VO Dummy para testar renderizacao de relacionamentos")
public class DummyVO extends AbstractBaseVO<Long> {
	
	private static final long serialVersionUID = 2184081255503074341L;
	
	private String descricao;
	private String nome;
	private Date dtInclusao;
	
	public DummyVO() {
	}
	
	@JazzProp(name = "Descrição", searchable = true)
	public String getDescricao() {
		return descricao;
	}
	
	@JazzProp(name = "Nome", searchable = true)
	public String getNome() {
		return nome;
	}
	
	@JazzProp(name = "Inclusão", searchable = true)
	public Date getDtInclusao() {
		return dtInclusao;
	}
	
	
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setDtInclusao(Date dtInclusao) {
		this.dtInclusao = dtInclusao;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getPK()+". "+getNome();
	}
}
