import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(locations = "/ApplicationContext_JazzAVbsn.xml")
public class SpringCalculaPrazoClientTest extends AbstractTestNGSpringContextTests{
	
/*
	@Resource
	CalcPrecoPrazoWSSoap calcPrecoPrazoWS;
	
	@Test
	public void testOnlyNumbersReplace() throws CalculoPrazoException{
		Integer nCdServico = 10014;
		String sCepOrigem = "03807300";
		String sCepDestino = "04052120";
		String prazo=null;
		CResultado resp1 = calcPrecoPrazoWS.calcPrazo(nCdServico+"", sCepOrigem, sCepDestino);
		
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
		
		
		System.out.println("prazo: "+prazo);
		
	}
	
	*/
	
	
}
