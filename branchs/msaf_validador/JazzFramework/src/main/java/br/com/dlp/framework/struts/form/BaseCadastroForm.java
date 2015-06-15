package br.com.dlp.framework.struts.form;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import br.com.dlp.framework.struts.action.ActionException;
import br.com.dlp.framework.util.FrameworkAcheUtil;
import br.com.dlp.framework.util.FrameworkAcheUtilError;
import br.com.dlp.framework.vo.ICadastroBaseVO;
import br.com.dlp.framework.vo.IUsuarioVO;
import br.com.dlp.framework.vowrapper.IVOWrapper;
import br.com.dlp.framework.vowrapper.VOWrapperException;

public abstract class BaseCadastroForm extends BaseForm {

	private ICadastroBaseVO iBaseVO;

	private IUsuarioVO iUsuarioVOLogado;

	private List resultadoPesquisa;

	public IUsuarioVO getIUsuarioVOLogado() {
		return iUsuarioVOLogado;
	}

	public void setIUsuarioVOLogado(IUsuarioVO usuarioVOLogado) {
		iUsuarioVOLogado = usuarioVOLogado;
	}

	public String getPesquisaIdSelecionado() {
		return pesquisaIdSelecionado;
	}

	public void setPesquisaIdSelecionado(String pesquisaIdSelecionado) {
		this.pesquisaIdSelecionado = pesquisaIdSelecionado;
	}

	public IVOWrapper getWrapper() {
		return wrapper;
	}

	public void setWrapper(IVOWrapper wrapper) {
		this.wrapper = wrapper;
	}

	private String pesquisaIdSelecionado;

	private IVOWrapper wrapper;

	protected IVOWrapper createIVOWrapper(Class voWrapperClass, ResourceBundle resourceBundle) throws VOWrapperException, ActionException {
		IVOWrapper wrapper;

		Class classes[] = new Class[] { ResourceBundle.class };
		Object objects[] = new Object[] { resourceBundle };

		try {
			wrapper = (IVOWrapper) FrameworkAcheUtil.instanciaObjeto(voWrapperClass, classes, objects);
		} catch (FrameworkAcheUtilError e) {
			throw new VOWrapperException("Nao foi possivel instanciar a implementacao de IVOWrapper " + voWrapperClass.getName() + "!", e);
		}

		return wrapper;
	}

	public IVOWrapper getIVOWrapper(ResourceBundle resourceBundle) throws VOWrapperException, ActionException {

		if (wrapper == null) {
			Class voWrapperClass = getWrapperClass();
			wrapper = createIVOWrapper(voWrapperClass, resourceBundle);
		} else {
			wrapper.setResourceBundle(resourceBundle);
		}

		return wrapper;
	}

	public IVOWrapper getIVOWrapper() {

		return wrapper;
	}

	public void setIVOWrapper(IVOWrapper wrapper) {
		this.wrapper = wrapper;
	}

	protected abstract Class getWrapperClass();

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);

	}

	public List getResultadoPesquisa() {
		return resultadoPesquisa;
	}

	public void setResultadoPesquisa(List resultadoPesquisa) {
		this.resultadoPesquisa = resultadoPesquisa;
	}

	public ICadastroBaseVO getIBaseVO() {
		return iBaseVO;
	}

	public void setIBaseVO(ICadastroBaseVO baseVO) {
		this.iBaseVO = baseVO;
	}

	public ICadastroBaseVO getBaseVOSelecionado() {
		if(pesquisaIdSelecionado==null){
			logger.warn("Atencao! Nao foi possivel identificar o item selecionado(pesquisaIdSelecionado==null)!");
			throw new NullPointerException("Atencao! Nao foi possivel identificar o item selecionado(pesquisaIdSelecionado==null)!");
		}
		
		int idSelecionado = new Integer(pesquisaIdSelecionado).intValue();
		ICadastroBaseVO baseVO = (ICadastroBaseVO) resultadoPesquisa.get(idSelecionado);
		return baseVO;
	}

}