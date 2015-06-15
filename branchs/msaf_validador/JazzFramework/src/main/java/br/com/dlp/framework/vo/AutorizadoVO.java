package br.com.dlp.framework.vo;

public class AutorizadoVO implements IBaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8746558264829945342L;

	private String codigo;

	private String nome;

	private String url;

	private String tipo;

	public String getCodigo() {
		return codigo;
	}

	public String getNome() {
		return nome;
	}

	public String getTipo() {
		return tipo;
	}

	public String getUrl() {
		return url;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
