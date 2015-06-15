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
public class InstituicaoVO implements Serializable {
	@Id
	@Column
	private long idInstituicao;

	@Column
	private BigDecimal codMastersaf;


	@OneToMany
	private Set<TpValidVO> tipoValidacaoFk;

	private static final long serialVersionUID = 1L;

	public InstituicaoVO() {
		super();
	}

	public long getIdInstituicao() {
		return this.idInstituicao;
	}

	public void setIdInstituicao(long idInstituicao) {
		this.idInstituicao = idInstituicao;
	}

	public BigDecimal getCodMastersaf() {
		return this.codMastersaf;
	}

	public void setCodMastersaf(BigDecimal codMastersaf) {
		this.codMastersaf = codMastersaf;
	}

	public Set<TpValidVO> getTipoValidacaoFk() {
		return this.tipoValidacaoFk;
	}

	public void setTipoValidacaoFk(Set<TpValidVO> tipoValidacaoCollection) {
		this.tipoValidacaoFk = tipoValidacaoCollection;
	}

}
