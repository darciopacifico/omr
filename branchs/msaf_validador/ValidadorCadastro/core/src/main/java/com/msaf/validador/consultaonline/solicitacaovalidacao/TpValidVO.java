package com.msaf.validador.consultaonline.solicitacaovalidacao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.msaf.validador.consultaonline.cliente.InstituicaoVO;
import com.msaf.validador.consultaonline.cliente.TarifaVO;

@Entity
public class TpValidVO implements Serializable {
	@Id
	@Column
	private Long id;

	@Column(name="DESCRICAO")
	private String descricao;

	@Temporal(TemporalType.DATE)
	@Column(name="VALIDADE")
	private Date validade;

	@ManyToOne
	private InstituicaoVO instituicaoFk;

	@ManyToOne
	private TarifaVO tarifaFk;

	private static final long serialVersionUID = 1L;

	
	
	public TpValidVO() {
		super();
	}

	public TpValidVO(Long l) {
		this.id = l;
	}

	public TpValidVO(String descricao) {
		this.descricao= descricao;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getValidade() {
		return this.validade;
	}

	public void setValidade(Date validade) {
		this.validade = validade;
	}

	public InstituicaoVO getInstituicaoFk() {
		return this.instituicaoFk;
	}

	public void setInstituicaoFk(InstituicaoVO instituicaoFk) {
		this.instituicaoFk = instituicaoFk;
	}

	public TarifaVO getTarifaFk() {
		return this.tarifaFk;
	}

	public void setTarifaFk(TarifaVO tarifaFk) {
		this.tarifaFk = tarifaFk;
	}

	@Override
	public String toString() {
		return "id:"+id+" descricao:"+descricao;
	}

	@Override
	public int hashCode() {
		return (id == null) ? 0 : id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {

		if ((obj == null) || (!(obj instanceof TpValidVO))){
			return false;
		}

		if (this == obj) {
			return true;
		}

		final TpValidVO vo = (TpValidVO) obj;

		return id != null ? id.equals(vo.getId()): id == vo.getId();
		
		
	}
	
}
