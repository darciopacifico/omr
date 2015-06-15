package br.com.dlp.framework.struts.action;

import java.rmi.RemoteException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.exception.LockException;
import br.com.dlp.framework.facade.ICadastroFacade;
import br.com.dlp.framework.facade.IFacade;
import br.com.dlp.framework.servicelocator.IServiceLocator;
import br.com.dlp.framework.struts.form.BaseCadastroForm;
import br.com.dlp.framework.struts.form.BaseForm;
import br.com.dlp.framework.util.FrameworkAcheUtil;
import br.com.dlp.framework.vo.ICadastroBaseVO;
import br.com.dlp.framework.vowrapper.IVOWrapper;

/**
 * @author darcio Classe Abstrata para implementação de módulos de cadastros básicos
 */
public abstract class BaseCadastroAction extends BaseAction {
	public static final String PARAM_FACADE = "FACADE";

	public static final String PARAM_MODULE_ID_ATUAL = "MODULE_ID";

	public static final String SERVICE_PESQUISAR = "pesquisar";

	public static final String SERVICE_PREPARA_INCLUIR = "preparaIncluir";

	public static final String SERVICE_PREPARA_ALTERAR = "preparaAlterar";

	public static final String SERVICE_PREPARA_CONSULTAR = "preparaConsultar";

	public static final String SERVICE_PREPARA_INCLUIR_ALTERAR = "preparaIncluirOuAlterar";

	public static final String SERVICE_EXCLUIR = "excluir";

	public static final String SERVICE_SALVAR = "salvar";

	public static final String SERVICE_FECHAR = "fechar";

	public static final String SERVICE_FINALIZAR = "finalizar";

	public static final String FORWARD_TELA_PESQUISA = "pesquisa";

	public static final String FORWARD_TELA_CADASTRO = "cadastro";

	public static final String FORWARD_TELA_CONSULTA = "consulta";

