/**
 * 
 */
package br.com.dlp.jazzomr.poc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author darcio
 *
 */
public class ZipStreamTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		FileInputStream fis = new FileInputStream("16.jpg");
		
		
		FileOutputStream fos = new FileOutputStream("16.zip");
		//BufferedOutputStream bos = new BufferedOutputStream(fos);
		ZipOutputStream zos = new ZipOutputStream(fos);
		zos.setLevel(Deflater.BEST_SPEED);
		zos.putNextEntry(new ZipEntry("16.jpg"));
		
		
		byte[] buffer = new byte[8];
		long count = 0;
		int n;
		while (-1 != (n = fis.read(buffer))) {
			zos.write(buffer, 0, n);
			count += n;
		}


		zos.flush();
		zos.close();
		
		fis.close();
		
		
	}

}
