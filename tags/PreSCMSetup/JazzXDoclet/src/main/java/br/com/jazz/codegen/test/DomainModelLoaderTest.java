package br.com.jazz.codegen.test;

import java.io.File;

import br.com.jazz.codegen.DomainModelLoaderFactory;
import br.com.jazz.codegen.IDomainModelLoaderFactory;

public class DomainModelLoaderTest {
	
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		
		System.out.println(new File("./").getAbsolutePath());
		
		final IDomainModelLoaderFactory domainModelLoader = new DomainModelLoaderFactory("^.+VO$",
				new String[] { "../JazzQA/core/src/main/java" }

		);
		
		System.out.println(domainModelLoader.listDomain());
	}
	
}
