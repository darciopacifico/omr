/**
 * Gerado por XDoclet, não edite!!
 **/
package br.com.dlp.validador.pedvalidacao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

import br.com.dlp.framework.dao.DAOException;
import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.pk.IPK;
import br.com.dlp.framework.servicelocator.ServiceLocatorException;
import br.com.dlp.framework.util.FrameworkAcheUtil;
import br.com.dlp.framework.vo.ICadastroBaseVO;
import br.com.dlp.validador.cliente.ClientePK;

/**
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 *
 * Implementação de PedValidacaoDAO para HibernateDAO
 *
 **/
public class PedValidacaoHibernateDAO 
	extends br.com.dlp.jazzqa.base.AbstractJazzQAHibernateDAO 
	implements PedValidacaoDAO{

	public Class getMappedClass() {
		return PedValidacaoVO.class;
	}

	public ICadastroBaseVO findByPrimaryKey(IPK chave) throws DAOException, BaseTechnicalError {
    PedValidacaoPK modulePk = (PedValidacaoPK) chave;
		Class voClass = getMappedClass();
		if(logger.isDebugEnabled()){
			logger.debug("EXECUTANDO findByPrimaryKey... ");
		}
		Session session = null;
		ICadastroBaseVO cadastroBaseVO;
		try {
			session = getHibernateSession();
			Criteria criteria = session.createCriteria(voClass);

			criteria.add(Expression.eq( "id",modulePk.getId()));

			cadastroBaseVO = (ICadastroBaseVO) criteria.uniqueResult();
		} catch (HibernateException e) {
			throw new DAOException("Não foi possível executar o findByPrimaryKey para a classe "+voClass,e);
		} catch (ServiceLocatorException e) {
			throw new DAOException(e);
		} catch (Exception e) {
			throw new DAOException(e);
		}finally{
			closeSession(session);
		}
		if(logger.isDebugEnabled()){
			logger.debug("... findByPrimaryKey executado com sucesso !!");
		}
		return cadastroBaseVO;
  }

	/**
	 * Implementacao de findPedValidacaoVO para Hibernate
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
		Class voClass = getMappedClass();
		if(logger.isDebugEnabled()){
			logger.debug("EXECUTANDO findPedValidacaoVO... ");
		}
		Session session = null;
		List list = new ArrayList();
		try {
			session = getHibernateSession();
			Criteria criteria = session.createCriteria(voClass);

			if(!FrameworkAcheUtil.isNullOrEmptyOrZero(pesquisaId)){
				criteria.add(Expression.eq( "id",pesquisaId));
			}
			if(!FrameworkAcheUtil.isNullOrEmptyOrZero(pesquisaDataDownloadDe)){
				criteria.add(Expression.ge( "dataDownload",pesquisaDataDownloadDe));
			}
			if(!FrameworkAcheUtil.isNullOrEmptyOrZero(pesquisaDataDownloadAte)){
				criteria.add(Expression.le( "dataDownload",pesquisaDataDownloadAte));
			}
			if(!FrameworkAcheUtil.isNullOrEmptyOrZero(pesquisaDataSolicitacaoDe)){
				criteria.add(Expression.ge( "dataSolicitacao",pesquisaDataSolicitacaoDe));
			}
			if(!FrameworkAcheUtil.isNullOrEmptyOrZero(pesquisaDataSolicitacaoAte)){
				criteria.add(Expression.le( "dataSolicitacao",pesquisaDataSolicitacaoAte));
			}
			if(!FrameworkAcheUtil.isNullOrEmptyOrZero(pesquisaDataTerminoDe)){
				criteria.add(Expression.ge( "dataTermino",pesquisaDataTerminoDe));
			}
			if(!FrameworkAcheUtil.isNullOrEmptyOrZero(pesquisaDataTerminoAte)){
				criteria.add(Expression.le( "dataTermino",pesquisaDataTerminoAte));
			}
			if(!FrameworkAcheUtil.isNullOrEmptyOrZero(pesquisaQtdeRegistrosArqDe)){
				criteria.add(Expression.ge( "qtdeRegistrosArq",pesquisaQtdeRegistrosArqDe));
			}
			if(!FrameworkAcheUtil.isNullOrEmptyOrZero(pesquisaQtdeRegistrosArqAte)){
				criteria.add(Expression.le( "qtdeRegistrosArq",pesquisaQtdeRegistrosArqAte));
			}
			if(!FrameworkAcheUtil.isNullOrEmptyOrZero(pesquisaQtdeRegistrosProcDe)){
				criteria.add(Expression.ge( "qtdeRegistrosProc",pesquisaQtdeRegistrosProcDe));
			}
			if(!FrameworkAcheUtil.isNullOrEmptyOrZero(pesquisaQtdeRegistrosProcAte)){
				criteria.add(Expression.le( "qtdeRegistrosProc",pesquisaQtdeRegistrosProcAte));
			}
			if(!FrameworkAcheUtil.isNullOrEmptyOrZero(pesquisaCliente)){
				criteria.add(Expression.eq( "clienteVO.id",pesquisaCliente.getId()));
			}
			configureOrderCriteria(criteria,queryOrders);
			list = criteria.list();
		} catch (HibernateException e) {
			throw new DAOException("Não foi possível executar o findPedValidacaoVO para a classe "+voClass,e);
		} catch (ServiceLocatorException e) {
			throw new DAOException(e);
		} catch (Exception e) {
			throw new DAOException(e);
		}finally{
			closeSession(session);
		}
		if(logger.isDebugEnabled()){
			logger.debug("... findPedValidacaoVO executado com sucesso ("+list.size()+" registros)!!");
		}
		return list;
	}
	/**
	 * Implementacao de findPedValidacaoVO para Hibernate
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
		List list = findPedValidacaoVO(
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
	,new QueryOrder[]{}
		);
		return list;
	}	
}