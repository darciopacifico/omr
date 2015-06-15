/*
 * Data de Criacao 05/06/2005 20:49:28
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.cadastro.struts;

import java.util.Collection;

import org.apache.commons.collections.Predicate;

import xdoclet.XDocletException;
import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.codegenerator.CodeGeneratorFactory;
import xdoclet.jazzwizard.codegenerator.base.AbstractStrutsCodeGenerator;
import xdoclet.jazzwizard.codegenerator.predicate.CamposModuloMestrePredicate;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;
import xjavadoc.XMethod;

/**
 * Gera uma chamada à tag html:text do struts
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:49:28
 */
public class LinkParaModuloEscravoCodeGenerator extends
		StrutsIndexedCodeGenerator {
	/**
	 * Override de getPredicate para retornar o Predicate (filtro) dos metos
	 * listaveis
	 */
	protected Predicate getPredicate() {
		return new CamposModuloMestrePredicate();
	}

	protected String generateIndexedControl(XMethod method,
			String itenOfCollectionName) throws Wj2eeException {

		CodeGeneratorFactory codeGeneratorFactory = CodeGeneratorFactory
				.getInstance();

		AbstractStrutsCodeGenerator strutsCodeGenerator = (AbstractStrutsCodeGenerator) codeGeneratorFactory
				.getCodeGenerator(method,
						TagHandlerUtil.TIPO_CONTROLE_CONSULTA, javaDoc);
		strutsCodeGenerator
				.setTipoControle(TagHandlerUtil.TIPO_CONTROLE_INDEXADO);

		Collection defaultAttributes = strutsCodeGenerator
				.getDefaultAttributes();

		/* recuperando o nome da propriedade */
		String property = method.getPropertyName();

		/*
		 * DEFININDO QUAIS OS ATRRIBUTOS QUE SERÃO GERADOS (O PADRÃO É APENAS
		 * PROPERTY, STYLECLASS ETC...)
		 */
		defaultAttributes.add(AbstractStrutsCodeGenerator.ATT_PROPERTY);
		defaultAttributes.add(AbstractStrutsCodeGenerator.ATT_NAME);
		defaultAttributes.add(AbstractStrutsCodeGenerator.ATT_FORMAT_KEY);

		defaultAttributes.remove(AbstractStrutsCodeGenerator.ATT_INDEXED);
		defaultAttributes.remove(AbstractStrutsCodeGenerator.ATT_DISABLED);

		/**/
		defaultAttributes.add(AbstractStrutsCodeGenerator.ATT_DISABLED);

		/* atribuindo valores aos atributos da tag do struts */
		strutsCodeGenerator.setIndexed("true");
		strutsCodeGenerator.setName(itenOfCollectionName);
		strutsCodeGenerator.setProperty(property);

		String code = strutsCodeGenerator.generateCode();

		return code;
	}

	protected String botaoIncluirNovoItem(XMethod method) throws Wj2eeException {
		String strBotaoIncluirNovoItem = super.botaoIncluirNovoItem(method);

		String celulaBranca = TD_LABELS_ABRE + "&nbsp;" + TD_LABELS_FECHA;
		strBotaoIncluirNovoItem += celulaBranca + celulaBranca;

		return strBotaoIncluirNovoItem;
	}

	/**
	 * Gera TDs de botoes para listagem de subItens (ex: listagem de itens de
	 * pedido da tela de cadastro de pedido)
	 */
	protected String generateTdBotoes(XMethod method,
			String itenOfCollectionName) throws XDocletException,
			Wj2eeException {
		String propertyNoSingular = TagHandlerUtil.toSingular(method
				.getPropertyName());

		String strServicoRemoverItem = TagHandlerUtil.servicoRemoverItem(
				method, javaDoc);
		String strServicoEditarItem = TagHandlerUtil.servicoEditarItem(method,
				javaDoc);
		String strServicoConsultarItem = TagHandlerUtil.servicoConsultarItem(
				method, javaDoc);

		/* DESCOBRE QUAIS SÃO AS CONSTANTES PARA OS SEGUINTES SERVICOS */
		String classeEConstanteServicoRemover = TagHandlerUtil
				.classeEConstanteParaServico(method, strServicoRemoverItem,
						javaDoc);
		String classeEConstanteServicoEditar = TagHandlerUtil
				.classeEConstanteParaServico(method, strServicoEditarItem,
						javaDoc);
		String classeEConstanteServicoConsultar = TagHandlerUtil
				.classeEConstanteParaServico(method, strServicoConsultarItem,
						javaDoc);

		/* ARRAY DE BOTOES */
		String[] botoes = new String[] {
				"<html:image indexed='true' property='"
						+ propertyNoSingular
						+ "' src='../assets/img_e.gif' onclick='<%=\"marcarItem(this); invocaServico('\"+"
						+ classeEConstanteServicoEditar
						+ "+\"') ; return false;\"%>' />",
				"<html:image indexed='true' property='"
						+ propertyNoSingular
						+ "' src='../assets/img_x.gif' onclick='<%=\"marcarItem(this); invocaServico('\"+"
						+ classeEConstanteServicoRemover
						+ "+\"') ; return false;\"%>' />",
				"<html:image indexed='true' property='"
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
		String completeTd = "\n\t\t\t\t<%-- INICIO - MECANISMO DE EXCLUSAO, EDICAO, CONSULTA --%>"
				+ "\n\t\t\t\t<html:hidden name='"
				+ propertyNoSingular
				+ "' property='selecionado' indexed='true' value='false'/>"
				+ new String(tdsBotoes)
				+ "\n\t\t\t\t\n\t\t\t\t<%-- FIM - MECANISMO DE EXCLUSAO, EDICAO, CONSULTA --%> ";

		return completeTd;
	}
}
