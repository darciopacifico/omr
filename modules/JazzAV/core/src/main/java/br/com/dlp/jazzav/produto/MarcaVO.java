package br.com.dlp.jazzav.produto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.dlp.jazzav.AbstractLogEntityVO;

/**
 * 
 * @author darcio
 *
 */
@Entity
public class MarcaVO extends AbstractLogEntityVO<Long> {

	private static final long serialVersionUID = 6715916803586893459L;

	private String nome;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

}
