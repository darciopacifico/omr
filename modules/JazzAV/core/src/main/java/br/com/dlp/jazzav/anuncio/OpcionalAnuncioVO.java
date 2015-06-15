package br.com.dlp.jazzav.anuncio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.dlp.jazzav.AbstractLogEntityVO;
import br.com.dlp.jazzav.produto.OpcionalVO;

@Entity
public class OpcionalAnuncioVO extends AbstractLogEntityVO<Long> {
	
	private static final long serialVersionUID = 9210652399983412903L;

	private OpcionalVO opcionalVO;

	private Boolean mostrar;
	
	private Integer ordem;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}

	public OpcionalVO getOpcionalVO() {
		return opcionalVO;
	}

	public Boolean getMostrar() {
		return mostrar;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOpcionalVO(OpcionalVO opcionalVO) {
		this.opcionalVO = opcionalVO;
	}

	public void setMostrar(Boolean mostrar) {
		this.mostrar = mostrar;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}



}
