/**
 * 
 */
package br.com.dlp.jazzomr.particle.counting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.dlp.jazzomr.omr.ParticleVO;
import br.com.dlp.jazzomr.poc.ParticleAnalyzerParamDTO;

/**
 * Implementacao de strategy para processamento de imagem scaneada para OMR com 2 particulas de referencia.<br>
 * 
 * Apura, dentre as particulas detectadas, quais as duas que sao para referencia de OMR.<br>
 * 
 * Resumindo: acha as bolinhas pretas na imagem do documento.<br>
 * <br>
 * 
 * @author darcio
 */
public class TwoRefParticleFinder implements RefParticleFinder {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public List<ParticleVO> executeFind(List<ParticleVO> particles, ParticleAnalyzerParamDTO params) throws ParticleFindingException {

		ParticleVO particleRetA = null;
		ParticleVO particleRetB = null;

		Iterator<ParticleVO> itX = particles.iterator();
		Iterator<ParticleVO> itY = particles.iterator();

		double greaterDistance = 0;
		
		//Itera todas as combinacoes possiveis de 2 pontos para encontrar a maior distancia 
		while (itX.hasNext()) {
			ParticleVO particleX = itX.next();

			while (itY.hasNext()) {
				ParticleVO particleY = itY.next();

				// nao faz sentido comparar a mesma particula
				if (!particleX.equals(particleY)) {
					double actualXYDistance = calcularDistancia(particleX, particleY);

					// se for a maior distancia atualiza as particulas a serem retornadas
					if (actualXYDistance > greaterDistance) {
						greaterDistance = actualXYDistance;
						particleRetA = particleX;
						particleRetB = particleY;
					}
				}
			}
		}

		List<ParticleVO> returnParticles = new ArrayList<ParticleVO>(2);

		//ultima protecao do algoritmo
		if(particleRetA==null || particleRetB==null){
			throw new ParticleFindingException("Erro ao tentar encontrar as 2 particulas de referencia! "+particles);
		}
		
		returnParticles.add(particleRetA);
		returnParticles.add(particleRetB);

		logParticles(returnParticles);
		
		return returnParticles;
	}

	/**
	 * @param returnParticles
	 */
	protected void logParticles(List<ParticleVO> returnParticles) {
		for (ParticleVO particleVO : returnParticles) {
			if(log.isDebugEnabled()){
				log.debug(" x=" + particleVO.getX());
				log.debug(" y=" + particleVO.getY());
			}
		}
	}

	/**
	 * Calcula a distancia entre as duas particulas <br>
	 * Em qualquer triangulo retangulo, o quadrado do comprimento da hipotenusa e igual a soma dos quadrados dos comprimentos dos catetos.<br>
	 * 
	 * @param particle1
	 * @param particle2
	 * @return
	 */
	protected double calcularDistancia(ParticleVO particle1, ParticleVO particle2) {

		double x1 = particle1.getX();
		double y1 = particle1.getY();

		double x2 = particle2.getX();
		double y2 = particle2.getY();

		double catetoX = Math.abs(x1 - x2);
		double catetoY = Math.abs(y1 - y2);

		double catetoXAoQuadrado = Math.pow(catetoX, 2);
		double catetoYAoQuadrado = Math.pow(catetoY, 2);

		double hipotenusa = Math.sqrt(catetoXAoQuadrado + catetoYAoQuadrado);

		return hipotenusa;
	}

}
