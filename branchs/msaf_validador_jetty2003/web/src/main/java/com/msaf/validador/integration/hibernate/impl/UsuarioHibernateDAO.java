package com.msaf.validador.integration.hibernate.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.msaf.validador.consultaonline.exceptions.ValidadorLoginException;
import com.msaf.validador.core.dominio.UsuarioVO;
import com.msaf.validador.integration.hibernate.base.DAOGenericoHibernate;
import com.msaf.validador.integration.hibernate.intf.IUsuarioDAO;

public class UsuarioHibernateDAO extends DAOGenericoHibernate<UsuarioVO, Long> implements IUsuarioDAO {
	Logger log = Logger.getLogger(UsuarioHibernateDAO.class);
	
	@Transactional()
	public UsuarioVO validaLoginSenha(final UsuarioVO usuarioVO) {
		return validarUsusarioLoginSenha(getEntityManager(), usuarioVO);
	}

	private UsuarioVO validarUsusarioLoginSenha(EntityManager entityManager, UsuarioVO usuarioVO) {
		
		Query query = entityManager.createQuery("select u from UsuarioVO u where " +
				" u.login = :login and " +
				" u.senha = :senha ");

		query.setParameter("login", usuarioVO.getLogin());
		query.setParameter("senha", usuarioVO.getSenha());

		UsuarioVO  usuarioVO2=null;
		
		try{
			List<UsuarioVO> listUsuario = query.getResultList();
			usuarioVO2 = listUsuario.get(0);
		}catch (NoResultException e) {
			log.warn("Nenhum usuario encontrado!",e);
		}

		return usuarioVO2;
	}

	/**
	 * Consulta usuario pelo userName informado. Normalmente utilizado para
	 * carregar informações do usuário após login com JAAS
	 */
	@Transactional()
	public UsuarioVO findByUserName(String userName) throws ValidadorLoginException {
		EntityManager entityManager = getEntityManager();
		Query query = entityManager.createQuery("select u from UsuarioVO u where u.login = :login ");

		query.setParameter("login", userName);

		UsuarioVO  usuarioVO2=null;
		
		try{
			List<UsuarioVO> listUsuario = query.getResultList();
			if(listUsuario!=null && listUsuario.size()>0){
				usuarioVO2 = listUsuario.get(0);
			}
			
		}catch (NoResultException e) {
			log.warn("Nenhum usuario encontrado!",e);
		}

		return usuarioVO2;
	}

	
	
	

}
