package br.com.dlp.jazzqa.tiporequisito;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlType;

import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.annotation.JazzTextArea;
import br.com.jazz.codegen.annotation.JazzProp.ComparisonOperator;
import br.com.jazz.codegen.annotation.JazzProp.Renderer;

@Entity
@JazzClass(name = "Tipo de Requisito")
@XmlType
public class TipoRequisitoVO extends AbstractBaseVO<Long> {
	
	private static final long serialVersionUID = 8541646026789968351L;
	
	private String descricao;
	private String nome;
	private Date dtInclusao;
	
	@JazzProp(name="Data de Inclusão", renderer=Renderer.CALENDAR, comparison=ComparisonOperator.RANGE)
	public Date getDtInclusao() {
		return dtInclusao;
	}
	
	
	public void setDtInclusao(Date dtInclusao) {
		this.dtInclusao = dtInclusao;
	}
	
	public TipoRequisitoVO() {
		// TODO Auto-generated constructor stub
	}
	
	public TipoRequisitoVO(final Long pk) {
		setPK(pk);
	}
	
	@JazzProp(name = "Descrição", size="23", searchable = true, comparison = ComparisonOperator.LIKE, renderer=Renderer.TEXTAREA)
	@JazzTextArea(rows=3, cols=40)
	@Column(length=200)
	public String getDescricao() {
		return descricao;
	}
	
	@JazzProp(name = "Nome", searchable = true)
	@Column(length=50)
	public String getNome() {
		return nome;
	}
	
	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}
	
	public void setNome(final String nome) {
		this.nome = nome;
	}
	
}
