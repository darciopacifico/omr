/**
 * Gerado por XDoclet, não edite!!
 **/
package br.com.dlp.framework.mdb.pool;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import br.com.dlp.framework.dao.DAOException;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.mdb.base.AbstractMDBJDBCDAO;
import br.com.dlp.framework.mdb.util.MDBConstants;
import br.com.dlp.framework.servicelocator.IServiceLocator;

public class MDBJDBCOracleDAO extends AbstractMDBJDBCDAO {

	public Set findByUserID(String userID) throws DAOException,
			BaseTechnicalError {

		HashSet mdbVOs = new HashSet();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {

			connection = getConnection();

			if (logger.isDebugEnabled()) {
				logger.debug("Iniciando recuperacao de filas de um usuario!");
			}

			StringBuffer selectQuery = new StringBuffer(
					"SELECT BATCHID, USERID, DTSOLICITACAO, DESCRICAO, TIPO, STATUS, DTEXECUCAO, BINARIO mensagem "
							+ "FROM POOL_PROCESSOSBATCH "
							+ "WHERE REGISTRO='mensagem' AND USERID=? ");

			preparedStatement = connection.prepareStatement(new String(
					selectQuery));

			preparedStatement.setString(1, userID);

			resultSet = preparedStatement.executeQuery();

			if (logger.isDebugEnabled()) {
				logger.debug("Consulta efetuada !");
			}

			while (resultSet.next()) {

				MDBVO mdbVO = new MDBVO();

				mdbVO.setId(resultSet.getString(1));
				mdbVO.setUsuarioId(resultSet.getString(2));
				mdbVO.setDtSolic(resultSet.getTimestamp(3).getTime());
				mdbVO.setDesc(resultSet.getString(4));
				mdbVO.setTipo(resultSet.getString(5));
				mdbVO.setStatus(resultSet.getString(6));
				if (resultSet.getTimestamp(7) != null) {
					mdbVO.setDtExec(resultSet.getTimestamp(7).getTime());
				}

				byte[] arrayDados = null;

				BufferedInputStream bis = new BufferedInputStream(resultSet
						.getBinaryStream("mensagem"));
				ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);

				byte bindata[] = new byte[2048];
				int bytesread = 0;

				if (!resultSet.wasNull()) {
					while ((bytesread = bis.read(bindata, 0, bindata.length)) != -1) {
						baos.write(bindata, 0, bytesread);
					}
					arrayDados = baos.toByteArray();
				}

				bis.close();

				if (arrayDados != null) {
					mdbVO.setMensagem(new String(arrayDados));
				} else {
					mdbVO.setMensagem("Não há mensagens");
				}

				mdbVOs.add(mdbVO);
			}

		} catch (SQLException e) {
			throw new DAOException(
					"Erro ao tentar incluir no pool de processamento assíncrono! ",
					e);

		} catch (IOException ex) {
			throw new DAOException("Erro ao tentar ler dados do Blob! ", ex);

		} finally {
			closeConnection(connection, preparedStatement);
		}

