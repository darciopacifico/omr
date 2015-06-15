package br.com.dlp.framework.transaction;

public interface Transacional {
	public void begin() throws TransactionException;

	public void rollBack() throws TransactionException;

	public void commit() throws TransactionException;
}
