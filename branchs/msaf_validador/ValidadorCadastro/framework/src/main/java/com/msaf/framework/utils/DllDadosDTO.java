package com.msaf.framework.utils;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;
 
@XmlType 
public class DllDadosDTO implements Serializable{
	/** 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String parametro;
	private String proxyAtivo;
	private String proxyHost;
	private String proxyUsuario;
	private String proxySenha;
	private String proxyPorta;
	private String dllPath;
	private String evServer;
	
	public static final String PROXYATIVO=   "proxyAtivo:";   
	public static final String PROXYHOST=    "proxyHost:";    
	public static final String PROXYUSUARIO=  "proxyUsuario:"; 
	public static final String PROXYSENHA=   "proxySenha:";   
	public static final String PROXYPORTA=   "proxyPorta:";   
	public static final String DLLPATH=      "dllPath:";      
	public static final String EVSERVER=     "evServer:"; 

	public DllDadosDTO() {
	}

	public DllDadosDTO(String parametro) {
		this.parametro = parametro;
	}

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	public String getProxyAtivo() {
		return proxyAtivo;
	}

	public void setProxyAtivo(String proxyAtivo) {
		this.proxyAtivo = proxyAtivo;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public String getProxyUsuario() {
		return proxyUsuario;
	}

	public void setProxyUsuario(String proxyUsuario) {
		this.proxyUsuario = proxyUsuario;
	}

	public String getProxySenha() {
		return proxySenha;
	}

	public void setProxySenha(String proxySenha) {
		this.proxySenha = proxySenha;
	}

	public String getProxyPorta() {
		return proxyPorta;
	}

	public void setProxyPorta(String proxyPorta) {
		this.proxyPorta = proxyPorta;
	}

	public String getDllPath() {
		return dllPath;
	}

	public void setDllPath(String dllPath) {
		this.dllPath = dllPath;
	}

	public String getEvServer() {
		return evServer;
	}

	public void setEvServer(String evServer) {
		this.evServer = evServer;
	}

	public static String getDLLPATH() {
		return DLLPATH;
	}
	
	
	public String getChave(){
		
		return PROXYHOST + proxyHost + PROXYPORTA  + proxyPorta;
	}
	
	
	
	
}