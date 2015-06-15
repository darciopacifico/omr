package com.msaf.validador.integration.hibernate.intf;

import com.msaf.validador.consultaonline.exceptions.ValidadorLoginException;
import com.msaf.validador.core.dominio.UsuarioVO;
import com.msaf.validador.integration.hibernate.base.IDAOGenerico;

public interface IUsuarioDAO extends IDAOGenerico<UsuarioVO, Long>{

	public UsuarioVO validaLoginSenha(UsuarioVO usuarioVO);

	public UsuarioVO findByUserName(String userName) throws ValidadorLoginException;

}
