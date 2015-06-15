package br.com.dlp.validador.cliente;

import br.com.dlp.framework.vo.AbstractCadastroBaseVO;
import br.com.dlp.jazzqa.base.AbstractJazzQAPK;

/**
 * @hibernate.class lazy="false"
 * @wj2ee baseentity="true"
 */


public class ClienteVO extends AbstractCadastroBaseVO{
	private static final long serialVersionUID = 1L;
	
	private String cnpj;
	
	private String nome;

	public ClienteVO() {
		super(new ClientePK());
	}
	
	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getCnpj() {
		return this.cnpj;
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
	
	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true"
	 *        pesquisavel="true"
	 */
	public String getNome() {
		return this.nome;
	}

	
	
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	public void setId(Integer id) {
		AbstractJazzQAPK abstractJazzQAPK = (AbstractJazzQAPK) getIPK();
		abstractJazzQAPK.setId(id);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}



