package com.msaf.validador.integration.hibernate.intf;

import java.util.List;

import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PessoaVO;
import com.msaf.validador.integration.hibernate.base.IDAOGenerico;

public interface IPessoaDAO   extends IDAOGenerico<PessoaVO, Long> {

	
	public List<PessoaVO> listByPedidoValidacao(PedValidacaoVO vo);

	
	public List<PessoaVO> findByIdPedido(Long idPedido);
}
