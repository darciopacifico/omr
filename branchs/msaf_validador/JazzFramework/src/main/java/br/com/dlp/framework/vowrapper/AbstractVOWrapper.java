package br.com.dlp.framework.vowrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.dao.WebQueryOrder;
import br.com.dlp.framework.util.AcheCollectionUtils;
import br.com.dlp.framework.util.FrameworkAcheUtil;
import br.com.dlp.framework.util.FrameworkAcheUtilError;
import br.com.dlp.framework.vo.IBaseVO;

/**
 * 
 */
public abstract class AbstractVOWrapper implements IVOWrapper {
	private IBaseVO baseVO;

	private transient ResourceBundle resourceBundle;

	private boolean selecionado;

	protected Log logger = LogFactory.getLog(AbstractVOWrapper.class);

	private List webQueryOrders = new ArrayList(6);

	public AbstractVOWrapper(ResourceBundle resourceBundle) {
		setResourceBundle(resourceBundle);
		setBaseVO(baseVO);

	}

	public WebQueryOrder getWebQueryOrder(int indexOf) {
		WebQueryOrder webQueryOrder;
		try {
			webQueryOrder = (WebQueryOrder) webQueryOrders.get(indexOf);
		} catch (IndexOutOfBoundsException e) {
			webQueryOrder = new WebQueryOrder("", true);
			webQueryOrders.add(indexOf, webQueryOrder);
		}
		return webQueryOrder;
	}

	public void setWebQueryOrder(int indexOf, WebQueryOrder webQueryOrder) {
		this.webQueryOrders.set(indexOf, webQueryOrder);
	}

	public List getWebQueryOrders() {
		List returnValues = new ArrayList(6);
		Iterator iterator = webQueryOrders.iterator();

		while (iterator.hasNext()) {
			WebQueryOrder webQueryOrder = (WebQueryOrder) iterator.next();
			if (webQueryOrder != null && webQueryOrder.isConsiderar()) {
				returnValues.add(webQueryOrder);
			}
		}

		return returnValues;
	}

	public QueryOrder[] getArrayQueryOrders() {
		List listQueryOrders = getWebQueryOrders();
		QueryOrder[] queryOrders = new QueryOrder[listQueryOrders.size()];

		int index = 0;
		Iterator iterator = listQueryOrders.iterator();
		while (iterator.hasNext()) {
			queryOrders[index++] = (QueryOrder) iterator.next();
		}

		return queryOrders;
	}

	public void setWebQueryOrders(List webQueryOrders) {
		this.webQueryOrders = webQueryOrders;
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	public int getIndexOf(List list, IBaseVO baseVO) {
		int indexOf = AcheCollectionUtils.indexOf(list, baseVO);
		return indexOf;
	}

	public IBaseVO getBaseVO(List list, int indexOf) {
		IBaseVO baseVO = AcheCollectionUtils.getBaseVO(list, indexOf);
		return baseVO;
	}

	public Object convert(String string, Class class1) {
		return getConvertUtilsBean().convert(string, class1);
	}

	public String convert(Object object) {
		return getConvertUtilsBean().convert(object);
	}

	public IBaseVO getBaseVO() {
		return baseVO;
	}

	public void setBaseVO(IBaseVO baseVO) {
		this.baseVO = baseVO;
	}

	public List generateVOWrapperList(Collection list, Class voWrapperClass)
			throws VOWrapperException {

		int listSize = 0;

		if (list == null) {
			/* retorna uma lista vazia só para não quebrar o contrado */
			return new ArrayList(0);
		}

		ArrayList voWrapperList = new ArrayList(listSize);

		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Object object = (Object) iterator.next();
			IVOWrapper wrapper = generateVOWrapper(object, voWrapperClass);
			voWrapperList.add(wrapper);
		}

		return voWrapperList;
	}

	public List generateBaseVOList(Collection list, Class voWrapperClass)
			throws VOWrapperException {

		ArrayList baseVOList = new ArrayList(list.size());

		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Object object = (Object) iterator.next();

			IBaseVO baseVO;
			if (object == null) {
				baseVO = null;

			} else if (object instanceof IVOWrapper) {
				IVOWrapper wrapper = (IVOWrapper) object;
				baseVO = getBaseVO(wrapper);

			} else if (object instanceof IBaseVO) {
				baseVO = (IBaseVO) object;

			} else {
				throw new VOWrapperException(
						"Os objetos contidos na colecao (argumento Collection list) "
								+ "devem ser instanceof IBaseVO ou IVOWrapper");

			}

			baseVOList.add(baseVO);
		}

