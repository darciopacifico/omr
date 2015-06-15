package br.com.dlp.jazz.tiporequisito;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.dlp.jazzqa.produto.ProdutoVO;
import br.com.dlp.jazzqa.tiporequisito.TipoRequisitoVO;
import br.com.dlp.jazzqa.usuariojazz.UsuarioJazzVO;

/**
 * @author dpacifico
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/ApplicationContext.xml")
public class TipoRequisitoEntityTeste extends TestCase{

	@Autowired
	protected SessionFactory sessionFactory;
	
	@Test
	public void testInjection() {
		
		
		Session session = sessionFactory.openSession();
					
		System.out.println(session.createCriteria(TipoRequisitoVO.class).list());
		System.out.println(session.createCriteria(ProdutoVO.class).list());
		System.out.println(session.createCriteria(UsuarioJazzVO.class).list());
	}
	


	
}
