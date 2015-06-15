package br.com.dlp.framework.facade;

import java.rmi.RemoteException;
import java.util.List;

import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.pk.IPK;
import br.com.dlp.framework.transaction.ITransaction;
import br.com.dlp.framework.transaction.TransactionFactoryException;
import br.com.dlp.framework.vo.ICadastroBaseVO;

public interface ICadastroFacade extends IFacade {

	public ICadastroBaseVO newVO() throws BaseBusinessException,
			BaseTechnicalError, RemoteException;

	public ICadastroBaseVO incluir(ICadastroBaseVO baseVO)
			throws BaseBusinessException, BaseTechnicalError, RemoteException;

	public ICadastroBaseVO alterar(ICadastroBaseVO baseVO)
			throws BaseBusinessException, BaseTechnicalError, RemoteException;

	public void excluir(ICadastroBaseVO baseVO) throws BaseBusinessException,
			BaseTechnicalError, RemoteException;

	public ICadastroBaseVO lockObject(ICadastroBaseVO baseVO)
			throws BaseBusinessException, BaseTechnicalError, RemoteException;

	public List findAll() throws BaseBusinessException, BaseTechnicalError,
			RemoteException;

	public ICadastroBaseVO findByPrimaryKey(IPK pk)
			throws BaseBusinessException, BaseTechnicalError, RemoteException;

	public ITransaction getNewTransaction() throws TransactionFactoryException,
			RemoteException;

	public boolean exists(IPK pk) throws BaseBusinessException,
			BaseTechnicalError, RemoteException;

}
