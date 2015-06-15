/**
 * 
 */
package br.com.dlp.jazzomr.omr;

/**
 * Areas padrao (em percentual) as partículas de referencia
 * @author darcio
 */
public enum EParticleArea{

	PEQUENA (0.0008, 0.0025, 0.55),
	MEDIA 	(0.0017, 0.0050, 0.55),
	GRANDE 	(0.0037, 0.0098, 0.55);
	
	public final double minPercArea;
	public final double maxPercArea;
	public final double minCircularity;
	
	private EParticleArea(double minPercArea, double maxPercArea, double minCircularity) {
		this.minPercArea = minPercArea;
		this.maxPercArea = maxPercArea;
		this.minCircularity = minCircularity;
	}
	
	
}
