/*
 * Data de Criacao 28/05/2005 21:49:53
 * 
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.tagshandler;

import java.io.CharArrayWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xdoclet.DocletContext;
import xdoclet.XDocletException;
import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.codegenerator.CodeGeneratorFactory;
import xdoclet.jazzwizard.codegenerator.MetodosPesquisaveisPredicate;
import xdoclet.jazzwizard.codegenerator.NotReadOnlyMethodPredicate;
import xdoclet.jazzwizard.codegenerator.Wj2eeXMethodComparator;
import xdoclet.jazzwizard.codegenerator.base.AbstractStrutsCodeGenerator;
import xdoclet.jazzwizard.codegenerator.base.ICodeGenerator;
import xdoclet.jazzwizard.codegenerator.predicate.MetodosModuloEscravo;
import xdoclet.jazzwizard.tagvalue.AtributosClasseVO;
import xdoclet.jazzwizard.tagvalue.AtributosMetodoVO;
import xjavadoc.XClass;
import xjavadoc.XJavaDoc;
import xjavadoc.XMethod;
import xjavadoc.XPackage;
import br.com.dlp.framework.util.FrameworkAcheUtil;

/**
 * Classe utilitaria para os TagsHandlers
 * 
 * @author Darcio L Pacifico - 28/05/2005 21:49:53
 * 
 * 
 */
public class TagHandlerUtil {
	protected Log logger = LogFactory.getLog(TagHandlerUtil.class);

	// protected Log logger = LogFactory.getLog(TagHandlerUtil.class);

	public static final String FORMATKEY_DATA = "formatoDataView";

	public static final String FORMATKEY_SHORT = "formatoShort";

	public static final String FORMATKEY_INTEGER = "formatoInteger";

	public static final String FORMATKEY_LONG = "formatoLong";

	public static final String FORMATKEY_FLOAT = "formatoFloat";

	public static final String FORMATKEY_DOUBLE = "formatoDouble";

	public static final String FORMATKEY_FLOAT_STRUTS = "formatoFloatStruts";

	public static final String FORMATKEY_DOUBLE_STRUTS = "formatoDoubleStruts";

	public static final String FORMATO_DATA_WJ2EE = "dd/MM/yyyy";

	public static final String CLASS_NAME_BASEVO = "br.com.dlp.framework.vo.ICadastroBaseVO";

	public static final String CLASS_NAME_STRING = "java.lang.String";

	public static final String CLASS_NAME_DATE = "java.util.Date";

	public static final String CLASS_NAME_NUMBER = "java.lang.Number";

	public static final String CLASS_NAME_COLLECTION = "java.util.Collection";

	public static final String W2JEE_NAMESPACE = "wj2ee";

	public static final String RENDERER_CHECKBOX = "checkbox";

	public static final String RENDERER_COMBO = "combo";

	public static final String RENDERER_INDEXADO = "indexado";

	public static final String RENDERER_LOCAL = "local";

	public static final String RENDERER_MODULO_ESCRAVO = "moduloescravo";

	public static final String RENDERER_MONO_POPUP = "monopopup";

	public static final String RENDERER_MULTI_POPUP = "multipopup";

	public static final String RENDERER_RADIO = "radio";

	public static final String RENDERER_TEXT = "text";

	public static final String RENDERER_TEXTAREA = "textarea";

	public static final String ATTRIBUTE_VARIAVEL = "isVar";

	public static final String ATTRIBUTE_ENTIDADE = "entidade";

	public static final String ATTRIBUTE_CONTROL_SUFIX = "controlSufix";

	public static final String ATTRIBUTE_TIPO = "tipo";

	public static final String ATTRIBUTE_SUFIX = "sufix";

	public static final String ATTRIBUTE_RECURSIVAMENTE = "recursivamente";

	/* permite que um método se repita em uma iteração recursiva */
	public static final String ATTRIBUTE_PERMITE_REPETIR = "permitirRepeticao";

	public static final String ATTRIBUTE_RENDERER = "renderer";

	public static final String ATTRIBUTE_NOME = "nome";

	public static final String ATTRIBUTE_TIPO_CONTROLE = "tipoControle";

	public static final String TIPO_CONTROLE_PESQUISA = "pesquisa";

	public static final String TIPO_CONTROLE_CADASTRO = "cadastro";

	public static final String TIPO_CONTROLE_CONSULTA = "consulta";

	public static final String TIPO_CONTROLE_INDEXADO = "indexado";

	public static final String TIPO_CONTROLE_DEFAULT = TIPO_CONTROLE_CADASTRO;

	public static final String ATTRIBUTE_OPERADOR = "operador";

	public static final String OPERADOR_LIKE = "like";

	public static final String OPERADOR_EQUAL = "equal";

	public static final String OPERADOR_RANGE_VALUE = "rangeValue";

	public static final String CONTROL_SUFIX_DE = "De";

	public static final String CONTROL_SUFIX_ATE = "Ate";

	public static final String CONTROL_SUFIX_EM_BRANCO = "";

	/**
	 * Retorna o formato de data informado como parâmetro
	 */
	public static String getFormatoData(XJavaDoc javaDoc) {
		return FORMATO_DATA_WJ2EE;
	}

	/**
	 * Implementação de meu sort que será chamado por
	 * Wj2eeMethodTagsHandler.forAllMembers()
	 */
	public static List sortMethods(List members) {
		Comparator comparator = new Wj2eeXMethodComparator();

		List newList = new ArrayList(members);

		Collections.sort(newList, comparator);

		return newList;
	}

	/**
	 * Retira da coleção os métodos com a flag ignorar=true
	 */
	public static Collection retiraMetodosIgnorados(Collection members)
			throws Wj2eeException {
		Collection aretirar = new ArrayList();

		members = new ArrayList(members);

		/*
		 * Algumas implementações de collection não permitem remoção de itens;
		 * ainda não descobrir pq disso, mas td bem!...
		 */

		Iterator iterator = members.iterator();
		while (iterator.hasNext()) {
			XMethod method = (XMethod) iterator.next();
			if (TagHandlerUtil.ignorar(method)) {
				aretirar.add(method);
			}
		}

		members.removeAll(aretirar);

		return members;

	}

	/**
	 * Retira da coleção os métodos com a flag somenteLeitua=true
	 */
	public static Collection retiraSomenteLeitua(Collection members)
			throws Wj2eeException {
		Collection aretirar = new ArrayList();

		members = new ArrayList(members);

		/*
		 * Algumas implementações de collection não permitem remoção de itens;
		 * ainda não descobrir pq disso, mas td bem!...
		 */

		Iterator iterator = members.iterator();
		while (iterator.hasNext()) {
			XMethod method = (XMethod) iterator.next();
			if (TagHandlerUtil.somenteLeitura(method)) {
				aretirar.add(method);
			}
		}

		members.removeAll(aretirar);

		return members;

	}

