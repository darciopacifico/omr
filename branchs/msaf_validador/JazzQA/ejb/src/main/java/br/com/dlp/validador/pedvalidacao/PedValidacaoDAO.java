/**
 * Gerado por XDoclet, não edite!!
 **/
package br.com.dlp.validador.pedvalidacao;

import java.util.List;

import br.com.dlp.framework.dao.DAOException;
import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.validador.cliente.ClientePK;

/**
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 *
 * Contrado de DAO para o componente PedValidacao
 *
 **/
public interface PedValidacaoDAO extends br.com.dlp.framework.dao.IDAO {

	/**
	 * Contrato para pesquisa findPedValidacaoVO
	 */
	public List findPedValidacaoVO  (
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
	)  throws DAOException, BaseTechnicalError;
	/**
	 * Contrato para pesquisa findPedValidacaoVO
	 */
	public List findPedValidacaoVO  (
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
	, QueryOrder[] queryOrders )  throws DAOException, BaseTechnicalError;

}

