package com.msaf.validador.ws;

import java.io.Serializable;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;

@WebService
public class ValidadorFacade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6828083310974104087L;


	@WebMethod
	public @WebResult(name="protocolo") Long processaValidacaoBatch(@WebParam(name="pedidoValidacao") PedValidacaoVO pedValidacaoVO ){
		Long  protocolo=1l;
		return protocolo;
	}
	

	@WebMethod
	public @WebResult(name="pedidoValidacaoRetorno") PedValidacaoVO recuperaValidacaoBatch(@WebParam(name="protocolo") Long protocol ){
		PedValidacaoVO pedValidacaoVO = new PedValidacaoVO();
		return pedValidacaoVO;
	}
	

	@WebMethod
	public @WebResult(name="pedidoValidacaoRetorno") PedValidacaoVO processaValidacao(@WebParam(name="pedidoValidacao") PedValidacaoVO pedValidacaoVO ){
		PedValidacaoVO pedValidacaoRetVO = new PedValidacaoVO();
		return pedValidacaoRetVO;
	}
	
	
}
