package br.com.dlp.framework.facade;

import java.rmi.RemoteException;

import br.com.dlp.framework.exception.BaseTechnicalError;

public interface IFacade {
	public void finishFacade() throws BaseTechnicalError, RemoteException;
}
