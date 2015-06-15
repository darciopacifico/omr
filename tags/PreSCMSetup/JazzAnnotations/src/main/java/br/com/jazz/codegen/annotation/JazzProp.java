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
@Target( { ElementType.METHOD })
public @interface JazzProp {
	
	public static enum ComparisonOperator {
		EQUALS, LIKE, RANGE;
	}
	
	public static enum Renderer {
		CALENDAR, CHECKBOX, COMBO, GRID, MODAL, POPUP, RADIO, SUBMODULE, TEXT, TEXTAREA, DEFAULT;
		public String getValue() {
			return toString();
		}
	}
	
	ComparisonOperator comparison() default ComparisonOperator.EQUALS;
	
	boolean ignore() default false;
	
	boolean listable() default true;
	
	String name();
	
	int order() default 0;
	
	boolean readOnly() default false;
	
	Renderer renderer() default Renderer.DEFAULT;
	
	boolean searchable() default true;
	
	boolean sortable() default true;
	
	String tip() default "";
	
	boolean visible() default true;
	
	String size() default "";
	
}
