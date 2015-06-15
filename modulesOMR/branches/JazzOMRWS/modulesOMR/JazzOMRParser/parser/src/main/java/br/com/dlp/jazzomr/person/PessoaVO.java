package br.com.dlp.jazzomr.person;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotBlank;

import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;

/**
 * Cadastro de pessoas. Especializado por envolvidoVO
 * @author t_dpacifico
 * @version 1.0
 * @created 19-mai-2010 16:52:19
 *
 */
public class PessoaVO extends AbstractBaseVO<Long>  {
	
	private static final long serialVersionUID = 4200913179790608280L;

	private String msgCriticLogin="";

	private String nome;
	
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

	
	public PessoaVO(Long pk,String nome){
		super(pk);
		setNome(nome);
	}
	
	
	
	@XmlTransient
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
	
	
	@Id
	@JazzProp(name="PK" ,tip="Chave de pessoa")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
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


	@Override
	public void setPK(Long ppk) {
		super.setPK(ppk);
	}


	
}