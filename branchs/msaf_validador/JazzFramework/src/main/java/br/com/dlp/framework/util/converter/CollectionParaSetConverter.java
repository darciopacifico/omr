package br.com.dlp.framework.util.converter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

public class CollectionParaSetConverter extends AbstractConverter {
	/**
	 * Este Converter não precisa de internacionalização
	 */
	public CollectionParaSetConverter(ResourceBundle resourceBundle) {
		super(resourceBundle);
	}

	public Object convert(Class setter, Object getter) {

		if (getter instanceof Set) {
			return getter;
		}

		if (getter == null) {
			return getter;
		}

		Collection collection = (Collection) getter;

		Set set = new HashSet();

		Iterator iterator = collection.iterator();

		while (iterator.hasNext()) {
			set.add(iterator.next());
		}

		return set;
	}

}
