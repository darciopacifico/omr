/**
 * 
 */
package br.com.dlp.jazzomr.particle.counting;

import java.util.ArrayList;
import java.util.List;

import br.com.dlp.jazzomr.omr.ParticleVO;
import br.com.dlp.jazzomr.poc.ParticleAnalyzerParamDTO;


/**
 * Implementacao de strategy para processamento de imagem scaneada para OMR com 3 particulas de referencia.<br>
 * 
 * Apura, dentre as particulas detectadas, quais as 3 que sao para referencia de OMR.<br>
 * 
 * Resumindo: acha as bolinhas pretas na imagem do documento.<br>
 * <br>
 * @author darcio
 */
public class ThreeRefParticleFinder extends TwoRefParticleFinder{

	@Override
	public ArrayList<ParticleVO> executeFind(List<ParticleVO> particles, ParticleAnalyzerParamDTO params) throws ParticleFindingException{
		
		List<ParticleVO> particulas = super.executeFind(particles, params);
		
		
		
		
		
		
		return null;
	}
	
}
