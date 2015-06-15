/*
 * Created on 04/07/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.dlp.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtilsBean;

/**
 * Clona Objetos utilizando Stream (DeepCopy)
 */
public class ObjectCloner implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5708865435352661599L;

	/* PROPERTY UTILS PARA USO GERAL */
	protected ObjectCloner() {
	}

	protected PropertyUtilsBean getPropertyUtilsBean() {
		return new PropertyUtilsBean();
	}

	protected String getFirstNestedProperty(String nestedProperty) {
		int pointByte = nestedProperty.indexOf(".");

		String returnValue;

		if (pointByte == -1) {
			returnValue = nestedProperty;
		} else {
			returnValue = nestedProperty.substring(0, pointByte);
		}

		return returnValue;
	}

	protected String getRestNestedProperty(String nestedProperty) {
		int pointByte = nestedProperty.indexOf(".");

		String returnValue;

		if (pointByte == -1) {
			returnValue = "";
		} else {
			returnValue = nestedProperty.substring(pointByte + 1);
		}

		return returnValue;
	}

	public void cloneNestedProperty(Object parentObj, String nestedProperty)
			throws ObjectClonerException {
		String firstNestedProperty = getFirstNestedProperty(nestedProperty);
		String restNestedProperty = getRestNestedProperty(nestedProperty);
		try {
			PropertyUtilsBean propertyUtilsBean = getPropertyUtilsBean();

			if (restNestedProperty == null || restNestedProperty.equals("")) {
				/** *********************** */
				/* achei quem devo clonar */
				/** *********************** */

				/* RECUPERANDO O OBJETO A SER CLONADO */
				Object oldObj = propertyUtilsBean.getNestedProperty(parentObj,
						nestedProperty);

				/* FAZENDO O CLONE COM deepCopy */
				Object newObj = deepCopy(oldObj);

				/* ATRIBUINDO O OBJETO CLONADO NO LUGAR DO OBJETO ORIGINAL */
				propertyUtilsBean.setNestedProperty(parentObj, nestedProperty,
						newObj);

			} else {
				/*
				 * ainda não ache a propriedade que devo clonar, vou pegar a
				 * próxima
				 */
				parentObj = propertyUtilsBean.getNestedProperty(parentObj,
						firstNestedProperty);

				if (parentObj instanceof Collection) {
					cloneNestedProperty(((Collection) parentObj),
							restNestedProperty);

				} else {
					cloneNestedProperty(parentObj, restNestedProperty);

				}
			}
		} catch (IllegalAccessException e) {
			throw new ObjectClonerException(e);

		} catch (InvocationTargetException e) {
			throw new ObjectClonerException(e);

		} catch (NoSuchMethodException e) {
			throw new ObjectClonerException(e);

		}

	}

	protected void cloneNestedProperty(Collection collection,
			String nestedProperty) throws ObjectClonerException {
		Iterator iterator = collection.iterator();

		while (iterator.hasNext()) {
			Object object = iterator.next();

			cloneNestedProperty(object, nestedProperty);

		}
	}

	public Object deepCopy(Object oldObj) throws ObjectClonerException {
		ObjectOutputStream oos;
		ObjectInputStream ois;
		ByteArrayOutputStream bos;

		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);

			oos.writeObject(oldObj);
			oos.flush();
			oos.close();

			ByteArrayInputStream bin = new ByteArrayInputStream(bos
					.toByteArray());
			ois = new ObjectInputStream(bin);

			Object newObj = ois.readObject();
			ois.close();

			return newObj;

		} catch (IOException e) {
			throw new ObjectClonerException(
					"Erro ao tentar clonar (serializar e deserializar) o objeto="
							+ oldObj, e);

		} catch (ClassNotFoundException e) {
			throw new ObjectClonerException(
					"Erro ao tentar clonar (serializar e deserializar) o objeto="
							+ oldObj, e);

		}
	}
}