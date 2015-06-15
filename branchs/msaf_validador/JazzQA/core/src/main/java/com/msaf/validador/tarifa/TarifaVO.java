package com.msaf.validador.tarifa;

import java.util.Set;

import br.com.dlp.framework.vo.AbstractCadastroBaseVO;
import br.com.dlp.jazzqa.base.AbstractJazzQAPK;

/**
 * @hibernate.class lazy="false"
 * @wj2ee baseentity="true"
 */

public class TarifaVO  extends AbstractCadastroBaseVO {
	
	private String descricao;

	
	private Long valorTarifa;

	private Set tpValids;

	private static final long serialVersionUID = 1L;

	public TarifaVO() {
		super();
	}

	
	
	
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getValorTarifa() {
		return this.valorTarifa;
	}

	public void setValorTarifa(Long valorTarifa) {
		this.valorTarifa = valorTarifa;
	}

	public Set getTipoValidacaoCollection() {
		return this.tpValids;
	}

	public void setTipoValidacaoCollection(Set tipoValidacaoCollection) {
		this.tpValids = tipoValidacaoCollection;
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

}
