package br.com.jazz.codegen.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ListTemplates {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
	
		List<String> fileNames = readTemplateDirectory("/fmTemplates/group");

		System.out.println(fileNames);
			
		
	}

	/**
	 * Retorna array de strings representando a lista de arquivos contidos no diretorio
	 * 
	 * @param urlPath TODO
	 * @return
	 * @throws IOException
	 */
	protected static List<String> readTemplateDirectory(String urlPath) throws IOException {
		List<String> fileNames = new ArrayList<String>(30);
		
		URL url = ListTemplates.class.getResource(urlPath);
		
		URLConnection connection = url.openConnection();
	
		Object obj = connection.getContent();
		
		InputStream inputStream = (InputStream) obj;
		
		InputStreamReader reader = new InputStreamReader(inputStream);
		
		BufferedReader bufferedReader = new BufferedReader(reader);
		
		
		String linha = bufferedReader.readLine();
		while(linha !=null ){
			fileNames.add(linha);
			linha = bufferedReader.readLine();
		}
		return fileNames;
	}

}
