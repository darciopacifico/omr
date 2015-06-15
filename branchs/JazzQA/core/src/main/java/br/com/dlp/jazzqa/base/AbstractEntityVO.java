package br.com.dlp.jazzqa.base;
import java.util.Date;

import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.dlp.jazzqa.projeto.enums.StatusEnum;
import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;

/**
 * Entidade abstrata do projeto, assume que todos as chaves s�o Long, �nicas e n�o
 * naturais
 * @author t_dpacifico
 * @version 1.0
 * @created 19-mai-2010 16:53:16
 */
@JazzClass(name="Entidade Abstrata")
public abstract class AbstractEntityVO extends AbstractBaseVO<Long> {
	
	private static final long serialVersionUID = -7027111787968224962L;
	
	private Date dtInc;
	private Date dtAlt;
	private StatusEnum status;
	
	/**
	 * Para Obrigar e garantir o construtor vazio. Contrato JavaBeans
	 */
	public AbstractEntityVO(){
	}
	
	/**
	 * Construtor VO. Composição com IPK
	 * 
	 * @param pk    pk
	 */
	@SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
	public AbstractEntityVO(final Long pk){
		super(pk);
	}
	
	@JazzProp(name="Inclusão", tip="Data de Inclusão.")
	public Date getDtInc() {
		return dtInc;
	}
	@JazzProp(name="Alteração", tip="Data da Última Alteração.")
	public Date getDtAlt() {
		return dtAlt;
	}
	
	@JazzProp(name="Status", tip="Status do Registro.")
	public StatusEnum getStatus() {
		return status;
	}
	
	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	
	public void setDtInc(Date dtInc) {
		this.dtInc = dtInc;
	}
	
	public void setDtAlt(Date dtAlt) {
		this.dtAlt = dtAlt;
	}
	
	
}