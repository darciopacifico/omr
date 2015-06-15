/**
 * 
 */
package br.com.dlp.jazzomr.event;


/**
 * @author darcio
 *
 */
public interface IImageFileWalk {

	void processFile(byte[] bytes, IImageParserVisitor visitor);
	
	

}
