package br.com.mastersaf.controller;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.mastersaf.model.Usuario;
import br.com.mastersaf.util.GenericBo;

public class LoginController {

	private GenericBo genericBo;
	private String nmUsuario;
	private String nmSenha;
	private Usuario usuario;
	private String errorMessage;
	
	private FacesContext facesContext;
	private HttpSession session;

	
	public void setGenericBo(GenericBo genericBo) {
		this.genericBo = genericBo;
	}
	
	
	public String logar(){
		String statusRetorno = "erro";
		usuario = new Usuario();
		usuario.setNomeUsuario(this.nmUsuario);
		usuario.setSenha(this.nmSenha);
		try{
			statusRetorno = "sucess";
			this.setErrorMessage("");
			this.registraUsuario();
			/*
			if(this.genericBo.get(usuario).size() == 0){
				this.setErrorMessage("Nome do usuario e/ou senha invalidos!!");
			}else{
			}*/
		}catch(Exception e){
			this.setErrorMessage("Nome do usuario e/ou senha invalidos!!");
			e.printStackTrace();
		}
		return statusRetorno;
	}
	
	
	private void registraUsuario(){
		 facesContext = FacesContext.getCurrentInstance();  
		 session = (HttpSession)facesContext.getExternalContext().getSession(false);  
		 session.setAttribute("userName", usuario.getNomeUsuario());
	}
	
	public String logout(){
		String statusRetorno = "logout";
		clearValues();
		return statusRetorno;
	}
	
	private void clearValues(){
		facesContext = FacesContext.getCurrentInstance();  
		session = (HttpSession)facesContext.getExternalContext().getSession(false);  
		this.usuario = null;
		this.nmSenha = null;
		this.nmUsuario = null;
		try{
			session.removeAttribute("userName");
		}catch(Exception e){}
	}
	
	/** 
	 * GETTERS AND SETTERS
	 */

	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}
	public String getNmUsuario() {
		return nmUsuario;
	}
	public void setNmSenha(String nmSenha) {
		this.nmSenha = nmSenha;
	}
	public String getNmSenha() {
		return nmSenha;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
}
