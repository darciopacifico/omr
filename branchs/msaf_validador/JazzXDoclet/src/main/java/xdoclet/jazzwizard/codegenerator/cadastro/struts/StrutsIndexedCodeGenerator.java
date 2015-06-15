/*
 * Data de Criacao 05/06/2005 20:49:28
 * 
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.cadastro.struts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Predicate;

import xdoclet.XDocletException;
import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.codegenerator.CodeGeneratorFactory;
import xdoclet.jazzwizard.codegenerator.Wj2eeXMethodComparator;
import xdoclet.jazzwizard.codegenerator.base.AbstractStrutsCodeGenerator;
import xdoclet.jazzwizard.codegenerator.predicate.MetodosParaIndexacaoPredicate;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;
import xjavadoc.XClass;
import xjavadoc.XMethod;

/**
 * Gera uma chamada à tag html:text do struts
 * 
 * @author Darcio L Pacifico - 05/06/2005 20:49:28
 */
public class StrutsIndexedCodeGenerator extends AbstractStrutsCodeGenerator {

	protected static final String PROPERTY_ID = "id";

	protected static final String TABLE_ABRE = "\n" + tabs(3)
			+ "<table cellspacing='0' cellpadding='0'>\n" + tabs(4) + "";

	protected static final String TR_LABELS_ABRE = "\n" + tabs(3) + "<tr>";

	protected static final String TD_LABELS_DICA = "\n" + tabs(4)
			+ "<td class='headerTabelaIndexada' nowrap='nowrap' ";

	protected static final String TD_LABELS_ABRE = "\n" + tabs(4)
			+ "<td class='headerTabelaIndexada' nowrap='nowrap' >";

	protected static final String TD_LABELS_FECHA = "</td>";

	protected static final String TR_LABELS_FECHA = "\n" + tabs(3) + "</tr>\n"
			+ tabs(3) + "";

	protected static final String TR_CONTROLS_ABRE = "\n" + tabs(3) + "<tr>";

	protected static final String TD_CONTROLS_ABRE = "\n" + tabs(4)
			+ "<td class='tabelaIndexada' nowrap=\"nowrap\" >\n\t\t\t\t\t";

	protected static final String TD_CONTROLS_FECHA = "\n\t\t\t\t</td>";

	protected static final String TR_CONTROLS_FECHA = "\n" + tabs(3)
			+ "</tr>\n" + tabs(3) + "";

	protected static final String TABLE_FECHA = "\n" + tabs(3) + "</table>";

	public String getTag() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Override do metodo generateCode da superclasse
	 * 
	 * @throws Wj2eeException
	 */
	public String generateCode() throws Wj2eeException {
		StringBuffer stringBuffer = new StringBuffer();

		/* ABRE TABLE DE CONTROLES INDEXADOS */
		stringBuffer.append(TABLE_ABRE);

		/* APENDA TR DE LABELS */
		stringBuffer.append(generateTrOfLabels());

		try {
			stringBuffer.append(generateTrOfControls());
		} catch (XDocletException e) {
			throw new Wj2eeException(e,
					"Não foi possivel gerar o TR (tag HTML) do controle!");
		}

		stringBuffer.append(TABLE_FECHA);
		/* FECHA TABLE DE CONTROLES INDEXADOS */

		return stringBuffer.toString();

	}

	protected String generateTrOfLabels() throws Wj2eeException {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(TR_LABELS_ABRE);

		stringBuffer.append(generateIndexedLabels());

		stringBuffer.append(TR_LABELS_FECHA);
		return stringBuffer.toString();
	}

	protected String generateAbreForCondicional(String itenOfCollectionName)
			throws XDocletException, Wj2eeException {
		XClass methodClass = TagHandlerUtil.classeDoMetodo(method, javaDoc);
		String entidade = TagHandlerUtil.entidadeDaClasse(methodClass);

		entidade = TagHandlerUtil.denifirVariavelParaTipo(entidade);

		String id = TagHandlerUtil.toSingular(entidade);
		String collection = TagHandlerUtil.toPlural(entidade);
		String voWrapperProperty = TagHandlerUtil
				.denifirVariavelParaTipo(TagHandlerUtil
						.voWrapperClassName(class1));

		return "<logic:notEmpty name=\"<%=Constants.BEAN_KEY%>\" property=\""
				+ voWrapperProperty + "." + collection + "\">\n"
				+ "\t\t\t<%index=0;%>\n" + tabs(3) + "<logic:iterate id=\""
				+ id + "\" name=\"<%=Constants.BEAN_KEY%>\" property=\""
				+ voWrapperProperty + "." + collection + "\">";
	}

