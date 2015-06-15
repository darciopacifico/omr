package com.msaf.validador.consultaonline;

import javax.xml.ws.Endpoint;

public class POJOPublisherImpl {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting Server");
		ValidadorFacadeImpl validadorFacadeImpl = new ValidadorFacadeImpl();
		String address = "http://localhost:9000/helloWorld";
		Endpoint.publish(address, validadorFacadeImpl);

	}

}
