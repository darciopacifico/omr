package br.com.dlp.framework.struts.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;

public abstract class BaseRelatorioForm extends BaseCadastroForm {
	private String relatorioTipo = "";

	private Collection relatorioTipos = new ArrayList();

	public abstract String getUrlRelatorio();

	public abstract void setUrlRelatorio(String urlRelatorio);

	public String getRelatorioTipo() {
		return relatorioTipo;
	}

	public Collection getRelatorioTipos() {
		return relatorioTipos;
	}

	public void setRelatorioTipo(String relatorioTipo) {
		this.relatorioTipo = relatorioTipo;
	}

	public void setRelatorioTipos(Collection relatorioTipos) {
		this.relatorioTipos = relatorioTipos;
	}

	public abstract Map getParametros(ActionMapping mapping,
			HttpServletRequest request) throws BaseTechnicalError,
			BaseBusinessException;

}
