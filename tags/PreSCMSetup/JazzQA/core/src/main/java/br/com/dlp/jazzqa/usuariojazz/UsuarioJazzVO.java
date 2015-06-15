package br.com.dlp.jazzqa.usuariojazz;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.dlp.jazzqa.status.StatusVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.annotation.JazzProp.ComparisonOperator;
import br.com.jazz.codegen.annotation.JazzProp.Renderer;

@Entity
@JazzClass(name = "Usuário")
public class UsuarioJazzVO extends AbstractBaseVO<Long> {
	private static final long serialVersionUID = 6010367305784305940L;
	
	private String login;
	private String nome;
	private Date dtInclusao;
	private double altura;
	
	private StatusVO status;
	
	private List<StatusVO> stati;
	
	@JazzProp(name = "Status")
	public StatusVO getStatus() {
		return status;
	}
	
	@JazzProp(name = "Stati")
	public List<StatusVO> getStati() {
		return stati;
	}
	
	public void setStati(List<StatusVO> stati) {
		this.stati = stati;
	}
	
	@JazzProp(name = "Status")
	public double getAltura() {
		return altura;
	}
	
	public void setAltura(double altura) {
		this.altura = altura;
	}
	
	public void setStatus(StatusVO status) {
		this.status = status;
	}
	
	@JazzProp(name = "Login")
	public String getLogin() {
		return login;
	}
	
	@JazzProp(name = "Data Inclusão", renderer = Renderer.CALENDAR, comparison = ComparisonOperator.RANGE)
	public Date getDtInclusao() {
		return dtInclusao;
	}
	
	public void setDtInclusao(Date dtInclusao) {
		this.dtInclusao = dtInclusao;
	}
	
	@JazzProp(name = "Nome", renderer = Renderer.TEXTAREA)
	public String getNome() {
		return nome;
	}
	
	public void setLogin(final String login) {
		this.login = login;
	}
	
	public void setNome(final String nome) {
		this.nome = nome;
	}
	
}
