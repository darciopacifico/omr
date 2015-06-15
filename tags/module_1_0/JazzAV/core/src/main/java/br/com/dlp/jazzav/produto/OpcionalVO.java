package br.com.dlp.jazzav.produto;

import javax.persistence.Entity;

/**
 * Opcional de um veiculo
 * @author darcio
 *
 */
@Entity
public class OpcionalVO {

	private TipoOpcionalEnum tipoOpcional;
	
	private String nome;
	
}
