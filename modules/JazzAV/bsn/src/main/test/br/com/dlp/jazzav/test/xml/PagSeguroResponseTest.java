package br.com.dlp.jazzav.test.xml;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import br.com.uol.pagseguro.domain.Transaction;

public class PagSeguroResponseTest {

	/**
	 * @param args
	 * @throws JAXBException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws JAXBException, IOException {

		InputStream stream = PagSeguroResponseTest.class.getResourceAsStream("/consultaResposta.xml");

		JAXBContext context = JAXBContext.newInstance(br.com.uol.pagseguro.domain.Transaction.class);
		
		Unmarshaller unmarshaller = context.createUnmarshaller();
		//Marshaller marshaller = context.createMarshaller();
		
		Transaction transaction = new Transaction();
		
		transaction.setCode("33");
		
		//marshaller.marshal(transaction, new File("consultaMarshall.xml"));
		
		Object obj = unmarshaller.unmarshal(stream);
		
		
		BufferedInputStream bis = new BufferedInputStream(stream)	;
		
		
		InputStreamReader inputStreamReader = new InputStreamReader(stream);
		
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		
		
		
		String line =  bufferedReader.readLine();
		
		
		System.out.println(line);
	}
}