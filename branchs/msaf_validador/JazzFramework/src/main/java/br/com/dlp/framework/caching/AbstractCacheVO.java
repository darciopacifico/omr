package br.com.dlp.framework.caching;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import br.com.dlp.framework.pk.PKException;
import br.com.dlp.framework.util.FrameworkAcheUtil;
import br.com.dlp.framework.util.FrameworkAcheUtilError;
import br.com.dlp.framework.vo.AbstractCadastroBaseVO;

/**
 * VO para configuracoes de Caching geral
 */
public abstract class AbstractCacheVO extends AbstractCadastroBaseVO {
	private Long cacheTimeOutMilis;

	private Collection cacheElements = new ArrayList(0);

	private static final long serialVersionUID = 1L;

	public AbstractCacheVO(AbstractCachePK abstractCachePK) {
		try {
			setIPK(abstractCachePK);
		} catch (PKException e) {
			// Sem tratamento
		}
	}

	/**
	 * Procura e incrementa recursivamente os getMaximumSize de todos os
	 * elementos-filho cacheados
	 * 
	 * @throws CachingException
	 */
	public Long getMaximumSize() throws CachingException {
		Collection cacheElements = getCacheElements();
		long returnValue = 0;
		Iterator iterator = cacheElements.iterator();
		while (iterator.hasNext()) {
			AbstractCacheVO abstractCacheVO = (AbstractCacheVO) iterator.next();
			returnValue += abstractCacheVO.getMaximumSize().longValue();
		}
		return new Long(returnValue);
	}

	/**
	 * Procura e incrementa recursivamente os getCurrentSize de todos os
	 * elementos-filho cacheados
	 */
	public Long getCurrentSize() {
		Collection cacheElements = getCacheElements();
		long returnValue = 0;
		Iterator iterator = cacheElements.iterator();
		while (iterator.hasNext()) {
			AbstractCacheVO abstractCacheVO = (AbstractCacheVO) iterator.next();
			returnValue += abstractCacheVO.getCurrentSize().longValue();
		}
		return new Long(returnValue);
	}

	/**
	 * Invoca recursivamente os fullClearCache de todos os elementos-filho
	 * cacheados
	 */
	public void fullClearCache() {
		Collection cacheElements = getCacheElements();
		Iterator iterator = cacheElements.iterator();
		while (iterator.hasNext()) {
			AbstractCacheVO abstractCacheVO = (AbstractCacheVO) iterator.next();
			abstractCacheVO.fullClearCache();
		}
	}

	/**
	 * Invoca recursivamente os toLimitCache de todos os elementos-filho
	 * cacheados
	 */
	public void toLimitCache() {
		Collection cacheElements = getCacheElements();
		Iterator iterator = cacheElements.iterator();
		while (iterator.hasNext()) {
			AbstractCacheVO abstractCacheVO = (AbstractCacheVO) iterator.next();
			abstractCacheVO.toLimitCache();
		}
	}

	public Collection getCacheElements() {
		return cacheElements;
	}

	public Long getCacheTimeOutMilis() {
		return cacheTimeOutMilis;
	}

	public void setCacheElements(Collection cacheElements) {
		this.cacheElements = cacheElements;
	}

	public void setCacheTimeOutMilis(Long cacheTimeOutMilis) {
		this.cacheTimeOutMilis = cacheTimeOutMilis;
	}

	public AbstractCacheVO getCacheElement(AbstractCachePK key) {
		Collection cacheElements = getCacheElements();
		Iterator iterator = cacheElements.iterator();

		AbstractCacheVO returnVO = null;
		while (iterator.hasNext()) {
			AbstractCacheVO voDaVez = (AbstractCacheVO) iterator.next();
			/* comparo o PK */
			if (voDaVez.getIPK().compare(key)) {
				returnVO = voDaVez;
				break;
			}
		}

		return returnVO;
	}

	public boolean isCached(AbstractCachePK key) {
		Collection cacheElements = getCacheElements();
		Iterator iterator = cacheElements.iterator();

		boolean returnValue = false;
		while (iterator.hasNext()) {
			AbstractCacheVO voDaVez = (AbstractCacheVO) iterator.next();
			/* comparo o PK */
			if (voDaVez.getIPK().compare(key)) {
				returnValue = true;
				break;
			}
		}

		return returnValue;
	}

	public AbstractCacheVO getCacheElement(AbstractCachePK key, boolean create)
			throws CachingException {
		AbstractCacheVO returnVO = getCacheElement(key);

		if (create && returnVO == null) {
			returnVO = createChildCacheVO(key);
			addCacheElement(returnVO);
		}

		return returnVO;
	}

	public void addCacheElement(AbstractCacheVO abstractCacheVO)
			throws CachingException {
		Collection collection = getCacheElements();
		collection.add(abstractCacheVO);
	}

	protected AbstractCacheVO createChildCacheVO(AbstractCachePK childKey)
			throws CachingException {
		Class childClass = getChildClass();
		AbstractCacheVO abstractCacheVO;
		try {
			abstractCacheVO = (AbstractCacheVO) FrameworkAcheUtil
					.instanciaObjeto(childClass, new Class[] {},
							new Object[] {});
			abstractCacheVO.setIPK(childKey);
		} catch (FrameworkAcheUtilError e) {
			throw new CachingException(
					"Erro ao tentar um objeto filho (tipoFilho=" + childClass
							+ ")", e);

		} catch (PKException e) {
			throw new CachingException("Erro ao tentar atribuir a pk='"
					+ childKey + "' no vo que esta sendo criado!", e);

		}

		return abstractCacheVO;
	}

	protected abstract Class getChildClass() throws CachingException;

}
