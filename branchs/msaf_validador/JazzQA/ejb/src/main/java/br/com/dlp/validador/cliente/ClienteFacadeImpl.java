/**
 * Gerado por XDoclet, n�o edite!!
 **/
package br.com.dlp.validador.cliente;

import java.rmi.RemoteException;
import java.util.List;

import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.exception.BaseBusinessException;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.vo.ICadastroBaseVO;
import br.com.dlp.jazzqa.util.JazzQAConstants;

/**
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 *

 * Implementa��o da fachada ClienteFacade

 *
 **/
public class ClienteFacadeImpl 
extends    br.com.dlp.jazzqa.base.AbstractJazzQAFacadeImpl 
implements ClienteFacade {

	/**
	 * Pesquisa gen�rica 
	 */
	public List findClienteVO(
				Integer pesquisaId
				,java.lang.String pesquisaCnpj
				,java.lang.String pesquisaNome
		)throws BaseBusinessException, BaseTechnicalError, RemoteException{

			List results = findClienteVO(
						pesquisaId
						,pesquisaCnpj
						,pesquisaNome
			, new QueryOrder[]{});

			return results;
		}
	/**
	 * Pesquisa gen�rica com Ordena��o
	 */
	public List findClienteVO(
				Integer pesquisaId
				,java.lang.String pesquisaCnpj
				,java.lang.String pesquisaNome
	, QueryOrder[] queryOrders
		)throws BaseBusinessException, BaseTechnicalError, RemoteException{
			if(logger.isDebugEnabled()){
				logger.debug("executando pesquisa... ");
			}

			ClienteDAO dao;
				dao = (ClienteDAO)getDAO();
				List result = dao.findClienteVO(
					pesquisaId
					,pesquisaCnpj
					,pesquisaNome
		,queryOrders);
			if(logger.isDebugEnabled()){
				logger.debug("... pesquisa executada com sucesso!");
			}
			return result;

	}

/* finders para relacionamentos */

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
   * Retorna o objeto Class deste m�dulo (Cliente)
   */
	protected Class getVOClass() throws BaseBusinessException, BaseTechnicalError {
		return ClienteVO.class;
	}

	/**
	 *Indica ao framework qual o id do modulo atual
	 */
	public String getModuleId() throws BaseTechnicalError {
    return JazzQAConstants.MODULE_CLIENTE;
	}

}

