/**
 * 
 */
package br.com.dlp.lab;

import java.util.EnumSet;
import java.util.Iterator;

import br.com.jazz.codegen.enums.JazzRenderer;

/**
 * @author darcio
 *
 */
public class EnumLab {

	/**
	 * @param args
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {

		
		listEnum(JazzRenderer.class);
		
	}

	/**
	 * 
	 */
	protected static void listEnum(Class<? extends Enum> class1) {
		
		EnumSet allOf = EnumSet.allOf(class1);
		
		
		System.out.println(allOf);
		
		EnumSet<? extends Enum<?>> enums = allOf;
		
		
		
		Iterator<? extends Enum<?>> it = enums.iterator();
		while(it.hasNext()){
			Enum<?> objEn = it.next();
			
			System.out.println(objEn.ordinal()+" - "+objEn.toString());
		}
	}

}
