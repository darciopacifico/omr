package com.msaf.framework.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe que realiza a manipulacao das informacoes da dll.
 * gera um objeto com as informacoes da dll das 
 * @author vista
 *
 */
public class ExtratorParamentrosDLL {
	private String PROXYATIVO=   "proxyAtivo:";   
	private String PROXYHOST=    "proxyHost:";    
	private String PROXYUSUARIO=  "proxyUsuario:"; 
	private String PROXYSENHA=   "proxySenha:";   
	private String PROXYPORTA=   "proxyPorta:";   
	private String DLLPATH=      "dllPath:";      
	private String EVSERVER=     "evServer:";  
		
	//DllDadosDTO data = new DllDadosDTO("proxyHost:192.168.10.24,proxyAtivo:False,proxyUsuario:,proxySenha:8002,proxyPorta:,dllPath:C:\\EV Server,evServer:C:\\EV Server");


	// Map conjunto formatado de dados.
	private Map<String, DllDadosDTO> dadosDLLMap = new HashMap<String, DllDadosDTO>();
	
	
	public ExtratorParamentrosDLL(List<String> listaInfoDLL) {
		this.preencherListaInfoDLL(listaInfoDLL);

	}
	
	
	private void preencherListaInfoDLL(List<String> listaInfoDLL){
		for (String strInfoDLL : listaInfoDLL) {
			preencherDadosInfoDLL(strInfoDLL);	
		}		
	}
	
	
	private void preencherDadosInfoDLL(String dados){
	
		DllDadosDTO dadosConfDLL = new DllDadosDTO("");
		
		String [] vetorString = dados.split(",");
		
		for (int i = 0; i < vetorString.length; i++) {
			
			if (vetorString[i].contains(PROXYATIVO)) {
				dadosConfDLL.setProxyAtivo(vetorString[i].replace(PROXYATIVO, ""));
			
			} else if (vetorString[i].contains(PROXYHOST)) {
				dadosConfDLL.setProxyHost(vetorString[i].replace(PROXYHOST, ""));
			
			} else if (vetorString[i].contains(PROXYUSUARIO)) {
				dadosConfDLL.setProxyUsuario( vetorString[i].replace(PROXYUSUARIO, ""));

			} else if (vetorString[i].contains(PROXYSENHA)) {
				dadosConfDLL.setProxySenha( vetorString[i].replace(PROXYSENHA, ""));

			} else if (vetorString[i].contains(PROXYPORTA)) {
				dadosConfDLL.setProxyPorta(vetorString[i].replace(PROXYPORTA, ""));

			} else if (vetorString[i].contains(DLLPATH)) {
				dadosConfDLL.setDllPath(vetorString[i].replace(DLLPATH, ""));

			} else if (vetorString[i].contains(EVSERVER)) 
				dadosConfDLL.setEvServer( vetorString[i].replace(EVSERVER, ""));
		}
		
		// armazena em um hashmap as informacoes formatadas.
		this.dadosDLLMap.put(dadosConfDLL.getChave(), dadosConfDLL);
	}
	
	
	
	
	
	public Map<String, DllDadosDTO> getDadosDLLMap() {
		return dadosDLLMap;
	}


	public void setDadosDLLMap(Map<String, DllDadosDTO> dadosDLLMap) {
		this.dadosDLLMap = dadosDLLMap;
	}


	public static void main(String[] args){
		
		//ExtratorParamentrosDLL extrair = new ExtratorParamentrosDLL();
	}

	
	

	
}
