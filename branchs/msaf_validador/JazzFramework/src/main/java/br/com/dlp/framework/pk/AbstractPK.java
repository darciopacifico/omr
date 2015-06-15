/*
 * Created on 07/07/2005
 */
package br.com.dlp.framework.pk;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Abstracao de Primary key
 */
public abstract class AbstractPK implements IPK {
	public AbstractPK() {
	}

	public boolean equals(Object obj) {

		if (obj != null && obj instanceof IPK) {
			IPK pk = (IPK) obj;
			return compare(pk);
		}

		return false;
	}

	/**
	 * Compara se o tipo da pk a comparar é compatível com this
	 */
	protected boolean comparePKType(IPK pk) {
		boolean returnValue = pk.getClass().isAssignableFrom(this.getClass());
		return returnValue;
	}

	public int hashCode() {
		int hashCode = 0;
		Map map;

		try {

			map = PropertyUtils.describe(this);

			Collection collection = map.values();
			Iterator iterator = collection.iterator();

			while (iterator.hasNext()) {
				Object object = iterator.next();
				hashCode += object.hashCode();
			}

		} catch (IllegalAccessException e) {
			throw new RuntimeException("Erro ao tentar processar o HashCode ",
					e);

		} catch (InvocationTargetException e) {
			throw new RuntimeException("Erro ao tentar processar o HashCode ",
					e);

		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Erro ao tentar processar o HashCode ",
					e);

		}

		return hashCode;

	}

}
