package br.com.dlp.validador.pedvalidacao;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

import br.com.dlp.framework.struts.action.BaseCadastroAction;
import br.com.dlp.framework.util.AcheActionMessage;
import br.com.dlp.framework.util.FrameworkAcheUtil;
import br.com.dlp.validador.cliente.ClienteVO;
/**
 *
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>

 * @struts.form 
 * name = "pedvalidacaoForm"
 * include-all ="true" 
 *
 * ActionForm para o componente PedValidacao

 *
 **/
public class PedValidacaoForm extends br.com.dlp.jazzqa.base.AbstractJazzQAForm{
	/**
	 * Controle de vers�o para classes serializ�veis
	 */
	private static final long serialVersionUID = 907976127239847L;

	
	/**
	 * M�todo getter para o wrapper de VO deste de m�dulo
	 */
	public PedValidacaoVOWrapper getPedValidacaoVOWrapper(){
		PedValidacaoVOWrapper pedValidacaoVOWrapper = (PedValidacaoVOWrapper)getIVOWrapper();
		return pedValidacaoVOWrapper;
	}

	/**
	 * <b>Consist�ncias e Validac�es</b> das entradas de dados do modulo 'PedValidacao'<br>
	 * ATEN��O!! NAO IMPLEMENTAR VALIDA��ES DE NEG�CIO NESTE M�TODO.<br> 
	 * VALIDA��ES DE NEG�CIO DO M�DULO 'PedValidacao' DEVEM ESTAR IMPLEMENTADAS <br>
	 * NA FACHADA 'PedValidacaoFacadeImpl' OU EM 'OBJETOS DE NEG�CIO' ACESSADOS POR ESTA
	 */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors actionErrors = super.validate(mapping, request);

    String paramService = getParamService();
    /* TODO: Caso queira validar os atributos de 'PedValidacaoVO'
     *  durante outros servicos, altere este 'if' */
    if(paramService!=null && paramService.equals(BaseCadastroAction.SERVICE_SALVAR)){
		MessageResources messageResources = (MessageResources) request.getAttribute(Globals.MESSAGES_KEY);
		PedValidacaoVO pedValidacaoVO = getPedValidacaoVOWrapper().getPedValidacaoVO();

		/******************************************************************/
		/*Validacoes do atributo 'dataDownload' de 'PedValidacaoVO' */
		Date dataDownload = pedValidacaoVO.getDataDownload();
		if(FrameworkAcheUtil.isNullOrEmptyOrZero(dataDownload)){
			/*VALIDACAO DO ATRIBUTO dataDownload COMO REQUERIDO*/
			actionErrors.add("dataDownload",new AcheActionMessage("errors.required","PedValidacao.dataDownload.label",messageResources));

		}		

		/******************************************************************/
		/*Validacoes do atributo 'dataSolicitacao' de 'PedValidacaoVO' */
		Date dataSolicitacao = pedValidacaoVO.getDataSolicitacao();
		if(FrameworkAcheUtil.isNullOrEmptyOrZero(dataSolicitacao)){
			/*VALIDACAO DO ATRIBUTO dataSolicitacao COMO REQUERIDO*/
			actionErrors.add("dataSolicitacao",new AcheActionMessage("errors.required","PedValidacao.dataSolicitacao.label",messageResources));

		}		

		/******************************************************************/
		/*Validacoes do atributo 'dataTermino' de 'PedValidacaoVO' */
		Date dataTermino = pedValidacaoVO.getDataTermino();
		if(FrameworkAcheUtil.isNullOrEmptyOrZero(dataTermino)){
			/*VALIDACAO DO ATRIBUTO dataTermino COMO REQUERIDO*/
			actionErrors.add("dataTermino",new AcheActionMessage("errors.required","PedValidacao.dataTermino.label",messageResources));

		}		

		/******************************************************************/
		/*Validacoes do atributo 'qtdeRegistrosArq' de 'PedValidacaoVO' */
		Integer qtdeRegistrosArq = pedValidacaoVO.getQtdeRegistrosArq();
		if(FrameworkAcheUtil.isNullOrEmptyOrZero(qtdeRegistrosArq)){
			/*VALIDACAO DO ATRIBUTO qtdeRegistrosArq COMO REQUERIDO*/
			actionErrors.add("qtdeRegistrosArq",new AcheActionMessage("errors.required","PedValidacao.qtdeRegistrosArq.label",messageResources));

		}		

		/******************************************************************/
		/*Validacoes do atributo 'qtdeRegistrosProc' de 'PedValidacaoVO' */
		Integer qtdeRegistrosProc = pedValidacaoVO.getQtdeRegistrosProc();
		if(FrameworkAcheUtil.isNullOrEmptyOrZero(qtdeRegistrosProc)){
			/*VALIDACAO DO ATRIBUTO qtdeRegistrosProc COMO REQUERIDO*/
			actionErrors.add("qtdeRegistrosProc",new AcheActionMessage("errors.required","PedValidacao.qtdeRegistrosProc.label",messageResources));

		}		

		/******************************************************************/
		/*Validacoes do atributo 'clienteVO' de 'PedValidacaoVO' */
		ClienteVO clienteVO = pedValidacaoVO.getClienteVO();
		if(FrameworkAcheUtil.isNullOrEmptyOrZero(clienteVO)){
			/*VALIDACAO DO ATRIBUTO clienteVO COMO REQUERIDO*/
			actionErrors.add("clienteVO",new AcheActionMessage("errors.required","PedValidacao.clienteVO.label",messageResources));

		}		

		}
		return actionErrors;
	}

	/**
	 * Retorna a Wrapper class para PedValidacaoVO
	 */
	protected Class getWrapperClass() {
		return PedValidacaoVOWrapper.class;
	}
}