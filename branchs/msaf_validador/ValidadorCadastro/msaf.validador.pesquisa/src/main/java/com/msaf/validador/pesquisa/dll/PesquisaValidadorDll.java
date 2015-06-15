package com.msaf.validador.pesquisa.dll;

import com.msaf.validador.consultaonline.jna.IConsultaOnLineJNA;

/**
 * Classe que implementa uma consulta para o validador,<br>
 * consulta que é realizada por uma dll
 * 
 * @author ederson
 *
 */
public class PesquisaValidadorDll {

	private static final long serialVersionUID = 9128686671774826449L;

	private static IConsultaOnLineJNA dll = null;
	
	private String path;
	
	private String evServer;
	
	public String getPath() {
		return path;
	}

	/**
  	 * @param path
	 *            Variável do tipo Pchar (null terminated string) contendo o
	 *            caminho completo onde se encontram as DLLs e os arquivos
	 *            auxiliares. Ex.: C:\EvServer\DLL_OnLine
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public String getEvServer() {
		return evServer;
	}

	/**
	 * @param evServer
	 *            Variável do tipo Pchar (null terminated string) contendo o
	 *            caminho completo onde se encontra o instalado o EvServer. Ex.:
	 *            C:\EvServer 
	 */
	public void setEvServer(String evServer) {
		this.evServer = evServer;
	}

	/**
	 * @param path
	 *            Variável do tipo Pchar (null terminated string) contendo o
	 *            caminho completo onde se encontram as DLLs e os arquivos
	 *            auxiliares. Ex.: C:\EvServer\DLL_OnLine
	 * @param evServer
	 *            Variável do tipo Pchar (null terminated string) contendo o
	 *            caminho completo onde se encontra o instalado o EvServer. Ex.:
	 *            C:\EvServer 
	 */
	public PesquisaValidadorDll(String path, String evServer) {
		this.path = path;
		this.evServer = evServer;
		dll = carregarDll();
	}

	/**
	 * Faz o load da dll, preparando a pesquisa
	 * @return
	 */
	private static IConsultaOnLineJNA carregarDll() {
	
		if(dll == null) {
			
	//		//Native.setProtected(true);
	//		Library library = (Library) Native.loadLibrary("ConsultaOnLine", IConsultaOnLineJNA.class);
	//		//library = Native.synchronizedLibrary(library);
	//		
	//		IConsultaOnLineJNA consultaOnLine = (IConsultaOnLineJNA) library;
	//		
	//		return consultaOnLine;
		}
		
		
		return null;
	}
	
	/**
	 * Consulta dados de cliente pela DLL do validador
	 * 
	 * @return id de ponteiro para limpeza de memória: serve de entrada para a
	 *         operação DBI_DLLStrFree
	 * @param servico
	 *            Variável do tipo inteiro, para determinar o tipo da consulta.
	 *            1  Sintegra
	 * @param parametros
	 *            Variável do tipo Pchar (null terminated string) contendo o XML
	 *            com os parâmetros variáveis de cada consulta e as
	 *            configurações de proxy. Utilizando o formato como descrito no
	 *            exemplo para consulta de um CNPJ no estado de Santa Catarina:
	 *            <?xml version="1.0" encoding="iso-8859-1"?>
	 *            <ParametrosConsulta Versao="1.0">
	 *            <TipoConsulta>0</TipoConsulta> <Estado>SC</Estado>
	 *            <Valor>07613982000136</Valor> <Proxy> <Ativo>True</Ativo>
	 *            <Usuario/> <Senha/> <Host>192.168.10.24</Host>
	 *            <Porta>8002</Porta> </Proxy> </ParametrosConsulta>
	 */	
	public String pesquisar(Integer servico, String parametros) {
		
		try {
			
			Thread.sleep(20000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Pesqisa da DLl" + parametros;
		
		
//		return dll.DBI_EfetuaConsultaServico(servico, parametros, this.path, this.evServer);
	}

	/**
	 * Limpa área de memória utilizada pela operacao DBI_EfetuaConsultaServico.
	 * Deve ser chamada após cada chamada da operação DBI_EfetuaConsultaServico
	 * 
	 * @param memId
	 *            endereço de memória: ver retorno da operação
	 *            DBI_EfetuaConsultaServico
	 * 
	 */
	public void freeMemory(String id) {
		dll.DBI_DLLStrFree(id);
	}

	
}
