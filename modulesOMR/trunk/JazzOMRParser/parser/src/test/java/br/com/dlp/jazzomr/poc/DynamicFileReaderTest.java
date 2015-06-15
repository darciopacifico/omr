/**
 * 
 */
package br.com.dlp.jazzomr.poc;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * @author darcio
 *
 */
@ContextConfiguration(locations = "/ApplicationContext_testng.xml")
@Test
public class DynamicFileReaderTest extends AbstractTestNGSpringContextTests {
	
	public static Logger log = LoggerFactory.getLogger(JazzOMRImageParserTeste.class);
		
	public void testConcat(){
		System.out.println("darcio");
		
	}
	
	public void testReadingZIP() throws IOException{
		
		String fileFolded = "/home/darcio/lixo/contabFoldedFiles.zip";
		String rootedZip = "/home/darcio/lixo/zipedExams.zip";
		
		
    // create a buffer to improve copy performance later.
    byte[] buffer = new byte[2048];

    // open the zip file stream
    InputStream theFile = new FileInputStream(fileFolded);
    ZipInputStream stream = new ZipInputStream(theFile);

    try
    {

        // now iterate through each item in the stream. The get next
        // entry call will return a ZipEntry for each file in the
        // stream
        ZipEntry entry;
        while((entry = stream.getNextEntry())!=null)
        {

            if(!entry.isDirectory()){
	            	
	            OutputStream output = new ByteArrayOutputStream();
	            try
	            {
	                int len = 0;
	                while ((len = stream.read(buffer)) > 0)
	                {
	                    output.write(buffer, 0, len);
	                }
	                
	                
	                
	                
	            }
	            finally
	            {
	                if(output!=null) output.close();
	            }
            }
        }
    }
    finally
    {
        // we must always close the zip file.
        stream.close();
    }
		
				
		
		log.debug("ok");
		
	}
	
	
	
}
