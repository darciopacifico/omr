/*
 * Gerado pelo XDoclet - Não edite!
 */
package br.com.dlp.validador.pedvalidacao;

/**
 * Interface Home para PedValidacaoEJB.
 * @xdoclet-generated at ${TODAY}
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface PedValidacaoEJBHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/PedValidacaoEJB";
   public static final String JNDI_NAME="br/com/dlp/validador/pedvalidacao/PedValidacao";

   public br.com.dlp.validador.pedvalidacao.PedValidacaoEJB create()
      throws javax.ejb.CreateException,java.rmi.RemoteException,java.rmi.RemoteException;

}
