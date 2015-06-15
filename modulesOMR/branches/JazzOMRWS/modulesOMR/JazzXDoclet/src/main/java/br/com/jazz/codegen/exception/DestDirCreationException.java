/**
 * 
 */
package br.com.jazz.codegen.exception;

/**
 * Denota erro na criaçao do diretório de destino do template
 * 
 * @author darcio
 * 
 */
public class DestDirCreationException extends FileWriterFactoryException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4975684651617256194L;
	
	/**
	 * 
	 */
	public DestDirCreationException() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param arg0
	 */
	public DestDirCreationException(final String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param arg0
	 * @param arg1
	 */
	public DestDirCreationException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param arg0
	 */
	public DestDirCreationException(final Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
}
