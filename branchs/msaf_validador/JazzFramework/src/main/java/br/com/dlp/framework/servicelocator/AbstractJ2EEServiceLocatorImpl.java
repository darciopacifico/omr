package br.com.dlp.framework.servicelocator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBObject;
import javax.ejb.RemoveException;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import br.com.dlp.framework.dao.DAOFactoryException;
import br.com.dlp.framework.dao.IDAOFactory;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.facade.IFacade;
import br.com.dlp.framework.util.FrameworkAcheUtil;
import br.com.dlp.framework.util.FrameworkAcheUtilError;
import br.com.dlp.framework.util.properties.PropertiesFactory;
import br.com.dlp.framework.util.properties.PropertiesFactoryException;

/**
 * Implementacao abstrata de IServiceLocator para sistemas com arquitetura J2EE
 */
public abstract class AbstractJ2EEServiceLocatorImpl implements IServiceLocator {
	public static final String PROPERTIE_PREFIX_JNDI = "jndi_";

	public static final String PROPERTIE_PREFIX_STATEFUL = "stateful_";

	public static final String PROPERTIE_PREFIX_EJBHOME = "ejbhome_";

	public static final String PROPERTIE_PREFIX_FACADE_IMPL = "facadeimpl_";

	public static final String PROPERTIE_PREFIX_SISTEMA_EXTERNO = "sistemaext_";

	public static final String PROPERTIE_EJB_CONTEXT_FACTORY = "contextFactory";

	public static final String PROPERTIE_EJB_PROVIDER_URL = "providerURL";

	public static final String PROPERTIE_JNDI_SESSION_FACTORY = "jndiSessionFactory";

	public static final String PROPERTIE_MDBJNDI_CONNECTION_FACTORY = "mdbJndiConnectionFactory";

	public static final String PROPERTIE_MDBJNDI_POOL = "mdbJndiPool";

	public static final String PROPERTIE_MDBJNDI_BATCH = "mdbJndiBatch";

	public static final String PROPERTIE_HIBERNATE_CFG_APP_SERVER = "hibernateCfgAppServer";

	private IDAOFactory DAOFactory;

	private SessionFactory sessionFactory;

	private Map serviceCache = new Hashtable(10);

	private InitialContext remoteInitialContext = null;

	private InitialContext localInitialContext;

	protected Log logger = LogFactory
			.getLog(AbstractJ2EEServiceLocatorImpl.class);

	private Properties serviceLocatorProperties = new Properties();

	private ResourceBundle resourceBundle;

	/* Esta variavel flega se o Configuration do Hibernate foi invocado */
	private boolean hibernateConfigurado = false;

	/**
	 * Avalia se, ao tentar recuperar as facades do projeto, estas estarão
	 * remotas ou não. O padrão é que as facades são remotas
	 */
	private boolean remoteFacades;

	/**
	 * Construtor privado para que a classe seja um Singleton
	 */
	protected AbstractJ2EEServiceLocatorImpl() throws ServiceLocatorException {
		this(true);
	}

	/**
	 * Construtor privado para que a classe seja um Singleton
	 */
	protected AbstractJ2EEServiceLocatorImpl(boolean remoteFacades)
			throws ServiceLocatorException {
		serviceLocatorProperties = createPropertie();
		this.remoteFacades = remoteFacades;
	}

	protected Properties createPropertie() throws ServiceLocatorException {
		Properties properties;

		try {
			PropertiesFactory propertiesFactory = PropertiesFactory
					.getInstance();
			properties = propertiesFactory.getProperties(this.getClass());

		} catch (PropertiesFactoryException e) {
			throw new ServiceLocatorException(
					"Não foi possível carregar as propriedades do ServiceLocator",
					e);
		}

		return properties;
	}

