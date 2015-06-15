package br.com.dlp.jazzomr.poc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopiaArquivo {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {

		FileInputStream fis = null;
		FileOutputStream fos = null;

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			fis = new FileInputStream(new File(
					"/home/darcio/trabalho/omr/marksfoundform-small.gif"));
			fos = new FileOutputStream(new File(
					"/home/darcio/trabalho/omr/marksfoundform-smallC.gif"));

			bis = new BufferedInputStream(fis);
			bos = new BufferedOutputStream(fos);

			copy(bis, bos, 1024);

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
				fos.flush();
				fos.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

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
