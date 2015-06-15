/*
 * Data de Criacao 05/06/2005 20:49:28
 * 
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.codegenerator.consulta.struts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

	protected Log logger = LogFactory.getLog(this.getClass());

	private static final String TABLE_ABRE = "\n" + tabs(3)
			+ "<table cellspacing='0' cellpadding='0'>\n" + tabs(4) + "";

	private static final String TR_LABELS_ABRE = "\n" + tabs(3) + "<tr>";

	private static final String TD_LABELS_ABRE = "\n" + tabs(4)
			+ "<td class='tt2c'>";

	private static final String TD_LABELS_DICA = "\n" + tabs(4)
			+ "<td class='tt2c' ";

	private static final String TD_LABELS_FECHA = "</td>";

	private static final String TR_LABELS_FECHA = "\n" + tabs(3) + "</tr>\n"
			+ tabs(3) + "";

	private static final String TR_CONTROLS_ABRE = "\n" + tabs(3) + "<tr>";

	private static final String TD_CONTROLS_ABRE = "\n" + tabs(4)
			+ "<td class='tt2l'>";

	private static final String TD_CONTROLS_FECHA = "</td>";

	private static final String TR_CONTROLS_FECHA = "\n" + tabs(3) + "</tr>\n"
			+ tabs(3) + "";

	private static final String TABLE_FECHA = "\n" + tabs(3) + "</table>";

	public String getTag() {
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

		logger.debug("gerando TrOfLabels");

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
				+ voWrapperProperty + "." + collection + "\">\n" + tabs(3)
				+ "<logic:iterate id=\"" + id
				+ "\" name=\"<%=Constants.BEAN_KEY%>\" property=\""
				+ voWrapperProperty + "." + collection + "\">";
	}

	protected String generateFechaForCondicional() throws Wj2eeException {
		return "</logic:iterate>\n" + tabs(3) + "</logic:notEmpty>";

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
		indexedLabels.append(TD_LABELS_ABRE);
		indexedLabels.append("&nbsp;");
		indexedLabels.append(TD_LABELS_FECHA);

		while (iterator.hasNext()) {
			XMethod method = (XMethod) iterator.next();
			String indexedControl = generateIndexedLabel(method);

			indexedLabels.append(TD_LABELS_DICA);

			indexedLabels.append(" title=\"<bean:message key='"
					+ TagHandlerUtil.dicaKeyProperty(method) + "' />\" ");
			indexedLabels.append(" >");

			indexedLabels.append(indexedControl);
			indexedLabels.append(TD_LABELS_FECHA);
		}

		return indexedLabels.toString();
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

		List list = classOfProperty.getMethods(predicate, true);

		list = new ArrayList(list);

		Collections.sort(list, comparator);

		// esta cópia dele mesmo é necessário, pois a List original não suporta
		// ordenação
		Iterator iterator = list.iterator();

		/* esta é a td que conteria o botao X para exclusao no cadastro */
		String tdBotaoX = "&nbsp;";
		indexedControls.append(TD_CONTROLS_ABRE);
		indexedControls.append(tdBotaoX);
		indexedControls.append(TD_CONTROLS_FECHA);

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
		AbstractStrutsCodeGenerator strutsCodeGenerator = (AbstractStrutsCodeGenerator) /*
																						 * forcando
																						 * para
																						 * ser
																						 * sempre
																						 * text
																						 */
		codeGeneratorFactory.getCodeGenerator(method,
				TagHandlerUtil.TIPO_CONTROLE_CONSULTA,
				TagHandlerUtil.RENDERER_TEXT, javaDoc);

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

		/* atribuindo valores aos atributos da tag do struts */
		strutsCodeGenerator.setIndexed("true");
		strutsCodeGenerator.setName(itenOfCollectionName);
		strutsCodeGenerator.setTipoControle(getTipoControle());
		strutsCodeGenerator.setProperty("indexado");

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

	public String getProperty() throws Wj2eeException {
		return "indexed";
	}

	protected Predicate getPredicate() {
		return new MetodosParaIndexacaoPredicate();
	}

}