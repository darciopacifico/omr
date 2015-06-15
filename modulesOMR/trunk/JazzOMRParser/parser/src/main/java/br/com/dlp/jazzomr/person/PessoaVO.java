package br.com.dlp.jazzomr.person;
import javax.xml.bind.annotation.XmlTransient;

import br.com.dlp.jazzomr.AbstractBaseVO;

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
	public String getMsgCriticLogin() {
		return msgCriticLogin;
	}
	
	/**
	 * @return
	 */
	public String getNome() {
		return nome;
	}
	
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