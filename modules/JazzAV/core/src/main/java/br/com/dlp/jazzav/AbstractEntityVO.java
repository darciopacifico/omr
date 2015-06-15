package br.com.dlp.jazzav;
import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.jazz.codegen.annotation.JazzClass;

/**
 * Entidade abstrata do projeto, assume que todos as chaves sao Long, unicas e nao
 * naturais
 * @author t_dpacifico
 * @version 1.0
 * @created 19-mai-2010 16:53:16
 */
@MappedSuperclass
@JazzClass(name="Entidade Abstrata")
public abstract class AbstractEntityVO<P extends Serializable> extends AbstractBaseVO<P> {
	private static final long serialVersionUID = -7027111787968224962L;
	
	/**
	 * Para Obrigar e garantir o construtor vazio. Contrato JavaBeans
	 */
	public AbstractEntityVO(){
	}
	
	/**
	 * Construtor VO. Composição com IPK
	 * @param pk
	 */
	public AbstractEntityVO(final P pk){
		super(pk);
	}

}