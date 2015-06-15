package br.com.dlp.framework.transaction;

/**
 * Contrato de controle de transação
 */
public interface ITransaction {
	public void begin() throws TransactionException;

	public void rollBack() throws TransactionException;

	public void commit() throws TransactionException;

	public boolean isCommited();

	public boolean isOpened();

	public boolean isTerminated();

	/**
	 * Associa à transação um recurso transacional, ou seja, eu estou incluindo
	 * a transação de um recurso externo à minha unidade de trabalho
	 */
	public void addTransacionalResource(Transacional transacional)
			throws TransactionException;

}
