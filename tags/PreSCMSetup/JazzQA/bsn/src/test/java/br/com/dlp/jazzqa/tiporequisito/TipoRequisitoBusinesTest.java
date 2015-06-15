package br.com.dlp.jazzqa.tiporequisito;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;

import br.com.dlp.framework.exception.BaseBusinessException;

//@ContextConfiguration(locations = "/ApplicationContext.xml")extends AbstractTestNGSpringContextTests
public class TipoRequisitoBusinesTest {
	
	@Autowired
	TipoRequisitoBusiness requisitoBusiness;
	
	TipoRequisitoVO requisitoVO = new TipoRequisitoVO();
	
	private TipoRequisitoVO requisitoVO_Fail;
	
	@BeforeTest
	public void setUp() {
		requisitoVO.setDescricao("descrica");
		requisitoVO.setNome("nome");
	}
	
	/**
	 * Testa se registro foi inserido com sucesso
	 */
	public void aatestInsertSucces() {
		try {
			
			requisitoBusiness.saveOrUpdate(requisitoVO);
			Assert.assertNotNull(requisitoVO.getPK(), "O registro nao foi salvo ou a pk  nao foi atribuida!");
			
		} catch (BaseBusinessException e) {
			Assert.fail("Erro ao tentar salvar:" + e.getMessage());
		}
	}
	
	/**
	 * Testa regras de domínio e de negócio
	 */
	public void aatestInsertFail() {
		try {
			
			requisitoBusiness.saveOrUpdate(requisitoVO_Fail);
			Assert.fail("O objeto requisitoVO_Fail deveria falhar " + "na inserção, pois a descrição de requisitoVO_Fail não foi preenchida!");
			
		} catch (Exception e) {
			Assert.assertTrue(e instanceof BaseBusinessException, "Era esperada uma exceção filha de BaseBusinessException!");
		}
	}
	
	/**
	 * Testa se registro foi inserido com sucesso
	 */
	public void testFindByPK() {
		try {
			Long pk = requisitoVO.getPK();
			
			requisitoVO = requisitoBusiness.findByPK(pk);
			
			Assert.assertNotNull(requisitoVO.getDescricao());
			Assert.assertNotNull(requisitoVO.getNome());
			
		} catch (BaseBusinessException e) {
			
			Assert.fail("Erro ao tentar salvar:" + e.getMessage());
			
		}
		
	}
	
	
}
