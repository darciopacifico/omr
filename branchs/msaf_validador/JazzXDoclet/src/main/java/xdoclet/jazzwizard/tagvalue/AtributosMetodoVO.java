/*
 * Data de Criacao 27/05/2005 22:43:41
 * 
 * Propriedade intelectual de Darcio L Pacifico
 */
package xdoclet.jazzwizard.tagvalue;

import xdoclet.jazzwizard.Wj2eeException;
import xdoclet.jazzwizard.tagshandler.TagHandlerUtil;
import xjavadoc.XClass;
import xjavadoc.XMethod;

/**
 * Encapsula os valores e as regras para as tags de metodo do wizard
 * 
 * @author Darcio L Pacifico - 27/05/2005 22:43:41
 */
public class AtributosMetodoVO extends AbstractAtributosVO {

	private static final String ATTRIBUTE_VALIDREGEX = "validRegex";

	private static final String ATTRIBUTE_MASKVIEW = "maskView";

	private static final String ATTRIBUTE_MASKBUSINESS = "maskBusiness";

	private static final String ATTRIBUTE_VALIDATORVIEW = "validatorView";

	private static final String ATTRIBUTE_VALIDATORBUSINESS = "validatorBusiness";

	private static final String ATTRIBUTE_SISTEMA = "sistema";

	private static final String ATTRIBUTE_CLASSE = "classe";

	private static final String ATTRIBUTE_ORDEM = "ordem";

	private static final String ATTRIBUTE_IGNORAR = "ignorar";

	private static final String ATTRIBUTE_TAM_MINIMO = "tamanhoMinimo";

	private static final String ATTRIBUTE_TAM_MAXIMO = "tamanhoMaximo";

	private static final String ATTRIBUTE_ROWS = "rows";

	private static final String ATTRIBUTE_LARGURA_CAMPO = "larguraCampo";

	private static final String ATTRIBUTE_REQUERIDO = "requerido";

	private static final String ATTRIBUTE_DESCRICAO = "descricao";

	private static final String ATTRIBUTE_DICA = "dica";

	private static final String ATTRIBUTE_SOMENTE_LEITURA = "somenteLeitura";

	private static final String ATTRIBUTE_RENDERER = "renderer";

	private static final String ATTRIBUTE_NESTED_PROPERTY_LIST = "nestedPropertyList";

	private static final String ATTRIBUTE_LISTAVEL = "listavel";

	private static final String ATTRIBUTE_ORDENAVEL = "ordenavel";

	private static final String ATTRIBUTE_VISIVEL = "visivel";

	private static final String ATTRIBUTE_PESQUISAVEL = "pesquisavel";

	private static final String ATTRIBUTE_OPERADOR_PESQUISA = "operadorPesquisa";

	private static final String ATTRIBUTE_AUTO_FULFILLING = "autoFulfilling";

	private static final String ATTRIBUTE_PK = "pk";

	private static String[] ATTRIBUTES = { ATTRIBUTE_PK,
			ATTRIBUTE_NESTED_PROPERTY_LIST, ATTRIBUTE_SISTEMA,
			ATTRIBUTE_VALIDREGEX, ATTRIBUTE_DESCRICAO, ATTRIBUTE_DICA,
			ATTRIBUTE_IGNORAR, ATTRIBUTE_MASKVIEW, ATTRIBUTE_MASKBUSINESS,
			ATTRIBUTE_AUTO_FULFILLING, ATTRIBUTE_VALIDATORVIEW,
			ATTRIBUTE_VALIDATORBUSINESS, ATTRIBUTE_TAM_MINIMO,
			ATTRIBUTE_TAM_MAXIMO, ATTRIBUTE_ROWS, ATTRIBUTE_LARGURA_CAMPO,
			ATTRIBUTE_REQUERIDO, ATTRIBUTE_SOMENTE_LEITURA, ATTRIBUTE_RENDERER,
			ATTRIBUTE_CLASSE, ATTRIBUTE_ORDEM, ATTRIBUTE_PESQUISAVEL,
			ATTRIBUTE_OPERADOR_PESQUISA, ATTRIBUTE_LISTAVEL,
			ATTRIBUTE_ORDENAVEL, ATTRIBUTE_VISIVEL };

	private static final String ATTRIBUTE_DEFAULT_VALIDREGEX = "";

	private static final String ATTRIBUTE_DEFAULT_ORDEM = "0";

	private static final String ATTRIBUTE_DEFAULT_IGNORAR = "false";

	private static final String ATTRIBUTE_DEFAULT_SISTEMA = "";

	private static final String ATTRIBUTE_DEFAULT_VALIDATORVIEW = "";

	private static final String ATTRIBUTE_DEFAULT_VALIDATORBUSINESS = "";

