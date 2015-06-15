package br.com.dlp.framework.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import br.com.dlp.framework.pk.IPK;
import br.com.dlp.framework.vo.IBaseVO;
import br.com.dlp.framework.vo.ICadastroBaseVO;
import br.com.dlp.framework.vowrapper.IVOWrapper;

public class AcheCollectionUtils implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5061394835147529565L;

	private AcheCollectionUtils() {
	};

	/**
	 * REMOVE UM OBJETO DE UMA COLLECTION, CASO equals SEJA SATISFEITO. A
	 * IMPLEMENTA ÇÃO DE remove() DA MAIORIA DAS COLLEÇÕES NÃO UTILIZA equals()
	 */
	public static boolean remove(Collection collection, Object obj) {
		boolean returnValue = false;

		if (collection == null || collection.size() <= 0 || obj == null) {
			returnValue = false;
		}

		Iterator iterator = collection.iterator();
		while (iterator.hasNext()) {
			Object objDaVez = iterator.next();
			if (objDaVez != null && objDaVez.equals(obj)) {
				if (collection.remove(objDaVez)) {
					/*
					 * DEVO SAIR IMEDIATAMENTE, POIS SE DAR MAIS UM next() POSSO
					 * RECEBER UM java.util.ConcurrentModificationException
					 */
					return true;
				}
			}
		}

		return returnValue;
	}

	public static boolean contains(Collection collection, Object obj) {
		if (collection == null || collection.size() <= 0 || obj == null) {
			return false;
		}

		Iterator iterator = collection.iterator();
		while (iterator.hasNext()) {
			Object objDaVez = iterator.next();
			if (objDaVez != null && objDaVez.equals(obj)) {
				return true;
			}
		}

		return false;
	}

	public static boolean contains(Collection collection, ICadastroBaseVO object) {

		/* se a collection for nula ou de tamanho zero */
		if (collection == null || collection.size() == 0) {
			return false;
		}

		/* se for nulo nem perco tempo */
		if (object == null) {
			return false;
		}

		Iterator iterator = collection.iterator();

		/* itera todos os elementos */
		while (iterator.hasNext()) {
			Object davez = iterator.next();

			if (davez instanceof ICadastroBaseVO) {
				ICadastroBaseVO baseVODaVez = (ICadastroBaseVO) davez;

				if (baseVODaVez.equals(object)
						&& baseVODaVez.getClass().getName().equals(
								object.getClass().getName())) {

					collection.remove(davez);

					/***********************************************************
					 * não vou dar return aqui, pq pode ser q exista mais de uma
					 * referencia
					 */
					return true;
				}

			}
		}

		return false;
	}

	public static boolean remove(Collection collection, ICadastroBaseVO object) {

		boolean returnValue = false;

		/* se a collection for nula ou de tamanho zero */
		if (collection == null || collection.size() == 0) {
			return false;
		}

		/* se for nulo nem perco tempo */
		if (object == null) {
			return false;
		}

		Iterator iterator = collection.iterator();

		/* itera todos os elementos */
		while (iterator.hasNext()) {
			Object davez = iterator.next();

			if (davez instanceof ICadastroBaseVO) {
				ICadastroBaseVO baseVODaVez = (ICadastroBaseVO) davez;

				if (baseVODaVez.equals(object)
						&& baseVODaVez.getClass().getName().equals(
								object.getClass().getName())) {

					collection.remove(davez);

					/***********************************************************
					 * não vou dar return aqui, pq pode ser q exista mais de uma
					 * referencia
					 */
					returnValue = true;
				}

			}
		}
		return returnValue;
	}

	public static ICadastroBaseVO getBaseVOByIPK(Collection collection, IPK ipk) {

		/* testo se a coleção é vazia ou de tamanho zero */
		if (collection == null || collection.size() == 0) {
			return null;
		}

		Iterator iterator = collection.iterator();
		while (iterator.hasNext()) {
			Object objDaVez = iterator.next();

			if (objDaVez != null && (objDaVez instanceof ICadastroBaseVO)) {

				ICadastroBaseVO baseVODaVez = (ICadastroBaseVO) objDaVez;

				if (baseVODaVez.getIPK().compare(ipk)) {
					return baseVODaVez;
				}
			}
		}

		return null;
	}

	public static IBaseVO getBaseVO(List list, int indexOf) {
		IBaseVO baseVO = null;
		if (list != null && indexOf > -1) {

			baseVO = (IBaseVO) list.get(indexOf);
		}
		return baseVO;
	}

	/**
	 * Retorna o indexOf pela implementação que deveria ser obvia, com equals()
	 */
	public static int indexOf(List list, Object aPesquisar) {

		if (list == null || aPesquisar == null) {
			return -1;
		}

		Iterator iterator = list.iterator();

		int returnValue = -1;

		while (iterator.hasNext()) {
			Object contidoNaCollection = iterator.next();

			if (aPesquisar.equals(contidoNaCollection)) {
				returnValue = list.indexOf(contidoNaCollection);
				break;
			}
		}

		return returnValue;

	}

	public static boolean remove(Collection collection, Collection itensAExcluir) {
		boolean excluiuAlgo = false;

		if (collection == null || itensAExcluir == null
				|| collection.size() == 0 || itensAExcluir.size() == 0) {
			excluiuAlgo = false;
			return excluiuAlgo;
		}

		Iterator iterator = itensAExcluir.iterator();
		while (iterator.hasNext()) {
			Object aExcluir = iterator.next();
			if (remove(collection, aExcluir)) {
				excluiuAlgo = true;
			}
		}

		return excluiuAlgo;
	}

	/**
	 * Remove da colecao de wrappers os wrappers que encapsulam os baseVOs
	 * informados
	 */
	public static void removeWrappers(Collection wrappers, Collection baseVOs) {
		Iterator iteratorBaseVOs = baseVOs.iterator();
		while (iteratorBaseVOs.hasNext()) {
			IBaseVO baseVO = (IBaseVO) iteratorBaseVOs.next();
			removeWrapper(wrappers, baseVO);
		}
	}

	/**
	 * Remove da colecao de wrappers os wrappers contidos em wrappersRemover
	 */
	public static void removeWrappers(Collection wrappers, List wrappersRemover) {
		wrappers.removeAll(wrappersRemover);
	}

	/**
	 * Remove da colecao de wrappers o wrapper que encapsula baseVO
	 */
	public static boolean removeWrapper(Collection wrappers, IBaseVO baseVO) {
		Iterator iteratorWrappers = wrappers.iterator();
		while (iteratorWrappers.hasNext()) {
			IVOWrapper wrapper = (IVOWrapper) iteratorWrappers.next();
			if (wrapper.getBaseVO() == baseVO) {
				wrappers.remove(wrapper);
				return true;
			}
		}

		return false;
	}

	/**
	 * Remove da colecao de wrappers o wrapper informado caso este esteja
	 * contido na colecao
	 */
	public static boolean removeWrapper(Collection wrappers,
			IVOWrapper wrapperRemover) {
		Iterator iteratorWrappers = wrappers.iterator();
		while (iteratorWrappers.hasNext()) {
			IVOWrapper wrapper = (IVOWrapper) iteratorWrappers.next();
			if (wrapper == wrapperRemover) {
				wrappers.remove(wrapperRemover);
				return true;
			}
		}

		return false;
	}

}
