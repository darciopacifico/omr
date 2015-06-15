package br.com.dlp.framework.mdb.pool;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MDBMensagemVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5923969049942905755L;

	private String usuarioId; // id do usuario

	private String batchId; // id do processamento

	private Date batchDtSolic; // data da solicitacao

	private String batchDesc; // descricao do processamento

	private String batchTipo; // tipo do processamento

	private String batchConsumidor; // classe que executa o processo

	private String mensagem; // mensagem de avisos diversos

	private Map parametros; // parametros para a execucao do processo

	// construtores
	public MDBMensagemVO() {
		this.parametros = new HashMap();
	}

	public MDBMensagemVO(Map hashMap) {
		this.parametros = hashMap;
	}

	// manipulacao dos parametros da aplicacao
	public boolean containsParameter(Object parameter) {
		if (parametros.containsKey(parameter)) {
			return true;
		}

		return false;
	}

	public void setParametro(Object key, Object value) {
		parametros.put(key, value);
	}

	public void removeParametro(Object key) {
		parametros.remove(key);
	}

	public Object getParametro(Object key) {
		return parametros.get(key);
	}

	public void setParametros(Hashtable parametros) {
		this.parametros = parametros;
	}

	public Map getParametros() {
		return parametros;
	}

	// manipulacao dos parametros para a fila
	public String getBatchConsumidor() {
		return batchConsumidor;
	}

	public String getBatchDesc() {
		return batchDesc;
	}

	public String getBatchId() {
		return batchId;
	}

	public Date getBatchSolicitacao() {
		return batchDtSolic;
	}

	public String getBatchTipo() {
		return batchTipo;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setBatchConsumidor(String batchConsumidor) {
		this.batchConsumidor = batchConsumidor;
	}

	public void setBatchDesc(String batchDesc) {
		this.batchDesc = batchDesc;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public void setBatchSolicitacao(Date batchDtSolicitacao) {
		this.batchDtSolic = batchDtSolicitacao;
	}

	public void setBatchTipo(String batchTipo) {
		this.batchTipo = batchTipo;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}
