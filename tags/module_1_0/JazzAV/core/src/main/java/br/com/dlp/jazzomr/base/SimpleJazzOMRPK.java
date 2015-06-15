package br.com.dlp.jazzomr.base;

import br.com.dlp.framework.pk.AbstractPK;

/**
 * Implementação padrão de PK para o projeto JazzOMR, contanto com apenas chaves não naturais.
 * 
 * @author dpacifico
 * @version 1.0
 * @updated 19-mai-2010 16:25:31
 */
public class SimpleJazzOMRPK extends AbstractPK {
	
	private static final long serialVersionUID = 5274179729553855739L;
	
	private Long id;
	
	public SimpleJazzOMRPK() {
	}
	
	public SimpleJazzOMRPK(final Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
}
