package br.com.dlp.framework.transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementação abstrata de ITransaction
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
					+ " à transação " + this);
		}

		transacional.begin();
		transacionalResources.add(transacional);
	}

	/**
	 * Só pode ser invocado uma única vez até que método commit seja invocado
	 */
	public void begin() throws TransactionException {
		if (!opened) {
			this.opened = true;
			this.commited = false;
			this.rollBack = false;
		} else {
			throw new TransactionException(
					"O método begin() falhou! A transação já foi iniciada!",
					"begin.transacaoJaFoiIniciada");
		}
	}

	/**
	 * Commita a transação. Só pode ser invocado uma única vez após o método
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
							+ transacional + " da transação " + this);
				}
				transacional.commit();

			} catch (TransactionException e) {
				logger.error("Não foi possível comitar o recurso transacional "
						+ transacional + " da transação " + this
						+ ". A unidade de trabalho foi quebrada!");
				throw new TransactionException(
						"Uma parte da unidade de trabalho da transação não pôde ser comitada!",
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
					"Erro ao tentar invocar rollback. A transação não foi iniciada!",
					"rollBack.TransactionException.naoaberta");
		}

		if (commited == true) {
			throw new TransactionException("A transação já foi comitada!",
					"rollBack.TransactionException.commitada");
		}

		Iterator iterator = transacionalResources.iterator();
		while (iterator.hasNext()) {
			Transacional transacional = (Transacional) iterator.next();
			try {
				logger.warn("Efetuando rollBack() no recurso transacional "
						+ transacional + " da transação " + this);
				transacional.rollBack();

			} catch (TransactionException e) {
				logger
						.error("Não foi possível efetuar rollBack no recurso transacional "
								+ transacional
								+ " da transação "
								+ this
								+ ". A unidade de trabalho foi quebrada!");
				throw new TransactionException(
						"Não foi possível efetuar rollback numa parte da transação! A unidade de trabalho foi quebrada!",
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
