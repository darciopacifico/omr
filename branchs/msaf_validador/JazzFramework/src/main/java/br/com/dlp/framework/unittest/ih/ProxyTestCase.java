package br.com.dlp.framework.unittest.ih;

import java.lang.reflect.Proxy;

import br.com.dlp.framework.invocationhandler.InvocationHandlerException;

/**
 * Test case para demonstrar a utilização de um proxy com InvocationHandler
 * @author deal.dpacifico
 *
 */
public class ProxyTestCase {

	/**
	 * @param args
	 * @throws InvocationHandlerException 
	 */
	public static void main(String[] args) throws InvocationHandlerException {
	
		BPessoa bpessoaSimples;
		BPessoa bpessoaProxy;
		
		//instanciando componente sem proxy
		bpessoaSimples = new BPessoaImpl();
		
		//instanciando componente com proxy
		TesteInvocationHandler invocationHandler = new TesteInvocationHandler(bpessoaSimples);
		bpessoaProxy = (BPessoa) Proxy.newProxyInstance(bpessoaSimples.getClass().getClassLoader(), new Class[]{BPessoa.class} , invocationHandler);
		

		bpessoaSimples.meuMetodoNegocio();
		bpessoaProxy.meuMetodoNegocio();
	}

}