		return mdbVOs;
	}

	public void inserir(MDBMensagemVO mdbMensagem) throws DAOException,
			BaseTechnicalError {

		String[] tipos = { "parametros", "mensagem", "resultado" };

		ByteArrayOutputStream bos = null;
		ObjectOutput out = null;
		ByteArrayInputStream bais = null;

		byte[] arrPar = null;

		java.util.Date data = null;

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			data = mdbMensagem.getBatchSolicitacao();

			// Obter array de bytes e ByteArrayInputStream a partir dos
			// parâmetros na classe VO.
			bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);
			out.writeObject(mdbMensagem);
			arrPar = bos.toByteArray();
			bais = new ByteArrayInputStream(arrPar);
			out.close();

			connection = getConnection();

			if (logger.isDebugEnabled()) {
				logger.debug("Iniciando inclusão de uma fila no banco!");
			}

			for (int i = 0; i < tipos.length; i++) {

				StringBuffer insertQuery = new StringBuffer(
						"INSERT INTO POOL_PROCESSOSBATCH "
								+ "( BATCHID, USERID, DTSOLICITACAO, REGISTRO, DESCRICAO, TIPO, CONSUMIDOR, STATUS, "
								+ "  BINARIO ) "
								+ "VALUES ( ?, ?, to_date( ?,'yyyy-mm-dd hh24:mi:ss'), ?, ?, ?, ?, ?, ? ) ");

				preparedStatement = connection.prepareStatement(new String(
						insertQuery));

				preparedStatement.setString(1, mdbMensagem.getBatchId());
				preparedStatement.setString(2, mdbMensagem.getUsuarioId());
				preparedStatement.setString(3, new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss").format(data));
				preparedStatement.setString(4, tipos[i]);
				preparedStatement.setString(5, mdbMensagem.getBatchDesc());
				preparedStatement.setString(6, mdbMensagem.getBatchTipo());
				preparedStatement
						.setString(7, mdbMensagem.getBatchConsumidor());
				preparedStatement.setString(8, MDBConstants.MDBSTATUS_EMFILA);

				if (tipos[i].equals("parametros")) {
					preparedStatement.setBinaryStream(9, bais, arrPar.length);
				} else if (tipos[i].equals("mensagem")) {
					String mensagem = mdbMensagem.getMensagem();
					if (mensagem == null) {
						preparedStatement.setBinaryStream(9,
								new ByteArrayInputStream("Não há mensagens"
										.getBytes()), 16);
					} else {
						preparedStatement.setBinaryStream(9,
								new ByteArrayInputStream(mensagem.getBytes()),
								mensagem.length());
					}
				} else { // resultado
					preparedStatement.setBinaryStream(9,
							new ByteArrayInputStream("Vazio".getBytes()), 5);
				}

				preparedStatement.close();
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Inclusão efetuada!");
			}

		} catch (SQLException e) {
			throw new DAOException(
					"Erro ao tentar incluir no pool de processamento assíncrono! ",
					e);
		} catch (IOException e) {
			throw new DAOException(
					"Erro ao tentar converter dados para Blob! ", e);
		} finally {
			closeConnection(connection, preparedStatement);
			if (mdbMensagem.getMensagem() != null) {
				updateMensagem(mdbMensagem);
			}
		}

	}

	/**
	 * Recupera os parametros informados originalmente
	 */
	public MDBMensagemVO recuperar(String batchID, String userID,
			java.util.Date data) throws DAOException, BaseTechnicalError {

		MDBMensagemVO mdbMensagem = null;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {

			connection = getConnection();

			if (logger.isDebugEnabled()) {
				logger.debug("Iniciando recuperacao de uma fila no banco!");
			}

			StringBuffer selectQuery = new StringBuffer(
					"SELECT BINARIO parametros "
							+ "FROM POOL_PROCESSOSBATCH "
							+ "WHERE REGISTRO='parametros' AND "
							+ "BATCHID=? AND "
							+ "USERID=? AND "
							+ "to_char(DTSOLICITACAO,'yyyy-mm-dd hh24:mi:ss') = ? ");

			preparedStatement = connection.prepareStatement(new String(
					selectQuery));

			preparedStatement.setString(1, batchID);
			preparedStatement.setString(2, userID);
			preparedStatement.setString(3, new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(data));

			resultSet = preparedStatement.executeQuery();

			if (logger.isDebugEnabled()) {
				logger.debug("Consulta efetuada !");
			}

			Object obj = null;

			if (resultSet.next()) {
				byte[] arrayDados = null;

				BufferedInputStream bis = new BufferedInputStream(resultSet
						.getBinaryStream("parametros"));
				ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);

				byte bindata[] = new byte[2048];
				int bytesread = 0;

				if (!resultSet.wasNull()) {
					while ((bytesread = bis.read(bindata, 0, bindata.length)) != -1) {
						baos.write(bindata, 0, bytesread);
					}
					arrayDados = baos.toByteArray();
				}

				bis.close();

				ObjectInputStream in = new ObjectInputStream(
						new ByteArrayInputStream(arrayDados));
				obj = in.readObject();
				in.close();
			}
			mdbMensagem = (MDBMensagemVO) obj;

		} catch (SQLException e) {
			throw new DAOException(
					"Erro ao tentar incluir no pool de processamento assíncrono! ",
					e);

		} catch (IOException ex) {
			throw new DAOException("Erro ao tentar ler dados do Blob! ", ex);

		} catch (ClassNotFoundException e) {
			throw new DAOException("Erro ao tentar ler dados do Blob! ", e);
		} finally {
			closeConnection(connection, preparedStatement);
		}

		return mdbMensagem;
	}

	public Object recuperarResultado(String batchID, String userID,
			java.util.Date data) throws DAOException, BaseTechnicalError {

		Object obj = null;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {

			connection = getConnection();

			if (logger.isDebugEnabled()) {
				logger
						.debug("Iniciando recuperacao do resultado de uma fila no banco!");
			}

			StringBuffer selectQuery = new StringBuffer(
					"SELECT BINARIO resultado "
							+ "FROM POOL_PROCESSOSBATCH "
							+ "WHERE REGISTRO='resultado' AND "
							+ "BATCHID=? AND "
							+ "USERID=? AND "
							+ "to_char(DTSOLICITACAO,'yyyy-mm-dd hh24:mi:ss') = ? ");

			preparedStatement = connection.prepareStatement(new String(
					selectQuery));

			preparedStatement.setString(1, batchID);
			preparedStatement.setString(2, userID);
			preparedStatement.setString(3, new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(data));

			resultSet = preparedStatement.executeQuery();

			if (logger.isDebugEnabled()) {
				logger.debug("Consulta efetuada !");
			}

			if (resultSet.next()) {

				byte[] arrayDados = null;

				BufferedInputStream bis = new BufferedInputStream(resultSet
						.getBinaryStream("resultado"));
				ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);

				byte bindata[] = new byte[2048];
				int bytesread = 0;

				if (!resultSet.wasNull()) {
					while ((bytesread = bis.read(bindata, 0, bindata.length)) != -1) {
						baos.write(bindata, 0, bytesread);
					}
					arrayDados = baos.toByteArray();
				}

				bis.close();

				ObjectInputStream in = new ObjectInputStream(
						new ByteArrayInputStream(arrayDados));
				obj = in.readObject();
				in.close();
			}

		} catch (SQLException e) {
			throw new DAOException(
					"Erro ao tentar ler resultado do pool de processamento assíncrono! ",
					e);

		} catch (IOException ex) {
			throw new DAOException("Erro ao tentar ler dados do Blob! ", ex);

		} catch (ClassNotFoundException e) {
			throw new DAOException("Erro ao tentar ler dados do Blob! ", e);
		} finally {
			closeConnection(connection, preparedStatement);
		}

		return obj;
	}

	public MDBMensagemVO updateStatus(MDBMensagemVO mdbMensagem, String status)
			throws DAOException, BaseTechnicalError {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		java.util.Date data = null;

		try {

			connection = getConnection();

			if (logger.isDebugEnabled()) {
				logger.debug("Iniciando inclusão de uma fila no banco!");
			}

			StringBuffer updateQuery = new StringBuffer(
					"UPDATE POOL_PROCESSOSBATCH "
							+ "SET STATUS = ? "
							+ "WHERE USERID = ? AND  "
							+ "BATCHID = ? AND  "
							+ "to_char(DTSOLICITACAO,'yyyy-mm-dd hh24:mi:ss') = ? ");

			preparedStatement = connection.prepareStatement(new String(
					updateQuery));

			data = mdbMensagem.getBatchSolicitacao();

			preparedStatement.setString(1, status);
			preparedStatement.setString(2, mdbMensagem.getUsuarioId());
			preparedStatement.setString(3, mdbMensagem.getBatchId());
			preparedStatement.setString(4, new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(data));

			int registrosAfetados = preparedStatement.executeUpdate();

			if (logger.isDebugEnabled()) {
				logger.debug("Atualizacao efetuada (" + registrosAfetados
						+ " registros)!");
			}

		} catch (SQLException e) {
			throw new DAOException(
					"Erro ao tentar atualizar o Status do processo ", e);

		} finally {
			closeConnection(connection, preparedStatement);
		}

		return mdbMensagem;
	}

	public MDBMensagemVO updateDtExecucao(MDBMensagemVO mdbMensagem, Date data)
			throws DAOException, BaseTechnicalError {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		java.util.Date keyData = null;

		try {

			connection = getConnection();

			if (logger.isDebugEnabled()) {
				logger
						.debug("Iniciando update de data de execucao de uma fila no banco!");
			}

			StringBuffer updateQuery = new StringBuffer(
					"UPDATE POOL_PROCESSOSBATCH "
							+ "SET DTEXECUCAO = to_date( ?, 'yyyy-mm-dd hh24:mi:ss' ) "
							+ "WHERE USERID = ? AND  "
							+ "BATCHID = ? AND  "
							+ "to_char(DTSOLICITACAO,'yyyy-mm-dd hh24:mi:ss') = ? ");

			preparedStatement = connection.prepareStatement(new String(
					updateQuery));

			keyData = mdbMensagem.getBatchSolicitacao();

			preparedStatement.setString(1, new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(data));
			preparedStatement.setString(2, mdbMensagem.getUsuarioId());
			preparedStatement.setString(3, mdbMensagem.getBatchId());
			preparedStatement.setString(4, new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(keyData));

			int registrosAfetados = preparedStatement.executeUpdate();

			if (logger.isDebugEnabled()) {
				logger.debug("Atualizacao efetuada (" + registrosAfetados
						+ " registros)!");
			}

		} catch (SQLException e) {
			throw new DAOException(
					"Erro ao tentar atualizar a data de processamento do processo ",
					e);

		} finally {
			closeConnection(connection, preparedStatement);
		}
		return mdbMensagem;
	}

	/**
	 * Atualiza o texto da mensagem que é exibida no console
	 */
	public MDBMensagemVO updateMensagem(MDBMensagemVO mdbMensagem)
			throws DAOException, BaseTechnicalError {

		ByteArrayOutputStream bos = null;
		ObjectOutput out = null;
		ByteArrayInputStream bais = null;

		byte[] arrPar = null;

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		java.util.Date data = null;

		try {

			connection = getConnection();
			data = mdbMensagem.getBatchSolicitacao();

			// Obter array de bytes e ByteArrayInputStream a partir dos
			// parâmetros na classe VO.
			bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);
			out.writeObject(mdbMensagem.getMensagem());
			arrPar = bos.toByteArray();
			bais = new ByteArrayInputStream(arrPar);
			out.close();

			if (logger.isDebugEnabled()) {
				logger.debug("Iniciando alteração de uma fila no banco!");
			}

			StringBuffer updateQuery = new StringBuffer(
					"UPDATE POOL_PROCESSOSBATCH "
							+ "SET BINARIO = ? "
							+ "WHERE REGISTRO='mensagem' AND "
							+ "USERID = ? AND  "
							+ "BATCHID = ? AND  "
							+ "to_char(DTSOLICITACAO,'yyyy-mm-dd hh24:mi:ss') = ? ");

			preparedStatement = connection.prepareStatement(new String(
					updateQuery));

			preparedStatement.setBinaryStream(1, bais, arrPar.length);
			preparedStatement.setString(2, mdbMensagem.getUsuarioId());
			preparedStatement.setString(3, mdbMensagem.getBatchId());
			preparedStatement.setString(4, new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(data));

			int registrosAfetados = preparedStatement.executeUpdate();

			if (logger.isDebugEnabled()) {
				logger.debug("Atualizacao efetuada (" + registrosAfetados
						+ " registros)!");
			}

		} catch (SQLException e) {
			throw new DAOException(
					"Erro ao tentar atualizar a mensagem do processo ", e);
		} catch (IOException e) {
			throw new DAOException(
					"Erro ao tentar converter dados para Blob! ", e);
		} finally {
			closeConnection(connection, preparedStatement);
		}

		return mdbMensagem;
	}

	public MDBMensagemVO updateResultado(MDBMensagemVO mdbMensagem, Object obj)
			throws DAOException, BaseTechnicalError {

		ByteArrayOutputStream bos = null;
		ObjectOutput out = null;
		ByteArrayInputStream bais = null;
		byte[] arrPar = null;

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		java.util.Date data = null;

		try {

			connection = getConnection();
			data = mdbMensagem.getBatchSolicitacao();

			// Obter array de bytes e ByteArrayInputStream a partir dos
			// parâmetros na classe VO.
			bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			arrPar = bos.toByteArray();
			bais = new ByteArrayInputStream(arrPar);
			out.close();

			if (logger.isDebugEnabled()) {
				logger
						.debug("Iniciando update do resultado de uma fila no banco!");
			}

			String updateQuery = new String("UPDATE POOL_PROCESSOSBATCH "
					+ "SET BINARIO = ? " + "WHERE REGISTRO='resultado' AND "
					+ "BATCHID = ? AND  " + "USERID = ? AND  "
					+ "to_char(DTSOLICITACAO,'yyyy-mm-dd hh24:mi:ss') = ? ");

			preparedStatement = connection.prepareStatement(new String(
					updateQuery));

			int tamanho = arrPar.length;
			preparedStatement.setBinaryStream(1, bais, tamanho);
			preparedStatement.setString(2, mdbMensagem.getBatchId());
			preparedStatement.setString(3, mdbMensagem.getUsuarioId());
			preparedStatement.setString(4, new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(data));

			int registrosAfetados = preparedStatement.executeUpdate();

			if (logger.isDebugEnabled()) {
				logger.debug("Atualizacao efetuada (" + registrosAfetados
						+ " registros)!");
			}

		} catch (SQLException e) {
			throw new DAOException(
					"Erro ao tentar atualizar o resultado do processo ", e);
		} catch (IOException e) {
			throw new DAOException(
					"Erro ao tentar converter dados para Blob! ", e);

		} finally {
			closeConnection(connection, preparedStatement);
		}

		return mdbMensagem;
	}

	public void excluir(String batchID, String userID, java.util.Date data)
			throws DAOException, BaseTechnicalError {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {

			connection = getConnection();

			if (logger.isDebugEnabled()) {
				logger.debug("Iniciando eliminacao de uma fila no banco!");
			}

			StringBuffer selectQuery = new StringBuffer(
					"DELETE FROM POOL_PROCESSOSBATCH "
							+ "WHERE BATCHID=? AND "
							+ "USERID=? AND "
							+ "to_char(DTSOLICITACAO,'yyyy-mm-dd hh24:mi:ss') = ? AND "
							+ "(STATUS =? OR STATUS =?)");

			preparedStatement = connection.prepareStatement(new String(
					selectQuery));

			preparedStatement.setString(1, batchID);
			preparedStatement.setString(2, userID);
			preparedStatement.setString(3, new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(data));
			preparedStatement.setString(4, MDBConstants.MDBSTATUS_FINALIZADO);
			preparedStatement.setString(5, MDBConstants.MDBSTATUS_PROBLEMA);

			int registrosAfetados = preparedStatement.executeUpdate();

			if (logger.isDebugEnabled()) {
				logger.debug("Exclusao efetuada (" + registrosAfetados
						+ " registros)!");
			}

		} catch (SQLException e) {
			throw new DAOException(
					"Erro ao tentar excluir do pool de processamento assíncrono! ",
					e);

		} finally {
			closeConnection(connection, preparedStatement);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.dlp.framework.dao.IDAO#serServiceLocator(br.com.dlp.framework.servicelocator.IServiceLocator)
	 */
	public void serServiceLocator(IServiceLocator serviceLocator)
			throws DAOException {
		// TODO Auto-generated method stub

	}
}
