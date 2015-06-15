/**
 * 
 */
package br.com.dlp.jazzomr.event;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author darcio
 *
 */
@Scope(value = "session")
@Component
public class UploadJSFBean  {
	private static final long serialVersionUID = -12341253425763l;

	private List<UploadedFile> files = new ArrayList<UploadedFile>(10);
	
	private List<File> uploadedFiles = new ArrayList<File>(10);
	
	
	/**
	 * Construtor padrao
	 */
	public UploadJSFBean(){}
	
	
  public void handleFileUpload(FileUploadEvent event) {  
    UploadedFile file = event.getFile();
    
		files.add(file);
    FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");  
    FacesContext.getCurrentInstance().addMessage(null, msg);  
  }  
  
  
	/*
  public void richFileUpload(UploadEvent event) {
  	System.out.println("");
  }*/


	public List<File> getUploadedFiles() {
		return uploadedFiles;
	}


	public void setUploadedFiles(List<File> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}  
  
}
