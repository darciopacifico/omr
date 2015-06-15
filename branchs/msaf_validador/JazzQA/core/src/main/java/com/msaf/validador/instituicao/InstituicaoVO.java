package com.msaf.validador.instituicao;

import br.com.dlp.framework.vo.AbstractCadastroBaseVO;
import br.com.dlp.jazzqa.base.AbstractJazzQAPK;


/**
 * @hibernate.class lazy="false"
 * @wj2ee baseentity="true"
 */

public class InstituicaoVO  extends AbstractCadastroBaseVO {

	
	private Long codMastersaf;

	
	private Long valorUnitario;

	private static final long serialVersionUID = 1L;

	public InstituicaoVO() {
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

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public Long getCodMastersaf() {
		return this.codMastersaf;
	}

	public void setCodMastersaf(Long codMastersaf) {
		this.codMastersaf = codMastersaf;
	}

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public Long getValorUnitario() {
		return this.valorUnitario;
	}

	public void setValorUnitario(Long valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

}
