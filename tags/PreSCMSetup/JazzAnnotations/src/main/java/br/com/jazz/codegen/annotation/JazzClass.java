package br.com.jazz.codegen.annotation;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author dpacifico
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE })
public @interface JazzClass {
	
	/**
	 * Null VO apenas para marcação de valor default. Não é possivel utilizar null como default. Unpleasant
	 * 
	 * @author dpacifico
	 */
	public static final class DefaultDummyVO implements Serializable {
		private static final long serialVersionUID = 4412045356716921286L;
	}
	
	boolean baseEntity() default true;
	
	String description() default "";
	
	boolean gerarController() default true;
	
	boolean gerarModel() default true;
	
	boolean gerarPersistencia() default true;
	
	boolean gerarView() default true;
	
	boolean ignore() default false;
	
	String name();
	
	int vlhPageSize() default 30;
	
	/*
	 * Atributos que provavelmente não serão mais úteis entidade renderer modulo indexado local
	 */

	Class<? extends Serializable> voMestre() default DefaultDummyVO.class;
}
