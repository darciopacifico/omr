package com.msaf.validador.integration.hibernate.intf;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.msaf.validador.consultaonline.cliente.ClienteVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.integration.hibernate.base.IDAOGenerico;

public interface IPedValidacaoDAO extends IDAOGenerico<PedValidacaoVO, Long>{
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,readOnly=true)
	public List<PedValidacaoVO> listaPedidosValidacaoCliente(ClienteVO cliente);

}
