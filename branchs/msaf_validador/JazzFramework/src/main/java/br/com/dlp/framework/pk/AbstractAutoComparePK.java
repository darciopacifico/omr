package br.com.dlp.framework.pk;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * Esta classe abstrata implementa o método compare() de IPK, nesta
 * implementação, o compare() busca e compara autimaticamente os atributos da
 * PK.<br>
 * Esta classe deve ser utilizada com muito critério, consulte o arquitetor e
 * certifique-se de que implementação de PK a ser escrita pode partir deste
 * nível de abstração
 */
public abstract class AbstractAutoComparePK extends AbstractPK {
	/**
	 * Implementação automática de compare
	 */
	public final boolean compare(IPK pk) {
		boolean returnValue;

		if (pk == null) {
			return false;
		}

		returnValue = comparePKType(pk);

		/* se forem do mesmo tipo, mando comparar os valores dos atributos */
		if (returnValue) {
			try {
				returnValue = comparePKAtributes(pk);
			} catch (AutoComparePKException e) {
				return false;
			}
		}

		return returnValue;
	}

	/**
	 * Compara os atributos da PK informada no parâmetro com this
	 */
	protected boolean comparePKAtributes(IPK pk) throws AutoComparePKException {
		boolean returnValue = true;

		Map map;
		try {
			map = PropertyUtils.describe(pk);

		} catch (IllegalAccessException e) {
			throw new AutoComparePKException(
					"Erro ao tentar descrever objeto para comparacao automatica de atributos!",
					e);

		} catch (InvocationTargetException e) {
			throw new AutoComparePKException(
					"Erro ao tentar descrever objeto para comparacao automatica de atributos!",
					e);

		} catch (NoSuchMethodException e) {
			throw new AutoComparePKException(
					"Erro ao tentar descrever objeto para comparacao automatica de atributos!",
					e);

		}

		Set keySet = map.keySet();

		/* filtra quais campos devem entrar na analise da PK */
		Predicate predicate = getPredicateFilter(pk);
		List nomeCampos = new ArrayList(CollectionUtils.select(keySet,
				predicate));

		/*
		 * organiza os campos a serem comparador considerando a facilidade em
		 * comparacao do tipo
		 */
		Comparator comparator = getComparatorPrecedencia(pk);
		Collections.sort(nomeCampos, comparator);

		/* itera os campos pela ordem de precedencia */
		Iterator iterator = nomeCampos.iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			Object object = map.get(key);

			/* invoca a comparacao do atributo atual */
			boolean compareAtributoAtual = comparePKAtribute(object, key);

			/* se um único atributo não for igual, já era, retorna false */
			if (!compareAtributoAtual) {
				returnValue = false;
				break;
			}
		}

		return returnValue;
	}

	/**
	 * Retorna o comparator de determinará a precedencia da análise dos campos
	 * da PK
	 */
	protected Comparator getComparatorPrecedencia(IPK pk) {
		PrecedenciaCamposPKComparator comparator = new PrecedenciaCamposPKComparator(
				pk);
		return comparator;
	}

	/**
	 * Retorna o Predicate que ira decidir quais campos deverão entrar na
	 * analise da PK
	 * 
	 * @param pk
	 */
	protected Predicate getPredicateFilter(IPK pk) {
		Predicate predicate = new CamposPKPredicate();
		return predicate;
	}

	/**
	 * Compara o valor especificado com a propriedade deste objeto
	 */
	protected boolean comparePKAtribute(Object value, String property)
			throws AutoComparePKException {
		Object localValue;

		try {
			PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance()
					.getPropertyUtils();
			localValue = propertyUtilsBean.getProperty(this, property);
		} catch (IllegalAccessException e) {
			throw new AutoComparePKException(
					"Erro ao tentar recuperar o valor da propriedade "
							+ property + " deste objeto!", e);

		} catch (InvocationTargetException e) {
			throw new AutoComparePKException(
					"Erro ao tentar recuperar o valor da propriedade "
							+ property + " deste objeto!", e);

		} catch (NoSuchMethodException e) {
			throw new AutoComparePKException(
					"Erro ao tentar recuperar o valor da propriedade "
							+ property + " deste objeto!", e);

		}

		if (value == null && localValue == null) {
			/* os dois valores são null */
			boolean resultForNullCompare = resultForNullCompare();
			return resultForNullCompare;

		} else if (value != null && localValue != null) {
			/* os dois valores NAO são null */
			return value.equals(localValue);

		} else/* if(um ou o outro valor é null) */{
			return false;
		}

	}

	/**
	 * Avalia se, ao comparar null com null, deve-se retornar true ou false
	 */
	protected boolean resultForNullCompare() throws AutoComparePKException {
		return true;
	}

}
