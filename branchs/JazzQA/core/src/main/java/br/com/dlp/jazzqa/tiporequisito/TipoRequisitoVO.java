package br.com.dlp.jazzqa.tiporequisito;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.validator.constraints.NotEmpty;

import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.annotation.JazzTextArea;
import br.com.jazz.codegen.enums.ComparisonOperator;
import br.com.jazz.codegen.enums.JazzRenderer;

@Entity
@JazzClass(name = "Tipo de Requisito")
@XmlType
public class TipoRequisitoVO extends AbstractBaseVO<Long> {
	private static final long serialVersionUID = 8541646026789968351L;
	private String descricao;
	private String nome;
	private Date dtInclusao;
	private DummyVO dummyVO;
	
	/**
	 * Construtor default
	 */
	public TipoRequisitoVO() {
		// Construtor padrao. Deliberadamente em branco.
	}
	public TipoRequisitoVO(final Long pk) {
		setPK(pk);
	}
	
	@JazzProp(name = "Data de Inclusão", renderer = JazzRenderer.CALENDAR, comparison = ComparisonOperator.RANGE, sortable=true)
	@NotNull
	public Date getDtInclusao() {
		return dtInclusao;
	}
	
	@JazzProp(name = "Descrição", size = "23", searchable = true, comparison = ComparisonOperator.LIKE, renderer = JazzRenderer.TEXTAREA)
	@JazzTextArea(rows = 3, cols = 40)
	@Column(length = 200)
	@NotEmpty
	public String getDescricao() {
		return descricao;
	}
	
	@JazzProp(name = "Nome", searchable = true, sortable=true)
	@Column(length = 50)
	@NotEmpty
	public String getNome() {
		return nome;
	}
	
	@JazzProp(name = "Dummy" , renderer=JazzRenderer.POPUP)
	@ManyToOne(fetch=FetchType.EAGER)
	@NotNull
	public DummyVO getDummyVO() {
		return dummyVO;
	}
	
	
	
	
	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}
	
	public void setDtInclusao(Date dtInclusao) {
		this.dtInclusao = dtInclusao;
	}
	
	public void setNome(final String nome) {
		this.nome = nome;
	}
	public void setDummyVO(DummyVO dummyVO) {
		this.dummyVO = dummyVO;
	}
	
}
