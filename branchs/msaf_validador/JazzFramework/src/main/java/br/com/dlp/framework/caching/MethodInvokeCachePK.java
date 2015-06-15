package br.com.dlp.framework.caching;

import br.com.dlp.framework.pk.IPK;

/**
 * Abstracao da chave para o vo MethodCacheVO
 */
public class MethodInvokeCachePK extends AbstractCachePK {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Object[] objectsChave;

	private Class[] classesChave;

	public MethodInvokeCachePK() {
	}

	public MethodInvokeCachePK(Object[] objectsChave, Class[] classesChave) {
		super();
		this.objectsChave = objectsChave;
		this.classesChave = classesChave;
	}

	public boolean comparePKValues(IPK pk) {
		boolean returValue = true;
		MethodInvokeCachePK pkParametro = (MethodInvokeCachePK) pk;

		/* separando arrays da chave parametro (IPK pk) */
		Object[] objectsChaveParametro = pkParametro.objectsChave;
		Class[] classesChaveParametro = pkParametro.classesChave;

		/*
		 * checando a quantidade de parametros desta chave e da chave parametro
		 */
		if ((classesChaveParametro.length != classesChave.length)
				|| (objectsChaveParametro.length != objectsChave.length)) {
			return false;
		}

		/* Comparando todos os objetos da chave */
		for (int index = 0; index < pkParametro.objectsChave.length; index++) {
			Object objetoParametro = pkParametro.objectsChave[index];
			Object objetoLocal = objectsChave[index];

			Class classParametro = pkParametro.classesChave[index];
			Class classLocal = classesChave[index];

			if (classLocal.isAssignableFrom(classParametro)) {
				/* os parametro sao do mesmo tipo, vou comparar os valores */

				if (objetoLocal != null && objetoParametro != null) {
					/* compara com equals */
					if (!objetoLocal.equals(objetoParametro)) {
						returValue = false;
						break;
					}

				} else if (objetoLocal == null ^ objetoParametro == null) {
					/* exclusivamente um ou o outro é nulo, entao é false */
					returValue = false;
					break;

				}

			} else {
				/*
				 * este parametro (pkParametro.classesChave[index]) nao é do
				 * mesmo tipo ou filho do tipo do parametro
				 * (classesChave[index])
				 */
				returValue = false;
				break;

			}
		}

		return returValue;
	}

	public Class[] getClassesChave() {
		return classesChave;
	}

	public Object[] getObjectsChave() {
		return objectsChave;
	}

	public void setClassesChave(Class[] classesChave) {
		this.classesChave = classesChave;
	}

	public void setObjectsChave(Object[] objectsChave) {
		this.objectsChave = objectsChave;
	}
}
