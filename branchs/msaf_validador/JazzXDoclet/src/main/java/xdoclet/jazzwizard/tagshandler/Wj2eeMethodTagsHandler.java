/*
 * Data de Criacao 27/05/2005 21:13:21
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.tagshandler;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xdoclet.XDocletException;
import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.codegenerator.Wj2eeXMethodComparator;
import xdoclet.jazzwizard.tagvalue.AtributosMetodoVO;
import xdoclet.tagshandler.MethodTagsHandler;
import xdoclet.util.TypeConversionUtil;
import xjavadoc.Type;
import xjavadoc.XClass;
import xjavadoc.XConstructor;
import xjavadoc.XField;
import xjavadoc.XJavaDoc;
import xjavadoc.XMember;
import xjavadoc.XMethod;
import br.com.dlp.framework.util.FrameworkAcheUtil;

/**
 * Tags para geração de artefatos (.java, .xml, .jsp) a partir de templates
 * 'per-class' (ActionForm.java, Facade, DAO)
 * 
 * @author Darcio L Pacifico - 27/05/2005 21:13:21
 * 
 * @xdoclet.taghandler namespace = "Wj2eeMethod"
 */
public class Wj2eeMethodTagsHandler extends MethodTagsHandler {
	public static final String CONFIG_PARAM_CONSTANTCLASS = "constantClass";

	protected Log logger = LogFactory.getLog(Wj2eeXMethodComparator.class);

	private boolean primeiroItemIteracao;

	private String controlSufix;

	private static int indexId;

