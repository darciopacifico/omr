package com.msaf.validador.consultaonline.exceptions;


/**
 * source: 	PersistenciaException.java
 * author: 	[Ericsson] <AUTHOR>
 * description: <DESCRIPTION>
 * version: First version 
 * creation date: 2008-11-10
 */
public class PersistenciaException extends Exception
{
   
   /**
    * @param 
    * @date 2008-11-12
    * @author Ekler Paulino de Mattos
    * @exception
    * @roseuid 491853D000C5
    */
   public PersistenciaException() 
   {
    super();
   }
   
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for 
     *          later retrieval by the {@link #getMessage()} method.
     * @date 2008-11-12
     * @author Ekler Paulino de Mattos
     */
    public PersistenciaException(String message) {
	super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @date 2008-11-12
     * @author Ekler Paulino de Mattos
     */
    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @date 2008-11-12
     * @author Ekler Paulino de Mattos
     */
    public PersistenciaException(Throwable cause) {
        super(cause);
    }
   
}
