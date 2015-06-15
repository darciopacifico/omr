package br.com.dlp.jazz.svnhook.vos;

import java.io.Serializable;
import java.util.Date;

/**
 * Critica à citacaoIssueVO
 * 
 * @author t_dpacifico
 * 
 */
public class CriticVO implements Serializable {

	private static final long serialVersionUID = -1566212115952888688L;

	public enum SEVERIDADE {
		IMPEDITIVO, RISCO, INFORMATIVO, NENHUMA
	}

	public enum TIPO_CRITICA {
		ISSUE_NAO_ENCONTRADO, ISSUE_DE_OUTRO_PROJETO, ISSUE_FECHADA, ESFORCO_NAO_CITADO, MUDANCAS_NAO_RELACIONADAS_AO_TICKET, ISSUE_RELACIONADA_AS_MUDANCAS
	}

	private String descricao;
	private Date dtCritica;
	private SEVERIDADE severidade;
	private TIPO_CRITICA tipoCritica;

	/**
	 * Construtor padrao
	 */
	public CriticVO() {
		this.dtCritica = new Date();
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @return the dtCritica
	 */
	public Date getDtCritica() {
		return dtCritica;
	}

	/**
	 * @param descricao
	 *          the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @param dtCritica
	 *          the dtCritica to set
	 */
	public void setDtCritica(Date dtCritica) {
		this.dtCritica = dtCritica;
	}

	/**
	 * @return the severidade
	 */
	public SEVERIDADE getSeveridade() {
		return severidade;
	}

	/**
	 * @param severidade
	 *          the severidade to set
	 */
	public void setSeveridade(SEVERIDADE severidade) {
		this.severidade = severidade;
	}

	/**
	 * @return the tipoCritica
	 */
	public TIPO_CRITICA getTipoCritica() {
		return tipoCritica;
	}

	/**
	 * @param tipoCritica
	 *          the tipoCritica to set
	 */
	public void setTipoCritica(TIPO_CRITICA tipoCritica) {
		this.tipoCritica = tipoCritica;
	}

	@Override
	public String toString() {
		return this.getTipoCritica() + " @@" + this.getSeveridade() + " " + this.descricao;
	}

}
