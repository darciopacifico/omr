/**
 * Gerado por XDoclet, não edite!!
 **/
package br.com.dlp.validador.cliente;

import java.rmi.RemoteException;
import java.util.List;

import br.com.dlp.framework.dao.DAOException;
import br.com.dlp.framework.dao.DAOFactoryException;
import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.facade.ValueListHandlerException;
import br.com.dlp.jazzqa.util.JazzQAConstants;

/**
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 *
 * Implementação da fachada ClienteVLHFacade
 **/
public class ClienteVLHFacadeImpl extends br.com.dlp.jazzqa.base.AbstractJazzQAVLHFacadeImpl implements ClienteVLHFacade {

	/**
	 *Indica ao framework qual o id do Módulo
	 */
	public String getModuleId() {
		return JazzQAConstants.MODULE_CLIENTE;
	}  

	/**
	 *Indica ao framework qual o id do VLH
	 */
	public String getModuleVLHId() {
    	return JazzQAConstants.MODULE_CLIENTE_VLH;
	}

	/**
	 * Atualiza as paginas do VLH com esta pesquisa
	 */
	public void executeFindClienteVO(
				Integer pesquisaId
				,java.lang.String pesquisaCnpj
				,java.lang.String pesquisaNome
		) throws ValueListHandlerException, RemoteException{

	executeFindClienteVO(
					pesquisaId
					,pesquisaCnpj
					,pesquisaNome
		, new QueryOrder[]{});

	}

	/**
	 * Atualiza as paginas do VLH com esta pesquisa
	 */
	public void executeFindClienteVO(
				Integer pesquisaId
				,java.lang.String pesquisaCnpj
				,java.lang.String pesquisaNome
	, QueryOrder[] queryOrders) throws ValueListHandlerException, RemoteException{

		if(logger.isDebugEnabled()){
			logger.debug("executando pesquisa pesquisando... ");
		}

		ClienteDAO dao;
		try {
			dao = (ClienteDAO)getDAO();
			List result = dao.findClienteVO(
				pesquisaId
				,pesquisaCnpj
				,pesquisaNome
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