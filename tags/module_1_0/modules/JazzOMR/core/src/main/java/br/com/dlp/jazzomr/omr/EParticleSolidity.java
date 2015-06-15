
/**
 * 
 */
package br.com.dlp.jazzomr.omr;

/**
 * @author darcio
 *
 */
public enum EParticleSolidity {
	SOLIDA(0.9), VAZADA(0.6);

	
	/**
	 * @param mediumSolidity
	 */
	private EParticleSolidity(double mediumSolidity) {
		this.mediumSolidity = mediumSolidity;
	}

	public final double mediumSolidity;
	
	

}
