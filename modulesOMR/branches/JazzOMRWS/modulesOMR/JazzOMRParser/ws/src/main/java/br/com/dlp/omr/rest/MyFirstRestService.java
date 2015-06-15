package br.com.dlp.omr.rest;

import java.io.Serializable;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import br.com.dlp.omr.ws.payload.GenerationPayloadVO;

@Component
@Path(value = "/firstRest")
@ApplicationPath(value = "firstRest")
public class MyFirstRestService implements Serializable {

	
	@POST
	public String helloRest(GenerationPayloadVO payLoad){
		System.out.println(payLoad);
		
		return "Olá "+payLoad+". Parabens pelo seu primeiro rest!";
	}
	
}
