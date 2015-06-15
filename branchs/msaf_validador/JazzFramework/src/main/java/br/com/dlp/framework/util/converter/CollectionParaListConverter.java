package br.com.dlp.framework.util.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

/**
 * TRANSFERE OS VOS DE UMA COLECAO QUALQUER PARA UMA IMPLEMENTACAO DE List
 * (ArrayList)<br>
 * Normalmente List é utilizado na parte view (Struts) e Set na parte Model
 * (Hibernate)
 */
public class CollectionParaListConverter extends AbstractConverter {

	/**
	 * Este Converter não precisa de internacionalização
	 */
	public CollectionParaListConverter(ResourceBundle resourceBundle) {
		super(resourceBundle);
	}

	public Object convert(Class setter, Object getter) {

		if (getter instanceof List) {
			return getter;
		}

		if (getter == null) {
			return getter;
		}

		Collection collection = (Collection) getter;
		List list = new ArrayList();

		/* FAZ A TRANSFERENCIA */
		list.addAll(collection);

		return list;
	}

}
