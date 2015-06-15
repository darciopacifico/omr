package br.com.dlp.validador.cliente;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import br.com.dlp.framework.struts.action.BaseCadastroAction;
import br.com.dlp.framework.util.AcheActionMessage;
import br.com.dlp.framework.util.FrameworkAcheUtil;
/**
 *
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>

 * @struts.form 
 * name = "clienteForm"
 * include-all ="true" 
 *
 * ActionForm para o componente Cliente

 *
 **/
public class ClienteForm extends br.com.dlp.jazzqa.base.AbstractJazzQAForm{
	/**
	 * Controle de versão para classes serializáveis
	 */
	private static final long serialVersionUID = 907976127239847L;

	/**
	 * Método getter para o wrapper de VO deste de módulo
	 */
	public ClienteVOWrapper getClienteVOWrapper(){
		ClienteVOWrapper clienteVOWrapper = (ClienteVOWrapper)getIVOWrapper();
		return clienteVOWrapper;
	}

	/**
	 * <b>Consistências e Validacões</b> das entradas de dados do modulo 'Cliente'<br>
	 * ATENÇÃO!! NAO IMPLEMENTAR VALIDAÇÕES DE NEGÓCIO NESTE MÉTODO.<br> 
	 * VALIDAÇÕES DE NEGÓCIO DO MÓDULO 'Cliente' DEVEM ESTAR IMPLEMENTADAS <br>
	 * NA FACHADA 'ClienteFacadeImpl' OU EM 'OBJETOS DE NEGÓCIO' ACESSADOS POR ESTA
	 */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors actionErrors = super.validate(mapping, request);

    String paramService = getParamService();
    /* TODO: Caso queira validar os atributos de 'ClienteVO'
     *  durante outros servicos, altere este 'if' */
    if(paramService!=null && paramService.equals(BaseCadastroAction.SERVICE_SALVAR)){
		MessageResources messageResources = (MessageResources) request.getAttribute(Globals.MESSAGES_KEY);
		ClienteVO clienteVO = getClienteVOWrapper().getClienteVO();

		/******************************************************************/
		/*Validacoes do atributo 'cnpj' de 'ClienteVO' */
		String cnpj = clienteVO.getCnpj();
		if(FrameworkAcheUtil.isNullOrEmptyOrZero(cnpj)){
			/*VALIDACAO DO ATRIBUTO cnpj COMO REQUERIDO*/
			actionErrors.add("cnpj",new AcheActionMessage("errors.required","Cliente.cnpj.label",messageResources));

		}		

		/******************************************************************/
		/*Validacoes do atributo 'nome' de 'ClienteVO' */
		String nome = clienteVO.getNome();
		if(FrameworkAcheUtil.isNullOrEmptyOrZero(nome)){
			/*VALIDACAO DO ATRIBUTO nome COMO REQUERIDO*/
			actionErrors.add("nome",new AcheActionMessage("errors.required","Cliente.nome.label",messageResources));

		}		

		}
		return actionErrors;
	}

	/**
	 * Retorna a Wrapper class para ClienteVO
	 */
	protected Class getWrapperClass() {
		return ClienteVOWrapper.class;
	}
}