	/**
	 * Retorna uma sessão com uma fila assincrona
	 * 
	 */
	private synchronized QueueSession getQueueSession(String jndiName)
			throws ServiceLocatorException {

		QueueSession session = (QueueSession) serviceCache.get(jndiName);
		if (session == null) {
			try {

				String connectionFactoryJNDI = getServiceLocatorProperties()
						.getProperty(PROPERTIE_MDBJNDI_CONNECTION_FACTORY);

				QueueConnectionFactory factory = (QueueConnectionFactory) getRemoteInitialContext()
						.lookup(connectionFactoryJNDI);
				Queue queue = (Queue) getRemoteInitialContext()
						.lookup(jndiName); // "queue/PoolEJBMDB"
				QueueConnection queueConn = factory.createQueueConnection();
				session = queueConn.createQueueSession(false,
						javax.jms.Session.AUTO_ACKNOWLEDGE);
				serviceCache.put(jndiName, session);
				serviceCache.put(jndiName + "Queue", queue);

			} catch (NamingException e) {
				logger
						.error("O objeto InitialContext não esta respondendo corretamente! Vou recriar o objeto!");
				logger.warn("RECRIANDO O InitialContext....");
				logger.warn("... InitialContext RECRIADO COM SUCESSO!!");
				throw new ServiceLocatorException("Erro no lookup de: "
						+ jndiName, e);
			} catch (JMSException e) {
				logger.error("Não foi possível criar sessão de " + jndiName
						+ "!");
				throw new ServiceLocatorException("Erro criando sessão de: "
						+ jndiName, e);
			}
		}

		return session;
	}

	public QueueSession getQueueSessionPool() throws ServiceLocatorException {
		String jndiName = getServiceLocatorProperties().getProperty(
				PROPERTIE_MDBJNDI_POOL);
		return getQueueSession(jndiName);
	}

	public QueueSession getQueueSessionBatch() throws ServiceLocatorException {
		String jndiName = getServiceLocatorProperties().getProperty(
				PROPERTIE_MDBJNDI_BATCH);
		return getQueueSession(jndiName);
	}

	public Queue getQueuePool() throws ServiceLocatorException {
		String jndiName = getServiceLocatorProperties().getProperty(
				PROPERTIE_MDBJNDI_POOL);
		return (Queue) serviceCache.get(jndiName + "Queue");
	}

	public Queue getQueueBatch() throws ServiceLocatorException {
		String jndiName = getServiceLocatorProperties().getProperty(
				PROPERTIE_MDBJNDI_BATCH);
		return (Queue) serviceCache.get(jndiName + "Queue");
	}

