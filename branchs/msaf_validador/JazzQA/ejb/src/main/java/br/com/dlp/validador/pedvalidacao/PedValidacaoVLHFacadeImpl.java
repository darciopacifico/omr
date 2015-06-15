/**
 * Gerado por XDoclet, não edite!!
 **/
package br.com.dlp.validador.pedvalidacao;

import java.rmi.RemoteException;
import java.util.List;

import br.com.dlp.framework.dao.DAOException;
import br.com.dlp.framework.dao.DAOFactoryException;
import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.facade.ValueListHandlerException;
import br.com.dlp.jazzqa.util.JazzQAConstants;
import br.com.dlp.validador.cliente.ClientePK;

/**
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 *
 * Implementação da fachada PedValidacaoVLHFacade
 **/
public class PedValidacaoVLHFacadeImpl extends br.com.dlp.jazzqa.base.AbstractJazzQAVLHFacadeImpl implements PedValidacaoVLHFacade {

	/**
	 *Indica ao framework qual o id do Módulo
	 */
	public String getModuleId() {
		return JazzQAConstants.MODULE_PEDVALIDACAO;
	}  

	/**
	 *Indica ao framework qual o id do VLH
	 */
	public String getModuleVLHId() {
    	return JazzQAConstants.MODULE_PEDVALIDACAO_VLH;
	}

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
		) throws ValueListHandlerException, RemoteException{

	executeFindPedValidacaoVO(
					pesquisaId
					,pesquisaDataDownloadDe
					,pesquisaDataDownloadAte
					,pesquisaDataSolicitacaoDe
					,pesquisaDataSolicitacaoAte
					,pesquisaDataTerminoDe
					,pesquisaDataTerminoAte
					,pesquisaQtdeRegistrosArqDe
					,pesquisaQtdeRegistrosArqAte
					,pesquisaQtdeRegistrosProcDe
					,pesquisaQtdeRegistrosProcAte
					,pesquisaCliente
		, new QueryOrder[]{});

	}

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
	, QueryOrder[] queryOrders) throws ValueListHandlerException, RemoteException{

		if(logger.isDebugEnabled()){
			logger.debug("executando pesquisa pesquisando... ");
		}

		PedValidacaoDAO dao;
		try {
			dao = (PedValidacaoDAO)getDAO();
			List result = dao.findPedValidacaoVO(
				pesquisaId
				,pesquisaDataDownloadDe
				,pesquisaDataDownloadAte
				,pesquisaDataSolicitacaoDe
				,pesquisaDataSolicitacaoAte
				,pesquisaDataTerminoDe
				,pesquisaDataTerminoAte
				,pesquisaQtdeRegistrosArqDe
				,pesquisaQtdeRegistrosArqAte
				,pesquisaQtdeRegistrosProcDe
				,pesquisaQtdeRegistrosProcAte
				,pesquisaCliente
	,queryOrders);
			setFullResult(result);

		} catch (DAOFactoryException e) {
			throw new ValueListHandlerException("Não foi possível construir um DAO para :"+getModuleId(),e);
		} catch (DAOException e) {
			throw new ValueListHandlerException(e);
		} catch (BaseTechnicalError e) {
			throw new ValueListHandlerException("Erro técnico não esperado",e);
		}
		if(logger.isDebugEnabled()){
			logger.debug("... pesquisa executada com sucesso!");
		}
	}

}