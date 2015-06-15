package br.com.jazz.codegen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.BeanProperty;
import com.thoughtworks.qdox.model.JavaClass;

public class QDoxAnnotationsTeste {

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, SecurityException, NoSuchMethodException {
		
		
		Class clazz = Class.forName("br.com.jazz.codegen.annotation.JazzProp");
		
		
		Method m = clazz.getDeclaredMethod("comparison",new Class[]{});
		
		System.out.println(m);
		
		JavaDocBuilder builder = new JavaDocBuilder();
		
		builder.addSource(new File("../JazzQA/core/src/main/java/br/com/dlp/jazzqa/tiporequisito/TipoRequisitoVO.java"));
		
		JavaClass[] classes =  builder.getClasses();
		
		for (JavaClass javaClass : classes) {
			BeanProperty[] beanProperties = javaClass.getBeanProperties(true);

			for (BeanProperty beanProperty : beanProperties) {
				Annotation[] annotas = beanProperty.getAccessor().getAnnotations();
				
				for (Annotation annotation : annotas) {
					
					System.out.println(annotation);
					System.out.println(annotation);
				}

			}
			
		}
		
	}
}
