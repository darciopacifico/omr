package com.msaf.validador.integration.util;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.msaf.validador.consultaonline.exceptions.PropriedadeInexistenteException;

public class ReflectionUtil {
	private static final IllegalArgumentException ILLEGAL_ARGUMENT_EXCEPTION_CLASSE = new IllegalArgumentException("Argumento classe não pode ser nulo");
	private static Map<Class<?>, ReflectionCache> cache = new HashMap<Class<?>, ReflectionCache>();
	/**
	 * Utiliza o Getter relativo à propriedade
	 * @param origem
	 * @param propriedade
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object getValue(Object origem, String propriedade) {
		if(origem == null) throw new IllegalArgumentException("Argumento origem não pode ser nulo");
		if(propriedade == null) throw new IllegalArgumentException("Argumento propriedade não pode ser nulo");
		if(propriedade.trim().length() == 0) throw new IllegalArgumentException("Argumento propriedade não pode ser vazio");
		
		if(propriedade.indexOf(".") != -1) {
			String newProperty = getPropertyPart(propriedade);
			Object newValue = getValue(origem, newProperty);
			if(newValue == null) return null;
			
			String remainingProperties = Util.right(propriedade, propriedade.length() - newProperty.length() - 1);
			return getValue(newValue, remainingProperties);
		} else {
			if(origem instanceof Map) {
				return ((Map)origem).get(propriedade);
			} else {
				Method method = findGetterByProperty(origem.getClass(), propriedade);
				
				if(method == null) throw new PropriedadeInexistenteException(MessageFormat.format("Propriedade inexistente: {0} na classe {1}.", propriedade, origem.getClass().getName()));
				
				return getGetterValue(origem, method);
			}
		}
	}

	private static String getPropertyPart(String propriedade) {
		String newProperty = Util.getPartBefore(propriedade, ".");
		return newProperty;
	}

	private static Object getGetterValue(Object origem, Method method) {
		Object retorno = null;
		try {
			retorno = method.invoke(origem, new Object[0]);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return retorno;
	}

	public static Method findGetterByProperty(Class<?> klass, String propriedade) {
		if(propriedade == null || propriedade.trim().length() == 0) throw new IllegalArgumentException("Argumento propriedade não pode ser vazio/nulo");
		validateClassArgument(klass);
		
		String getterName = propertyToGetter(propriedade);

		return getMethodByName(klass, getterName);
	}

	private static void validateClassArgument(Class<?> klass) {
		if(klass == null) throw ILLEGAL_ARGUMENT_EXCEPTION_CLASSE;
	}

	public static Method getMethodByName(Class<?> klass, String methodName, Class<?>[] parameters) {
		validateClassArgument(klass);
		if(methodName == null || methodName.trim().length() == 0) throw new IllegalArgumentException("Argumento Nome do Método não pode ser nulo/vazio");
		
		ReflectionCache reflectionCache = getReflectionCache(klass);
		Method method = reflectionCache.getMethodByName(methodName);
		
		if(method != null) return method;
		
		try {
			method = klass.getDeclaredMethod(methodName, parameters);
			reflectionCache.setMethodByName(methodName, method);
		} catch (SecurityException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (NoSuchMethodException e) {
			method = findAmongAllMethods(klass, methodName);
		}
		
		return method;
	}
	
	private static Method findAmongAllMethods(Class<?> klass, String methodName) {
		Method[] methods = klass.getDeclaredMethods();
		
		if(methods != null) {
			for (int indice = 0; indice < methods.length; indice++) {
				if(methodName.equals(methods[indice].getName())) {
					getReflectionCache(klass).setMethodByName(methodName, methods[indice]);
					return methods[indice];
				}
			}
		}
		return null;
	}

	public static Method getMethodByName(Class<?> klass, String methodName) {
		return getMethodByName(klass, methodName, new Class[0]);
	}

	private static ReflectionCache getReflectionCache(Class<?> klass) {
		ReflectionCache refCache = cache.get(klass);
		if(refCache == null) {
			refCache = new ReflectionCache();
			cache.put(klass, refCache);
		}
		
		return refCache;
	}
	
	private static String propertyToGetter(String property) {
		return "get" + Util.camelize(property);
	}
	
	private static String getterToProperty(Method getter) {
		String methodName = getter.getName();
		return Util.decamelize(Util.right(methodName, methodName.length() - 3));
	}
	
	private static String propertyToSetter(String property) {
		return "set" + Util.camelize(property);
	}
	
	private static class ReflectionCache {
		private Map<String, Method> metodos;
		private List<Method> getters;
		
		public Method getMethodByName(String name) {
			return metodos == null?null:metodos.get(name);
		}
		
		public void setMethodByName(String name, Method method) {
			if(metodos == null) metodos = new HashMap<String, Method>();
			
			metodos.put(name, method);
		}
		
		public List<Method> getGetters() {
			return getters;
		}

		public void setGetters(List<Method> getters) {
			this.getters = getters;
		}
	}

	public static Object newInstance(Class<?> entity) {
		if(entity == null) throw ILLEGAL_ARGUMENT_EXCEPTION_CLASSE;
		
		Object retorno = null;
		
		try {
			retorno = entity.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Classe deve ter construtor padrão");
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Classe deve ter construtor padrão");
		}
		
		return retorno;
	}

	public static void setProperty(Object destino, String propriedade, Object valor) {
		if(destino == null) throw new IllegalArgumentException("Objeto destino não pode ser nulo.");
		if(Util.isEmpty(propriedade)) throw new IllegalArgumentException("Propriedade não pode ser nula/vazia.");
		
		if(propriedade.indexOf(".") != -1) {
			String newProperty = Util.getPartAfter(propriedade, ".");
			String remainingProperties = Util.left(propriedade, propriedade.length() - newProperty.length() - 1);
			
			destino = getValue(destino, remainingProperties);
			propriedade = newProperty;
			
			if(destino == null) return;
		}
		
		String methodName = propertyToSetter(propriedade);
		
		Method method = getMethodByName(destino.getClass(), methodName);
		if(method == null) throw new IllegalArgumentException(MessageFormat.format("Propriedade ({0}) inexistente.", propriedade));
		
		try {
			Class<?> type = getSetterParameterType(method);
			method.invoke(destino, convertValue(valor, type));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private static Class<?> getSetterParameterType(Method method) {
		if(method == null) throw new IllegalArgumentException("Metodo não pode ser nulo");
		if(!method.getName().startsWith("set")) throw new IllegalArgumentException("Método não é um setter");
		
		Class<?>[] types = method.getParameterTypes();
		if(types == null || types.length != 1) throw new IllegalArgumentException("Setter deve ter 1 parâmetro");
		
		return types[0];
	}

	public static Method findSetterByProperty(Class<?> klass, String propriedade) {
		if(Util.isEmpty(propriedade)) throw new IllegalArgumentException("Argumento propriedade não pode ser vazio/nulo");
		validateClassArgument(klass);
		
		String setterName = propertyToSetter(propriedade);

		return getMethodByName(klass, setterName);
	}

	public static Object convertValue(Object o, Class<?> type) {
		validateClassArgument(type);
		if(o == null) return null;
		
		if(type == Long.class || type == long.class) {
			if(o instanceof Number) return ((Number)o).longValue();
			if(o instanceof String) return MathUtil.parseLong((String)o);
		} else if (type == Integer.class || type == int.class) {
			if(o instanceof Number) return ((Number)o).intValue();
			if(o instanceof String) return MathUtil.parseInteger((String)o);
		}
		
		return o;
	}
	
	public static void copyProperties(Object origem, Object destino) {
		copyProperties(origem, destino, new String[0]);
	}

	public static void copyProperties(Object origem, Object destino, String...excludeList) {
		if(origem == null) throw new IllegalArgumentException("Argumento origem não pode ser nulo");
		if(destino == null) throw new IllegalArgumentException("Argumento destino não pode ser nulo");
		
		Set<String> excludedProperties = new HashSet<String>();
		
		if(excludeList != null && excludeList.length > 0)
			excludedProperties.addAll(Arrays.asList(excludeList));
		
		List<Method> getters = findGetters(origem.getClass());
		Object value = null;
		String propriedade = null;
		
		if(!Util.isEmpty(getters)) {
			for (Method method : getters) {
				propriedade = getterToProperty(method);
				
				if(excludedProperties.contains(propriedade)) continue;
				
				value = invoke(origem, method);
				setProperty(destino, propriedade, value);
			}
		}
	}
	
	private static Object invoke(Object o, Method method) {
		try {
			return method.invoke(o);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static List<Method> findGetters(Class<? extends Object> klass) {
		if(klass == null) throw new IllegalArgumentException("Argumento classe não pode ser nulo");
		ReflectionCache reflectionCache = getReflectionCache(klass);
		List<Method> retorno = reflectionCache.getGetters();
		
		if(retorno != null) return retorno;
		
		retorno = findMethods(klass, new IFilter<Method>() {
			public boolean aceita(Method metodo) {
				if(!metodo.getName().startsWith("get")) return false;
				if(metodo.getParameterTypes().length > 0) return false;
				if(Modifier.isNative(metodo.getModifiers())) return false;
				
				return true;
			}
		});
		
		reflectionCache.setGetters(retorno);
		return retorno;
	}

	private static List<Method> findMethods(Class<? extends Object> klass, IFilter<Method> filter) {
		if(klass == null) throw new IllegalArgumentException("Argumento classe não pode ser nulo");
		List<Method> retorno = null;
		
		Method[] metodos = klass.getMethods();
		if(metodos == null || metodos.length == 0) return retorno;
		
		for (int i = 0; i < metodos.length; i++) {
			if(!filter.aceita(metodos[i])) continue;
			
			if(retorno == null) retorno = new ArrayList<Method>();
			retorno.add(metodos[i]);
		}
		
		return retorno;
	}

	public static List<Method> findGetters(Object object) {
		if(object == null) throw new IllegalArgumentException("Argumento objeto não pode ser nulo");
		
		return findGetters(object.getClass());
	}

	@SuppressWarnings("unchecked")
	public static List<String> findGettersProperties(Object o) {
		if(o == null) return null;
		
		if(o instanceof Map) {
			List<String> retorno = new ArrayList<String>();
			
			Map map = (Map)o;
			Set chaves = map.keySet();
			Iterator iterator = chaves.iterator();
			while (iterator.hasNext()) {
				Object object = (Object) iterator.next();
				retorno.add(Util.toString(object));
			}
			
			return retorno;
		} else
			return findGettersProperties(o.getClass());
	}
	
	public static List<String> findGettersProperties(Class<?> klass) {
		if(klass == null) throw new IllegalArgumentException("Argumento classe não pode ser nulo");
		
		List<Method> getters = findGetters(klass);
		if(getters == null) return null;
		
		List<String> retorno = new ArrayList<String>();
		
		for (Method method : getters) {
			retorno.add(getterToProperty(method));
		}
		
		return retorno;
	}
}
