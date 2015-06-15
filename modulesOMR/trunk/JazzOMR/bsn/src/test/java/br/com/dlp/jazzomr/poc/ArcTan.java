package br.com.dlp.jazzomr.poc;

public class ArcTan {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double tangente;
		/*
		
		int angdeg = 60;
		
		System.out.println("angulo entrada graus = "+angdeg);
		
		double radians = Math.toRadians(angdeg);
		
		
		System.out.println("angulo entrada radianos/Pi = "+radians);
		
		tangente = Math.tan(radians);
		
		System.out.println("tangente = "+tangente);
		
		 */
		
		
		
		
		
		double catOposto = 722; //delta y
		double catAdjacente = 542;		//delta x
		
		
		tangente = catOposto/catAdjacente;
		
		double arcTan = Math.atan(tangente);
		
		System.out.println("arcTan="+arcTan);
		
		System.out.println("arctan radians/pi = "+arcTan/Math.PI);
		
		double graus = Math.toDegrees(arcTan);
		
		System.out.println("arctan graus = "+graus);
		
		System.out.println("arctan graus = "+(90-graus));
		
		
		
	}
	
}
