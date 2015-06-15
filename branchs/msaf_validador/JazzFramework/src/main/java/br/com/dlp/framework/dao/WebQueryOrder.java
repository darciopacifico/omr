package br.com.dlp.framework.dao;

import br.com.dlp.framework.util.FrameworkAcheUtil;

public class WebQueryOrder extends QueryOrder {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4101943656234805392L;

	public static final String TIPO_ORDENACAO_ASC = "asc";

	public static final String TIPO_ORDENACAO_DESC = "desc";

	public static final String TIPO_ORDENACAO_VAZIA = "";

	private boolean considerar;

	public boolean isConsiderar() {
		String campo = getCampo();
		if (FrameworkAcheUtil.isNullOrEmpty(campo)) {
			considerar = false;
		}

		return considerar;
	}

	public WebQueryOrder(String campo, boolean asc) {
		super(campo, asc);
	}

	public WebQueryOrder(String campo) {
		super(campo);
	}

	public String getTipoOrdenacao() {
		String campo = getCampo();
		boolean asc = isAsc();

		if (FrameworkAcheUtil.isNullOrEmptyOrZero(campo) || !considerar) {
			return TIPO_ORDENACAO_VAZIA;

		} else if (asc) {
			return TIPO_ORDENACAO_ASC;

		} else {
			return TIPO_ORDENACAO_DESC;

		}
	}

	public void setTipoOrdenacao(String tipoOrdenacao) {
		if (tipoOrdenacao != null) {

			if (tipoOrdenacao.equals(TIPO_ORDENACAO_ASC)) {
				setAsc(true);
				considerar = true;

			} else if (tipoOrdenacao.equals(TIPO_ORDENACAO_DESC)) {
				setAsc(false);
				considerar = true;

			} else {
				setAsc(true);
				considerar = false;
			}
		}
	}

}
