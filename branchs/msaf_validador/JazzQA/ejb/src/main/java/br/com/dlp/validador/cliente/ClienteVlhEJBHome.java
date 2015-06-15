/*
 * Gerado pelo XDoclet - Não edite!
 */
package br.com.dlp.validador.cliente;

/**
 * Interface Home para ClienteVlhEJB.
 * @xdoclet-generated at ${TODAY}
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface ClienteVlhEJBHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/ClienteVlhEJB";
   public static final String JNDI_NAME="br/com/dlp/validador/cliente/ClienteVlh";

   public br.com.dlp.validador.cliente.ClienteVlhEJB create()
      throws javax.ejb.CreateException,java.rmi.RemoteException,java.rmi.RemoteException;

}
