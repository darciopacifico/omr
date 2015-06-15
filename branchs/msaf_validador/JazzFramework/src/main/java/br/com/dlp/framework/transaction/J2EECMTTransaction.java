package br.com.dlp.framework.transaction;

import javax.ejb.SessionContext;

public class J2EECMTTransaction extends AbstractTransaction {

	private SessionContext sessionContext;

	public void addTransacionalResource(Transacional transacional)
			throws TransactionException {
		if (logger.isDebugEnabled()) {
			logger
					.debug("O método addTransacionalResource foi sobreescrito, pois este não tem "
							+ "efeito sobre uma transação CMT");
		}
	}

	public void begin() throws TransactionException {
		if (logger.isDebugEnabled()) {
			logger.debug("O método begin foi sobreescrito, pois este não tem "
					+ "efeito sobre uma transação CMT");
		}
	}

	public void commit() throws TransactionException {
		if (logger.isDebugEnabled()) {
			logger.debug("O método commit foi sobreescrito, pois este não tem "
					+ "efeito sobre uma transação CMT");
		}
	}

	public void rollBack() throws TransactionException {
		if (logger.isDebugEnabled()) {
			logger
					.debug("O método rollBack foi sobreescrito. Este apenas invoca o método "
							+ "sessionContext.setRollbackOnly(). Pois a transação é CMT ");
		}
		sessionContext.setRollbackOnly();
	}

	public J2EECMTTransaction(SessionContext sessionContext) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("Construindo um gerenciador de transação do tipo J2EECMTTransaction!");
		}
		this.sessionContext = sessionContext;
	}

	public boolean isTerminated() {
		if (logger.isDebugEnabled()) {
			logger
					.debug("O método isTerminated foi sobreescrito. Pois uma transação CMT"
							+ "sempre acaba. A especificação J2EE garante! ");
		}
		return true;
	}

}
