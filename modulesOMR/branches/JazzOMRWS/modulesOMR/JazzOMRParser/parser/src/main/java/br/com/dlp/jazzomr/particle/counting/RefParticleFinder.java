/**
 * 
 */
package br.com.dlp.jazzomr.particle.counting;

import java.util.List;

import br.com.dlp.jazzomr.omr.ParticleVO;
import br.com.dlp.jazzomr.parser.ParticleAnalyzerParamDTO;

/**
 * @author darcio
 *
 */
public interface RefParticleFinder {

	public abstract List<ParticleVO> executeFind(List<ParticleVO> particles, ParticleAnalyzerParamDTO params) throws ParticleFindingException;

}
