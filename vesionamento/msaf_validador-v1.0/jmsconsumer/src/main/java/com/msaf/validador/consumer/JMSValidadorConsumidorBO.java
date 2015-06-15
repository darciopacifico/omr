package com.msaf.validador.consumer;

import java.util.List;
import java.util.Properties;

import javax.annotation.PreDestroy;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.msaf.framework.utils.DllDadosDTO;
import com.msaf.validador.consultaonline.exceptions.BaseValidadorException;
import com.msaf.validador.consultaonline.exceptions.ValidadorException;

/**
 * Componente de negócio consumidor das Mensagens JMS para validação de cadastro
 * Recebe mensagen JMS, consulta DLL do Validador e armazena resultados.
 * 
 */
public class JMSValidadorConsumidorBO {
	private Logger log = Logger.getLogger(JMSValidadorConsumidorBO.class);

	private IValidadorJMSListener messageListener;
	private String jmsContextFactory;
	private String jmsConectionFactory;
	private String jmsRequestQueue;
	private List<String> jmsProviderURLList;
	private List<String> dllConfList;
	
	private QueueConnectionFactory connectionFactory = null;
	private QueueConnection connection = null;
	private QueueSession session = null;
	private QueueReceiver queueReceiver = null;
	private Queue aQueue = null;

	private boolean inicializado=false;
	
	/**
	 * Retorna boleano dizendo se está inicializado ou não
	 * @return
	 */
	public boolean isInicializado(){
		return inicializado;
	}

	public List<String> getJmsProviderURLList() {
		return jmsProviderURLList;
	}

	public void setJmsProviderURLList(List<String> jmsProviderURLList) {
		this.jmsProviderURLList = jmsProviderURLList;
	}

	/**
	 * Inicializa configurações do recebedor de mensagens JMS
	 * @throws ValidadorException 
	 * 
	 * @throws NamingException
	 * @throws JMSException
	 */
	public void inicializaConsumidorJMS(String proxy, DllDadosDTO configuracaoDll) throws BaseValidadorException {
		try {
			if (!isInicializado()) {
				// seta a configuracao da dll.
				messageListener.setConfiguracaoDll(configuracaoDll);

				// recupera initialContext conectado ao ActiveMQ
				InitialContext initialContext = getInitialContext(proxy);
				connectionFactory = (QueueConnectionFactory) initialContext.lookup(getJmsConectionFactory());
				connection = connectionFactory.createQueueConnection();
				session = createQueueSession(connection);
				aQueue = (Queue) initialContext.lookup(getJmsRequestQueue());
				queueReceiver = session.createReceiver(aQueue);
				queueReceiver.setMessageListener(messageListener);
				connection.start();
			}
			inicializado = true;
		} catch (Exception e) {

			throw new ValidadorException("Erro ao tentar inicializar consumidor JMS!", e);
		}
	}

	/**
	 * @return
	 * @throws NamingException
	 */
	protected InitialContext getInitialContext(String proxy) throws NamingException {
		Properties props = getProperties(proxy);
		InitialContext aIC = new InitialContext(props);
		return aIC;
	}

	/**
	 * @return
	 */
	protected Properties getProperties(String proxy) {
		Properties props = new Properties();
		props.setProperty(Context.INITIAL_CONTEXT_FACTORY, getJmsContextFactory());
		props.setProperty(Context.PROVIDER_URL, proxy);
		return props;
	}

	/**
	 * Finaliza sessão e conexão JMS
	 * 
	 * @throws ValidadorException
	 */
	@PreDestroy
	public void finalizaConsumidorJMS() throws BaseValidadorException {

		if(isInicializado()){
			
			try {
				if (queueReceiver != null) {
					queueReceiver.close();
				}
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.stop();
					connection.close();
				}
				inicializado=false;
			} catch (JMSException e) {
				throw new ValidadorException("Erro ao finalizar consumidor JMS", e);
			}

		}
	}


	protected QueueSession createQueueSession(QueueConnection aQC) throws JMSException {

		return aQC.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	public static void main(String[] args) throws BaseValidadorException {
		JMSValidadorConsumidorBO jmsConsumidor = new JMSValidadorConsumidorBO();
		jmsConsumidor.finalizaConsumidorJMS();
	}

	
	
	
	public void setJmsRequestQueue(String jmsRequestQueue) {
		this.jmsRequestQueue = jmsRequestQueue;
	}

	public String getJmsRequestQueue() {
		return jmsRequestQueue;
	}

	public void setJmsContextFactory(String jmsContextFactory) {
		this.jmsContextFactory = jmsContextFactory;
	}

	public String getJmsContextFactory() {
		return jmsContextFactory;
	}


	public void setJmsConectionFactory(String jmsConectionFactory) {
		this.jmsConectionFactory = jmsConectionFactory;
	}

	public String getJmsConectionFactory() {
		return jmsConectionFactory;
	}

	public List<String> getDllConfList() {
		return dllConfList;
	}
	
	public IValidadorJMSListener getMessageListener() {
		return messageListener;
	}

	public void setMessageListener(IValidadorJMSListener messageListener) {
		this.messageListener = messageListener;
	}

	public void setDllConfList(List<String> dllConfList) {
		this.dllConfList = dllConfList;
	}

}
