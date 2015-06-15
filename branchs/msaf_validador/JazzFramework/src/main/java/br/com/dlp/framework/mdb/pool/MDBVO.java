package br.com.dlp.framework.mdb.pool;

import br.com.dlp.framework.vo.IBaseVO;

public class MDBVO implements IBaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1013079287705013379L;

	private String usuarioId; // id do usuario

	private String Id; // id do processamento

	private long dtSolic; // data da solicitacao

	private String Desc; // descricao do processamento

	private String tipo; // tipo do processamento

	private String status; // status de execucao

	private long dtExec; // data da execucao

	private String mensagem; // mensagem de avisos diversos

	// construtores
	public MDBVO() {
	}

	public String getDesc() {
		return Desc;
	}

	public long getDtExec() {
		return dtExec;
	}

	public long getDtSolic() {
		return dtSolic;
	}

	public String getId() {
		return Id;
	}

	public String getMensagem() {
		return mensagem;
	}

	public String getStatus() {
		return status;
	}

	public String getTipo() {
		return tipo;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setDesc(String desc) {
		Desc = desc;
	}

	public void setDtExec(long dtExec) {
		this.dtExec = dtExec;
	}

	public void setDtSolic(long dtSolic) {
		this.dtSolic = dtSolic;
	}

	public void setId(String id) {
		Id = id;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}
}
