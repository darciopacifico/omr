package br.com.dlp.jazzqa.projeto;

import java.util.Date;

import javax.persistence.Entity;

import br.com.dlp.jazzqa.base.AbstractEntityVO;
import br.com.dlp.jazzqa.projeto.enums.TipoHistoricoEnum;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.JazzRenderer;


/**
 * Histórico
 * @author t_dpacifico
 * @version 1.0
 * @created 19-mai-2010 16:52:17
 */
@Entity
@JazzClass(name="Histórico de Projeto",voMestre=ProjetoVO.class)
public class HistoricoVO extends AbstractEntityVO {
	
	private static final long serialVersionUID = -4212609245952090609L;
	private Date data;
	private String descricao;
	private TipoHistoricoEnum tipoHistorico;
	
	
	@JazzProp(name="Tipo de Histórico",renderer=JazzRenderer.COMBO, tip="Tipo do evento.")
	public TipoHistoricoEnum getTipoHistorico() {
		return tipoHistorico;
	}
	
	@JazzProp(name = "Data", tip="Data do evento.")
	public java.util.Date getData() {
		return data;
	}
	
	
	@JazzProp(name="Descrição", tip="Descrição do evento")
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setTipoHistorico(TipoHistoricoEnum tipoHistorico) {
		this.tipoHistorico = tipoHistorico;
	}
	
	
	public void setData(java.util.Date data) {
		this.data = data;
	}
	
	/**
	 * Para Obrigar e garantir o construtor vazio. Contrato JavaBeans
	 */
	public HistoricoVO(){
	}
	
	/**
	 * Construtor VO. Composição com IPK
	 * 
	 * @param pk    pk
	 */
	@SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
	public HistoricoVO(final Long pk){
		super(pk);
	}
	
}