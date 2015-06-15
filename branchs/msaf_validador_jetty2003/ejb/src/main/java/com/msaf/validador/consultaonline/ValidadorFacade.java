package com.msaf.validador.consultaonline;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.JAXBException;

import com.msaf.framework.utils.DllDadosDTO;
import com.msaf.validador.consultaonline.exceptions.BaseValidadorException;
import com.msaf.validador.consultaonline.exceptions.ValidadorConsumerException;
import com.msaf.validador.consultaonline.jna.IConsultaOnLineJNA;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;

/**
 * Contrato para serviços do Validador. Não confundir contrato para invocação da
 * DLL do Validador ({@link IConsultaOnLineJNA})
 * 
 */
@WebService
public interface ValidadorFacade {

	/**
	 * Consulta Validador e retorna dados do cliente
	 * @param parameterObject TODO
	 * @param resultadosConsultas 
	 * 
	 * @return Abstração de resultado de consulta. Contém coleção com os
	 *         registros retornados.
	 * @throws ValidadorConsumerException 
	 * @throws JAXBException 
	 */
	@WebMethod
	public void consultaDadosCliente(
			@WebParam(name="parametroConsulta")
			ParametrosConsultaOnLineDTO parameterObject, 
			DllDadosDTO configuracaoDLL, List<ResulConsVO> resultadosConsultas) throws ValidadorConsumerException;
	
	@WebMethod(exclude=true)
	public void senderDadosCliente(
			@WebParam(name="parametroConsulta")
			ParametrosConsultaOnLineDTO parameterObject) throws BaseValidadorException;
	
	
	/**
	 * Contrato para implementar a gravacao dos dados
	 * 
	 * @param consultaOnLineDTO
	 * @param resultado
	 */
		@WebMethod(exclude=true)
	public void gravaResultado(ParametrosConsultaOnLineDTO consultaOnLineDTO, List<ResulConsVO> resultadoList);
	
	
	
	
}
