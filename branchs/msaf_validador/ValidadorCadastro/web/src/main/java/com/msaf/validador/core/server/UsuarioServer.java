package com.msaf.validador.core.server;

import com.msaf.validador.core.dominio.UsuarioVO;
import com.msaf.validador.integration.hibernate.Repositorio;

public class UsuarioServer {
	
	private Repositorio repositorio;
	
	public UsuarioServer() {}
	
	public UsuarioServer(Repositorio repositorio) {
		this.repositorio = repositorio;
	}
	
	public UsuarioVO validarUsuario(UsuarioVO usuarioVO) {
		return repositorio.getUsuarioDAO().validaLoginSenha(usuarioVO);
	}



}
