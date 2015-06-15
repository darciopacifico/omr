package com.msaf.validador.consultaonline;

import java.util.List;
import java.util.Properties;
import java.util.Set;

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
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;


/**
 * Implementação do serviço de validação com pooling de mensagens JMS
 */
public class ValidadorFacadeMQImpl implements ValidadorFacade {
	
	public ValidadorFacadeMQImpl() {
		try {
			createQueue("jndi/validadorOnlineRequest");
		} catch (NamingException e) {
			throw new ValidadorRuntimeException("Erro fatal ao tentar instanciar ValidadorFacade. Verifique se o Servidor de mensagens está disponível!",e);
		} catch (JMSException e) {
			throw new ValidadorRuntimeException("Erro fatal ao tentar instanciar ValidadorFacade. Verifique se o Servidor de mensagens está disponível!",e);
		}
	}
	
	public ValidadorFacadeMQImpl(String jndiName) {
		try {
			createQueue(jndiName);
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
			
			aSender.send(message);
			aSender.close();

		} catch (Exception e) {//
			e.printStackTrace();
			throw new RuntimeException(e);
		} 	finally {
			try {
				if (aSender != null) {
					aSender.close();
				}
			} catch (Exception e) {}
		}
	}
	
	@Override
	public void senderDadosPersistence(Object[] parameterObject) {
		if(queue==null) throw new IllegalArgumentException("Queue não iniciada! Verifique se o servidor de mensagens esta disponível!");
		QueueSender aSender = null;
		try {
			
			aSender = jmsSession.createSender(queue);
			jmsConn.start();
			
			Message message = jmsSession.createObjectMessage(parameterObject);
			
			aSender.send(message);
			aSender.close();
			
			if(log.isDebugEnabled()) log.debug("Mensagem enviada para a fila " + queue.getQueueName());

		} catch (Exception e) {//
			e.printStackTrace();
			throw new RuntimeException(e);
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

		if(log.isDebugEnabled()) log.debug("Starting...");
		QueueSender aSender = null;
		try {

			Queue aQueue = createQueue("jndi/validadorOnlineRequest");

			aSender = jmsSession.createSender(aQueue);
			jmsConn.start();
			
			aSender.send(jmsSession.createObjectMessage(parameterObject));
			aSender.close();

		} catch (Exception e) {//
			e.printStackTrace();
			throw new RuntimeException(e);
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
			} catch (JMSException e) {//
				e.printStackTrace();
			}
				 
		}
		if(log.isDebugEnabled()) log.debug("Ending...");

	}

	/**
	 * @return
	 * @throws NamingException
	 * @throws JMSException
	 */
	public Queue createQueue(String jndiName) throws NamingException, JMSException {

		if (jmsConnFactory == null) {

			Properties props = new Properties();
		    props.put(Context.PROVIDER_URL,"iiop://127.0.0.1:3700");
		    props.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.enterprise.naming.SerialInitContextFactory");
			InitialContext initialContext = new InitialContext(props);

			jmsConnFactory = (QueueConnectionFactory) initialContext.lookup("jndi/validadorOnlineConnFactory");
			jmsConn = jmsConnFactory.createQueueConnection();
			jmsSession = jmsConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			queue = (Queue) initialContext.lookup(jndiName);
		}

		return queue;
	}
	
		public void destroyQueue() throws NamingException, JMSException {

		if (jmsSession != null) {
			jmsSession.close();
			jmsSession = null;
		}
		if (jmsConn != null) {
			//jmsConn.stop();
			jmsConn.close();
			jmsConn = null;
		}
	

	}
	@Override
	public void gravaResultado(ParametrosConsultaOnLineDTO consultaOnLineDTO, List<ResulConsVO> resultadoList) {
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

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<DadosRetInstVO> consultaCliente(String documento, Integer tpConsulta,
			Integer tpServico, String uf) throws ValidadorConsumerException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
