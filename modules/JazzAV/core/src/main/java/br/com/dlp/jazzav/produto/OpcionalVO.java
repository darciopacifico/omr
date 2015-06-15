package br.com.dlp.jazzav.produto;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.dlp.jazzav.AbstractLogEntityVO;

/**
 * Opcional de um veiculo
 * @author darcio
 *
 */
@Entity
public class OpcionalVO extends AbstractLogEntityVO<Long> {
	
	private static final long serialVersionUID = 9210652399983412903L;

	private TipoOpcionalEnum tipoOpcional;
	
	private String nome;
	private String sigla;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}

	@Enumerated(EnumType.STRING)
	public TipoOpcionalEnum getTipoOpcional() {
		return tipoOpcional;
	}

	
	
	

	public String getNome() {
		return nome;
	}


	public void setTipoOpcional(TipoOpcionalEnum tipoOpcional) {
		this.tipoOpcional = tipoOpcional;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
}
