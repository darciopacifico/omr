/**
 * 
 */
package br.com.jazz.codegen.exception;


/**
 * Denota erro na criaçao do diretório de destino do template
 * @author darcio
 *
 */
public class DestDirCreationException extends FileWriterFactoryException {

	/**
	 * 
	 */
	public DestDirCreationException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public DestDirCreationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public DestDirCreationException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public DestDirCreationException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
