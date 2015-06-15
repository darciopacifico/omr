package br.com.dlp.jazzav.produto;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import br.com.dlp.jazzav.AbstractLogEntityVO;
import br.com.dlp.jazzav.StatusEnum;

/**
 * Produto a ser anunciado no site
 * @author darcio
 *
 */
@Entity
public class ProdutoVO extends AbstractLogEntityVO<Long> {

	private static final long serialVersionUID = 7350383558311553100L;

	private String nome;

	private MarcaVO marcaVO;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}


	public String getNome() {
		return nome;
	}


	@ManyToOne
	public MarcaVO getMarcaVO() {
		return marcaVO;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setMarcaVO(MarcaVO marcaVO) {
		this.marcaVO = marcaVO;
	}

	
}
