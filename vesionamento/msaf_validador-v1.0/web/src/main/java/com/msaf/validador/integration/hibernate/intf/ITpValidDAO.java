package com.msaf.validador.integration.hibernate.intf;

import java.util.List;

import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;
import com.msaf.validador.integration.hibernate.base.IDAOGenerico;

public interface ITpValidDAO extends IDAOGenerico<TpValidVO, Long>{

	List<TpValidVO> validacoeComerciais();


}
