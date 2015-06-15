/**
 * Gerado por XDoclet, não edite!!
 **/
package br.com.dlp.validador.cliente;

import java.util.List;

import br.com.dlp.framework.dao.DAOException;
import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.exception.BaseTechnicalError;

/**
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 *
 * Contrado de DAO para o componente Cliente
 *
 **/
public interface ClienteDAO extends br.com.dlp.framework.dao.IDAO {

	/**
	 * Contrato para pesquisa findClienteVO
	 */
	public List findClienteVO  (
				Integer pesquisaId
				,java.lang.String pesquisaCnpj
				,java.lang.String pesquisaNome
	)  throws DAOException, BaseTechnicalError;
	/**
	 * Contrato para pesquisa findClienteVO
	 */
	public List findClienteVO  (
				Integer pesquisaId
				,java.lang.String pesquisaCnpj
				,java.lang.String pesquisaNome
	, QueryOrder[] queryOrders )  throws DAOException, BaseTechnicalError;

}

