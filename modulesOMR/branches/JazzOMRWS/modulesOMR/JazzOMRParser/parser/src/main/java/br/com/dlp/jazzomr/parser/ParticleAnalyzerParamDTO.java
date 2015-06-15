package br.com.dlp.jazzomr.parser;

import java.util.List;

import br.com.dlp.framework.exception.JazzRuntimeException;
import br.com.dlp.framework.vo.AbstractBaseVO;
import br.com.dlp.jazzomr.omr.ParticleTemplateVO;

/**
 * Determinar os parametros de construcao do analizador de particulas.<br>
 * Informa a area total da imagem scaneada e os modelos de particula que deverao ser encontrados.
 * @author darcio
 */
public class ParticleAnalyzerParamDTO {
	
	private double minCirc = 1;
	private final int count;
	private final List<ParticleTemplateVO> particleTemplateVOs;
	
	//setup inicial do loop
	double minPercArea = 1;
	double maxPercArea = 0;
	
	private IdentityRegion identityRegion;
	
	//private double particlesAngle=-0.9268521520137681; // letter
	//private double particlesAngle=-0.973004239118532; //a4 errado, scaneando torto
	private double particlesAngle=-0.973584239118532; //a4

	
	
	/**
	 * A partir do tamanho da figura e dos templates de particula, determinar a faixa de area de particula a ser encontrada.<br>
	 * <br>
	 * Ex: Uma particula de referencia media pode ter area de 0.17% ate 0.50% da area total da figura, alem da circularidade de no minimo 0.55.<br>
	 * Isto significa que, numa folha A4, as particulas de referencia sao um pouco menores que uma moeda de 1 centavo. <br>
	 * Normalmente sao 2 ou 3 particulas, uma em cada diagonal da folha. <br>
	 * 
	 */
	public ParticleAnalyzerParamDTO(List<ParticleTemplateVO> particleTemplateVOs) {
		// TODO Auto-generated constructor stub
		
		if(particleTemplateVOs == null || particleTemplateVOs.size()<2) {
			throw new JazzRuntimeException("Ao menos dois modelos de particula devem ser informados!");
		}
		
		this.particleTemplateVOs = particleTemplateVOs;
		
		//quantidade de particulas
		count = particleTemplateVOs.size();
		
		// determinar, dentre os modelos de particula informados, quais as medidas mais abrangentes para o analizador de particulas
		for (ParticleTemplateVO particleTemplateVO : particleTemplateVOs) {
			
			//pega a menor area percentual dos modelos informados
			minPercArea = Math.min(minPercArea, particleTemplateVO.getModel().minPercArea);
			
			//pega a maior area percentual dos modelos informados
			maxPercArea = Math.max(maxPercArea, particleTemplateVO.getModel().maxPercArea);
			
			// menor circularidade informada
			setMinCirc(Math.min(getMinCirc(), particleTemplateVO.getModel().minCircularity));
		}
		
		
		
	}
	
	/**
	 * @param minCirc the minCirc to set
	 */
	private void setMinCirc(double minCirc) {
		this.minCirc = minCirc;
	}
	
	/**
	 * @return the minCirc
	 */
	public double getMinCirc() {
		return minCirc;
	}
	
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * @return the particleTemplateVOs
	 */
	public List<ParticleTemplateVO> getParticleTemplateVOs() {
		return particleTemplateVOs;
	}
	
	/**
	 * @return the fartherParticlesAngle
	 */
	public double getParticlesAngle() {
		return particlesAngle;
	}
	
	/**
	 * @param fartherParticlesAngle the fartherParticlesAngle to set
	 */
	public void setParticlesAngle(double fartherParticlesAngle) {
		particlesAngle = fartherParticlesAngle;
	}
	
	/**
	 * @return the minPercArea
	 */
	public double getMinPercArea() {
		return minPercArea;
	}
	
	/**
	 * @return the maxPercArea
	 */
	public double getMaxPercArea() {
		return maxPercArea;
	}
	
	/**
	 * @param minPercArea the minPercArea to set
	 */
	public void setMinPercArea(double minPercArea) {
		this.minPercArea = minPercArea;
	}
	
	/**
	 * @param maxPercArea the maxPercArea to set
	 */
	public void setMaxPercArea(double maxPercArea) {
		this.maxPercArea = maxPercArea;
	}
	
	/**
	 * 
	 * @param totalImageArea
	 * @return
	 */
	public double getMinArea(double totalImageArea) {
		return totalImageArea*minPercArea;
	}
	
	/**
	 * 
	 * @param totalImageArea
	 * @return
	 */
	public double getMaxArea(double totalImageArea) {
		return totalImageArea * maxPercArea;
	}
	
	/**
	 * 
	 * @param totalImageArea
	 * @return
	 */
	public double getAvgParticleRadius(List<? extends java.awt.geom.Point2D.Double > pontos) {
		
		
		Double maxX = (Double) AbstractBaseVO.getMax("x", pontos);
		Double maxY = (Double) AbstractBaseVO.getMax("y", pontos);

		Double minX = (Double) AbstractBaseVO.getMin("x", pontos);
		Double minY = (Double) AbstractBaseVO.getMin("y", pontos);
		
		
		Double catetoA = maxX - minX;
		Double catetoB = maxY - minY;
				
		Double hipotenusa = Math.sqrt(Math.pow(catetoA, 2)+Math.pow(catetoB, 2)); 
		
		Double borderRef = 70d/2323d;
		Double radiusRef = hipotenusa*borderRef;
		
		
		return radiusRef;
	}
	
	public IdentityRegion getIdentityRegion() {
		return identityRegion;
	}
	
	public void setIdentityRegion(IdentityRegion identityRegionSubImage) {
		this.identityRegion = identityRegionSubImage;
	}
	
	
	
}