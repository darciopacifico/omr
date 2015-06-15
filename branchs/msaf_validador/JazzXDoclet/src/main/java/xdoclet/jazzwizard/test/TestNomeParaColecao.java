/*
 * Data de Criacao 11/06/2005 15:16:37
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.test;

import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;

/**
 * Sr(a). Darcio L Pacifico comente aqui a utilidade desta classe.
 * 
 * @author Darcio L Pacifico - 11/06/2005 15:16:37
 */
public class TestNomeParaColecao {

	public static void main(String[] args) throws Wj2eeException {

		String nome = "br.com.dlp.wizard.endereco.EnderecoCasaVO";

		nome = TagHandlerUtil.getNomeDaColecaoNoSingular(nome);

		System.out.println(nome);

	}
}
