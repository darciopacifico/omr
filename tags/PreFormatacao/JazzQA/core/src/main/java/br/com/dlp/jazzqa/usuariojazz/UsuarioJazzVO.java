package br.com.dlp.jazzqa.usuariojazz;

import javax.persistence.Entity;

import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;

@Entity
@JazzClass(name = "Usuário")
public class UsuarioJazzVO extends AbstractBaseVO<Long> {
	private static final long serialVersionUID = 6010367305784305940L;

	private String nome;
	private String login;

	@JazzProp(name="Nome")
	public String getNome() {
		return nome;
	}

	@JazzProp(name="Login")
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
