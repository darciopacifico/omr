package com.msaf.validador.core.lote;

public class CertificadoTO {

	public CertificadoTO(){
		
	}
	
	public CertificadoTO(String fileName, String label) {
		this.fileName = fileName;
		this.label = label;
	}

	private String fileName;
	
	private String label;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
