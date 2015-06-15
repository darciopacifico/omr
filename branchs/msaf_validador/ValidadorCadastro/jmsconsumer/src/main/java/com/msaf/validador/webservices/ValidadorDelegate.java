package com.msaf.validador.webservices;

import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.msaf.validador.consultaonline.exceptions.ValidadorConsumerException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;

@WebService
public interface ValidadorDelegate {

	@WebMethod
	public Set<DadosRetInstVO> consultaCliente(String documento, Integer tpConsulta, Integer tpServico, String uf)
			throws ValidadorConsumerException;
	
	
}
