package br.com.dlp.framework.struts.action;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.tiles.ComponentDefinition;
import org.apache.struts.tiles.DefinitionsFactory;
import org.apache.struts.tiles.DefinitionsFactoryException;
import org.apache.struts.tiles.NoSuchDefinitionException;
import org.apache.struts.tiles.TilesUtil;

import br.com.dlp.framework.dao.DAOException;
import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.facade.IFacade;
import br.com.dlp.framework.popup.PopUpController;
import br.com.dlp.framework.popup.VLHPopUpController;
import br.com.dlp.framework.servicelocator.IServiceLocator;
import br.com.dlp.framework.servicelocator.ServiceLocatorException;
import br.com.dlp.framework.servicelocator.ServiceLocatorFactory;
import br.com.dlp.framework.struts.form.ActionFormException;
import br.com.dlp.framework.struts.form.BaseForm;
import br.com.dlp.framework.util.FrameworkAcheUtil;
import br.com.dlp.framework.vo.DialogMessageVO;
import br.com.dlp.framework.vo.IUsuarioVO;

public abstract class BaseAction extends Action {
	public static final String EXCECOES_NEGOCIO = "BaseAction.EXCECOES_NEGOCIO";

	public static final String EXCECOES_TECNICAS = "BaseAction.EXCECOES_TECNICAS";

	public static final String EXCECOES_OUTRAS = "BaseAction.EXCECOES_OUTRAS";

	public static final String PARAM_SERVICE = "paramService";

	public static final String PARAM_TITULO_CAMINHO_FORWARDS = "tituloCaminhoForwards";

	/* serve para controlar mudanca de modulo */
	public static final String PARAM_ULTIMO_FORM = "BaseAction.PARAM_ULTIMO_FORM";

	public static final String PARAM_ACTIONPATH_PARA_POPUP = "actionPathParaPopUP";

	public static final String PARAM_RESOURCE_BUNDLE = "BaseAction.ResourceBundle";

	public static final String PARAM_SESSION_SYNCH_TOKEN = "BaseAction.synchronizedToken";

	public static final String PARAM_FORM_ATUAL = "BaseAction.formAtual";

	public static final String DIALOG_MESSAGE_CLOSING = "<FIM_MSG>";

	public static final String DIALOG_MESSAGES = "BaseAction.dialogMessages";

	public static final String FORWARD_GLOBAL_POPUP = "globalPopUp";

	public static final String FORWARD_TELA_PRINCIPAL = "telaPrincipal";

	public static final String SERVICE_CHAMAR_POPUP = "chamarPopUp";

	public static final String SERVICE_PESQUISA_POPUP = "pesquisaPopUp";

	public static final String SERVICE_CONFIRMA_POPUP = "confirmaPopUp";

	public static final String SERVICE_PAGINAR_POPUP = "paginarPopUp";

	protected Log logger = LogFactory.getLog(BaseAction.class);

	/**
	 * - Faz uma chamada ao servico (metodo) informado no parametro
	 * PARAM_SERVICE<BR> - Registra navegação de telas<br> - Trata erros não
	 * capturados<br> - Desaloca recursos ao sair do módulo<br> - Invoca
	 * validação do formulário<br>
	 * 
	 * @return Forward destino selecionado no serviço executado
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward actionForward;

		/*
		 * coloca o resource bundle na sessao para uso geral (conversao,
		 * internacionalizacao etc...)
		 */
		registrarResourceBundle(mapping, form, request, response);

		/* Desonera a sessao de BaseForms que nao serao mais utilizados */
		desalocarObjetosDoModuloAnterior(mapping, form, request, response);

		/* faz validacao do formulario */
		int qtdErros = 0;

		ActionErrors actionErrors = form.validate(mapping, request);
		if (actionErrors != null) {
			qtdErros = actionErrors.size();
		}

