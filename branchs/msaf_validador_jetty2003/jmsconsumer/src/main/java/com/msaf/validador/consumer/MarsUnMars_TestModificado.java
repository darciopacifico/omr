
package com.msaf.validador.consumer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.msaf.validador.consultaonline.solicitacaovalidacao.DadosRetInstVO;
import com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO;

public class MarsUnMars_TestModificado {

	/**
	 * 
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws JAXBException 
	 */
	public static void main(String[] args) throws FileNotFoundException, JAXBException {
		//meu diretorio - ekler
		//C:\\MasterSafCVSRep\\MSaf_ValidadorCadastro\\ProjetoJava\\web\\src\\main\\resources
		
		FileInputStream fileInputStream = new FileInputStream("C:\\MasterSafCVSRep\\MSaf_ValidadorCadastro\\ProjetoJava\\web\\src\\main\\resources\\Resp.xml");
		JAXBContext jc = JAXBContext.newInstance("com.msaf.validador.consultaonline.solicitacaovalidacao");
		
		Marshaller marshaller = jc.createMarshaller();
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		
		//Object source = unmarshaller.unmarshal(fileInputStream);
		com.msaf.validador.consultaonline.solicitacaovalidacao.ResulConsVO resultadoConsultaVO = new ResulConsVO();
		resultadoConsultaVO.setQuantidade(1);
		resultadoConsultaVO.setVersao("1.0");
		
		DadosRetInstVO dadosRetorno = new  DadosRetInstVO();
		
		dadosRetorno.setRazaoSocial("Empresa Tal");
		dadosRetorno.setCnpj("1234567");
		dadosRetorno.setEstado("RJ");

		//dadosRetorno.getRegistrosRetorno().add(registroRetornoVO);		
		//marshaller.marshal(dadosRetorno, new File("C:\\MastersafDoc\\XMLs\\Resp.xml"));
		
		ResulConsVO dadosRetorno2 = (ResulConsVO) unmarshaller.unmarshal(new File("C:\\MastersafDoc\\XMLs\\Resp.xml"));
	}

}
