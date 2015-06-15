/**
 * 
 */
package br.com.jazz.jrds.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author darciopa
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.TYPE })
public @interface Navigable {
	boolean navigable() default true;
	Class<?> nextType() default Navigable.class;
	String alias() default "";
	Class visitorClass() default Navigable.class;
}

