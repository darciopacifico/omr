package com.msaf.validador.pesquisa.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Factory {

	static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("META-INF/application-context.xml");

	public static Object getBean(String id) {
		return applicationContext.getBean(id);
	}
	
}
