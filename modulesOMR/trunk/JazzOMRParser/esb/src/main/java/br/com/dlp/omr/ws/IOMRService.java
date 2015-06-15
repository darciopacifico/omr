package br.com.dlp.omr.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface IOMRService {

	@WebMethod
	public abstract String gerarProva(String nome);

}