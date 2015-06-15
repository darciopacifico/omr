package br.com.dlp.validador.pedvalidacao;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import br.com.dlp.framework.facade.J2EECMTFacade;

/** 
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 * 	
 * Extensão de PedValidacaoFacadeImpl para criação de um EJB
 * 	
 * @ejb.bean description="value"
 *           display-name="PedValidacao"
 *           jndi-name="br/com/dlp/validador/pedvalidacao/PedValidacao"
 *           name="PedValidacaoEJB"
 *           type="Stateless"
 *           view-type="remote"
 * 						
 * @ejb.interface extends="javax.ejb.EJBObject,br.com.dlp.validador.pedvalidacao.PedValidacaoFacade"
 * 
 * @ejb.transaction type = "Required"
 */

public class PedValidacaoSessionFacade extends PedValidacaoFacadeImpl implements J2EECMTFacade, SessionBean {
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