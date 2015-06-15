package br.com.dlp.framework.servicelocator;

import javax.jms.QueueSession;

import org.hibernate.Session;

public class RelatorioServiceLocator extends AbstractJ2EEServiceLocatorImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8264244757027885399L;

	public RelatorioServiceLocator() throws ServiceLocatorException {
		super();

	}

	public void closeHibernateSession(Session session)
			throws ServiceLocatorException {
		throw new ServiceLocatorException(
				"Esta implementação de IServiceLocator "
						+ "serve apenas para o componente de Relatorios, não"
						+ "sendo possível invocar este servico de fechar sessao do hibernate");
	}

	public synchronized Session getHibernateSession()
			throws ServiceLocatorException {
		throw new ServiceLocatorException(
				"Esta implementação de IServiceLocator "
						+ "serve apenas para o componente de Relatorios, não"
						+ "sendo possível invocar este servico de criar sessao do hibernate");
	}

	public QueueSession getQueueSessionPool() throws ServiceLocatorException {
		throw new ServiceLocatorException(
				"Esta implementação de IServiceLocator "
						+ "serve apenas para o componente de Relatorios, não"
						+ "sendo possível invocar este servico de criar sessao de filas");
	}

	public QueueSession getQueueSessionBatch() throws ServiceLocatorException {
		throw new ServiceLocatorException(
				"Esta implementação de IServiceLocator "
						+ "serve apenas para o componente de Relatorios, não"
						+ "sendo possível invocar este servico de criar sessao de filas");
	}

}
