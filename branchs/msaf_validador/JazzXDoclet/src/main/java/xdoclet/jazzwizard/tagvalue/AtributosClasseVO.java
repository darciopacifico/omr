/*
 * Data de Criacao 27/05/2005 22:42:47
 *
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.tagvalue;

import xdoclet.jazzwizard.Wj2eeException;
import xjavadoc.XClass;
import xjavadoc.XPackage;

/**
 * Encapsula os valores e as regras para as tags de classe do wizard
 * 
 * @author Darcio L Pacifico - 27/05/2005 22:42:47
 */
public class AtributosClasseVO extends AbstractAtributosVO {
	private XClass xClass;

	private static final String ATTRIBUTE_ENTIDADE = "entidade";

	private static final String ATTRIBUTE_NOME = "nome";

	private static final String ATTRIBUTE_UC = "uc";

	private static final String ATTRIBUTE_DESCRICAO = "descricao";

	private static final String ATTRIBUTE_IGNORAR = "ignorar";

	private static final String ATTRIBUTE_GERAR_MODEL = "gerarModel";

	private static final String ATTRIBUTE_GERAR_PERSISTENCIA = "gerarPersistencia";

	private static final String ATTRIBUTE_GERAR_VIEW = "gerarView";

	private static final String ATTRIBUTE_GERAR_CONTROLLER = "gerarController";

	private static final String ATTRIBUTE_RENDERER = "renderer";

	private static final String ATTRIBUTE_BASEENTITY = "baseentity";

	private static final String ATTRIBUTE_VOMESTRE = "voMestre";

	private static final String ATTRIBUTE_VLH_PAGESIZE = "vlhPageSize";

	public static final String RENDERER_MODULO = "modulo";

	public static final String RENDERER_INDEXADO = "indexado";

	public static final String RENDERER_LOCAL = "local";

	public static final String RENDERER_MODULOESCRAVO = "moduloescravo";

	private static String[] ATTRIBUTES = { ATTRIBUTE_BASEENTITY,
			ATTRIBUTE_ENTIDADE, ATTRIBUTE_NOME, ATTRIBUTE_DESCRICAO,
			ATTRIBUTE_IGNORAR, ATTRIBUTE_GERAR_MODEL,
			ATTRIBUTE_GERAR_PERSISTENCIA, ATTRIBUTE_RENDERER,
			ATTRIBUTE_GERAR_VIEW, ATTRIBUTE_GERAR_CONTROLLER,
			ATTRIBUTE_VLH_PAGESIZE, ATTRIBUTE_VOMESTRE, ATTRIBUTE_UC };

	private static final String ATTRIBUTE_DEFAULT_IGNORAR = "false";

	private static final String ATTRIBUTE_DEFAULT_GERAR_MODEL = "true";

	private static final String ATTRIBUTE_DEFAULT_GERAR_PERSISTENCIA = "true";

	private static final String ATTRIBUTE_DEFAULT_GERAR_VIEW = "true";

	private static final String ATTRIBUTE_DEFAULT_GERAR_CONTROLLER = "true";

	private static final String ATTRIBUTE_DEFAULT_RENDERER = RENDERER_MODULO;

	private static final String ATTRIBUTE_DEFAULT_BASEENTITY = "false";

	private static final String ATTRIBUTE_DEFAULT_VLH_PAGESIZE = "15";

	private static final String ATTRIBUTE_DEFAULT_VOMESTRE = "";

	private static final String ATTRIBUTE_DEFAULT_UC = "XXX";

	private String entidade;

	private String nome;

	private String descricao;

	private String ignorar;

	private String pacote;

	private String gerarModel;

	private String gerarPersistencia;

	private String gerarView;

	private String gerarController;

	private String renderer;

	private String baseentity;

	private String vlhPageSize;

	private String voMestre;

	private String uc;

	/** Override do metodo initializeAttributes da superclasse */
	protected void initializeAttributes(String[] attributes)
			throws AtributosException {

		XClass xClass = getXClass();
		XPackage xPackage = xClass.getContainingPackage();

		setPacote(xPackage.getName());

		super.initializeAttributes(attributes);
	}

	/**
	 * Trata o XClass extraindo o XDoc para o super construtor
	 */
	public AtributosClasseVO(XClass xClass) throws Wj2eeException {

		super(getXDoc(xClass));
		this.xClass = xClass;
		initializeAttributes(ATTRIBUTES);
	}

