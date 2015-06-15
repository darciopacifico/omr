package br.com.dlp.framework.vo;

public class AutenticadoVO implements IBaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5574637981009643936L;

	private String id;

	private String password;

	public String getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
