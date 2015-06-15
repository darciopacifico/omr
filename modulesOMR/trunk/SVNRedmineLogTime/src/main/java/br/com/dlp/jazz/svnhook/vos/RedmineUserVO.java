package br.com.dlp.jazz.svnhook.vos;

import java.io.Serializable;

/**
 * 
 * @author t_dpacifico
 *
 */
public class RedmineUserVO implements Serializable {
	
	private static final long serialVersionUID = 9126774148743758148L;
	
	private Long id;
	private String login;
	private Integer status;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	

	@Override
	/**
	 * printa id e login o usuario
	 */
	public String toString() {
		return this.id+" "+this.login;
	}
}
