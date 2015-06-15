package br.com.dlp.jazzomr.omr;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.dlp.jazzomr.base.AbstractEntityVO;

/**
 * Gabarito para análise de particulas
 * 
 * @author darcio
 */
public class ParticleTemplateVO extends AbstractEntityVO<Long>{

	private static final long serialVersionUID = 8357025111318994501L;
	
	/**
	 * Construtor padrão
	 * @param model
	 * @param position
	 */
	public ParticleTemplateVO(EParticleArea model, EParticlePosition position) {
		super();
		this.model = model;
		this.position = position;
	}
	
	/**
	 * Constroi um particleTemplate
	 * @param model
	 * @param position
	 * @return
	 */
	public static ParticleTemplateVO getInstance(EParticleArea model, EParticlePosition position){
		return new ParticleTemplateVO(model, position);
	}
	
	private EParticleArea model;
	private EParticlePosition position;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public Long getPK() {
		return this.pk;
	}
	

	
	/**
	 * @return the model
	 */
	public EParticleArea getModel() {
		return model;
	}
	/**
	 * @return the position
	 */
	public EParticlePosition getPosition() {
		return position;
	}
	/**
	 * @param model the model to set
	 */
	public void setModel(EParticleArea model) {
		this.model = model;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(EParticlePosition position) {
		this.position = position;
	}
	
	
}