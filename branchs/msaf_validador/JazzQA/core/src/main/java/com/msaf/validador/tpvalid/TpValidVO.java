package com.msaf.validador.tpvalid;

import java.util.Date;
import java.util.Set;

import br.com.dlp.framework.vo.AbstractCadastroBaseVO;
import br.com.dlp.jazzqa.base.AbstractJazzQAPK;

import com.msaf.validador.instituicao.InstituicaoVO;


/**
 * @hibernate.class lazy="false"
 * @wj2ee baseentity="true"
 */

public class TpValidVO extends AbstractCadastroBaseVO {
	private static final long serialVersionUID = 546763425656431L;

	
	private TpValidPK tpValidPK;

	private String descricao;

	private Date validade;

	private InstituicaoVO instituicaoVO;

	private Set dadosRetInsts;

	public TpValidVO() {
		super();
	}

	/**
	 * @wj2ee ordem="10" somenteLeitura="true" descricao="Código"
	 *        pesquisavel="true" listavel="true" pk="true"
	 * 
	 * @hibernate.id generator-class="increment"
	 */
	public Integer getId() {
		AbstractJazzQAPK abstractJazzQAPK = (AbstractJazzQAPK) getIPK();
		return abstractJazzQAPK.getId();
	}




	public void setId(Integer id) {
		AbstractJazzQAPK abstractJazzQAPK = (AbstractJazzQAPK) getIPK();
		abstractJazzQAPK.setId(id);
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
		return this.instituicaoVO;
	}

	public void setInstituicaoFk(InstituicaoVO instituicaoFk) {
		this.instituicaoVO = instituicaoFk;
	}

	public Set getDadosRetornoInstituicaoCollection() {
		return this.dadosRetInsts;
	}

	public void setDadosRetornoInstituicaoCollection(Set dadosRetornoInstituicaoCollection) {
		this.dadosRetInsts = dadosRetornoInstituicaoCollection;
	}



	public TpValidPK getTpValidPK() {
		return tpValidPK;
	}



	public void setTpValidPK(TpValidPK tpValidPK) {
		this.tpValidPK = tpValidPK;
	}

}