	protected String generateFechaForCondicional() throws Wj2eeException {
		return "\n" + "\t\t\t<%index++;%></logic:iterate>\n" + tabs(3)
				+ "</logic:notEmpty>";

	}

	protected String generateTrOfControls() throws XDocletException,
			Wj2eeException {
		StringBuffer stringBuffer = new StringBuffer();

		String itemOfCollectionName = getNomeDaColecaoNoSingular();

		stringBuffer.append(generateAbreForCondicional(itemOfCollectionName));

		stringBuffer.append(TR_CONTROLS_ABRE);
		String indexedControls = generateIndexedControls();
		stringBuffer.append(indexedControls);
		stringBuffer.append(TR_CONTROLS_FECHA);

		stringBuffer.append(generateFechaForCondicional());

		return stringBuffer.toString();
	}

	/** Override do metodo generateCode da superclasse */
	protected String generateIndexedLabels() throws Wj2eeException {
		StringBuffer indexedLabels = new StringBuffer();

		XClass classOfProperty = getClassOfProperty();

		// filtro, vide classe Wj2eeXMethodPredicate
		Predicate predicate = getPredicate();

		// comparator para ordenação vide classe Wj2eeXMethodComparator
		Comparator comparator = new Wj2eeXMethodComparator();

		List list = new ArrayList(TagHandlerUtil
				.retiraMetodosIgnorados(classOfProperty.getMethods(predicate,
						true)));

		list = new ArrayList(list);

		Collections.sort(list, comparator);

		// esta cópia dele mesmo é necessário, pois a List original não suporta
		// ordenação
		Iterator iterator = list.iterator();

		// criando TD vazio para casar a posição com os TDs contendo botao 'X'

		String botaoIncluir = botaoIncluirNovoItem(method);
		indexedLabels.append(botaoIncluir);

		while (iterator.hasNext()) {
			XMethod method = (XMethod) iterator.next();
			String indexedLabel = generateIndexedLabel(method);

			indexedLabels.append(TD_LABELS_DICA);
			indexedLabels.append(" title=\"<bean:message key='"
					+ TagHandlerUtil.dicaKeyProperty(method) + "' />\" ");
			indexedLabels.append(" >");

			indexedLabels.append(indexedLabel);
			indexedLabels.append(TD_LABELS_FECHA);
		}

		return indexedLabels.toString();
	}

	protected String botaoIncluirNovoItem(XMethod method) throws Wj2eeException {
		String servicoIncluir = TagHandlerUtil.servicoIncluirItem(method,
				javaDoc);
		String classeEConstanteParaServico = TagHandlerUtil
				.classeEConstanteParaServico(method, servicoIncluir, javaDoc);

		String botaoIncluir = "<a href=\"javascript:invocaServico('<%="
				+ classeEConstanteParaServico + "%>')\">Add</a>";

		return TD_LABELS_ABRE + botaoIncluir + TD_LABELS_FECHA;
	}

	protected String generateTdBotoes(XMethod method,
			String itenOfCollectionName) throws XDocletException,
			Wj2eeException {

		String propertyNoSingular = TagHandlerUtil.toSingular(method
				.getPropertyName());
		String servico = TagHandlerUtil.servicoRemoverItem(method, javaDoc);
		String classeEConstanteParaServico = TagHandlerUtil
				.classeEConstanteParaServico(method, servico, javaDoc);

		String completeTd = "\n\t\t\t\t\t<%-- MECANISMO DE EXCLUSAO --%>"
				+ "\n\t\t\t\t\t<html:image indexed='true' property='"
				+ propertyNoSingular
				+ "' src='../assets/img_x.gif' onclick='<%=\"marcarItem(this); invocaServico('\"+"
				+ classeEConstanteParaServico + "+\"') ; return false;\"%>' />"
				+ "\n\t\t\t\t\t<html:hidden name='" + propertyNoSingular
				+ "' property='selecionado' indexed='true' />" + "\n\t\t\t\t";

		return TD_LABELS_ABRE + completeTd + TD_LABELS_FECHA;

	}

