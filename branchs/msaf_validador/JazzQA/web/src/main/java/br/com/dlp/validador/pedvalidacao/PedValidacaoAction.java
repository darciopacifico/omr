package br.com.dlp.validador.pedvalidacao;

import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.struts.action.ActionException;
import br.com.dlp.framework.struts.form.BaseCadastroForm;
import br.com.dlp.framework.vo.ICadastroBaseVO;
import br.com.dlp.framework.vowrapper.IVOWrapper;
import br.com.dlp.framework.vowrapper.VOWrapperException;
import br.com.dlp.jazzqa.util.JazzQAConstants;
import br.com.dlp.validador.cliente.ClientePK;

/**
 * teste gcode
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>

 * Action para o componente PedValidacao<br>
 *
 * @struts.action input = "pedvalidacao.tile"
 * name = "pedvalidacaoForm"
 * path = "/pedvalidacao/pedvalidacao"
 * scope = "session"
 * validate = "false"
 * 
 * @struts.action-forward 
 * 	name = "pesquisa" 
 * 	path = "pesquisaPedValidacao.tile"
 * 
 * @struts.action-forward 
 * 	name = "cadastro" 
 * 	path = "pedvalidacao.tile"
 *
 * @struts.action-forward 
 * 	name = "consulta" 
 * 	path = "consultaPedValidacao.tile"
 *

 *
 **/

public class PedValidacaoAction extends br.com.dlp.jazzqa.base.AbstractJazzQAAction{

	
	/**
	 * Sobrescrita do método salvar para tratar upload de arquivo de clientes.
	 */
	@Override
	public ActionForward salvar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseTechnicalError, RemoteException {
		PedValidacaoVO pedValidacaoVO = (PedValidacaoVO) recuperaVO(mapping, form, request, response);
		
		return super.salvar(mapping,form,request,response);
	}

	
	/**
	 * Sobrescrita do método salvar para tratar upload de arquivo de clientes.
	 */
	public ActionForward exibeOrcamento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseTechnicalError, RemoteException {

		
		PedValidacaoVO pedValidacaoVO = (PedValidacaoVO) recuperaVO(mapping, form, request, response);
		
		
		
		
		
		return super.salvar(mapping,form,request,response);
	}
	


