/*
 * Data de Criacao 05/06/2005 20:49:28
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.consulta.struts;

import xdoclet.XDocletException;
import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;
import xjavadoc.XMethod;

/**
 * Gera uma chamada à tag html:text do struts
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:49:28
 */
public class LinkParaModuloEscravoCodeGenerator
		extends
		xdoclet.jazzwizard.codegenerator.cadastro.struts.LinkParaModuloEscravoCodeGenerator {

	/**
	 * Gera TDs de botoes para listagem de subItens (ex: listagem de itens de
	 * pedido da tela de cadastro de pedido)
	 */
	protected String generateTdBotoes(XMethod method,
			String itenOfCollectionName) throws XDocletException,
			Wj2eeException {
		String propertyNoSingular = TagHandlerUtil.toSingular(method
				.getPropertyName());

		String strServicoConsultarItem = TagHandlerUtil.servicoConsultarItem(
				method, javaDoc);

		/* DESCOBRE QUAIS SÃO AS CONSTANTES PARA OS SEGUINTES SERVICOS */
		String classeEConstanteServicoConsultar = TagHandlerUtil
				.classeEConstanteParaServico(method, strServicoConsultarItem,
						javaDoc);

		/* ARRAY DE BOTOES */
		String[] botoes = new String[] { "<html:image indexed='true' property='"
				+ propertyNoSingular
				+ "' src='../assets/img_c.gif' onclick='<%=\"marcarItem(this); invocaServico('\"+"
				+ classeEConstanteServicoConsultar
				+ "+\"') ; return false;\"%>' />" };

		/* ITERA E APENDA OS BOTOES DENTRO DAS TDS */
		StringBuffer tdsBotoes = new StringBuffer("");
		for (int i = 0; i < botoes.length; i++) {
			tdsBotoes.append("\n\t\t\t\t\t" + TD_CONTROLS_ABRE + botoes[i]
					+ TD_CONTROLS_FECHA);
		}

		/* CONCATENA A HIDDEN COM AS TDS DE BOTOES */
		String completeTd = "\n\t\t\t\t<%-- INICIO - MECANISMO DE CONSULTA --%>"
				+ "\n\t\t\t\t<html:hidden name='"
				+ propertyNoSingular
				+ "' property='selecionado' indexed='true' value='false'/>"
				+ new String(tdsBotoes)
				+ "\n\t\t\t\t\n\t\t\t\t<%-- FIM - MECANISMO DE CONSULTA --%> ";

		return completeTd;
	}

	protected String botaoIncluirNovoItem(XMethod method) throws Wj2eeException {
		String celulaBranca = TD_LABELS_ABRE + "&nbsp;" + TD_LABELS_FECHA;

		return celulaBranca;
	}

}
