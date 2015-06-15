package br.com.dlp.jazzqa;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import br.com.jazz.codegen.SimpleGeradorController;
import br.com.jazz.jazzwizard.exception.GeradorException;

public class SimpleCaller {

	/**
	 * @param args
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchMethodException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws GeradorException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) throws SecurityException, IllegalArgumentException, GeradorException, IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		SimpleGeradorController.main(new String[]{});
		
	}

}
