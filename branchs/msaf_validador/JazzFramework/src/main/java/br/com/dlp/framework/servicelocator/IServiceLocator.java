package br.com.dlp.framework.servicelocator;

import java.io.Serializable;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.dlp.framework.dao.DAOFactoryException;
import br.com.dlp.framework.dao.IDAOFactory;
import br.com.dlp.framework.facade.IFacade;

/**
 * Contrato de ServiceLocator. As implementações de IServiceLocator devem ser
 * construidas por ServiceLocatorFactory
 */
public interface IServiceLocator extends Serializable {
	public static final String DEFAULT_VLH_PAGESIZE = "5";

	public static final String PROPERTIE_PREFIX_VLH_PAGESIZE = "pagesize_";

	public static final String PROPERTIE_PREFIX_JNDI = "jndi_";

	public static final String PROPERTIE_PREFIX_FACADE = "facade_";

	public static final String PROPERTIE_PREFIX_EJBHOME = "ejbhome_";

	public static final String PROPERTIE_EJB_CONTEXT_FACTORY = "contextFactory";

	public static final String PROPERTIE_EJB_PROVIDER_URL = "providerURL";

	public static final String PROPERTIE_JNDI_SESSION_FACTORY = "jndiSessionFactory";

	public static final String PROPERTIE_HIBERNATE_CFG_APP_SERVER = "hibernateCfgAppServer";

	public static final String PROPERTIE_HIBERNATE_CFG_STAND_ALONE = "hibernateCfgStandAlone";

	public static final String PROPERTIE_RESOURCE_BUNDLE = "systemResourceBundle";

	public static final String PROPERTIE_MDBJNDI_CONNECTION_FACTORY = "mdbJndiConnectionFactory";

	public static final String PROPERTIE_MDBJNDI_POOL = "mdbJndiPool";

	public static final String PROPERTIE_MDBJNDI_BATCH = "mdbJndiBatch";

	/**
	 * Avalia se, ao tentar recuperar as facades do sistema, estas estarão
	 * remotas ou locais. Esta regra não se aplica à facades de outros sistemas,
	 * que sempre serão acessadas remotamente.<br>
	 * 
	 * @throws ServiceLocatorException
	 * @return Os facades são remotos ou não
	 */
	public boolean isRemoteFacades() throws ServiceLocatorException;

	/**
	 * Avalia se, ao tentar recuperar as facades do sistema, estas estarão
	 * remotas ou locais. Esta regra não se aplica à facades de outros sistemas,
	 * que sempre serão acessadas remotamente.
	 * 
	 * @throws ServiceLocatorException
	 * @param Os
	 *            facades são remotos ou não
	 */
	public void setRemoteFacades(boolean remoteFacades)
			throws ServiceLocatorException;

	/**
	 * Recupera uma SessionFactory de hibernate
	 */
	public SessionFactory getSessionFactory() throws ServiceLocatorException;

	/**
	 * Cria retorna um objeto Session para Hibernate
	 */
	public abstract Session getHibernateSession()
			throws ServiceLocatorException;

	/**
	 * Fecha um objeto Session para Hibernate
	 */
	public abstract void closeHibernateSession(Session session)
			throws ServiceLocatorException;

	/**
	 * Retorna a implementação de fachada referente ao modulo em questao
	 * <b>String module</b>.<br>
	 * A implementação de IFacade retornado pode ser a instância de FacadeImpl
	 * propriamente, um proxy (Stub EJB) para uma referência remota, um proxy
	 * para um WebService etc...
	 */
	public abstract IFacade getFacade(String module)
			throws ServiceLocatorException;

	/**
	 * Finaliza uma Facade
	 */
	public abstract void finishFacade(IFacade facade)
			throws ServiceLocatorException;

	/**
	 * Retorna a implementação de fachada referente ao modulo em questao
	 * <b>String module</b>.<br>
	 * A implementação de IFacade retornado pode ser a instância de FacadeImpl
	 * propriamente, um proxy (Stub EJB) para uma referência remota, um proxy
	 * para um WebService etc...
	 */
	public abstract IFacade getFacade(String module,
			Object[] constructorParameters) throws ServiceLocatorException;

	/**
	 * Retorna uma implementacao de javax.sql.DataSource
	 */
	public abstract DataSource getDataSource(String propertieDataSource)
			throws ServiceLocatorException;

	/**
	 * Retorna o conjunto de propriedades para internacionalização de labels e
	 * formatos do sistema
	 * 
	 * @throws ServiceLocatorException
	 */
	public ResourceBundle getResourceBundle() throws ServiceLocatorException;

	/**
	 * Configura um InitialContext para localização remota de recursos
	 */
	public void setRemoteInitialContext(InitialContext ctxEJB);

	/**
	 * Recupera um InitialContext para localização remota de recursos
	 */
	public InitialContext getRemoteInitialContext()
			throws ServiceLocatorException;

	/**
	 * Configura um InitialContext para localização local de recursos
	 */
	public void setLocalInitialContext(InitialContext localInitialContext);

	/**
	 * Recupera um InitialContext para localização local de recursos
	 */
	public InitialContext getLocalInitialContext()
			throws ServiceLocatorException;

	/**
	 * Retorna o tamanho da página do VLH do modulo informado
	 */
	public abstract int getVlhPageSize(String module)
			throws ServiceLocatorException;

	/**
	 * Atribui um objeto Properties contendo as configuracoes do ServiceLocator
	 * 
	 * @param serviceLocatorProperties
	 *            Objeto propriedades de configuracao
	 * @throws ServiceLocatorException
	 */
	public void setServiceLocatorProperties(Properties serviceLocatorProperties)
			throws ServiceLocatorException;

	/**
	 * Retora o Properties contendo as configuracoes do ServiceLocator
	 * 
	 * @return Objeto propriedades de configuracao
	 * @throws ServiceLocatorException
	 */
	public Properties getServiceLocatorProperties()
			throws ServiceLocatorException;

	public QueueSession getQueueSessionPool() throws ServiceLocatorException;

	public QueueSession getQueueSessionBatch() throws ServiceLocatorException;

	public Queue getQueuePool() throws ServiceLocatorException;

	public Queue getQueueBatch() throws ServiceLocatorException;

	public IDAOFactory getDAOFactory() throws ServiceLocatorException,
			DAOFactoryException;

	public void setDAOFactory(IDAOFactory factory)
			throws ServiceLocatorException, DAOFactoryException;
}