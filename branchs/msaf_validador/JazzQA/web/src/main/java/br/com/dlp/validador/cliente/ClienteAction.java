package br.com.dlp.validador.cliente;

import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.jazzqa.util.JazzQAConstants;

/**
 * teste gcode
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>

 * Action para o componente Cliente<br>
 *
 * @struts.action input = "cliente.tile"
 * name = "clienteForm"
 * path = "/cliente/cliente"
 * scope = "session"
 * validate = "false"
 * 
 * @struts.action-forward 
 * 	name = "pesquisa" 
 * 	path = "pesquisaCliente.tile"
 * 
 * @struts.action-forward 
 * 	name = "cadastro" 
 * 	path = "cliente.tile"
 *
 * @struts.action-forward 
 * 	name = "consulta" 
 * 	path = "consultaCliente.tile"
 *

 *
 **/

public class ClienteAction extends br.com.dlp.jazzqa.base.AbstractJazzQAAction{

  /**
   * Prepara para inclusão ou alteração o Wrapper ClienteVOWrapper e os Wrappers relacionados a este.
   */
  public ActionForward preparaIncluirOuAlterar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseBusinessException, BaseTechnicalError, RemoteException {
    return super.preparaIncluirOuAlterar(mapping, form, request, response);
  }

	/**
	 * Efetua as operacoes necessárias para preparar o IVOWrapper para a tela de consulta
	 */
	public ActionForward preparaConsultar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseBusinessException, BaseTechnicalError, RemoteException {
		ActionForward actionForward = super.preparaConsultar(mapping, form, request, response); 

		return actionForward;
	}

    /** Override do metodo preparaPesquisa da superclasse */
  public ActionForward preparaPesquisa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseBusinessException, BaseTechnicalError, RemoteException{
    return super.preparaIncluirOuAlterar(mapping, form, request, response);
  }

	/**
	 * SOBRESCRITA DO MECANISMO DE PESQUISA ORIGINAL DA SUPERCLASSE
	 */
	public ActionForward pesquisar(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) throws BaseBusinessException, BaseTechnicalError, RemoteException {

    	/*Prepara os objetos necessário para a tela de pesquisa (lista para combos, radios etc...)*/
    	preparaPesquisa(mapping, form, request, response);

		/*Recupera o VLHFacade deste módulo*/
		ClienteVLHFacade facade = (ClienteVLHFacade) getVLHFacade(mapping,form,request,response);

		/*Cast do form para ClienteForm*/
		ClienteForm baseCadastroForm=(ClienteForm)form;

		/*ResourceBundle padrao*/
		ResourceBundle resourceBundle = getResourceBundle(mapping, form, request, response);

		/*Recupera o voWrapper do módulo contido no Form*/
		ClienteVOWrapper voWrapper = (ClienteVOWrapper) baseCadastroForm.getIVOWrapper(resourceBundle);

		/******************************/
		/*TRATA PARAMETROS DA PESQUISA*/

		Integer pesquisaId=null;

		java.lang.String pesquisaCnpj=null;

		java.lang.String pesquisaNome=null;

		if(voWrapper!=null){

			pesquisaId = voWrapper.getPesquisaIdInteger();

			pesquisaCnpj = voWrapper.getPesquisaCnpjString();

			pesquisaNome = voWrapper.getPesquisaNomeString();

		}
		/*ordenações escolhidas pelo usuário*/
		QueryOrder[] queryOrders = voWrapper.getArrayQueryOrders();
		/*TRATA PARAMETROS DA PESQUISA*/
		/******************************/

		/* Invoca a pesquisa no VLH (não retorna nada, fica aguardando a solicitação de paginação) */
		facade.executeFindClienteVO(

			pesquisaId

			,pesquisaCnpj

			,pesquisaNome
,queryOrders); 

		/*Seta a quantidade de páginas da pesquisa*/
		setQtdPaginas(mapping,form,request,response);
		/*pega página atual do vlh Seta resultados da pesquisa*/
		if(logger.isDebugEnabled()){
			logger.debug("Recuperando a pagina atual do VLH");
		}
		List page = facade.getPage();
		baseCadastroForm.setResultadoPesquisa(page);
		/*RECUPERA O ÍNDICE DA PÁGINA DO VLH ATUAL*/
		int pageIndex = facade.getPageIndex();
		baseCadastroForm.setPaginaVLH(pageIndex);

		/*appenda uma mensagem de diálogo para o usuário*/
		appendDialogMessage(mapping,form,request,response,"geral.sucesso.pesquisar");
		return super.pesquisar(mapping,form,request,response);
	}

	/**
	 *Indica ao framework qual o id do modulo atual
	 */
	public String getModuleId() throws BaseTechnicalError, RemoteException {
	    return JazzQAConstants.MODULE_CLIENTE;
	}

	/**
	 *Indica ao framework qual o id do VLH modulo atual
	 */
	public String getModuleVLHId() throws BaseTechnicalError, RemoteException {
    	return JazzQAConstants.MODULE_CLIENTE_VLH;
	}

}