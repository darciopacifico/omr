package br.com.dlp.framework.mdb.pool;

import java.util.Date;

import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;

public interface IPoolEJBMDBConsumer {

	public void onMessage(MDBMensagemVO mdbMensagem);

	public void setStatus(MDBMensagemVO mdbMensagem, String status)
			throws BaseBusinessException, BaseTechnicalError;

	public void setDtExecucao(MDBMensagemVO mdbMensagem, Date data)
			throws BaseBusinessException, BaseTechnicalError;

	public void setResultado(MDBMensagemVO mdbMensagem, Object obj)
			throws BaseBusinessException, BaseTechnicalError;

	public void setMensagem(MDBMensagemVO mdbMensagem)
			throws BaseBusinessException, BaseTechnicalError;

	public void trataExcecao(MDBMensagemVO mdbMensagem, Throwable excessao);
}