	/**
	 * Recupera VO de pedido validação montado na no JSP
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws VOWrapperException
	 * @throws ActionException
	 */
	private ICadastroBaseVO recuperaVO(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws VOWrapperException, ActionException {
		BaseCadastroForm baseCadastroForm = (BaseCadastroForm) form;
		String acaoEstado = baseCadastroForm.getAcaoEstado();
		IVOWrapper wrapper = baseCadastroForm.getIVOWrapper(getResourceBundle(mapping, form, request, response));
		ICadastroBaseVO baseVO = (ICadastroBaseVO) wrapper.getBaseVO();
		
		return baseVO;
	}
	
  /**
   * Prepara para inclusï¿½o ou alteraï¿½ï¿½o o Wrapper PedValidacaoVOWrapper e os Wrappers relacionados a este.
   */
  public ActionForward preparaIncluirOuAlterar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseBusinessException, BaseTechnicalError, RemoteException {
	PedValidacaoFacade facade = (PedValidacaoFacade) getCadastroFacade(mapping,form,request,response);
  	PedValidacaoForm moduleForm = (PedValidacaoForm)form;
	PedValidacaoVOWrapper pedValidacaoVOWrapper = moduleForm.getPedValidacaoVOWrapper();

	/* Alimenta a listagem para a seleï¿½ï¿½o de 'Cliente' */					
	List clienteVOs = facade.findAllCliente();
	pedValidacaoVOWrapper.setListClienteVOs(clienteVOs);
    return super.preparaIncluirOuAlterar(mapping, form, request, response);
  }

	/**
	 * Efetua as operacoes necessï¿½rias para preparar o IVOWrapper para a tela de consulta
	 */
	public ActionForward preparaConsultar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseBusinessException, BaseTechnicalError, RemoteException {
		ActionForward actionForward = super.preparaConsultar(mapping, form, request, response); 

		return actionForward;
	}

    /** Override do metodo preparaPesquisa da superclasse */
  public ActionForward preparaPesquisa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws BaseBusinessException, BaseTechnicalError, RemoteException{
  	PedValidacaoForm moduleForm = (PedValidacaoForm)form;
    PedValidacaoFacade facade = (PedValidacaoFacade) getCadastroFacade(mapping,form,request,response);
	/* Recupera o arquivo de internacionalizacao do sistema. As configuracoes de 
	 * formatacao de data e numeros serao utilizadas para construir o IVOWrapper */
    ResourceBundle resourceBundle = getResourceBundle(mapping, form, request, response);

    /*Constroi e/ou recupera o IVOWrapper do modulo */
	PedValidacaoVOWrapper pedValidacaoVOWrapper = (PedValidacaoVOWrapper)moduleForm.getIVOWrapper(resourceBundle);
	/* Alimenta a listagem para a seleï¿½ï¿½o de 'Cliente' */					
	List clienteVOs = facade.findAllCliente();
	pedValidacaoVOWrapper.setListClienteVOs(clienteVOs);
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

    	/*Prepara os objetos necessï¿½rio para a tela de pesquisa (lista para combos, radios etc...)*/
    	preparaPesquisa(mapping, form, request, response);

		/*Recupera o VLHFacade deste mï¿½dulo*/
		PedValidacaoVLHFacade facade = (PedValidacaoVLHFacade) getVLHFacade(mapping,form,request,response);

		/*Cast do form para PedValidacaoForm*/
		PedValidacaoForm baseCadastroForm=(PedValidacaoForm)form;

		/*ResourceBundle padrao*/
		ResourceBundle resourceBundle = getResourceBundle(mapping, form, request, response);

		/*Recupera o voWrapper do mï¿½dulo contido no Form*/
		PedValidacaoVOWrapper voWrapper = (PedValidacaoVOWrapper) baseCadastroForm.getIVOWrapper(resourceBundle);

		/******************************/
		/*TRATA PARAMETROS DA PESQUISA*/

		Integer pesquisaId=null;

		java.util.Date pesquisaDataDownloadDe=null;
		java.util.Date pesquisaDataDownloadAte=null;

		java.util.Date pesquisaDataSolicitacaoDe=null;
		java.util.Date pesquisaDataSolicitacaoAte=null;

		java.util.Date pesquisaDataTerminoDe=null;
		java.util.Date pesquisaDataTerminoAte=null;

		Integer pesquisaQtdeRegistrosArqDe=null;
		Integer pesquisaQtdeRegistrosArqAte=null;

		Integer pesquisaQtdeRegistrosProcDe=null;
		Integer pesquisaQtdeRegistrosProcAte=null;

		ClientePK pesquisaClienteIPK=null;

		if(voWrapper!=null){

			pesquisaId = voWrapper.getPesquisaIdInteger();

			pesquisaDataDownloadDe = voWrapper.getPesquisaDataDownloadDeDate();
			pesquisaDataDownloadAte = voWrapper.getPesquisaDataDownloadAteDate();

			pesquisaDataSolicitacaoDe = voWrapper.getPesquisaDataSolicitacaoDeDate();
			pesquisaDataSolicitacaoAte = voWrapper.getPesquisaDataSolicitacaoAteDate();

			pesquisaDataTerminoDe = voWrapper.getPesquisaDataTerminoDeDate();
			pesquisaDataTerminoAte = voWrapper.getPesquisaDataTerminoAteDate();

			pesquisaQtdeRegistrosArqDe = voWrapper.getPesquisaQtdeRegistrosArqDeInteger();
			pesquisaQtdeRegistrosArqAte = voWrapper.getPesquisaQtdeRegistrosArqAteInteger();

			pesquisaQtdeRegistrosProcDe = voWrapper.getPesquisaQtdeRegistrosProcDeInteger();
			pesquisaQtdeRegistrosProcAte = voWrapper.getPesquisaQtdeRegistrosProcAteInteger();

			pesquisaClienteIPK = (ClientePK)voWrapper.getPesquisaClienteIPK();

		}
		/*ordenaï¿½ï¿½es escolhidas pelo usuï¿½rio*/
		QueryOrder[] queryOrders = voWrapper.getArrayQueryOrders();
		/*TRATA PARAMETROS DA PESQUISA*/
		/******************************/

		/* Invoca a pesquisa no VLH (nï¿½o retorna nada, fica aguardando a solicitaï¿½ï¿½o de paginaï¿½ï¿½o) */
		facade.executeFindPedValidacaoVO(

			pesquisaId

			,pesquisaDataDownloadDe
			,pesquisaDataDownloadAte

			,pesquisaDataSolicitacaoDe
			,pesquisaDataSolicitacaoAte

			,pesquisaDataTerminoDe
			,pesquisaDataTerminoAte

			,pesquisaQtdeRegistrosArqDe
			,pesquisaQtdeRegistrosArqAte

			,pesquisaQtdeRegistrosProcDe
			,pesquisaQtdeRegistrosProcAte

			,pesquisaClienteIPK
,queryOrders); 

		/*Seta a quantidade de pï¿½ginas da pesquisa*/
		setQtdPaginas(mapping,form,request,response);
		/*pega pï¿½gina atual do vlh Seta resultados da pesquisa*/
		if(logger.isDebugEnabled()){
			logger.debug("Recuperando a pagina atual do VLH");
		}
		List page = facade.getPage();
		baseCadastroForm.setResultadoPesquisa(page);
		/*RECUPERA O ï¿½NDICE DA Pï¿½GINA DO VLH ATUAL*/
		int pageIndex = facade.getPageIndex();
		baseCadastroForm.setPaginaVLH(pageIndex);

		/*appenda uma mensagem de diï¿½logo para o usuï¿½rio*/
		appendDialogMessage(mapping,form,request,response,"geral.sucesso.pesquisar");
		return super.pesquisar(mapping,form,request,response);
	}

	/**
	 *Indica ao framework qual o id do modulo atual
	 */
	public String getModuleId() throws BaseTechnicalError, RemoteException {
	    return JazzQAConstants.MODULE_PEDVALIDACAO;
	}

	/**
	 *Indica ao framework qual o id do VLH modulo atual
	 */
	public String getModuleVLHId() throws BaseTechnicalError, RemoteException {
    	return JazzQAConstants.MODULE_PEDVALIDACAO_VLH;
	}

}