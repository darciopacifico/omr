package br.com.dlp.jazzav;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import br.com.dlp.framework.vo.ILoggableVO;
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
public abstract class AbstractLogEntityVO<P extends Serializable>  extends AbstractEntityVO<P> implements ILoggableVO {
	
	private static final long serialVersionUID = -7027111787968224962L;
	
	private Date dtInc=new Date();
	private Date dtAlt=new Date();
	
	private String criadoPor;
	private String alteradoPor; 
	
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
	public AbstractLogEntityVO(final P pk){
		super(pk);
	}
	
	/* (non-Javadoc)
	 * @see br.com.dlp.jazzomr.base.IBaseLogVO#getDtInc()
	 */
	@Override
	@JazzProp(name="Inclusão", tip="Data de Inclusão.",comparison=ComparisonOperator.RANGE, readOnly=true)
	public Date getDtInc() {
		return dtInc;
	}
	
	/* (non-Javadoc)
	 * @see br.com.dlp.jazzomr.base.IBaseLogVO#getDtAlt()
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see br.com.dlp.jazzomr.base.IBaseLogVO#setDtInc(java.util.Date)
	 */
	@Override
	public void setDtInc(Date dtInc) {
		this.dtInc = dtInc;
	}
	
	/* (non-Javadoc)
	 * @see br.com.dlp.jazzomr.base.IBaseLogVO#setDtAlt(java.util.Date)
	 */
	@Override
	public void setDtAlt(Date dtAlt) {
		this.dtAlt = dtAlt;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.jazzomr.base.IBaseLogVO#getCriadoPor()
	 */
	@Override
	public String getCriadoPor() {
		return criadoPor;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.jazzomr.base.IBaseLogVO#getAlteradoPor()
	 */
	@Override
	public String getAlteradoPor() {
		return alteradoPor;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.jazzomr.base.IBaseLogVO#setCriadoPor(java.lang.String)
	 */
	@Override
	public void setCriadoPor(String criadoPor) {
		this.criadoPor = criadoPor;
	}

	/* (non-Javadoc)
	 * @see br.com.dlp.jazzomr.base.IBaseLogVO#setAlteradoPor(java.lang.String)
	 */
	@Override
	public void setAlteradoPor(String alteradoPor) {
		this.alteradoPor = alteradoPor;
	}

}