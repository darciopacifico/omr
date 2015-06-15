/**
 * Gerado por XDoclet, não edite!!
 **/
package br.com.dlp.validador.pedvalidacao;

import java.rmi.RemoteException;

import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.facade.ValueListHandlerException;
import br.com.dlp.validador.cliente.ClientePK;
/**
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 *
 * Fachada para o componente PedValidacaoVlh
 *
 **/
public interface PedValidacaoVLHFacade extends br.com.dlp.framework.facade.IValueListHandlerFacade{

	/**
	 * Atualiza as paginas do VLH com esta pesquisa
	 */
	public void executeFindPedValidacaoVO(
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
		)throws ValueListHandlerException, RemoteException ;
	/**
	 * Atualiza as paginas do VLH com esta pesquisa (com ordenação)
	 */
	public void executeFindPedValidacaoVO(
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
		)throws ValueListHandlerException, RemoteException ;

}

