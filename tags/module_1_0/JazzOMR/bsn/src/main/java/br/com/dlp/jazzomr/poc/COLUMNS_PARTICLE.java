/**
 * 
 */
package br.com.dlp.jazzomr.poc;

/**
 * ID das colunas de resultado do analizador de particulas do imagej
 * @author darcio
 *
 */
public enum COLUMNS_PARTICLE {

	AREA(0),
	MEAN ( 1),
	STDDEV ( 2),
	MODE ( 3),
	MIN ( 4),
	MAX ( 5),
	X ( 6),
	Y ( 7),
	XM ( 8),
	YM ( 9),
	PERIM ( 10),
	BX ( 11),
	BY ( 12),
	WIDTH ( 13),
	HEIGHT ( 14),
	MAJOR ( 15),
	MINOR ( 16),
	ANGLE ( 17),
	CIRC ( 18),
	FERET ( 19),
	INTDEN ( 20),
	MEDIAN ( 21),
	SKEW ( 22),
	KURT ( 23),
	PERC_AREA ( 24),
	RAWINTDEN ( 25),
	CH ( 26),
	SLICE ( 27),
	FRAME ( 28),
	FERETX ( 29),
	FERETY ( 30),
	FERETANGLE ( 31),
	MINFERET ( 32),
	AR ( 33),
	ROUND ( 34),
	SOLIDITY ( 35);

	public int col;
	
	COLUMNS_PARTICLE(int col){
		this.col = col;
	}

	/**
	 * @return the col
	 */
	public int getCol() {
		return col;
	}
	
	
}
