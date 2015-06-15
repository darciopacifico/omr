package com.msaf.validador.integration.util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe utilit�ria para manipular cole��es
 */
public abstract class CollectionsUtil {

	/**
	 * Cria um List a partir dos par�metros
	 *
	 * @param <T>
	 *            Tipo dos itens do Set
	 * @param items
	 *            Item que ser�o retornados no List
	 * @return List com os itens
	 */
	public static <T> List<T> makeList(final T... items) {
		if(items == null) return null;

		final List<T> result = new ArrayList<T>();
		for (final T item : items) {
			result.add(item);
		}
		return result;
	}

	/**
	 * Cria um Set a partir dos par�metros
	 *
	 * @param <T>
	 *            Tipo dos itens do Set
	 * @param items
	 *            Item que ser�o retornados no Set
	 * @return Set com os itens
	 */
	public static <T> Set<T> makeSet(final T... items) {
		final Set<T> result = new LinkedHashSet<T>();
		for (final T item : items) {
			result.add(item);
		}
		return result;
	}

	/**
	 * Faz um split no valor com a regex especificada e retorna uma cole��o de
	 * strings
	 *
	 * @param value
	 * @param regex
	 * @return
	 */
	public static Set<String> splitToSet(final String value, final String regex) {
		final String[] items = value.split(regex);
		final Set<String> result = new LinkedHashSet<String>();
		for (final String item : items) {
			result.add(item);
		}
		return result;
	}

	public static <C, T> List<T> filter(IFilter<T> filter, List<T> collection) {
		if(filter == null) throw new IllegalArgumentException("Argumento filter n�o pode ser nulo.");
		if(collection == null) return null;

		List<T> retorno = new ArrayList<T>();
		for (T t : collection) {
			if(filter.aceita(t)) {
				retorno.add(t);
			}
		}

		return retorno;
	}

	@SuppressWarnings("unchecked")
	public static Set<?> getAsSet(List<?> lista, String propriedade) {
		if(lista == null) return Collections.emptySet();

		Set retorno = new HashSet();

		for (int indice = 0; indice < lista.size(); indice++) {
			Object o = lista.get(indice);
			retorno.add(ReflectionUtil.getValue(o, propriedade));
		}

		return retorno;
	}

	public static <V> Map<String, V> listToMap(List<V> lista, String propriedadeChave) {
		if(lista == null) return Collections.emptyMap();

		Map<String, V> retorno = new HashMap<String, V>();
		for (V v : lista) {
			retorno.put("" + ReflectionUtil.getValue(v, propriedadeChave), v);
		}

		return retorno;
	}

	public static <T> void sort(List<T> lista, final Comparator<T> comparator) {
		Collections.sort(lista, comparator);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Recebe lista que ser� ordenada pela propriedade de cada objeto dentro da lista
	 */
	public static void sort(List<?> lista, final String propriedade) {
		if(lista == null) throw new IllegalArgumentException("Argumento lista n�o pode ser nulo.");
		if(Util.isEmpty(propriedade)) throw new IllegalArgumentException("Argumento lista n�o pode ser nulo/vazio.");

		sort(lista, new Comparator() {
			public int compare(Object o1, Object o2) {
				if(o1 == o2) return 0;
				if(o1 == null) return 1;
				if(o2 == null) return -1;

				Object value1 = ReflectionUtil.getValue(o1, propriedade);
				Object value2 = ReflectionUtil.getValue(o2, propriedade);

				if(value1 == value2) return 0;
				if(value1 == null) return 1;
				if(value2 == null) return -1;

				if(value1 instanceof Comparable) return ((Comparable)value1).compareTo(value2);

				throw new IllegalArgumentException("Tipo da propriedade n�o implementa Comparable");
			}
		});
	}

	public static <T> List<T> paginar(List<T> lista, Paginacao paginacao) {
		if(paginacao == null) return lista;

		Integer inicio = paginacao.getInicio();
		int limite = inicio + paginacao.getLimite();
		limite = Math.min(limite, lista.size());

		if(inicio == 0 && limite >=  lista.size()) return lista;

		return lista.subList(inicio, limite);
	}

	public static Object[] paginar(Object[] array, Paginacao paginacao) {
		if(paginacao == null) return array;

		Integer inicio = paginacao.getInicio();
		int limite = inicio + paginacao.getLimite();
		limite = Math.min(limite, array.length);

		if(inicio == 0 && limite >=  array.length) return array;

		Object[] retorno = new Object[limite - inicio];
		int indice = 0;
		for (int i = inicio; i < limite; i++) {
			retorno[indice] = array[i];
			indice++;
		}

		return retorno;
	}


	/**
	 * Devolve o primeiro item da Cole��o. Caso seja um Set n�o ordenado, o resultado deste m�todo � imprevis�vel.
	 */
	public static <T> T getFirst(Collection<T> collection) {
		if(Util.isEmpty(collection)) return null;

		return collection.iterator().next();
	}

	public static void setProperty(Collection<?> collection, String propriedade, Object valor) {
		if(!Util.isEmpty(collection)) {
			for (Object object : collection) {
				ReflectionUtil.setProperty(object, propriedade, valor);
			}
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * Recebe lista de par�metros sendo considerados consecutivamente CHAVE/VALOR - CHAVE/VALOR...
	 * Lan�a IllegalArgumentException caso o n�mero de par�metros for �mpar.
	 */
	public static Map<?, ?> makeMap(Object... object) {
		if(object == null || object.length == 0) return new HashMap();
		if(MathUtil.isOdd(object.length)) throw new IllegalArgumentException("N�mero de argumentos deve ser par.");

		Map retorno = new LinkedHashMap();
		for (int i = 0; i < object.length; i+=2)
			retorno.put(object[i], object[i+1]);

		return retorno;
	}
}
