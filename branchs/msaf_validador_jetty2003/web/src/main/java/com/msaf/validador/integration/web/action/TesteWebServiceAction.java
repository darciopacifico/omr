package com.msaf.validador.integration.web.action;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.msaf.validador.consultaonline.exceptions.ValidadorLoginException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;
import com.msaf.validador.core.dominio.UsuarioVO;
import com.msaf.validador.core.server.WebServiceServer;
import com.msaf.validador.integration.util.Util;
import com.msaf.validador.integration.web.action.base.BaseAction;
import com.msaf.validador.integration.web.form.TesteWebServiceForm;

public class TesteWebServiceAction extends BaseAction {
	
	protected static final String MSG_KEY_DEFAULT = "default";
	protected static final String MSG_PROP_GERAL = "geral";

	protected UsuarioVO pesquisaUsuario(HttpServletRequest request,
			String userName) throws ValidadorLoginException {
		return null;
	}

	public ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		TesteWebServiceForm webServiceForm = getFormLocal(form);
		webServiceForm.restart();
		return mapping.findForward("init");
	}
	
	public ActionForward doValidar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		TesteWebServiceForm webServiceForm = getFormLocal(form);
		ActionMessages actionMessages = validarRequisicao(request, webServiceForm);
		saveMessages(request, actionMessages);
		if(actionMessages.size()>0){
			return mapping.findForward("init");
		}		
		
		ResulConsVO resulConsVO = webServiceServer.processarRegistro(webServiceForm.getPessoaVO(), webServiceForm.getTipo());
		
		if(null!=resulConsVO && null!=resulConsVO.getRegistrosRetorno() && resulConsVO.getRegistrosRetorno().size() !=0 ) {
			Set<DadosRetInstVO> registrosRetorno = resulConsVO.getRegistrosRetorno();
			Object[] lista = (Object[]) registrosRetorno.toArray();
			if(lista.length != 0){
				webServiceForm.setDadosRetInstVO((DadosRetInstVO)lista[0]);
				webServiceForm.setResulConsVO(resulConsVO);
			} 
		} else {
			if(null!=resulConsVO.getCodErro() &&!"".equals(resulConsVO.getCodErro())) {
				webServiceForm.setDadosRetInstVO(new DadosRetInstVO());
				actionMessages = getMessages(request) ;
				actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,resulConsVO.getCodErro() + " - " + resulConsVO.getErro()));
				saveMessages(request, actionMessages);
			}
		}
		
		
		
		return mapping.findForward("init");
	}

	private ActionMessages validarRequisicao(HttpServletRequest request,
			TesteWebServiceForm webServiceForm) {
		ActionMessages actionMessages = getMessages(request);
		if("3".equals(webServiceForm.getTipo())) {
			if(Util.isEmpty(webServiceForm.getPessoaVO().getCnpj()))
				actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Atenção! CNPJ não pode ser Vazio!"));
		} else {
			if(Util.isEmpty(webServiceForm.getPessoaVO().getCnpj()))
				actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Atenção! CNPJ não pode ser Vazio!"));
			if(Util.isEmpty(webServiceForm.getPessoaVO().getEstado()))
				actionMessages.add(MSG_PROP_GERAL,new ActionMessage(MSG_KEY_DEFAULT,"Atenção! Estado não pode ser Vazio!"));
		}
		return actionMessages;
	}

	private TesteWebServiceForm getFormLocal(ActionForm form) {
		return (TesteWebServiceForm) form;
	}
	
	
	private WebServiceServer webServiceServer;

	public WebServiceServer getWebServiceServer() {
		return webServiceServer;
	}

	public void setWebServiceServer(WebServiceServer webServiceServer) {
		this.webServiceServer = webServiceServer;
	}
	



}
