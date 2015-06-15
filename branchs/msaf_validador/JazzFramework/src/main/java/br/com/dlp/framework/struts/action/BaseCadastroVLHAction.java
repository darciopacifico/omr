package br.com.dlp.framework.struts.action;

import java.rmi.RemoteException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.facade.IValueListHandlerFacade;
import br.com.dlp.framework.struts.form.BaseCadastroVLHForm;

/**
 * Abstração de Action que contém funcionalidades de paginação para módulos de
 * cadastro
 */
public abstract class BaseCadastroVLHAction extends BaseCadastroAction {
	public static final String PARAM_VLH_FACADE = "VLH_FACADE";

	public static final String PARAM_MODULE_VLH_ID_ATUAL = "MODULE_VLH_ID";

	public static final String SERVICE_VLH_NEXT_PAGE = "vlhNextPage";

	public static final String SERVICE_VLH_PREVIOUS_PAGE = "vlhPreviousPage";

	public static final String SERVICE_VLH_FIRST_PAGE = "vlhFirstPage";

	public static final String SERVICE_VLH_LAST_PAGE = "vlhLastPage";

	public static final String SERVICE_VLH_PAGE_INDEX = "vlhPageByIndex";

	/**
	 * Servico para Value List Handler
	 * 
	 * @throws RemoteException
	 */
	public ActionForward vlhNextPage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws BaseBusinessException, BaseTechnicalError, RemoteException {

		/* cast do form */
		BaseCadastroVLHForm baseCadastroForm = (BaseCadastroVLHForm) form;

		/* recupera VLHFacade */
		IValueListHandlerFacade valueListHandlerFacade = (IValueListHandlerFacade) getVLHFacade(
				mapping, form, request, response);

		/* recupera a página requerida */
		if (logger.isDebugEnabled()) {
			logger.debug("Recuperando a pagina atual do VLH");
		}
		List page = valueListHandlerFacade.getNextPage();
		baseCadastroForm.setResultadoPesquisa(page);

		/* RECUPERA O ÍNDICE DA PÁGINA DO VLH ATUAL */
		int pageIndex = valueListHandlerFacade.getPageIndex();
		baseCadastroForm.setPaginaVLH(pageIndex);

		/* encaminha para a página de pesquisa */
		return mapping.findForward(FORWARD_TELA_PESQUISA);
	}

