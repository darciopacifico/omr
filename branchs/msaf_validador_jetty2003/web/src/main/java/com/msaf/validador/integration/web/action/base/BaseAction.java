package com.msaf.validador.integration.web.action.base;

import java.security.Principal;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.actions.DispatchAction;

import com.msaf.validador.consultaonline.exceptions.ValidadorLoginException;
import com.msaf.validador.core.dominio.UsuarioVO;
import com.msaf.validador.integration.util.Util;

/**
 * Implementa��o abstrata de action para o projeto. Implementa consulta de usu�rio logado.
 * @author dlopes
 *
 */
public abstract class BaseAction extends DispatchAction {
	
	/**
	 * Recupera o registro de usu�rio do BD a partir do usu�rio logado no JAAS
	 * @param request
	 * @return
	 * @throws ValidadorLoginException 
	 */
	public UsuarioVO getUsuario(HttpServletRequest request) throws ValidadorLoginException {
		Principal principal = request.getUserPrincipal();
		String loginName = principal.getName();
		
		UsuarioVO usuarioVO = pesquisaUsuario(request, loginName);
		
		return usuarioVO;
	}
	
	
	/**
	 * Pesquisar usu�rio na base de dados
	 * @param userName
	 * @return
	 * @throws ValidadorLoginException 
	 */
	protected abstract UsuarioVO pesquisaUsuario(HttpServletRequest request, String userName) throws ValidadorLoginException;


	public Long validaID(ServletRequest request, String parm) {
		String id = request.getParameter(parm);
		if(Util.isEmpty(id))   throw new IllegalArgumentException("Id pedido vazio!");
		return Long.parseLong(id);
	}
	
}