		if (qtdErros <= 0) {
			/* Caso esteja válido */

			/* INVOCA O SERVICO SELECIONADO PELO USUARIO */
			String strMethod = getParamService(mapping, form, request, response);
			Method method = this.getClass()
					.getMethod(
							strMethod,
							new Class[] { ActionMapping.class,
									ActionForm.class, HttpServletRequest.class,
									HttpServletResponse.class });
			try {
				/** ************************************* */
				/* invoca o servico */
				actionForward = (ActionForward) method.invoke(this,
						new Object[] { mapping, form, request, response });
				/* invoca o servico */
				/** ************************************* */

				if (!isForwardRegistrado(actionForward, mapping, form, request,
						response)) {
					/*
					 * registra a navegacao de forward ORIGEM-DESTINO para que o
					 * processo contrario tbm possa ser feito
					 */
					registrarForwardParaVoltar(actionForward, mapping, form,
							request, response);
				}
				/*
				 * em caso de SUCESSO!!! registra o forward atual como "ultimo
				 * acessado"
				 */
				registraUltimoForward(mapping, ((BaseForm) form), request,
						response, actionForward);

			} catch (Exception e) {
				renderizaExcecao(mapping, form, request, response, e);

				/*
				 * em caso de FALHA!!! procuro o ultimo forward acessado de
				 * redireciono p ele
				 */
				actionForward = getUltimoForward(mapping, form, request,
						response);
				registraUltimoForward(mapping, ((BaseForm) form), request,
						response, actionForward);
			} finally {
				/* Limpa os recursos utilizados no último servico invocado */
				limparRecursosUltimoServico(mapping, form, request, response);

			}

		} else {

			/*
			 * caso o formulario nao tenha sido validado, volto para o ultimo
			 * forward (tela JSP) acessado
			 */
			actionForward = getUltimoForward(mapping, form, request, response);

			/* salvo as mensagens de erro (para que aparecam da tela) */
			saveErrors(request, (ActionMessages) actionErrors);

			logger.warn("O formulario nao contem inconsistencias (qtd = "
					+ qtdErros + ") de negocio, "
					+ "vou redirecionar para o ultimo forward acessado");
		}

		/* REGISTRA NA SESSAO O MODULO ATUAL */
		registrarModuloAtual(mapping, form, request, response);

		/* Registra no form o caminho percorrido para chegar até a tela atual */
		registrarTituloCaminhoForwards(mapping, form, request, response);

