package com.msaf.test.process;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream; 
import java.io.ByteArrayOutputStream;
import java.io.IOException; 
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.hamcrest.core.Is;

import com.msaf.validador.consultaonline.exceptions.ValidadorUploadException;

public class ProcessSlaveTest {

	Logger log = Logger.getLogger(ProcessSlaveTest.class);
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ValidadorUploadException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ValidadorUploadException, ClassNotFoundException {
		
		new ProcessSlaveTest().recebeObjeto();
		
	}

	
	public void recebeObjeto() throws ValidadorUploadException, IOException, ClassNotFoundException{
		InputStream inputStream = System.in;
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		log.debug("OK stream de entrada recuperado");
		
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
		
		log.debug("recebendo objeto vindo do mestre...");
		transfereObjeto(bufferedOutputStream,bufferedInputStream);
		log.debug("...objeto vindo o mestre recebido.");
		
		
		log.debug("transformando array de bytes em objeto...");
		byte[] obj = byteArrayOutputStream.toByteArray();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(obj);
		ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
		Object object = objectInputStream.readObject();
		log.debug("... OK OBJETO RECUPERADO");
		
		
		log.debug("escrevendo string de retorno...");
		System.out.println("Objeto Origem:"+object);
		System.out.println("Objeto Origem:"+object);
		System.out.println("Objeto Origem:"+object);
		log.debug("...String de retorno OK");
		
		log.debug("flush e close no strea de saída...");
		System.out.flush();
		System.out.close();
		log.debug("...OK fechados");
		
		System.exit(0);
	}
	
	
	/**
	 * Executa transferência de bytes do arquivo de origem para o array de bytes na memoria
	 * @param outputStream
	 * @param bufferedOutputStream
	 * @param inputStream
	 * @param bufferedInputStream
	 * @throws ValidadorUploadException
	 */
	private long transfereObjeto(BufferedOutputStream bufferedOutputStream, BufferedInputStream bufferedInputStream) throws ValidadorUploadException {
		long tamanhoArquivo = 0;
		int qtdBytesLidos =0;
		try {
			do{
				
				byte[] bytesLidos=new byte[1000];
				qtdBytesLidos = bufferedInputStream.read(bytesLidos);
				
				//conta tamanho do arquivo
				tamanhoArquivo = tamanhoArquivo + qtdBytesLidos;
				
				bufferedOutputStream.write(bytesLidos);
				
			}while(qtdBytesLidos>0);
			bufferedOutputStream.flush();

		} catch (IOException e) {
			throw new ValidadorUploadException("Erro ao processar transferência de arquivo para array de bytes na memoria!",e);
		}finally{
			try {
				bufferedInputStream.close();
			} catch (IOException e) {
				log.error("Erro ao tentar fechar bufferedInputStream",e);
			}
			
			try {
				bufferedOutputStream.close();
			} catch (IOException e) {
				log.error("Erro ao tentar fechar bufferedOutputStream",e);
			}
		}
		return tamanhoArquivo;
	}
	
}
