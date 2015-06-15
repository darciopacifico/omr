package br.com.dlp.framework.mdb.base;

import java.util.Date;

import br.com.dlp.framework.dao.IDAO;
import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.mdb.pool.IPoolEJBMDBConsumer;
import br.com.dlp.framework.mdb.pool.MDBMensagemVO;
import br.com.dlp.framework.mdb.util.MDBConstants;
import br.com.dlp.framework.mdb.util.MDBUtils;
import br.com.dlp.framework.servicelocator.IServiceLocator;

public abstract class AbstractPoolEJMMDBConsumer implements IPoolEJBMDBConsumer {

	public AbstractPoolEJMMDBConsumer() {
	}

	public void setStatus(IServiceLocator serviceLocator,
			MDBMensagemVO mdbMensagem, String status)
			throws BaseBusinessException, BaseTechnicalError {
		IDAO dao;
		dao = MDBUtils.getDAO(serviceLocator);
		((AbstractMDBJDBCDAO) dao).updateStatus(mdbMensagem, status);
	}

	public void setDtExecucao(IServiceLocator serviceLocator,
			MDBMensagemVO mdbMensagem, Date data) throws BaseBusinessException,
			BaseTechnicalError {
		IDAO dao;
		dao = MDBUtils.getDAO(serviceLocator);
		((AbstractMDBJDBCDAO) dao).updateDtExecucao(mdbMensagem, data);
	}

	public void setResultado(IServiceLocator serviceLocator,
			MDBMensagemVO mdbMensagem, Object obj)
			throws BaseBusinessException, BaseTechnicalError {
		IDAO dao;
		dao = MDBUtils.getDAO(serviceLocator);
		((AbstractMDBJDBCDAO) dao).updateResultado(mdbMensagem, obj);
	}

	public void setMensagem(IServiceLocator serviceLocator,
			MDBMensagemVO mdbMensagem) throws BaseBusinessException,
			BaseTechnicalError {
		IDAO dao;
		dao = MDBUtils.getDAO(serviceLocator);
		((AbstractMDBJDBCDAO) dao).updateMensagem(mdbMensagem);
	}

	public void trataExcecao(IServiceLocator serviceLocator,
			MDBMensagemVO mdbMensagem, Throwable excessao) {

		String mensagem = "O processamento encontrou o seguinte problema : \n";

		Throwable excecaoDaVez = excessao;

		do {
			mensagem = mensagem + excecaoDaVez.toString() + "\n";
			mensagem = mensagem + excecaoDaVez.getMessage() + "\n";

			StackTraceElement[] stack = excessao.getStackTrace();
			for (int i = 0; i < stack.length; i++) {
				mensagem = mensagem + "\t " + stack[i].toString() + "\n";
			}
			mensagem = mensagem + "\n";

			excecaoDaVez = ((Throwable) excecaoDaVez).getCause();

		} while (excecaoDaVez != null);

		mdbMensagem.setMensagem(mensagem);
		try {
			setMensagem(mdbMensagem);
			setStatus(mdbMensagem, MDBConstants.MDBSTATUS_PROBLEMA);
		} catch (BaseBusinessException e) {
			e.printStackTrace();
		} catch (BaseTechnicalError e) {
			e.printStackTrace();
		}

	}

}
