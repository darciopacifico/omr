package br.com.dlp.jazzomr.poc;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageRotator {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {

		FileInputStream fis = null;

		BufferedInputStream bis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = new FileInputStream(new File(
					"ImagemOriginal.gif"));

			bis = new BufferedInputStream(fis);
			bos = new ByteArrayOutputStream();

			copy(bis, bos, 1024);

			byte[] rotacionada =  rotateImage(bos.toByteArray());
			
			FileOutputStream fosRot = new FileOutputStream(new File("ImagemOriginalRotaticionada.gif"));
			
			fosRot.write(rotacionada);
			
			fosRot.flush();
			fosRot.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				bis.close();
				bos.flush();
				bos.close();
				fis.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * 
	 * @param byteArray
	 * @return
	 * @throws IOException 
	 */
	private static byte[] rotateImage(byte[] byteArray) throws IOException {
		
	  
		
	  
		return byteArray;
	}

	public static long copy(InputStream inputStream, OutputStream outputStream,
			int bufferSize) throws IOException {

		byte[] buffer = new byte[bufferSize];
		long count = 0;
		int n;
		while (-1 != (n = inputStream.read(buffer))) {
			outputStream.write(buffer, 0, n);
			count += n;
		}
		return count;

	}

}
