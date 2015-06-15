package com.msaf.test.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.msaf.validador.consultaonline.ParametrosConsultaOnLineDTO;

public class ProcessMasterTest {
	static Logger log = Logger.getLogger(ProcessMasterTest.class);
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		if(log.isDebugEnabled()) log.debug("iniciando adm de processos");
		//inicializa processo separado
		
		
		ProcessBuilder processBuilder = new ProcessBuilder("java","-jar", "msaf.validador.jmsconsumer-1.0.jar");
		processBuilder.directory(new File("C:\\darcio\\trabalho\\SVN_Validador\\jmsconsumer\\target"));

		
		Process process = processBuilder.start();
		
		ParametrosConsultaOnLineDTO dtoParaConsulta = new ParametrosConsultaOnLineDTO();
		transfereParametrosConsultaOnLine(process, dtoParaConsulta);
		
		
		
		long tempoMaximo = System.currentTimeMillis()+1000;
		
		while(System.currentTimeMillis() < tempoMaximo){
			process.destroy();
		}
		
		
		//checa resposta
		InputStream inputStream = process.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String linha = bufferedReader.readLine();

		
		if(log.isDebugEnabled()) log.debug(linha);

		
		bufferedReader.close();
		inputStreamReader.close();
		inputStream.close();
		
		
	}

	/**
	 * @param process
	 * @param dtoParaConsulta
	 * @throws IOException
	 */
	protected static void transfereParametrosConsultaOnLine(Process process, ParametrosConsultaOnLineDTO dtoParaConsulta) throws IOException {
		//recupera outputstream do processo (canal de troca de dados com processo pai)
		OutputStream processOutputStream = process.getOutputStream();
		

		//cria e envia onbjeto para processo filho
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(processOutputStream);
		objectOutputStream.writeObject(dtoParaConsulta);
		
		objectOutputStream.close();
		processOutputStream.close();
	}

	/**
	 * @return
	 * @throws IOException
	 */
	protected static Process criaProcesso_ConsultaOnLine() throws IOException {

		ProcessBuilder processBuilder = new ProcessBuilder(
				"java", 
				//"-Xdebug", "-Xrunjdwp:transport=dt_socket,address=4142,server=y,suspend=n",		
				"-jar", "msaf.validador.jmsconsumer-1.0.jar"
		);
		
		processBuilder.directory(new File("C:\\darcio\\trabalho\\SVN_Validador\\jmsconsumer\\target"));
		Process process = processBuilder.start();
		return process;
	}

}
