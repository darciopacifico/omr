package com.msaf.validador.integration.hibernate.bean;

import java.util.Date;

import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;

public class PesquisaPedidoValidacao {

	
	private PedValidacaoVO pedidoValidacao;
	
	private Date dataInicioSolicitacao;
	
	private Date dataFimSolicitacao;

	public PedValidacaoVO getPedidoValidacao() {
		return pedidoValidacao;
	}

	public void setPedidoValidacao(PedValidacaoVO pedidoValidacao) {
		this.pedidoValidacao = pedidoValidacao;
	}

	public Date getDataInicioSolicitacao() {
		return dataInicioSolicitacao;
	}

	public void setDataInicioSolicitacao(Date dataInicioSolicitacao) {
		this.dataInicioSolicitacao = dataInicioSolicitacao;
	}

	public Date getDataFimSolicitacao() {
		return dataFimSolicitacao;
	}

	public void setDataFimSolicitacao(Date dataFimSolicitacao) {
		this.dataFimSolicitacao = dataFimSolicitacao;
	}
	
	
}