	/**
	 * Retorna uma interface remota de um EJB home
	 * 
	 * @param homeClass
	 *            Implementacao de homeClass do EJB
	 * @param jndiName
	 *            JNDIName do EJB
	 * @throws ServiceLocatorException
	 * @return Implementacao de EJBHome do EJB
	 */
	private synchronized EJBHome getHome(String jndiName, Class homeClass)
			throws ServiceLocatorException {
		EJBHome home = (EJBHome) serviceCache.get(jndiName);

		if (home == null) {
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("ServiceLocator: fazendo lookup do EJB "
							+ jndiName);
				}
				Object objref = getRemoteInitialContext().lookup(jndiName);
				if (logger.isDebugEnabled()) {
					logger.debug("ServiceLocator: Narrowing " + jndiName
							+ " to " + homeClass.getName());
				} 
				//home = (EJBHome) PortableRemoteObject.narrow(objref, homeClass);
				
				home = (EJBHome) (objref);
				serviceCache.put(jndiName, home);

			} catch (NamingException e) {
				logger
						.error("O objeto InitialContext não esta respondendo corretamente! Vou recriar o objeto!");
				logger.warn("RECRIANDO O InitialContext....");
				logger.warn("... InitialContext RECRIADO COM SUCESSO!!");
				throw new ServiceLocatorException("Erro no lookup de: "
						+ jndiName, e);

			} catch (ClassCastException e) {
				logger
						.error("O objeto InitialContext não esta respondendo corretamente! Vou recriar o objeto!");
				logger.warn("RECRIANDO O InitialContext....");
				logger.warn("... InitialContext RECRIADO COM SUCESSO!!");

				throw new ServiceLocatorException("ClassCastException em "
						+ homeClass.getName(), e);
			}
		}

		return home;
	}

	/**
	 * Retorna uma interface local de um EJB home (EJBLocalHome)
	 * 
	 * @return Implementacao de EJBLocalHome referente ao jndiName
	 * @param jndiName
	 *            jndiName do EJB
	 * @throws ServiceLocatorException
	 */
	public synchronized EJBLocalHome getLocalHome(String jndiName)
			throws ServiceLocatorException {
		EJBLocalHome home = (EJBLocalHome) serviceCache.get(jndiName);

		if (home == null) {
			try {

				home = (EJBLocalHome) getLocalInitialContext().lookup(jndiName);
				serviceCache.put(jndiName, home);

			} catch (NamingException e) {
				throw new ServiceLocatorException(
						"Erro ao tentar recuperar a interface home local do EJB "
								+ jndiName, e);

			} catch (Exception e) {
				throw new ServiceLocatorException(
						"Erro ao tentar recuperar a interface home local do EJB "
								+ jndiName, e);
			}
		}
		return home;
	}

	/**
	 * Remove o EJBHome do cache do ServiceLocator
	 * 
	 * @throws ServiceLocatorException
	 * @param module
	 *            nome do modulo
	 */
	protected void removerEJBHomeDoCache(String module)
			throws ServiceLocatorException {
		String jndiName = getServiceLocatorProperties().getProperty(
				PROPERTIE_PREFIX_JNDI + module);
		String classNameEJBHome = getServiceLocatorProperties().getProperty(
				PROPERTIE_PREFIX_EJBHOME + module);

		if (jndiName == null || classNameEJBHome == null) {
			logger
					.warn("Não foi encontrado um modulo no arquivo de propriedades "
							+ "registrado com nome '"
							+ module
							+ "'. Verifique se o arquivo de "
							+ "propriedades desta aplicacao (<NomeApp>ServiceLocator.properties) está correto.");
		} else {
			logger
					.warn("################################### !!! ATENCAO !!! ############################################################");
			logger.warn("ATENCAO!!! O OBJETO EJBHome REFERENTE AO JNDI:"
					+ jndiName + " E homeClass:" + classNameEJBHome
					+ " NÃO ESTÁ RESPONDENDO CORRETAMENTE!! ");
			logger
					.warn("CASO O SERVIDOR DE APLICAÇÃO TENHA SIDO REINICIADO, É NORMAL QUE NUMA PRÓXIMA REQUISICAO TODOS OS OBJETOS EJBHome SEJAM CRIADOS NOVAMENTE! ");
			logger
					.warn("CASO O SERVIDOR DE APLICAÇÃO !!NAO!! TENHA SIDO REINICIADO, ENTRE EM CONTATO COM O ARQUITETO RESPONSAVEL PELA APLICACAO ");
			Object object = serviceCache.remove(jndiName);
			logger.warn("Objeto removido do HashMap sob chave '" + jndiName
					+ "'; objeto='" + object + "'");
			logger.warn("");
			logger
					.warn("################################################################################################################");
		}

	}

	/**
	 * Recupera uma fachada pelo nome do modulo, considerando que o método de
	 * criacao da Home não recebe argumentos
	 * 
	 * @return Implementacao de IFacade referente ao modulo
	 * @param module
	 *            Nome do modulo
	 */
	public synchronized IFacade getFacade(String module)
			throws ServiceLocatorException {
		return getFacade(module, new Object[] {});
	}

	/**
	 * Avalia se o modulo está contido num sistema externo
	 * 
	 * @param module
	 *            nome do modulo
	 */
	protected boolean isSistemaExterno(String module)
			throws ServiceLocatorException {
		Properties properties = getServiceLocatorProperties();
		String sistemaExt = properties
				.getProperty(PROPERTIE_PREFIX_SISTEMA_EXTERNO + module);
		return !FrameworkAcheUtil.isNullOrEmpty(sistemaExt);
	}

	/**
	 * Recupera facades remotas (POJOs)
	 * 
	 * @param constructorParameters
	 *            argumentos do construtor da facade
	 * @param module
	 *            Nome do modulo do facade
	 * @return Implementacao da facade
	 */
	protected IFacade getLocalFacade(String module,
			Object[] constructorParameters) throws ServiceLocatorException {
		if (logger.isDebugEnabled()) {
			logger
					.debug("Recuperando fachada local (FacadeImpl) para o modulo "
							+ module);
		}
		IFacade localFacade;

		String facadeImplClass = getServiceLocatorProperties().getProperty(
				PROPERTIE_PREFIX_FACADE_IMPL + module);

		if (facadeImplClass == null) {
			throw new ServiceLocatorException(
					"Não foi possível encontrar a classe de implementacao da "
							+ "Facade do modulo "
							+ module
							+ "! Verifique se esta classe existe, esta com o nome correto,"
							+ " e esta no classPath!");
		}

		Class[] classParameters = FrameworkAcheUtil
				.getClasses(constructorParameters);

		try {
			localFacade = (IFacade) FrameworkAcheUtil.instanciaObjeto(
					facadeImplClass, classParameters, constructorParameters);
		} catch (FrameworkAcheUtilError e) {
			throw new ServiceLocatorException(
					"Não foi possível instanciar a implementacao de facade "
							+ facadeImplClass
							+ " do modulo "
							+ module
							+ "! Verifique se esta classe existe, esta com o nome correto, e esta no classPath!",
					e);
		}

		return localFacade;

	}

	/**
	 * Recupera facades remotas (EJBs)
	 * 
	 * @param constructorParameters
	 *            argumentos do construtor da facade
	 * @param module
	 *            Nome do modulo do facade
	 * @return referencia da facade remota
	 */
	protected IFacade getRemoteFacade(String module,
			Object[] constructorParameters) throws ServiceLocatorException {
		if (logger.isDebugEnabled()) {
			logger.debug("Recuperando fachada para o modulo " + module);
		}
		IFacade facade;

		String jndiName = getServiceLocatorProperties().getProperty(
				PROPERTIE_PREFIX_JNDI + module);
		String classNameEJBHome = getServiceLocatorProperties().getProperty(
				PROPERTIE_PREFIX_EJBHOME + module);

		EJBHome home;
		try {
			/* recupera um array de classes a partir de um array de objetos */
			Class[] classes = FrameworkAcheUtil
					.getClasses(constructorParameters);

			/* recupera a interface home */
			Class classEJBHome = Class.forName(classNameEJBHome);
			home = getHome(jndiName, classEJBHome);
			Class homeClass2 = home.getClass();

			/* invoca a criacao do Stub Cliente */
			Method createMethod = homeClass2.getMethod("create", classes);
			Object object = createMethod.invoke(home, constructorParameters);

			/* cast */
			facade = (IFacade) object;

		} catch (SecurityException e) {
			removerEJBHomeDoCache(module);
			throw new ServiceLocatorException("Exceção de seguranca", e);

		} catch (NoSuchMethodException e) {
			removerEJBHomeDoCache(module);
			throw new ServiceLocatorException(
					"Não foi possível encontrar o metodo create() no EJBHome",
					e);

		} catch (IllegalArgumentException e) {
			removerEJBHomeDoCache(module);
			throw new ServiceLocatorException(
					"Argumentos ilegais para o método create", e);

		} catch (IllegalAccessException e) {
			removerEJBHomeDoCache(module);
			throw new ServiceLocatorException("Acesso ilegar", e);

		} catch (InvocationTargetException e) {
			removerEJBHomeDoCache(module);
			throw new ServiceLocatorException(
					"Erro ao tentar recuperar o EJBHome", e);

		} catch (Exception e) {
			removerEJBHomeDoCache(module);
			throw new ServiceLocatorException("Exceção não esperada ", e);

		}

		return facade;

	}

	/**
	 * Recupera uma fachada pelo nome do modulo, mais seus argumentos de seu
	 * construtor
	 * 
	 * @return Implementacao de IFacade referente ao modulo
	 * @param constructorParameters
	 *            Argumentos do construtor
	 * @param module
	 *            Nome do modulo
	 */
	public synchronized IFacade getFacade(String module,
			Object[] constructorParameters) throws ServiceLocatorException {
		IFacade facade;

		/* TESTA SE O MODULO É DE UM SISTEMA EXTERNO */
		if (isSistemaExterno(module)) {
			facade = getFacadeExterna(module, constructorParameters);

		} else {
			/*
			 * facades DESTE SISTEMA (sistema que está extentendo esta classe)
			 * remotas ou locais NNNNAAAAAOOOOOOO
			 * COOONNNNNFFFFUUUNNNDDDDIIIIRRRRR facades EXTERNAS com facades
			 * REMOTAS
			 */
			facade = getRemoteFacade(module, constructorParameters);


		}
		return facade;
	}

	/**
	 * Recupera uma facade externa a este sistema
	 * 
	 * @param constructorParameters
	 *            Parametros do construtor da facade
	 * @param module
	 *            Nome do modulo da facade
	 * @return Facade externa
	 */
	protected IFacade getFacadeExterna(String module,
			Object[] constructorParameters) throws ServiceLocatorException {
		IServiceLocator serviceLocator = getServiceLocatorFor(module);

		IFacade facade = serviceLocator
				.getFacade(module, constructorParameters);

		return facade;
	}

	/**
	 * Recupera o IServiceLocator que pode recuperar a facade do modulo
	 * informado no argumento
	 * 
	 * @param module
	 *            nome do modulo
	 * @return Implementacao de IServiceLocator que pode recuperar o modulo
	 *         informado no argumento
	 * @throws ServiceLocatorException
	 */
	protected IServiceLocator getServiceLocatorFor(String module)
			throws ServiceLocatorException {
		Properties properties = getServiceLocatorProperties();
		String serviceLocatorClasName = properties
				.getProperty(PROPERTIE_PREFIX_SISTEMA_EXTERNO + module);

		if (FrameworkAcheUtil.isNullOrEmpty(serviceLocatorClasName)) {
			throw new ServiceLocatorException(
					"Nao foi possivel identificar qual e a implementacao de "
							+ "IServiceLocator para o modulo " + module
							+ ". Verifique se o modulo " + module
							+ " é um modulo externo a" + " este sistema !");
		}

		IServiceLocator serviceLocator;
		try {
			serviceLocator = (IServiceLocator) FrameworkAcheUtil
					.instanciaObjeto(serviceLocatorClasName, new Class[] {},
							new Object[] {});
		} catch (FrameworkAcheUtilError e) {
			throw new ServiceLocatorException(
					"Erro ao tentar instanciar a implementação de "
							+ "IServiceLocator "
							+ serviceLocatorClasName
							+ " para recuperar a facade externa do modulo "
							+ module
							+ ". Verifique se esta implementacao de IServiceLocator existe e está no classPath!",
					e);
		}

		return serviceLocator;
	}

	/**
	 * Retorna uma implementacao de SessionFactory de Hibernate
	 * 
	 * @throws ServiceLocatorException
	 * @return Implementacao de SessionFactory de Hibernate
	 */
	public synchronized SessionFactory getSessionFactory()
			throws ServiceLocatorException {
		if (sessionFactory == null) {
			String sessionFactoryJNDI = sessionFactoryJNDI = getServiceLocatorProperties()
					.getProperty(PROPERTIE_JNDI_SESSION_FACTORY);

			try {
				/*
				 * ESTE CÓDIGO CONFIGURA, INSTANCIA E BINDA O SessionFactory no
				 * JBoss
				 */
				if (!hibernateConfigurado) {
					logger
							.warn("Invocando o metodo buildSessionFactory() pela primeira vez. Esta mensagem so deve aparecer uma unica vez");
					Configuration configuration;
					String arquivoCfgXml = getServiceLocatorProperties()
							.getProperty(PROPERTIE_HIBERNATE_CFG_APP_SERVER);
					configuration = new Configuration()
							.configure(arquivoCfgXml);
					configuration.buildSessionFactory();
					hibernateConfigurado = true;
				}
				/*
				 * ESTE CÓDIGO CONFIGURA, INSTANCIA E BINDA O SessionFactory no
				 * JBoss
				 */
				InitialContext ctx = new InitialContext();
				if (logger.isDebugEnabled()) {
					logger
							.debug("Fazendo lookup para encontrar SessionFactory ("
									+ sessionFactoryJNDI + ")");
				}
				Object obj = ctx.lookup(sessionFactoryJNDI);
				sessionFactory = (SessionFactory) obj;

			} catch (HibernateException e) {
				throw new ServiceLocatorException(
						"Erro ao tentar recuperar o SessionFactory para Hibernate (JNDI:"
								+ sessionFactoryJNDI + ")", e);

			} catch (NamingException e) {
				throw new ServiceLocatorException(
						"Erro ao tentar recuperar o SessionFactory para Hibernate (JNDI:"
								+ sessionFactoryJNDI + ")", e);

			}

		}
		return sessionFactory;
	}

	/**
	 * Retorna uma implementacao de Session de Hibernate
	 * 
	 * @return Hibernate Session
	 * @throws ServiceLocatorException
	 */
	public synchronized Session getHibernateSession()
			throws ServiceLocatorException {
		Session session = null;

		try {

			SessionFactory sessionFactory = getSessionFactory();
			session = sessionFactory.getCurrentSession();
			// session.setFlushMode(FlushMode.COMMIT);
		} catch (HibernateException e) {
			hibernateConfigurado = false;
			sessionFactory = null;
			throw new ServiceLocatorException(
					"Não foi possível abrir a sessão Hibernate", e);

		}
		return session;
	}

	/**
	 * Fecha uma sessao hibernate
	 * 
	 * @param session
	 *            Sessao Hibernate a ser fechada
	 * @throws ServiceLocatorException
	 */
	public void closeHibernateSession(Session session)
			throws ServiceLocatorException {
		if (logger.isDebugEnabled()) {
			logger.debug("fechando Hibernate session ");
		}

		if (session == null) {
			logger.warn("ATENÇÃO!!!  SESSÃO HIBERNATE == NULL");
		} else {
			try {
				// session.flush();
				// session.close();
			} catch (HibernateException e) {
				throw new ServiceLocatorException(
						"NÃO FOI POSSÍVEL INVOCAR O session.flush() "
								+ "E session.close(). A TRANSACAO NÃO PODE COMPLETADA!",
						e);

			}
		}
	}

	/**
	 * Recupera o jndiname do DataSource do sistema
	 * 
	 * @return jndi name do sistema
	 * @param alias
	 *            do DataSourceJNDIName
	 * @throws ServiceLocatorException
	 */
	public String getDataSourceJNDIName(String propertieName)
			throws ServiceLocatorException {

		Properties properties = getServiceLocatorProperties();
		String jndiName = properties.getProperty(propertieName);

		return jndiName;
	}

	/**
	 * Retora uma implementacao de DataSource
	 * 
	 * @param propertieName
	 *            Nome do DataSource
	 * @throws BaseTechnicalError
	 */
	public DataSource getDataSource(String propertieName)
			throws ServiceLocatorException {
		String jndiName = getDataSourceJNDIName(propertieName);
		DataSource dataSource = null;

		try {
			InitialContext ctx = new InitialContext();

			Object object = ctx.lookup(jndiName);
			dataSource = (DataSource) object;

		} catch (NamingException e) {
			throw new ServiceLocatorException(
					"Erro ao tentar fazer Lookup do DataSource "
							+ propertieName
							+ " no app server. Verifique se este DataSource esta configurado corretamente "
							+ "no appServer e se seu nome esta correto!", e);
		}

		return dataSource;
	}

	/**
	 * Atribui um objeto Properties contendo as configuracoes do ServiceLocator
	 * 
	 * @param serviceLocatorProperties
	 *            Objeto propriedades de configuracao
	 * @throws ServiceLocatorException
	 */
	public void setServiceLocatorProperties(Properties serviceLocatorProperties)
			throws ServiceLocatorException {
		this.serviceLocatorProperties = serviceLocatorProperties;
	}

	/**
	 * Retora o Properties contendo as configuracoes do ServiceLocator
	 * 
	 * @return Objeto propriedades de configuracao
	 * @throws ServiceLocatorException
	 */
	public Properties getServiceLocatorProperties()
			throws ServiceLocatorException {

		return serviceLocatorProperties;
	}

	/**
	 * Retorna o ResourceBundle do sistema
	 * 
	 * @throws ServiceLocatorException
	 * @return Objeto ResourceBundle para internacionalizacao
	 */
	public ResourceBundle getResourceBundle() throws ServiceLocatorException {
		if (resourceBundle == null) {
			Properties properties = getServiceLocatorProperties();
			String systemResourceBundle = properties
					.getProperty(PROPERTIE_RESOURCE_BUNDLE);
			resourceBundle = ResourceBundle.getBundle(systemResourceBundle);

		}
		return resourceBundle;
	}

	/**
	 * Procura no (Sistema)ServiceLocator.properties o tamanho da página do vlh
	 * do módulo informado. <br>
	 * Se nao encontrar a propriedade no arquivo de properties, retorna o
	 * tamanho de página default DEFAULT_VLH_PAGESIZE
	 * 
	 * @param module
	 *            Nome do modulo
	 * @throws ServiceLocatorException
	 */
	public int getVlhPageSize(String module) throws ServiceLocatorException {
		Properties properties = getServiceLocatorProperties();
		String vlhPageSize = properties
				.getProperty(PROPERTIE_PREFIX_VLH_PAGESIZE + module);

		if (vlhPageSize == null || vlhPageSize.trim().equals("")) {
			logger.warn("Nao foi possivel encontrar a propriedade '"
					+ PROPERTIE_PREFIX_VLH_PAGESIZE + module + "'!...");
			vlhPageSize = DEFAULT_VLH_PAGESIZE;
		}

		int intPageSize = Integer.parseInt(vlhPageSize);

		return intPageSize;
	}

	/**
	 * Recuperar a implementacao de IDAOFactory
	 * 
	 * @return Implementacao de IDAOFactory
	 * @throws DAOFactoryException
	 * @throws ServiceLocatorException
	 */
	public synchronized IDAOFactory getDAOFactory()
			throws ServiceLocatorException, DAOFactoryException {
		return this.DAOFactory;
	}

	/**
	 * Atribui uma implementacao de IDAOFactory
	 * 
	 * @param Implementacao
	 *            de IDAOFactory
	 * @throws DAOFactoryException
	 * @throws ServiceLocatorException
	 */
	public void setDAOFactory(IDAOFactory factory)
			throws ServiceLocatorException, DAOFactoryException {
		this.DAOFactory = factory;
	}

	/**
	 * Avalia se este IServiceLocator recuperara referencias remotas de facade
	 */
	public boolean isRemoteFacades() throws ServiceLocatorException {
		return remoteFacades;
	}

	/**
	 * Avalia se este IServiceLocator recuperara referencias remotas de facade
	 */
	public void setRemoteFacades(boolean remoteFacades)
			throws ServiceLocatorException {
		this.remoteFacades = true;
	}

	/**
	 * Implementação de finalização de facades para J2EEServiceLocator
	 */
	public void finishFacade(IFacade facade) throws ServiceLocatorException {
		try {
			facade.finishFacade();
		} catch (BaseTechnicalError e) {
			throw new ServiceLocatorException(
					"Erro técnico ao tentar finalizar facade", e);

		} catch (RemoteException e) {
			throw new ServiceLocatorException(
					"Excecao remota (Contrato EJB) ao tentar finalizar facade",
					e);

		}

		/* testa se o Facade é um SessionFacade, caso sim, invoca o remove() */
		if (facade instanceof EJBObject) {
			EJBObject ejbObject = (EJBObject) facade;
			try {
				ejbObject.remove();

			} catch (RemoteException e) {
				throw new ServiceLocatorException(
						"Excecao do contrado de EJB (RemoteException)! ", e);

			} catch (RemoveException e) {
				throw new ServiceLocatorException(
						"Erro ao tentar invocar o remove do EJB", e);

			}
		}

	}

	public void setRemoteInitialContext(InitialContext ctxEJB) {
		this.remoteInitialContext = ctxEJB;
	}

	public InitialContext getRemoteInitialContext()
			throws ServiceLocatorException {
		if (this.remoteInitialContext == null) {

			String ejbContextFactory = null;
			String ejbProviderURL = null;

			ejbContextFactory = getServiceLocatorProperties().getProperty(
					PROPERTIE_EJB_CONTEXT_FACTORY);
			ejbProviderURL = getServiceLocatorProperties().getProperty(
					PROPERTIE_EJB_PROVIDER_URL);

			Hashtable env = new Hashtable();
			env.put(InitialContext.INITIAL_CONTEXT_FACTORY, ejbContextFactory);
			env.put(InitialContext.PROVIDER_URL, ejbProviderURL);
			try {
				if (logger.isDebugEnabled()) {
					logger
							.debug("ServiceLocator: Creating initial context for: EJBContextFactory = '"
									+ ejbContextFactory
									+ "', EJBProviderURL = '"
									+ ejbProviderURL
									+ "'");
				}
				//setRemoteInitialContext(new InitialContext(env));
				setRemoteInitialContext(new InitialContext());
			} catch (NamingException e) {
				throw new ServiceLocatorException(
						"Erro na criação do EJB Initial Context!", e);
			}
		}

		return remoteInitialContext;
	}

	public void setLocalInitialContext(InitialContext localInitialContext) {
		this.localInitialContext = localInitialContext;
	}

	public InitialContext getLocalInitialContext()
			throws ServiceLocatorException {
		if (this.localInitialContext == null) {
			try {
				this.localInitialContext = new InitialContext();
			} catch (NamingException e) {
				throw new ServiceLocatorException(
						"Erro ao tentar construir um InitialContext para localização de servicos locais",
						e);
			}
		}
		return localInitialContext;
	}
}