package br.com.dlp.framework.unittest.ih;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Implementa��o de teste de invocationHandler. 
 * Esta classe � respons�vel por interceptar dinamicamente qualquer chamada � interface de negocio.
 * 
 * Esta implementa��o apenas 
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
		System.out.println("Interceptando a chamada ao m�todo '"+method.getName()+"' do objeto => "+proxiedObject);
		System.out.println("****************************************");
		returnObject = method.invoke(proxiedObject, objects);
		return returnObject;
	}

}
