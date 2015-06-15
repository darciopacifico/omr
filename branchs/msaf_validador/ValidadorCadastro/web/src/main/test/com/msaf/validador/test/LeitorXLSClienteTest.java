package com.msaf.validador.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.msaf.validador.consultaonline.ValidadorFacade;
import com.msaf.validador.consultaonline.ValidadorFacadeMQImpl;
import com.msaf.validador.consultaonline.cliente.ClienteVO;
import com.msaf.validador.consultaonline.exceptions.ArquivoInvalidoException;
import com.msaf.validador.consultaonline.solicitacaovalidacao.PedValidacaoVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.TpValidVO;
import com.msaf.validador.core.lerXLS.LeitorXLSException;
import com.msaf.validador.core.tratamentoxls.LeitorXLSCliente;

public class LeitorXLSClienteTest {

	
	Logger log = Logger.getLogger(LeitorXLSClienteTest.class);
	
	
	@Test
	public void testGerarArquivoXLS() throws ArquivoInvalidoException {
		
		ValidadorFacade validadorFacade = new ValidadorFacadeMQImpl();
		List<TpValidVO> listaTipos = criaTiposValid();
		PedValidacaoVO pedValidacaoVO = new PedValidacaoVO();

		pedValidacaoVO.setTpValidacoes(listaTipos);
		pedValidacaoVO.setId(41);
		
		ClienteVO clienteFk = new ClienteVO();
		clienteFk.setIdCliente(1);
		pedValidacaoVO.setClienteFk(clienteFk);
		
		pedValidacaoVO.setQtdeRegistrosArq(1000);
		LeitorXLSCliente leitorXLSCliente = new LeitorXLSCliente(validadorFacade, pedValidacaoVO);
		
		InputStream inputStreamUpload = getInputStreamFake();
		

		try {
			if(log.isDebugEnabled()) log.debug("Iniciando...");
			leitorXLSCliente.processarArquivo(inputStreamUpload);
			if(log.isDebugEnabled()) log.debug("Fim... ");	
			
			//leitorXLSCliente.processarArquivo(inputStreamUpload);
		} catch (LeitorXLSException e) {
			if(log.isDebugEnabled()) log.debug("Erro ao tentar processar arquivo");
		}
		
	}

	
	/**
	 * gera um input stream fake a partir de um arquivo no disco
	 * @return
	 */
	protected InputStream getInputStreamFake() {
		//File file = new File("C:\\darcio\\trabalho\\DOC_Validador\\ModeloParaTrabalho50000.xls");
		//File file = new File("C:\\darcio\\trabalho\\DOC_Validador\\ModelosComDadosRetornando_6reg.xls");
		File file = new File("C:\\temp\\ModeloParaTrabalho09 - 100.xls");
		
		
		
		FileInputStream fileInputStream=null;
		try {
			fileInputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			if(log.isDebugEnabled()) log.debug("Erro ao tentar ler o arquivo xls de teste!");
			
		}
		return fileInputStream;
	}

	/**
	 * Popula uma pequena lista de tipos de validação para testes
	 * 
	 */
	protected List<TpValidVO> criaTiposValid() {
		List<TpValidVO> tiposValid = new ArrayList<TpValidVO>();
		
		TpValidVO tpValidVO1 = new TpValidVO();
		tpValidVO1.setId(1l);
		tpValidVO1.setDescricao("cnpj sintegra");
		
		//TpValidVO tpValidVO2 = new TpValidVO();
		//tpValidVO2.setId(3l);
		//tpValidVO2.setDescricao("cnpj receita");
		
		tiposValid.add(tpValidVO1);
		//tiposValid.add(tpValidVO2);
		return tiposValid;
	}
}
