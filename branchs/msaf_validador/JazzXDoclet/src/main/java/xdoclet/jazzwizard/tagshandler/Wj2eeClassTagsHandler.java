/*
 * Data de Criacao 28/05/2005 15:44:45
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.tagshandler;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xdoclet.DocletContext;
import xdoclet.XDocletException;
import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.tagvalue.AtributosClasseVO;
import xdoclet.tagshandler.ClassTagsHandler;
import xjavadoc.XClass;
import xjavadoc.XJavaDoc;
import xjavadoc.XMethod;
import br.com.dlp.framework.util.FrameworkAcheUtil;

/**
 * Tags utilitarias para geração de artefatos a partir de templates
 * 'single-file' (struts-config.xml, Constants.java, HibernateDAO.properties)
 * 
 * @author Darcio L Pacifico - 28/05/2005 15:44:45
 * 
 * @xdoclet.taghandler namespace = "Wj2eeClass"
 */
public class Wj2eeClassTagsHandler extends ClassTagsHandler {

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
				Log logger = LogFactory.getLog(TagHandlerUtil.class);
				logger.error("Erro ao tentar tratar excecao!!");
			}

		}
	}

	/**
	 * @doc.tag type="block"
	 * @doc.param name="superclasses" optional="true" values="true,false"
	 *            description="If true then traverse superclasses also,
	 *            otherwise look up the tag in current concrete class only."
	 * @doc.param name="recursivamente" optional="true" values="true,false"
	 *            description="If true then traverse superclasses also,
	 *            otherwise look up the tag in current concrete class only."
	 * @doc.param name="sort" optional="true" values="true,false"
	 *            description="If true then sort the methods list."
	 * @doc.param name="tipo" optional="true" description="Classe que terá seus
	 *            métodos iterados"
	 * @doc.param name="incluirMestre" optional="true" values="true,false"
	 *            description="Decide se o VO mestre será incluído na iteracao"
	 */
	public void forTodosOsEscravos2(String template, Properties attributes)
			throws Throwable {
		/* PRESERVA ESTADO */
		XClass preservaEstadoXClass = getCurrentClass();
		XMethod preservaEstadoXMethod = getCurrentMethod();
		/* PRESERVA ESTADO */

		/* for real */
		forTodosOsEscravosRec2(template, attributes);

		/* PRESERVA ESTADO */
		setCurrentClass(preservaEstadoXClass);
		setCurrentMethod(preservaEstadoXMethod);
		/* PRESERVA ESTADO */
	}

	/**
	 * @doc.tag type="block"
	 * @doc.param name="superclasses" optional="true" values="true,false"
	 *            description="If true then traverse superclasses also,
	 *            otherwise look up the tag in current concrete class only."
	 * @doc.param name="recursivamente" optional="true" values="true,false"
	 *            description="If true then traverse superclasses also,
	 *            otherwise look up the tag in current concrete class only."
	 * @doc.param name="sort" optional="true" values="true,false"
	 *            description="If true then sort the methods list."
	 * @doc.param name="tipo" optional="true" description="Classe que terá seus
	 *            métodos iterados"
	 */
	private void forTodosOsEscravosRec2(String template, Properties attributes)
			throws Throwable {
		String strIncluirMestre = attributes.getProperty("incluirMestre");

		/* INCLUSAO DO MESTRE NA ITERACAO */
		if (strIncluirMestre != null && strIncluirMestre.equals("true")) {
			generate(template);
		}

		/* procuro os escravos de meu módulo */
		String strSort = attributes.getProperty("sort");
		boolean sort = Boolean.valueOf(strSort).booleanValue();
		XClass class1 = getCurrentClass();
		List list = TagHandlerUtil.metodosModuloEscravos(class1, getXJavaDoc(),
				sort);

		/* itero os escravos e invoco a geração destes recursivamente */
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			XMethod method = (XMethod) iterator.next();
			XClass xClassModuloEscravo = TagHandlerUtil.classeDoMetodo(method,
					getXJavaDoc());
			setCurrentClass(xClassModuloEscravo);
			// setCurrentMethod(null);

			/* gera do módulo atual */
			if (strIncluirMestre == null || !strIncluirMestre.equals("true")) {
				generate(template);
			}

			forTodosOsEscravos2(template, attributes);
		}
	}

	/**
	 * Iterates over all methods of current class and evaluates the body of the
	 * tag for each method.
	 * 
	 * @param template
	 *            The body of the block tag
	 * @param attributes
	 *            The attributes of the template tag
	 * @throws XDocletException
	 * @exception XDocletException
	 *                Description of Exception
	 * @throws Throwable
	 * 
	 * @doc.tag type="block"
	 * @doc.param name="superclasses" optional="true" values="true,false"
	 *            description="If true then traverse superclasses also,
	 *            otherwise look up the tag in current concrete class only."
	 * @doc.param name="recursivamente" optional="true" values="true,false"
	 *            description="If true then traverse superclasses also,
	 *            otherwise look up the tag in current concrete class only."
	 * @doc.param name="sort" optional="true" values="true,false"
	 *            description="If true then sort the methods list."
	 * @doc.param name="tipo" optional="true" description="Classe que terá seus
	 *            métodos iterados"
	 */
	public void forTodosOsEscravos(String template, Properties attributes)
			throws Throwable {
		String strSort = attributes.getProperty("sort");
		boolean sort = Boolean.valueOf(strSort).booleanValue();

		/* PRESERVA ESTADO */
		XClass preservaEstadoXClass = getCurrentClass();
		XMethod preservaEstadoXMethod = getCurrentMethod();
		/* PRESERVA ESTADO */

		XJavaDoc javaDoc = getXJavaDoc();

		/*
		 * se foi informado o tipo, itero o tipo, se nao, itero a classe
		 * corrente
		 */
		String className = attributes
				.getProperty(TagHandlerUtil.ATTRIBUTE_TIPO);
		XClass currentClass;
		if (FrameworkAcheUtil.isNullOrEmptyOrZero(className)) {
			currentClass = getCurrentClass();
		} else {
			currentClass = javaDoc.getXClass(className);
		}

		/* peço a lista dos modulos escravos de currentClass */
		List metodosModuloEscravos;
		try {
			metodosModuloEscravos = TagHandlerUtil.metodosModuloEscravos(
					currentClass, javaDoc, sort);
		} catch (Throwable e) {
			TagHandlerUtil.trataExcecao(e);
			throw e;
		}

		/*
		 * recupera a propriedade que me diz se o for de modulosescravo deverá
		 * ser recursivo
		 */
		String strRecursivamente = attributes
				.getProperty(TagHandlerUtil.ATTRIBUTE_RECURSIVAMENTE);
		boolean recursivamente = Boolean.valueOf(strRecursivamente)
				.booleanValue();

		try {
			/* itera todos os XClass dos modulos (recursivamente se for pedido) */
			forTodosOsEscravos(template, metodosModuloEscravos, recursivamente,
					attributes);
		} catch (Throwable e) {
			TagHandlerUtil.trataExcecao(e);

		}

		/* PRESERVA ESTADO */
		setCurrentClass(preservaEstadoXClass);
		setCurrentMethod(preservaEstadoXMethod);
		/* PRESERVA ESTADO */

	}

	/**
	 * Serve para iterar recursivamente o XClass
	 * 
	 * @throws Wj2eeException
	 */
	private void forTodosOsEscravos(String template,
			List metodosModuloEscravos, boolean recursivamente,
			Properties attributes) {
		String strSort = attributes.getProperty("sort");
		boolean sort = Boolean.valueOf(strSort).booleanValue();

		try {

			Iterator iterator = metodosModuloEscravos.iterator();
			while (iterator.hasNext()) {

				XMethod methodModuloEscravo = (XMethod) iterator.next();
				XClass xClassModuloEscravo = TagHandlerUtil.classeDoMetodo(
						methodModuloEscravo, getXJavaDoc());

				XClass currentClass = methodModuloEscravo.getContainingClass();
				setCurrentClass(currentClass);
				setCurrentMethod(methodModuloEscravo);

				/* PROCESSA O BLOCO DE TEMPLATE */
				generate(template);

				/* ANALISA SE DEVE SER RECURSIVO */
				if (recursivamente) {
					List list = TagHandlerUtil.metodosModuloEscravos(
							xClassModuloEscravo, getXJavaDoc(), sort);

					forTodosOsEscravos(template, list, recursivamente,
							attributes);

				}
			}
		} catch (XDocletException e) {
			TagHandlerUtil.trataExcecao(e);
		} catch (Wj2eeException e) {
			TagHandlerUtil.trataExcecao(e);
		}

	}

	/**
	 * Analisa se a classe corrente é gerarView==true
	 * 
	 * @throws Wj2eeException
	 * 
	 * @doc.tag type="block"
	 */
	public void ifIsGerarView(String template) throws XDocletException,
			Wj2eeException {
		AtributosClasseVO atributosClasseVO = new AtributosClasseVO(
				getCurrentClass());
		String renderer = atributosClasseVO.getRenderer();
		if (!renderer.equals(TagHandlerUtil.RENDERER_INDEXADO)
				&& !renderer.equals(TagHandlerUtil.RENDERER_LOCAL)) {
			if (atributosClasseVO.getGerarView().equals("true")) {
				generate(template);
			}
		}
	}

	/**
	 * Analisa se a classe corrente é gerarView!=true
	 * 
	 * @throws Wj2eeException
	 * 
	 * @doc.tag type="block"
	 */
	public void ifIsNotGerarView(String template) throws XDocletException,
			Wj2eeException {
		AtributosClasseVO atributosClasseVO = new AtributosClasseVO(
				getCurrentClass());
		if (!atributosClasseVO.getGerarView().equals("true")) {
			generate(template);
		}
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String moduleConstant() throws Wj2eeException {
		return TagHandlerUtil.moduleConstant(getCurrentClass());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String moduleVLHConstant() throws Wj2eeException {
		return TagHandlerUtil.moduleVLHConstant(getCurrentClass());
	}

	public String classSerialVersionUID() throws Wj2eeException {
		DocletContext docletContext = getDocletContext();
		String classSerialVersionUID = (String) docletContext
				.getConfigParam("classSerialVersionUID");

		if (FrameworkAcheUtil.isNullOrEmptyOrZero(classSerialVersionUID)) {
			classSerialVersionUID = "1L; /*TODO: defina SerialVersionUID*/";
		} else {
			classSerialVersionUID += ";";
		}

		return classSerialVersionUID;
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleId() throws Wj2eeException {
		return TagHandlerUtil.moduleId(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleId(Properties attributes) throws Wj2eeException {
		String moduleId = TagHandlerUtil.moduleId(getCurrentClass());

		moduleId = TagHandlerUtil.variavelDe(moduleId, attributes);

		return moduleId;
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleIdMestre() throws Wj2eeException {
		XClass classeMestre = TagHandlerUtil.classePrimeiroMestre(
				getCurrentClass(), getXJavaDoc());
		return TagHandlerUtil.moduleId(classeMestre);
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleIdMestre(Properties attributes) throws Wj2eeException {
		String strModuleIdMestre = moduleIdMestre();
		strModuleIdMestre = TagHandlerUtil.variavelDe(strModuleIdMestre,
				attributes);
		return strModuleIdMestre;
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleIdPrimLetraMaiuscula() throws Wj2eeException {
		return TagHandlerUtil.moduleIdPrimLetraMaiuscula(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleVLHId() throws Wj2eeException {
		return TagHandlerUtil.moduleVLHId(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleDescription() throws Wj2eeException {
		return TagHandlerUtil.moduleDescription(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleNome() throws Wj2eeException {
		return TagHandlerUtil.moduleNome(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleUc() throws Wj2eeException {
		return TagHandlerUtil.moduleUc(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String fullModuleNome() throws Wj2eeException {
		return TagHandlerUtil.fullModuleNome(getCurrentClass(), getXJavaDoc());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleWebDirectory() throws Wj2eeException {
		return TagHandlerUtil.moduleWebDirectory(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleCadastroJSPPage() throws Wj2eeException {
		return TagHandlerUtil.moduleCadastroJSPPage(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleConsultaJSPPage() throws Wj2eeException {
		return TagHandlerUtil.moduleConsultaJSPPage(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String modulePesquisaJSPPage() throws Wj2eeException {
		return TagHandlerUtil.modulePesquisaJSPPage(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String moduleJavaScriptFile() throws Wj2eeException {
		return TagHandlerUtil.moduleJavaScriptFile(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String ejbHomeModule() throws Wj2eeException {
		// ejbhome_curso=br.com.dlp.cursoache.curso.CursoEJBHome

		return TagHandlerUtil.ejbHomeModule(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String ejbHomeModuleLocal() throws Wj2eeException {
		// ejbhome_curso=br.com.dlp.cursoache.curso.CursoEJBHome

		return TagHandlerUtil.ejbHomeModuleLocal(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String ejbVLHHomeModule() throws Wj2eeException {
		// ejbhome_cursoVlh=br.com.dlp.cursoache.curso.CursoVLHEJBHome
		return TagHandlerUtil.ejbVLHHomeModule(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String pacoteModulo() throws Wj2eeException {
		// ejbhome_cursoVlh=br.com.dlp.cursoache.curso.CursoVLHEJBHome
		return TagHandlerUtil.pacoteModulo(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String ejbModule() throws Wj2eeException {
		// ejbhome_curso=br.com.dlp.cursoache.curso.CursoEJBHome

		return TagHandlerUtil.ejbModule(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String ejbVLHModule() throws Wj2eeException {
		// ejbhome_cursoVlh=br.com.dlp.cursoache.curso.CursoVLHEJBHome
		return TagHandlerUtil.ejbVLHModule(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String jdniModule() throws Wj2eeException {
		// jndi_curso=br/com/dlp/cursoache/curso/Curso
		return TagHandlerUtil.jdniModule(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String jdniModuleLocal() throws Wj2eeException {
		// jndi_curso=br/com/dlp/cursoache/curso/Curso
		return TagHandlerUtil.jdniModuleLocal(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String jdniVLHModule() throws Wj2eeException {
		// jndi_cursoVlh=br/com/dlp/cursoache/curso/CursoVLH
		return TagHandlerUtil.jdniVLHModule(getCurrentClass());
	}

	public String actionPath() throws Wj2eeException {
		// /curso/curso
		return TagHandlerUtil.actionPath(getCurrentClass(), getXJavaDoc());
	}

	public String actionFormName() throws Wj2eeException {
		return TagHandlerUtil.actionFormName(getCurrentClass());
	}

	public String actionFormNameMestre() throws Wj2eeException {
		return TagHandlerUtil.actionFormNameMestre(getCurrentClass(),
				getXJavaDoc());
	}

	public String cadastroTileName() throws Wj2eeException {
		String cadastroTileName = TagHandlerUtil
				.cadastroTileName(getCurrentClass());
		return cadastroTileName;
	}

	public String consultaTileName() throws Wj2eeException {
		String consultaTileName = TagHandlerUtil
				.consultaTileName(getCurrentClass());
		return consultaTileName;
	}

	public String pesquisaTileName() throws Wj2eeException {
		String pesquisaTileName = TagHandlerUtil
				.pesquisaTileName(getCurrentClass());
		return pesquisaTileName;
	}

	public String multiLinguaNomeKey() throws Wj2eeException {
		return TagHandlerUtil.multiLinguaNomeKey(getCurrentClass());

	}

	public String multiLinguaNomevalue() throws Wj2eeException {
		return TagHandlerUtil.multiLinguaNomevalue(getCurrentClass());

	}

	public String multiLinguaDescricaoKey() throws Wj2eeException {
		return TagHandlerUtil.multiLinguaDescricaoKey(getCurrentClass());

	}

	public String multiLinguaDescricaovalue() throws Wj2eeException {
		return TagHandlerUtil.multiLinguaDescricaovalue(getCurrentClass());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String voClassName() throws Wj2eeException {
		String voClassName = TagHandlerUtil.voClassName(getCurrentClass());
		return voClassName;
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String voClassNameMestre(Properties attributes)
			throws Wj2eeException {
		String strClasseDoMestre = voClassNameMestre();

		strClasseDoMestre = TagHandlerUtil.variavelDe(strClasseDoMestre,
				attributes);

		return strClasseDoMestre;
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String voClassNameMestre() throws Wj2eeException {
		XClass currentClass = getCurrentClass();

		XClass classeDoMestre = TagHandlerUtil.classePrimeiroMestre(
				currentClass, getXJavaDoc());

		return classeDoMestre.getName();
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String actionClassName() throws Wj2eeException {
		return TagHandlerUtil.actionClassName(getCurrentClass(), getXJavaDoc());
	}

	public String actionFullClassName() throws Wj2eeException {

		XClass currentClass = getCurrentClass();

		return TagHandlerUtil.actionFullClassName(currentClass, getXJavaDoc());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String facadeClassName() throws Wj2eeException {
		return TagHandlerUtil.facadeClassName(getCurrentClass(), getXJavaDoc());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String facadeClassNameMestre() throws Wj2eeException {
		XClass currentClass = getCurrentClass();
		XClass classeMestre = TagHandlerUtil.classeMestre(currentClass,
				getXJavaDoc());

		return TagHandlerUtil.facadeClassName(classeMestre, getXJavaDoc());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String facadeImplClassName() throws Wj2eeException {
		return TagHandlerUtil.facadeImplClassName(getCurrentClass(),
				getXJavaDoc());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String sessionFacadeClassName() throws Wj2eeException {
		return TagHandlerUtil.sessionFacadeClassName(getCurrentClass());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String vlhFacadeClassName() throws Wj2eeException {
		return TagHandlerUtil.vlhFacadeClassName(getCurrentClass());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String vlhFacadeImplClassName() throws Wj2eeException {
		return TagHandlerUtil.vlhFacadeImplClassName(getCurrentClass());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String vlhSessionFacadeClassName() throws Wj2eeException {
		return TagHandlerUtil.vlhSessionFacadeClassName(getCurrentClass());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String extendedVlhFacadeClassName() throws Wj2eeException {
		return TagHandlerUtil.extendedVlhFacadeClassName(getDocletContext(),
				getCurrentClass());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String extendedVlhFacadeImplClassName() throws Wj2eeException {
		return TagHandlerUtil.extendedVlhFacadeImplClassName(
				getDocletContext(), getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String actionFormClassName() throws Wj2eeException {
		XClass class1 = getCurrentClass();
		return TagHandlerUtil.actionFormClassName(class1);
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String actionFormClassNameMestre() throws Wj2eeException {
		XClass currentClass = getCurrentClass();
		XClass classeMestre = TagHandlerUtil.classeMestre(currentClass,
				getXJavaDoc());

		return TagHandlerUtil.actionFormClassName(classeMestre);
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String voWrapperClassName() throws Wj2eeException {
		XClass class1 = getCurrentClass();
		return TagHandlerUtil.voWrapperClassName(class1);
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String voWrapperClassName(Properties attributes)
			throws Wj2eeException {
		String strVoWrapperClassName = voWrapperClassName();

		strVoWrapperClassName = TagHandlerUtil.variavelDe(
				strVoWrapperClassName, attributes);

		return strVoWrapperClassName;
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String voWrapperClassNameMestre() throws Wj2eeException {
		XClass currentClass = getCurrentClass();

		XClass classeDoMestre = TagHandlerUtil.classePrimeiroMestre(
				currentClass, getXJavaDoc());

		if (classeDoMestre == null) {
			throw new Wj2eeException(
					"Não é possivel identificar o VO mestre, verifique se o atributo 'voMestre' foi especificado na classe '"
							+ currentClass.getQualifiedName() + "'");
		}

		return TagHandlerUtil.voWrapperClassName(classeDoMestre);
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String voWrapperClassNameMestre(Properties attributes)
			throws Wj2eeException {
		String strVoWrapperClassNameMestre = voWrapperClassNameMestre();

		strVoWrapperClassNameMestre = TagHandlerUtil.variavelDe(
				strVoWrapperClassNameMestre, attributes);

		return strVoWrapperClassNameMestre;
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String actionFormClassName(Properties properties)
			throws Wj2eeException {
		XClass class1 = getCurrentClass();
		return TagHandlerUtil.actionFormClassName(class1);
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String extendedActionClassName() throws Wj2eeException {
		return TagHandlerUtil.extendedActionClassName(getDocletContext(),
				getCurrentClass());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String extendedFacadeClassName() throws Wj2eeException {
		return TagHandlerUtil.extendedFacadeClassName(getDocletContext(),
				getCurrentClass());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String extendedFacadeImplClassName() throws Wj2eeException {
		return TagHandlerUtil.extendedFacadeImplClassName(getDocletContext(),
				getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String extendedActionFormClassName() throws Wj2eeException {
		return TagHandlerUtil.extendedActionFormClassName(getDocletContext(),
				getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String extendedVOWrapperClassName() throws Wj2eeException {
		return TagHandlerUtil.extendedVOWrapperClassName(getDocletContext(),
				getCurrentClass());
	}

	/**
	 * Analisa se a classe corrente é gerarView==true
	 * 
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * 
	 * @doc.tag type="block"
	 */
	public void ifIsSobrescreverMecanismoDePesquisa(String template)
			throws Wj2eeException, XDocletException {
		XClass class1 = getCurrentClass();

		if (TagHandlerUtil.ifIsSobrescreverMecanismoDePesquisa(class1)) {
			generate(template);
		}
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String metodoMecanismoPesquisaVLH() throws Wj2eeException {
		String metodo = TagHandlerUtil
				.metodoMecanismoPesquisa(getCurrentClass());
		return "execute" + TagHandlerUtil.primeiraLetraMaiuscula(metodo);
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String metodoMecanismoPesquisa() throws Wj2eeException {
		String metodo = TagHandlerUtil
				.metodoMecanismoPesquisa(getCurrentClass());
		return metodo;
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String daoClassName(Properties attributes) throws Wj2eeException {
		return TagHandlerUtil.daoClassName(attributes);
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String daoClassName() throws Wj2eeException {
		XClass class1 = getCurrentClass();
		return TagHandlerUtil.daoClassName(class1);
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String extendedDaoClassName() throws Wj2eeException {
		return TagHandlerUtil.extendedDaoClassName(getDocletContext(),
				getCurrentClass());

	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String extendedHibernateDAOClassName() throws Wj2eeException {
		return TagHandlerUtil.extendedHibernateDAOClassName(getDocletContext(),
				getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String hibernateDAOFullClassName() throws Wj2eeException {
		String returnValue = TagHandlerUtil
				.hibernateDAOFullClassName(getCurrentClass());
		return returnValue;
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String hibernateDAOClassName() throws Wj2eeException {
		return TagHandlerUtil.hibernateDAOClassName(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String JDBCDAOFullClassName() throws Wj2eeException {
		String returnValue = TagHandlerUtil
				.JDBCDAOFullClassName(getCurrentClass());
		return returnValue;
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String JDBCOracleDAOClassName() throws Wj2eeException {
		return TagHandlerUtil.JDBCOracleDAOClassName(getCurrentClass());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String extendedJDBCOracleDAOClassName() throws Wj2eeException {
		return TagHandlerUtil.extendedJDBCOracleDAOClassName(
				getDocletContext(), getCurrentClass());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String JDBCDB2DAOClassName() throws Wj2eeException {
		return TagHandlerUtil.JDBCDB2DAOClassName(getCurrentClass());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String extendedJDBCDB2DAOClassName() throws Wj2eeException {
		return TagHandlerUtil.extendedJDBCDB2DAOClassName(getDocletContext(),
				getCurrentClass());
	}

	/***************************************************************************
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 **************************************************************************/
	public String pkClassName() throws Wj2eeException {
		return TagHandlerUtil.pkClassName(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String nomeClasseSemSufixoVO() {
		return TagHandlerUtil.nomeClasseSemSufixoVO(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String vlhPageSize() throws Wj2eeException {
		return TagHandlerUtil.vlhPageSize(getCurrentClass());
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
				getCurrentClass());
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
				getCurrentClass());
		if (!returnValue) {
			generate(template);
		}
	}

	/**
	 * @throws Wj2eeException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String forwardParaCadastroModuloMestre() throws Wj2eeException {

		String forward;
		try {
			forward = TagHandlerUtil.forwardParaCadastroModuloMestre(
					getCurrentClass(), getXJavaDoc());
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

		String forward;
		try {
			forward = TagHandlerUtil.forwardParaConsultaModuloMestre(
					getCurrentClass(), getXJavaDoc());
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

		String forward;
		try {
			forward = TagHandlerUtil.forwardParaCadastro(getCurrentClass(),
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
	public String forwardParaConsulta() throws Wj2eeException {

		String forward;
		try {
			forward = TagHandlerUtil.forwardParaConsulta(getCurrentClass(),
					getXJavaDoc());
		} catch (Wj2eeException e) {
			TagHandlerUtil.trataExcecao(e);
			throw e;
		}
		return forward;
	}

	/**
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String servicoSalvarItem() throws XDocletException, Wj2eeException {
		return TagHandlerUtil.servicoSalvarItem(getCurrentClass());
	}

	public String servicoFecharItem() throws XDocletException, Wj2eeException {
		return TagHandlerUtil.servicoFecharItem(getCurrentClass());
	}

	/**
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String servicoIncluirItem() throws XDocletException, Wj2eeException {
		return TagHandlerUtil.servicoIncluirItem(getCurrentClass());
	}

	/**
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String servicoRemoverItem() throws XDocletException, Wj2eeException {
		return TagHandlerUtil.servicoRemoverItem(getCurrentClass());
	}

	/**
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String servicoEditarItem() throws XDocletException, Wj2eeException {
		return TagHandlerUtil.servicoEditarItem(getCurrentClass());
	}

	/**
	 * @throws XDocletException
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String servicoConsultarItem() throws XDocletException,
			Wj2eeException {
		return TagHandlerUtil.servicoConsultarItem(getCurrentClass());
	}

	public void ifIsEscravoMuitos(String template) throws XDocletException,
			Wj2eeException {

		boolean returnValue = TagHandlerUtil
				.ifIsEscravoMuitos(getCurrentClass());
		if (returnValue) {
			generate(template);
		}
	}

	public void ifIsEscravoUm(String template) throws XDocletException,
			Wj2eeException {

		boolean returnValue = TagHandlerUtil.ifIsEscravoUm(getCurrentClass());
		if (returnValue) {
			generate(template);
		}
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

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String primeiroElementoCadastro() throws Wj2eeException {
		return TagHandlerUtil.primeiroElementoCadastro(getCurrentClass());
	}

	/**
	 * @throws Wj2eeException
	 * @doc.tag type = "content"
	 */
	public String primeiroElementoPesquisa() throws Wj2eeException {
		return TagHandlerUtil.primeiroElementoPesquisa(getCurrentClass());
	}
	
	

}