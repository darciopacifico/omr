package br.com.dlp.framework.vo;

import java.util.Date;

import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.ComparisonOperator;

public interface ILoggableVO {

	@JazzProp(name = "Inclus�o", tip = "Data de Inclus�o.", comparison = ComparisonOperator.RANGE, readOnly = true)
	public abstract Date getDtInc();

	/**
	 * TODO IMPLEMENTAR READONLY NO GERADOR
	 * TODO descobrir qual � a anotacao que faz o hibernate gravar horas e minutos
	 *  
	 * @return
	 */
	@JazzProp(name = "Altera��o", tip = "Data da Última Altera��o.", comparison = ComparisonOperator.RANGE, readOnly = true)
	public abstract Date getDtAlt();

	public abstract void setDtInc(Date dtInc);

	public abstract void setDtAlt(Date dtAlt);

	public abstract String getCriadoPor();

	public abstract String getAlteradoPor();

	public abstract void setCriadoPor(String criadoPor);

	public abstract void setAlteradoPor(String alteradoPor);

}