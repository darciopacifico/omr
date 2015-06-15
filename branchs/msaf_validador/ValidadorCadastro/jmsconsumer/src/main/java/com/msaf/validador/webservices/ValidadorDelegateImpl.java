/**
 * 
 */
package com.msaf.validador.webservices;

import java.net.ConnectException;
import java.util.Set;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.msaf.validador.consultaonline.ValidadorFacade;
import com.msaf.validador.consultaonline.exceptions.ValidadorConsumerException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;

/**
 * @author abborges
 *
 */
@WebService
@Stateless
public class ValidadorDelegateImpl implements ValidadorDelegate {
	
	private final static ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/com/msaf/validador/consumer/JMSConsumerConfiguration.xml");
	
	@WebMethod
	public Set<DadosRetInstVO> consultaCliente(String documento, Integer tpConsulta,	Integer tpServico, String uf) throws ValidadorConsumerException {
		ValidadorFacade facade = (ValidadorFacade) applicationContext.getBean("validadorFacade");
		try {
			return facade.consultaCliente(documento, tpConsulta, tpServico, uf);
		} catch (ConnectException e) {
			throw new ValidadorConsumerException(e);
		}
	}

}
