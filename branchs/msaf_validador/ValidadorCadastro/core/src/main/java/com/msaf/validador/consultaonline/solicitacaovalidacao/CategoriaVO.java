package com.msaf.validador.consultaonline.solicitacaovalidacao;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.msaf.validador.consultaonline.cliente.AbstractVO;

@Entity
@Table(name="CategoriaVO")
public class CategoriaVO extends AbstractVO {

	private static final long serialVersionUID = 8602531554404000842L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PK_CATEGORIA")
	@SequenceGenerator(name = "PK_CATEGORIA", sequenceName = "SEQUENCE_CATEGORIA", allocationSize = 1)
	@Column(name="idCategoria")
	private Long id;

	@Column(name="nome",length=50, nullable=true, unique=true)
	private String nome;

	@Column(name="descricao", length=100)
	private String descricao;

	@OneToMany(mappedBy="categoria", fetch=FetchType.EAGER)
	private Set<ValorCategoriaVO> valores;

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

	public Set<ValorCategoriaVO> getValores() {
		return valores;
	}

	public void setValores(Set<ValorCategoriaVO> valores) {
		this.valores = valores;
	}


}
