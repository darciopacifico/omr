package com.msaf.test.process;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.apache.log4j.Logger;

import com.msaf.validador.consultaonline.ParametrosConsultaOnLineDTO;
import com.msaf.validador.consultaonline.exceptions.ValidadorUploadException;
import com.msaf.validador.consultaonline.jna.IConsultaOnLineJNA;
import com.msaf.validador.consultaonline.validador.ParametrosConsultaSimplesDTO;
import com.sun.jna.Library;
import com.sun.jna.Native;


/**
 * Classe que implementa execução do processo escravo para o validador
 * 
 * @author dlopes
 *
 */
public class ConsultaOnlineProcessoEscravo {

	static Logger log = Logger.getLogger(ConsultaOnlineProcessoEscravo.class);
	
	
	/**
	 * Recebe parametro de consulta, executa consulta na DLL do validador e escreve o resultado em outputstream
	 * @param args
	 * @throws IOException 
	 * @throws ValidadorUploadException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ValidadorUploadException, ClassNotFoundException {
		
		ConsultaOnlineProcessoEscravo processSlaveTest = new ConsultaOnlineProcessoEscravo();
		
		ParametrosConsultaSimplesDTO consultaSimplesDTO = processSlaveTest.recebeObjeto();
		
		String resultado = processSlaveTest.executaConsultaValidador(consultaSimplesDTO);
		
		if(log.isDebugEnabled()) log.debug(resultado);
	}


	/**
	 * Executa consulta na DLL
	 * @param consultaSimplesDTO
	 * @return
	 */
	protected String executaConsultaValidador(ParametrosConsultaSimplesDTO consultaSimplesDTO) {

		IConsultaOnLineJNA consultaOnLineJNA = getConsultaOnLineJNA();
		
		Integer servico = consultaSimplesDTO.getServico();
		String parametros = consultaSimplesDTO.getParametros();
		String dllPath = consultaSimplesDTO.getDllPath();
		String evServer = consultaSimplesDTO.getEvServer();
		
		String resultado = consultaOnLineJNA.DBI_EfetuaConsultaServico(servico, parametros, dllPath, evServer);

		return resultado;
		
	}


	/**
	 * Carrega dll de consulta on line
	 */
	protected IConsultaOnLineJNA getConsultaOnLineJNA() {
		Native.setProtected(true);
		
		Library library = (Library) Native.loadLibrary("ConsultaOnLine", IConsultaOnLineJNA.class);
		
		library = Native.synchronizedLibrary(library);
		
		IConsultaOnLineJNA consultaOnLine = (IConsultaOnLineJNA) library;
		
		return consultaOnLine;
	}


	/**
	 * Recupera objeto enviado pelo processo Pai (provavelmente uma VM de um servidor de aplicacoes).
	 * @return
	 * @throws ValidadorUploadException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ParametrosConsultaSimplesDTO recebeObjeto() throws ValidadorUploadException, IOException, ClassNotFoundException{
		InputStream inputStream = System.in;
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
		
		transfereObjeto(bufferedOutputStream,bufferedInputStream);
		
		Object object = recriaObjetoParametro(byteArrayOutputStream);
		
		ParametrosConsultaSimplesDTO consultaSimplesDTO = null;
		
		if(object!=null && object instanceof ParametrosConsultaOnLineDTO){
			consultaSimplesDTO = (ParametrosConsultaSimplesDTO) object;
		}
		
		return consultaSimplesDTO;
	}


	/**
	 * @param byteArrayOutputStream
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	protected Object recriaObjetoParametro(ByteArrayOutputStream byteArrayOutputStream) throws IOException, ClassNotFoundException {
		byte[] obj = byteArrayOutputStream.toByteArray();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(obj);
		ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
		Object object = objectInputStream.readObject();
		return object;
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
				if(log.isDebugEnabled()) log.debug("Erro ao tentar fechar bufferedInputStream");
			}
			
			try {
				bufferedOutputStream.close();
			} catch (IOException e) {
				if(log.isDebugEnabled()) log.debug("Erro ao tentar fechar bufferedOutputStream");
			}
		}
		return tamanhoArquivo;
	}
	
}