	/** Override do metodo getDefaultValue da superclasse */
	protected String getDefaultValue(String chave) throws AtributosException {
		String returnValue;

		if (chave.equals(ATTRIBUTE_ENTIDADE)) {
			String className = xClass.getName();
			className = className.replaceFirst("VO", "");
			returnValue = className;

		} else if (chave.equals(ATTRIBUTE_NOME)) {
			returnValue = getEntidade();

		} else if (chave.equals(ATTRIBUTE_DESCRICAO)) {
			returnValue = getEntidade();

		} else if (chave.equals(ATTRIBUTE_IGNORAR)) {
			returnValue = ATTRIBUTE_DEFAULT_IGNORAR;

		} else if (chave.equals(ATTRIBUTE_GERAR_MODEL)) {
			returnValue = ATTRIBUTE_DEFAULT_GERAR_MODEL;

		} else if (chave.equals(ATTRIBUTE_GERAR_VIEW)) {
			returnValue = ATTRIBUTE_DEFAULT_GERAR_VIEW;

		} else if (chave.equals(ATTRIBUTE_GERAR_CONTROLLER)) {
			returnValue = ATTRIBUTE_DEFAULT_GERAR_CONTROLLER;

		} else if (chave.equals(ATTRIBUTE_GERAR_PERSISTENCIA)) {
			returnValue = ATTRIBUTE_DEFAULT_GERAR_PERSISTENCIA;

		} else if (chave.equals(ATTRIBUTE_RENDERER)) {
			returnValue = ATTRIBUTE_DEFAULT_RENDERER;

		} else if (chave.equals(ATTRIBUTE_BASEENTITY)) {
			returnValue = ATTRIBUTE_DEFAULT_BASEENTITY;

		} else if (chave.equals(ATTRIBUTE_VLH_PAGESIZE)) {
			returnValue = ATTRIBUTE_DEFAULT_VLH_PAGESIZE;

		} else if (chave.equals(ATTRIBUTE_VOMESTRE)) {
			returnValue = ATTRIBUTE_DEFAULT_VOMESTRE;

		} else if (chave.equals(ATTRIBUTE_UC)) {
			returnValue = ATTRIBUTE_DEFAULT_UC;

		} else {
			throw new AtributosException(
					"Não foi possível definir um valor default para o atrributo "
							+ chave);
		}

		return returnValue;
	}

	/**
	 * Metodo Getter do atributo descricao.
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Metodo Getter do atributo entidade.
	 */
	public String getEntidade() {
		return entidade;
	}

	/**
	 * Metodo Getter do atributo ignorar.
	 */
	public String getIgnorar() {
		return ignorar;
	}

	/**
	 * Metodo Getter do atributo nome.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Metodo Getter do atributo xClass.
	 */
	public XClass getXClass() {
		return xClass;
	}

	/**
	 * Metodo Setter do atributo descricao.
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Metodo Setter do atributo entidade.
	 */
	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}

	/**
	 * Metodo Setter do atributo ignorar.
	 */
	public void setIgnorar(String ignorar) {
		this.ignorar = ignorar;
	}

	/**
	 * Metodo Setter do atributo nome.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Metodo Setter do atributo xClass.
	 */
	public void setXClass(XClass class1) {
		xClass = class1;
	}

	/**
	 * Metodo Getter do atributo pacote.
	 */
	public String getPacote() {
		return pacote;
	}

	/**
	 * Metodo Setter do atributo pacote.
	 */
	public void setPacote(String pacote) {
		this.pacote = pacote;
	}

	/** Metodo Getter do atributo gerarController. */
	public String getGerarController() {
		return gerarController;
	}

	/** Metodo Getter do atributo gerarModel. */
	public String getGerarModel() {
		return gerarModel;
	}

	public String getUc() {
		return uc;
	}

	public void setUc(String uc) {
		this.uc = uc;
	}

	/** Metodo Setter do atributo gerarController. */
	public void setGerarController(String gerarController) {
		this.gerarController = gerarController;
	}

	/** Metodo Setter do atributo gerarModel. */
	public void setGerarModel(String gerarModel) {
		this.gerarModel = gerarModel;
	}

	/** Metodo Getter do atributo gerarView. */
	public String getGerarView() {
		return gerarView;
	}

	/** Metodo Setter do atributo gerarView. */
	public void setGerarView(String gerarView) {
		this.gerarView = gerarView;
	}

	/** Metodo Getter do atributo gerarPersistencia. */
	public String getGerarPersistencia() {
		return gerarPersistencia;
	}

	/** Metodo Setter do atributo gerarPersistencia. */
	public void setGerarPersistencia(String gerarPersistencia) {
		this.gerarPersistencia = gerarPersistencia;
	}

	/** Metodo Getter do atributo renderer. */
	public String getRenderer() {
		return renderer;
	}

	/** Metodo Setter do atributo renderer. */
	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}

	/** Metodo Getter do atributo baseentity. */
	public String getBaseentity() {
		return baseentity;
	}

	/** Metodo Setter do atributo baseentity. */
	public void setBaseentity(String baseentity) {
		this.baseentity = baseentity;
	}

	public String getVoMestre() {
		return voMestre;
	}

	public void setVoMestre(String voMestre) {
		this.voMestre = voMestre;
	}

	public String getVlhPageSize() {
		return vlhPageSize;
	}

	public void setVlhPageSize(String vlhPageSize) {
		this.vlhPageSize = vlhPageSize;
	}
}
