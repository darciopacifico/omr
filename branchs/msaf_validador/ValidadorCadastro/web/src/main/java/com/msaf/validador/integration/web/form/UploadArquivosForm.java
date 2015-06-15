package com.msaf.validador.integration.web.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;
import com.msaf.validador.core.dominio.UsuarioVO;
import com.msaf.validador.integration.hibernate.jdbc.StatusPediValidacao;
import com.msaf.validador.integration.web.form.base.BaseForm;

public class UploadArquivosForm extends BaseForm {

	private static final long serialVersionUID = 4294242013432948993L;
	
	//atributos de uma solicitação de validacao
	private FormFile arquivo;
	private String[] tipoSelecionado = new String[]{};
	
	private Float tempoMedioProcessamento;
	
	//para escolha dos tipos de validacao
	private List<TpValidVO> tiposValidacaoEscolhaOrcamento;

	//para escolha dos tipos de validacao
	private List<TpValidVO> tiposValidacao;
	
	private List<PessoaVO> listPessoas;

	private Integer page;
	
	private Long quantidadeRegistros;
	

	private String dataInicioSolicitacao;
	
	private String dataFimSolicitacao;
	
	
	public List<PessoaVO> getListPessoas() {
		return listPessoas;
	}

	public void setListPessoas(List<PessoaVO> listPessoas) {
		this.listPessoas = listPessoas;
	}

	private int qtdLinhasArquivoXLS;
	
	//pedidos de validacao do mesmo cliente
	private List<PedValidacaoVO> listPedidosValidacao;
	
	public int getQtdLinhasArquivoXLS() {
		return qtdLinhasArquivoXLS;
	}

	public void setQtdLinhasArquivoXLS(int qtdLinhasArquivoXLS) {
		this.qtdLinhasArquivoXLS = qtdLinhasArquivoXLS;
	}

	//status dos pedidos de validacao de um mesmo cliente
	private List<StatusPediValidacao> listStatusPediValidacao;

	private Integer quantidadeDeRegistros;
	private Double valorTotal;
	private PedValidacaoVO  pedValidacaoVO;
	private Double subtotalTarifas;
	
	private UsuarioVO usuarioVO;
	
	private Boolean habiliarBtnProcesso=false;
	
	
	public Float getPercentualCompleto() {
		Float percentual = 0f;

		
		
		//quantidade de registros do arquivo
		Integer qtdRegistrosArquivo = pedValidacaoVO.getQtdeRegistrosArq();
		if (qtdRegistrosArquivo == null || qtdRegistrosArquivo==0) {
			return 0f;
		}

		
		
		//quantidade de pessoas no pedido de validacao
		Integer qtdPessoas = 0;
		if(listPessoas!=null){
			qtdPessoas = listPessoas.size();
		}
		
		
		percentual = qtdPessoas.floatValue() / qtdRegistrosArquivo.floatValue();

		percentual = percentual * 100;

		if (percentual > 100) {
			percentual = 100f;
		}

		return percentual;
	}
		
	public PedValidacaoVO getPedValidacaoVO() {
		return pedValidacaoVO;
	}

	public void setPedValidacaoVO(PedValidacaoVO idPedido) {
		this.pedValidacaoVO = idPedido;
	}

	
	
	
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		return super.validate(mapping, request);
	}
	
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.tipoSelecionado = new String[]{}; 
		super.reset(mapping, request);
	}
		public List<StatusPediValidacao> getListStatusPediValidacao() {
		return listStatusPediValidacao;
	}

	public void setListStatusPediValidacao(
			List<StatusPediValidacao> listStatusPediValidacao) {
		this.listStatusPediValidacao = listStatusPediValidacao;
	}
	
	public Double getSubtotalTarifas() {
		return subtotalTarifas;
	}

	public void setSubtotalTarifas(Double subtotalTarifas) {
		this.subtotalTarifas = subtotalTarifas;
	}

	public String[] getTipoSelecionado() {
		return tipoSelecionado;
	}

	public void setTipoSelecionado(String[] teste) {
		this.tipoSelecionado = teste;
	}

	
	
	public List<TpValidVO> getTiposValidacao() {
		return tiposValidacao;
	}

	public void setTiposValidacao(List<TpValidVO> tiposValidacao) {
		this.tiposValidacao = tiposValidacao;
	}

	public List<PedValidacaoVO> getListPedidosValidacao() {
		return listPedidosValidacao;
	}

	public void setListPedidosValidacao(List<PedValidacaoVO> listPedidosValidacao) {
		this.listPedidosValidacao = listPedidosValidacao;
	}

	public Integer getQuantidadeDeRegistros() {
		return quantidadeDeRegistros;
	}

	public void setQuantidadeDeRegistros(Integer quantidadeDeRegistros) {
		this.quantidadeDeRegistros = quantidadeDeRegistros;
	}



	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public FormFile getArquivo() {
		return arquivo;
	}

	public void setArquivo(FormFile arquivo) {
		this.arquivo = arquivo;
	}
	
	public void clean() {
		this.arquivo=null;
		this.quantidadeDeRegistros=null;
		this.valorTotal=null;
		this.listPedidosValidacao=new ArrayList<PedValidacaoVO>();
	}

	/**
	 * @return the usuarioVO
	 */
	public UsuarioVO getUsuarioVO() {
		return usuarioVO;
	}

	/**
	 * @param usuarioVO the usuarioVO to set
	 */
	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

	/**
	 * @return the habiliarBtnProcesso
	 */
	public Boolean getHabiliarBtnProcesso() {
		return habiliarBtnProcesso;
	}

	/**
	 * @param habiliarBtnProcesso the habiliarBtnProcesso to set
	 */
	public void setHabiliarBtnProcesso(Boolean habiliarBtnProcesso) {
		this.habiliarBtnProcesso = habiliarBtnProcesso;
	}

	/**
	 * @return the tiposValidacaoEscolhaOrcamento
	 */
	public List<TpValidVO> getTiposValidacaoEscolhaOrcamento() {
		return tiposValidacaoEscolhaOrcamento;
	}

	/**
	 * @param tiposValidacaoEscolhaOrcamento the tiposValidacaoEscolhaOrcamento to set
	 */
	public void setTiposValidacaoEscolhaOrcamento(List<TpValidVO> tiposValidacaoEscolhaOrcamento) {
		this.tiposValidacaoEscolhaOrcamento = tiposValidacaoEscolhaOrcamento;
	}

	/**
	 * @return the tempoMedioProcessamento
	 */
	public Float getTempoMedioProcessamento() {
		return tempoMedioProcessamento;
	}

	/**
	 * @param tempoMedioProcessamento the tempoMedioProcessamento to set
	 */
	public void setTempoMedioProcessamento(Float tempoMedioProcessamento) {
		this.tempoMedioProcessamento = tempoMedioProcessamento;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Long getQuantidadeRegistros() {
		return quantidadeRegistros;
	}

	public void setQuantidadeRegistros(Long quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}

	public String getDataInicioSolicitacao() {
		return dataInicioSolicitacao;
	}

	public void setDataInicioSolicitacao(String dataInicioSolicitacao) {
		this.dataInicioSolicitacao = dataInicioSolicitacao;
	}

	public String getDataFimSolicitacao() {
		return dataFimSolicitacao;
	}

	public void setDataFimSolicitacao(String dataFimSolicitacao) {
		this.dataFimSolicitacao = dataFimSolicitacao;
	}

	

}