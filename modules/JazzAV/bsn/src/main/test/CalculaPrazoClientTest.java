
public class CalculaPrazoClientTest {

	/**
	 * @param args
	 * @throws CalculoPrazoException 
	 */
	/*
	public static void main(String[] args) throws CalculoPrazoException {

		Integer nCdServico = 10014;
		String sCepOrigem = "03807300";
		String sCepDestino = "04052120";
		
		String prazo = calculaPrazo(nCdServico, sCepOrigem, sCepDestino);
		
		System.out.println("prazo: "+prazo);
		
		
	}
	*/

	/**
	 * Aciona WebService dos correios para 
	 * @param nCdServico
	 * @param sCepOrigem
	 * @param sCepDestino
	 * @return
	 * @throws CalculoPrazoException 
	 */
	
	/*
	protected static String calculaPrazo(Integer nCdServico, String sCepOrigem, String sCepDestino) throws CalculoPrazoException {
		CalcPrecoPrazoWS servico = new CalcPrecoPrazoWS();
		
         //10014 carta registrada
         //10030 carta simples
         //40010 		 
		
		CalcPrecoPrazoWSSoap calcPrecoPrazoWSSoap = servico.getCalcPrecoPrazoWSSoap();
		
		String prazo=null;
		
		CResultado resp1 = calcPrecoPrazoWSSoap.calcPrazo(nCdServico+"", sCepOrigem, sCepDestino);
		
		ArrayOfCServico aServicos = resp1.getServicos();
		
		List<CServico> servicos = aServicos.getCServico();
		
		
		for (CServico cServico : servicos) {
			if(cServico.getCodigo()==nCdServico){
				prazo = cServico.getPrazoEntrega();
			}
		}
		
		if(prazo==null){
			throw new CalculoPrazoException("Erro ao tentar calcular o prazo! Servico nao identificado");
		}
		
		return prazo;
	}
	 * */

}
