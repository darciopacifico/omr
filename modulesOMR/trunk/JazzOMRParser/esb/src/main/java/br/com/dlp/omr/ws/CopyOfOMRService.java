package br.com.dlp.omr.ws;


public class CopyOfOMRService implements IOMRService {

	
	/* (non-Javadoc)
	 * @see br.com.dlp.omr.ws.IOMRService#gerarProva(java.lang.String)
	 */
	@Override
	public String gerarProva(String nome){
		
		
		
		return "olá "+nome;
		//throw new RuntimeException("Teste 123");
	}

}
