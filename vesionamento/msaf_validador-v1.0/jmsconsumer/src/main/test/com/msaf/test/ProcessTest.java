package com.msaf.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ProcessTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//	ProcessBuilder processBuilder = new ProcessBuilder("C:\\darcio\\appInstall\\apache-maven-2.0.9\\bin\\mvn.bat -version");
		ProcessBuilder processBuilder = new ProcessBuilder("notepad");
		Process process;
		try {
			process = processBuilder.start();
		} catch (IOException e2) {
			throw new RuntimeException("Erro ao tentar rodar processo",e2);
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
		
		BufferedWriter bufferedWriter = new BufferedWriter(writer);

		try {
			for(int i=0; i<100; i++){
				System.out.println(reader.readLine());
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		process.destroy();
	}

}
