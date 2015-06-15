package com.msaf.validador.consultaonline;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;

/**
 * Abstrai parâmetros da consulta on line.
 * 
 * 
 * 
 * aqui dentro tem que vir todas as informa;óes necessarias para a persistëncia do retorno da consulta.
 * 
 */ 
@XmlType
public class ParametrosConsultaOnLineDTO implements Serializable{
	private static final long serialVersionUID = -3381890909743691208L;
	
	/**
	 * obs: pedido de validacao já estará persistido quando o consumidor JMS for acionado
	 * 
	 * 
	 * - RegistroPessoaVO
	 * - código do pedido de validacao
	 * - tipo de validacao
	 * 
	 */
		
	public ParametrosConsultaOnLineDTO() {
		super();
	}
	
	private List<TpValidVO>  tipoValidacaoVO;
	private PedValidacaoVO pedidoValidacaoVO;
	private PessoaVO registroPessoaVO;
	

	public List<TpValidVO> getTipoValidacaoVO() {
		return tipoValidacaoVO;
	}

	public void setTipoValidacaoVO(List<TpValidVO> tipoValidacaoVO) {
		this.tipoValidacaoVO = tipoValidacaoVO;
	}

	public PedValidacaoVO getPedidoValidacaoVO() {
		return pedidoValidacaoVO;
	}

	public void setPedidoValidacaoVO(PedValidacaoVO pedidoValidacaoVO) {
		this.pedidoValidacaoVO = pedidoValidacaoVO;
	}

	public PessoaVO getRegistroPessoaVO() {
		return registroPessoaVO;
	}

	public void setRegistroPessoaVO(PessoaVO registroPessoaVO) {
		this.registroPessoaVO = registroPessoaVO;
	}


}