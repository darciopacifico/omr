package com.msaf.validador.pesquisa.dll;

import org.apache.log4j.Logger;

import com.msaf.validador.consultaonline.jna.IConsultaOnLineJNA;
import com.msaf.validador.consumer.util.Util;
import com.msaf.validador.pesquisa.Pesquisador;
import com.sun.jna.Native;

public class PesquisaDll implements Pesquisador {
	
	private static IConsultaOnLineJNA consultaOnLine;
	
	private static final transient Logger log = Logger.getLogger(PesquisaDll.class);
	
	private String dllPath;

	public void setDllPath(String dllPath) {
		this.dllPath = dllPath;
	}


	static {
		
		Native.setProtected(true);
		
		//Library library = (Library) Native.loadLibrary("ConsultaOnLine", IConsultaOnLineJNA.class);
		
		consultaOnLine = (IConsultaOnLineJNA) Native.loadLibrary("ConsultaOnLine", IConsultaOnLineJNA.class);//(IConsultaOnLineJNA) Native.synchronizedLibrary(library); 
	}

	
	@Override
	public void freeMemory(String id) {
		consultaOnLine.DBI_DLLStrFree(id);
	}


	@Override
	public String pesquisar(Integer service, String xmlParametro) {

		if (Util.isEmpty(xmlParametro)) {
			throw new IllegalArgumentException("XML de consulta nï¿½o pode ser vazio/nulo!!!");
		}
		
		if (null == service) {
			throw new IllegalArgumentException("Tipo validacao nï¿½o pode ser nulo!!!");
		}
		
		if(log.isDebugEnabled()) log.debug("consulta dll: " + xmlParametro);
		
		long timeInicial = System.currentTimeMillis();
		
		String retonoConsultaServico = consultaOnLine.DBI_EfetuaConsultaServico(service, xmlParametro, dllPath, dllPath);
		
		if(log.isDebugEnabled()) log.debug("Tempo de execucao da DLL " + Thread.currentThread().hashCode() + ": " + ((System.currentTimeMillis() - timeInicial) / 1000));
		
		if(log.isDebugEnabled()) log.debug("retorno dll: " + retonoConsultaServico);
		
		freeMemory(retonoConsultaServico);
		
		return retonoConsultaServico;
	}
}