	private static final String ATTRIBUTE_DEFAULT_MASKVIEW = "";

	private static final String ATTRIBUTE_DEFAULT_MASKBUSINESS = "";

	private static final String ATTRIBUTE_DEFAULT_TAM_MINIMO = "";

	private static final String ATTRIBUTE_DEFAULT_TAM_MAXIMO = "";

	private static final String ATTRIBUTE_DEFAULT_ROWS = "3";

	private static final String ATTRIBUTE_DEFAULT_LARGURA_CAMPO = "30";

	private static final String ATTRIBUTE_DEFAULT_REQUERIDO = "true";

	private static final String ATTRIBUTE_DEFAULT_SOMENTE_LEITURA = "false";

	private static final String ATTRIBUTE_DEFAULT_RENDERER = "text";

	private static final String ATTRIBUTE_DEFAULT_DICA = "Campo ':descricao' ";

	private static final String ATTRIBUTE_DEFAULT_NESTED_PROPERTY_LIST = "";

	private static final String ATTRIBUTE_DEFAULT_LISTAVEL = "false";

	private static final String ATTRIBUTE_DEFAULT_VISIVEL = "true";

	private static final String ATTRIBUTE_DEFAULT_PK = "false";

	private static final String ATTRIBUTE_DEFAULT_PESQUISAVEL = "false";

	private static final String ATTRIBUTE_DEFAULT_AUTO_FULFILLING = "false";

	private static final String ATTRIBUTE_DEFAULT_OPERADOR_PESQUISA = TagHandlerUtil.OPERADOR_EQUAL;

	private String maskView;

	private String maskBusiness;

	private String validatorView;

	private String validatorBusiness;

	private String ignorar;

	private String tamanhoMinimo;

	private String tamanhoMaximo;

	private String rows;

	private String larguraCampo;

	private String requerido;

	private String descricao;

	private String dica;

	private String somenteLeitura;

	private String renderer;

	private String ordem;

	private String classe;

	private String validRegex;

	private String nestedPropertyList;

	private String listavel;

	private String ordenavel;

	private String visivel;

	private String pesquisavel;

	private String operadorPesquisa;

	private String pk;

	private String sistema;

	private XMethod method;

	private String autoFulfilling;

	public AtributosMetodoVO(XMethod method) throws Wj2eeException {
		super(getXDoc(method));
		this.method = method;

		initializeAttributes(ATTRIBUTES);

	}

