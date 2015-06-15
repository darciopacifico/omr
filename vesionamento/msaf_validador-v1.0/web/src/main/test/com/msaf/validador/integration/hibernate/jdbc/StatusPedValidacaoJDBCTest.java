package com.msaf.validador.integration.hibernate.jdbc;

import java.io.IOException;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;


public class StatusPedValidacaoJDBCTest {

	
	
	protected static ApplicationContext getContext() {return context;}
	private static final String[] CONTEXT_CONFIG_LOCATION = {"src/main/webapp/WEB-INF/action-servlet.xml"};
	private static final ApplicationContext context = new FileSystemXmlApplicationContext(CONTEXT_CONFIG_LOCATION);
	
	public void testGetListaStatusPedidoValidacao() throws IOException{
		
		ApplicationContext context = getContext();

		IStatusPedValidacaoJDBC statusPedValidadorJDBC = (IStatusPedValidacaoJDBC) context.getBean("statusPedValidacaoJDBC");
		List<StatusPediValidacao> l = statusPedValidadorJDBC.getListaStatusPedidoValidacao("1");
		if(null!=l)
			System.out.println("Fim: "+ l.size());
		else 
			System.out.println("Fim: null");
		

	}
	
}