		return baseVOList;
	}

	public IBaseVO getBaseVO(IVOWrapper wrapper) {
		return wrapper.getBaseVO();
	}

	public IVOWrapper generateVOWrapper(Object object, Class voWrapperClass)
			throws VOWrapperException {

		IVOWrapper wrapper;

		if (voWrapperClass == null) {
			throw new VOWrapperException(
					"O argumento 'Class voWrapperClass' deste metodo nao pode ser nulo!");

		} else if (object == null) {
			throw new VOWrapperException("O objeto a ser encapsulado por "
					+ voWrapperClass.getName() + " não pode ser nulo!");

		}

		if (object instanceof IBaseVO) {
			IBaseVO baseVO = (IBaseVO) object;
			ResourceBundle resourceBundle = getResourceBundle();

			Class classes[] = new Class[] { ResourceBundle.class };
			Object objects[] = new Object[] { resourceBundle };

			try {
				wrapper = (IVOWrapper) FrameworkAcheUtil.instanciaObjeto(
						voWrapperClass, classes, objects);
				wrapper.setBaseVO(baseVO);

			} catch (FrameworkAcheUtilError e) {
				throw new VOWrapperException(
						"Nao foi possivel instanciar a implementacao de IVOWrapper "
								+ voWrapperClass.getName() + "!", e);
			}

		} else if (object instanceof IVOWrapper) {
			wrapper = (IVOWrapper) object;

		} else {
			throw new VOWrapperException(
					"O objeto a ser encapsulado deve ser instanceof de IBaseVO ou IVOWrapper !");

		}

		return wrapper;
	}

	protected PropertyUtilsBean getPropertyUtilsBean() {
		return new PropertyUtilsBean();
	}

	protected ConvertUtilsBean getConvertUtilsBean() {

		ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
		FrameworkAcheUtil.configureConvertUtilsBean(convertUtilsBean,
				getResourceBundle());

		return convertUtilsBean;
	}

	protected BeanUtilsBean getBeanUtilsBean() {

		PropertyUtilsBean propertyUtilsBean = getPropertyUtilsBean();
		ConvertUtilsBean convertUtilsBean = getConvertUtilsBean();
		BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean,
				propertyUtilsBean);

		return beanUtilsBean;

	}

	/**
	 * Itera uma coleção de IVOWrapper e retorna o !! <b>PRIMEIRO </b>!!
	 * IVOWrapper cujo atributo 'selecionado' é true
	 */
	public static IVOWrapper getIVOWrapperSelecionado(Collection wrappers) {

		Iterator iterator = wrappers.iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object == null) {
			} else if (!(object instanceof IVOWrapper)) {

			} else {
				IVOWrapper wrapper = (IVOWrapper) object;

				if (wrapper.isSelecionado()) {
					return wrapper;
				}

			}
		}

		return null;
	}

	/**
	 * Itera uma coleção de IVOWrapper e retorna o !! <b>PRIMEIRO </b>!! IBaseVO
	 * cujo atributo 'selecionado' do wrapper é true
	 */
	public static IBaseVO getBaseVOSelecionado(Collection wrappers) {
		IVOWrapper wrapper = getIVOWrapperSelecionado(wrappers);

		IBaseVO baseVO = null;

		if (wrapper != null) {
			baseVO = wrapper.getBaseVO();
		}

		return baseVO;
	}

	/**
	 * Itera uma coleção de IVOWrapper e retorna !! <b>TODOS </b>!! os
	 * IVOWrapper cujo atributo 'selecionado' é true
	 */
	public static List getIVOWrapperSelecionados(Collection wrappers) {

		List list = new ArrayList(4);

		Iterator iterator = wrappers.iterator();
		while (iterator.hasNext()) {
			Object object = iterator.next();

			if (object == null) {
				// logger.warn("Atencao!! Item object==null !");

			} else if (!(object instanceof IVOWrapper)) {
				// logger.warn("Atencao!! O item '" + object + "' contido na
				// colecao, nao e instance of IVOWrapper!");

			} else {
				IVOWrapper wrapper = (IVOWrapper) object;

				if (wrapper.isSelecionado()) {
					list.add(wrapper);
				}

			}
		}

		return list;
	}

	/**
	 * Itera uma coleção de IVOWrapper e retorna !! <b>TODOS </b>!! os IBaseVO
	 * cujo atributo 'selecionado' do wrapper é true
	 */
	public static List getIBaseVOSelecionados(Collection wrappers) {

		List wrapperList = getIVOWrapperSelecionados(wrappers);

		List ibaseVOSelecionados = new ArrayList(wrapperList.size());

		Iterator iterator = wrapperList.iterator();

		while (iterator.hasNext()) {

			IVOWrapper wrapper = (IVOWrapper) iterator.next();

			ibaseVOSelecionados.add(wrapper.getBaseVO());
		}

		return ibaseVOSelecionados;

	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

}
