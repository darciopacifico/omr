package br.com.dlp.omr.ws.vo;

import java.io.Serializable;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author darciopa
 */
@XmlType
@XmlRootElement
public class GenerationReturnVO implements Serializable{

	private static final long serialVersionUID = -4629491756754317219L;

	private DataHandler provaFile; 
	private DataHandler arquivoGabarito; 
	
	
	
	@XmlMimeType("application/zip")
	public DataHandler getArquivoGabarito() {
		return arquivoGabarito;
	}
	
	@XmlMimeType("application/zip")
	public DataHandler getProvaFile() {
		return provaFile;
	}
	
	public void setArquivoGabarito(DataHandler dataHandlerGabarito) {
		this.arquivoGabarito = dataHandlerGabarito;
	}
	
	public void setProvaFile(DataHandler dataHandlerProva) {
		this.provaFile = dataHandlerProva;
	}

}
