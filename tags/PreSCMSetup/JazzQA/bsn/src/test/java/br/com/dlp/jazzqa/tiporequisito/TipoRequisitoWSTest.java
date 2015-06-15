package br.com.dlp.jazzqa.tiporequisito;

import java.util.Date;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import br.com.dlp.framework.exception.BaseBusinessException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/WSClients.xml")
public class TipoRequisitoWSTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	@Qualifier("tipoRequisitows")
	TipoRequisitoBusiness requisitoWs;
	
	@Test
	public void testFindTipos() throws BaseBusinessException {
		
		List<TipoRequisitoVO> tipos = null;
		try {
			tipos = requisitoWs.findAll();
			long now = System.currentTimeMillis();
			tipos = requisitoWs.findAll();
			tipos = requisitoWs.findAll();
			tipos = requisitoWs.findAll();
			tipos = requisitoWs.findAll();
			tipos = requisitoWs.findAll();
			tipos = requisitoWs.findAll();
			tipos = requisitoWs.findAll();
			tipos = requisitoWs.findAll();
			tipos = requisitoWs.findAll();
			tipos = requisitoWs.findAll();
			tipos = requisitoWs.findAll();
			tipos = requisitoWs.findAll();
			tipos = requisitoWs.findAll();
			System.out.println("TEMPO TOTAL - INICIO:" + (System.currentTimeMillis() - now));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TipoRequisitoVO requisitoVO = tipos.get(2);
		requisitoVO.setDescricao("alterado via ws em https");
		requisitoVO.setDtInclusao(new Date());
		
		requisitoWs.saveOrUpdate(requisitoVO);
		
	}
}