	public static AtributosMetodoVO getAtributosMetodoVO(XMethod method)
			throws Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = new AtributosMetodoVO(method);
		return atributosMetodoVO;
	}

	public static AtributosClasseVO getAtributosClasseVO(XClass class1)
			throws Wj2eeException {
		AtributosClasseVO atributosClasseVO = new AtributosClasseVO(class1);
		return atributosClasseVO;
	}

	public static boolean ifIsCriarMecanismoDeFind(XMethod method,
			XJavaDoc javaDoc) throws XDocletException, Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);

		/* teste se não é somente leitura e se é uma propertie */
		if ((!ifIsSomenteLeitura(method) || ifIsPropriedadePesquisavel(method))
				&& ifIsMetodoParaRelacionamento(method, javaDoc)) {

			String renderer = atributosMetodoVO.getRenderer();

			/*
			 * Darcio - 16/02/2005 Problema do combo de InformacaoSolicitavelVO;
			 * InformacaoSolicitavelVO é indexado de InformacaoSolicitavelVO
			 * mudei o IF Original:
			 * 
			 * XClass classeDoMetodo = classeDoMetodo(method, javaDoc);
			 * AtributosClasseVO atributosClasseVO = new
			 * AtributosClasseVO(classeDoMetodo); String classRenderer =
			 * atributosClasseVO.getRenderer(); if (! (
			 * classRenderer.equals(RENDERER_MODULO_ESCRAVO) ||
			 * classRenderer.equals(RENDERER_INDEXADO) ||
			 * classRenderer.equals(RENDERER_LOCAL) ) && (
			 * renderer.equals(RENDERER_CHECKBOX) ||
			 * renderer.equals(RENDERER_COMBO) ||
			 * renderer.equals(RENDERER_RADIO) ) ) {
			 * 
			 */

			if ((renderer.equals(RENDERER_CHECKBOX)
					|| renderer.equals(RENDERER_COMBO) || renderer
					.equals(RENDERER_RADIO))) {

				return true;

			}
		}

		return false;
	}

	public static boolean ifIsCriarMecanismoDeFindUmParaMuitos(XMethod method,
			XJavaDoc javaDoc) throws XDocletException, Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);

		/* teste se não é somente leitura e se é uma propertie */
		if ((!ifIsSomenteLeitura(method) || ifIsPropriedadePesquisavel(method))
				&& ifIsMetodoParaRelacionamento(method, javaDoc)) {

			String renderer = atributosMetodoVO.getRenderer();

			/*
			 * Darcio - 16/02/2005 Problema do combo de InformacaoSolicitavelVO;
			 * InformacaoSolicitavelVO é indexado de InformacaoSolicitavelVO
			 * mudei o IF Original:
			 * 
			 * XClass classeDoMetodo = classeDoMetodo(method, javaDoc);
			 * AtributosClasseVO atributosClasseVO = new
			 * AtributosClasseVO(classeDoMetodo); String classRenderer =
			 * atributosClasseVO.getRenderer(); if (! (
			 * classRenderer.equals(RENDERER_MODULO_ESCRAVO) ||
			 * classRenderer.equals(RENDERER_INDEXADO) ||
			 * classRenderer.equals(RENDERER_LOCAL) ) && (
			 * renderer.equals(RENDERER_CHECKBOX) ||
			 * renderer.equals(RENDERER_COMBO) ||
			 * renderer.equals(RENDERER_RADIO) ) ) {
			 * 
			 */

			if ((renderer.equals(RENDERER_INDEXADO) || renderer
					.equals(RENDERER_MODULO_ESCRAVO))) {

				return true;

			}
		}

		return false;
	}

	public static boolean ifIsCriarMecanismoDeFindVirtual(XMethod method,
			XJavaDoc javaDoc) throws XDocletException, Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);

		/* teste se não é somente leitura e se é uma proprie */
		if (!ifIsSomenteLeitura(method)
				&& ifIsMetodoParaRelacionamento(method, javaDoc)) {

			String renderer = atributosMetodoVO.getRenderer();
			XClass classeDoMetodo = classeDoMetodo(method, javaDoc);
			AtributosClasseVO atributosClasseVO = new AtributosClasseVO(
					classeDoMetodo);

			String classRenderer = atributosClasseVO.getRenderer();

			if ((classRenderer.equals(RENDERER_MODULO_ESCRAVO)
					|| classRenderer.equals(RENDERER_INDEXADO) || classRenderer
					.equals(RENDERER_LOCAL))
					&& (renderer.equals(RENDERER_CHECKBOX)
							|| renderer.equals(RENDERER_COMBO) || renderer
							.equals(RENDERER_RADIO))) {

				return true;

			}
		}

		return false;
	}

	public static boolean ifIsSomenteLeitura(XMethod method)
			throws XDocletException, Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		String somenteLeitura = atributosMetodoVO.getSomenteLeitura();
		return somenteLeitura.equals("true");
	}

	public static boolean ifIsSistemaExterno(XMethod method)
			throws XDocletException, Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);

		String sistema = atributosMetodoVO.getSistema();

		return !FrameworkAcheUtil.isNullOrEmpty(sistema);
	}

	/**
	 * Analisa de determinado metodo deve aparecer na tela de consulta
	 * 
	 * @throws Wj2eeException
	 */
	public static boolean ifIsConsultavel(XMethod method)
			throws XDocletException, Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);

		String visivel = atributosMetodoVO.getVisivel();

		return visivel.equals("true");
	}

	public static boolean ifIsPk(XMethod method) throws XDocletException,
			Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		return atributosMetodoVO.getPk().equals("true");
	}

	public static boolean ifIsPropriedadeTratavel(XClass class1)
			throws XDocletException {
		return (isInstanceOf(class1, CLASS_NAME_BASEVO)
				|| isInstanceOf(class1, CLASS_NAME_COLLECTION)
				|| isInstanceOf(class1, CLASS_NAME_DATE)
				|| isInstanceOf(class1, CLASS_NAME_NUMBER) || isInstanceOf(
				class1, CLASS_NAME_STRING));
	}

	public static boolean ifIsPropriedadeTratavelNaoTipoVO(XClass class1)
			throws XDocletException {

		if (class1 == null) {
			return false;
		}

		return (isInstanceOf(class1, CLASS_NAME_STRING)
				|| isInstanceOf(class1, CLASS_NAME_NUMBER)
				|| isInstanceOf(class1, CLASS_NAME_DATE) || class1
				.isPrimitive());
	}

	public static boolean ifIsPropriedadeTratavelTipoVO(XClass class1) {
		return (isInstanceOf(class1, CLASS_NAME_BASEVO));
		// return (true);
	}

	public static boolean ifIsPropriedadeTratavelTipoCollection(XClass class1) {
		return isInstanceOf(class1, CLASS_NAME_COLLECTION);
	}

	public static boolean ifIsPropriedadeTratavelTipoVO(XMethod method) {
		XClass class1 = xClassDoMetodoAcessor(method);
		return isInstanceOf(class1, CLASS_NAME_BASEVO);
	}

	public static XClass xClassDoMetodoAcessor(XMethod method) {
		XClass class1 = null;
		class1 = method.getReturnType().getType();
		return class1;
	}

	/**
	 * TESTA SE É UM ACESSOR PARA COLLECTION OU VO
	 * 
	 * @throws Wj2eeException
	 */
	public static boolean ifIsMetodoParaRelacionamento(XMethod method,
			XJavaDoc javaDoc) throws XDocletException, Wj2eeException {
		XClass class1 = classeDoMetodo(method, javaDoc);
		if (ifIsPropriedadeTratavelTipoVO(class1)
				|| ifIsPropriedadeTratavelTipoCollection(class1)) {
			return true;

		} else {
			return false;

		}
	}

	/**
	 * TESTA SE É UM ACESSOR PARA COLLECTION OU VO
	 */
	public static boolean ifIsMetodoParaRelacionamentoMuitos(XMethod method) {
		XClass class1 = xClassDoMetodoAcessor(method);

		if (ifIsPropriedadeTratavelTipoCollection(class1)) {
			return true;

		} else {
			return false;

		}
	}

	/**
	 * TESTA SE É UM ACESSOR PARA COLLECTION OU VO
	 */
	public static boolean ifIsMetodoParaRelacionamentoUm(XMethod method) {
		XClass class1 = xClassDoMetodoAcessor(method);

		if (ifIsPropriedadeTratavelTipoVO(class1)) {
			return true;

		} else {
			return false;

		}
	}

	public static String entidadeDoMetodo(XMethod method, XJavaDoc javaDoc)
			throws Wj2eeException {

		XClass class1 = classeDoMetodo(method, javaDoc);

		String entidadeDoMetodo = class1.getName();

		return entidadeDoMetodo;

	}

	public static String entidadeDaClasse(XClass class1) {

		String entidadeDaClasse = class1.getName();
		entidadeDaClasse = entidadeDaClasse.replaceFirst("VO", "");

		return entidadeDaClasse;
	}

	public static String voClassName(XClass class1) throws Wj2eeException {
		String entidate = getAtributosClasseVO(class1).getEntidade();
		return entidate + "VO";
	}

	public static String moduleVLHConstant(Properties attributes)
			throws Wj2eeException {

		String entidate = attributes.getProperty(ATTRIBUTE_ENTIDADE);
		return "MODULE_" + entidate.toUpperCase() + "_VLH";
	}

	public static String moduleVLHConstant(XClass class1) throws Wj2eeException {
		String entidate = getAtributosClasseVO(class1).getEntidade();

		return "MODULE_" + entidate.toUpperCase() + "_VLH";
	}

	public static String moduleConstant(XClass class1) throws Wj2eeException {
		String entidate = getAtributosClasseVO(class1).getEntidade();
		return "MODULE_" + entidate.toUpperCase();
	}

	public static String moduleConstant(Properties attributes)
			throws Wj2eeException {
		String entidate = attributes.getProperty(ATTRIBUTE_ENTIDADE);
		return "MODULE_" + entidate.toUpperCase();
	}

	public static String hibernateDAOFullClassName(XClass class1)
			throws Wj2eeException {
		String pacote = class1.getContainingPackage().getName();
		String entidate = getAtributosClasseVO(class1).getEntidade();

		return pacote + "." + entidate + "HibernateDAO";
	}

	public static String denifirVariavelParaTipo(Properties attributes) {
		String returnValue = attributes.getProperty(ATTRIBUTE_TIPO);

		return denifirVariavelParaTipo(returnValue);
	}

	public static String simpleNameOfClass(String fullName) {
		fullName = fullName.substring(fullName.lastIndexOf(".") + 1);
		return fullName;
	}

	public static String denifirVariavelParaTipo(String returnValue) {

		returnValue = simpleNameOfClass(returnValue);

		String primeiraLetra = returnValue.substring(0, 1);
		String restante = returnValue.substring(1);

		returnValue = primeiraLetra.toLowerCase() + restante;

		return returnValue;
	}

	public static String moduleId(XClass class1) throws Wj2eeException {
		String entidate = getAtributosClasseVO(class1).getEntidade();
		return entidate;
	}

	public static String moduleIdPrimLetraMaiuscula(XClass class1)
			throws Wj2eeException {
		String entidate = getAtributosClasseVO(class1).getEntidade();

		entidate = primeiraLetraMaiuscula(entidate);

		return entidate;
	}

	public static String moduleVLHId(XClass class1) throws Wj2eeException {
		String moduleVLHId = moduleId(class1);
		moduleVLHId += "Vlh";

		return moduleVLHId;
	}

	public static String ejbHomeModule(XClass class1) throws Wj2eeException {
		// br.com.dlp.cursoache.curso.CursoEJBHome

		String ejbHomeModule;

		String moduleId = moduleId(class1);
		String packageModule = class1.getContainingPackage().getName();

		ejbHomeModule = packageModule + "." + moduleId + "EJBHome";

		return ejbHomeModule;
	}

	public static String ejbHomeModuleLocal(XClass class1)
			throws Wj2eeException {
		// br.com.dlp.cursoache.curso.CursoEJBHome

		String ejbHomeModule;

		String moduleId = moduleId(class1);
		String packageModule = class1.getContainingPackage().getName();

		ejbHomeModule = packageModule + "." + moduleId + "EJBLocalHome";

		return ejbHomeModule;
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public static String ejbVLHHomeModule(XClass class1) throws Wj2eeException {
		// br.com.dlp.cursoache.curso.CursoVLHEJBHome
		String ejbVLHHomeModule;

		String moduleVLHId = moduleVLHId(class1);
		String packageModule = class1.getContainingPackage().getName();

		ejbVLHHomeModule = packageModule + "." + moduleVLHId + "EJBHome";

		return ejbVLHHomeModule;
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public static String jdniModule(XClass class1) throws Wj2eeException {
		// br/com/dlp/cursoache/curso/Curso
		String jdniModule = ejbHomeModule(class1);

		jdniModule = jdniModule.replace('.', '/');

		jdniModule = jdniModule.replaceAll("EJBHome", "");

		return jdniModule;
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public static String jdniModuleLocal(XClass class1) throws Wj2eeException {
		// br/com/dlp/cursoache/curso/Curso
		String jdniModule = ejbHomeModule(class1);

		jdniModule = jdniModule.replace('.', '/');

		jdniModule = jdniModule.replaceAll("EJBLocalHome", "");

		return jdniModule;
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public static String jdniVLHModule(XClass class1) throws Wj2eeException {
		// br/com/dlp/cursoache/curso/CursoVLH
		String jdniVLHModule = ejbVLHHomeModule(class1);

		jdniVLHModule = jdniVLHModule.replace('.', '/');

		jdniVLHModule = jdniVLHModule.replaceAll("EJBHome", "");

		return jdniVLHModule;
	}

	public static String ejbModule(XClass class1) throws Wj2eeException {
		// br.com.dlp.cursoache.curso.CursoEJBHome
		String ejbHomeModule;
		String moduleId = moduleId(class1);

		ejbHomeModule = moduleId + "EJB";
		return ejbHomeModule;
	}

	public static String pacoteModulo(XClass class1) throws Wj2eeException {
		String pacoteModulo = class1.getContainingPackage().getName();
		return pacoteModulo;
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public static String ejbVLHModule(XClass class1) throws Wj2eeException {
		// br.com.dlp.cursoache.curso.CursoVLHEJBHome
		String ejbVLHHomeModule;

		String moduleVLHId = moduleVLHId(class1);

		ejbVLHHomeModule = moduleVLHId + "EJB";

		return ejbVLHHomeModule;
	}

	public static String cadastroTileName(XClass class1) throws Wj2eeException {

		String moduleId = moduleId(class1).toLowerCase();
		String returnValue = moduleId + ".tile";

		return returnValue;
	}

	public static String consultaTileName(XClass class1) throws Wj2eeException {

		String moduleId = moduleId(class1);
		String returnValue = "consulta" + moduleId + ".tile";

		return returnValue;
	}

	public static String pesquisaTileName(XClass class1) throws Wj2eeException {

		String moduleId = moduleId(class1);
		String returnValue = "pesquisa" + moduleId + ".tile";

		return returnValue;
	}

	public static String actionPath(XClass class1, XJavaDoc javaDoc)
			throws Wj2eeException {

		XClass voMestre = classeMestre(class1, javaDoc);

		String moduleLower = moduleId(voMestre).toLowerCase();

		String returnValue = "/" + moduleLower + "/" + moduleLower;

		return returnValue;

	}

	public static String actionFormName(XClass class1) throws Wj2eeException {

		String moduleLower = moduleId(class1).toLowerCase();

		String returnValue = moduleLower + "Form";

		return returnValue;

	}

	public static String actionFormNameMestre(XClass class1, XJavaDoc javaDoc)
			throws Wj2eeException {
		XClass mestre = classeMestre(class1, javaDoc);

		String formMestre = actionFormName(mestre);

		return formMestre;
	}

	public static String moduleDescription(XClass class1) throws Wj2eeException {
		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);
		return atributosClasseVO.getDescricao();
	}

	public static String moduleNome(XClass class1) throws Wj2eeException {
		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);
		return atributosClasseVO.getNome();
	}

	public static String moduleUc(XClass class1) throws Wj2eeException {
		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);
		return atributosClasseVO.getUc();
	}

	public static String fullModuleNome(XClass class1, XJavaDoc javaDoc)
			throws Wj2eeException {

		String nomeModulo = moduleNome(class1);

		if (!ifIsMestre(class1)) {

			XClass classeMestre = classeMestre(class1, javaDoc);

			nomeModulo = moduleNome(classeMestre) + " / " + nomeModulo;

		}

		return nomeModulo;

	}

	public static String moduleWebDirectory(XClass class1)
			throws Wj2eeException {

		String moduleWebDirectory = class1.getContainingPackage().getName();

		int lastIndexOf = moduleWebDirectory.lastIndexOf(".");

		moduleWebDirectory = moduleWebDirectory.substring(lastIndexOf + 1);

		moduleWebDirectory = "/" + moduleWebDirectory;

		return moduleWebDirectory;
	}

	public static String moduleCadastroJSPPage(XClass class1)
			throws Wj2eeException {
		String moduleWebDirectory = entidadeDaClasse(class1);
		moduleWebDirectory = "cadastro" + moduleWebDirectory + ".jsp";
		return moduleWebDirectory;
	}

	public static String moduleConsultaJSPPage(XClass class1)
			throws Wj2eeException {
		String moduleWebDirectory = entidadeDaClasse(class1);
		moduleWebDirectory = "consulta" + moduleWebDirectory + ".jsp";
		return moduleWebDirectory;
	}

	public static String modulePesquisaJSPPage(XClass class1)
			throws Wj2eeException {
		String moduleWebDirectory = entidadeDaClasse(class1);
		moduleWebDirectory = "pesquisa" + moduleWebDirectory + ".jsp";
		return moduleWebDirectory;
	}

	public static String moduleJavaScriptFile(XClass class1)
			throws Wj2eeException {
		String jsFile = "javaScript" + entidadeDaClasse(class1) + ".js";
		return jsFile;
	}

	public static String conteudoColunaListagemTelaPesquisa(XMethod method,
			XJavaDoc javaDoc) throws Wj2eeException {
		String conteudo;

		CodeGeneratorFactory codeGeneratorFactory = CodeGeneratorFactory
				.getInstance();
		AbstractStrutsCodeGenerator abstractStrutsCodeGenerator = (AbstractStrutsCodeGenerator) codeGeneratorFactory
				.getCodeGenerator(method,
						TagHandlerUtil.TIPO_CONTROLE_CONSULTA, javaDoc);
		abstractStrutsCodeGenerator.setTipoControle("null");

		String propertyName = method.getPropertyName();

		abstractStrutsCodeGenerator.setName("basevo");
		abstractStrutsCodeGenerator.setProperty(propertyName);

		/* estou definindo o valor de ignore de bean:write do struts */
		abstractStrutsCodeGenerator.setIgnore("true");
		/* estou definindo que o atributo 'ignore' deverá ser considerado */
		abstractStrutsCodeGenerator.getDefaultAttributes().add(
				AbstractStrutsCodeGenerator.ATT_IGNORE);
		abstractStrutsCodeGenerator.getDefaultAttributes().add(
				AbstractStrutsCodeGenerator.ATT_FORMAT_KEY);

		conteudo = abstractStrutsCodeGenerator.generateCode();

		return conteudo;
	}

	public static String labelKeyProperty(XMethod method) throws Wj2eeException {
		XClass class1 = method.getContainingClass();
		return labelKeyProperty(class1, method);
	}

	public static String validacaoCampoRequerido(XMethod method,
			XJavaDoc javaDoc, Properties attributes) throws Wj2eeException {

		String sufix = attributes.getProperty(ATTRIBUTE_SUFIX);
		if (sufix == null) {
			sufix = "";
		}

		XClass classeDoMetodo = method.getPropertyType().getType();
		String propertyName = method.getPropertyName() + sufix;
		String validacaoCampoRequerido = "";

		/*
		 * --->
		 * if(FrameworkAcheUtil.isNullOrEmptyOrZero(atividadeVO.getDescricao())){
		 * XYZ XYZ XYZ XYZ XYZ }
		 */

		/** ARGUMENTO 'VALIDACAO TIPO' * */
		if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_BASEVO)) {
			validacaoCampoRequerido = "FrameworkAcheUtil.isNullOrEmptyOrZero("
					+ propertyName + ")";

		} else if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_COLLECTION)) {
			validacaoCampoRequerido = "FrameworkAcheUtil.isNullOrEmptyOrZero("
					+ propertyName + ")  && " + propertyName + ".size()>0 ";

		} else if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_DATE)) {
			validacaoCampoRequerido = "FrameworkAcheUtil.isNullOrEmptyOrZero("
					+ propertyName + ")";

		} else if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_NUMBER)) {
			validacaoCampoRequerido = "FrameworkAcheUtil.isNullOrEmptyOrZero("
					+ propertyName + ")";

		} else if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_STRING)) {
			validacaoCampoRequerido = "FrameworkAcheUtil.isNullOrEmptyOrZero("
					+ propertyName + ")";

		} else {
			validacaoCampoRequerido = "FrameworkAcheUtil.isNullOrEmptyOrZero("
					+ propertyName + ")";

		}

		return validacaoCampoRequerido;

	}

	public static String validacaoValorMaximo(XMethod method, XJavaDoc javaDoc,
			Properties attributes) throws Wj2eeException {
		String sufix = attributes.getProperty(ATTRIBUTE_SUFIX);
		if (sufix == null) {
			sufix = "";
		}

		XClass classeDoMetodo = method.getPropertyType().getType();
		String propertyName = method.getPropertyName() + sufix;

		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		String validacaoValor = "";

		/*
		 * --->
		 * if(FrameworkAcheUtil.isNullOrEmptyOrZero(atividadeVO.getDescricao())){
		 * XYZ XYZ XYZ XYZ XYZ }
		 */
		String valorMaximo = valorParaComparacao(atributosMetodoVO
				.getTamanhoMaximo(), method, javaDoc);

		/** ARGUMENTO 'VALIDACAO TIPO' * */
		if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_COLLECTION)) {
			validacaoValor = propertyName + ".size()>" + valorMaximo
					+ ".longValue()";

		} else if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_DATE)) {
			validacaoValor = propertyName + ".after(" + valorMaximo + ")";

		} else if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_NUMBER)) {
			validacaoValor = propertyName + ".doubleValue()>" + valorMaximo
					+ "";

		} else if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_STRING)) {
			validacaoValor = propertyName + ".length()>" + valorMaximo
					+ ".longValue()";

		} else {
			validacaoValor = propertyName + ">" + valorMaximo + "";
			validacaoValor += validacaoValor
					+ "\n"
					+ "TODO: NAO FOI POSSIVEL IDENTIFICAR O TIPO DA PROPRIEDADE '"
					+ propertyName
					+ "'. \n "
					+ "CERTIFIQUE-SE DE QUE ESTE COMANDO ESTA COERENTE COM A REGRA DE VALIDACAO!!";
		}

		return validacaoValor;

	}

	public static String validacaoValorMinimo(XMethod method, XJavaDoc javaDoc,
			Properties attributes) throws Wj2eeException {
		String sufix = attributes.getProperty(ATTRIBUTE_SUFIX);
		if (sufix == null) {
			sufix = "";
		}

		XClass classeDoMetodo = method.getPropertyType().getType();
		String propertyName = method.getPropertyName() + sufix;

		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		String validacaoValor = "";

		/*
		 * --->
		 * if(FrameworkAcheUtil.isNullOrEmptyOrZero(atividadeVO.getDescricao())){
		 * XYZ XYZ XYZ XYZ XYZ }
		 */
		String valorMinimo = valorParaComparacao(atributosMetodoVO
				.getTamanhoMinimo(), method, javaDoc);

		/** ARGUMENTO 'VALIDACAO TIPO' * */
		if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_COLLECTION)) {
			validacaoValor = propertyName + ".size()<" + valorMinimo
					+ ".longValue()";

		} else if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_DATE)) {
			validacaoValor = propertyName + ".before(" + valorMinimo + ")";

		} else if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_NUMBER)) {
			validacaoValor = propertyName + ".doubleValue()<" + valorMinimo;

		} else if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_STRING)) {
			validacaoValor = propertyName + ".length()<" + valorMinimo
					+ ".longValue()";

		} else {
			validacaoValor = propertyName + "<" + valorMinimo;
			validacaoValor += validacaoValor
					+ "\n"
					+ "TODO: NAO FOI POSSIVEL IDENTIFICAR O TIPO DA PROPRIEDADE '"
					+ propertyName
					+ "'. \n "
					+ "CERTIFIQUE-SE DE QUE ESTE COMANDO ESTA COERENTE COM A REGRA DE VALIDACAO!!";
		}

		return validacaoValor;

	}

	public static String validacaoValorRange(XMethod method, XJavaDoc javaDoc,
			Properties attributes) throws Wj2eeException {
		String sufix = attributes.getProperty(ATTRIBUTE_SUFIX);
		if (sufix == null) {
			sufix = "";
		}

		XClass classeDoMetodo = method.getPropertyType().getType();
		String propertyName = method.getPropertyName() + sufix;

		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		String validacaoValor = "";

		/*
		 * --->
		 * if(FrameworkAcheUtil.isNullOrEmptyOrZero(atividadeVO.getDescricao())){
		 * XYZ XYZ XYZ XYZ XYZ }
		 */
		String valorMaximo = valorParaComparacao(atributosMetodoVO
				.getTamanhoMaximo(), method, javaDoc);
		String valorMinimo = valorParaComparacao(atributosMetodoVO
				.getTamanhoMinimo(), method, javaDoc);

		/** ARGUMENTO 'VALIDACAO TIPO' * */
		if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_COLLECTION)) {
			validacaoValor = propertyName + ".size()<" + valorMinimo
					+ ".longValue() || " + propertyName + ".size()>"
					+ valorMaximo + ".longValue()";

		} else if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_DATE)) {
			validacaoValor = propertyName + ".before(" + valorMinimo + ") || "
					+ propertyName + ".after(" + valorMaximo + ")";

		} else if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_NUMBER)) {
			validacaoValor = propertyName + ".doubleValue()<" + valorMinimo
					+ ".doubleValue() || " + propertyName + ".doubleValue()>"
					+ valorMaximo + ".doubleValue()";

		} else if (classeDoMetodo.isA(TagHandlerUtil.CLASS_NAME_STRING)) {
			validacaoValor = propertyName + ".length()<" + valorMinimo
					+ ".longValue() || " + propertyName + ".length()>"
					+ valorMaximo + ".longValue()";

		} else {
			validacaoValor = propertyName + "<" + valorMinimo + " || "
					+ propertyName + ">" + valorMaximo + "";
			validacaoValor += validacaoValor
					+ "\n"
					+ "TODO: NAO FOI POSSIVEL IDENTIFICAR O TIPO DA PROPRIEDADE '"
					+ propertyName
					+ "'. \n "
					+ "CERTIFIQUE-SE DE QUE ESTE COMANDO ESTA COERENTE COM A REGRA DE VALIDACAO!!";
		}

		return validacaoValor;

	}

	public static String validacaoValorDelimitado(XMethod method,
			XJavaDoc javaDoc, Properties attributes) throws Wj2eeException {
		String validacaoValorDelimitado;

		if (ifIsValorDelimitadoMinimo(method)
				&& ifIsValorDelimitadoMaximo(method)) {
			validacaoValorDelimitado = validacaoValorRange(method, javaDoc,
					attributes);

		} else if (ifIsValorDelimitadoMinimo(method)) {
			validacaoValorDelimitado = validacaoValorMinimo(method, javaDoc,
					attributes);

		} else if (ifIsValorDelimitadoMaximo(method)) {
			validacaoValorDelimitado = validacaoValorMaximo(method, javaDoc,
					attributes);

		} else {
			validacaoValorDelimitado = "??? /*TODO:ESTE CAMPO NÃO POSSUI NENHUM TIPO DE VALIDACAO DE VALORES? */ ???";

		}

		return validacaoValorDelimitado;

	}

	/**
	 * Monta o comando java para criacao de um objeto para comparacao num
	 * processo de validacao
	 * 
	 * @throws ParseException
	 */
	public static String valorParaComparacao(String valor, XMethod method,
			XJavaDoc javaDoc) throws Wj2eeException {
		XClass class1 = method.getPropertyType().getType();

		String valueToCompare = "";

		if (class1.isA(TagHandlerUtil.CLASS_NAME_COLLECTION)) {
			/** ************* */
			/* COLLECTION */
			/** ************* */
			valueToCompare = "new Long(" + valor + ")";

		} else if (class1.isA(TagHandlerUtil.CLASS_NAME_DATE)) {
			/** ************* */
			/* DATA */
			/** ************* */

			/* recupera a formatacao de data do wizard */
			String formatoData = getFormatoData(javaDoc);

			/* cria o formatador */
			SimpleDateFormat sdf = new SimpleDateFormat(formatoData);

			/* parseia o valor */
			Date date;
			try {
				date = sdf.parse(valor);
			} catch (ParseException e) {
				throw new Wj2eeException(e,
						"Nao foi possivel parsear o valor '" + valor
								+ "' para data");

			}
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTime(date);

			valueToCompare = "new GregorianCalendar("
					+ gregorianCalendar.get(Calendar.YEAR) + ","
					+ gregorianCalendar.get(Calendar.MONTH) + ","
					+ gregorianCalendar.get(Calendar.DAY_OF_MONTH)
					+ ").getTime()";

		} else if (class1.isA(TagHandlerUtil.CLASS_NAME_NUMBER)) {
			/** ************* */
			/* NUMBER */
			/** ************* */
			String numberChildClass = method.getPropertyType().getType()
					.getName();
			valueToCompare = "new " + numberChildClass + "(" + valor + ")";

		} else if (class1.isA(TagHandlerUtil.CLASS_NAME_STRING)) {
			/** ************* */
			/* STRING */
			/** ************* */
			valueToCompare = "new Long(" + valor + ")";

		} else {
			/** ************************** */
			/* OUTRA - EXPRESSAO LITERAL */
			/** ************************** */
			valueToCompare = valor + "/*valor especificado diretamente*/";

		}

		return valueToCompare;
	}

	public static String labelKeyProperty(XClass class1, XMethod method)
			throws Wj2eeException {
		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);
		String entidade = atributosClasseVO.getEntidade();

		String returnValue = entidade + "." + method.getPropertyName();
		returnValue = returnValue + ".label";
		return returnValue;
	}

	public static String dicaKeyProperty(XMethod method) throws Wj2eeException {
		XClass class1 = method.getContainingClass();
		return dicaKeyProperty(class1, method);
	}

	public static String dicaKeyProperty(XClass class1, XMethod method)
			throws Wj2eeException {
		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);
		String entidade = atributosClasseVO.getEntidade();

		String returnValue = entidade + "." + method.getPropertyName();

		returnValue = returnValue + ".dica";
		return returnValue;
	}

	public static String labelValueProperty(XMethod method)
			throws Wj2eeException {
		String returnValue = "";
		returnValue = descricao(method);
		return returnValue;
	}

	public static String dicaValueProperty(XMethod method)
			throws Wj2eeException {
		String returnValue = "";
		returnValue = dica(method);
		return returnValue;
	}

	public static String mascaraView(XMethod method) throws Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		return atributosMetodoVO.getMaskView();
	}

	public static String tamanhoMinimo(XMethod method) throws Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		return atributosMetodoVO.getTamanhoMinimo();
	}

	public static String tamanhoMaximo(XMethod method) throws Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		return atributosMetodoVO.getTamanhoMaximo();
	}

	public static boolean ifIsRequerido(XMethod method)
			throws XDocletException, Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);

		boolean requerido = atributosMetodoVO.getRequerido().equals("true");
		boolean ifIsPk = ifIsPk(method);

		return requerido && !ifIsPk;
	}

	public static boolean ifIsValorDelimitado(XMethod method)
			throws Wj2eeException {

		boolean returnValue = ifIsValorDelimitadoMaximo(method)
				|| ifIsValorDelimitadoMinimo(method);

		return returnValue;
	}

	public static boolean ifIsValorDelimitadoMinimo(XMethod method)
			throws Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		boolean valorDelimitadoMinimo = false;

		String tamanhoMinimo = atributosMetodoVO.getTamanhoMinimo();

		if (tamanhoMinimo != null && !tamanhoMinimo.trim().equals("")) {
			valorDelimitadoMinimo = true;
		}

		return valorDelimitadoMinimo;

	}

	public static boolean ifIsValorDelimitadoMaximo(XMethod method)
			throws Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		boolean valorDelimitadoMaximo = false;

		String tamanhoMaximo = atributosMetodoVO.getTamanhoMaximo();

		if (tamanhoMaximo != null && !tamanhoMaximo.trim().equals("")) {
			valorDelimitadoMaximo = true;
		}

		return valorDelimitadoMaximo;
	}

	public static String descricao(XMethod method) throws Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		return atributosMetodoVO.getDescricao();
	}

	public static String dica(XMethod method) throws Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		return atributosMetodoVO.getDica();
	}

	public static boolean somenteLeitura(XMethod method) throws Wj2eeException {

		boolean somenteLeitura = false;

		if (method.getDoc().hasTag(TagHandlerUtil.W2JEE_NAMESPACE)) {
			AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
			somenteLeitura = atributosMetodoVO.getSomenteLeitura().equals(
					"true");
		}

		return somenteLeitura;
	}

	public static boolean ignorar(XMethod method) throws Wj2eeException {

		boolean ignorar = false;

		if (method.getDoc().hasTag(TagHandlerUtil.W2JEE_NAMESPACE)) {
			AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
			ignorar = atributosMetodoVO.getIgnorar().equals("true");
		}

		return ignorar;
	}

	public static String renderer(XMethod method) throws Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		return atributosMetodoVO.getRenderer();
	}

	public static String renderControl(String tipoControle, XMethod method,
			XJavaDoc javaDoc, String controlSufix) throws Wj2eeException,
			XDocletException {

		CodeGeneratorFactory codeGeneratorFactory;
		codeGeneratorFactory = CodeGeneratorFactory.getInstance();
		ICodeGenerator codeGenerator = codeGeneratorFactory.getCodeGenerator(
				method, tipoControle, javaDoc);
		codeGenerator.setXJavaDoc(javaDoc);
		codeGenerator.setTipoControle(tipoControle);
		codeGenerator.setControlSufix(controlSufix);

		return codeGenerator.generateCode();

	}

	public static String multiLinguaNomeKey(XClass currentClass)
			throws Wj2eeException {
		String multiLinguaNomeKey = currentClass.getName();
		multiLinguaNomeKey = multiLinguaNomeKey + ".nome";
		return multiLinguaNomeKey;
	}

	public static String multiLinguaNomevalue(XClass currentClass)
			throws Wj2eeException {
		return getAtributosClasseVO(currentClass).getNome();
	}

	public static String multiLinguaDescricaoKey(XClass currentClass) {
		String multiLinguaNomeKey = currentClass.getName();
		multiLinguaNomeKey = multiLinguaNomeKey + ".descricao";
		return multiLinguaNomeKey;
	}

	public static String multiLinguaDescricaovalue(XClass currentClass)
			throws Wj2eeException {
		return getAtributosClasseVO(currentClass).getDescricao();
	}

	/**
	 * Identifica qual a classe da propriedade. <br>
	 * Caso seja instanceOf Collection, tenta descobir pelo atributo 'classe'
	 */
	public static XClass classeDoMetodo(XMethod method, XJavaDoc javaDoc)
			throws Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		XClass class1 = method.getPropertyType().getType();

		/* testa se não é instanceof collection */
		if (!isInstanceOf(class1, CLASS_NAME_COLLECTION)) {
			return class1;
		}

		String classe = atributosMetodoVO.getClasse();

		class1 = javaDoc.getXClass(classe);

		if (class1 == null) {
			throw new Wj2eeException(
					"Não foi possivel criar um objeto XClass para a classe de nome:'"
							+ classe + "'. "
							+ "Verifique no atrributo 'class' do método "
							+ method.getName()
							+ " se o nome completo da classe está correto!!");
		}

		return class1;
	}

	/**
	 * Transforma um nome qualquer para o mesmo nome no Singular
	 */
	public static String toSingular(String nome) {
		String ultimaLetra = nome.substring(nome.length() - 1);

		if (ultimaLetra.equalsIgnoreCase("s")) {
			nome = nome.substring(0, nome.length() - 1);
			nome = toSingular(nome);
		}

		return nome;
	}

	/**
	 * Transforma um nome qualquer para o mesmo nome no Plural
	 */
	public static String toPlural(String nome) {
		String ultimaLetra = nome.substring(nome.length() - 1);

		if (!ultimaLetra.equalsIgnoreCase("s")) {
			nome = nome + "s";
		}

		return nome;
	}

	public static String getNomeDaColecaoNoSingular(XMethod method)
			throws Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);

		String nomeDaColecaoNoSingular = atributosMetodoVO.getClasse();

		return getNomeDaColecaoNoSingular(nomeDaColecaoNoSingular);
	}

	public static String getNomeDaColecaoNoSingular(
			String nomeDaColecaoNoSingular) throws Wj2eeException {

		nomeDaColecaoNoSingular = nomeDaColecaoNoSingular
				.substring(nomeDaColecaoNoSingular.lastIndexOf(".") + 1);
		nomeDaColecaoNoSingular = nomeDaColecaoNoSingular
				.replaceFirst("VO", "");
		nomeDaColecaoNoSingular = denifirVariavelParaTipo(nomeDaColecaoNoSingular);
		nomeDaColecaoNoSingular = toSingular(nomeDaColecaoNoSingular);

		return nomeDaColecaoNoSingular;
	}

	public static String methodTypeFilter(XMethod method)
			throws XDocletException {
		XClass class1 = method.getPropertyType().getType();
		String returnValue;

		if (isInstanceOf(class1, CLASS_NAME_COLLECTION)) {
			/* Qualque colecao vira List no form */
			returnValue = List.class.getName();
		} else {
			returnValue = class1.getName();
		}

		return returnValue;
	}

	public static String primeiraLetraMaiuscula(String nome) {

		String primeiraLetra = nome.substring(0, 1).toUpperCase();
		String resto = nome.substring(1);

		return primeiraLetra + resto;
	}

	public static String primeiraLetraMinuscula(String nome) {

		String primeiraLetra = nome.substring(0, 1).toLowerCase();
		String resto = nome.substring(1);

		return primeiraLetra + resto;
	}

	public static String idEntidadeSelecionado(String entidade) {

		entidade = toSingular(entidade);
		entidade = primeiraLetraMaiuscula(entidade);

		return "id" + entidade + "Selecionado";
	}

	public static String extendedActionFormClassName(
			DocletContext docletContext, XClass class1) throws Wj2eeException {

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);
		String entidate;

		if (!atributosClasseVO.getBaseentity().equals("true")) {
			/* A ENTIDADE EM QUESTÃO !!!NÃO!!! É A ENTIDADE BASE */
			entidate = getAtributosClasseVO(class1.getSuperclass())
					.getEntidade()
					+ "Form";
		} else {
			/* A ENTIDADE EM QUESTÃO !!É!! A ENTIDADE BASE */
			String baseActionFormClass = (String) docletContext
					.getConfigParam("baseActionFormClass");

			entidate = baseActionFormClass;

		}

		return entidate;
	}

	public static String extendedVOWrapperClassName(
			DocletContext docletContext, XClass class1) throws Wj2eeException {

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);
		String entidate;

		if (!atributosClasseVO.getBaseentity().equals("true")) {
			/* A ENTIDADE EM QUESTÃO !!!NÃO!!! É A ENTIDADE BASE */
			entidate = getAtributosClasseVO(class1.getSuperclass())
					.getEntidade()
					+ "VOWrapper";
		} else {
			/* A ENTIDADE EM QUESTÃO !!É!! A ENTIDADE BASE */
			String strClass = (String) docletContext
					.getConfigParam("baseVOWrapperClass");

			entidate = strClass;

		}

		return entidate;
	}

	public static String extendedActionClassName(DocletContext docletContext,
			XClass class1) throws Wj2eeException {
		String entidate;

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);

		if (!atributosClasseVO.getBaseentity().equals("true")) {
			entidate = getAtributosClasseVO(class1.getSuperclass())
					.getEntidade()
					+ "Action";

		} else {
			String baseActionClass = (String) docletContext
					.getConfigParam("baseActionClass");
			entidate = baseActionClass;
		}
		return entidate;
	}

	public static String extendedFacadeImplClassName(
			DocletContext docletContext, XClass class1) throws Wj2eeException {
		String entidate;

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);

		if (!atributosClasseVO.getBaseentity().equals("true")) {
			entidate = getAtributosClasseVO(class1.getSuperclass())
					.getEntidade()
					+ "FacadeImpl";

		} else {
			String baseFacadeImpl = (String) docletContext
					.getConfigParam("baseFacadeImpl");

			entidate = baseFacadeImpl;
		}
		return entidate;

	}

	public static String extendedFacadeClassName(DocletContext docletContext,
			XClass class1) throws Wj2eeException {

		String entidate;

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);

		if (!atributosClasseVO.getBaseentity().equals("true")) {
			entidate = getAtributosClasseVO(class1.getSuperclass())
					.getEntidade()
					+ "Facade";

		} else {
			String baseFacade = (String) docletContext
					.getConfigParam("baseFacade");

			entidate = baseFacade;
		}
		return entidate;

	}

	public static String extendedVlhFacadeImplClassName(
			DocletContext docletContext, XClass class1) throws Wj2eeException {
		String entidate;

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);

		if (!atributosClasseVO.getBaseentity().equals("true")) {
			entidate = getAtributosClasseVO(class1.getSuperclass())
					.getEntidade()
					+ "VLHFacadeImpl";

		} else {
			String baseFacade = (String) docletContext
					.getConfigParam("baseVLHFacadeImpl");

			entidate = baseFacade;
		}
		return entidate;
	}

	public static String extendedVlhFacadeClassName(
			DocletContext docletContext, XClass class1) throws Wj2eeException {
		String entidate;

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);

		if (!atributosClasseVO.getBaseentity().equals("true")) {
			entidate = getAtributosClasseVO(class1.getSuperclass())
					.getEntidade()
					+ "VLHFacade";

		} else {
			String baseFacade = (String) docletContext
					.getConfigParam("baseVLHFacade");

			entidate = baseFacade;
		}
		return entidate;
	}

	public static String actionFormClassName(XClass class1)
			throws Wj2eeException {

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);
		String entidate;

		/* A ENTIDADE EM QUESTÃO !!!NÃO!!! É A ENTIDADE BASE */
		entidate = atributosClasseVO.getEntidade() + "Form";

		return entidate;
	}

	public static String voWrapperClassName(XClass class1)
			throws Wj2eeException {
		String entidate;

		/* A ENTIDADE EM QUESTÃO !!!NÃO!!! É A ENTIDADE BASE */
		entidate = getAtributosClasseVO(class1).getEntidade() + "VOWrapper";

		return entidate;
	}

	public static String newAcheActionMessageRequerido(XMethod method)
			throws Wj2eeException {

		/*
		 * errors.required={0} é requerido. errors.minlength={0} não pode conter
		 * menos de {1} caracteres. errors.maxlength={0} não pode conter mais de
		 * {1} caracteres. errors.invalid={0} is invalid.
		 * 
		 * errors.byte={0} deve ser um 'byte'. errors.short={0} deve ser um
		 * 'short'. errors.integer={0} deve ser um 'integer'. errors.long={0}
		 * deve ser um 'long'. errors.float={0} deve ser um 'float'.
		 * errors.double={0}deve ser um 'double'.
		 * 
		 * errors.date={0} não é uma data válida. errors.range=O valor de {0}
		 * não está entre os valores {1} e {2}. errors.creditcard={0} é um
		 * número de cartão de crédito inválido. errors.email={0} não é um
		 * e-mail válida.
		 */

		return "new AcheActionMessage(\"errors.required\",\""
				+ TagHandlerUtil.labelKeyProperty(method)
				+ "\",messageResources)";
	}

	public static String tipoValorParaKeyDeMensagem(XMethod method) {

		String tipoValor;

		XClass class1 = method.getPropertyType().getType();

		if (class1.isA(TagHandlerUtil.CLASS_NAME_DATE)) {
			tipoValor = "date.";

		} else if (class1.isA(TagHandlerUtil.CLASS_NAME_NUMBER)) {
			tipoValor = "number.";

		} else if (class1.isA(TagHandlerUtil.CLASS_NAME_COLLECTION)) {
			tipoValor = "collection.";

		} else if (class1.isA(TagHandlerUtil.CLASS_NAME_STRING)) {
			tipoValor = "string.";

		} else {
			/* vou deixar vazio */
			tipoValor = "";

		}

		return tipoValor;
	}

	/**
	 * Filtra todos os metodos de XClass cujo renderer é moduloescravo
	 * 
	 * @throws Wj2eeException
	 */
	public static List metodosModuloEscravos(XClass voClass, XJavaDoc javaDoc,
			boolean sort) throws Wj2eeException {
		/*
		 * instancia um implementacao de Predicate para filtrar da colecao
		 * apenas os methodos de renderer = modulo escrado
		 */
		Predicate predicate = new MetodosModuloEscravo();
		List methods = voClass.getMethods(predicate, true);

		/* atributo sor */

		if (sort) {
			Comparator comparator = new Wj2eeXMethodComparator();

			/*
			 * essa coisa bizarra serve para efetuar uma cópia da colecao pq a
			 * colecao original não pode ser ordenada
			 */
			methods = new ArrayList(methods);

			Collections.sort(methods, comparator);

		}

		return methods;
	}

	public static String newAcheActionMessageValorDelimitado(XMethod method,
			XJavaDoc javaDoc) throws Wj2eeException {

		AtributosMetodoVO atributosMetodoVO = new AtributosMetodoVO(method);

		String messageErrorKey = "";
		String valorDe = "";
		String labelPropertyKey = "";
		String valorAte = "";

		String strParametros = "";
		List parametros = new ArrayList(4);

		String tamanhoMinimo = "" + atributosMetodoVO.getTamanhoMinimo();
		String tamanhoMaximo = "" + atributosMetodoVO.getTamanhoMaximo();

		String tipoValor = tipoValorParaKeyDeMensagem(method);

		/* LABEL DE MSG DE INCONSISTENCIAS */
		if (!tamanhoMinimo.trim().equals("")
				&& !tamanhoMaximo.trim().equals("")) {
			messageErrorKey = "errors." + tipoValor + "range";

		} else if (!tamanhoMinimo.trim().equals("")
				&& tamanhoMaximo.trim().equals("")) {
			messageErrorKey = "errors." + tipoValor + "minlength";

		} else if (!tamanhoMinimo.trim().equals("")
				&& tamanhoMaximo.trim().equals("")) {
			messageErrorKey = "errors." + tipoValor + "maxlength";

		}
		parametros.add("\"" + messageErrorKey + "\"");

		/* LABEL DA PROPRIEDADE */
		labelPropertyKey = TagHandlerUtil.labelKeyProperty(method);
		parametros.add("\"" + labelPropertyKey + "\"");

		/* VALOR DE */
		if (tamanhoMinimo != null && !tamanhoMinimo.trim().equals("")) {
			valorDe = TagHandlerUtil.valorParaComparacao(tamanhoMinimo, method,
					javaDoc);
			parametros.add(valorDe);
		}

		/* VALOR ATE */
		if (tamanhoMaximo != null && !tamanhoMaximo.trim().equals("")) {
			valorAte = TagHandlerUtil.valorParaComparacao(tamanhoMaximo,
					method, javaDoc);
			parametros.add(valorAte);
		}

		Iterator iterator = parametros.iterator();

		while (iterator.hasNext()) {
			strParametros += iterator.next() + ", ";
		}

		return "new AcheActionMessage(" + strParametros
				+ "messageResources, getResourceBundle(mapping,request))";
	}

	/**
	 * Pesquisa a classe voClass recursivamente até encontrar o voMestre
	 */
	public static XClass classePrimeiroMestre(XClass voClass, XJavaDoc javaDoc)
			throws Wj2eeException {

		XClass returnValue;
		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(voClass);
		String voMestre = atributosClasseVO.getVoMestre();

		if (voMestre == null || voMestre.trim().equals("")) {
			/* não existe indicacao de voMestre */
			returnValue = null;
		} else {
			/* o voMestre foi indicado */
			try {
				returnValue = javaDoc.getXClass(voMestre);

			} catch (Exception e) {
				throw new Wj2eeException(
						"Erro ao tentar identificar a classe pelo qualified name:"
								+ voMestre);
			}
		}

		return returnValue;
	}

	/**
	 * Pesquisa a classe voClass recursivamente até encontrar o voMestre
	 */
	public static XClass classeMestre(XClass voClass, XJavaDoc javaDoc)
			throws Wj2eeException {

		XClass returnValue;
		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(voClass);
		String voMestre = atributosClasseVO.getVoMestre();

		if (voMestre == null || voMestre.trim().equals("")) {
			/* não existe indicacao de voMestre */
			returnValue = voClass;
		} else {
			/* o voMestre foi indicado */
			try {
				returnValue = javaDoc.getXClass(voMestre);

				/* recursivamente peço o vo mestre do vo mestre */
				returnValue = classeMestre(returnValue, javaDoc);

			} catch (Exception e) {
				throw new Wj2eeException(e,
						"Erro ao tentar identificar a classe pelo qualified name:"
								+ voMestre);
			}
		}
		if (returnValue == null) {
			throw new Wj2eeException(
					"Nao foi possivel identificar a classe pelo qualified name:"
							+ voMestre);
		}

		return returnValue;
	}

	public static String actionClassName(XClass voClass, XJavaDoc javaDoc)
			throws Wj2eeException {

		XClass voMestre = TagHandlerUtil.classeMestre(voClass, javaDoc);

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(voMestre);

		String entidade = atributosClasseVO.getEntidade();
		String sufixo = "Action";

		String strActionClassName = entidade + sufixo;

		String returnValue = strActionClassName;

		return returnValue;

	}

	public static String actionFullClassName(XClass voClass, XJavaDoc javaDoc)
			throws Wj2eeException {

		XClass voMestre = TagHandlerUtil.classeMestre(voClass, javaDoc);
		XPackage voMestrePackage = voMestre.getContainingPackage();

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(voMestre);

		String entidade = atributosClasseVO.getEntidade();
		String sufixo = "Action";

		String strActionClassName = entidade + sufixo;
		String strPackage = voMestrePackage.getName();

		String returnValue = strPackage + "." + strActionClassName;

		return returnValue;
	}

	public static String facadeImplClassName(XClass class1, XJavaDoc javaDoc)
			throws Wj2eeException {
		String entidate;

		XClass classeMestre = classeMestre(class1, javaDoc);

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(classeMestre);
		entidate = atributosClasseVO.getEntidade() + "FacadeImpl";
		return entidate;

	}

	public static String facadeClassName(XClass class1, XJavaDoc javaDoc)
			throws Wj2eeException {

		String entidate;

		XClass classeMestre = classeMestre(class1, javaDoc);

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(classeMestre);

		entidate = atributosClasseVO.getEntidade() + "Facade";

		return entidate;

	}

	public static String sessionFacadeClassName(XClass class1)
			throws Wj2eeException {
		String entidate = getAtributosClasseVO(class1).getEntidade();
		return entidate + "SessionFacade";
	}

	public static String vlhFacadeImplClassName(XClass class1)
			throws Wj2eeException {
		String entidate;

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);
		entidate = atributosClasseVO.getEntidade() + "VLHFacadeImpl";
		return entidate;

	}

	public static String vlhFacadeClassName(XClass class1)
			throws Wj2eeException {

		String entidate;

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);
		entidate = atributosClasseVO.getEntidade() + "VLHFacade";
		return entidate;

	}

	public static String vlhSessionFacadeClassName(XClass class1)
			throws Wj2eeException {
		String entidate = getAtributosClasseVO(class1).getEntidade();
		return entidate + "VLHSessionFacade";
	}

	public static boolean ifIsCriarGetterSetterVirtual(XMethod method)
			throws Wj2eeException {
		boolean returnValue;
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		String renderer = atributosMetodoVO.getRenderer();
		if (renderer.equals(RENDERER_CHECKBOX)
				|| renderer.equals(RENDERER_COMBO)
				|| renderer.equals(RENDERER_RADIO)) {
			returnValue = true;

		} else {
			returnValue = false;

		}
		return returnValue;
	}

	public static boolean isInstanceOf(XClass class1, String fullName) {

		if (class1 != null
				&& !class1.getQualifiedName().equals("java.lang.Object")) {

			boolean returValue;
			try {

				returValue = (class1.getQualifiedName().equals(fullName)
						|| class1.isA(fullName)
						|| class1.isSubclassOf(fullName) || class1
						.isImplementingInterface(fullName));
			} catch (Exception e) {
				returValue = false;
			}
			if (returValue) {
				return true;
			} else {
				List list = class1.getSuperInterfaceElements();

				Iterator iterator = list.iterator();
				while (iterator.hasNext()) {

					if (isInstanceOf((XClass) iterator.next(), fullName)) {
						return true;
					}
				}

				return isInstanceOf(class1.getSuperclass(), fullName);
			}
		} else {
			return false;

		}
	}

	public static String propertyNameSemSufixoVO(XMethod method) {
		String propertyNameSemSufixoVO = method.getPropertyName();

		propertyNameSemSufixoVO = propertyNameSemSufixoVO
				.replaceFirst("VO", "");

		return propertyNameSemSufixoVO;

	}

	public static String nomeClasseSemSufixoVO(XClass class1) {
		String nomeClasseSemSufixoVO = class1.getName();
		nomeClasseSemSufixoVO = nomeClasseSemSufixoVO.replaceFirst("VO", "");
		return nomeClasseSemSufixoVO;

	}

	public static String vlhPageSize(XClass class1) throws Wj2eeException {

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);

		String vlhPageSize = atributosClasseVO.getVlhPageSize();

		return vlhPageSize;

	}

	public static String nestedPropertyList(XMethod method)
			throws Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = new AtributosMetodoVO(method);
		return atributosMetodoVO.getNestedPropertyList();

	}

	public static String constanteParaServico(String nomeServico) {
		return "SERVICE_" + nomeServico.toUpperCase();
	}

	public static String constanteParaOrdenacao(String nomeServico) {
		return "ORDERBY_" + nomeServico.toUpperCase();
	}

	public static String servicoFecharItem(XMethod method, XJavaDoc javaDoc)
			throws Wj2eeException {
		XClass classeDoMetodo = classeDoMetodo(method, javaDoc);

		return servicoFecharItem(classeDoMetodo);
	}

	public static String servicoFecharItem(XClass classeModulo)
			throws Wj2eeException {
		AtributosClasseVO atributosClasseVO = new AtributosClasseVO(
				classeModulo);

		String servico = "fechar";

		String renderer = atributosClasseVO.getRenderer();
		if (renderer.equals(AtributosClasseVO.RENDERER_MODULOESCRAVO)) {
			String nomeClasseSemSufix = TagHandlerUtil
					.nomeClasseSemSufixoVO(classeModulo);

			servico = servico + nomeClasseSemSufix;
		}

		return servico;
	}

	public static String servicoIncluirItem(XMethod method, XJavaDoc javaDoc)
			throws Wj2eeException {
		XClass classeDoMetodo = classeDoMetodo(method, javaDoc);
		return servicoIncluirItem(classeDoMetodo);
	}

	public static String servicoIncluirItem(XClass classeModulo)
			throws Wj2eeException {
		String servico = "incluir";

		if (!ifIsMestre(classeModulo)) {

			String nomeClasseSemSufix = TagHandlerUtil
					.nomeClasseSemSufixoVO(classeModulo);
			servico = servico + nomeClasseSemSufix;
		}

		return servico;
	}

	public static String servicoSalvarItem(XMethod method, XJavaDoc javaDoc)
			throws Wj2eeException {
		XClass classDoMetodo = classeDoMetodo(method, javaDoc);
		return servicoSalvarItem(classDoMetodo);
	}

	public static String serviceLocatorFullClassName(XMethod method,
			XJavaDoc javaDoc) throws Wj2eeException {
		String sistema = sistema(method, javaDoc);

		String className = "br.com.dlp." + sistema.toLowerCase()
				+ ".util.servicelocator." + sistema + "ServiceLocator";

		return className;
	}

	public static String sistema(XMethod method, XJavaDoc javaDoc)
			throws Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		String sistema = atributosMetodoVO.getSistema();
		return sistema;
	}

	public static String servicoSalvarItem(XClass classeModulo)
			throws Wj2eeException {
		AtributosClasseVO atributosClasseVO = new AtributosClasseVO(
				classeModulo);

		String servico = "salvar";

		String renderer = atributosClasseVO.getRenderer();
		if (renderer.equals(AtributosClasseVO.RENDERER_MODULOESCRAVO)) {
			String nomeClasseSemSufix = TagHandlerUtil
					.nomeClasseSemSufixoVO(classeModulo);

			servico = servico + nomeClasseSemSufix;
		}

		return servico;
	}

	public static String servicoEditarItem(XMethod method, XJavaDoc javaDoc)
			throws Wj2eeException {
		XClass classeDoMetodo = classeDoMetodo(method, javaDoc);
		return servicoEditarItem(classeDoMetodo);
	}

	public static String servicoEditarItem(XClass classeModulo)
			throws Wj2eeException {
		AtributosClasseVO atributosClasseVO = new AtributosClasseVO(
				classeModulo);

		String servico = "editar";

		String renderer = atributosClasseVO.getRenderer();
		if (renderer.equals(AtributosClasseVO.RENDERER_MODULOESCRAVO)) {
			String nomeClasseSemSufix = TagHandlerUtil
					.nomeClasseSemSufixoVO(classeModulo);

			servico = servico + nomeClasseSemSufix;
		}

		return servico;
	}

	public static String servicoRemoverItem(XClass classeModulo)
			throws Wj2eeException {
		AtributosClasseVO atributosClasseVO = new AtributosClasseVO(
				classeModulo);

		String servico = "remover";

		String renderer = atributosClasseVO.getRenderer();
		if (renderer.equals(AtributosClasseVO.RENDERER_MODULOESCRAVO)
				|| renderer.equals(AtributosClasseVO.RENDERER_INDEXADO)) {

			String nomeClasseSemSufix = TagHandlerUtil
					.nomeClasseSemSufixoVO(classeModulo);

			servico = servico + nomeClasseSemSufix;
		}

		return servico;
	}

	public static String servicoRemoverItem(XMethod method, XJavaDoc javaDoc)
			throws Wj2eeException {
		XClass classeDoMetodo = classeDoMetodo(method, javaDoc);
		return servicoRemoverItem(classeDoMetodo);
	}

	public static String servicoConsultarItem(XClass classeModulo)
			throws Wj2eeException {
		AtributosClasseVO atributosClasseVO = new AtributosClasseVO(
				classeModulo);

		String servico = "consultar";

		String renderer = atributosClasseVO.getRenderer();
		if (renderer.equals(AtributosClasseVO.RENDERER_MODULOESCRAVO)) {
			String nomeClasseSemSufix = TagHandlerUtil
					.nomeClasseSemSufixoVO(classeModulo);

			servico = servico + nomeClasseSemSufix;
		}

		return servico;
	}

	public static String servicoConsultarItem(XMethod method, XJavaDoc javaDoc)
			throws Wj2eeException {
		XClass classeDoMetodo = classeDoMetodo(method, javaDoc);
		return servicoConsultarItem(classeDoMetodo);
	}

	public static String classeEConstanteParaServico(XMethod method,
			String servico, XJavaDoc javaDoc) throws Wj2eeException {
		XClass class1 = method.getContainingClass();

		String actionClassName = actionClassName(class1, javaDoc);
		String constantName = constanteParaServico(servico);

		String result = actionClassName + "." + constantName;

		return result;
	}

	public static boolean ifIsPropriedadePesquisavel(XMethod method)
			throws Wj2eeException {

		AtributosMetodoVO atributosMetodoVO = new AtributosMetodoVO(method);

		boolean returnValue = atributosMetodoVO.getPesquisavel().equals("true");

		return returnValue;

	}

	public static boolean ifIsPropriedadeListavel(XMethod method)
			throws Wj2eeException {

		AtributosMetodoVO atributosMetodoVO = new AtributosMetodoVO(method);

		boolean returnValue = atributosMetodoVO.getListavel().equals("true");

		return returnValue;

	}

	public static boolean isAutoFulfilling(XMethod method)
			throws Wj2eeException {

		AtributosMetodoVO atributosMetodoVO = new AtributosMetodoVO(method);

		boolean returnValue = atributosMetodoVO.getAutoFulfilling().equals(
				"true");

		return returnValue;

	}

	public static boolean ifIsPropriedadeOrdenavel(XMethod method)
			throws Wj2eeException {

		AtributosMetodoVO atributosMetodoVO = new AtributosMetodoVO(method);

		boolean returnValue = atributosMetodoVO.getOrdenavel().equals("true");

		return returnValue;

	}

	public static String nomeAtributoParaPesquisa(XMethod method,
			String sufixPesquisa) throws Wj2eeException {
		String nomeAtributoParaPesquisa = TagHandlerUtil
				.propertyNameSemSufixoVO(method);

		nomeAtributoParaPesquisa = "pesquisa"
				+ primeiraLetraMaiuscula(nomeAtributoParaPesquisa)
				+ sufixPesquisa;

		return nomeAtributoParaPesquisa;
	}

	public static String tipoAtributoParaPesquisa(XMethod method,
			XJavaDoc javaDoc) throws Wj2eeException {
		XClass class1 = classeDoMetodo(method, javaDoc);
		String returnValue;

		if (isInstanceOf(class1, CLASS_NAME_BASEVO)) {
			/* VOU FAZER A PESQUISA PELA PK DO VO */
			String pkClassName = TagHandlerUtil.pkClassName(class1);
			returnValue = pkClassName;

		} else if (isInstanceOf(class1, CLASS_NAME_COLLECTION)) {
			/* VOU FAZER A PESQUISA PELA PK DO VO */
			String voWrapperClassName = TagHandlerUtil
					.voWrapperClassName(class1);
			returnValue = voWrapperClassName;

		} else if (isInstanceOf(class1, CLASS_NAME_DATE)) {
			/* VOU FAZER A PESQUISA POR UMA STRING QUE REPRESENTA UMA DATA */
			returnValue = Date.class.getName();

		} else if (isInstanceOf(class1, CLASS_NAME_NUMBER)) {
			/* VOU FAZER A PESQUISA POR UM DOUBLE */
			returnValue = class1.getName();

		} else if (isInstanceOf(class1, CLASS_NAME_STRING)) {
			/* VOU FAZER A PESQUISA POR UMA STRING */
			returnValue = String.class.getName();

		} else {
			/*
			 * Erro, verifique se procede dizer que este atributo é para
			 * pesquisa
			 */
			throw new Wj2eeException(
					"Nao foi possivel estabelecer o tipo do atributo para pesquisa ("
							+ method
							+ "). "
							+ "Certifique-se de que este atributo pode ser utilizado para pesquisa!");
		}

		return returnValue;
	}

	public static boolean ifIsSobrescreverMecanismoDePesquisa(XClass class1)
			throws Wj2eeException {
		/* COMO DEFAULT EU DIGO QUE NÃO VOI Sobrescrever Mecanismo De Pesquisa */
		boolean returnValue = false;

		Collection methods = class1.getMethods();
		Iterator iterator = methods.iterator();

		while (iterator.hasNext()) {
			XMethod method = (XMethod) iterator.next();
			/*
			 * se encontrar ao menos 1 metodo pesquisavel eu ja devo substituir
			 * o mecanismo de pesquisa do modulo
			 */
			if (method.isPropertyAccessor()
					&& ifIsPropriedadePesquisavel(method)) {
				returnValue = true;
				break;
			}
		}

		return returnValue;
	}

	public static String metodoMecanismoPesquisa(XClass class1)
			throws Wj2eeException {

		String returnValue = "find";

		returnValue += class1.getName();

		return returnValue;
	}

	public static String daoClassName(Properties attributes)
			throws Wj2eeException {
		String entidate = attributes.getProperty(ATTRIBUTE_ENTIDADE);
		return entidate + "DAO";
	}

	public static String pkClassName(XClass class1) throws Wj2eeException {
		String entidate = getAtributosClasseVO(class1).getEntidade();
		return entidate + "PK";
	}

	public static String daoClassName(XClass class1) throws Wj2eeException {
		String entidate = getAtributosClasseVO(class1).getEntidade();
		return entidate + "DAO";
	}

	public static String hibernateDAOClassName(XClass class1)
			throws Wj2eeException {

		String entidate = getAtributosClasseVO(class1).getEntidade();
		return entidate + "HibernateDAO";
	}

	public static String extendedHibernateDAOClassName(
			DocletContext docletContext, XClass class1) throws Wj2eeException {

		String entidate;

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);

		if (!atributosClasseVO.getBaseentity().equals("true")) {
			entidate = getAtributosClasseVO(class1.getSuperclass())
					.getEntidade()
					+ "HibernateDAO";

		} else {
			String baseFacade = (String) docletContext
					.getConfigParam("baseHibernateDAO");

			entidate = baseFacade;
		}
		return entidate;
	}

	public static String extendedDaoClassName(DocletContext docletContext,
			XClass class1) throws Wj2eeException {
		String entidate;

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);

		if (!atributosClasseVO.getBaseentity().equals("true")) {
			entidate = getAtributosClasseVO(class1.getSuperclass())
					.getEntidade()
					+ "DAO";

		} else {
			String baseFacade = (String) docletContext
					.getConfigParam("baseDAO");

			entidate = baseFacade;
		}
		return entidate;
	}

	public static String JDBCDAOFullClassName(XClass class1)
			throws Wj2eeException {
		String pacote = class1.getContainingPackage().getName();
		String entidate = getAtributosClasseVO(class1).getEntidade();

		return pacote + "." + entidate + "JDBCDAO";
	}

	public static String JDBCOracleDAOClassName(XClass class1)
			throws Wj2eeException {

		String entidate = getAtributosClasseVO(class1).getEntidade();
		return entidate + "JDBCOracleDAO";
	}

	public static String extendedJDBCOracleDAOClassName(
			DocletContext docletContext, XClass class1) throws Wj2eeException {

		String entidate;

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);

		if (!atributosClasseVO.getBaseentity().equals("true")) {
			entidate = getAtributosClasseVO(class1.getSuperclass())
					.getEntidade()
					+ "JDBCOracleDAO";

		} else {
			String baseFacade = (String) docletContext
					.getConfigParam("baseJDBCDAO");

			entidate = baseFacade;
		}
		return entidate;
	}

	public static String JDBCDB2DAOClassName(XClass class1)
			throws Wj2eeException {

		String entidate = getAtributosClasseVO(class1).getEntidade();
		return entidate + "JDBCDB2DAO";
	}

	public static String extendedJDBCDB2DAOClassName(
			DocletContext docletContext, XClass class1) throws Wj2eeException {

		String entidate;

		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);

		if (!atributosClasseVO.getBaseentity().equals("true")) {
			entidate = getAtributosClasseVO(class1.getSuperclass())
					.getEntidade()
					+ "JDBCDB2DAO";

		} else {
			String baseFacade = (String) docletContext
					.getConfigParam("baseJDBCDAO");

			entidate = baseFacade;
		}
		return entidate;
	}

	public static boolean ifIsCriarMecanismoPopUp(XMethod method,
			XJavaDoc javaDoc) throws XDocletException, Wj2eeException {
		boolean returnValue = false;

		if (ifIsMetodoParaRelacionamento(method, javaDoc)) {
			AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
			String renderer = atributosMetodoVO.getRenderer();
			if (renderer.equals(TagHandlerUtil.RENDERER_MONO_POPUP)
					|| renderer.equals(TagHandlerUtil.RENDERER_MULTI_POPUP)) {
				returnValue = true;
			}
		}

		return returnValue;
	}

	public static String callServiceParaPopUp(XMethod method, XJavaDoc javaDoc)
			throws Wj2eeException {
		XClass class1 = TagHandlerUtil.classeDoMetodo(method, javaDoc);
		String className = class1.getName();

		String callServiceParaPopUp = "callPopup" + className;
		;

		return callServiceParaPopUp;
	}

	public static String confirmServiceParaPopUp(XMethod method,
			XJavaDoc javaDoc) throws Wj2eeException {
		XClass class1 = TagHandlerUtil.classeDoMetodo(method, javaDoc);
		String className = class1.getName();

		String callServiceParaPopUp = "confirmPopup" + className;
		;

		return callServiceParaPopUp;

	}

	public static String defaultValueParaPesquisa(XMethod method) {
		XClass class1 = method.getPropertyType().getType();
		String returnValue;

		if (TagHandlerUtil.isInstanceOf(class1, TagHandlerUtil.CLASS_NAME_DATE)) {
			returnValue = "";
		} else if (TagHandlerUtil.isInstanceOf(class1,
				TagHandlerUtil.CLASS_NAME_NUMBER)) {
			returnValue = "0";
		} else if (TagHandlerUtil.isInstanceOf(class1,
				TagHandlerUtil.CLASS_NAME_STRING)) {
			returnValue = "";
		} else {
			returnValue = "";
		}

		return returnValue;
	}

	public static String initialValueParaPesquisa(XMethod method) {
		return "";
	}

	public static String hibernateDAOCriteriaExpressionFindByPK(XMethod method) {
		String returValue = "criteria.add(Expression.eq( \""
				+ method.getPropertyName() + "\",modulePk." + method.getName()
				+ "()));";
		return returValue;
	}

	public static String hibernateDAOCriteriaExpression(XMethod method,
			String sufixPesquisa) throws XDocletException, Wj2eeException {
		XClass class1 = method.getPropertyType().getType();

		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		String operadorPesquisa = atributosMetodoVO.getOperadorPesquisa();

		String returValue;

		if (operadorPesquisa.equals(TagHandlerUtil.OPERADOR_RANGE_VALUE)) {
			if (sufixPesquisa.equals(TagHandlerUtil.CONTROL_SUFIX_DE)) {
				returValue = "criteria.add(Expression.ge( \""
						+ method.getPropertyName() + "\","
						+ nomeAtributoParaPesquisa(method, sufixPesquisa)
						+ "));";

			} else if (sufixPesquisa.equals(TagHandlerUtil.CONTROL_SUFIX_ATE)) {
				returValue = "criteria.add(Expression.le( \""
						+ method.getPropertyName() + "\","
						+ nomeAtributoParaPesquisa(method, sufixPesquisa)
						+ "));";

			} else {
				throw new Wj2eeException("O valor de sufixo '" + sufixPesquisa
						+ "' nao e valido "
						+ "para o atributo operadorPesquisa='"
						+ operadorPesquisa + "'!");

			}
		} else if (operadorPesquisa.equals(TagHandlerUtil.OPERADOR_LIKE)) {

			returValue = "criteria.add(Expression.like( \""
					+ method.getPropertyName() + "\","
					+ nomeAtributoParaPesquisa(method, sufixPesquisa)
					+ ",MatchMode.START).ignoreCase());";

		} else if (operadorPesquisa.equals(TagHandlerUtil.OPERADOR_EQUAL)) {

			if (class1.isA(TagHandlerUtil.CLASS_NAME_BASEVO)) {
				/* SE FOR VO DEVO COMPARAR OS CAMPOS DA PK */

				/* precisa ser inicializada para funcionar '+=' */
				returValue = "";

				Collection methods = class1.getMethods();
				Iterator iterator = methods.iterator();
				while (iterator.hasNext()) {
					XMethod methodDaVez = (XMethod) iterator.next();
					if (methodDaVez.isPropertyAccessor()) {
						if (TagHandlerUtil.ifIsPk(methodDaVez)) {

							String property = methodDaVez.getPropertyName();

							returValue += "\n\t\t\t\t"
									+ "criteria.add(Expression.eq( \""
									+ method.getPropertyName()
									+ "."
									+ property
									+ "\","
									+ nomeAtributoParaPesquisa(method,
											sufixPesquisa) + "."
									+ methodDaVez.getName() + "()));";

							// Ex: criteria.add(Expression.eq(
							// "projetoVO.codigo",pesquisaProjeto.getCodigo()));
						}
					}
				}

			} else {
				returValue = "criteria.add(Expression.eq( \""
						+ method.getPropertyName() + "\","
						+ nomeAtributoParaPesquisa(method, sufixPesquisa)
						+ "));";
			}

		} else {
			throw new Wj2eeException(
					"Nao foi possivel identificar o 'operadorPesquisa' deste método pesquisavel='true' !. "
							+ "Verifique se o atributo 'operadorPesquisa' contém um valor válido!");
		}

		return returValue;
	}

	public static boolean ifIsOperadorPesquisa(XMethod method,
			Properties attributes) throws Wj2eeException {
		String operadorAComparar = attributes.getProperty(ATTRIBUTE_OPERADOR);

		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		String operadorPesquisa = atributosMetodoVO.getOperadorPesquisa();

		return operadorPesquisa.equals(operadorAComparar);

	}

	public static boolean ifIsControlSufix(Properties attributes,
			String atualControlSufix) {
		String controlSufix = attributes.getProperty(ATTRIBUTE_CONTROL_SUFIX);

		boolean returnValue = (atualControlSufix.equals(controlSufix));

		return returnValue;

	}

	public static boolean ifIsRenderer(Properties attributes, XMethod method)
			throws Wj2eeException {
		AtributosMetodoVO atributosMetodoVO = getAtributosMetodoVO(method);
		String redererDoMetodo = atributosMetodoVO.getRenderer() + "";
		String redererDoParametro = attributes.getProperty(ATTRIBUTE_RENDERER)
				+ "";

		return redererDoMetodo.equals(redererDoParametro);
	}

	public static boolean ifIsRenderer(Properties attributes, XClass class1)
			throws Wj2eeException {
		AtributosClasseVO atributosClasseVO = getAtributosClasseVO(class1);
		String redererDaClasse = atributosClasseVO.getRenderer() + "";
		String redererDoParametro = attributes.getProperty(ATTRIBUTE_RENDERER)
				+ "";

		return redererDaClasse.equals(redererDoParametro);
	}

	public static void writeException(CharArrayWriter charArrayWriter,
			Throwable throwable) throws IOException {
		String message = throwable.getMessage();

		Date date = new Date();

		charArrayWriter.write(date + "\n");
		charArrayWriter.write("\n\n" + throwable.getClass().getName() + ":"
				+ message + "\n");

		StackTraceElement[] stackTraceElements = throwable.getStackTrace();

		for (int i = 0; i < stackTraceElements.length; i++) {
			charArrayWriter.write(stackTraceElements[i] + "\n");
		}

		Throwable throwableFilho = throwable.getCause();

		if (throwableFilho != null) {
			writeException(charArrayWriter, throwableFilho);
		}

		if (throwable != null && throwable instanceof xdoclet.XDocletException) {
			XDocletException docletException = (XDocletException) throwable;
			throwableFilho = docletException.getNestedException();
			writeException(charArrayWriter, throwableFilho);
		}

	}

	public static void trataExcecao(Throwable throwable) {
		FileWriter fileWriter;
		try {

			fileWriter = getFileWriter();

			CharArrayWriter charArrayWriter = new CharArrayWriter();

			/* escreve todas as mensagens de erro mais StackTrace */
			writeException(charArrayWriter, throwable);

			charArrayWriter.writeTo(fileWriter);

			/* "salva" */
			fileWriter.flush();
			fileWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		throwable.printStackTrace();
	}

	public static FileWriter getFileWriter() throws IOException {
		String strFile = "c:\\logXdoclet.log";

		return new FileWriter(strFile);
	}

	public static String forwardParaConsultaModuloMestre(XMethod method,
			XJavaDoc javaDoc) throws Wj2eeException {
		XClass containingClass = method.getContainingClass();

		return forwardParaConsultaModuloMestre(containingClass, javaDoc);
	}

	public static String forwardParaConsultaModuloMestre(XClass moduloEscravo,
			XJavaDoc javaDoc) throws Wj2eeException {
		String forward = "FORWARD_TELA_CONSULTA";

		AtributosClasseVO atributosModuloEscravo = new AtributosClasseVO(
				moduloEscravo);

		String classeVOMestre = atributosModuloEscravo.getVoMestre();

		if (!FrameworkAcheUtil.isNullOrEmptyOrZero(classeVOMestre)) {
			XClass moduloMestre = javaDoc.getXClass(classeVOMestre);

			AtributosClasseVO atributosModuloMestre = new AtributosClasseVO(
					moduloMestre);
			String rendererMestre = atributosModuloMestre.getRenderer();

			if (!FrameworkAcheUtil.isNullOrEmptyOrZero(rendererMestre)
					&& rendererMestre
							.equals(AtributosClasseVO.RENDERER_MODULOESCRAVO)) {
				String moduleIdMestre = moduleId(moduloMestre);
				forward = forward + "_" + moduleIdMestre.toUpperCase();
			}
		}

		return forward;
	}

	public static String forwardParaCadastro(XMethod method, XJavaDoc javaDoc)
			throws Wj2eeException {

		XClass classeModulo = classeDoMetodo(method, javaDoc);

		String forward = forwardParaCadastro(classeModulo, javaDoc);

		return forward;
	}

	public static String forwardParaCadastro(XClass classeModulo,
			XJavaDoc javaDoc) throws Wj2eeException {
		String forward = "FORWARD_TELA_CADASTRO";
		Log logger = LogFactory.getLog(TagHandlerUtil.class);

		AtributosClasseVO atributosModulo = new AtributosClasseVO(classeModulo);

		String rendererClasse = atributosModulo.getRenderer();

		if (!ifIsMestre(classeModulo)) {
			logger.info("VO " + classeModulo + " NÃO é mestre!");
			if (!FrameworkAcheUtil.isNullOrEmptyOrZero(rendererClasse)
					&& rendererClasse
							.equals(AtributosClasseVO.RENDERER_MODULOESCRAVO)) {

				/* se for modulo escravo */
				logger
						.info("VO " + classeModulo
								+ " Renderer = moduloEscravo!");
				String moduleId = moduleId(classeModulo);
				forward = forward + "_" + moduleId.toUpperCase();

			} else {
				/* se for indexado */
				logger.info("VO " + classeModulo + " Renderer = indexado!");
				XClass classMestre = classeMestre(classeModulo, javaDoc);

				/** *********************************** */
				/* CÓDIGO CORRIGIDO DARCIO - 08/02/2006 */
				if (ifPossuiMestre(classMestre)) {
					String moduleId = moduleId(classMestre);
					forward = forward + "_" + moduleId.toUpperCase();
				}
				/* CÓDIGO CORRIGIDO DARCIO - 08/02/2006 */
				/** *********************************** */

				/** ************************************* */
				/*
				 * CÓDIGO COM DEFEITO DARCIO - 08/02/2006
				 * if(!ifIsMestre(classMestre)){ String moduleId =
				 * moduleId(classMestre); forward = forward + "_" +
				 * moduleId.toUpperCase(); } CÓDIGO COM DEFEITO DARCIO -
				 * 08/02/2006
				 */
				/** ************************************* */

			}
		} else {
			logger.info("VO " + classeModulo + " é mestre!");
		}
		return forward;
	}

	public static String forwardParaCadastroModuloMestre(XMethod method,
			XJavaDoc javaDoc) throws Wj2eeException {
		XClass containingClass = method.getContainingClass();

		return forwardParaCadastroModuloMestre(containingClass, javaDoc);
	}

	public static String forwardParaCadastroModuloMestre(XClass moduloEscravo,
			XJavaDoc javaDoc) throws Wj2eeException {
		String forward = "FORWARD_TELA_CADASTRO";

		/* Atributos da classe */
		AtributosClasseVO atributosModuloEscravo = new AtributosClasseVO(
				moduloEscravo);

		/* recuperei o mestre */
		String classeVOMestre = atributosModuloEscravo.getVoMestre();

		/* testo se existe mestre */
		if (!FrameworkAcheUtil.isNullOrEmptyOrZero(classeVOMestre)) {

			/* recupero a classe do mestre */
			XClass moduloMestre = javaDoc.getXClass(classeVOMestre);

			/* recupero os atributos XDoclet do mestre */
			AtributosClasseVO atributosModuloMestre = new AtributosClasseVO(
					moduloMestre);

			/* descubro o renderer do mestre */
			String rendererMestre = atributosModuloMestre.getRenderer();

			/* avalio se o renderer do mestre é moduloescravo */
			if (!FrameworkAcheUtil.isNullOrEmptyOrZero(rendererMestre)
					&& rendererMestre
							.equals(AtributosClasseVO.RENDERER_MODULOESCRAVO)) {

				String moduleIdMestre = moduleId(moduloMestre);
				forward = forward + "_" + moduleIdMestre.toUpperCase();

			}
		}

		return forward;
	}

	public static String forwardParaConsulta(XClass classeModulo,
			XJavaDoc javaDoc) throws Wj2eeException {
		String forward = "FORWARD_TELA_CONSULTA";

		AtributosClasseVO atributosModulo = new AtributosClasseVO(classeModulo);

		String rendererClasse = atributosModulo.getRenderer();

		if (!FrameworkAcheUtil.isNullOrEmptyOrZero(rendererClasse)
				&& rendererClasse
						.equals(AtributosClasseVO.RENDERER_MODULOESCRAVO)) {
			String moduleId = moduleId(classeModulo);
			forward = forward + "_" + moduleId.toUpperCase();
		}

		return forward;
	}

	public static String forwardParaConsulta(XMethod method, XJavaDoc javaDoc)
			throws Wj2eeException {
		String forward = "FORWARD_TELA_CONSULTA";

		XClass classeModulo = classeDoMetodo(method, javaDoc);
		AtributosClasseVO atributosModulo = new AtributosClasseVO(classeModulo);

		String rendererClasse = atributosModulo.getRenderer();

		if (!FrameworkAcheUtil.isNullOrEmptyOrZero(rendererClasse)
				&& rendererClasse
						.equals(AtributosClasseVO.RENDERER_MODULOESCRAVO)) {
			String moduleId = moduleId(classeModulo);
			forward = forward + "_" + moduleId.toUpperCase();
		}

		return forward;
	}

	public static boolean ifPossuiMestre(XClass currentClass)
			throws Wj2eeException {
		AtributosClasseVO atributosClasseVO = new AtributosClasseVO(
				currentClass);

		String renderer = "" + atributosClasseVO.getRenderer();

		return !renderer.equals(AtributosClasseVO.RENDERER_MODULO);
	}

	public static boolean ifIsMestre(XClass currentClass) throws Wj2eeException {
		AtributosClasseVO atributosClasseVO = new AtributosClasseVO(
				currentClass);
		Log logger = LogFactory.getLog(TagHandlerUtil.class);

		String renderer = "" + atributosClasseVO.getRenderer();

		logger.info(currentClass.getName() + " renderer =" + renderer);

		return renderer.equals(AtributosClasseVO.RENDERER_MODULO);
	}

	public static boolean ifIsEscravoMuitos(XClass currentClass)
			throws Wj2eeException {
		AtributosClasseVO atributosClasseVO = new AtributosClasseVO(
				currentClass);

		String renderer = "" + atributosClasseVO.getRenderer();

		return renderer.equals(AtributosClasseVO.RENDERER_INDEXADO)
				|| renderer.equals(AtributosClasseVO.RENDERER_MODULOESCRAVO);
	}

	public static boolean ifIsEscravoUm(XClass currentClass)
			throws Wj2eeException {
		AtributosClasseVO atributosClasseVO = new AtributosClasseVO(
				currentClass);

		String renderer = "" + atributosClasseVO.getRenderer();

		return renderer.equals(AtributosClasseVO.RENDERER_LOCAL);
	}

	/**
	 * Analisa segundo os atributos, se a figuracao deverá aparecer como classe
	 * ou nomes de variavel<br>
	 * Ex: 'ProjetoVO' ou 'projetoVO'
	 */
	public static String variavelDe(String classe, Properties attributes) {
		String strIsVar = attributes
				.getProperty(TagHandlerUtil.ATTRIBUTE_VARIAVEL);
		if (Boolean.valueOf(strIsVar).booleanValue()) {
			classe = TagHandlerUtil.primeiraLetraMinuscula(classe);
		}

		return classe;
	}

	public static String primeiroElementoCadastro(XClass classeCorrente)
			throws Wj2eeException {
		XMethod method = getPrimeiroMethodNotReadOnly(classeCorrente);

		// String propertyName = TagHandlerUtil.propertyNameSemSufixoVO(method);

		/*
		 * Darcio 10/02/2005 - este código mantinha o sufixo VO no final do
		 * property, sendo que no wrapper não tinha o sufixo VO
		 */
		String propertyName = method.getPropertyName();

		String strVoWrapperClassName = voWrapperClassName(classeCorrente);

		strVoWrapperClassName = primeiraLetraMinuscula(strVoWrapperClassName);

		return strVoWrapperClassName + "." + propertyName;
	}

	public static String primeiroElementoPesquisa(XClass currentClass)
			throws Wj2eeException {
		XMethod method = getPrimeiroMethodPesquisavel(currentClass);

		String propertyName = TagHandlerUtil.propertyNameSemSufixoVO(method);

		/*
		 * Darcio 10/02/2005 - este código mantinha o sufixo VO no final do
		 * property, sendo que no wrapper não tinha o sufixo VO String
		 * propertyName = method.getPropertyName();
		 */

		propertyName = primeiraLetraMaiuscula(propertyName);

		String strVoWrapperClassName = voWrapperClassName(currentClass);

		strVoWrapperClassName = primeiraLetraMinuscula(strVoWrapperClassName);

		return strVoWrapperClassName + ".pesquisa" + propertyName;
	}

	public static XMethod getPrimeiroMethodNotReadOnly(XClass currentClass) {

		List metodos = currentClass.getMethods(
				new NotReadOnlyMethodPredicate(), true);

		metodos = sortMethods(metodos);

		XMethod primeiroMethod = (XMethod) metodos.get(0);

		return primeiroMethod;
	}

	public static XMethod getPrimeiroMethodPesquisavel(XClass currentClass) {

		List metodos = currentClass.getMethods(
				new MetodosPesquisaveisPredicate(), true);

		metodos = sortMethods(metodos);

		XMethod primeiroMethod = (XMethod) metodos.get(0);

		return primeiroMethod;
	}

	public static String constantClassName(XMethod currentMethod) {

		// TODO Auto-generated method stub
		return null;
	}

}