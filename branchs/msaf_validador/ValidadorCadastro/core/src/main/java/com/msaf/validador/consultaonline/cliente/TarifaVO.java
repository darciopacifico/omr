package com.msaf.validador.consultaonline.cliente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;

@Entity
public class TarifaVO implements Serializable {
	@Id
	@Column
	private long idTarifa;

	@Column
	private String descricao;


    @Column(name="VALORTARIFA", precision=12, scale=2)
	private BigDecimal valorTarifa;

	@OneToMany
	private Set<TpValidVO> tipoValidacaoFk;

	private static final long serialVersionUID = 1L;

	public TarifaVO() {
		super();
	}

	public long getIdTarifa() {
		return this.idTarifa;
	}

	public void setIdTarifa(long idTarifa) {
		this.idTarifa = idTarifa;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValorTarifa() {
		return this.valorTarifa;
	}

	public void setValorTarifa(BigDecimal valorTarifa) {
		this.valorTarifa = valorTarifa;
	}

	public Set<TpValidVO> getTipoValidacaoFk() {
		return this.tipoValidacaoFk;
	}

	public void setTipoValidacaoFk(Set<TpValidVO> tipoValidacaoCollection) {
		this.tipoValidacaoFk = tipoValidacaoCollection;
	}

}
