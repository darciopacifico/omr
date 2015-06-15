package br.com.dlp.framework.pk;

import org.apache.commons.collections.Predicate;

/**
 * Implementação de predicate para filtrar apenas os propertys da PK que devem
 * ser analisados.
 */
public class CamposPKPredicate implements Predicate {

	public boolean evaluate(Object nomeCampo) {

		if (nomeCampo == null || nomeCampo.equals("class")) {
			return false;

		} else {
			return true;

		}
	}
}
