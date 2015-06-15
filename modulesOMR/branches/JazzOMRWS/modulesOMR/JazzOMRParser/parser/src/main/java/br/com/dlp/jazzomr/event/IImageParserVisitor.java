/**
 * 
 */
package br.com.dlp.jazzomr.event;

import java.awt.image.BufferedImage;


/**
 * @author darcio
 *
 */
public interface IImageParserVisitor {

	/*void visit(byte[] bs);*/
	
	void visit(BufferedImage bi);
	
}
