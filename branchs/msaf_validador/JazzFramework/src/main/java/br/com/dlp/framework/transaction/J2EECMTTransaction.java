package br.com.dlp.framework.transaction;

import javax.ejb.SessionContext;

public class J2EECMTTransaction extends AbstractTransaction {

	private SessionContext sessionContext;

	public void addTransacionalResource(Transacional transacional)
			throws TransactionException {
		if (logger.isDebugEnabled()) {
			logger
					.debug("O m�todo addTransacionalResource foi sobreescrito, pois este n�o tem "
							+ "efeito sobre uma transa��o CMT");
		}
	}

	public void begin() throws TransactionException {
		if (logger.isDebugEnabled()) {
			logger.debug("O m�todo begin foi sobreescrito, pois este n�o tem "
					+ "efeito sobre uma transa��o CMT");
		}
	}

	public void commit() throws TransactionException {
		if (logger.isDebugEnabled()) {
			logger.debug("O m�todo commit foi sobreescrito, pois este n�o tem "
					+ "efeito sobre uma transa��o CMT");
		}
	}

	public void rollBack() throws TransactionException {
		if (logger.isDebugEnabled()) {
			logger
					.debug("O m�todo rollBack foi sobreescrito. Este apenas invoca o m�todo "
							+ "sessionContext.setRollbackOnly(). Pois a transa��o � CMT ");
		}
		sessionContext.setRollbackOnly();
	}

	public J2EECMTTransaction(SessionContext sessionContext) {
		if (logger.isDebugEnabled()) {
			logger
					.debug("Construindo um gerenciador de transa��o do tipo J2EECMTTransaction!");
		}
		this.sessionContext = sessionContext;
	}

	public boolean isTerminated() {
		if (logger.isDebugEnabled()) {
			logger
					.debug("O m�todo isTerminated foi sobreescrito. Pois uma transa��o CMT"
							+ "sempre acaba. A especifica��o J2EE garante! ");
		}
		return true;
	}

}
