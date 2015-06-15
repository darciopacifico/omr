package br.com.dlp.jazzav.person;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.MatchesPattern;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
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
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.ScriptAssert;
import org.jboss.logging.Field;
import org.springframework.validation.annotation.Validated;

import br.com.dlp.jazzav.AbstractLogEntityVO;
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
		uniqueConstraints ={
				@UniqueConstraint(columnNames = { "email" })
		}
		)
@ScriptAssert(lang="javascript",script="_this.senha==_this.senhaConfirm",message="A confirmação de senha não confere!")

public class PessoaVO extends AbstractLogEntityVO<Long>  {
	
	protected static final String COM_ABREVIADO = "c/ ";

	public static final String REGEX_ENTRE_PARENTESES = "\\((([\\d]*))\\)(.*)";

	public static final Pattern pattern = Pattern.compile(REGEX_ENTRE_PARENTESES);

	private static final long serialVersionUID = 4200913179790608280L;

	private String msgCriticLogin="";
	
	private String email;
	private String nome="";
	private String senha;
	private String senhaConfirm;
	
	private String resetToken;

	private Boolean novo=false;

	private String ddd1="";
	private String telefone1="";

	private String ddd2="";
	private String telefone2="";
	
	
	
	@Expose(deserialize=false,serialize=false)
	private List<EAuthority> authorities = new ArrayList<EAuthority>(4);
	
	
	private EnderecoVO enderecoVO = new EnderecoVO();
	
	
	/**
	 * Construtor auto gerado
	 */
	public PessoaVO(){
	}
	
	
	/**
	 * Formata nome com o prefixo "c/"
	 */
	@Transient
	public String getNomeF() {
		return COM_ABREVIADO +nome;
	}
	

	
	
	/**
	 * Para Obrigar e garantir o construtor vazio. Contrato JavaBeans
	 */
	public PessoaVO(Long pk){
		super(pk);
	}
	
	
	/**
	 * 
	 * @return
	 */
	//@NotBlank(message="Email não pode ser branco",groups=VGroupUsuario.class)
	//@Email(message="E-mail inválido",groups=VGroupUsuario.class)
	@JazzProp(name="E-mail", stereotype=PropertyStereotype.EMAIL)
	@Email(message="Email inválido!")
	@NotBlank(message="Email não pode ficar em branco")
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
	//@NotBlank(message="Nome não pode ficar em branco")
	@JazzProp(name = "Nome Completo")
	@Index(name="idxNome")
	@Length(min=10, max=80)
	public String getNome() {
		return nome;
	}
	
	@Transient
	@javax.validation.constraints.AssertTrue(message="O nome completo deve possuir pelo menos um sobrenome!")
	public boolean getNameValidation(){
		
		if(StringUtils.isNotBlank(this.nome)){
		
			int length = this.nome.trim().split("\\s").length;
			System.out.println(length);
			return length>1;
			
		}else{
			return true;
		}
		
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
	@Length(min=7, max=25, message="Senha deve possuir entre 7 e 25 caracteres!")
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
	
	//@Length(min=7, max=9)
	
	public String getTelefone1() {
		return telefone1;
	}

	/**
	 * @return
	 */
	public String getTelefone2() {
		return telefone2;
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
	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}


	@ElementCollection(targetClass=EAuthority.class,fetch=FetchType.EAGER)
	@JoinTable
	@Enumerated(EnumType.STRING)
	@Fetch(FetchMode.SELECT)
	public List<EAuthority> getAuthorities() {
		return authorities;
	}


	public void setAuthorities(List<EAuthority> authorities) {
		this.authorities = authorities;
	}


	public String getResetToken() {
		return resetToken;
	}


	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	@Embedded
	public EnderecoVO getEnderecoVO() {
		return enderecoVO;
	}


	public void setEnderecoVO(EnderecoVO enderecoVO) {
		this.enderecoVO = enderecoVO;
	}


	
	public String getDdd1() {
		return ddd1;
	}


	public String getDdd2() {
		return ddd2;
	}


	public void setDdd1(String ddd1) {
		this.ddd1 = ddd1;
	}


	public void setDdd2(String ddd2) {
		this.ddd2 = ddd2;
	}

	
	
	
	
	
	
	
	
	
	public void parseDDDTelefone(String telefoneComDDD) {
		
		
	 	String strContato = telefoneComDDD;
	 	
	 	if(StringUtils.isNotBlank(strContato)){
	 		
	 		String[] telefones = strContato.split("\n");
	 		
	 		if(telefones.length>0){
	 			
	 			String telefone1 = telefones[0];
				
	 			
	 			if(StringUtils.isNotBlank(telefone1)){
				
	 				String ddd1 = separaDDD(telefone1);
	 				String tel1 = separaTel(telefone1);
	 				
	 				setDdd1(ddd1);
	 				setTelefone1(tel1);
	 				
				}
	
	 		}
	 		

	 		if(telefones.length>1){
	 			
	 			String telefone2 = telefones[1];
				
				if(StringUtils.isNotBlank(telefone2)){
					
					
	 				String ddd2 = separaDDD(telefone2);
	 				String tel2 = separaTel(telefone2);
	 				
	 				setDdd2(ddd2);
	 				setTelefone2(tel2);
					
				}
	 		}
	 	}
		
	}


	/**
	 * 
	 * 
	 * @param dddTel
	 * @return
	 */
	protected String separaTel(String dddTel) {
		if(StringUtils.isBlank(dddTel)){
			return "";
		}
		
		Matcher matcher = pattern.matcher(dddTel);
		
		if(matcher.find()){
			dddTel = matcher.group(3);
		}
		
		if(StringUtils.isNotBlank(dddTel)){
			dddTel = dddTel.replaceAll("[\\D]", "");
		}
		
		return dddTel;
	}

	
	/**
	 * 
	 * 
	 * @param dddTel
	 * @return
	 */
	protected String separaDDD(String dddTel) {
		
		if(StringUtils.isBlank(dddTel)){
			return "";
		}
		
		String ddd="";

		Matcher matcher = pattern.matcher(dddTel);
		
		if(matcher.find()){
			ddd=matcher.group(1);
			if(StringUtils.isNotBlank(ddd)  && ddd.length()>=3){
				ddd = ddd.substring(ddd.length()-2);
			}
		}
		return ddd;
	}


	/**
	 * Simplesmente testa se a pessoa possui algum telefone registrado.
	 * 
	 * Utilizado para evitar o preenchimento automatico de telefone
	 * @return
	 */
	@Transient
	public boolean isPossuiTelefone() {

		return StringUtils.isNotBlank(getTelefone1());
	}
	
	
	
	
}