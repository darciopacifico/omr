package br.com.dlp.framework.pk;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.dlp.framework.vo.IBaseVO;

/**
 * Organizar a ordem de precedAncia os atributs de chave a ser analizada, exemplo:<br>
 * 1 - Valores null<br>
 * 2 - Primitivos<br>
 * 3 - Abstracao de primitivos e String<br>
 * 4 - Outros VOs<br>
 */
public class PrecedenciaCamposPKComparator implements Comparator {
	private IPK ipk;

	protected Log logger = LogFactory.getLog(PrecedenciaCamposPKComparator.class);

	protected PrecedenciaCamposPKComparator(IPK ipk) {
		this.ipk = ipk;
	}

	public int compare(Object object1, Object object2) {
		String atribute1 = (String) object1;
		String atribute2 = (String) object2;

		Object value1;
		Object value2;
		try {
			PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
			value1 = propertyUtilsBean.getProperty(ipk, atribute1);
			value2 = propertyUtilsBean.getProperty(ipk, atribute2);
		} catch (IllegalAccessException e) {
			logger.error("Erro ao tentar estabelecer precedencia entre campos " + object1 + " e " + object2);
			return 0;

		} catch (InvocationTargetException e) {
			logger.error("Erro ao tentar estabelecer precedencia entre campos " + object1 + " e " + object2);
			return 0;

		} catch (NoSuchMethodException e) {
			logger.error("Erro ao tentar estabelecer precedencia entre campos " + object1 + " e " + object2);
			return 0;

		}

		Class class1 = value1.getClass();
		Class class2 = value2.getClass();

		int precedencia1 = precedenciaPorTipo(class1);
		int precedencia2 = precedenciaPorTipo(class2);

		return precedencia1 - precedencia2;
	}

	/**
	 * Determina qual a precedencia por tipo de objeto
	 */
	protected int precedenciaPorTipo(Class clazz) {
		if (Number.class.isAssignableFrom(clazz)) {
			return 1;

		} else if (String.class.isAssignableFrom(clazz)) {
			return 2;

		} else if (Date.class.isAssignableFrom(clazz)) {
			return 3;

		} else if (IPK.class.isAssignableFrom(clazz)) {
			return 4;

		} else if (IBaseVO.class.isAssignableFrom(clazz)) {
			return 5;

		} else {
			return 0;
		}

	}

}
