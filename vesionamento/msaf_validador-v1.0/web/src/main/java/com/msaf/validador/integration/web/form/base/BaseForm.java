package com.msaf.validador.integration.web.form.base;

import org.apache.struts.action.ActionForm;

import com.msaf.validador.core.dominio.UsuarioVO;

public class BaseForm extends ActionForm {
	
	private static final long serialVersionUID = -8400375406890966599L;
	
	private UsuarioVO usuarioVO = new UsuarioVO();
	
	public UsuarioVO getUsuarioVO() {
		return usuarioVO;
	}
	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}
}