	/**
	 * Servico para Value List Handler
	 * 
	 * @throws RemoteException
	 */
	public ActionForward vlhPreviousPage(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws BaseBusinessException,
			BaseTechnicalError, RemoteException {

		/* cast do form */
		BaseCadastroVLHForm baseCadastroForm = (BaseCadastroVLHForm) form;

		/* recupera IValueListHandlerFacade */
		IValueListHandlerFacade valueListHandlerFacade = (IValueListHandlerFacade) getVLHFacade(
				mapping, form, request, response);

		/* recupera a página requerida */
		if (logger.isDebugEnabled()) {
			logger.debug("Recuperando a pagina atual do VLH");
		}
		List page = valueListHandlerFacade.getPreviousPage();
		baseCadastroForm.setResultadoPesquisa(page);

		/* RECUPERA O ÍNDICE DA PÁGINA DO VLH ATUAL */
		int pageIndex = valueListHandlerFacade.getPageIndex();
		baseCadastroForm.setPaginaVLH(pageIndex);

		/* encaminha para a página de pesquisa */
		return mapping.findForward(FORWARD_TELA_PESQUISA);
	}

	/**
	 * Servico para Value List Handler
	 * 
	 * @throws RemoteException
	 */
	public ActionForward vlhFirstPage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws BaseBusinessException, BaseTechnicalError, RemoteException {

		/* cast do form */
		BaseCadastroVLHForm baseCadastroForm = (BaseCadastroVLHForm) form;

		/* recupera IValueListHandlerFacade */
		IValueListHandlerFacade valueListHandlerFacade = (IValueListHandlerFacade) getVLHFacade(
				mapping, form, request, response);

		/* recupera a página requerida */
		if (logger.isDebugEnabled()) {
			logger.debug("Recuperando a pagina atual do VLH");
		}
		List page = valueListHandlerFacade.getFirstPage();
		baseCadastroForm.setResultadoPesquisa(page);

		/* RECUPERA O ÍNDICE DA PÁGINA DO VLH ATUAL */
		int pageIndex = valueListHandlerFacade.getPageIndex();
		baseCadastroForm.setPaginaVLH(pageIndex);

		/* encaminha para a página de pesquisa */
		return mapping.findForward(FORWARD_TELA_PESQUISA);
	}

	/**
	 * Servico para Value List Handler
	 * 
	 * @throws RemoteException
	 */
	public ActionForward vlhLastPage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws BaseBusinessException, BaseTechnicalError, RemoteException {

		/* cast do form */
		BaseCadastroVLHForm baseCadastroForm = (BaseCadastroVLHForm) form;

		/* recupera IValueListHandlerFacade */
		IValueListHandlerFacade valueListHandlerFacade = (IValueListHandlerFacade) getVLHFacade(
				mapping, form, request, response);

		/* recupera a página requerida */
		if (logger.isDebugEnabled()) {
			logger.debug("Recuperando a pagina atual do VLH");
		}
		List page = valueListHandlerFacade.getLastPage();
		baseCadastroForm.setResultadoPesquisa(page);

		/* RECUPERA O ÍNDICE DA PÁGINA DO VLH ATUAL */
		int pageIndex = valueListHandlerFacade.getPageIndex();
		baseCadastroForm.setPaginaVLH(pageIndex);

		/* encaminha para a página de pesquisa */
		return mapping.findForward(FORWARD_TELA_PESQUISA);
	}

	/**
	 * Servico para Value List Handler
	 * 
	 * @throws RemoteException
	 */
	public ActionForward vlhPageByIndex(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws BaseBusinessException, BaseTechnicalError, RemoteException {

		/* cast do form */
		BaseCadastroVLHForm baseCadastroFormVLH = (BaseCadastroVLHForm) form;

		/* recupera IValueListHandlerFacade */
		IValueListHandlerFacade valueListHandlerFacade = (IValueListHandlerFacade) getVLHFacade(
				mapping, form, request, response);

		/* recupera o índice de página selecionado */
		int pageIndex = baseCadastroFormVLH.getPaginaVLH();
		if (logger.isDebugEnabled()) {
			logger.debug("Índice de página do VLH selecionado:" + pageIndex);
		}

		/* recupera a página requerida */
		if (logger.isDebugEnabled()) {
			logger.debug("Recuperando a pagina atual do VLH");
		}
		List page = valueListHandlerFacade.getPage(pageIndex);
		baseCadastroFormVLH.setResultadoPesquisa(page);

		/* encaminha para a página de pesquisa */
		return mapping.findForward(FORWARD_TELA_PESQUISA);
	}

	/**
	 * Configura no forma a quantidade de páginas contidas no VLH
	 */
	protected void setQtdPaginas(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws BaseTechnicalError, RemoteException {

		/* recupera IValueListHandlerFacade */
		IValueListHandlerFacade valueListHandlerFacade = (IValueListHandlerFacade) getVLHFacade(
				mapping, form, request, response);

		/* cast do form */
		BaseCadastroVLHForm baseCadastroFormVLH = null;

		try {
			baseCadastroFormVLH = (BaseCadastroVLHForm) form;
		} catch (ClassCastException e) {
			throw new BaseTechnicalError(
					"Para utilizar uma implementação de BaseCadastroVLHAction, "
							+ "o ActionForm deve ser implementação de BaseCadastroVLHForm");
		}

		/* seta Quantidade de páginas */
		baseCadastroFormVLH.setQtdPaginas(valueListHandlerFacade.getPages());

	}

	/**
	 * Recupera a implementação de IValueListHandlerFacade do módulo
	 */
	public IValueListHandlerFacade getVLHFacade(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws BaseTechnicalError,
			RemoteException {

		IValueListHandlerFacade valueListHandlerFacade;

		/* FACADE PARA ESTE MODULO */
		String moduleVLHId = getModuleVLHId();

		/* FACADE Q ESTA NA SESSAO */
		String sessionModuleVLHId = (String) request.getSession().getAttribute(
				PARAM_MODULE_VLH_ID_ATUAL);

		if (sessionModuleVLHId == null) {
			/* caso ainda nao exista facade na sessao */
			valueListHandlerFacade = (IValueListHandlerFacade) getFacade(
					mapping, form, request, response, moduleVLHId);

		} else if (!sessionModuleVLHId.equals(moduleVLHId)) {
			/* caso exista mas seja do tipo antigo */
			finalizar(mapping, form, request, response);
			valueListHandlerFacade = (IValueListHandlerFacade) getFacade(
					mapping, form, request, response, moduleVLHId);

		} else {
			/* caso exista e seja do tipo esperado */
			valueListHandlerFacade = (IValueListHandlerFacade) request
					.getSession().getAttribute(PARAM_VLH_FACADE);

		}

		request.getSession().setAttribute(PARAM_MODULE_VLH_ID_ATUAL,
				moduleVLHId);
		request.getSession().setAttribute(PARAM_VLH_FACADE,
				valueListHandlerFacade);

		return valueListHandlerFacade;
	}

}
