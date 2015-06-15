package br.com.dlp.framework.vo;

import java.util.Date;

public class DialogMessageVO implements IBaseVO {
	public static final int PRIORIDADE_DEBUG = 0;

	public static final int PRIORIDADE_INFO = 1;

	public static final int PRIORIDADE_WARN = 2;

	public static final int PRIORIDADE_ERROR = 3;

	public static final int PRIORIDADE_DEFAULT = PRIORIDADE_DEBUG;

	private static final long serialVersionUID = -2834473076505100233L;

	private String mensagem = "";

	private String mensagemKey = "";

	private Date date = new Date();

	private int prioridade = PRIORIDADE_DEFAULT;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}

	public String getMensagemKey() {
		return mensagemKey;
	}

	public void setMensagemKey(String mensagemKey) {
		this.mensagemKey = mensagemKey;
	}

}
