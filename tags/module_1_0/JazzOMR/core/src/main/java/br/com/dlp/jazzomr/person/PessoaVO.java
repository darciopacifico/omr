package br.com.dlp.jazzomr.person;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.AssertTrue;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.dlp.jazzomr.base.AbstractBelongsOrgVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.PropertyStereotype;

import com.google.gson.annotations.Expose;

/**
 * Cadastro de pessoas. Especializado por envolvidoVO
 * @author t_dpacifico
 * @version 1.0
 * @created 19-mai-2010 16:52:19
 *
 */
@Entity
@JazzClass(name="Pessoa")
@Table(
		uniqueConstraints = @UniqueConstraint(columnNames = { "login" })
		)
public class PessoaVO extends AbstractBelongsOrgVO<Long>  {
	
	private static final long serialVersionUID = 4200913179790608280L;

	private String msgCriticLogin="";
	
	private String senha;
	private String senhaConfirm;

	private Boolean novo=false;

	private String nome;
	private String email;
	private String telefone;
	
	private String login;
	
	@Expose(deserialize=false,serialize=false)
	private List<EAuthority> authorities = new ArrayList<EAuthority>(4);
	
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
	
	
	@NotBlank(message="O login deve ser informado!", groups=VGroupUsuario.class)
	public String getLogin() {
		return login;
	}

	
	@ElementCollection(targetClass=EAuthority.class,fetch=FetchType.EAGER)
	@JoinTable(name = "pessoa_autorities")
	@Enumerated(EnumType.STRING)
	@Fetch(FetchMode.SELECT)
	public List<EAuthority> getAuthorities() {
		return authorities;
	}

	/**
	 * 
	 * @return
	 */
	@NotBlank(message="Email não pode ser branco",groups=VGroupUsuario.class)
	@Email(message="E-mail inválido",groups=VGroupUsuario.class)
	@JazzProp(name="E-mail", stereotype=PropertyStereotype.EMAIL)
	public String getEmail() { 
		return email;
	}
	
	
	@Transient
	public String getMsgCriticLogin() {
		return msgCriticLogin;
	}
	
	/**
	 * @return
	 */
	@NotBlank(message="Nome não pode ficar em branco")
	@JazzProp(name = "Nome Completo")
	@Index(name="idxNome")
	public String getNome() {
		return nome;
	}
	
	@Transient
	public Boolean getNovo() {
		return novo;
	}
	
	@Id
	@JazzProp(name="PK" ,tip="Chave de pessoa")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}
	
	@NotEmpty(groups=VGroupUsuario.class, message="Senha não pode ficar em branco!")
	@Length(min=7, max=25, groups=VGroupUsuario.class, message="Senha deve possuir entre 7 e 25 caracteres!")
	public String getSenha() {
		return senha;
	}
	
	@NotEmpty(groups=VGroupUsuario.class, message="Confirmação de Senha não pode ficar em branco!")
	@Length(min=7, max=25, groups=VGroupUsuario.class, message="Confirmação de Senha deve possuir entre 7 e 25 caracteres!")
	public String getSenhaConfirm() {
		if(senhaConfirm==null){
			return senha;
		}
		
		return senhaConfirm;
	}

	/**
	 * @return
	 */
	@JazzProp(name = "Telefone")
	@Length(max=30)
	public String getTelefone() {
		return telefone;
	}

	@Transient
  @AssertTrue(message="A confirmação de senha não coincide com a senha digitada!", groups=VGroupUsuario.class)
  public boolean isEqualsSenha(){
		
		
		return StringUtils.isNotBlank(senha) && StringUtils.isNotBlank(senhaConfirm) && senha.equals(senhaConfirm);
  }

	@Transient
  @AssertTrue(message="Por favor, informe uma senha com mais de 6 caracteres.", groups=VGroupUsuario.class)
  public boolean isValidSenha(){
		return true;
  }

	public void setAuthorities(List<EAuthority> authorities) {
		this.authorities = authorities;
	}

	
	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public void setMsgCriticLogin(String msgCriticLogin) {
		this.msgCriticLogin = msgCriticLogin;
	}

	
	/**
	 * @param nome
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setNovo(Boolean novo) {
		this.novo = novo;
	}

	@Override
	public void setPK(Long ppk) {
		super.setPK(ppk);
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setSenhaConfirm(String senhaConfirm) {
		this.senhaConfirm = senhaConfirm;
	}

	/**
	 * @param ramal
	 */
	public void setTelefone(String ramal) {
		telefone = ramal;
	}

	public void setLogin(String login) {
		if(StringUtils.isBlank(login)){
			this.login = null;
		}else{
			this.login = login;
		}
	}
	
}