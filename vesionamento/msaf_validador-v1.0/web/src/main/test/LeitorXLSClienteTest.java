//
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.junit.Test;
//
//import com.msaf.validador.consultaonline.ValidadorFacade;
//import com.msaf.validador.consultaonline.ValidadorFacadeImpl;
//import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
//import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;
//import com.msaf.validador.core.lerXLS.LeitorXLSException;
//import com.msaf.validador.core.tratamentoxls.LeitorXLSCliente;
//
//public class LeitorXLSClienteTest {
//
//	
//	Logger log = Logger.getLogger(LeitorXLSClienteTest.class);
//	
//	
//	@Test
//	public void testGerarArquivoXLS() {
//		
//		ValidadorFacade validadorFacade = new ValidadorFacadeImpl();
//		List<TpValidVO> listaTipos = criaTiposValid();
//		PedValidacaoVO pedValidacaoVO = new PedValidacaoVO();
//
//		LeitorXLSCliente leitorXLSCliente = new LeitorXLSCliente(validadorFacade, pedValidacaoVO);
//		
//		InputStream inputStreamUpload = getInputStreamFake();
//		
//
//		try {
//			leitorXLSCliente.processarArquivo(inputStreamUpload);
//		} catch (LeitorXLSException e) {
//			log.error("Erro ao tentar processar arquivo",e);
//		}
//		
//	}
//
//	
//	/**
//	 * gera um input stream fake a partir de um arquivo no disco
//	 * @return
//	 */
//	protected InputStream getInputStreamFake() {
//		File file = new File("C:\\darcio\\trabalho\\DOC_Validador\\ModeloParaTrabalho09 - 100.xls");
//		FileInputStream fileInputStream=null;
//		try {
//			fileInputStream = new FileInputStream(file);
//		} catch (FileNotFoundException e) {
//			log.error("Erro ao tentar ler o arquivo xls de teste!",e);
//			
//		}
//		return fileInputStream;
//	}
//
//	/**
//	 * Popula uma pequena lista de tipos de validação para testes
//	 * 
//	 */
//	protected List<TpValidVO> criaTiposValid() {
//		List<TpValidVO> tiposValid = new ArrayList<TpValidVO>();
//		
//		TpValidVO tpValidVO1 = new TpValidVO();
//		tpValidVO1.setId(1l);
//		tpValidVO1.setDescricao("cnpj sintegra");
//		
//		TpValidVO tpValidVO2 = new TpValidVO();
//		tpValidVO2.setId(2l);
//		tpValidVO2.setDescricao("cnpj receita");
//		
//		tiposValid.add(tpValidVO1);
//		tiposValid.add(tpValidVO2);
//		return tiposValid;
//	}
//}
