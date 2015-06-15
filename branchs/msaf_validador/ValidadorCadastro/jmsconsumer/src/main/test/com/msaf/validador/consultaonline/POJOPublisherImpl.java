package com.msaf.validador.consultaonline;

import javax.xml.ws.Endpoint;

import org.apache.log4j.Logger;

public class POJOPublisherImpl {

	private static Logger log = Logger.getLogger(POJOPublisherImpl.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(log.isDebugEnabled()) log.debug("Starting Server");
		ValidadorFacadeImpl validadorFacadeImpl = new ValidadorFacadeImpl();
		String address = "http://localhost:9000/helloWorld";
		Endpoint.publish(address, validadorFacadeImpl);

	}

}
