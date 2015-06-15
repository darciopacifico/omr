/**
 * Gerado por XDoclet, não edite!!
 **/
package br.com.dlp.validador.cliente;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;

import br.com.dlp.framework.dao.DAOException;
import br.com.dlp.framework.dao.QueryOrder;
import br.com.dlp.framework.exception.BaseTechnicalError;
import br.com.dlp.framework.pk.IPK;
import br.com.dlp.framework.servicelocator.ServiceLocatorException;
import br.com.dlp.framework.util.FrameworkAcheUtil;
import br.com.dlp.framework.vo.ICadastroBaseVO;

/**
 * Gerado por Tags XDoclet do projeto Wizard J2EE<br>
 * Propriedade intelectual de Darcio L Pacifico<br><br>
 *
 * Implementação de ClienteDAO para HibernateDAO
 *
 **/
public class ClienteHibernateDAO 
	extends br.com.dlp.jazzqa.base.AbstractJazzQAHibernateDAO 
	implements ClienteDAO{

	public Class getMappedClass() {
		return ClienteVO.class;
	}

	public ICadastroBaseVO findByPrimaryKey(IPK chave) throws DAOException, BaseTechnicalError {
    ClientePK modulePk = (ClientePK) chave;
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
	 * Implementacao de findClienteVO para Hibernate
	 */
	public List findClienteVO(
				Integer pesquisaId
				,java.lang.String pesquisaCnpj
				,java.lang.String pesquisaNome
	,
		QueryOrder[] queryOrders	
		) throws DAOException, BaseTechnicalError{
		Class voClass = getMappedClass();
		if(logger.isDebugEnabled()){
			logger.debug("EXECUTANDO findClienteVO... ");
		}
		Session session = null;
		List list = new ArrayList();
		try {
			session = getHibernateSession();
			Criteria criteria = session.createCriteria(voClass);

			if(!FrameworkAcheUtil.isNullOrEmptyOrZero(pesquisaId)){
				criteria.add(Expression.eq( "id",pesquisaId));
			}
			if(!FrameworkAcheUtil.isNullOrEmptyOrZero(pesquisaCnpj)){
				criteria.add(Expression.like( "cnpj",pesquisaCnpj,MatchMode.START).ignoreCase());
			}
			if(!FrameworkAcheUtil.isNullOrEmptyOrZero(pesquisaNome)){
				criteria.add(Expression.like( "nome",pesquisaNome,MatchMode.START).ignoreCase());
			}
			configureOrderCriteria(criteria,queryOrders);
			list = criteria.list();
		} catch (HibernateException e) {
			throw new DAOException("Não foi possível executar o findClienteVO para a classe "+voClass,e);
		} catch (ServiceLocatorException e) {
			throw new DAOException(e);
		} catch (Exception e) {
			throw new DAOException(e);
		}finally{
			closeSession(session);
		}
		if(logger.isDebugEnabled()){
			logger.debug("... findClienteVO executado com sucesso ("+list.size()+" registros)!!");
		}
				
		return list;
	}
	/**
	 * Implementacao de findClienteVO para Hibernate
	 */
	public List findClienteVO(
				Integer pesquisaId
				,java.lang.String pesquisaCnpj
				,java.lang.String pesquisaNome
		) throws DAOException, BaseTechnicalError{
		List list = findClienteVO(
				pesquisaId
				,pesquisaCnpj
				,pesquisaNome
	,new QueryOrder[]{}
		);
		return list;
	}	
}