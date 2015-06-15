package br.com.dlp.validador.pedvalidacao;

import java.util.Date;

import br.com.dlp.framework.vo.AbstractCadastroBaseVO;
import br.com.dlp.jazzqa.base.AbstractJazzQAPK;
import br.com.dlp.validador.cliente.ClienteVO;


/**
 * @hibernate.class lazy="false"
 * @wj2ee baseentity="true"
 */

public class PedValidacaoVO extends AbstractCadastroBaseVO {

	private static final long serialVersionUID = 1L;

	private ClienteVO clienteVO;

	private Date dataDownload;

	private Date dataSolicitacao;

	private Date dataTermino;

	//private Set pessoas = new HashSet(0);

	private Integer qtdeRegistrosArq;

	private Integer qtdeRegistrosProc;

	public PedValidacaoVO() {
		super(new PedValidacaoPK());
	}

	/**
	 * @hibernate.many-to-one
	 * 
	 * @wj2ee descricao="Cliente" dica="Cliente Mastersaf logado no portal"
	 *        somenteLeitura="true" listavel="true" pesquisavel="true"
	 *        ordenavel="true" ordem="60" renderer="combo"
	 * 
	 */
	public ClienteVO getClienteVO() {
		return this.clienteVO;
	}

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true" pesquisavel="true" 
	 */
	public Date getDataDownload() {
		return this.dataDownload;
	}

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true" pesquisavel="true" requerido="true"
	 */
	public Date getDataSolicitacao() {
		return this.dataSolicitacao;
	}

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true" pesquisavel="true"
	 */
	public Date getDataTermino() {
		return this.dataTermino;
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

	// * @hibernate.collection-key column = "idRequisito"

	/*
	 * @wj2ee classe = "com.msaf.validador.pessoa.PessoaVO" ordem="130" descricao="Histórico"
	 *        dica="Registros de pessoas (PF e PJ) que serão validados"
	 *        somenteLeitura="false" renderer="indexado"
	 * 
	 * @hibernate.set cascade="all-delete-orphan" inverse = "false"
	 * @hibernate.collection-one-to-many class = "com.msaf.validador.pessoa.PessoaVO"
	public Set getPessoas() {
		return this.pessoas;
	}
	 */
	
	
	
	
	
	
	

	/*
	 * @wj2ee classe="br.com.dlp.jazzqa.requisito.HistoricoRequisitoVO"
	 *        ordem="130" descricao="Histórico" dica="Os eventos de históricos
	 *        de um requisito definem seu ciclo de vida, exemplo: criação,
	 *        alterações, aprovação, cancelamento, etc... "
	 *        somenteLeitura="false" renderer="indexado"
	 * 
	 * @hibernate.set cascade="all-delete-orphan" inverse = "false"
	 * @hibernate.collection-key column = "idRequisito"
	 * @hibernate.collection-one-to-many class =
	 *                                   "br.com.dlp.jazzqa.requisito.HistoricoRequisitoVO"
	 */
	
	
	
	
	

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true" pesquisavel="true"
	 */
	public Integer getQtdeRegistrosArq() {
		return this.qtdeRegistrosArq;
	}

	/**
	 * @hibernate.property
	 * 
	 * @wj2ee ordem="20" listavel="true" pesquisavel="true"
	 */
	public Integer getQtdeRegistrosProc() {
		return this.qtdeRegistrosProc;
	}

	public void setClienteVO(ClienteVO clienteFk) {
		this.clienteVO = clienteFk;
	}

	public void setDataDownload(Date dataDownload) {
		this.dataDownload = dataDownload;
	}

	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	public void setId(Integer id) {
		AbstractJazzQAPK abstractJazzQAPK = (AbstractJazzQAPK) getIPK();
		abstractJazzQAPK.setId(id);
	}

	/*
	public void setPessoas(Set registroPessoaCollection) {
		this.pessoas = registroPessoaCollection;
	}
	 */

	public void setQtdeRegistrosArq(Integer qtdeRegistrosArq) {
		this.qtdeRegistrosArq = qtdeRegistrosArq;
	}

	public void setQtdeRegistrosProc(Integer qtdeRegistrosProc) {
		this.qtdeRegistrosProc = qtdeRegistrosProc;
	}

}
