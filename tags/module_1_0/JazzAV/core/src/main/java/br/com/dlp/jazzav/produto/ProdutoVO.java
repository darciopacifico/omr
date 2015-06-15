package br.com.dlp.jazzav.produto;

import javax.persistence.Entity;

import br.com.dlp.jazzomr.base.StatusEnum;

/**
 * Produto a ser anunciado no site
 * @author darcio
 *
 */
@Entity
public class ProdutoVO {

	private String nome;

	private MarcaVO marcaVO;
	
	private StatusEnum status;
	
	
	
}
