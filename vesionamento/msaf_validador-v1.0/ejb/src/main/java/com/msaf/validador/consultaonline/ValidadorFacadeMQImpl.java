package com.msaf.validador.consultaonline;

import java.util.List;
import java.util.Properties;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.msaf.framework.utils.DllDadosDTO;
import com.msaf.validador.consultaonline.exceptions.ValidadorConsumerException;
import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;


/**
 * Implementação do serviço de validação com pooling de mensagens JMS
 */
public class ValidadorFacadeMQImpl implements ValidadorFacade {
	
	
	public ValidadorFacadeMQImpl() {
		try {
			createQueue();
		} catch (NamingException e) {
			throw new ValidadorRuntimeException("Erro fatal ao tentar instanciar ValidadorFacade. Verifique se o Servidor de mensagens está disponível!",e);
		} catch (JMSException e) {
			throw new ValidadorRuntimeException("Erro fatal ao tentar instanciar ValidadorFacade. Verifique se o Servidor de mensagens está disponível!",e);
		}
	}
	
	QueueConnectionFactory jmsConnFactory = null;
	QueueConnection jmsConn = null;
	QueueSession jmsSession = null;
	Queue queue = null;
	Logger log = Logger.getLogger(ValidadorFacadeMQImpl.class);
	
	@Override
	public void senderDadosCliente(ParametrosConsultaOnLineDTO parameterObject) {
		if(queue==null) throw new IllegalArgumentException("Queue não iniciada! Verifique se o servidor de mensagens esta disponível!");
		QueueSender aSender = null;
		try {
			
			aSender = jmsSession.createSender(queue);
			jmsConn.start();
			
			Message message = jmsSession.createObjectMessage(parameterObject);
			message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
			aSender.send(message);
			aSender.close();

		} catch (Exception e) {
			e.printStackTrace();
		} 	finally {
			try {
				if (aSender != null) {
					aSender.close();
				}
			} catch (Exception e) {}
		}
	}
	
	@Override
	public void consultaDadosCliente(ParametrosConsultaOnLineDTO parameterObject, DllDadosDTO dllPro, List<ResulConsVO> list) {

		log.debug("Starting...");
		QueueSender aSender = null;
		try {

			Queue aQueue = createQueue();

			aSender = jmsSession.createSender(aQueue);
			jmsConn.start();
			
			aSender.send(jmsSession.createObjectMessage(parameterObject));
			aSender.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				if (aSender != null) {
					aSender.close();
				}
				
				if (jmsSession != null) {
					jmsSession.close();
				}
				if (jmsConn != null) {
					jmsConn.stop();
					jmsConn.close();
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
				 
		}
		log.debug("Ending...");

	}

	/**
	 * @return
	 * @throws NamingException
	 * @throws JMSException
	 */
	public Queue createQueue() throws NamingException, JMSException {

		if (jmsConnFactory == null) {

			Properties props = new Properties();
			props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
			InitialContext initialContext = new InitialContext(props);

			jmsConnFactory = (QueueConnectionFactory) initialContext.lookup("ConnectionFactory");
			jmsConn = jmsConnFactory.createQueueConnection();
			jmsSession = jmsConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			queue = (Queue) initialContext.lookup("validadorOnLineRequest");
		}

		return queue;
	}
	
		public void destroyQueue() throws NamingException, JMSException {

		if (jmsSession != null) {
			jmsSession.close();
			jmsSession = null;
		}
		if (jmsConn != null) {
			jmsConn.stop();
			jmsConn.close();
			jmsConn = null;
		}
	

	}
	@Override
	public void gravaResultado(ParametrosConsultaOnLineDTO consultaOnLineDTO, List<ResulConsVO> resultadoList) {
		// FIXME: NÁO FAZ NADA, DEPOIS EU REVISO O CONTRADO
	}

	@Override
	public void registraErroDeProcessamento(Exception e,
			ParametrosConsultaOnLineDTO consultaOnLineDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registraErroDeProcessamento(ValidadorConsumerException e) {
		// TODO Auto-generated method stub
		
	}
	
}
