/**
 * 
 */
package br.com.dlp.jazzomr.particle.counting;

import java.util.Comparator;

import br.com.dlp.jazzomr.omr.EParticlePosition;
import br.com.dlp.jazzomr.omr.ParticleVO;

/**
 * Comparador para encontrar as particulas mais proximas da posicao informada no construtor
 * 
 * @author darcio
 * 
 */
public class ParticlePositionComparator implements Comparator<ParticleVO> {

	private EParticlePosition position;

	public ParticlePositionComparator(EParticlePosition position) {
		super();
		this.position = position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(ParticleVO particleVO1, ParticleVO particleVO2) {

		switch (position) {
		case TOP_LEFT:
			double tlx1 = particleVO1.getX();
			double tly1 = particleVO1.getY();
			double tlx2 = particleVO2.getX();
			double tly2 = particleVO2.getY();
			
			return (int) ((tlx2*tly2)-(tlx1*tly1)); 

		case TOP_RIGHT:

			double trx1 = particleVO1.getX();
			double try1 = particleVO1.getY();
			double trx2 = particleVO2.getX();
			double try2 = particleVO2.getY();

			return (int) ((trx1*try1) - (trx2*try2)); 
			

		case BOTTOM_LEFT:
			double blx1 = particleVO1.getX();
			double bly1 = particleVO1.getY();
			double blx2 = particleVO2.getX();
			double bly2 = particleVO2.getY();
			
			return (int) ((blx1*bly1) - (blx2*bly2)); 

		case BOTTOM_RIGHT:
			double brx1 = particleVO1.getX();
			double bry1 = particleVO1.getY();
			double brx2 = particleVO2.getX();
			double bry2 = particleVO2.getY();
			
			return (int) ((brx1*bry1)-(brx2*bry2)); 

		default:
			break;
		}

		return 0;
	}

}
