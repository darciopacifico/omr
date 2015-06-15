package br.com.dlp.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import br.com.dlp.jazzqa.produto.ProdutoBusiness;

@ContextConfiguration(locations={"/WSGeneratedClients.xml"})
public class TestProduto extends AbstractTestNGSpringContextTests{
	
	@Autowired
	ProdutoBusiness produtoBusiness;
	
	/**
	 * @param args
	 */
	@Test
	public void testGeneratedClient() {
		
		/*
		
		ArrayOfProdutoVO arrayOfProdutoVO = produtoBusiness.findProdutoVO(null, null, null, null, null);
		List<ProdutoVO> produtoVOs = arrayOfProdutoVO.getProdutoVO();
		for (ProdutoVO produtoVO : produtoVOs) {
			System.out.println(produtoVO.getNome().getValue());
		}
		*/
	}
	
}
