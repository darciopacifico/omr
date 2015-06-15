package com.msaf.validador.consultaonline.solicitacaovalidacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.msaf.validador.consultaonline.cliente.AbstractVO;

@Entity
@Table(name="Valor_CategoriaVO")
public class ValorCategoriaVO extends AbstractVO {
	
	private static final long serialVersionUID = -23689300969621571L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_VALOR_CATEGORIA")
	@SequenceGenerator(name = "PK_VALOR_CATEGORIA", sequenceName = "SEQUENCE_VALOR_CATEGORIA", allocationSize = 1)
	@Column(name="idValorCategoria")
	private Long id;
	
	@Column(name="nome", length=50, nullable=true)
	private String nome;
	
	@Column(name="descricao", length=100)
	private String descricao;
	
	@ManyToOne
	@JoinColumn(name="idCategoriafk")
	private CategoriaVO categoria;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public CategoriaVO getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaVO categoria) {
		this.categoria = categoria;
	}
}
