package br.com.dlp.framework.util.comparator;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CollectionBeanSortComparator implements Comparator {
	protected Log logger = LogFactory
			.getLog(CollectionBeanSortComparator.class);

	private boolean asc;

	private String property;

	/**
	 * Informe a propriedade a ser comparada e o sentido da ordenação
	 */
	public CollectionBeanSortComparator(String property, boolean asc) {
		super();
		this.asc = asc;
		this.property = property;
	}

	/**
	 * Informe a propriedade a ser comparada e o sentido da ordenação
	 */
	public CollectionBeanSortComparator() {
	}

	/**
	 * Informe a propriedade a ser comparada e o sentido da ordenação
	 */
	public CollectionBeanSortComparator(String property) {
		this(property, true);
	}

	/**
	 * Nesta implementação padrão, converte para String ou número e compara
	 */
	public int compareProperty(Object objProperty1, Object objProperty2) {
		int returnValue = 0;

		try {

			if (objProperty1 == null && objProperty2 == null) {
				returnValue = 0;

			} else if (objProperty1 != null && objProperty2 == null) {
				returnValue = 1;

			} else if (objProperty1 == null && objProperty2 != null) {
				returnValue = -1;

			} else if (objProperty1 instanceof Number
					&& objProperty2 instanceof Number) {
				Number nbrProperty1 = (Number) objProperty1;
				Number nbrProperty2 = (Number) objProperty2;
				returnValue = (int) (nbrProperty1.doubleValue() - nbrProperty2
						.doubleValue());

			} else if (objProperty1 instanceof Date
					&& objProperty2 instanceof Date) {
				Date dtProperty1 = (Date) objProperty1;
				Date dtProperty2 = (Date) objProperty2;
				returnValue = (int) (dtProperty1.getTime() - dtProperty2
						.getTime());

			} else if (objProperty1 instanceof String
					&& objProperty2 instanceof String) {
				String strProperty1 = objProperty1 + "";
				String strProperty2 = objProperty2 + "";
				returnValue = (int) (strProperty1.compareTo(strProperty2));

			}

		} catch (Exception exception) {
			logger.warn("Erro ao tentar comparar os objetos '" + objProperty1
					+ "', '" + objProperty2 + "'!", exception);
			returnValue = 0;
		}

		return returnValue;

	}

	/**
	 * Compara a propriedade informada no construtor
	 */
	public int compare(Object arg1, Object arg2) {

		Object objProperty1;
		Object objProperty2;
		int returnValue = 0;

		try {

			/* descobre os dois properties */
			objProperty1 = BeanUtilsBean.getInstance().getPropertyUtils()
					.getProperty(arg1, property);
			objProperty2 = BeanUtilsBean.getInstance().getPropertyUtils()
					.getProperty(arg2, property);

			/* invoca o compare da propriedade */
			returnValue = compareProperty(objProperty1, objProperty2);

			/* inverte ordem */
			if (!asc) {
				returnValue = returnValue * -1;
			}

		} catch (IllegalAccessException e) {
			logger.warn("Erro ao tentar comparar a propriedade '" + property
					+ "' objetos dos '" + arg1 + "', '" + arg2 + "'!", e);
			return 0;
		} catch (InvocationTargetException e) {
			logger.warn("Erro ao tentar comparar a propriedade '" + property
					+ "' objetos dos '" + arg1 + "', '" + arg2 + "'!", e);
			return 0;
		} catch (NoSuchMethodException e) {
			logger.warn("Erro ao tentar comparar a propriedade '" + property
					+ "' objetos dos '" + arg1 + "', '" + arg2 + "'!", e);
			return 0;
		}

		return returnValue;
	}

	public boolean isAsc() {
		return asc;
	}

	public String getProperty() {
		return property;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}
