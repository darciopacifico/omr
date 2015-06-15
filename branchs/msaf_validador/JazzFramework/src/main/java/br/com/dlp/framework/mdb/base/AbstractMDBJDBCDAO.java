package br.com.dlp.framework.mdb.base;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.com.dlp.framework.dao.AbstractJDBCDAO;
import br.com.dlp.framework.dao.DAOException;
import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.mdb.pool.MDBMensagemVO;
import br.com.dlp.framework.mdb.util.servicelocator.MDBServiceLocator;
import br.com.dlp.framework.pk.IPK;
import br.com.dlp.framework.vo.ICadastroBaseVO;

/**
 * Implementação base de JDBCDAO para o sistema MDB
 */
public abstract class AbstractMDBJDBCDAO extends AbstractJDBCDAO {
	protected Log logger = LogFactory.getLog(this.getClass());

	/**
	 * DataSourceName
	 */
	protected String getDefaultDataSouceName() {

		return MDBServiceLocator.PROPERTIE_MDB_DS;
	}

	public abstract void inserir(MDBMensagemVO mdbMensagem)
			throws DAOException, BaseTechnicalError;

	public abstract MDBMensagemVO recuperar(String batchID, String userID,
			java.util.Date data) throws DAOException, BaseTechnicalError;

	public abstract Object recuperarResultado(String batchID, String userID,
			java.util.Date data) throws DAOException, BaseTechnicalError;

	public abstract MDBMensagemVO updateStatus(MDBMensagemVO mdbMensagems,
			String status) throws DAOException, BaseTechnicalError;

	public abstract MDBMensagemVO updateDtExecucao(MDBMensagemVO mdbMensagem,
			Date data) throws DAOException, BaseTechnicalError;

	public abstract MDBMensagemVO updateMensagem(MDBMensagemVO mdbMensagem)
			throws DAOException, BaseTechnicalError;

	public abstract MDBMensagemVO updateResultado(MDBMensagemVO mdbMensagem,
			Object obj) throws DAOException, BaseTechnicalError;

	public abstract Set findByUserID(String userID) throws DAOException,
			BaseTechnicalError;

	public abstract void excluir(String batchID, String userID,
			java.util.Date data) throws DAOException, BaseTechnicalError;

	// apenas para cumprir contrato, nunca são utilizadas :)
	protected Class getMappedClass() {
		return null;
	}

	public ICadastroBaseVO inserir(ICadastroBaseVO baseVO) throws DAOException,
			BaseTechnicalError {
		return null;
	}

	public ICadastroBaseVO atualizar(ICadastroBaseVO baseVO)
			throws DAOException, BaseTechnicalError {
		return null;
	}

	public void excluir(ICadastroBaseVO baseVO) throws DAOException,
			BaseTechnicalError {
	}

	public ICadastroBaseVO findByPrimaryKey(IPK chave) throws DAOException,
			BaseTechnicalError {
		return null;
	}

	public List findAll() throws DAOException, BaseTechnicalError {
		return null;
	}

	public List findAll(QueryOrder[] queryOrders) throws DAOException,
			BaseTechnicalError {
		return null;
	}
}
