package br.com.dlp.framework.vo;

import java.util.Date;

import br.com.jazz.codegen.annotation.JazzProp;
import br.com.jazz.codegen.enums.ComparisonOperator;

public interface ILoggableVO {

	@JazzProp(name = "Inclusão", tip = "Data de Inclusão.", comparison = ComparisonOperator.RANGE, readOnly = true)
	public abstract Date getDtInc();

	/**
	 * TODO IMPLEMENTAR READONLY NO GERADOR
	 * TODO descobrir qual é a anotacao que faz o hibernate gravar horas e minutos
	 *  
	 * @return
	 */
	@JazzProp(name = "Alteração", tip = "Data da Última Alteração.", comparison = ComparisonOperator.RANGE, readOnly = true)
	public abstract Date getDtAlt();

	public abstract void setDtInc(Date dtInc);

	public abstract void setDtAlt(Date dtAlt);

	public abstract String getCriadoPor();

	public abstract String getAlteradoPor();

	public abstract void setCriadoPor(String criadoPor);

	public abstract void setAlteradoPor(String alteradoPor);

}