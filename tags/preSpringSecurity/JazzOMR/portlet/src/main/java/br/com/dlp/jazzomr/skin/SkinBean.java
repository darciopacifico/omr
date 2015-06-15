package br.com.dlp.jazzomr.skin;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope(value="session")
@Component
public class SkinBean {
	
	
	private String skin="DEFAULT";
	
	
	public String getSkin() {
		
		return skin;
		
	}
	
	public void setSkin(String skin) {
		
		this.skin = skin;
		
	}
	
}