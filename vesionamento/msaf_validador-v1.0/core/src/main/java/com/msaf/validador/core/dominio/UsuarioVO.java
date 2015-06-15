package com.msaf.validador.core.dominio;

import java.io.File;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.msaf.validador.consultaonline.cliente.ClienteVO;

@Entity
public class UsuarioVO implements Serializable {

	private static final long serialVersionUID = -2871564128740316310L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "PK_USU")
    @SequenceGenerator(name="PK_USU", sequenceName="SEQUENCE_USUARIO", allocationSize=1)
	@Column(name="ID_Usuario")
	private Long idUsuario;

	@Column
	private String login;

	@Column
	private String senha;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "CLIENTE_FK")}) 
	private ClienteVO idCliente;
	
	@Transient
	File fileParaProcessar;
	
	public ClienteVO getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(ClienteVO clienteVO) {
		this.idCliente = clienteVO;
	}

	
	

	public UsuarioVO() {}
	public UsuarioVO(String login, String senha) {
		this.login = login;
		this.senha = senha;
	}

	public UsuarioVO(long idUsuario, String login, String senha, ClienteVO idCliente) {
		this.idUsuario = idUsuario;
		this.login = login;
		this.senha = senha;
		this.idCliente = idCliente;
	}

	public File getFileParaProcessar() {
		return fileParaProcessar;
	}

	public void setFileParaProcessar(File file) {
		this.fileParaProcessar = file;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}


	
	

}
