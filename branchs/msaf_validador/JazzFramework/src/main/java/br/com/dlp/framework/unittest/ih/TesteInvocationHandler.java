package br.com.dlp.framework.unittest.ih;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Implementação de teste de invocationHandler. 
 * Esta classe é responsável por interceptar dinamicamente qualquer chamada à interface de negocio.
 * 
 * Esta implementação apenas 
 * 
 * @author deal.dpacifico
 *
 */
public class TesteInvocationHandler implements InvocationHandler {

	private Object proxiedObject;

	public TesteInvocationHandler(Object proxiedObject) {
		this.proxiedObject = proxiedObject;
	};

	public Object invoke(Object object, Method method, Object[] objects)
			throws Throwable {
		Object returnObject;
		
		System.out.println("");
		System.out.println("");
		System.out.println("****************************************");
		System.out.println("ESTA CHAMADA PODE SER SUBSTITUIDA POR UMA CHAMADA REMOTA AO ESB");
		System.out.println("Interceptando a chamada ao método '"+method.getName()+"' do objeto => "+proxiedObject);
		System.out.println("****************************************");
		returnObject = method.invoke(proxiedObject, objects);
		return returnObject;
	}

}
