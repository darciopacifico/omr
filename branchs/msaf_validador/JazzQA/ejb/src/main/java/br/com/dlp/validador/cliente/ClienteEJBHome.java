/*
 * Gerado pelo XDoclet - Não edite!
 */
package br.com.dlp.validador.cliente;

/**
 * Interface Home para ClienteEJB.
 * @xdoclet-generated at ${TODAY}
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface ClienteEJBHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/ClienteEJB";
   public static final String JNDI_NAME="br/com/dlp/validador/cliente/Cliente";

   public br.com.dlp.validador.cliente.ClienteEJB create()
      throws javax.ejb.CreateException,java.rmi.RemoteException,java.rmi.RemoteException;

}
