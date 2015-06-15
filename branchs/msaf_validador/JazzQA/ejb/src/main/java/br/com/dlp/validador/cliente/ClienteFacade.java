/**
 * Gerado por XDoclet, não edite!!
 **/
package br.com.dlp.validador.cliente;

import java.rmi.RemoteException;
import java.util.List;

import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;

/**
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 *

 * Fachada para o componente Cliente

 *
 **/
public interface ClienteFacade extends br.com.dlp.framework.facade.ICadastroFacade{

	/*****************************************/
	/*CONSTANTES PARA ORDENACAO DE REGISTROS */

		public static final String ORDERBY_ID ="orderById";

		public static final String ORDERBY_CNPJ ="orderByCnpj";

		public static final String ORDERBY_NOME ="orderByNome";

	/*CONSTANTES PARA ORDENACAO DE REGISTROS */
	/*****************************************/

	/**
	 * Pesquisa genérica 
	 */
	public List findClienteVO(
				Integer pesquisaId
				,java.lang.String pesquisaCnpj
				,java.lang.String pesquisaNome
		)throws BaseBusinessException, BaseTechnicalError, RemoteException ;
	/**
	 * Pesquisa genérica com Ordenação
	 */
	public List findClienteVO(
				Integer pesquisaId
				,java.lang.String pesquisaCnpj
				,java.lang.String pesquisaNome
	, QueryOrder[] queryOrders
		)throws BaseBusinessException, BaseTechnicalError, RemoteException ;

}

