package com.msaf.validador.integration.hibernate.intf;

import java.util.List;

import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.integration.hibernate.base.IDAOGenerico;

public interface IDadosRetInstDAO  extends IDAOGenerico<DadosRetInstVO, Long>{

	
	public List<DadosRetInstVO> findByPessoa(Long idPessoa);
	
	public List<DadosRetInstVO> findPagesByPessoa(Long idPessoa);
}
