package br.com.jazz.codegen;

import java.util.Map;

import br.com.jazz.jazzwizard.exception.GeradorException;

import com.thoughtworks.qdox.model.BeanProperty;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;

/**
 * Contrato para implementação de fábrica abstrata responsável por carregar o modelo de domínio para geração de código
 * 
 * @author darcio
 * 
 */
public interface IDomainModelLoaderFactory {
	
	public Object getAnnotationValue(BeanProperty beanProperty, String annotName, String attribute) throws GeradorException;
	
	public Object getAnnotationValue(JavaClass javaClass, String annotName, String attribute) throws GeradorException;
	
	public Object getAnnotationValue(JavaMethod javaMethod, String annotName, String attribute) throws GeradorException;
	
	
	/**
	 * Lista modelo de domínio carregado.
	 * 
	 * @return Todos as entidades de dominio disponíveis
	 */
	public abstract Map<String, JavaClass> getDomainMap();
	
	/**
	 * Lista modelo de domínio permitindo forcar refresh do ultimo estado do modelo lido
	 * 
	 * @param forceRefresh
	 *          forçar releitura do meta modelo de dominio
	 */
	public abstract Map<String, JavaClass> getDomainMap(boolean forceRefresh);

	public Boolean getBooleanValue(final JavaMethod javaMethod, final String annotName, final String attribute) throws GeradorException;

	public Boolean getBooleanValue(final JavaClass javaClass, final String annotName, final String attribute) throws GeradorException;

	public Boolean getBooleanValue(final BeanProperty beanProperty, final String annotName, final String attribute) throws GeradorException;
	
}
