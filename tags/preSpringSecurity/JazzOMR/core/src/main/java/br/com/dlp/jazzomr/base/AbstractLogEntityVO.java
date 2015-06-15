package br.com.dlp.jazzomr.base;
import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import br.com.jazz.codegen.annotation.JazzClass;
import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.ComparisonOperator;

/**
 * Especializacao de AbstractEntityVO, com caracteristicas de logging de registros
 * naturais
 * @author t_dpacifico
 * @version 1.0
 * @created 19-mai-2010 16:53:16
 */
@MappedSuperclass
@JazzClass(name="Entidade Abstrata")
public abstract class AbstractLogEntityVO extends AbstractEntityVO {
	
	private static final long serialVersionUID = -7027111787968224962L;
	
	private Date dtInc=new Date();
	private Date dtAlt=new Date();
	private StatusEnum status=StatusEnum.ACTIVE;
	
	/**
	 * Para Obrigar e garantir o construtor vazio. Contrato JavaBeans
	 */
	public AbstractLogEntityVO(){
	}
	
	/**
	 * Construtor VO. Composição com IPK
	 * 
	 * @param pk    pk
	 */
	@SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
	public AbstractLogEntityVO(final Long pk){
		super(pk);
	}
	
	@JazzProp(name="Inclusão", tip="Data de Inclusão.",comparison=ComparisonOperator.RANGE, readOnly=true)
	public Date getDtInc() {
		return dtInc;
	}
	
	/**
	 * TODO IMPLEMENTAR READONLY NO GERADOR
	 * TODO descobrir qual é a anotacao que faz o hibernate gravar horas e minutos
	 *  
	 * @return
	 */
	@JazzProp(name="Alteração", tip="Data da Última Alteração.",comparison=ComparisonOperator.RANGE, readOnly=true)
	public Date getDtAlt() {
		return dtAlt;
	}
	
	@JazzProp(name="Status", tip="Status do Registro.")
	@Enumerated(EnumType.ORDINAL)
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