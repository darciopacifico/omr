package com.msaf.validador.consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TesteDarcio {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistenciaProject");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
	}

}
