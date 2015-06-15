/*
 * Created on 03/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.dlp.framework.servicelocator.IServiceLocator;
import br.com.dlp.framework.util.FrameworkAcheUtil;
import br.com.dlp.framework.util.FrameworkAcheUtilError;
import br.com.dlp.framework.util.properties.PropertiesFactory;
import br.com.dlp.framework.util.properties.PropertiesFactoryException;

/**
 * @author PLDarcio
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractDAOFactory implements IDAOFactory {
	protected Log logger = LogFactory.getLog(AbstractDAOFactory.class);

	private Properties daoProperties = null;

	private IServiceLocator serviceLocator;

	private Map daoCache = new HashMap(10);

	/**
	 * escrever um método getInstance (é Singleton) para este construtor
	 * 
	 * @throws PropertiesFactoryException
	 */
	public AbstractDAOFactory(IServiceLocator serviceLocator)
			throws DAOFactoryException, PropertiesFactoryException {
		this.serviceLocator = serviceLocator;
		daoProperties = getProperties();
	}

	/**
	 * Override do metodo getProperties da superclasse
	 * 
	 * @throws PropertiesFactoryException
	 * @return Objeto contendo as propriedades para o DAOFactory
	 */
	protected Properties getProperties() throws PropertiesFactoryException {
		PropertiesFactory propertiesFactory = PropertiesFactory.getInstance();
		Properties properties = propertiesFactory
				.getProperties(this.getClass());
		return properties;
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
		if (logger.isDebugEnabled()) {
			logger.debug("Construindo uma implementação de DAO para o módulo "
					+ nomeModulo + "...");
		}

		String nomeClasse = daoProperties.getProperty(nomeModulo);

		if (logger.isDebugEnabled()) {
			logger.debug("..." + nomeClasse
					+ " é a implementação de DAO para o módulo " + nomeModulo);
		}
		try {

			IDAO dao = (IDAO) FrameworkAcheUtil.instanciaObjeto(nomeClasse,
					new Class[] {}, new Object[] {});

			dao.setServiceLocator(this.serviceLocator);
			return dao;

		} catch (FrameworkAcheUtilError e) {
			throw new DAOFactoryException(
					"Erro ao tentar instanciar a classe de implementacao de IDAO '"
							+ nomeClasse + "' para o modulo '" + nomeModulo
							+ "'!!", e);

		} catch (DAOException e) {
			throw new DAOFactoryException(
					"Erro ao tentar atribuir a referencia de IServiceLocator '"
							+ this.serviceLocator + "' " + "ao dao '"
							+ nomeClasse + "' do modulo '" + nomeModulo + "'",
					e);
		}

	}

	protected IDAO getCachedDAO(String nomeModulo) throws DAOFactoryException {
		Map map = getDaoCache();
		IDAO idao = (IDAO) map.get(nomeModulo);

		if (logger.isDebugEnabled()) {
			logger.debug("DAO do modulo '" + nomeModulo + "' cacheado = '"
					+ idao + "'!");
		}

		return idao;
	}

	protected void cacheDAO(String nomeModulo, IDAO idao)
			throws DAOFactoryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Fazendo cache do DAO do modulo '" + nomeModulo
					+ "' = '" + idao + "'!");
		}

		Map map = getDaoCache();
		map.put(nomeModulo, idao);
	}

	public final IDAO getDAO(String nomeModulo) throws DAOFactoryException {
		IDAO idao = getCachedDAO(nomeModulo);

		if (idao == null) {
			idao = createDAO(nomeModulo);
			if (logger.isDebugEnabled()) {
				logger.debug("Colocando no cache o dao '" + idao
						+ "' criado para o modulo '" + nomeModulo + "'!");
			}
			cacheDAO(nomeModulo, idao);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Ja existe um dao cacheado para o modulo '"
						+ nomeModulo + "' = '" + idao + "'!");
			}
		}

		/* APENAS LOG DO TAMANHO DO CACHE */
		Map daoCache = getDaoCache();
		if (daoCache != null) {
			logger.warn("TAMANHO DO CACHE DE DAOS = " + daoCache.size());
			if (logger.isDebugEnabled()) {
				Iterator daosCacheados = daoCache.keySet().iterator();
				logger
						.debug("***************************************************************************");
				logger
						.debug("**                    LISTA DOS DAOS CACHEADOS                           **");
				logger
						.debug("**                                                                       **");
				while (daosCacheados.hasNext()) {
					Object chaveDaoModulo = daosCacheados.next();
					Object daoCacheado = daoCache.get(chaveDaoModulo);
					logger.debug("** " + chaveDaoModulo + ":");
					logger.debug("** " + daoCacheado);
					logger
							.debug("**                                                                       **");
				}
				logger
						.debug("**                                                                       **");
				logger
						.debug("**                    LISTA DOS DAOS CACHEADOS                           **");
				logger
						.debug("***************************************************************************");
			}
		}

		return idao;
	}

	/**
	 * @return Returns the daoCache.
	 */
	protected Map getDaoCache() {
		return this.daoCache;
	}

	/**
	 * @param daoCache
	 *            The daoCache to set.
	 */
	protected void setDaoCache(Map daoCache) {
		this.daoCache = daoCache;
	}
}
