/**
 * 
 */
package br.com.jazz.codegen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.jazz.codegen.enums.ComparisonOperator;
import br.com.jazz.codegen.enums.JazzRenderer;
import br.com.jazz.codegen.enums.PropertyStereotype;

/**
 * TODO: IMPLEMENTAR CONTROLE DE LARGURA DE COLUNA NA TELA
 * 
 * @author dpacifico
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD })
public @interface JazzProp {
	
	ComparisonOperator comparison() default ComparisonOperator.EQUALS;
	JazzRenderer renderer() default JazzRenderer.DEFAULT;
	PropertyStereotype stereotype() default PropertyStereotype.SIMPLE_TEXT;
	
	String name();
	
	int order() default 0;
	
	boolean ignore() default false;
	
	boolean listable() default true;
	
	boolean readOnly() default false;
	
	boolean searchable() default true;
	
	boolean instantSearch() default false;
	
	boolean sortable() default false;
	
	boolean visible() default true;
	
	boolean updatable() default true;
	
	String tip() default "";
	
	String size() default ""; // TODO: VERIFICAR POR QUE ESTE CAMPO NAO PODE SER INT
	
}
