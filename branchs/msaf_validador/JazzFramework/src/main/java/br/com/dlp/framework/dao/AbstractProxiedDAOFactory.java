package br.com.dlp.framework.dao;

import java.lang.reflect.Proxy;
import java.util.Properties;

import br.com.dlp.framework.invocationhandler.IBaseInvocationHandler;
import br.com.dlp.framework.invocationhandler.InvocationHandlerException;
import br.com.dlp.framework.invocationhandler.InvocationHandlerFactory;
import br.com.dlp.framework.servicelocator.IServiceLocator;
import br.com.dlp.framework.util.properties.PropertiesFactoryException;

/**
 * Cria proxies com finalidades diversas para os DAOs retornados pela classe pai
 * (AbstractDAOFactory)
 */
public abstract class AbstractProxiedDAOFactory extends AbstractDAOFactory {
	private static final String PROPERTIE_PREFIX_ISPROXIED_DAO = "isproxied_";

	private static final String PROPERTIE_PREFIX_INVOCATION_HANDLER = "inv_hand_";

	private InvocationHandlerFactory invocationHandlerFactory;

	/**
	 * Construtor de AbstractProxiedDAOFactory. Explicita o relacionamento de
	 * composicao com InvocationHandlerFactory
	 */
	protected AbstractProxiedDAOFactory(IServiceLocator serviceLocator)
			throws DAOFactoryException, PropertiesFactoryException {
		super(serviceLocator);
		invocationHandlerFactory = InvocationHandlerFactory.getInstance();
	}

	/**
	 * Este metodo identifica qual a classe de implementacao de IDAO para o
	 * modulo informado logo depois instancia e retorna o IDAO.
	 * 
	 * @param nomeModulo
	 *            Código de referencia do modulo
	 * @return Implementacao de IDAO referente ao modulo informado
	 */
	protected IDAO createDAO(String nomeModulo) throws DAOFactoryException {
		IDAO idao;

		if (isProxiedDAO(nomeModulo)) {

			if (logger.isDebugEnabled()) {
				logger.debug("A implementacao de dao do modulo '" + nomeModulo
						+ "' DEVERA possuir proxy (caching, logging etc...) ");
			}
			idao = createProxiedDAO(nomeModulo);
		} else {

			if (logger.isDebugEnabled()) {
				logger
						.debug("A implementacao de dao do modulo '"
								+ nomeModulo
								+ "' !!NAO!! DEVERA possuir proxy (caching, logging etc...) ");
			}
			idao = createNotProxiedDAO(nomeModulo);
		}

		return idao;
	}

	/**
	 * Pesquiso no arquivo de propriedades se o IDAO de "nomeModulo" deve ser
	 * ProxiedDAO
	 */
	protected boolean isProxiedDAO(String nomeModulo)
			throws DAOFactoryException {
		Properties properties;
		try {
			properties = getProperties();
		} catch (PropertiesFactoryException e) {
			throw new DAOFactoryException(
					"Erro ao tentar recuperar properties do daofactory!!", e);
		}

		boolean isProxied;
		String strIsProxied = properties
				.getProperty(PROPERTIE_PREFIX_ISPROXIED_DAO + nomeModulo);
		isProxied = Boolean.valueOf(strIsProxied).booleanValue();

		return isProxied;
	}

	/**
	 * Recupero o dao notProxiedDAO e crio com este um proxiedDAO
	 */
	protected IDAO createProxiedDAO(String nomeModulo)
			throws DAOFactoryException {

		IDAO notProxiedDAO = createNotProxiedDAO(nomeModulo);
		IDAO returnDAO;
		InvocationHandlerFactory invocationHandlerFactory = getInvocationHandlerFactory();

		String invocationHandlerClassName;
		IBaseInvocationHandler baseInvocationHandler;
		try {
			invocationHandlerClassName = getInvocationHandlerClassName(nomeModulo);
			baseInvocationHandler = invocationHandlerFactory
					.getInvocationHandler(notProxiedDAO,
							invocationHandlerClassName);

		} catch (PropertiesFactoryException e1) {
			logger
					.error("ATENCAO!! ERRO AO TENTAR DETERMINAR SE O DAO DO MODULO '"
							+ nomeModulo
							+ "' POSSUI ALGUM IBaseInvocationHandler!");
			logger
					.warn("VOU RETORNAR A IMPLEMENTACAO DE IDAO SEM NENHUM PROXY!");
			baseInvocationHandler = null;
			e1.printStackTrace();

		} catch (InvocationHandlerException e) {
			logger
					.error("ATENCAO!! ERRO AO TENTAR DETERMINAR SE O DAO DO MODULO '"
							+ nomeModulo
							+ "' POSSUI ALGUM IBaseInvocationHandler!");
			logger
					.warn("VOU RETORNAR A IMPLEMENTACAO DE IDAO SEM NENHUM PROXY!");
			baseInvocationHandler = null;
			e.printStackTrace();

		}

		/* a principio e por seguranca, o dao a ser retornado e o nao proxied */
		returnDAO = notProxiedDAO;
		if (baseInvocationHandler != null) {
			try {

				Class[] interfacesDAO = getInterfacesDAO(notProxiedDAO);
				returnDAO = (IDAO) Proxy.newProxyInstance(notProxiedDAO
						.getClass().getClassLoader(), interfacesDAO,
						baseInvocationHandler);

				if (logger.isDebugEnabled()) {
					logger
							.debug("Sucesso ao criar um proxied DAO para a implementacao de dao do modulo '"
									+ nomeModulo + "'!!");
				}

			} catch (Throwable throwable) {
				logger
						.error("ATENCAO!! NAO FOI POSSIVEL CRIAR UM PROXY PARA O DAO DO MODULO "
								+ nomeModulo + "!!");
				logger
						.warn("VOU CANCELAR O RETORNO DE UM PROXIED DAO E RETORNAR O DAO SEM PROXY!");
				throwable.printStackTrace();
				returnDAO = notProxiedDAO;
			}
		}

		return returnDAO;
	}

	/**
	 * Crio uma implementacao de dao normalmente
	 */
	protected IDAO createNotProxiedDAO(String nomeModulo)
			throws DAOFactoryException {
		IDAO dao = super.createDAO(nomeModulo);
		return dao;
	}

	/**
	 * Recupero qual sera o IBaseInvocationHandler utilizado no proxy do dao
	 */
	protected String getInvocationHandlerClassName(String nomeModulo)
			throws PropertiesFactoryException {
		Properties properties = getProperties();
		String invocationHandlerClassName = properties
				.getProperty(PROPERTIE_PREFIX_INVOCATION_HANDLER + nomeModulo);
		return invocationHandlerClassName;
	}

	/**
	 * Recupero as interfaces do dao
	 */
	protected Class[] getInterfacesDAO(Object object)
			throws DAOFactoryException {
		if (object == null) {
			throw new DAOFactoryException(
					"Nao e possivel identificar as interfaces de um objeto nulo!!");
		}

		Class[] interfaces = object.getClass().getInterfaces();

		return interfaces;
	}

	protected InvocationHandlerFactory getInvocationHandlerFactory() {
		return this.invocationHandlerFactory;
	}

	protected void setInvocationHandlerFactory(
			InvocationHandlerFactory invocationHandlerFactory) {
		this.invocationHandlerFactory = invocationHandlerFactory;
	}
}
