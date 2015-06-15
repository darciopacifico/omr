package br.com.dlp.jazzav.anuncio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import br.com.dlp.jazzav.AbstractEntityVO;
import br.com.jazz.codegen.annotation.JazzProp;

/**
 * Armazena arquivo. Uso generico
 * @author darcio
 *
 */
@Entity
public class FileVO extends AbstractEntityVO<Long> {

	private static final long serialVersionUID = 912680199583198243L;

	@Id
	@JazzProp(name="PK" ,tip="Chave de pessoa")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}
	
	public byte[] lob;

	/**
	 * Conteudo do arquivo propriamente
	 * @return
	 */
	@Lob
	public byte[] getLob() {
		return lob;
	}

	public void setLob(byte[] lob) {
		this.lob = lob;
	}
	
	
	
	
}
