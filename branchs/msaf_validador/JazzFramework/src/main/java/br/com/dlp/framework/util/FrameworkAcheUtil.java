package br.com.dlp.framework.util;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;

import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.util.comparator.CollectionBeanSortComparator;
import br.com.dlp.framework.util.converter.CollectionParaListConverter;
import br.com.dlp.framework.util.converter.CollectionParaSetConverter;
import br.com.dlp.framework.util.converter.QualquerObjetoParaString;
import br.com.dlp.framework.util.converter.StringParaDataConverter;
import br.com.dlp.framework.util.converter.StringParaNumberConverter;

/**
 * Servicos utilitarios do FrameworkAche
 */
public class FrameworkAcheUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8515801790377769227L;

	private FrameworkAcheUtil() {
	}

	/**
	 * Ordena e recupera o primeiro item da coleção, conforme property e sentido
	 * da ordenação informados
	 */
	public static Object getPrimeiroItem(List list, String property, boolean asc)
			throws FrameworkAcheUtilError {

		Object object = null;

		try {
			if (list != null && list.size() > 0) {
				Comparator comparator = new CollectionBeanSortComparator(
						property, asc);
				Collections.sort(list, comparator);
				object = list.get(0);
			}

		} catch (Throwable t) {
			throw new FrameworkAcheUtilError(
					"Erro ao tentar recuperar o primeiro item da colecao!", t);

		}

		return object;

	}

	/**
	 * Invoca método de um objeto qualquer
	 */
	public static Object invocaMetodo(Object parent, String methodName,
			Class[] argumentTypes, Object[] argumentValues)
			throws FrameworkAcheUtilError {
		Object returnValue;

		try {

			Method method = parent.getClass().getMethod(methodName,
					argumentTypes);
			returnValue = invocaMetodo(parent, method, argumentTypes,
					argumentValues);

		} catch (SecurityException e) {
			throw new FrameworkAcheUtilError(
					"Erro ao tentar invocar reflexivamente o método '"
							+ methodName + "' do objeto " + parent + "!", e);

		} catch (NoSuchMethodException e) {
			throw new FrameworkAcheUtilError(
					"Erro ao tentar invocar reflexivamente o método '"
							+ methodName + "' do objeto " + parent + "!", e);

		} catch (IllegalArgumentException e) {
			throw new FrameworkAcheUtilError(
					"Erro ao tentar invocar reflexivamente o método '"
							+ methodName + "' do objeto " + parent + "!", e);

		}

		return returnValue;
	}

	/**
	 * Invoca método de um objeto qualquer
	 */
	public static Object invocaMetodo(Object parent, Method method,
			Class[] argumentTypes, Object[] argumentValues)
			throws FrameworkAcheUtilError {
		Object returnValue;

		try {

			returnValue = method.invoke(parent, argumentValues);

		} catch (SecurityException e) {
			throw new FrameworkAcheUtilError(
					"Erro ao tentar invocar reflexivamente o método '" + method
							+ "' do objeto " + parent + "!", e);

		} catch (IllegalArgumentException e) {
			throw new FrameworkAcheUtilError(
					"Erro ao tentar invocar reflexivamente o método '" + method
							+ "' do objeto " + parent + "!", e);

		} catch (IllegalAccessException e) {
			throw new FrameworkAcheUtilError(
					"Erro ao tentar invocar reflexivamente o método '" + method
							+ "' do objeto " + parent + "!", e);

		} catch (InvocationTargetException e) {
			throw new FrameworkAcheUtilError(
					"Erro ao tentar invocar reflexivamente o método '" + method
							+ "' do objeto " + parent + "!", e);

		}

		return returnValue;
	}

	/**
	 * Método utilitário para instanciar objetos a partir da classe
	 */
	public static Object instanciaObjeto(Class class1, Class[] parameterTypes,
			Object[] initargs) throws FrameworkAcheUtilError {
		Object instanciado;

		try {

			Constructor constructor = class1.getConstructor(parameterTypes);
			instanciado = constructor.newInstance(initargs);

		} catch (SecurityException e) {
			throw new FrameworkAcheUtilError(
					"Erro ao tentar instanciar um objeto da classe '" + class1
							+ "' com parameterTypes=" + parameterTypes
							+ " e initargs=" + initargs, e);

		} catch (NoSuchMethodException e) {
			throw new FrameworkAcheUtilError(
					"Erro ao tentar instanciar um objeto da classe '" + class1
							+ "' com parameterTypes=" + parameterTypes
							+ " e initargs=" + initargs, e);

		} catch (IllegalArgumentException e) {
			throw new FrameworkAcheUtilError(
					"Erro ao tentar instanciar um objeto da classe '" + class1
							+ "' com parameterTypes=" + parameterTypes
							+ " e initargs=" + initargs, e);

		} catch (InstantiationException e) {
			throw new FrameworkAcheUtilError(
					"Erro ao tentar instanciar um objeto da classe '" + class1
							+ "' com parameterTypes=" + parameterTypes
							+ " e initargs=" + initargs, e);

		} catch (IllegalAccessException e) {
			throw new FrameworkAcheUtilError(
					"Erro ao tentar instanciar um objeto da classe '" + class1
							+ "' com parameterTypes=" + parameterTypes
							+ " e initargs=" + initargs, e);

		} catch (InvocationTargetException e) {
			throw new FrameworkAcheUtilError(
					"Erro ao tentar instanciar um objeto da classe '" + class1
							+ "' com parameterTypes=" + parameterTypes
							+ " e initargs=" + initargs, e);

		}

		return instanciado;
	}

	/**
	 * 
	 */
	public static String repeatString(String string, long vezes) {
		StringBuffer stringBuffer = new StringBuffer();

		for (int i = 0; i < vezes; i++) {
			stringBuffer.append(string);
		}

		return stringBuffer.toString();
	}

	/**
	 * Método utilitário para instanciar objetos a partir do NOME DA classe ou
	 * nome da classe
	 */
	public static Object instanciaObjeto(String classQualifiedName,
			Class[] parameterTypes, Object[] initargs)
			throws FrameworkAcheUtilError {
		Class class1;

		try {
			class1 = Class.forName(classQualifiedName);
		} catch (ClassNotFoundException e) {
			throw new FrameworkAcheUtilError(
					"Erro ao tentar instanciar um objeto da classe '"
							+ classQualifiedName + "' com parameterTypes="
							+ parameterTypes + " e initargs=" + initargs, e);
		}

		return instanciaObjeto(class1, parameterTypes, initargs);
	}

	/**
	 * Cria e devolve instancias de ObjectCloner <br>
	 * Futuramente posso implementar como Singleton ou Flyweight
	 */
	public static ObjectCloner getObjectClonerFactoryMethod() {
		ObjectCloner objectCloner = new ObjectCloner();
		return objectCloner;
	}

	/**
	 * Metodo utilitario de uso geral para testar 'nulo', 'vazio' ou 'zero'
	 */
	public static boolean isNullOrEmptyOrZero(Object object) {

		if (object == null) {
			return true;

		} else if (object instanceof Number) {
			return ((Number) object).doubleValue() == 0;

		} else if (object instanceof String) {
			return ((String) object).equals("");

		}

		return false;

	}

	public static void configureConvertUtilsBean(
			ConvertUtilsBean convertUtilsBean, ResourceBundle resourceBundle) {

		convertUtilsBean.register(new QualquerObjetoParaString(resourceBundle),
				String.class);
		convertUtilsBean.register(new StringParaDataConverter(resourceBundle),
				Date.class);
		convertUtilsBean.register(
				new StringParaNumberConverter(resourceBundle), Double.class);
		convertUtilsBean.register(
				new StringParaNumberConverter(resourceBundle), Float.class);
		convertUtilsBean.register(
				new StringParaNumberConverter(resourceBundle), Integer.class);
		convertUtilsBean.register(
				new StringParaNumberConverter(resourceBundle), Long.class);
		convertUtilsBean.register(
				new StringParaNumberConverter(resourceBundle), Short.class);
		convertUtilsBean.register(
				new CollectionParaSetConverter(resourceBundle), Set.class);
		convertUtilsBean.register(new CollectionParaListConverter(
				resourceBundle), List.class);

	}

	/**
	 * Converte a strDate de um formato para outro
	 * 
	 * @param strDate
	 * @param formatoDe
	 * @param formatoPara
	 * @return
	 * @throws BaseTechnicalError
	 */
	public static String formatDate(String strDate, String formatoDe,
			String formatoPara) throws BaseTechnicalError {

		SimpleDateFormat sdfDe = new SimpleDateFormat(formatoDe);
		SimpleDateFormat sdfPara = new SimpleDateFormat(formatoPara);

		Date dtDe;
		String returnValue;

		try {
			dtDe = sdfDe.parse(strDate);
		} catch (ParseException e) {
			throw new BaseTechnicalError("Não foi possível converter a string "
					+ strDate + " para Data no formato " + formatoDe, e);
		}

		returnValue = sdfPara.format(dtDe);

		return returnValue;
	}

	/**
	 * RETORNA true CASO A STRING SEJA NULA OU VAZIA
	 */
	public static boolean isNullOrEmpty(Object object) {

		boolean returnValue = false;

		/* INDEPENDENTE DO TIPO DA INFORMAÇÃO, SE FOR NULL EU JA RETORNO TRUE */
		if (object == null) {
			return true;
		}

		if (object instanceof String) {
			returnValue = (((String) object).trim().equals(""));
		} else {
			/* SE NÃO FOR NENHUM TIPO ESPERADO, NÃO VOU VALIDAR */
			return true;
		}

		return returnValue;
	}

	/**
	 * Recupera um array de classes a partir de um array de objetos
	 */
	public static Class[] getClasses(Object[] objects) {
		if (objects == null) {
			return null;
		}

		Class[] classes = new Class[objects.length];
		for (int i = 0; i < objects.length; i++) {
			Object object = objects[i];
			if (object == null) {
				classes[i] = null;
			} else {
				classes[i] = object.getClass();

			}

		}
		return classes;
	}

	public static void resetaAtributosBoleanos(Object object)
			throws BaseTechnicalError {

		if (object == null) {
			return;
		}

		try {

			Map props = BeanUtilsBean.getInstance().describe(object);

			Iterator iterator = props.keySet().iterator();
			while (iterator.hasNext()) {

				Object key = iterator.next();
				Object value = props.get(key);
				if (value instanceof Boolean) {
					BeanUtilsBean.getInstance().setProperty(object, "" + key,
							new Boolean(false));
				}
			}

			System.out.println(props.values());

		} catch (IllegalAccessException e) {
			throw new BaseTechnicalError("Erro ao tentar descrever Objeto!", e);
		} catch (InvocationTargetException e) {
			throw new BaseTechnicalError("Erro ao tentar descrever Objeto!", e);
		} catch (NoSuchMethodException e) {
			throw new BaseTechnicalError("Erro ao tentar descrever Objeto!", e);
		}

	}

}
