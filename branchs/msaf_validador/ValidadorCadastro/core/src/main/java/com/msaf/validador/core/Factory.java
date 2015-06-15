package com.msaf.validador.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Factory {

	static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/com/msaf/validador/tipovalidacao/application-context.xml");

	public static Object getBean(String id) {
		return applicationContext.getBean(id);
	}
	
}
