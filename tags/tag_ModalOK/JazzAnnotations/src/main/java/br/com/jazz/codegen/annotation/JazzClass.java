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
@Target({ElementType.TYPE})
public @interface JazzClass {

	String name();

	String description() default "";
	
	boolean ignore() default false;

	boolean gerarModel() default true;

	boolean gerarPersistencia() default true;

	boolean gerarView() default true;

	boolean gerarController() default true;

	boolean baseEntity() default true;

	Class<? extends Serializable> voMestre() default DefaultDummyVO.class;

	int vlhPageSize() default 30;

	/*
	Atributos que provavelmente não serão mais úteis
	entidade
	renderer
	modulo
	indexado
	local
	 */
	
	/**
	 * Null VO apenas para marcação de valor default. Não é possivel utilizar null como default. Unpleasant
	 * @author dpacifico
	 */
	public static final class DefaultDummyVO implements Serializable {
		private static final long serialVersionUID = 4412045356716921286L;
	}
}

