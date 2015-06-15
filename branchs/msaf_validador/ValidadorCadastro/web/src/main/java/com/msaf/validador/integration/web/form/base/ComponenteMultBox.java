package com.msaf.validador.integration.web.form.base;

import java.io.Serializable;

public class ComponenteMultBox implements Serializable{

	private static final long serialVersionUID = 5764139580060469075L;
	private String label;
	private String id;
	
	
	public ComponenteMultBox() {}
	public ComponenteMultBox(String id,String label) {
		this.id = id;
		this.label = label;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	

}
