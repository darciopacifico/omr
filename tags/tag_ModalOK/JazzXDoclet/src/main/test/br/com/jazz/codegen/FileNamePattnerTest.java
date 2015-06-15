package br.com.jazz.codegen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author darcio
 *
 */
public class FileNamePattnerTest {

	public static void main(String[] args) throws IOException {

		String pkg = "br.com.dlp.jazzqa.projeto";
		
		String regex 			= "^.*\\.(.*$)";
		
		String regexDest 	= "$1";
		
		
		String somenteUltimo = pkg.replaceAll(regex, regexDest);
		
		System.out.println(somenteUltimo);
		
		
	}
	
}
