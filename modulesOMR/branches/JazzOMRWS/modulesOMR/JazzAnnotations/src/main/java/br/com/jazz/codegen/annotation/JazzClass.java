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
	 * Null VO apenas para marca��o de valor default. N�o � possivel utilizar null como default. Unpleasant
	 * 
	 * @author dpacifico
	 * @version 1.0
	 * @updated 19-mai-2010 16:29:28
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
	 * Atributos que provavelmente n�o ser�o mais �teis entidade renderer modulo indexado local
	 */

	Class<? extends Serializable> voMestre() default DefaultDummyVO.class;
}
