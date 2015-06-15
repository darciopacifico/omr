package com.msaf.validador.consultaonline.dao.impl;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import com.msaf.validador.consultaonline.cliente.ClienteVO;
import com.msaf.validador.consultaonline.dao.ClienteDAO;
import com.msaf.validador.consultaonline.exceptions.PersistenciaException;



/**
 * source: ClienteDAO.java 
 * author: Ekler Paulino de Mattos
 * description: Responsible for gets data from Clients tables version: First
 * version creation date: 2009-02-09
 */
public class ClienteDAOImpl implements ClienteDAO {

	private EntityManager em;
	
	@PersistenceUnit
	private EntityManagerFactory emf;


    public ClienteDAOImpl() {
        try {
            setUp();
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    }


    @Resource
    /**
    * Resonsible for setup a Entity manager for a Cliente
    * @param 
    * @date 2009-02-09
    * @author Ekler Paulino de Mattos
    * @exception
    * @return 
    * @roseuid 49184F590220
    */
    private void setUp() throws Exception {
    	
        emf = Persistence.createEntityManagerFactory("persistenciaProject");
        em = emf.createEntityManager();
        
    }


	/* (non-Javadoc)
	 * @see persistencia.daos.ClienteDAO#criarCliente(persistencia.vos.ClienteVO)
	 */
	public void criarCliente(ClienteVO cliente) throws PersistenciaException{
		em.getTransaction().begin();
		em.persist(cliente);
		em.flush();
		em.getTransaction().commit();
	}

	
	/* (non-Javadoc)
	 * @see persistencia.daos.ClienteDAO#buscarPorId(java.lang.Long)
	 */
	public ClienteVO buscarPorId(Long id) throws PersistenciaException{
		return em.find(ClienteVO.class, id);
	}

	
	/* (non-Javadoc)
	 * @see persistencia.daos.ClienteDAO#atualizarCliente(persistencia.vos.ClienteVO)
	 */
	public void atualizarCliente(ClienteVO cliente) throws PersistenciaException{
		em.getTransaction().begin();
		em.merge(cliente);
		em.flush();
		em.getTransaction().commit();
	}

	/* (non-Javadoc)
	 * @see persistencia.daos.ClienteDAO#removerCliente(persistencia.vos.ClienteVO)
	 */
	public void removerCliente(ClienteVO cliente) throws PersistenciaException{
		em.getTransaction().begin();

		ClienteVO ClienteVOToRemove = buscarPorId(cliente.getIdCliente());

		em.remove(ClienteVOToRemove);
		em.flush();
		em.getTransaction().commit();
	}

	/* (non-Javadoc)
	 * @see persistencia.daos.ClienteDAO#buscarTodos()
	 */
	public List buscarTodos() throws PersistenciaException{
		Query query = em.createQuery("select a from ClienteVO a");
		List list = query.getResultList();
		return list;
	}

	

	
	/**
	 * Search a Cliente by AirName
	 * 
	 * @param name of the AIR
	 * @date 2009-02-09
	 * @author Ekler Paulino de Mattos
	 * @exception
	 * @return Return a list of runnable Jobs
	 * @roseuid 49184F590220
	 */
	public ClienteVO buscarPorNome(String clienteNome) throws PersistenciaException{

		List<ClienteVO> clienteList = em.createNamedQuery("ClienteVO.buscarClientePorNome")
				.setParameter("nome", clienteNome).getResultList();

		if (clienteList.isEmpty())
			return null;
		else
			return clienteList.get(0);
	}

	
	/* (non-Javadoc)
	 * @see persistencia.daos.ClienteDAO#close()
	 */
	public void close() throws PersistenciaException{
		em.close();
	}
}
