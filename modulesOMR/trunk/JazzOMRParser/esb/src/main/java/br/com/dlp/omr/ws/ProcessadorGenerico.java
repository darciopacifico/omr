package br.com.dlp.omr.ws;

public class ProcessadorGenerico {

	public Object process(Object object){
		
		String string = "### processando @@@ : "+object;
		
		System.out.println(string);
		
		return string;
		
	}
	
}
