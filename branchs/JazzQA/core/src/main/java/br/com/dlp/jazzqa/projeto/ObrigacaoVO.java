package br.com.dlp.jazzqa.projeto;
import java.util.Date;

import javax.persistence.Entity;

import br.com.dlp.jazzqa.base.AbstractEntityVO;
import br.com.dlp.jazzqa.projeto.enums.TipoObrigacaoEnum;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;

/**
 * Obrigação relacionada a um projeto
 * @author t_dpacifico
 * @version 1.0
 * @created 19-mai-2010 16:52:18
 */
@Entity
@JazzClass(name="Obrigação",voMestre=ProjetoVO.class)
public class ObrigacaoVO extends AbstractEntityVO {
	
	private static final long serialVersionUID = -726021412560720344L;
	
	public TipoObrigacaoEnum tipoObrigacao;
	private EnvolvimentoVO responsavel;
	private String descricao;
	private Date dtPrazo;
	private Date dtEntrega;
	
	
	/**
	 * Para Obrigar e garantir o construtor vazio. Contrato JavaBeans
	 */
	public ObrigacaoVO(){
		
	}
	
	/**
	 * Construtor VO. Composição com IPK
	 * 
	 * @param pk    pk
	 */
	@SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
	public ObrigacaoVO(final Long pk){
		super(pk);
	}
	
	
	@JazzProp(name="Tipo de Obrigação")
	public TipoObrigacaoEnum getTipoObrigacao() {
		return tipoObrigacao;
	}
	
	@JazzProp(name="Responsável", tip="Responsável pelo cumprimento da obrigação do projeto.")
	public EnvolvimentoVO getResponsavel() {
		return responsavel;
	}
	
	@JazzProp(name="Descrição", tip="Descrição da obrigação.")
	public String getDescricao() {
		return descricao;
	}
	
	@JazzProp(name = "Prazo", tip="Prazo para cumprimento da obrigação.")
	public Date getDtPrazo() {
		return dtPrazo;
	}
	
	@JazzProp(name="Data Entrega", tip="Data da entrega da obrigação")
	public Date getDtEntrega() {
		return dtEntrega;
	}
	
	
	public void setResponsavel(EnvolvimentoVO responsavel) {
		this.responsavel = responsavel;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public void setDtPrazo(Date dtPrazo) {
		this.dtPrazo = dtPrazo;
	}
	
	public void setDtEntrega(Date dtEntrega) {
		this.dtEntrega = dtEntrega;
	}
	public void setTipoObrigacao(TipoObrigacaoEnum tipoObrigacao) {
		this.tipoObrigacao = tipoObrigacao;
	}
}