	/**
	 * Delega que o Business delegate desaloque recursos
	 * 
	 * @throws RemoteException
	 */
	protected void removeFacade(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseTechnicalError, RemoteException {
		if (logger.isDebugEnabled()) {
			logger.debug("ATENÇÃO!!  FINALIZANDO O BUSINESS DELEGATE!!....");
		}

		finalizar(mapping, form, request, response);
		if (logger.isDebugEnabled()) {
			logger.debug(".... BUSINESS DELEGATE FINALIZADO COM SUCESSO!!!");
		}
	}

	/**
	 * Serviço padrão invocado ao acessar módulos de cadastro
	 */
	protected String getDefaultService() throws ActionException {
		return SERVICE_PESQUISAR;
	}

	/**
	 * Apenas ajusta acaoEstado. Deve ser sobreescrito por um método de pesquisa real, sem deixar de chamar super.pesquisa()!
	 */
	public ActionForward pesquisar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseBusinessException, BaseTechnicalError, RemoteException {
		BaseForm baseForm = (BaseForm) form;
		baseForm.setAcaoEstado(BaseForm.ACAOESTADO_PESQUISA);
		return mapping.findForward(FORWARD_TELA_PESQUISA);
	}

	/**
	 * Prepara tela de cadastro para uma nova inclusao
	 * 
	 * @throws RemoteException
	 */
	public ActionForward preparaIncluir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseBusinessException, BaseTechnicalError, RemoteException {

		if (logger.isDebugEnabled()) {
			logger.debug("PREPARANDO TELA DE INCLUSAO");
		}

		BaseCadastroForm baseCadastroForm = (BaseCadastroForm) form;

		/* cria um vo deste módulo em branco (novo registro) */
		ICadastroBaseVO cadastroBaseVO = criarNovoVO(mapping, form, request, response);

		/* arquivo de internacionalizacao */
		ResourceBundle resourceBundle = getResourceBundle(mapping, baseCadastroForm, request, response);

		/* cria um IVOWrapper para este VO */
		IVOWrapper wrapper = baseCadastroForm.getIVOWrapper(resourceBundle);
		wrapper.setBaseVO(cadastroBaseVO);

		/** Executa tarefas comuns à preparação de alteração e inclusão * */
		preparaIncluirOuAlterar(mapping, form, request, response);

		return mapping.findForward(FORWARD_TELA_CADASTRO);
	}

	/**
	 * Implementar estratégia de lock de registro que será alterado
	 */
	protected ICadastroBaseVO lockObject(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, ICadastroBaseVO cadastroBaseVO) throws BaseBusinessException, BaseTechnicalError, RemoteException {
		ICadastroFacade cadastroFacade = getCadastroFacade(mapping, form, request, response);

		try {
			// cadastroFacade.lockObject(cadastroBaseVO);

			cadastroBaseVO = cadastroFacade.findByPrimaryKey(cadastroBaseVO.getIPK());

		} catch (LockException e) {
			appendDialogMessage(mapping, form, request, response, "Não foi possível reservar o registro para alteração. " + "Provavelmente outro usuário está alterando este registro (" + cadastroBaseVO + ")! ");
		}
		return cadastroBaseVO;
	}

	/**
	 * Prepara tela de cadastro para uma nova alteracao
	 * 
	 * @throws BaseTechnicalError
	 * @throws BaseBusinessException
	 * @throws RemoteException
	 */
	public ActionForward preparaAlterar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseBusinessException, BaseTechnicalError, RemoteException {
		BaseCadastroForm baseCadastroForm = (BaseCadastroForm) form;

		/* Selecionado VO que foi selecionado pelo usuário na tela */
		ICadastroBaseVO cadastroBaseVO = baseCadastroForm.getBaseVOSelecionado();

		/* invoca a estratégia de lock do registro que será alterado */
		//lockObject(mapping, form, request, response, cadastroBaseVO);

		/* Atualiza o VO selecionado */
		cadastroBaseVO = atualizarIBaseVOSelecionado(mapping, form, request, response, cadastroBaseVO);

		/* Arquivo de internacionalizacao. Será utilizado pelo wrapper */
		ResourceBundle resourceBundle = getResourceBundle(mapping, baseCadastroForm, request, response);

		/* Cria um IVOWrapper para este VO */
		IVOWrapper wrapper = baseCadastroForm.getIVOWrapper(resourceBundle);

		/* Coloca o VO dentro do Wrapper */
		wrapper.setBaseVO(cadastroBaseVO);

		/* Executa tarefas comuns à preparação de alteração e inclusão */
		preparaIncluirOuAlterar(mapping, form, request, response);

		/* Log */
		if (logger.isDebugEnabled()) {
			logger.debug("PREPARANDO TELA DE ALTERACAO. Modulo=" + getModuleId());
		}

		/* encaminha para a tela de cadastro */
		return mapping.findForward(FORWARD_TELA_CADASTRO);
	}

	/**
	 * Prepara tela de cadastro para uma nova alteracao
	 * 
	 * @throws BaseTechnicalError
	 * @throws BaseBusinessException
	 * @throws RemoteException
	 */
	public ActionForward preparaConsultar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseBusinessException, BaseTechnicalError, RemoteException {
		BaseCadastroForm baseCadastroForm = (BaseCadastroForm) form;
		ICadastroBaseVO cadastroBaseVO = baseCadastroForm.getBaseVOSelecionado();

		/* retorna o baseVO selecionado pelo usuário na tela de pesquisa */
		cadastroBaseVO = atualizarIBaseVOSelecionado(mapping, form, request, response, cadastroBaseVO);

		/* arquivo de internacionalizacao */
		ResourceBundle resourceBundle = getResourceBundle(mapping, baseCadastroForm, request, response);

		/* cria um IVOWrapper para este VO */
		IVOWrapper wrapper = baseCadastroForm.getIVOWrapper(resourceBundle);
		wrapper.setBaseVO(cadastroBaseVO);

		/* chama tela de consulta */
		if (logger.isDebugEnabled()) {
			logger.debug("PREPARANDO TELA DE CONSULTA. Classe=" + cadastroBaseVO.getClass());
		}
		return mapping.findForward(FORWARD_TELA_CONSULTA);
	}

	/**
	 * Executa tarefas comuns à preparação de inclusão, alteração ou simplesmente para atualizar a tela de cadastro atual. <br>
	 * Ex: Atualizar um combo de cidade assim que um combo de estado for alterado
	 * 
	 * @throws BaseTechnicalError
	 * @throws BaseBusinessException
	 * @throws RemoteException
	 */
	public ActionForward preparaPesquisa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseBusinessException, BaseTechnicalError, RemoteException {
		return mapping.findForward(FORWARD_TELA_CADASTRO);
	}

	/**
	 * Prepara para entrar na tela de pesquisa Ex: Listar todos os VOs de Pro
	 * 
	 * @throws BaseTechnicalError
	 * @throws BaseBusinessException
	 * @throws RemoteException
	 */
	public ActionForward preparaIncluirOuAlterar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseBusinessException, BaseTechnicalError, RemoteException {
		return mapping.findForward(FORWARD_TELA_CADASTRO);
	}

	/**
	 * Invoca uma exclusão a partir do atributo BaseCadastroForm.getPesquisaIdSelecionado()
	 * 
	 * @throws BaseTechnicalError
	 * @throws BaseBusinessException
	 * @throws RemoteException
	 */
	public ActionForward excluir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseBusinessException, BaseTechnicalError, RemoteException {

		ICadastroFacade cadastroFacade = getCadastroFacade(mapping, form, request, response);
		BaseCadastroForm baseCadastroForm = (BaseCadastroForm) form;

		/* PARA UNIFORMIZAR CONTRADO DE DAOs - */
		ICadastroBaseVO baseVO = baseCadastroForm.getBaseVOSelecionado();

		cadastroFacade.excluir(baseVO);

		/** ***************************************** */
		/* DESALOCANDO TODOS OS RECURSOS UTILIZADOS */
		/** ***************************************** */
		removeFacade(mapping, form, request, response);

		/* appenda uma mensagem de diálogo para o usuário */
		appendDialogMessage(mapping, form, request, response, "geral.sucesso.excluir");

		return pesquisar(mapping, form, request, response);
	}

	/**
	 * Inclui ou altera um objeto de acordo com o atributo BaseCadastroForm.getAcaoEstado()
	 * 
	 * @throws BaseTechnicalError
	 * @throws RemoteException
	 * @throws BaseBusinessException
	 */
	public ActionForward salvar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseTechnicalError, RemoteException {

		ResourceBundle resourceBundle = getResourceBundle(mapping, form, request, response);
		ICadastroFacade cadastroFacade = getCadastroFacade(mapping, form, request, response);
		BaseCadastroForm baseCadastroForm = (BaseCadastroForm) form;
		String acaoEstado = baseCadastroForm.getAcaoEstado();

		/** ************ */
		/* recupera o vo */
		/** ************ */
		/* recupera o wrapper do vo atual */
		IVOWrapper wrapper = baseCadastroForm.getIVOWrapper(resourceBundle);

		/* recupera o VO de dentro do Wrapper */
		ICadastroBaseVO baseVO = (ICadastroBaseVO) wrapper.getBaseVO();

		/** *************** */
		/* inclui ou altera */
		/** *************** */

		ActionForward actionForward = null;

		try {
			if (acaoEstado != null && acaoEstado.equals(BaseCadastroForm.ACAOESTADO_INCLUSAO)) {
				
				logger.warn("INVOCANDO cadastroFacade.incluir(), acaoEstado=" + acaoEstado);
				baseVO = cadastroFacade.incluir(baseVO);

				/* appenda uma mensagem de diálogo para o usuário */
				appendDialogMessage(mapping, form, request, response, "geral.sucesso.incluir");

			} else if (acaoEstado != null && acaoEstado.equals(BaseCadastroForm.ACAOESTADO_ALTERACAO)) {
				logger.warn("INVOCANDO cadastroFacade.alterar(), acaoEstado=" + acaoEstado);
				baseVO = cadastroFacade.alterar(baseVO);

				/* appenda uma mensagem de diálogo para o usuário */
				appendDialogMessage(mapping, form, request, response, "geral.sucesso.alterar");

			} else {
				logger.warn("NÃO FOI POSSIVEL RECONHECER A AcaoEstado=" + acaoEstado + " AO INVOCAR O SERVICO salvar()");
			}

			/*************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
			 * a alteração foi um sucesso, encaminhando para a tela de pesquisa inicial
			 ************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
			actionForward = pesquisar(mapping, form, request, response);

		} catch (BaseBusinessException be) {
			/*************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
			 * se der exceção de negócio, eu renderizo a exceção e encaminho p/ a tela
			 ************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
			renderizaExcecao(mapping, form, request, response, be);
			actionForward = mapping.findForward(FORWARD_TELA_CADASTRO);
		} finally {

			/** ***************************************** */
			/* DESALOCANDO TODOS OS RECURSOS UTILIZADOS */
			/** ***************************************** */
			removeFacade(mapping, form, request, response);
			// baseCadastroForm.setIVOWrapper(null);
		}

		return actionForward;
	}

	/**
	 * Fecha uma tela de cadastro voltando para a tela de pesquisa Invoca o método abstrato cancelar() de CadastroFacade
	 */
	public ActionForward fechar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseBusinessException, BaseTechnicalError, RemoteException {
		finalizar(mapping, form, request, response);

		/* appenda uma mensagem de diálogo para o usuário */
		appendDialogMessage(mapping, form, request, response, "geral.sucesso.fechar");

		return pesquisar(mapping, form, request, response);
	}

	/**
	 * Implementar este método devolvendo uma nova instancia do VO a ser editado
	 * 
	 * @throws RemoteException
	 */
	public ICadastroBaseVO criarNovoVO(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseTechnicalError, RemoteException, BaseBusinessException {
		ICadastroFacade cadastroFacade = getCadastroFacade(mapping, form, request, response);
		ICadastroBaseVO cadastroBaseVO = cadastroFacade.newVO();

		return cadastroBaseVO;
	}

	/**
	 * É chamado no momento em que o usuário seleciona um item na lista da tela de pesquisa. Se for necessário atualizar os dados do item selecionado (IBaseVO) implemente esta atualização neste método
	 */
	protected abstract ICadastroBaseVO atualizarIBaseVOSelecionado(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, ICadastroBaseVO baseVO) throws BaseBusinessException, BaseTechnicalError;

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (isMudancaDeModulo(mapping, form, request, response)) {

			logger.warn("Mudanca de modulo interceptada! Desalocando recursos do módulo anterior!!...");

			ICadastroFacade cadastroFacade = (ICadastroFacade) request.getSession().getAttribute(PARAM_FACADE);

			request.getSession().removeAttribute(PARAM_FACADE);

			if (cadastroFacade != null) {
				finalizar(mapping, form, request, response);
			}

			if (logger.isDebugEnabled()) {
				logger.debug(getSessionAtributesList(request));
			}

		}

		return super.execute(mapping, form, request, response);
	}

	/**
	 * Cria e configura uma instância do Helper BeanUtilsBean
	 */
	protected BeanUtilsBean getBeanUtilsBean(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ActionException {

		PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
		ConvertUtilsBean convertUtilsBean = getConvertUtilsBean(getResourceBundle(mapping, form, request, response));

		BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean, propertyUtilsBean);

		return beanUtilsBean;

	}

	/**
	 * Cria e configura uma instância do Helper ConvertUtilsBean
	 */
	protected ConvertUtilsBean getConvertUtilsBean(ResourceBundle resourceBundle) {
		ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
		FrameworkAcheUtil.configureConvertUtilsBean(convertUtilsBean, resourceBundle);
		return convertUtilsBean;
	}

	/**
	 * Cria e configura uma instância do Helper ConvertUtilsBean
	 */
	protected ConvertUtilsBean getConvertUtilsBean(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ActionException {

		ResourceBundle resourceBundle = getResourceBundle(mapping, form, request, response);
		ConvertUtilsBean convertUtilsBean = getConvertUtilsBean(resourceBundle);

		return convertUtilsBean;
	}

	/**
	 * Controla o ciclo de vida de um cadastro facade
	 */
	public ICadastroFacade getCadastroFacade(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseTechnicalError, RemoteException {

		ICadastroFacade cadastroFacade;

		/* FACADE PARA ESTE MODULO */
		String moduleId = getModuleId();

		/* FACADE Q ESTA NA SESSAO */
		String sessionModuleId = (String) request.getSession().getAttribute(PARAM_MODULE_ID_ATUAL);

		if (sessionModuleId == null) {
			/* caso ainda nao exista facade na sessao */
			cadastroFacade = (ICadastroFacade) getFacade(mapping, form, request, response, moduleId);

		} else if (!sessionModuleId.equals(moduleId)) {
			/* caso exista mas seja do tipo antigo */
			finalizar(mapping, form, request, response);
			cadastroFacade = (ICadastroFacade) getFacade(mapping, form, request, response, moduleId);

		} else {
			/* caso exista e seja do tipo esperado */
			cadastroFacade = (ICadastroFacade) request.getSession().getAttribute(PARAM_FACADE);

			if (cadastroFacade == null) {
				cadastroFacade = (ICadastroFacade) getFacade(mapping, form, request, response, moduleId);
			}
		}

		if (cadastroFacade == null) {
			throw new BaseTechnicalError("Nao e permitido armazenar em cache um facade nulo! ");
		}

		/* Guarda o Facade na sessão */
		request.getSession().setAttribute(PARAM_FACADE, cadastroFacade);
		request.getSession().setAttribute(PARAM_MODULE_ID_ATUAL, moduleId);

		return cadastroFacade;
	}

	/**
	 * Finaliza o módulo corrente voltando para o menu principal Invoca o método abstrato cancelar() de CadastroFacade
	 * 
	 * @throws RemoteException
	 */
	public ActionForward finalizar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseTechnicalError, RemoteException {

		IFacade facade = (ICadastroFacade) request.getSession().getAttribute(PARAM_FACADE);

		if (facade != null) {
			IServiceLocator serviceLocator = getServiceLocator(mapping, form, request, response);
			serviceLocator.finishFacade(facade);

		} else {
			logger.warn("O facade encontrado é nulo!");

		}

		return mapping.findForward(FORWARD_TELA_PRINCIPAL);
	}
}