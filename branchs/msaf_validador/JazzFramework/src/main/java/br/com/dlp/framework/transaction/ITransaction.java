package br.com.dlp.framework.transaction;

/**
 * Contrato de controle de transa��o
 */
public interface ITransaction {
	public void begin() throws TransactionException;

	public void rollBack() throws TransactionException;

	public void commit() throws TransactionException;

	public boolean isCommited();

	public boolean isOpened();

	public boolean isTerminated();

	/**
	 * Associa � transa��o um recurso transacional, ou seja, eu estou incluindo
	 * a transa��o de um recurso externo � minha unidade de trabalho
	 */
	public void addTransacionalResource(Transacional transacional)
			throws TransactionException;

}
