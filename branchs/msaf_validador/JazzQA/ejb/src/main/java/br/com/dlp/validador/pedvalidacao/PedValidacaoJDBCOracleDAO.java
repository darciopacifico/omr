/**
 * Gerado por XDoclet, não edite!!
 **/
package br.com.dlp.validador.pedvalidacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import br.com.dlp.framework.dao.DAOException;
import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.pk.IPK;
import br.com.dlp.framework.vo.ICadastroBaseVO;
import br.com.dlp.validador.cliente.ClientePK;

/**
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 *
 * Implementação de PedValidacaoDAO para JDBC Oracle
 *
 **/
public class PedValidacaoJDBCOracleDAO 
	extends br.com.dlp.jazzqa.base.AbstractJazzQAJDBCDAO 
	implements PedValidacaoDAO{

	/**
	 * Implementacao de findPedValidacaoVO para JDBC Oracle
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
	,
		QueryOrder[] queryOrders	
		) throws DAOException, BaseTechnicalError{
		StringBuffer sbQuery = new StringBuffer();
    List list;
    if(logger.isDebugEnabled()){
	    logger.debug("Montando a query...");
    }
    /*TODO: ESCREVA AQUI A QUERY DESTA PESQUISA CONTEMPLANDO: 
     * - CAMPOS RETORNADOS 
     * - TABELAS ENVOLVIDAS 
     * - CLAUSULA INNER JOINS (SE NECESSÁRIO)
     * - CLAUSULA WHERE (SE NECESSÁRIO)
     * - CLAUSULA ORDER BY (SE NECESSÁRIO)
     * - CLAUSULA GROUP BY (SE NECESSÁRIO)
     * */ 
    sbQuery.append("");
    Connection connection = getConnection();
    PreparedStatement preparedStatement=null;
    ResultSet resultSet=null;
    String query = new String(sbQuery);
    try {
	  if(logger.isDebugEnabled()){
	      logger.debug("Criando PreparedStatement...");
	  }
      preparedStatement = connection.prepareStatement(query);
	    //preparedStatement.setLong(0, clienteVO.getId());
	    //preparedStatement.setString(1, clienteVO.getNome());
      //TODO: CONTEMPLAR AS DECISÕES DE ORDENAÇÃO
      if(logger.isDebugEnabled()){
	      logger.debug("Executando query...");
      }
      resultSet = preparedStatement.executeQuery();
      if(logger.isDebugEnabled()){
	      logger.debug("Populando Colecao de VOs para retorno...");
      }
      list = getCadastroBaseVOs(resultSet);
    } catch (SQLException e) {
      throw new DAOException("Não foi possivel executar a query:"+query,e);
    }finally{
      closeConnection(connection, preparedStatement, resultSet);
    }
    if(logger.isDebugEnabled()){
	    logger.debug("...find executado com sucesso!");
    }

    return list;

	}
	/**
	 * Implementacao de findPedValidacaoVO para JDBC Oracle
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
		) throws DAOException, BaseTechnicalError{
		StringBuffer sbQuery = new StringBuffer();
    List list;
    logger.debug("Montando a query...");
    /*TODO: ESCREVA AQUI A QUERY DESTA PESQUISA CONTEMPLANDO: 
     * - CAMPOS RETORNADOS 
     * - TABELAS ENVOLVIDAS 
     * - CLAUSULA INNER JOINS (SE NECESSÁRIO)
     * - CLAUSULA WHERE (SE NECESSÁRIO)
     * - CLAUSULA ORDER BY (SE NECESSÁRIO)
     * - CLAUSULA GROUP BY (SE NECESSÁRIO)
     * */ 
    sbQuery.append("");
    Connection connection = getConnection();
    PreparedStatement preparedStatement=null;
    ResultSet resultSet=null;
    String query = new String(sbQuery);
    try {
      if(logger.isDebugEnabled()){
	      logger.debug("Criando PreparedStatement...");
      }
      preparedStatement = connection.prepareStatement(query);
	    //preparedStatement.setLong(0, clienteVO.getId());
	    //preparedStatement.setString(1, clienteVO.getNome());

      logger.debug("Executando query...");
      resultSet = preparedStatement.executeQuery();
      if(logger.isDebugEnabled()){
	      logger.debug("Populando Colecao de VOs para retorno...");
      }
      list = getCadastroBaseVOs(resultSet);
    } catch (SQLException e) {
      throw new DAOException("Não foi possivel executar a query:"+query,e);
    }finally{
      closeConnection(connection, preparedStatement, resultSet);
    }

    logger.debug("...find executado com sucesso!");
    return list;
	}	

	/* (non-Javadoc)
	 * @see br.com.dlp.framework.dao.IDAO#findAll()
	 */
	public List findAll() throws DAOException, BaseTechnicalError {
		StringBuffer sbQuery = new StringBuffer();
    List list;
    if(logger.isDebugEnabled()){
	    logger.debug("Montando a query...");
    }
    /*TODO: ESCREVA AQUI A QUERY DESTA PESQUISA CONTEMPLANDO: 
     * - CAMPOS RETORNADOS 
     * - TABELAS ENVOLVIDAS 
     * - CLAUSULA INNER JOINS (SE NECESSÁRIO)
     * - CLAUSULA WHERE (SE NECESSÁRIO)
     * - CLAUSULA ORDER BY (SE NECESSÁRIO)
     * - CLAUSULA GROUP BY (SE NECESSÁRIO)
     * */ 
    sbQuery.append("");
    Connection connection = getConnection();
    PreparedStatement preparedStatement=null;
    ResultSet resultSet=null;
    String query = new String(sbQuery);
    try {
      logger.debug("Criando PreparedStatement...");
      preparedStatement = connection.prepareStatement(query);
      if(logger.isDebugEnabled()){
	      logger.debug("Executando query...");
      }
      resultSet = preparedStatement.executeQuery();
      logger.debug("Populando Colecao de VOs para retorno...");
      list = getCadastroBaseVOs(resultSet);
    } catch (SQLException e) {
      throw new DAOException("Não foi possivel executar a query:"+query,e);
    }finally{
      closeConnection(connection, preparedStatement, resultSet);
    }
	if(logger.isDebugEnabled()){
    	logger.debug("...find executado com sucesso!");
	}

    return list;

	}
	/* (non-Javadoc)
	 * @see br.com.dlp.framework.dao.IDAO#findAll(br.com.dlp.framework.dao.QueryOrder[])
	 */
	public List findAll(QueryOrder[] queryOrders) throws DAOException,	BaseTechnicalError {
		StringBuffer sbQuery = new StringBuffer();
    List list;
    logger.debug("Montando a query...");
    /*TODO: ESCREVA AQUI A QUERY DESTA PESQUISA CONTEMPLANDO: 
     * - CAMPOS RETORNADOS 
     * - TABELAS ENVOLVIDAS 
     * - CLAUSULA INNER JOINS (SE NECESSÁRIO)
     * - CLAUSULA WHERE (SE NECESSÁRIO)
     * - CLAUSULA ORDER BY (SE NECESSÁRIO)
     * - CLAUSULA GROUP BY (SE NECESSÁRIO)
     * */ 
    sbQuery.append("");
    Connection connection = getConnection();
    PreparedStatement preparedStatement=null;
    ResultSet resultSet=null;
    String query = new String(sbQuery);
    try {
      if(logger.isDebugEnabled()){
	      logger.debug("Criando PreparedStatement...");
      }
      preparedStatement = connection.prepareStatement(query);
      //TODO: CONTEMPLAR AS DECISÕES DE ORDENAÇÃO
      logger.debug("Executando query...");
      resultSet = preparedStatement.executeQuery();
      if(logger.isDebugEnabled()){
	      logger.debug("Populando Colecao de VOs para retorno...");
      }
      list = getCadastroBaseVOs(resultSet);
    } catch (SQLException e) {
      throw new DAOException("Não foi possivel executar a query:"+query,e);
    } finally{
      closeConnection(connection, preparedStatement, resultSet);
    }

    logger.debug("...find executado com sucesso!");
    return list;
	}

	/**
	 * Implementação de IDAO.inserir() para JDBC Oracle
	 */
	public ICadastroBaseVO inserir(ICadastroBaseVO baseVO) throws DAOException,BaseTechnicalError {

    Connection connection = getConnection();
    if(logger.isDebugEnabled()){
	    logger.debug("Iniciando inclusão de PedValidacaoVO!");
    }

    //TODO: Escreva aqui seu Statement de inserção...
    StringBuffer insertQuery = new StringBuffer("insert into PedValidacaoVO (id, nome, ....) values (?,?, ...)");
    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement(new String(insertQuery));

      //preparedStatement.setLong(0, moduleVO.getId());
      //preparedStatement.setString(1, moduleVO.getNome());

      int registrosAfetados = preparedStatement.executeUpdate();

      logger.debug("Inclusão efetuada (" + registrosAfetados + " registros)!");

    } catch (SQLException e) {
      throw new DAOException("Erro ao tentar incluir o VO " + baseVO, e);

    } finally {
      closeConnection(connection, preparedStatement);
    }

    /* invoca atualizacao do VO */
    baseVO = updateVO(baseVO);

    return baseVO;

	}

	/** 
	 * Implementação de IDAO.atualizar() para JDBC Oracle
	 */
	public ICadastroBaseVO atualizar(ICadastroBaseVO baseVO) throws DAOException, BaseTechnicalError {

    Connection connection = getConnection();
    if(logger.isDebugEnabled()){
	    logger.debug("Iniciando atualização de PedValidacaoVO!");
    }

    //TODO: Escreva aqui seu Statement de atualizacao...
    StringBuffer updateQuery = new StringBuffer("update PedValidacaoVO set nome=?,... where id=?");
    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement(new String(updateQuery));

      //preparedStatement.setString(0, moduleVO.getNome());
      //preparedStatement.setLong(1, moduleVO.getId());

      int registrosAfetados = preparedStatement.executeUpdate();

      logger.debug("Atualizacao efetuada (" + registrosAfetados + " registros)!");

    } catch (SQLException e) {
      throw new DAOException("Erro ao tentar atualizar o VO " + baseVO, e);

    } finally {
      closeConnection(connection, preparedStatement);
    }

    /* invoca atualizacao do VO */
    baseVO = updateVO(baseVO);

    return baseVO;

	}

	/**
	 * Implementacao de IDAO.excluir() para JDBC Oracle
	 */
	public void excluir(ICadastroBaseVO baseVO) throws DAOException, BaseTechnicalError {

    Connection connection = getConnection();

	if(logger.isDebugEnabled()){
	    logger.debug("Iniciando exclusão de PedValidacaoVO!");
	}

    //TODO: Escreva aqui seu Statement de atualizacao...
    StringBuffer deleteQuery = new StringBuffer("delete from PedValidacaoVO where id=?");
    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement(new String(deleteQuery));
      //preparedStatement.setLong(), moduleVO.getId());

      int registrosAfetados = preparedStatement.executeUpdate();

      logger.debug("Exclusao efetuada (" + registrosAfetados + " registros)!");

    } catch (SQLException e) {
      throw new DAOException("Erro ao tentar deletar o VO " + baseVO, e);

    } finally {
      closeConnection(connection, preparedStatement);
    }
	}
	/**
	 * Abstracao da classe do VO deste DAO
	 **/
	protected Class getMappedClass(){
		return PedValidacaoVO.class;
	}

  public ICadastroBaseVO findByPrimaryKey(IPK chave) throws DAOException, BaseTechnicalError {
    //return (ICadastroBaseVO) findById((Long)chave);
    throw new BaseTechnicalError("Este método ainda precisa ser implementado!");
  }

	/**
	 * Recupera o VO preenchido pela camada de abstração 
	 * superior e popula a com sua parte dos campos.
	 */
  protected ICadastroBaseVO getCadastroBaseVO(ResultSet rs) throws DAOException {
    PedValidacaoVO pedValidacaoVO = (PedValidacaoVO) super.getCadastroBaseVO(rs);
    /*popule o objeto pedValidacaoVO com os dados do ResultSet rs */
    return pedValidacaoVO;
  }
}

