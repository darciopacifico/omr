/**
 * Gerado por XDoclet, não edite!!
 **/
package br.com.dlp.validador.cliente;

import java.rmi.RemoteException;

import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.facade.ValueListHandlerException;
/**
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 *
 * Fachada para o componente ClienteVlh
 *
 **/
public interface ClienteVLHFacade extends br.com.dlp.framework.facade.IValueListHandlerFacade{

	/**
	 * Atualiza as paginas do VLH com esta pesquisa
	 */
	public void executeFindClienteVO(
				Integer pesquisaId
				,java.lang.String pesquisaCnpj
				,java.lang.String pesquisaNome
		)throws ValueListHandlerException, RemoteException ;
	/**
	 * Atualiza as paginas do VLH com esta pesquisa (com ordenação)
	 */
	public void executeFindClienteVO(
				Integer pesquisaId
				,java.lang.String pesquisaCnpj
				,java.lang.String pesquisaNome
	, QueryOrder[] queryOrders
		)throws ValueListHandlerException, RemoteException ;

}

