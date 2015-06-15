/**
 * 
 */
package br.com.jazz.codegen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author dpacifico
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface JazzProp {

	public static enum ComparisonOperator {
		LIKE, RANGE, EQUALS;
	}

	public static enum Renderer {
		COMBO, POPUP, RADIO, CHECKBOX, TEXT, TEXTAREA, CALENDAR, MODAL, SUBMODULE, GRID;
		public String getValue(){
			return toString();
		}
	}

	String name();
	String tip() default "";

	Renderer renderer() default Renderer.TEXT; 
	ComparisonOperator comparison() default ComparisonOperator.EQUALS;

	boolean readOnly() default false;

	boolean listable() default true;

	boolean sortable() default true;

	boolean visible() default true;

	boolean searchable() default true;

	boolean ignore() default false;

	int order() default 0;


}
