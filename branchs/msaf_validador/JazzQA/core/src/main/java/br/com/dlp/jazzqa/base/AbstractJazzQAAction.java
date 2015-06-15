package br.com.dlp.jazzqa.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.servicelocator.IServiceLocator;
import br.com.dlp.framework.servicelocator.ServiceLocatorException;
import br.com.dlp.framework.struts.action.ActionException;
import br.com.dlp.framework.struts.action.BaseCadastroVLHAction;
import br.com.dlp.framework.vo.ICadastroBaseVO;
import br.com.dlp.framework.vo.IUsuarioVO;
import br.com.dlp.jazzqa.util.servicelocator.JazzQAServiceLocator;


/**
 * Base para implementação de actions do sistema JazzQA
 */
public abstract class AbstractJazzQAAction extends BaseCadastroVLHAction {

	/** Override do metodo atualizarIBaseVOSelecionado da superclasse */
	protected ICadastroBaseVO atualizarIBaseVOSelecionado(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response, ICadastroBaseVO baseVO)
			throws BaseBusinessException, BaseTechnicalError {

		return baseVO;
	}

	/** Override do metodo getUsuarioLogado da superclasse */
	public IUsuarioVO getUsuarioLogado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	public Class getServiceLocatorClass() throws ActionException {
		return JazzQAServiceLocator.class;
	}
	
	
	public IServiceLocator getServiceLocator(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ActionException {
		
		IServiceLocator serviceLocator = super.getServiceLocator(mapping, form, request, response);
		try {  
			serviceLocator.setRemoteFacades(true); 
		} catch (ServiceLocatorException e) {
			throw new ActionException("Erro ao tentar configurar locator como local",e);
		}
		return serviceLocator;
	}
	
}