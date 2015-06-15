/**
 * Gerado por XDoclet, não edite!!
 **/
package br.com.dlp.validador.pedvalidacao;

import java.rmi.RemoteException;
import java.util.List;

import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.vo.ICadastroBaseVO;
import br.com.dlp.jazzqa.util.JazzQAConstants;
import br.com.dlp.validador.cliente.ClienteFacade;
import br.com.dlp.validador.cliente.ClientePK;

/**
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 *

 * Implementação da fachada PedValidacaoFacade

 *
 **/
public class PedValidacaoFacadeImpl 
extends    br.com.dlp.jazzqa.base.AbstractJazzQAFacadeImpl 
implements PedValidacaoFacade {

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
		)throws BaseBusinessException, BaseTechnicalError, RemoteException{

			List results = findPedValidacaoVO(
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

			return results;
		}
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
		)throws BaseBusinessException, BaseTechnicalError, RemoteException{
			if(logger.isDebugEnabled()){
				logger.debug("executando pesquisa... ");
			}

			PedValidacaoDAO dao;
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
			if(logger.isDebugEnabled()){
				logger.debug("... pesquisa executada com sucesso!");
			}
			return result;

	}

/* finders para relacionamentos */

	public List findAllCliente() throws BaseBusinessException, BaseTechnicalError, RemoteException{
		/* Pesquisa todas as ocorrências de Cliente*/
		/* Repassa para o componente ClienteFacade */
		ClienteFacade clienteFacade = (ClienteFacade)getFacade(JazzQAConstants.MODULE_CLIENTE); 
			List list = clienteFacade.findAll();

		return list;
	}

  /** Override do metodo validacoesDeNegocioAlterar da superclasse */
  public void validacoesDeNegocioAlterar(ICadastroBaseVO baseVO) throws BaseBusinessException{
  }
  /** Override do metodo validacoesDeNegocioExcluir da superclasse */
  public void validacoesDeNegocioExcluir(ICadastroBaseVO baseVO) throws BaseBusinessException{
  }
  /** Override do metodo validacoesDeNegocioExcluir da superclasse */
  public void validacoesDeNegocioExcluir(Object id) throws BaseBusinessException{
  }
  /** Override do metodo validacoesDeNegocioIncluir da superclasse */
  public void validacoesDeNegocioIncluir(ICadastroBaseVO baseVO) throws BaseBusinessException{
  }
  /** Override do metodo validacoesDeNegocioIncluirAlterar da superclasse */
  public void validacoesDeNegocioIncluirAlterar(ICadastroBaseVO id) throws BaseBusinessException{
  }

  /**
   * Retorna o objeto Class deste módulo (PedValidacao)
   */
	protected Class getVOClass() throws BaseBusinessException, BaseTechnicalError {
		return PedValidacaoVO.class;
	}

	/**
	 *Indica ao framework qual o id do modulo atual
	 */
	public String getModuleId() throws BaseTechnicalError {
    return JazzQAConstants.MODULE_PEDVALIDACAO;
	}

}

