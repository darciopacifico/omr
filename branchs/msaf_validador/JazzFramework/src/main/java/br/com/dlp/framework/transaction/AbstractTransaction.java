package br.com.dlp.framework.transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementa��o abstrata de ITransaction
 */
public abstract class AbstractTransaction implements ITransaction {
	protected Log logger = LogFactory.getLog(AbstractTransaction.class);

	private boolean opened = false;

	private boolean commited = false;

	private boolean rollBack = false;

	private Collection transacionalResources = new ArrayList(2);

	public void addTransacionalResource(Transacional transacional)
			throws TransactionException {
		if (transacional == null) {
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Adicionando o recurso transacional " + transacional
					+ " � transa��o " + this);
		}

		transacional.begin();
		transacionalResources.add(transacional);
	}

	/**
	 * S� pode ser invocado uma �nica vez at� que m�todo commit seja invocado
	 */
	public void begin() throws TransactionException {
		if (!opened) {
			this.opened = true;
			this.commited = false;
			this.rollBack = false;
		} else {
			throw new TransactionException(
					"O m�todo begin() falhou! A transa��o j� foi iniciada!",
					"begin.transacaoJaFoiIniciada");
		}
	}

	/**
	 * Commita a transa��o. S� pode ser invocado uma �nica vez ap�s o m�todo
	 * begin() ser invocado
	 * 
	 * @throws TransactionException
	 * @throws
	 */
	public void commit() throws TransactionException {

		Iterator iterator = transacionalResources.iterator();
		while (iterator.hasNext()) {
			Transacional transacional = (Transacional) iterator.next();
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("Comitando o recurso transacional "
							+ transacional + " da transa��o " + this);
				}
				transacional.commit();

			} catch (TransactionException e) {
				logger.error("N�o foi poss�vel comitar o recurso transacional "
						+ transacional + " da transa��o " + this
						+ ". A unidade de trabalho foi quebrada!");
				throw new TransactionException(
						"Uma parte da unidade de trabalho da transa��o n�o p�de ser comitada!",
						e);
			}
		}

		this.opened = false;
		this.commited = true;
		this.rollBack = false;
	}

	public void rollBack() throws TransactionException {
		if (opened == false) {
			throw new TransactionException(
					"Erro ao tentar invocar rollback. A transa��o n�o foi iniciada!",
					"rollBack.TransactionException.naoaberta");
		}

		if (commited == true) {
			throw new TransactionException("A transa��o j� foi comitada!",
					"rollBack.TransactionException.commitada");
		}

		Iterator iterator = transacionalResources.iterator();
		while (iterator.hasNext()) {
			Transacional transacional = (Transacional) iterator.next();
			try {
				logger.warn("Efetuando rollBack() no recurso transacional "
						+ transacional + " da transa��o " + this);
				transacional.rollBack();

			} catch (TransactionException e) {
				logger
						.error("N�o foi poss�vel efetuar rollBack no recurso transacional "
								+ transacional
								+ " da transa��o "
								+ this
								+ ". A unidade de trabalho foi quebrada!");
				throw new TransactionException(
						"N�o foi poss�vel efetuar rollback numa parte da transa��o! A unidade de trabalho foi quebrada!",
						e);
			}
		}

		opened = false;
		commited = false;
		rollBack = true;
	}

	public boolean isCommited() {
		return commited;
	}

	public boolean isOpened() {
		return opened;
	}

	public boolean isRollBack() {
		return rollBack;
	}

	public boolean isTerminated() {
		return (commited || rollBack);
	}
}
