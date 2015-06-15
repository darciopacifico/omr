package com.msaf.validador.consultaonline.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Implementação do serviço do Validador (WebService)
 * 
 * @author eAmaral
 * 
 */
public class ConsultaOnlineJNA {

	private IConsultaOnLineJNA consultaOnLine;

	public ConsultaOnlineJNA() {
		Native.setProtected(true);
		
		Library library = (Library) Native.loadLibrary("ConsultaOnLine", IConsultaOnLineJNA.class);
		
		this.consultaOnLine = (IConsultaOnLineJNA)Native.synchronizedLibrary(library); 
	}

	/**
	 * @param tpValidVO
	 * @param consultaXML
	 * @return string
	 */
	public String executaConsultaDLL(Integer tpValidacao, String consultaXML) {
		final String dllPath = "C:\\EV Server";
		if (isEmpty(consultaXML))
			throw new IllegalArgumentException(
					"XML de consulta não pode ser vazio/nulo!!!");
		if (null == tpValidacao)
			throw new IllegalArgumentException(
					"Tipo validacao não pode ser nulo!!!");
		
		String retonoConsultaServico = consultaOnLine.DBI_EfetuaConsultaServico(tpValidacao, consultaXML, dllPath, dllPath);
		
		
		
		
		
		return retonoConsultaServico;

	
	
	
	}

	/**
	 * Retorna se a String passada está vazia
	 * 
	 * @param texto
	 * @return
	 */
	public boolean isEmpty(final String texto) {
		return texto == null || texto.trim().length() == 0;
	}
	
	public static void main(String[] args) {
		ConsultaOnlineJNA jna = new ConsultaOnlineJNA();
		
		
		String s = jna.executaConsultaDLL(Integer.parseInt(args[0]),args[1]);
		
		
		System.out.println(s);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