	public void generate(String arg0) throws XDocletException {

		try {
			super.generate(arg0);

		} catch (Throwable t) {
			try {
				/* escreve o stacktrace num String */
				CharArrayWriter charArrayWriter = new CharArrayWriter();
				TagHandlerUtil.writeException(charArrayWriter, t);
				String msgErro = new String(charArrayWriter.toCharArray());

				/* escreve a string no arquivo gerado */
				super.generate(msgErro);

				if (t instanceof XDocletException) {
					throw (XDocletException) t;
				}
			} catch (IOException e) {
				logger.error("Erro ao tentar tratar excecao!!");
			}

		}
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String pkClassName() throws Wj2eeException {
		return TagHandlerUtil.pkClassName(getCurrentMethodProperty());
	}

	/***************************************************************************
	 * Renderiza o conteúdo da de uma coluna da listagem da tela de pesquisa<br>
	 * Ex: Para struts "bean: write"
	 * 
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String conteudoColunaListagemTelaPesquisa() throws Wj2eeException {
		return TagHandlerUtil.conteudoColunaListagemTelaPesquisa(
				getCurrentMethod(), getXJavaDoc());
	}

	public String methodType(Properties arg0) throws XDocletException {
		XClass class1 = getCurrentMethod().getPropertyType().getType();
		return class1.getName();
	}

	/**
	 * @param sufixPesquisa
	 *            The sufixPesquisa to set.
	 */
	protected void setControlSufix(String controlSufix) {
		this.controlSufix = controlSufix;
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String constantClassName() throws Wj2eeException {
		String constantClass;

		AtributosMetodoVO atributosMetodoVO = new AtributosMetodoVO(
				getCurrentMethod());
		String sistema = atributosMetodoVO.getSistema();

		if (FrameworkAcheUtil.isNullOrEmpty(sistema)) {
			constantClass = (String) getDocletContext().getConfigParam(
					CONFIG_PARAM_CONSTANTCLASS);
		} else {
			constantClass = sistema + "Constants";
		}

		return constantClass;
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String actionClassName() throws Wj2eeException {

		XClass classeDoMetodo = TagHandlerUtil.classeDoMetodo(
				getCurrentMethod(), getXJavaDoc());

		return TagHandlerUtil.actionClassName(classeDoMetodo, getXJavaDoc());
	}

	public String actionFullClassName() throws Wj2eeException {

		XClass classeDoMetodo = TagHandlerUtil.classeDoMetodo(
				getCurrentMethod(), getXJavaDoc());

		return TagHandlerUtil
				.actionFullClassName(classeDoMetodo, getXJavaDoc());
	}

	/**
	 * @return Returns the sufixPesquisa.
	 */
	protected String getControlSufix() {
		return controlSufix;
	}

	/**
	 * Atribui á this.sufixPesquisa o valor do parâmetro sufixPesquisa
	 */
	public void generate(String template, String sufixo)
			throws XDocletException {
		setControlSufix(sufixo);
		super.generate(template);
	}

	/**
	 * @throws Wj2eeException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String forwardParaCadastroModuloMestre() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		String forward;
		try {
			forward = TagHandlerUtil.forwardParaCadastroModuloMestre(method,
					getXJavaDoc());
		} catch (Wj2eeException e) {
			TagHandlerUtil.trataExcecao(e);
			throw e;
		}
		return forward;

	}

	/**
	 * @throws Wj2eeException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String forwardParaConsultaModuloMestre() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		String forward;
		try {
			forward = TagHandlerUtil.forwardParaConsultaModuloMestre(method,
					getXJavaDoc());
		} catch (Wj2eeException e) {
			TagHandlerUtil.trataExcecao(e);
			throw e;
		}
		return forward;

	}

	/**
	 * @throws Wj2eeException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String forwardParaCadastro() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		String forward;
		try {
			forward = TagHandlerUtil.forwardParaCadastro(method, getXJavaDoc());
		} catch (Wj2eeException e) {
			TagHandlerUtil.trataExcecao(e);
			throw e;
		}
		return forward;

	}

	/**
	 * @throws Wj2eeException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public static String indexId() throws Wj2eeException {
		String returnValue = "" + indexId;
		return returnValue;
	}

	/**
	 * @throws Wj2eeException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public static void incrementIndexId() throws Wj2eeException {
		indexId++;
	}

	/**
	 * @throws Wj2eeException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String forwardParaConsulta() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		String forward;
		try {
			forward = TagHandlerUtil.forwardParaConsulta(method, getXJavaDoc());
		} catch (Wj2eeException e) {
			TagHandlerUtil.trataExcecao(e);
			throw e;
		}
		return forward;

	}

	/**
	 * Iterates over all methods of current class and evaluates the body of the
	 * tag for each method.
	 * 
	 * @param template
	 *            The body of the block tag
	 * @param attributes
	 *            The attributes of the template tag
	 * @exception XDocletException
	 *                Description of Exception
	 * @doc.tag type="block"
	 * @doc.param name="superclasses" optional="true" values="true,false"
	 *            description="If true then traverse superclasses also,
	 *            otherwise look up the tag in current concrete class only."
	 * @doc.param name="sort" optional="true" values="true,false"
	 *            description="If true then sort the methods list."
	 * @doc.param name="tipo" optional="true" description="Classe que terá seus
	 *            métodos iterados"
	 */
	public void forTodosOsMethodDaClasse(String template, Properties attributes)
			throws XDocletException {

		XClass preservaEstadoXClass = getCurrentClass();
		XMethod preservaEstadoXMethod = getCurrentMethod();

		String className = attributes
				.getProperty(TagHandlerUtil.ATTRIBUTE_TIPO);
		XClass class1 = getXJavaDoc().getXClass(className);

		logger.debug("iterando os metodos de " + className + ": classe criada:"
				+ class1);

		forAllMembers(template, attributes, FOR_METHOD, class1);

		setCurrentClass(preservaEstadoXClass);
		setCurrentMethod(preservaEstadoXMethod);

	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String fullMethodType() throws Wj2eeException {
		XClass class1 = TagHandlerUtil.classeDoMetodo(getCurrentMethod(),
				getXJavaDoc());
		return class1.getQualifiedName();
	}

	/**
	 * Override do metodo forAllMembers da superclasse<br>
	 * Para implementar um mecanismo de ordenação específico
	 */
	protected void forAllMembers(String template, Properties attributes,
			int forType) throws XDocletException {
		XClass class1 = getCurrentClass();
		forAllMembers(template, attributes, FOR_METHOD, class1);
	}

	/**
	 * Override do metodo forAllMembers da superclasse<br>
	 * Para implementar um mecanismo de ordenação específico
	 */
	protected void forAllMembers(String template, Properties attributes,
			int forType, XClass currentClass) throws XDocletException {

		boolean superclasses = TypeConversionUtil.stringToBoolean(attributes
				.getProperty("superclasses"), true);
		boolean sort = TypeConversionUtil.stringToBoolean(attributes
				.getProperty("sort"), true);

		if (currentClass == null) {
			throw new XDocletException("currentClass == null!!!");
		}

		Collection members = null;

		switch (forType) {
		case FOR_FIELD:
			members = currentClass.getFields(superclasses);
			break;
		case FOR_CONSTRUCTOR:
			members = currentClass.getConstructors();
			break;
		case FOR_METHOD:
			members = currentClass.getMethods(superclasses);
			break;
		default:
			throw new XDocletException("Bad type: " + forType);
		}

		members = new ArrayList(members);

		if (sort) {
			/* IF COLOCADO POR DARCIO PARA ORDENAR MÉTODOS DE FORMA ESPECÍFICA */
			if (forType == FOR_METHOD) {
				/* MINHA IMPLEMENTAÇÃO DE ORDENAÇÃO */
				members = TagHandlerUtil.sortMethods((List) members);
			} else {
				/* IMPLEMENTAÇÃO DE ORDENAÇÃO ANTIGA */
				// sort fields, but we should make a copy first, because members
				// is not a new copy, it's shared by all
				List sortedMembers = new ArrayList(members);
				members = sortedMembers;
			}
		}

		/*
		 * Darcio - 16/02/2005 Retira métodos que devem ser ignorados. Problema
		 * com relacionamento bidirecional de informacao<->informaçãoItem
		 */
		try {

			members = TagHandlerUtil.retiraMetodosIgnorados(members);
		} catch (Exception e) {
			throw new XDocletException(e,
					"Erro ao tentar retirar metodos com a flag ignorar=true");
		}

		primeiroItemIteracao = true;
		indexId = 0;
		for (Iterator j = members.iterator(); j.hasNext();) {
			XMember member = (XMember) j.next();

			switch (forType) {
			case FOR_FIELD:
				setCurrentField((XField) member);
				break;
			case FOR_CONSTRUCTOR:
				setCurrentConstructor((XConstructor) member);
				break;
			case FOR_METHOD:
				setCurrentMethod((XMethod) member);
				break;
			default:
				throw new XDocletException("Bad type: " + forType);
			}

			setCurrentClass(member.getContainingClass());
			generate(template);
		}
		setCurrentClass(currentClass);

	}

	/**
	 * Analisa se o metodo corrente é o primeiro item de uma iteracao
	 * 
	 * @throws XDocletException
	 * @doc.tag type="block"
	 */
	public void ifIsPrimeiroItemIteracao(String template)
			throws XDocletException {
		if (primeiroItemIteracao) {
			generate(template);
		}

		primeiroItemIteracao = false;
	}

	/**
	 * Analisa se o metodo corrente é o primeiro item de uma iteracao
	 * 
	 * @throws XDocletException
	 * @doc.tag type="block"
	 */
	public void ifIsNotPrimeiroItemIteracao(String template)
			throws XDocletException {
		if (!primeiroItemIteracao) {
			generate(template);
		}
		primeiroItemIteracao = false;
	}

	/**
	 * Analisa se o metodo corrente é somente leitura
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsNotSomenteLeitura(String template) throws Wj2eeException,
			XDocletException {
		XMethod method = getCurrentMethod();
		if (!TagHandlerUtil.ifIsSomenteLeitura(method)) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo corrente é consultavel
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsConsultavel(String template) throws Wj2eeException,
			XDocletException {
		XMethod method = getCurrentMethod();
		if (TagHandlerUtil.ifIsConsultavel(method)) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo corrente !!NÃO!! é consultavel
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsNotConsultavel(String template) throws Wj2eeException,
			XDocletException {
		XMethod method = getCurrentMethod();
		if (!TagHandlerUtil.ifIsConsultavel(method)) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo corrente é pesquisavel
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsPropriedadePesquisavel(String template)
			throws Wj2eeException, XDocletException {
		XMethod method = getCurrentMethod();
		if (TagHandlerUtil.ifIsPropriedadePesquisavel(method)) {
			AtributosMetodoVO atributosMetodoVO = new AtributosMetodoVO(method);
			String operadorPesquisa = atributosMetodoVO.getOperadorPesquisa();
			if (operadorPesquisa.equals(TagHandlerUtil.OPERADOR_RANGE_VALUE)) {
				generate(template, TagHandlerUtil.CONTROL_SUFIX_DE);
				generate(template, TagHandlerUtil.CONTROL_SUFIX_ATE);
			} else {
				generate(template, TagHandlerUtil.CONTROL_SUFIX_EM_BRANCO);
			}
		}
	}

	/**
	 * Analisa se o metodo corrente é pesquisavel
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsPropriedadeListavel(String template) throws Wj2eeException,
			XDocletException {
		XMethod method = getCurrentMethod();
		if (TagHandlerUtil.ifIsPropriedadeListavel(method)) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo é de preenchimento automático
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsAutoFulfilling(String template) throws Wj2eeException,
			XDocletException {
		XMethod method = getCurrentMethod();
		if (TagHandlerUtil.isAutoFulfilling(method)) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo corrente é ordenavel
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsPropriedadeOrdenavel(String template)
			throws Wj2eeException, XDocletException {
		XMethod method = getCurrentMethod();
		if (TagHandlerUtil.ifIsPropriedadeOrdenavel(method)) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo corrente NAO é ordenavel
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsNotPropriedadeOrdenavel(String template)
			throws Wj2eeException, XDocletException {
		XMethod method = getCurrentMethod();
		if (!TagHandlerUtil.ifIsPropriedadeOrdenavel(method)) {
			generate(template);
		}
	}

	public void ifIsSomenteLeitura(String template) throws Wj2eeException,
			XDocletException {
		XMethod method = getCurrentMethod();
		if (TagHandlerUtil.ifIsSomenteLeitura(method)) {
			generate(template);
		}
	}

	/**
	 * Testa se é necessário criar mecanismo de find + (combo|radio|checkbox)
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 */
	public void ifIsCriarMecanismoDeFind(String template, Properties attributes)
			throws Wj2eeException, XDocletException {
		String strRecursivamente = attributes
				.getProperty(TagHandlerUtil.ATTRIBUTE_RECURSIVAMENTE);

		String strPermiteRepetir = attributes
				.getProperty(TagHandlerUtil.ATTRIBUTE_PERMITE_REPETIR);

		boolean recursivamente = (strRecursivamente != null && strRecursivamente
				.equals("true"));
		boolean permiteRepeticaoRecursiva = (strPermiteRepetir != null && strPermiteRepetir
				.equals("true"));

		/* grava o estado atual */
		XClass preservaEstadoXClass = getCurrentClass();
		XMethod preservaEstadoXMethod = getCurrentMethod();

		/* processa recursivamente o método corrente */
		XMethod method = getCurrentMethod();

		Collection tipoRetornoMetodosVisitados = new ArrayList(0);

		criarMecanismoDeFind(method, getXJavaDoc(), template, recursivamente,
				permiteRepeticaoRecursiva, tipoRetornoMetodosVisitados);

		/* retorna o estado gravado */
		setCurrentClass(preservaEstadoXClass);
		setCurrentMethod(preservaEstadoXMethod);

	}

	/**
	 * Testa se é necessário criar mecanismo de find + (combo|radio|checkbox)
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 */
	public void ifIsCriarMecanismoDeFindUmParaMuitos(String template,
			Properties attributes) throws Wj2eeException, XDocletException {
		String strRecursivamente = attributes
				.getProperty(TagHandlerUtil.ATTRIBUTE_RECURSIVAMENTE);

		String strPermiteRepetir = attributes
				.getProperty(TagHandlerUtil.ATTRIBUTE_PERMITE_REPETIR);

		boolean recursivamente = (strRecursivamente != null && strRecursivamente
				.equals("true"));
		boolean permiteRepeticaoRecursiva = (strPermiteRepetir != null && strPermiteRepetir
				.equals("true"));

		/* grava o estado atual */
		XClass preservaEstadoXClass = getCurrentClass();
		XMethod preservaEstadoXMethod = getCurrentMethod();

		/* processa recursivamente o método corrente */
		XMethod method = getCurrentMethod();

		Collection tipoRetornoMetodosVisitados = new ArrayList(0);

		criarMecanismoDeFindUmParaMuitos(method, getXJavaDoc(), template,
				recursivamente, permiteRepeticaoRecursiva,
				tipoRetornoMetodosVisitados);

		/* retorna o estado gravado */
		setCurrentClass(preservaEstadoXClass);
		setCurrentMethod(preservaEstadoXMethod);

	}

	/**
	 * Itera recursivamente o método corrente verificando a necessidade de se
	 * criar um mecanismo de find
	 * 
	 * @param permiteRepeticaoRecursiva
	 * @throws XDocletException
	 * @throws Wj2eeException
	 */
	protected void criarMecanismoDeFindUmParaMuitos(XMethod method,
			XJavaDoc javaDoc, String template, boolean recursivamente,
			boolean permiteRepeticaoRecursiva,
			Collection tipoRetornoMetodosVisitados) throws XDocletException,
			Wj2eeException {

		/* teste se é um getter */
		if (method.isPropertyAccessor()) {

			if (TagHandlerUtil.ifIsCriarMecanismoDeFindUmParaMuitos(method,
					getXJavaDoc())) {

				// setCurrentClass(method.getContainingClass());
				setCurrentMethod(method);

				/* tipo do retorno do método */
				String tipoRetorno = method.getReturnType().getType().getName();

				/* verifico se já visitei o método */
				if (permiteRepeticaoRecursiva
						|| !tipoRetornoMetodosVisitados.contains(tipoRetorno)) {
					generate(template);
				} else {
					logger
							.debug("O método '"
									+ method.getName()
									+ "' da classe '"
									+ method.getContainingClass().getName()
									+ "' não será processado. Um outro método que possui o mesmo tipo de retorno "
									+ "já foi processado. Esta restricao tem o intuito de nao permitir a repeticao de codigo nas classes"
									+ "geradas. Caso deseje forcar a repeticao do processamento do metodo defina como \"true\" o"
									+ "atributo permitirRepeticao da tag ifIsCriarMecanismoDeFind ");
				}

				/*
				 * incremento minha coleção com o tipo do retorno do método
				 * visitado
				 */
				tipoRetornoMetodosVisitados.add(tipoRetorno);
			}

			if (recursivamente) {
				/* recupera PropertyType do método atual */
				XClass class1 = TagHandlerUtil.classeDoMetodo(method, javaDoc);
				List methods = class1.getMethods();

				/* filtra métodos que devem ser ignorados */
				methods = new ArrayList(TagHandlerUtil
						.retiraMetodosIgnorados(methods));

				/* ordena de acordo com atributo 'ordem' de @wj2ee */
				methods = TagHandlerUtil.sortMethods(methods);

				Iterator iterator = methods.iterator();
				while (iterator.hasNext()) {

					XMethod methodDaVez = (XMethod) iterator.next();

					criarMecanismoDeFindUmParaMuitos(methodDaVez, javaDoc,
							template, recursivamente,
							permiteRepeticaoRecursiva,
							tipoRetornoMetodosVisitados);

				}
			}
		}

	}

	/**
	 * Itera recursivamente o método corrente verificando a necessidade de se
	 * criar um mecanismo de find
	 * 
	 * @param permiteRepeticaoRecursiva
	 * @throws XDocletException
	 * @throws Wj2eeException
	 */
	protected void criarMecanismoDeFind(XMethod method, XJavaDoc javaDoc,
			String template, boolean recursivamente,
			boolean permiteRepeticaoRecursiva,
			Collection tipoRetornoMetodosVisitados) throws XDocletException,
			Wj2eeException {

		/* teste se é um getter */
		if (method.isPropertyAccessor()) {

			if (TagHandlerUtil.ifIsCriarMecanismoDeFind(method, getXJavaDoc())) {

				// setCurrentClass(method.getContainingClass());
				setCurrentMethod(method);

				/* tipo do retorno do método */
				String tipoRetorno = method.getReturnType().getType().getName();

				/* verifico se já visitei o método */
				if (permiteRepeticaoRecursiva
						|| !tipoRetornoMetodosVisitados.contains(tipoRetorno)) {
					generate(template);
				} else {
					logger
							.debug("O método '"
									+ method.getName()
									+ "' da classe '"
									+ method.getContainingClass().getName()
									+ "' não será processado. Um outro método que possui o mesmo tipo de retorno "
									+ "já foi processado. Esta restricao tem o intuito de nao permitir a repeticao de codigo nas classes"
									+ "geradas. Caso deseje forcar a repeticao do processamento do metodo defina como \"true\" o"
									+ "atributo permitirRepeticao da tag ifIsCriarMecanismoDeFind ");
				}

				/*
				 * incremento minha coleção com o tipo do retorno do método
				 * visitado
				 */
				tipoRetornoMetodosVisitados.add(tipoRetorno);
			}

			if (recursivamente) {
				/* recupera PropertyType do método atual */
				XClass class1 = TagHandlerUtil.classeDoMetodo(method, javaDoc);
				List methods = class1.getMethods();

				/* filtra métodos que devem ser ignorados */
				methods = new ArrayList(TagHandlerUtil
						.retiraMetodosIgnorados(methods));

				/* ordena de acordo com atributo 'ordem' de @wj2ee */
				methods = TagHandlerUtil.sortMethods(methods);

				Iterator iterator = methods.iterator();
				while (iterator.hasNext()) {

					XMethod methodDaVez = (XMethod) iterator.next();

					criarMecanismoDeFind(methodDaVez, javaDoc, template,
							recursivamente, permiteRepeticaoRecursiva,
							tipoRetornoMetodosVisitados);

				}
			}
		}

	}

	/**
	 * Testa se é necessário criar mecanismo de find virtual para módulos
	 * indexados/escravo/local
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 */
	public void ifIsCriarMecanismoDeFindVirtual(String template)
			throws Wj2eeException, XDocletException {
		XMethod method = getCurrentMethod();
		if (TagHandlerUtil.ifIsCriarMecanismoDeFindVirtual(method,
				getXJavaDoc())) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo corrente é um método para relacionamento entre
	 * entidades getter ou setter para Collection ou ICadastroBaseVO
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsMetodoParaRelacionamento(String template)
			throws Wj2eeException, XDocletException {
		XMethod method = getCurrentMethod();
		if (TagHandlerUtil.ifIsMetodoParaRelacionamento(method, getXJavaDoc())) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo corrente é um método para relacionamento entre
	 * entidades getter ou setter para Collection ou ICadastroBaseVO e avalia se
	 * sua cardinalidade é "MUITOS"
	 * 
	 * @doc.tag type="block"
	 */
	public void ifIsMetodoParaRelacionamentoMuitos(String template)
			throws XDocletException {
		XMethod method = getCurrentMethod();
		if (TagHandlerUtil.ifIsMetodoParaRelacionamentoMuitos(method)) {
			generate(template);
		}
	}

	public void ifIsNotMetodoParaRelacionamentoMuitos(String template)
			throws XDocletException {
		XMethod method = getCurrentMethod();
		if (!TagHandlerUtil.ifIsMetodoParaRelacionamentoMuitos(method)) {
			generate(template);
		}
	}

	/**
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsUmPraMuitos(String template) throws Wj2eeException,
			XDocletException {
		XMethod method = getCurrentMethod();
		if (TagHandlerUtil.ifIsMetodoParaRelacionamentoMuitos(method)) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo corrente é um método para relacionamento entre
	 * entidades getter ou setter para Collection ou ICadastroBaseVO e avalia se
	 * sua cardinalidade é "UM"
	 * 
	 * @doc.tag type="block"
	 */
	public void ifIsMetodoParaRelacionamentoUm(String template)
			throws XDocletException {
		XMethod method = getCurrentMethod();
		if (TagHandlerUtil.ifIsMetodoParaRelacionamentoUm(method)) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo corrente NÃO é um método para relacionamento entre
	 * entidades getter ou setter para Collection ou ICadastroBaseVO
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsNotMetodoParaRelacionamento(String template)
			throws Wj2eeException, XDocletException {
		XMethod method = getCurrentMethod();
		if (!TagHandlerUtil.ifIsMetodoParaRelacionamento(method, getXJavaDoc())) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo corrente é de tipo conhecido
	 * 
	 * @throws XDocletException
	 * @doc.tag type="block"
	 */
	public void ifIsPropriedadeTratavelNaoTipoVO(String template)
			throws XDocletException {
		XMethod method = getCurrentMethod();
		XClass class1 = TagHandlerUtil.xClassDoMetodoAcessor(method);
		if (TagHandlerUtil.ifIsPropriedadeTratavelNaoTipoVO(class1)) {
			generate(template);
		}
	}

	public void ifIsPropriedadeTratavelTipoVO(String template)
			throws XDocletException {
		XMethod method = getCurrentMethod();
		XClass class1 = TagHandlerUtil.xClassDoMetodoAcessor(method);
		if (!TagHandlerUtil.ifIsPropriedadeTratavelNaoTipoVO(class1)) {
			generate(template);
		}
	}

	public void ifIsSistemaExterno(String template) throws XDocletException,
			Wj2eeException {
		if (TagHandlerUtil.ifIsSistemaExterno(getCurrentMethod())) {
			generate(template);
		}
	}

	public void ifIsNotSistemaExterno(String template) throws XDocletException,
			Wj2eeException {
		if (!TagHandlerUtil.ifIsSistemaExterno(getCurrentMethod())) {
			generate(template);
		}
	}

	public void ifIsPropriedadeTratavelTipoCollection(String template)
			throws XDocletException {
		XMethod method = getCurrentMethod();
		if (TagHandlerUtil.ifIsPropriedadeTratavelTipoCollection(method
				.getPropertyType().getType())) {
			generate(template);
		}
	}

	public void ifIsNotPropriedadeTratavelTipoCollection(String template)
			throws XDocletException {
		XMethod method = getCurrentMethod();
		if (!TagHandlerUtil.ifIsPropriedadeTratavelTipoCollection(method
				.getPropertyType().getType())) {
			generate(template);
		}
	}

	public void ifIsPropriedadeTratavel(String template)
			throws XDocletException {
		XMethod method = getCurrentMethod();
		XClass class1 = TagHandlerUtil.xClassDoMetodoAcessor(method);
		if (TagHandlerUtil.ifIsPropriedadeTratavel(class1)) {
			generate(template);
		}
	}

	/**
	 * @doc.tag type = "content"
	 */
	public String actionAttribute() {
		return "";
	}

	/** @doc.tag type = "content" * */
	public String actionFormXDocletComment() {
		return "";
	}

	/** @doc.tag type = "content" * */
	public String actionXDocletComment() {
		return "";
	}

	/**
	 * Escreve um nome de variável (ref a Objeto) para um determinado tipo <br>
	 * ex:<br>
	 * para o tipo 'StatusDAO' variavel 'statusDAO'<br>
	 * 
	 * StatusDAO statusDAO = (StatusDAO)DAOFactory(xyz);
	 * 
	 */
	public String denifirVariavelParaTipo(Properties attributes) {
		return TagHandlerUtil.denifirVariavelParaTipo(attributes);
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String methodTypeFilter() throws XDocletException {
		String retorno = TagHandlerUtil.methodTypeFilter(getCurrentMethod());

		return retorno;
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String propertyNameSemSufixoVO() {
		return TagHandlerUtil.propertyNameSemSufixoVO(getCurrentMethod());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String facadeClassName() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		XClass class1 = method.getReturnType().getType();
		return TagHandlerUtil.facadeClassName(class1, getXJavaDoc());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String vlhFacadeClassName() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		XClass class1 = method.getReturnType().getType();
		return TagHandlerUtil.vlhFacadeClassName(class1);
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String entidadeDoMetodo() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		String entidadeDoMetodo = TagHandlerUtil.entidadeDoMetodo(method,
				getXJavaDoc());

		entidadeDoMetodo = entidadeDoMetodo.replaceFirst("VO", "");

		return entidadeDoMetodo;
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String mestreEntidadeDoMetodo() throws Wj2eeException {
		XMethod method = getCurrentMethod();

		/* classe d retorno */
		XClass class1 = method.getReturnType().getType();

		/* mestre da classe do retorno */
		XClass classeMestre = TagHandlerUtil
				.classeMestre(class1, getXJavaDoc());

		/* descubro a entidade */
		String entidadeDoMetodo = classeMestre.getName();

		/* replace */
		entidadeDoMetodo = entidadeDoMetodo.replaceFirst("VO", "");

		return entidadeDoMetodo;
	}

	/** @doc.tag type = "content" * */
	public String ejbName() {
		return "";
	}

	/** @doc.tag type = "content" * */
	public String ejbVLHName() {
		return "";
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String facadeVLHName() throws Wj2eeException {
		return "";
	}

	/** @doc.tag type = "content" * */
	public String jdniModule() {
		return "";
	}

	/** @doc.tag type = "content" * */
	public String jdniVLHModule() {
		return "";
	}

	/** @doc.tag type = "content" * */
	public String jspCadastroFileName() {
		return "";
	}

	/** @doc.tag type = "content" * */
	public String jspPesquisaFileName() {
		return "";
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String moduleConstant() throws Wj2eeException {
		return TagHandlerUtil.moduleConstant(getCurrentClass());
	}

	public String moduleConstant(Properties attributes) throws Wj2eeException {
		return TagHandlerUtil.moduleConstant(attributes);

	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String moduleVLHConstant() throws Wj2eeException {
		return TagHandlerUtil.moduleVLHConstant(getCurrentClass());

	}

	public String moduleVLHConstant(Properties attributes)
			throws Wj2eeException {

		String moduleVLHConstant = TagHandlerUtil.moduleVLHConstant(attributes);
		return moduleVLHConstant;
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String voClassName() throws Wj2eeException {
		XClass class1 = TagHandlerUtil.classeDoMetodo(getCurrentMethod(),
				getXJavaDoc());

		String voClassName = TagHandlerUtil.voClassName(class1);

		return voClassName;
	}

	public XClass getCurrentMethodProperty() {

		XMethod method = getCurrentMethod();

		Type type = method.getPropertyType();

		XClass class1 = type.getType();

		return class1;
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String multiLinguaLabelKey() throws Wj2eeException {
		return TagHandlerUtil.labelKeyProperty(getCurrentClass(),
				getCurrentMethod());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String multiLinguaDicaKey() throws Wj2eeException {
		return TagHandlerUtil.dicaKeyProperty(getCurrentClass(),
				getCurrentMethod());

	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String multiLinguaLabelValue() throws Wj2eeException {
		return TagHandlerUtil.labelValueProperty(getCurrentMethod());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String multiLinguaDicaValue() throws Wj2eeException {
		return TagHandlerUtil.dicaValueProperty(getCurrentMethod());

	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String mascaraView() throws Wj2eeException {
		return TagHandlerUtil.mascaraView(getCurrentMethod());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String tamanhoMinimo() throws Wj2eeException {
		return TagHandlerUtil.tamanhoMinimo(getCurrentMethod());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String tamanhoMaximo() throws Wj2eeException {
		return TagHandlerUtil.tamanhoMaximo(getCurrentMethod());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String descricao() throws Wj2eeException {
		return TagHandlerUtil.descricao(getCurrentMethod());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String dica() throws Wj2eeException {
		return TagHandlerUtil.dica(getCurrentMethod());
	}

	/**
	 * Analisa se o metodo corrente é somente leitura
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsIgnorar(String template) throws Wj2eeException,
			XDocletException {
		if (TagHandlerUtil.ignorar(getCurrentMethod())) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo corrente é requerido
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsRequerido(String template) throws Wj2eeException,
			XDocletException {
		if (TagHandlerUtil.ifIsRequerido(getCurrentMethod())) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo corrente possui delimitador de valor (malor minimo,
	 * valor maximo)
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsValorDelimitado(String template) throws Wj2eeException,
			XDocletException {
		if (TagHandlerUtil.ifIsValorDelimitado(getCurrentMethod())) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo corrente !!NAO!! possui delimitador de valor (valor
	 * minimo, valor maximo)
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsNotValorDelimitado(String template) throws Wj2eeException,
			XDocletException {
		if (!TagHandlerUtil.ifIsValorDelimitado(getCurrentMethod())) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo corrente é somente leitura
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsNotIgnorar(String template) throws Wj2eeException,
			XDocletException {
		if (!TagHandlerUtil.ignorar(getCurrentMethod())) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo corrente é somente leitura
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsNotRequerido(String template) throws Wj2eeException,
			XDocletException {
		if (!TagHandlerUtil.ifIsRequerido(getCurrentMethod())) {
			generate(template);
		}
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String renderer() throws Wj2eeException {
		return TagHandlerUtil.renderer(getCurrentMethod());
	}

	/**
	 * Constroi um gerador de código com base no atributo XDoclet renderer do
	 * método e invoca este
	 * 
	 * @throws Wj2eeException
	 * @throws XDocletException
	 * 
	 * @doc.tag type = "content"
	 */
	public String renderControl(Properties properties) throws Wj2eeException,
			XDocletException {
		String tipoControle = properties
				.getProperty(TagHandlerUtil.ATTRIBUTE_TIPO_CONTROLE);
		return TagHandlerUtil.renderControl(tipoControle, getCurrentMethod(),
				getXJavaDoc(), getControlSufix());
	}

	/**
	 * Constroi um gerador de código com base no atributo XDoclet renderer do
	 * método e invoca este
	 * 
	 * @throws Throwable
	 * @throws Wj2eeException
	 * @throws XDocletException
	 * 
	 * @doc.tag type = "content"
	 */
	public String renderControl() throws Throwable {
		try {

			XJavaDoc javaDoc = getXJavaDoc();
			if (javaDoc == null) {
				throw new Wj2eeException("nuuuuuuulllllllloooo o getxjavadoc");
			}

			return TagHandlerUtil.renderControl(
					TagHandlerUtil.TIPO_CONTROLE_DEFAULT, getCurrentMethod(),
					javaDoc, getControlSufix());
		} catch (Throwable t) {
			TagHandlerUtil.trataExcecao(t);
			throw t;
		}
	}

	/**
	 * Identifica qual a classe contida dentro de uma colecao atraves do
	 * atributo 'classe' da tag wj2ee<br>
	 * Ex: Collection contendo EnderecoVO, retorno
	 * br.com.dlp.wizard.enderevo.EnderecoVO
	 * 
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String tipoDoConteudoDaColecao() throws Wj2eeException {
		XClass class1 = TagHandlerUtil.classeDoMetodo(getCurrentMethod(),
				getXJavaDoc());
		return class1.getName();
	}

	/**
	 * Identifica qual a classe contida dentro de uma colecao atraves do
	 * atributo 'classe' da tag wj2ee<br>
	 * Ex: Collection contendo EnderecoVO, retorno
	 * br.com.dlp.wizard.enderevo.EnderecoVO
	 * 
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String tipoDoConteudoDaColecaoQualifiedName() throws Wj2eeException {
		XClass class1 = TagHandlerUtil.classeDoMetodo(getCurrentMethod(),
				getXJavaDoc());
		return class1.getQualifiedName();
	}

	/**
	 * @doc.tag type = "content"
	 */
	public String toSingular(Properties properties) {
		String nome = properties.getProperty(TagHandlerUtil.ATTRIBUTE_NOME);

		String returnValue = TagHandlerUtil.toSingular(nome);

		return returnValue;
	}

	/**
	 * @doc.tag type = "content"
	 */
	public String toPlural(Properties properties) {
		String nome = properties.getProperty(TagHandlerUtil.ATTRIBUTE_NOME);

		String returnValue = TagHandlerUtil.toPlural(nome);

		return returnValue;
	}

	/**
	 * @throws XDocletException
	 * @doc.tag type = "content"
	 */
	public String toSingularPrimeiraMaiuscula(Properties properties)
			throws XDocletException {
		String nome = properties.getProperty(TagHandlerUtil.ATTRIBUTE_NOME);

		String returnValue = TagHandlerUtil.toSingular(nome);

		returnValue = TagHandlerUtil.primeiraLetraMaiuscula(returnValue);

		return returnValue;
	}

	/**
	 * @throws XDocletException
	 * @doc.tag type = "content"
	 */
	public String toPluralPrimeiraMaiuscula(Properties properties)
			throws XDocletException {
		String nome = properties.getProperty(TagHandlerUtil.ATTRIBUTE_NOME);

		String returnValue = TagHandlerUtil.toPlural(nome);

		returnValue = TagHandlerUtil.primeiraLetraMaiuscula(returnValue);

		return returnValue;
	}

	/**
	 * @throws XDocletException
	 * @doc.tag type = "content"
	 */
	public String primeiraLetraMaiuscula(Properties properties)
			throws XDocletException {
		String nome = properties.getProperty(TagHandlerUtil.ATTRIBUTE_NOME);
		nome = TagHandlerUtil.primeiraLetraMaiuscula(nome);
		return nome;
	}

	/**
	 * @throws XDocletException
	 * @doc.tag type = "content"
	 */
	public String maiusculo(Properties properties) throws XDocletException {
		String nome = properties.getProperty(TagHandlerUtil.ATTRIBUTE_NOME);
		nome = nome.toUpperCase();
		return nome;
	}

	/**
	 * @throws XDocletException
	 * @doc.tag type = "content"
	 */
	public String minusculo(Properties properties) throws XDocletException {
		String nome = properties.getProperty(TagHandlerUtil.ATTRIBUTE_NOME);
		nome = nome.toLowerCase();
		return nome;
	}

	/**
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String idEntidadeSelecionada() throws XDocletException,
			Wj2eeException {

		XClass class1 = TagHandlerUtil.classeDoMetodo(getCurrentMethod(),
				getXJavaDoc());
		String entidade = TagHandlerUtil.entidadeDaClasse(class1);

		entidade = TagHandlerUtil.primeiraLetraMinuscula(entidade);

		String returnValue = TagHandlerUtil.idEntidadeSelecionado(entidade);

		return returnValue;
	}

	/**
	 * @throws XDocletException
	 * @doc.tag type = "content"
	 */
	public String constanteParaServico(Properties properties)
			throws XDocletException {
		String servico = properties.getProperty(TagHandlerUtil.ATTRIBUTE_NOME);
		return TagHandlerUtil.constanteParaServico(servico);
	}

	/**
	 * @throws XDocletException
	 * @doc.tag type = "content"
	 */
	public String constanteParaOrdenacao(Properties properties)
			throws XDocletException {
		String nome = properties.getProperty(TagHandlerUtil.ATTRIBUTE_NOME);
		return TagHandlerUtil.constanteParaOrdenacao(nome);
	}

	/**
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String servicoFecharItem() throws XDocletException, Wj2eeException {
		return TagHandlerUtil.servicoFecharItem(getCurrentMethod(),
				getXJavaDoc());
	}

	/**
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String servicoSalvarItem() throws XDocletException, Wj2eeException {
		return TagHandlerUtil.servicoSalvarItem(getCurrentMethod(),
				getXJavaDoc());
	}

	/**
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String serviceLocatorFullClassName() throws XDocletException,
			Wj2eeException {
		return TagHandlerUtil.serviceLocatorFullClassName(getCurrentMethod(),
				getXJavaDoc());
	}

	/**
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String sistema() throws XDocletException, Wj2eeException {
		return TagHandlerUtil.sistema(getCurrentMethod(), getXJavaDoc());
	}

	/**
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String servicoIncluirItem() throws XDocletException, Wj2eeException {
		return TagHandlerUtil.servicoIncluirItem(getCurrentMethod(),
				getXJavaDoc());
	}

	/**
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String servicoRemoverItem() throws XDocletException, Wj2eeException {
		return TagHandlerUtil.servicoRemoverItem(getCurrentMethod(),
				getXJavaDoc());
	}

	/**
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String servicoEditarItem() throws XDocletException, Wj2eeException {
		return TagHandlerUtil.servicoEditarItem(getCurrentMethod(),
				getXJavaDoc());
	}

	/**
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String servicoConsultarItem() throws XDocletException,
			Wj2eeException {
		return TagHandlerUtil.servicoConsultarItem(getCurrentMethod(),
				getXJavaDoc());
	}

	/**
	 * Analisa se para o property accessor corrente deve-se criar no ActionForm
	 * um Getter/Setter virtual, que funciona com uma lista de valores e um
	 * ponteiro
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsCriarGetterSetterVirtual(String template)
			throws Wj2eeException, XDocletException {
		if (TagHandlerUtil.ifIsCriarGetterSetterVirtual(getCurrentMethod())) {
			generate(template);
		}
	}

	/**
	 * Analisa se para o property accessor corrente !!NAO!! deve-se criar no
	 * ActionForm um Getter/Setter virtual, que funciona com uma lista de
	 * valores e um ponteiro
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsNotCriarGetterSetterVirtual(String template)
			throws Wj2eeException, XDocletException {
		if (!TagHandlerUtil.ifIsCriarGetterSetterVirtual(getCurrentMethod())) {
			generate(template);
		}
	}

	/**
	 * @throws Wj2eeException
	 * @throws XDocletException
	 * @doc.tag type = "content"
	 */
	public String nestedPropertyList() throws Wj2eeException {
		return TagHandlerUtil.nestedPropertyList(getCurrentMethod());
	}

	/**
	 * @throws Wj2eeException
	 * @throws XDocletException
	 * @doc.tag type = "content"
	 */
	public String tipoAtributoParaPesquisa() throws Wj2eeException {
		return TagHandlerUtil.tipoAtributoParaPesquisa(getCurrentMethod(),
				getXJavaDoc());

	}

	/**
	 * @throws Wj2eeException
	 * @throws XDocletException
	 * @doc.tag type = "content"
	 */
	public String nomeAtributoParaPesquisa() throws Wj2eeException {

		String nomeAtributoParaPesquisa = TagHandlerUtil
				.nomeAtributoParaPesquisa(getCurrentMethod(), getControlSufix());

		return nomeAtributoParaPesquisa;
	}

	/**
	 * Analisa se o metodo corrente é de um campo PK
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsPk(String template) throws Wj2eeException, XDocletException {
		XMethod method = getCurrentMethod();

		if (TagHandlerUtil.ifIsPk(method)) {
			generate(template);
		}
	}

	/**
	 * Analisa se o metodo corrente !!NÃO!! é de um campo PK
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsNotPk(String template) throws Wj2eeException,
			XDocletException {
		XMethod method = getCurrentMethod();

		if (!TagHandlerUtil.ifIsPk(method)) {
			generate(template);
		}
	}

	/**
	 * Analisa se é para criar um mecanismo de popup para o metodo corrente
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsCriarMecanismoPopUp(String template) throws Wj2eeException,
			XDocletException {
		XMethod method = getCurrentMethod();

		if (TagHandlerUtil.ifIsCriarMecanismoPopUp(method, getXJavaDoc())) {
			generate(template);
		}

	}

	/**
	 * Analisa se é para criar um mecanismo de popup para o metodo corrente
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type="block"
	 */
	public void ifIsNotCriarMecanismoPopUp(String template)
			throws Wj2eeException, XDocletException {
		XMethod method = getCurrentMethod();

		if (!TagHandlerUtil.ifIsCriarMecanismoPopUp(method, getXJavaDoc())) {
			generate(template);
		}

	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String callConstantServiceParaPopUp() throws Wj2eeException {
		return TagHandlerUtil.constanteParaServico(callServiceParaPopUp());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String callServiceParaPopUp() throws Wj2eeException {
		String callService = TagHandlerUtil.callServiceParaPopUp(
				getCurrentMethod(), getXJavaDoc());
		return callService;
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String confirmConstantServiceParaPopUp() throws Wj2eeException {
		return TagHandlerUtil.constanteParaServico(confirmServiceParaPopUp());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String confirmServiceParaPopUp() throws Wj2eeException {
		String confirmService = TagHandlerUtil.confirmServiceParaPopUp(
				getCurrentMethod(), getXJavaDoc());
		return confirmService;
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String metodoMecanismoPesquisaVLH() throws Wj2eeException {
		String metodo = TagHandlerUtil
				.metodoMecanismoPesquisa(getCurrentMethod().getPropertyType()
						.getType());
		return "execute" + TagHandlerUtil.primeiraLetraMaiuscula(metodo);
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String defaultValueParaPesquisa() {
		return TagHandlerUtil.defaultValueParaPesquisa(getCurrentMethod());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String initialValueParaPesquisa() {
		return TagHandlerUtil.initialValueParaPesquisa(getCurrentMethod());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String hibernateDAOCriteriaExpressionFindByPK() {
		return TagHandlerUtil
				.hibernateDAOCriteriaExpressionFindByPK(getCurrentMethod());
	}

	/**
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String hibernateDAOCriteriaExpression() throws Wj2eeException,
			XDocletException {
		return TagHandlerUtil.hibernateDAOCriteriaExpression(
				getCurrentMethod(), getControlSufix());
	}

	/**
	 * Avalia se o operador de pesquisa é do tipo especificado
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * 
	 * @doc.tag type="block"
	 * @doc.param name="operador"
	 */
	public void ifIsOperadorPesquisa(String template, Properties attributes)
			throws XDocletException, Wj2eeException {
		boolean returnValue = TagHandlerUtil.ifIsOperadorPesquisa(
				getCurrentMethod(), attributes);
		if (returnValue) {
			generate(template);
		}
	}

	/**
	 * Avalia se o operador de pesquisa !!NÃO!! é do tipo especificado
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * 
	 * @doc.tag type="block"
	 * @doc.param name="operador"
	 */
	public void ifIsNotOperadorPesquisa(String template, Properties attributes)
			throws XDocletException, Wj2eeException {
		boolean returnValue = TagHandlerUtil.ifIsOperadorPesquisa(
				getCurrentMethod(), attributes);
		if (!returnValue) {
			generate(template);
		}
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String htmlTDAberturaCampoPesquisa() throws Wj2eeException {

		String sufixPesquisa = getControlSufix();
		String returValue;

		if (sufixPesquisa.equals(TagHandlerUtil.CONTROL_SUFIX_DE)) {
			returValue = "<td class=\"tt3L\"  width=\"70%\">De:";
		} else if (sufixPesquisa.equals(TagHandlerUtil.CONTROL_SUFIX_ATE)) {
			returValue = "Até:";
		} else if (sufixPesquisa.equals(TagHandlerUtil.CONTROL_SUFIX_EM_BRANCO)) {
			returValue = "<td class=\"tt3L\"  width=\"70%\">";
		} else {
			throw new Wj2eeException("O sufixo de controle '" + sufixPesquisa
					+ "' nao e valido!");
		}

		return returValue;

	}

	/**
	 * Avalia se o ControlSufix informado é o atual
	 * 
	 * @throws XDocletException
	 * 
	 * @doc.tag type="block"
	 * @doc.param name="controlSufix"
	 */
	public void ifIsControlSufix(String template, Properties attributes)
			throws XDocletException {
		boolean returnValue = TagHandlerUtil.ifIsControlSufix(attributes,
				getControlSufix());
		if (returnValue) {
			generate(template);
		}
	}

	/**
	 * Avalia se o ControlSufix informado !!NÃO!! é o atual
	 * 
	 * @throws XDocletException
	 * 
	 * @doc.tag type="block"
	 * @doc.param name="controlSufix"
	 */
	public void ifIsNotControlSufix(String template, Properties attributes)
			throws XDocletException {
		boolean returnValue = TagHandlerUtil.ifIsControlSufix(attributes,
				getControlSufix());
		if (!returnValue) {
			generate(template);
		}
	}

	/**
	 * Avalia o renderer do método
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * 
	 * @doc.tag type="block"
	 * @doc.param name="renderer"
	 */
	public void ifIsRenderer(String template, Properties attributes)
			throws XDocletException, Wj2eeException {
		boolean returnValue = TagHandlerUtil.ifIsRenderer(attributes,
				getCurrentMethod());
		if (returnValue) {
			generate(template);
		}
	}

	/**
	 * Avalia o renderer do método
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * 
	 * @doc.tag type="block"
	 * @doc.param name="renderer"
	 */
	public void ifIsNotRenderer(String template, Properties attributes)
			throws XDocletException, Wj2eeException {
		boolean returnValue = TagHandlerUtil.ifIsRenderer(attributes,
				getCurrentMethod());
		if (!returnValue) {
			generate(template);
		}
	}

	/**
	 * @throws Wj2eeException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String voWrapperClassName() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		XClass class1 = TagHandlerUtil.classeDoMetodo(method, getXJavaDoc());

		String voWrapperClassName = TagHandlerUtil.voWrapperClassName(class1);

		return voWrapperClassName;

	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String newAcheActionMessageRequerido() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		String newAcheActionMessage = TagHandlerUtil
				.newAcheActionMessageRequerido(method);
		return newAcheActionMessage;
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String newAcheActionMessageValorDelimitado() throws Wj2eeException {
		XMethod method = getCurrentMethod();

		String newAcheActionMessageRange = TagHandlerUtil
				.newAcheActionMessageValorDelimitado(method, getXJavaDoc());
		return newAcheActionMessageRange;
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String validacaoCampoRequerido(Properties attributes)
			throws Wj2eeException {

		return TagHandlerUtil.validacaoCampoRequerido(getCurrentMethod(),
				getXJavaDoc(), attributes);

	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String validacaoValorDelimitado(Properties attributes)
			throws Wj2eeException {

		try {
			return TagHandlerUtil.validacaoValorDelimitado(getCurrentMethod(),
					getXJavaDoc(), attributes);
		} catch (Wj2eeException e) {
			TagHandlerUtil.trataExcecao(e);
			throw e;
		}

	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleId() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		XClass class1 = TagHandlerUtil.classeDoMetodo(method, getXJavaDoc());
		return TagHandlerUtil.moduleId(class1);
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleId(Properties properties) throws Wj2eeException {
		String strModuleId = moduleId();

		strModuleId = TagHandlerUtil.variavelDe(strModuleId, properties);

		return strModuleId;
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleJavaScriptFile() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		XClass class1 = TagHandlerUtil.classeDoMetodo(method, getXJavaDoc());
		return TagHandlerUtil.moduleJavaScriptFile(class1);
	}

	public String moduleDescription() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		XClass class1 = TagHandlerUtil.classeDoMetodo(method, getXJavaDoc());
		return TagHandlerUtil.moduleDescription(class1);
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleWebDirectory() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		XClass class1 = TagHandlerUtil.classeDoMetodo(method, getXJavaDoc());
		return TagHandlerUtil.moduleWebDirectory(class1);
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleCadastroJSPPage() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		XClass class1 = TagHandlerUtil.classeDoMetodo(method, getXJavaDoc());
		return TagHandlerUtil.moduleCadastroJSPPage(class1);
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleConsultaJSPPage() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		XClass class1 = TagHandlerUtil.classeDoMetodo(method, getXJavaDoc());
		return TagHandlerUtil.moduleConsultaJSPPage(class1);
	}

	public String cadastroTileName() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		XClass class1 = TagHandlerUtil.classeDoMetodo(method, getXJavaDoc());
		String cadastroTileName = TagHandlerUtil.cadastroTileName(class1);
		return cadastroTileName;
	}

	public String consultaTileName() throws Wj2eeException {
		XMethod method = getCurrentMethod();
		XClass class1 = TagHandlerUtil.classeDoMetodo(method, getXJavaDoc());
		String consultaTileName = TagHandlerUtil.consultaTileName(class1);
		return consultaTileName;
	}

}
