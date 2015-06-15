package br.com.dlp.framework.facade;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;

/**
 * Contrato para que um SessionFacade do Framework tenha sua transacao CMT
 */
public abstract interface J2EECMTFacade {
	public SessionContext getSessionContext() throws EJBException,
			RemoteException;
}
