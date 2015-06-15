package com.msaf.validador.consultaonline;

import java.net.ConnectException;
import java.util.List;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.bind.JAXBException;

import com.msaf.framework.utils.DllDadosDTO;
import com.msaf.validador.consultaonline.exceptions.BaseValidadorException;
import com.msaf.validador.consultaonline.exceptions.ValidadorConsumerException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;

/**
 * Contrato para servi�os do Validador. N�o confundir contrato para invoca��o da
 * DLL do Validador ({@link IConsultaOnLineJNA})
 * 
 */
public interface ValidadorFacade {

	/**
	 * Consulta Validador e retorna dados do cliente
	 * @param parameterObject TODO
	 * @param resultadosConsultas 
	 * 
	 * @return Abstra��o de resultado de consulta. Cont�m cole��o com os
	 *         registros retornados.
	 * @throws ValidadorConsumerException 
	 * @throws ConnectException 
	 * @throws JAXBException 
	 */
	@WebMethod
	public void consultaDadosCliente(
			@WebParam(name="parametroConsulta")
			ParametrosConsultaOnLineDTO parameterObject, 
			DllDadosDTO configuracaoDLL, List<ResulConsVO> resultadosConsultas) throws ValidadorConsumerException, ConnectException;
	
	@WebMethod
	public void senderDadosCliente(
			@WebParam(name="parametroConsulta")
			ParametrosConsultaOnLineDTO parameterObject) throws BaseValidadorException;
	
	
	/**
	 * Contrato para implementar a gravacao dos dados
	 * 
	 * @param consultaOnLineDTO
	 * @param resultado
	 */
	public void gravaResultado(ParametrosConsultaOnLineDTO consultaOnLineDTO, List<ResulConsVO> resultadoList);
	
	
	/**
	 * Registrar este erro em meio persistente. Infelizmente n�o � poss�vel
	 * identificar de qual arquivo/cliente veio a mensagem JMS com erro
	 * 
	 * @param e
	 * @param consultaOnLineDTO 
	 */
	public void registraErroDeProcessamento(Exception e, ParametrosConsultaOnLineDTO consultaOnLineDTO);


	/**
	 * registra erro em log ...
	 * @param e
	 */
	public void registraErroDeProcessamento(ValidadorConsumerException e);
	
	/**
	 * Libera recursos
	 */
	public void close();
	
	/**
	 * Consulta validador e retorna dados do cliente
	 * @param pessoaVO
	 * @param tpServico
	 * @param tpConsulta
	 * @return
	 * @throws ValidadorConsumerException
	 * @throws ConnectException 
	 */
	@WebMethod
	public Set<DadosRetInstVO> consultaCliente(
			@WebParam(name="documento")
			String documento, 
			@WebParam(name="tipoConsulta")
			Integer tpConsulta,
			@WebParam(name="tipoServico")
			Integer tpServico,
			@WebParam(name="uf")
			String uf)
			throws ValidadorConsumerException, ConnectException;

	public void senderDadosPersistence(Object[] parameterObject);

	
	
}
