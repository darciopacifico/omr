package br.com.dlp.jazzqa.pessoa;
import javax.persistence.Entity;

import br.com.dlp.jazzqa.base.AbstractEntityVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.PropertyStereotype;

/**
 * Cadastro de pessoas. Especializado por envolvidoVO
 * @author t_dpacifico
 * @version 1.0
 * @created 19-mai-2010 16:52:19
 *
 */
@Entity
@JazzClass(name="Pessoa")
public class PessoaVO extends AbstractEntityVO {
	
	private static final long serialVersionUID = 4200913179790608280L;
	
	private String login;
	private String email;
	private String nome;
	private String telefone;
	
	/**
	 * Construtor auto gerado
	 */
	public PessoaVO(){
	}
	
	/**
	 * Para Obrigar e garantir o construtor vazio. Contrato JavaBeans
	 */
	public PessoaVO(Long pk){
		super(pk);
	}
	
	/**
	 * @return
	 */
	@JazzProp(name="Login" ,size="30",tip="Login do usuário na rede local")
	public String getLogin() {
		return login;
	}
	
	/**
	 * 
	 * @return
	 */
	@JazzProp(name="E-mail", stereotype=PropertyStereotype.EMAIL)
	public String getEmail() {
		return email;
	}
	
	/**
	 * @return
	 */
	@JazzProp(name = "Nome Completo")
	public String getNome() {
		return nome;
	}
	
	/**
	 * @return
	 */
	@JazzProp(name = "Telefone")
	public String getTelefone() {
		return telefone;
	}
	
	
	/**
	 * @param login
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	
	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @param nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * @param ramal
	 */
	public void setTelefone(String ramal) {
		telefone = ramal;
	}
	
	
}