	/**
	 * Monta um bean:message com a key da label da propriedade
	 */
	protected String generateIndexedLabel(XMethod method) throws Wj2eeException {
		StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append("<bean:message key='");
		stringBuffer.append(TagHandlerUtil.labelKeyProperty(method));
		stringBuffer.append("'/>");

		return stringBuffer.toString();
	}

	/**
	 * Override do metodo generateCode da superclasse
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 */
	protected String generateIndexedControls() throws XDocletException,
			Wj2eeException {
		StringBuffer indexedControls = new StringBuffer();

		XClass classOfProperty = getClassOfProperty();
		String itemOfCollectionName = getNomeDaColecaoNoSingular();
		// filtro, vide classe Wj2eeXMethodPredicate
		Predicate predicate = getPredicate();

		// comparator para ordenação vide classe Wj2eeXMethodComparator
		Comparator comparator = new Wj2eeXMethodComparator();

		List list = new ArrayList(TagHandlerUtil
				.retiraMetodosIgnorados(classOfProperty.getMethods(predicate,
						true)));

		list = new ArrayList(list);

		Collections.sort(list, comparator);

		// esta cópia dele mesmo é necessário, pois a List original não suporta
		// ordenação
		Iterator iterator = list.iterator();

		/* gera a TD contendo o botão X para exclusao */
		String tdBotaoX = generateTdBotoes(method, itemOfCollectionName);
		indexedControls.append(tdBotaoX);

		while (iterator.hasNext()) {
			XMethod method = (XMethod) iterator.next();
			String indexedControl = generateIndexedControl(method,
					itemOfCollectionName);

			indexedControls.append(TD_CONTROLS_ABRE);
			indexedControls.append(indexedControl);
			indexedControls.append(TD_CONTROLS_FECHA);
		}

		return indexedControls.toString();
	}

	public String getTipoControle() {
		return TagHandlerUtil.TIPO_CONTROLE_INDEXADO;
	}

	protected String generateIndexedControl(XMethod method,
			String itenOfCollectionName) throws Wj2eeException {

		/* INSTANCIANDO UMA IMPLEMENTAÇÃO DE AbstractStrutsCodeGenerator */
		CodeGeneratorFactory codeGeneratorFactory = CodeGeneratorFactory
				.getInstance();
		AbstractStrutsCodeGenerator strutsCodeGenerator = (AbstractStrutsCodeGenerator) codeGeneratorFactory
				.getCodeGenerator(method, getTipoControle(), javaDoc);

		/*
		 * DEFININDO QUAIS OS ATRRIBUTOS QUE SERÃO GERADOS (O PADRÃO É APENAS
		 * PROPERTY, STYLECLASS ETC...)
		 */
		strutsCodeGenerator.getDefaultAttributes().add(
				AbstractStrutsCodeGenerator.ATT_PROPERTY);
		strutsCodeGenerator.getDefaultAttributes().add(
				AbstractStrutsCodeGenerator.ATT_NAME);
		strutsCodeGenerator.getDefaultAttributes().add(
				AbstractStrutsCodeGenerator.ATT_INDEXED);
		strutsCodeGenerator.getDefaultAttributes().add(
				AbstractStrutsCodeGenerator.ATT_DISABLED);

		/* recuperando o nome da propriedade */
		String property = method.getPropertyName();

		/* atribuindo valores aos atributos da tag do struts */
		strutsCodeGenerator.setIndexed("true");
		strutsCodeGenerator.setName(itenOfCollectionName);
		strutsCodeGenerator.setXJavaDoc(javaDoc);
		strutsCodeGenerator.setProperty(property);

		/* gerando o código */
		String generatedCoded = strutsCodeGenerator.generateCode();

		return generatedCoded;
	}

	/**
	 * Recupera o nome da colecao para construir metodos indexados
	 * 
	 * @throws Wj2eeException
	 */
	protected String getNomeDaColecaoNoSingular() throws Wj2eeException {
		String nomeDaClasse = this.atributosMetodoVO.getClasse();
		String nomeDaColecaoNoSingular = TagHandlerUtil
				.getNomeDaColecaoNoSingular(nomeDaClasse);
		return nomeDaColecaoNoSingular;
	}

	protected Predicate getPredicate() {
		return new MetodosParaIndexacaoPredicate();
	}

}