	/** Override do metodo getDefaultValue da superclasse */
	protected String getDefaultValue(String chave) throws AtributosException {

		String returnValue;
		if (chave.equals(ATTRIBUTE_IGNORAR)) {
			returnValue = ATTRIBUTE_DEFAULT_IGNORAR;

		} else if (chave.equals(ATTRIBUTE_MASKVIEW)) {
			returnValue = ATTRIBUTE_DEFAULT_MASKVIEW;

		} else if (chave.equals(ATTRIBUTE_MASKBUSINESS)) {
			returnValue = ATTRIBUTE_DEFAULT_MASKBUSINESS;

		} else if (chave.equals(ATTRIBUTE_VALIDATORVIEW)) {
			returnValue = ATTRIBUTE_DEFAULT_VALIDATORVIEW;

		} else if (chave.equals(ATTRIBUTE_VALIDATORBUSINESS)) {
			returnValue = ATTRIBUTE_DEFAULT_VALIDATORBUSINESS;

		} else if (chave.equals(ATTRIBUTE_ROWS)) {
			returnValue = ATTRIBUTE_DEFAULT_ROWS;

		} else if (chave.equals(ATTRIBUTE_TAM_MAXIMO)) {
			returnValue = ATTRIBUTE_DEFAULT_TAM_MAXIMO;

		} else if (chave.equals(ATTRIBUTE_TAM_MINIMO)) {
			returnValue = ATTRIBUTE_DEFAULT_TAM_MINIMO;

		} else if (chave.equals(ATTRIBUTE_LARGURA_CAMPO)) {

			XClass propertyType = method.getPropertyType().getType();

			if (propertyType.isA(TagHandlerUtil.CLASS_NAME_BASEVO)) {
				returnValue = "50";

			} else if (propertyType.isA(TagHandlerUtil.CLASS_NAME_DATE)) {
				returnValue = "13";

			} else if (propertyType.isA(TagHandlerUtil.CLASS_NAME_NUMBER)) {
				returnValue = "13";

			} else if (propertyType.isA(TagHandlerUtil.CLASS_NAME_STRING)) {
				returnValue = "50";

			} else {
				returnValue = ATTRIBUTE_DEFAULT_LARGURA_CAMPO;
			}

		} else if (chave.equals(ATTRIBUTE_REQUERIDO)) {
			returnValue = ATTRIBUTE_DEFAULT_REQUERIDO;

		} else if (chave.equals(ATTRIBUTE_DICA)) {
			String descricao = getDescricao();
			returnValue = ATTRIBUTE_DEFAULT_DICA.replaceFirst(":descricao",
					descricao);

		} else if (chave.equals(ATTRIBUTE_DESCRICAO)) {
			returnValue = method.getNameWithoutPrefix();
			returnValue = returnValue.replaceFirst("VO", "");

		} else if (chave.equals(ATTRIBUTE_SOMENTE_LEITURA)) {
			returnValue = ATTRIBUTE_DEFAULT_SOMENTE_LEITURA;

		} else if (chave.equals(ATTRIBUTE_RENDERER)) {
			returnValue = ATTRIBUTE_DEFAULT_RENDERER;

		} else if (chave.equals(ATTRIBUTE_ORDEM)) {
			returnValue = ATTRIBUTE_DEFAULT_ORDEM;

		} else if (chave.equals(ATTRIBUTE_VALIDREGEX)) {
			returnValue = ATTRIBUTE_DEFAULT_VALIDREGEX;

		} else if (chave.equals(ATTRIBUTE_NESTED_PROPERTY_LIST)) {
			returnValue = ATTRIBUTE_DEFAULT_NESTED_PROPERTY_LIST;

		} else if (chave.equals(ATTRIBUTE_PESQUISAVEL)) {
			returnValue = ATTRIBUTE_DEFAULT_PESQUISAVEL;

		} else if (chave.equals(ATTRIBUTE_VISIVEL)) {
			returnValue = ATTRIBUTE_DEFAULT_VISIVEL;

		} else if (chave.equals(ATTRIBUTE_ORDENAVEL)) {
			returnValue = getListavel();

		} else if (chave.equals(ATTRIBUTE_OPERADOR_PESQUISA)) {
			XClass class1 = method.getPropertyType().getType();

			if (class1.isA(TagHandlerUtil.CLASS_NAME_BASEVO)) {
				returnValue = TagHandlerUtil.OPERADOR_EQUAL;

			} else if (class1.isA(TagHandlerUtil.CLASS_NAME_COLLECTION)) {
				returnValue = TagHandlerUtil.OPERADOR_EQUAL;

			} else if (class1.isA(TagHandlerUtil.CLASS_NAME_DATE)) {
				returnValue = TagHandlerUtil.OPERADOR_RANGE_VALUE;

			} else if (class1.isA(TagHandlerUtil.CLASS_NAME_NUMBER)) {

				if (getPk().equals("true")) {
					returnValue = TagHandlerUtil.OPERADOR_EQUAL;
				} else {
					returnValue = TagHandlerUtil.OPERADOR_RANGE_VALUE;
				}

			} else if (class1.isA(TagHandlerUtil.CLASS_NAME_STRING)) {
				returnValue = TagHandlerUtil.OPERADOR_LIKE;

			} else {
				returnValue = ATTRIBUTE_DEFAULT_OPERADOR_PESQUISA;
			}

		} else if (chave.equals(ATTRIBUTE_LISTAVEL)) {
			returnValue = ATTRIBUTE_DEFAULT_LISTAVEL;

		} else if (chave.equals(ATTRIBUTE_PK)) {
			returnValue = ATTRIBUTE_DEFAULT_PK;

		} else if (chave.equals(ATTRIBUTE_SISTEMA)) {
			returnValue = ATTRIBUTE_DEFAULT_SISTEMA;

		} else if (chave.equals(ATTRIBUTE_AUTO_FULFILLING)) {
			returnValue = ATTRIBUTE_DEFAULT_AUTO_FULFILLING;

		} else if (chave.equals(ATTRIBUTE_CLASSE)) {

			XClass class1 = method.getPropertyType().getType();
			if (method.isPropertyAccessor()
					&& TagHandlerUtil.isInstanceOf(class1,
							TagHandlerUtil.CLASS_NAME_COLLECTION)) {
				throw new AtributosException(
						"Para atributos que são instanceof "
								+ "Collection é obrigatório informar o "
								+ "atributo 'classe' no método getter");
			}

			returnValue = "";

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
	 * Metodo Getter do atributo dica.
	 */
	public String getDica() {
		return dica;
	}

	/**
	 * Metodo Getter do atributo ignorar.
	 */
	public String getIgnorar() {
		return ignorar;
	}

	/**
	 * Metodo Getter do atributo renderer.
	 */
	public String getRenderer() {
		return renderer;
	}

	/**
	 * Metodo Getter do atributo requerido.
	 */
	public String getRequerido() {
		return requerido;
	}

	/**
	 * Metodo Getter do atributo somenteLeitura.
	 */
	public String getSomenteLeitura() {
		return somenteLeitura;
	}

	/**
	 * Metodo Getter do atributo tamanhoMaximo.
	 */
	public String getTamanhoMaximo() {
		return tamanhoMaximo;
	}

	/**
	 * Metodo Getter do atributo tamanhoMinimo.
	 */
	public String getTamanhoMinimo() {
		return tamanhoMinimo;
	}

	public String getRows() {
		return rows;
	}

	public String getAutoFulfilling() {
		return autoFulfilling;
	}

	public void setAutoFulfilling(String autoFulfilling) {
		this.autoFulfilling = autoFulfilling;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getLarguraCampo() {
		return larguraCampo;
	}

	public void setLarguraCampo(String larguraCampo) {
		this.larguraCampo = larguraCampo;
	}

	/**
	 * Metodo Setter do atributo descricao.
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Metodo Setter do atributo dica.
	 */
	public void setDica(String dica) {
		this.dica = dica;
	}

	/**
	 * Metodo Setter do atributo ignorar.
	 */
	public void setIgnorar(String ignorar) {
		this.ignorar = ignorar;
	}

	/**
	 * Metodo Setter do atributo renderer.
	 */
	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}

	/**
	 * Metodo Setter do atributo requerido.
	 */
	public void setRequerido(String requerido) {
		this.requerido = requerido;
	}

	/**
	 * Metodo Setter do atributo somenteLeitura.
	 */
	public void setSomenteLeitura(String somenteLeitura) {
		this.somenteLeitura = somenteLeitura;
	}

	/**
	 * Metodo Setter do atributo tamanhoMaximo.
	 */
	public void setTamanhoMaximo(String tamanhoMaximo) {
		this.tamanhoMaximo = tamanhoMaximo;
	}

	/**
	 * Metodo Setter do atributo tamanhoMinimo.
	 */
	public void setTamanhoMinimo(String tamanhoMinimo) {
		this.tamanhoMinimo = tamanhoMinimo;
	}

	/** Metodo Getter do atributo classe. */
	public String getClasse() {
		return classe;
	}

	/** Metodo Setter do atributo classe. */
	public void setClasse(String classe) {
		this.classe = classe;
	}

	/**
	 * Metodo Getter do atributo ordem.
	 */
	public String getOrdem() {
		return ordem;
	}

	/**
	 * Metodo Setter do atributo ordem.
	 */
	public void setOrdem(String ordem) {
		this.ordem = ordem;
	}

	/** Metodo Getter do atributo nestedPropertyList. */
	public String getNestedPropertyList() {
		return nestedPropertyList;
	}

	/** Metodo Setter do atributo nestedPropertyList. */
	public void setNestedPropertyList(String nestedPropertyList) {
		this.nestedPropertyList = nestedPropertyList;
	}

	public String getPesquisavel() {
		return pesquisavel;
	}

	public void setPesquisavel(String pesquisavel) {
		this.pesquisavel = pesquisavel;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getListavel() {
		return listavel;
	}

	public void setListavel(String listavel) {
		this.listavel = listavel;
	}

	public String getMaskView() {
		return maskView;
	}

	public String getValidatorBusiness() {
		return validatorBusiness;
	}

	public String getValidatorView() {
		return validatorView;
	}

	public String getValidRegex() {
		return validRegex;
	}

	public void setMaskView(String maskView) {
		this.maskView = maskView;
	}

	public void setValidatorBusiness(String validatorBusiness) {
		this.validatorBusiness = validatorBusiness;
	}

	public void setValidatorView(String validatorView) {
		this.validatorView = validatorView;
	}

	public void setValidRegex(String validRegex) {
		this.validRegex = validRegex;
	}

	/**
	 * @return Returns the maskBusiness.
	 */
	public String getMaskBusiness() {
		return maskBusiness;
	}

	/**
	 * @param maskBusiness
	 *            The maskBusiness to set.
	 */
	public void setMaskBusiness(String maskBusiness) {
		this.maskBusiness = maskBusiness;
	}

	public String getVisivel() {
		return visivel;
	}

	public void setVisivel(String visivel) {
		this.visivel = visivel;
	}

	/**
	 * @return Returns the operadorPesquisa.
	 */
	public String getOperadorPesquisa() {
		return operadorPesquisa;
	}

	/**
	 * @param operadorPesquisa
	 *            The operadorPesquisa to set.
	 */
	public void setOperadorPesquisa(String operadorPesquisa) {
		this.operadorPesquisa = operadorPesquisa;
	}

	public String getOrdenavel() {
		return ordenavel;
	}

	public void setOrdenavel(String ordenavel) {
		this.ordenavel = ordenavel;
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
}