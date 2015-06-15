package com.msaf.validador.consultaonline;

import java.util.List;

import org.apache.log4j.Logger;

import junit.framework.TestCase;

import com.msaf.validador.consultaonline.cliente.ClienteVO;
import com.msaf.validador.consultaonline.dao.ClienteDAO;
import com.msaf.validador.consultaonline.dao.impl.ClienteDAOImpl;
import com.msaf.validador.consultaonline.exceptions.PersistenciaException;


public class ClienteDAOTest extends TestCase {
	
	private Logger log = Logger.getLogger(ClienteDAOTest.class);

	public void testClienteDAO() {
		fail("Not yet implemented");
	}

	private ClienteVO getClienteVO(){
		
		ClienteVO cliente = new ClienteVO();
		cliente.setCnpj("10000");
		cliente.setNome("joao da silva");
		
		return cliente;
	}
	
	public void testCriarClienteVO() {
		
		ClienteDAO clienteDAO = new ClienteDAOImpl(); 
		
		try {
			clienteDAO.criarCliente(this.getClienteVO());
		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//assertEquals(true);
	}

	
	
	public void testBuscarPorId() {
		ClienteDAO clienteDAO = new ClienteDAOImpl();
		ClienteVO cliente;
		try {
			cliente = clienteDAO.buscarPorId(new Long(47));
			if(log.isDebugEnabled()) log.debug("cliente:"+cliente.getNome());
		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		fail("Not yet implemented");
	}

	public void testAtualizarCliente() {
		ClienteDAO clienteDAO = new ClienteDAOImpl();
		
		ClienteVO cliente;
		try {
			cliente = clienteDAO.buscarPorId(new Long(1));
			cliente.setNome("Joao da Silva Sauro");
			if(log.isDebugEnabled()) log.debug("cliente:"+cliente.getNome());
			clienteDAO.atualizarCliente(cliente);

		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fail("Not yet implemented");
	}

	public void testRemoverCliente() {
		ClienteDAO clienteDAO = new ClienteDAOImpl();
		ClienteVO cliente;
		try {
			cliente = clienteDAO.buscarPorId(new Long(1));
			clienteDAO.removerCliente(cliente);
		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		fail("Not yet implemented");
	}

	public void testBuscarTodos() {
		ClienteDAO clienteDAO = new ClienteDAOImpl();
		List<ClienteVO> todos;
		try {
			todos = clienteDAO.buscarTodos();
			if(log.isDebugEnabled()) log.debug("todos:"+todos.size());
		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		
		fail("Not yet implemented");
	}

	
	public void testBuscarTodosIDs() {
		fail("Not yet implemented");
	}

	
	public void testBuscarPorNome() {
		fail("Not yet implemented");
	}

}
