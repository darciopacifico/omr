
package com.msaf.validador.consumer;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.msaf.framework.utils.DllDadosDTO;
import com.msaf.validador.consultaonline.ParametrosConsultaOnLineDTO;
import com.msaf.validador.consultaonline.ValidadorFacade;
import com.msaf.validador.consultaonline.exceptions.ValidadorConsumerException;
import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;

/**
 * Implementação do recebedor de mensagens. Esta classe deve conter apenas a
 * orquestração de chamada dos componentes de negócio para efetuar a consulta
 * pela DLL e chamar componente que armazena resultado.
 * 
 * @author dlopes
 * 
 */
public class ValidadorJMSListener implements IValidadorJMSListener, MessageDrivenBean {
	
	private static final long serialVersionUID = -8013094024828074641L;

	private static ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/com/msaf/validador/consumer/JMSConsumerConfiguration.xml");

	private ValidadorFacade validadorFacade;
	
	private final static Logger log = Logger.getLogger("Validador");

	private DllDadosDTO configuracaoDll;
	
	@Override
	public void onMessage(Message message) {
		try {
			long timeInicial = System.currentTimeMillis();
			if(log.isDebugEnabled()) log.debug("recebendo mensagem");
	
			//recupera parâmetros para consulta: Simplesmente recupera  o ParametrosConsultaOnLineDTO de dentro da mensagem
			ParametrosConsultaOnLineDTO consultaOnLineDTO;
			try {
				consultaOnLineDTO = recuperaParametroConsulta(message);
			} catch (ValidadorConsumerException e) {
				validadorFacade.registraErroDeProcessamento(e);
				if(log.isDebugEnabled()) log.debug("Erro ao tentar receber mensagem JMS!");
				throw new ValidadorRuntimeException("Erro ao tentar receber mensagem JMS!",e);
			}
	
			if(log.isDebugEnabled()) log.debug("Efetuando validações...");
			processaConsultaValidador(consultaOnLineDTO, this.configuracaoDll);
			if(log.isDebugEnabled()) log.debug("Validações contidas na mensagem foram efetuadas.");
	
			if(log.isDebugEnabled()) log.debug("mensagem recebida");
			if(log.isDebugEnabled()) log.debug("Tempo de execucao total da mensagem " + Thread.currentThread().hashCode() + ": " + ((System.currentTimeMillis() - timeInicial) / 1000) + " segundos.");
		} catch (Exception e){
			if(log.isDebugEnabled()) log.debug(e);
			this.context.setRollbackOnly();
			throw new EJBException(e);
		}
	}

	/**
	 * @param consultaOnLineDTO
	 * @throws ValidadorConsumerException 
	 * @throws ConnectException 
	 */
	private void processaConsultaValidador(ParametrosConsultaOnLineDTO consultaOnLineDTO, DllDadosDTO configuracaoDLL) throws ConnectException, ValidadorConsumerException {
		//Executa consulta dos dados do cliente: fazer funcionar

		List<ResulConsVO> resultadosConsultas = new ArrayList<ResulConsVO>();
		
		if(log.isDebugEnabled()) log.debug("Efetuando consulta...");
		validadorFacade.consultaDadosCliente(consultaOnLineDTO, configuracaoDLL, resultadosConsultas);
		if(log.isDebugEnabled()) log.debug("Consulta efetuada com sucesso!");
		
		//GRAVA CONSULTA
		if(log.isDebugEnabled()) log.debug("Enviando mensagem para persistencia do dados...");
		validadorFacade.gravaResultado(consultaOnLineDTO, resultadosConsultas);
		if(log.isDebugEnabled()) log.debug("Mensagem para persistencia do dados efetuada com sucesso!");
		
		if(log.isDebugEnabled()) log.debug("Recebida mensagem:");
	}

	/**
	 * @param message
	 * @return
	 * @throws ValidadorConsumerException
	 *             Registrar no sistema erro ao tentar consumir mensagem. Não
	 *             deixar subir esta exceção
	 */
	private ParametrosConsultaOnLineDTO recuperaParametroConsulta(Message message) throws ValidadorConsumerException {
		
		ObjectMessage objectMessage = (ObjectMessage) message;
		ParametrosConsultaOnLineDTO consultaOnLineDTO;
		
		try {
			consultaOnLineDTO = (ParametrosConsultaOnLineDTO) objectMessage.getObject();
		} catch (JMSException e) {
			if(log.isDebugEnabled()) log.debug("Erro ao tentar recuperar o objeto de parametros de consulta!");
			throw new ValidadorConsumerException("Erro ao tentar recuperar o objeto de parametros de consulta!",e);
		}
		return consultaOnLineDTO;
	}

	public ValidadorFacade getValidadorFacade() {
		return validadorFacade;
	}

	public void setValidadorFacade(ValidadorFacade validadorFacade) {
		this.validadorFacade = validadorFacade;
	}

	public DllDadosDTO getConfiguracaoDll() {
		return configuracaoDll;
	}

	public void setConfiguracaoDll(DllDadosDTO configuracaoDll) {
		this.configuracaoDll = configuracaoDll;
	}

	@Override
	public void ejbRemove() throws EJBException {
		if(log.isDebugEnabled()) log.debug("Removendo server RMI (removendo instancia de MDB Listener) ...");
		this.validadorFacade.close();
	}
	
	public void ejbCreate() throws EJBException {
		if(this.validadorFacade == null){
			if(log.isDebugEnabled()) log.debug("Criando novo server RMI (nova instancia de MDB Listener) ...");
			this.validadorFacade = (ValidadorFacade) applicationContext.getBean("validadorFacade");
			this.configuracaoDll = new DllDadosDTO();
			if(log.isDebugEnabled()) log.debug("Novo server RMI (nova instancia de MDB Listener) criado com sucesso!");
		}
	}

	@Override
	public void setMessageDrivenContext(MessageDrivenContext arg0)
		throws EJBException {
		this.context = arg0;
	}
	
	private MessageDrivenContext context;

}
