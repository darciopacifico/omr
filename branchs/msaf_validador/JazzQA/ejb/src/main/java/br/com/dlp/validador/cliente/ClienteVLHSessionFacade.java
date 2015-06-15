package br.com.dlp.validador.cliente;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/** 
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 * 	
 * Extensão de ClienteVLHFacadeImpl para criação de um EJB
 * 	
 * @ejb.bean description="value"
 *           display-name="ClienteVlh"
 *           jndi-name="br/com/dlp/validador/cliente/ClienteVlh"
 *           name="ClienteVlhEJB"
 *           type="Stateful"
 *           view-type="remote"
 * 						
 * @ejb.interface extends="javax.ejb.EJBObject,br.com.dlp.validador.cliente.ClienteVLHFacade"
 * 
 * @ejb.transaction type = "Required"
 */

public class ClienteVLHSessionFacade extends ClienteVLHFacadeImpl implements SessionBean {
	/**
	 * Controle de versão para classes serializáveis
	 */
	private static final long serialVersionUID = 907976127239847L;

	private SessionContext sessionContext;
	public void ejbActivate() throws EJBException, RemoteException {
	}
	public void ejbPassivate() throws EJBException, RemoteException {
	}
	public void ejbRemove() throws EJBException, RemoteException {
	}

	public SessionContext getSessionContext() throws EJBException,RemoteException {
		return sessionContext;
	}
	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		sessionContext = ctx;
	}
	/**
	 * @ejb.create-method view-type = "remote"
	 **/
	public void ejbCreate()throws CreateException, RemoteException {
	}
}