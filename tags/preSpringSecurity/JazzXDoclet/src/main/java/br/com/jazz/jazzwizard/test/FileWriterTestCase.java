/*
 * Created on 10/08/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.jazz.jazzwizard.test;

import java.io.CharArrayWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author PLDarcio
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class FileWriterTestCase {
	
	public static void main(final String[] args) throws IOException {
		
		final FileWriter fileWriter = new FileWriter("c:\\java\\lixo\\logXdoclet.log");
		
		final CharArrayWriter charArrayWriter = new CharArrayWriter();
		
		charArrayWriter.write("zuba lele");
		
		charArrayWriter.writeTo(fileWriter);
		
		fileWriter.flush();
		fileWriter.close();
		
	}
	
}