		return actionForward;
	}

	/**
	 * Registra os titulos dos forwards do caminho para a tela atual na request
	 * (chave = BaseAction.PARAM_TITULO_CAMINHO_FORWARDS)
	 */
	protected void registrarTituloCaminhoForwards(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ActionException {

		BaseForm baseForm = (BaseForm) form;
		List tituloCaminhoForwards = new ArrayList();

		/* Recupera o último forward */
		String forward = baseForm.getUltimoForward();

		/* Descobre qual o título do forward atual */
		tituloCaminhoForwards.add(resolveTituloForward(mapping, form, request,
				response, forward));

		/* itera o caminho percorrido (Map) até o */
		forward = (String) baseForm.getCaminhoForwards().get(forward);
		while (forward != null) {
			tituloCaminhoForwards.add(resolveTituloForward(mapping, form,
					request, response, forward));
			forward = (String) baseForm.getCaminhoForwards().get(forward);

		}

		Collections.reverse(tituloCaminhoForwards);

		request.setAttribute(PARAM_TITULO_CAMINHO_FORWARDS,
				tituloCaminhoForwards);

	}

	/**
	 * Resolve qual o titulo de um forward (normalmente associado ao
	 * tiles-defs.xml)
	 */
	protected String resolveTituloForward(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, String nomeForward)
			throws ActionException {

		String tituloForward;

		/* se o nome vem nulo retorno branco! */
		if (nomeForward == null) {
			return "";
		}

		try {
			ActionForward actionForward = mapping.findForward(nomeForward);

			DefinitionsFactory definitionsFactory = TilesUtil
					.getDefinitionsFactory(request, getServlet()
							.getServletContext());
			ComponentDefinition componentDefinition;
			componentDefinition = definitionsFactory.getDefinition(
					actionForward.getPath(), request, getServlet()
							.getServletContext());

			Object object = componentDefinition.getAttribute("title");

			tituloForward = object + "";

			logger.debug(">>>>>>>>>>>>>>>>>" + object + ">>>>>>>>>>>>");

		} catch (NoSuchDefinitionException e) {
			throw new ActionException(
					"Erro ao tentar recuperar o nome do módulo referente ao forward '"
							+ nomeForward + "'", e);

		} catch (DefinitionsFactoryException e) {
			throw new ActionException(
					"Erro ao tentar recuperar o nome do módulo referente ao forward '"
							+ nomeForward + "'", e);

		} catch (Exception e) {
			throw new ActionException(
					"Erro ao tentar recuperar o nome do módulo referente ao forward '"
							+ nomeForward + "'", e);
		}

		return tituloForward;

	}

	/**
	 * Limpa os recursos utilzados no ultimo servico
	 */
	protected void limparRecursosUltimoServico(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws DAOException {
		BaseForm baseForm = (BaseForm) form;
		baseForm.setParamService(null);
	}

	/**
	 * Recupera ou cria coleção de mensagens de diálogo
	 * 
	 * @return Coleção de mensagens de diálogo
	 */
	protected Collection getDialogMessages(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ActionException {
		Collection collection = (Collection) request
				.getAttribute(DIALOG_MESSAGES);

		if (collection == null) {
			collection = new ArrayList(3);
			request.setAttribute(DIALOG_MESSAGES, collection);
		}

		return collection;

	}

	/**
	 * Para uso geral; apenda mensagens de diálogo que serão renderizadas na
	 * GUI. Exemplos:<br>
	 * "Registro salvo com sucesso!"<br>
	 * "Registro excluido com sucesso!"<br>
	 * "O item xyz foi selecionado no popup!"<br>
	 * "Nenhum item encontrado na pesquisa!"<br>
	 * 
	 * @throws ActionException
	 * @param dialogMessage
	 *            Mensagem a ser renderizada
	 */
	protected void appendDialogMessage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String dialogMessage) throws ActionException {
		BaseForm baseForm = (BaseForm) form;

		if (baseForm != null) {
			Collection dialogMessages = getDialogMessages(mapping, form,
					request, response);
			DialogMessageVO dialogMessageVO = new DialogMessageVO();

			dialogMessageVO.setMensagem(dialogMessage);
			dialogMessages.add(dialogMessageVO);

		} else {
			logger.warn("Não foi possivel apendar mensagem pq form==null");
		}
	}

	/**
	 * Analisa se o forward informado já possui registro no mapeamento de
	 * navegação das telas
	 * 
	 * @throws DAOException
	 */
	public boolean isForwardRegistrado(ActionForward forwardDestino,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws DAOException {
		if (form == null || !(form instanceof BaseForm)) {
			throw new DAOException(
					"Nao e possivel analisar o forwardAtual pq o form e nulo ou nao e instanceof BaseForm");
		}

		BaseForm baseForm = (BaseForm) form;

		Map caminhoForwards = baseForm.getCaminhoForwards();

		return caminhoForwards.containsKey(forwardDestino.getName());
	}

	/**
	 * Registra na sessao o forward de origem e o de destino para a correga
	 * navegacao dos botoes voltar, fechar, salvar
	 * 
	 * @throws DAOException
	 */
	public void registrarForwardParaVoltar(ActionForward forwardDestino,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws DAOException {
		if (form == null || !(form instanceof BaseForm)) {
			throw new DAOException(
					"Nao e possivel registrar o 'forward para voltar' pq o form atual e nulo ou nao e instanceof BaseForm");
		}

		BaseForm baseForm = (BaseForm) form;

		String forwardOrigem = baseForm.getUltimoForward();

		Map caminhoForwards = baseForm.getCaminhoForwards();

		caminhoForwards.put(forwardDestino.getName(), forwardOrigem);

	}

	/**
	 * Recupera o forward a partir de onde o forward atual foi chamado<br>
	 * Serve para controlar a correta navegacao das telas apos o clique dos
	 * botoes voltar, fechar, salvar
	 * 
	 * @throws ActionException
	 */
	public String getForwardOrigem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ActionException {

		BaseForm baseForm = (BaseForm) form;

		String forwardDestino = baseForm.getUltimoForward();

		String forwardOrigem = getForwardOrigem(mapping, form, request,
				response, forwardDestino);

		return forwardOrigem;
	}

	/**
	 * Recupera o forward a partir de onde o forward atual foi chamado<br>
	 * Serve para controlar a correta navegacao das telas apos o clique dos
	 * botoes voltar, fechar, salvar
	 * 
	 * @throws ActionException
	 */
	public String getForwardOrigem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String forwardDestino) throws ActionException {

		if (form == null || !(form instanceof BaseForm)) {
			throw new ActionException(
					"Nao e possivel identificar o forward de origem (para voltar) pq o form atual e nulo ou nao e instanceof BaseForm!");
		}

		BaseForm baseForm = (BaseForm) form;

		Map caminhoForwards = baseForm.getCaminhoForwards();
		String forwardOrigem = (String) caminhoForwards.get(forwardDestino);

		caminhoForwards.remove(forwardDestino);

		/*
		 * Darcio 09/03/2006 Resolvi permitir o retorno de nulo; a
		 * funcionalidade "registrarCaminhoParaForward" precisou que fosse assim
		 * if (forwardOrigem == null) { throw new ActionException("O forward de
		 * origem do forward '" + forwardDestino + "' nao foi encontrado (null). " +
		 * "Dessa forma nao e possivel saber qual e a tela de destino apos os
		 * eventos voltar, fechar, salvar etc..." + "verifique se o metodo
		 * registrarForwardParaVoltar() esta sendo invocado!"); }
		 */

		return forwardOrigem;
	}

	/**
	 * Coloca na sessao o ResourceBundle do sistema
	 */
	protected void registrarResourceBundle(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ActionException {
		ResourceBundle resourceBundle = (ResourceBundle) request.getSession()
				.getAttribute(BaseAction.PARAM_RESOURCE_BUNDLE);

		if (resourceBundle == null) {
			IServiceLocator serviceLocator = getServiceLocator(mapping, form,
					request, response);

			try {
				resourceBundle = serviceLocator.getResourceBundle();

			} catch (ServiceLocatorException e) {
				throw new ActionException(
						"Nao foi possivel recuperar o ResourceBundle do sistema!",
						e);

			}
			request.getSession().setAttribute(BaseAction.PARAM_RESOURCE_BUNDLE,
					resourceBundle);
		}
	}

	/**
	 * Coloca no form o último forward acessado. Serve para controlar o fluxo
	 * das telas!
	 */
	public void registraUltimoForward(ActionMapping mapping, BaseForm baseForm,
			HttpServletRequest request, HttpServletResponse response,
			ActionForward actionForward) throws ActionException {
		String forwardName = actionForward.getName();
		baseForm.setUltimoForward(forwardName);
	}

	/**
	 * Recupera o ultimo forward acessado. Serve para controlar o fluxo das
	 * telas!
	 */
	public ActionForward getUltimoForward(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ActionException {

		ActionForward actionForward;

		if (form != null && form instanceof BaseForm) {
			BaseForm baseForm = (BaseForm) form;

			String ultimoForward = baseForm.getUltimoForward();

			actionForward = mapping.findForward(ultimoForward);

			if (actionForward == null) {

				if (ultimoForward == null) {
					logger
							.warn("Atencao!! Nao foi possivel identificar qual foi o ultimo forward acessado");

				} else {
					logger.warn("Atencao!! O ultimo forward acessado ("
							+ ultimoForward + ") nao e valido!");

				}

				actionForward = mapping.getInputForward();
				logger.warn("Redirecionando para o input forward ("
						+ actionForward + ") !");
			}

		} else {
			actionForward = mapping.getInputForward();
			logger
					.warn("Atencao!!! Não foi possivel encontrar o ultimo forward (tela JSP) "
							+ "acessado, vou retornar o input forward (JSP default do modulo)");
		}

		return actionForward;
	}

	/**
	 * Verifico se a chamada atual de servico é uma mudança de módulo
	 */
	protected boolean isMudancaDeModulo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ActionException {

		boolean returnValue = false;

		String ultimoTipoForm = (String) request.getSession().getAttribute(
				PARAM_ULTIMO_FORM);
		String atualTipoForm = "nenhumModuloAcessado";

		if (form != null) {
			atualTipoForm = form.getClass().getName();
		}

		/*
		 * se o último form do modulo acessado não for do meu tipo, então o
		 * usuário mudou de modulo
		 */
		if (ultimoTipoForm != null && !atualTipoForm.equals(ultimoTipoForm)) {

			logger
					.warn("Atenção!! O HttpServletRequest atual é uma mudança de modulo!");

			/* é uma mudanca de modulo */
			returnValue = true;
		}

		return returnValue;
	}

	/**
	 * Recupera o servico a ser chamado <br>
	 * Caso identifique que se trata de uma mudanca de modulo, invoca servico
	 * padrao
	 * 
	 * @throws ActionException
	 */
	protected String getParamService(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ActionException {

		BaseForm baseForm = null;
		String strMethod = null;

		boolean mudancaDeModulo = isMudancaDeModulo(mapping, form, request,
				response);
		boolean postDoPassado = isPostDoPassado(mapping, form, request,
				response);
		boolean permitePostRepetido = isPermitePostRepetido(mapping, form,
				request, response);
		/*
		 * o form nao é nulo, é instancia de BaseForm e a ação atual não é uma
		 * mudanca de modulo
		 */
		if ((form != null) && (form instanceof BaseForm) && !mudancaDeModulo) {
			baseForm = (BaseForm) form;
			strMethod = baseForm.getParamService();
		}

		/*
		 * se não for permitido para o modulo atual post repetido e se foi
		 * constatado que o post atual é repetido
		 */
		if (postDoPassado) {
			logger
					.warn("O post atual é repetido ou do passado, ou seja, provavelmente o usuario mandaou atualizar a tela com F5!!");
			if (!permitePostRepetido) {
				logger
						.warn("O módulo atual nao permite post repetido, vou atribuir nulo ao strMethod ");
				strMethod = null;
			}
		}

		/* o método é nulo ou vazio ou é uma mudanca de modulo */
		if (strMethod == null || strMethod.trim().equals("") || mudancaDeModulo) {
			strMethod = getDefaultService();
			logger
					.warn("NENHUM SERVICO FOI INFORMADO NO PARAMETRO paramService"
							+ ". SERVICO PADRÃO RETORNADO PELO METODO BaseAction.getDefaultService()="
							+ strMethod);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SERVICO A SER INVOCADO = " + strMethod);
		}

		return strMethod;
	}

	/**
	 * Avalia se o post atual é um post repetido ou um post do passado, exemplos
	 * de qdo isso acontece:<br> - Utilizar botoes de navegação do browser<br> -
	 * Utilizar a funcionalidade "atualizar" do browser<br>
	 */
	protected boolean isPostDoPassado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ActionException {
		boolean returnValue = false;

		if (form == null || !(form instanceof BaseForm)) {
			return returnValue;
		}

		BaseForm baseForm = (BaseForm) form;
		String formSynchronizedToken = baseForm.getSynchronizedToken();
		String sessionSynchronizedToken = (String) request.getSession()
				.getAttribute(PARAM_SESSION_SYNCH_TOKEN);

		if (!FrameworkAcheUtil.isNullOrEmptyOrZero(formSynchronizedToken)
				&& !FrameworkAcheUtil
						.isNullOrEmptyOrZero(sessionSynchronizedToken)) {

			/* sera post repetido ou post errado se nao forem iguais */
			returnValue = !formSynchronizedToken
					.equals(sessionSynchronizedToken);

		}

		/* cria um novo token */
		String novoSynchronizedToken = Math.random() + "";

		/* atribui o token no form e na session para comparacao posterior */
		baseForm.setSynchronizedToken(novoSynchronizedToken);
		request.getSession().setAttribute(PARAM_SESSION_SYNCH_TOKEN,
				novoSynchronizedToken);

		return returnValue;
	}

	/**
	 * Avalia se o módulo atual permite post repetido (F5 no Browser)
	 */
	protected boolean isPermitePostRepetido(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ActionException {
		return false;
	}

	/**
	 * ANALISA A REQUISICAO ATUAL, CASO SEJA UMA MUDANCA DE MODULO, DESALOCA OS
	 * RECURSOS E TIRA DA SESSAO O FORM DO MODULO VISITADO ANTERIORMENTE POR
	 * ESTA SESSAO
	 * 
	 * @throws ActionException
	 */
	protected void desalocarObjetosDoModuloAnterior(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ActionException {
		Set sessionFormAttributes = getSessionFormAttributes(request);
		if (isMudancaDeModulo(mapping, form, request, response)) {
			/*
			 * Elimina da sessao qualquer outro form do tipo BaseForm que não
			 * seja o form atual
			 */

			HttpSession session = request.getSession();
			Iterator iterator = sessionFormAttributes.iterator();
			String attributeName = "";

			while (iterator.hasNext()) {
				try {
					attributeName = iterator.next() + "";
					Object value = session.getAttribute(attributeName);

					if (value != null && value instanceof BaseForm
							&& value != form) {
						if (logger.isDebugEnabled()) {
							logger
									.warn("\n###############################################################################"
											+ "\nO BaseForm '"
											+ attributeName
											+ "' nao e referente ao modulo atual!! O form '"
											+ attributeName
											+ "' Sera removido da sessao! "
											+ "\n###############################################################################");
						}
						session.removeAttribute(attributeName);

					}
				} catch (Exception e) {
					throw new ActionException(
							"Erro ao tentar desalocar recursos da sessao", e);
				}
			}
		}
	}

	/**
	 * Registra numa chave da sessao o nome do modulo atual. Serve para controle
	 * de estado da aplicacao
	 */
	protected void registrarModuloAtual(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ActionException {
		Set sessionFormAttributes = getSessionFormAttributes(request);

		request.getSession().setAttribute(BaseAction.PARAM_FORM_ATUAL, form);

		/* ALIMENTA VARIAVEIS DE CONTROLE DE ESTADO */
		String atualTipoForm = "null";
		if (form != null) {
			atualTipoForm = form.getClass().getName();
		}
		request.getSession().setAttribute(PARAM_ULTIMO_FORM, atualTipoForm);
		sessionFormAttributes.add(mapping.getName());
	}

	/**
	 * Coleciona todos as chaves de forms utilizadas
	 */
	protected Set getSessionFormAttributes(HttpServletRequest request) {
		String PARAM_SESSION_FORM_ATTRIBUTES = "BaseAction.SessionFormAttributes";

		Set set = (Set) request.getSession().getAttribute(
				PARAM_SESSION_FORM_ATTRIBUTES);
		if (set == null) {
			set = new HashSet(2);
			request.getSession().setAttribute(PARAM_SESSION_FORM_ATTRIBUTES,
					set);
		}
		return set;
	}

	/**
	 * Recupera o servico default quando nenhum é informado atraves do parametro
	 * 'service'
	 */
	protected abstract String getDefaultService() throws ActionException;

	/**
	 * Coloca no formulario BaseForm as exceções
	 * 
	 * @throws BaseTechnicalError
	 * @throws RemoteException
	 * @throws Throwable
	 */
	public void renderizaExcecao(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			Exception e) throws BaseTechnicalError, RemoteException {
		
		addExcecao(request, e);

	}

	/**
	 * Recupera do request a coleção de exceções do tipo informado (parametro
	 * exceptionsKey) geradas até o momento
	 */
	private Collection getExcecoes(HttpServletRequest request,
			String exceptionsKey) throws ActionException {
		Collection collection = (Collection) request
				.getAttribute(exceptionsKey);

		if (collection == null) {
			collection = new ArrayList();
			request.setAttribute(exceptionsKey, collection);
		}

		return collection;
	}

	/**
	 * Recupera do request a coleção de exceções do tipo informado
	 * (EXCECOES_NEGOCIO,EXCECOES_TECNICAS,EXCECOES_OUTRAS)
	 */
	protected Collection getExcecoes(HttpServletRequest request,
			Throwable exception) throws ActionException {
		Collection collection = null;

		if (exception instanceof BaseBusinessException) {
			collection = getExcecoes(request, EXCECOES_NEGOCIO);
		} else if (exception instanceof BaseTechnicalError) {
			collection = getExcecoes(request, EXCECOES_TECNICAS);
		} else {
			collection = getExcecoes(request, EXCECOES_OUTRAS);
		}

		return collection;
	}

	/**
	 * Adiciona exceção no request, posiciona na coleção do tipo informado
	 * (parâmetro Exception)
	 */
	protected void addExcecao(HttpServletRequest request, Exception exception)
			throws ActionException {
		Collection excecoes = getExcecoes(request, exception);

		/*
		 * caso uma exceção de negócio seja um composite de exceções de negócio,
		 * eu descarto a exceção composite e considero apenas as exceções
		 * internas de forma recursiva
		 */
		if (exception instanceof BaseBusinessException
				&& ((BaseBusinessException) exception)
						.getBaseBusinessExceptions().size() > 0) {

			BaseBusinessException businessException = (BaseBusinessException) exception;
			Iterator iterator = businessException.getBaseBusinessExceptions()
					.iterator();
			while (iterator.hasNext()) {
				Exception exceptionFolha = (Exception) iterator.next();
				addExcecao(request, exceptionFolha);
			}

		} else {
			excecoes.add(exception);

		}
	}

	/**
	 * Remove exceção da coleção específica ( coleção de exceçoes de negócio,
	 * coleção de exceçoes de técnicas, coleção de exceçoes de outros tipos, )
	 */
	protected void removeExcecao(HttpServletRequest request, Exception exception)
			throws ActionException {
		Collection excecoes = getExcecoes(request, exception);
		excecoes.remove(exception);
	}

	/**
	 * Limpa todas as listas de exceções existentes no request
	 */
	protected void clearExcecoes(HttpServletRequest request)
			throws ActionException {
		request.removeAttribute(EXCECOES_NEGOCIO);
		request.removeAttribute(EXCECOES_TECNICAS);
		request.removeAttribute(EXCECOES_OUTRAS);
	}

	/**
	 * RETORNA USUARIO LOGADO
	 * 
	 * @throws RemoteException
	 * @throws BaseTechnicalError
	 */
	public abstract IUsuarioVO getUsuarioLogado(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ActionException,
			BaseTechnicalError, RemoteException;

	/**
	 * Carregar e popular tela de popups
	 * 
	 * @throws BaseTechnicalError
	 * @throws BaseBusinessException
	 * @throws RemoteException
	 */
	public ActionForward chamarPopUp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws BaseBusinessException, BaseTechnicalError, RemoteException {

		String pathParaActionPopUP = mapping.getPath();
		request.setAttribute(PARAM_ACTIONPATH_PARA_POPUP, pathParaActionPopUP);

		BaseForm baseForm = (BaseForm) form;

		PopUpController popUpController = baseForm.getPopUpController();

		String serviceConfirmacao = popUpController.getServicoDepoisDoPopUp();
		if (serviceConfirmacao == null || serviceConfirmacao.equals("")) {
			throw new BaseTechnicalError(
					"Antes de encaminhar para o FORWARD do popUp, e necessario "
							+ "configurar qual sera o servico de confirmacao do "
							+ "popUp no atributo servicoDepoisDoPopUp de BaseForm");
		}

		if (serviceConfirmacao.equals(SERVICE_CONFIRMA_POPUP)) {
			throw new BaseTechnicalError(
					"Este servico nao pode receber SERVICE_CONFIRMA_POPUP "
							+ "como valor para o atributo servicoDepoisDoPopUp do form. "
							+ "Isto pode gerar um loop infinito! ");
		}

		/* RECUPERA O FORWARD PADRAO PARA POPUP */
		String popUpForward = popUpController.getPopUpForward();
		ActionForward actionForward = mapping.findForward(popUpForward);

		baseForm.setParamService(BaseAction.SERVICE_CONFIRMA_POPUP);

		/* executa a pesquisa para o popup */
		popUpController.executePopUpQuery();

		/* CHAMA O TILE OU JSP DO POPUP */
		return actionForward;
	}

	/**
	 * Invoca a pesquisa das informações exibidas no popup
	 */
	public ActionForward pesquisaPopUp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return chamarPopUp(mapping, form, request, response);
	}

	/**
	 * Invoca a paginação do popup
	 */
	public ActionForward paginarPopUp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String pathParaActionPopUP = mapping.getPath();
		request.setAttribute(PARAM_ACTIONPATH_PARA_POPUP, pathParaActionPopUP);

		BaseForm baseForm = (BaseForm) form;
		PopUpController popUpController = baseForm.getPopUpController();

		/* RECUPERA O FORWARD PADRAO PARA POPUP */
		String popUpForward = popUpController.getPopUpForward();
		ActionForward actionForward = mapping.findForward(popUpForward);

		/* executa a pesquisa para o popup */

		if (popUpController instanceof VLHPopUpController) {
			VLHPopUpController vlhPopUpController = (VLHPopUpController) popUpController;
			vlhPopUpController.processarPaginacao();
		}

		/* CHAMA O TILE OU JSP DO POPUP */
		return actionForward;

	}

	/**
	 * Carregar e popular tela de popups
	 * 
	 * @throws Exception
	 */
	public ActionForward confirmaPopUp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		BaseForm baseForm = (BaseForm) form;

		PopUpController popUpController = baseForm.getPopUpController();

		boolean appendarMensagem = false;

		if (!popUpController.isMultiplaEscolha()) {
			Object object;
			object = popUpController.getUniquePopUpItem();
			appendarMensagem = (object == null);
		} else {
			Collection collection = popUpController.getCheckedPopUpItens();
			appendarMensagem = (collection.size() == 0);
		}
		if (appendarMensagem) {
			appendDialogMessage(mapping, form, request, response,
					"geral.popup.nadaselecionado");
		}

		String serviceConfirmacao = popUpController.getServicoDepoisDoPopUp();

		if (serviceConfirmacao == null || serviceConfirmacao.equals("")) {
			throw new BaseTechnicalError(
					"Antes de encaminhar para o FORWARD do popUp, e necessario "
							+ "configurar qual sera o servico de confirmacao do "
							+ "popUp no atributo servicoDepoisDoPopUp de BaseForm");
		}

		if (serviceConfirmacao.equals(SERVICE_CONFIRMA_POPUP)) {
			throw new BaseTechnicalError(
					"Este servico nao pode receber SERVICE_CONFIRMA_POPUP "
							+ "como valor para o atributo servicoDepoisDoPopUp do form. "
							+ "Isto pode gerar um loop infinito! ");
		}
		/*
		 * atribui ao form o servico a ser invocado como servico de confirmacao
		 * do form
		 */
		baseForm.setParamService(serviceConfirmacao);

		return execute(mapping, baseForm, request, response);
	}

	/**
	 * Devolve uma listagem simples de todas os valores contidos na sessao
	 */
	protected String getSessionAtributesList(HttpServletRequest request) {

		StringBuffer sb = new StringBuffer();

		Enumeration enumeration = request.getSession().getAttributeNames();
		sb
				.append("\n")
				.append(
						"####################################################################");
		sb
				.append("\n")
				.append(
						"### Objetos ainda contidos na sessao depois da mudanca de modulo ###");
		sb.append("\n")
				.append("### >>>> nomeAtributo >>>> valor >>>> tipo ###");
		while (enumeration.hasMoreElements()) {
			String attributeName = enumeration.nextElement() + "";
			Object value = request.getSession().getAttribute(attributeName);
			String tipoValue = "";
			if (value != null) {
				tipoValue = value.getClass().getName();
			}

			sb.append("\n").append(
					">>>> " + attributeName + " >>>> " + value + " >>>> "
							+ tipoValue);

		}
		sb
				.append("\n")
				.append(
						"### Objetos ainda contidos na sessao depois da mudanca de modulo ###");
		sb
				.append("\n")
				.append(
						"####################################################################");

		return new String(sb);
	}

	/**
	 * Retorna o ResourceBundle para internacionalização de formatos e labels
	 * 
	 * @throws ActionFormException
	 * @throws ActionException
	 */
	public ResourceBundle getResourceBundle(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ActionException {
		IServiceLocator serviceLocator = getServiceLocator(mapping, form,
				request, response);
		ResourceBundle resourceBundle;
		try {
			resourceBundle = serviceLocator.getResourceBundle();

		} catch (ServiceLocatorException e) {
			throw new ActionException(
					"Erro ao tentar recuperar o ResourceBundle do IServiceLocator do sistema!!",
					e);

		}
		return resourceBundle;
	}

	/**
	 * Recupera a refência da implementação de IServiceLocator do projeto
	 * 
	 * @throws ActionException
	 */
	public IServiceLocator getServiceLocator(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ActionException {
		ServiceLocatorFactory serviceLocatorFactory = ServiceLocatorFactory
				.getInstance();
		Class serviceLocatorClass = getServiceLocatorClass();
		IServiceLocator serviceLocator;
		try {
			serviceLocator = serviceLocatorFactory
					.getServiceLocator(serviceLocatorClass);

			/*
			 * Estou no appServer, por tanto, devo acessar os facades
			 * remotamente
			 */
			serviceLocator.setRemoteFacades(true);
		} catch (ServiceLocatorException e) {
			throw new ActionException(
					"Erro ao tentar recuperar o ServiceLocator!", e);
		}

		return serviceLocator;
	}

	/**
	 * Deve retornar a abstração da classe da implementação de IServiceLocator
	 */
	public abstract Class getServiceLocatorClass() throws ActionException;

	/**
	 * Deve retornar uma string que representa o id do modulo. Utilizada para
	 * que o ServiceLocator recupere o componente de negócio do módulo.
	 */
	public abstract String getModuleId() throws BaseTechnicalError,
			RemoteException;

	/**
	 * Deve retornar uma string que representa o id do VLH modulo. Utilizada
	 * para que o ServiceLocator recupere o VLH do módulo.
	 */
	public abstract String getModuleVLHId() throws BaseTechnicalError,
			RemoteException;

	/**
	 * Recupera o CadastroFacade da sessão
	 * 
	 * @throws BaseTechnicalError
	 * @throws RemoteException
	 */
	public IFacade getFacade(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			String moduleId) throws BaseTechnicalError, RemoteException {

		IServiceLocator serviceLocator = getServiceLocator(mapping, form,
				request, response);
		IFacade facade = serviceLocator.getFacade(moduleId);

		return facade;

	}

}
