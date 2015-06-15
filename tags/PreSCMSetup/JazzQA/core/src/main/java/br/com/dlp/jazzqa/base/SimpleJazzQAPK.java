package br.com.dlp.jazzqa.base;

import br.com.dlp.framework.pk.AbstractPK;

/**
 * Implementação padrão de PK para o projeto JazzQA, contanto com apenas chaves não naturais.
 * 
 * @author dpacifico
 * 
 */
public class SimpleJazzQAPK extends AbstractPK {
	
	private static final long serialVersionUID = 5274179729553855739L;
	
	private Long id;
	
	public SimpleJazzQAPK() {
	}
	
	public SimpleJazzQAPK(final Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
}
