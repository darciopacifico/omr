/**
 * Gerado por XDoclet, não edite!!
 **/
package br.com.dlp.validador.pedvalidacao;

import java.rmi.RemoteException;
import java.util.List;

import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.validador.cliente.ClientePK;

/**
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 *

 * Fachada para o componente PedValidacao

 *
 **/
public interface PedValidacaoFacade extends br.com.dlp.framework.facade.ICadastroFacade{

	/*****************************************/
	/*CONSTANTES PARA ORDENACAO DE REGISTROS */

		public static final String ORDERBY_ID ="orderById";

		public static final String ORDERBY_DATADOWNLOAD ="orderByDataDownload";

		public static final String ORDERBY_DATASOLICITACAO ="orderByDataSolicitacao";

		public static final String ORDERBY_DATATERMINO ="orderByDataTermino";

		public static final String ORDERBY_QTDEREGISTROSARQ ="orderByQtdeRegistrosArq";

		public static final String ORDERBY_QTDEREGISTROSPROC ="orderByQtdeRegistrosProc";

		public static final String ORDERBY_CLIENTEVO ="orderByClienteVO";

	/*CONSTANTES PARA ORDENACAO DE REGISTROS */
	/*****************************************/

			/*classe:PedValidacaoVO  method:getClienteVO*/
			public List findAllCliente() throws BaseBusinessException, BaseTechnicalError, RemoteException;

	/**
	 * Pesquisa genérica 
	 */
	public List findPedValidacaoVO(
				Integer pesquisaId
				,java.util.Date pesquisaDataDownloadDe
				,java.util.Date pesquisaDataDownloadAte
				,java.util.Date pesquisaDataSolicitacaoDe
				,java.util.Date pesquisaDataSolicitacaoAte
				,java.util.Date pesquisaDataTerminoDe
				,java.util.Date pesquisaDataTerminoAte
				,Integer pesquisaQtdeRegistrosArqDe
				,Integer pesquisaQtdeRegistrosArqAte
				,Integer pesquisaQtdeRegistrosProcDe
				,Integer pesquisaQtdeRegistrosProcAte
				,ClientePK pesquisaCliente
		)throws BaseBusinessException, BaseTechnicalError, RemoteException ;
	/**
	 * Pesquisa genérica com Ordenação
	 */
	public List findPedValidacaoVO(
				Integer pesquisaId
				,java.util.Date pesquisaDataDownloadDe
				,java.util.Date pesquisaDataDownloadAte
				,java.util.Date pesquisaDataSolicitacaoDe
				,java.util.Date pesquisaDataSolicitacaoAte
				,java.util.Date pesquisaDataTerminoDe
				,java.util.Date pesquisaDataTerminoAte
				,Integer pesquisaQtdeRegistrosArqDe
				,Integer pesquisaQtdeRegistrosArqAte
				,Integer pesquisaQtdeRegistrosProcDe
				,Integer pesquisaQtdeRegistrosProcAte
				,ClientePK pesquisaCliente
	, QueryOrder[] queryOrders
		)throws BaseBusinessException, BaseTechnicalError, RemoteException ;

}

