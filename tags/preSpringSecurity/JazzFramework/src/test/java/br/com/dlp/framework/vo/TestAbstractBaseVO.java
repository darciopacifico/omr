package br.com.dlp.framework.vo;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.com.dlp.framework.exception.BaseError;

@ContextConfiguration(locations = "/ApplicationContext_JazzFramework.xml")

@Test
public class TestAbstractBaseVO extends AbstractTestNGSpringContextTests{
	
	public final class ConcreteFakeVO extends AbstractBaseVO<Long> {
		public String getNome() {
			return nome;
		}
		
		public void setNome(String nome) {
			this.nome = nome;
		}
		
		private String nome;
		
		public ConcreteFakeVO(Long l) {
			super(l);
		}
		
		public ConcreteFakeVO() {
			// TODO Auto-generated constructor stub
		}
		
		private static final long serialVersionUID = 3359615994950609068L;
	}
	
	public TestAbstractBaseVO() {
		// TODO Auto-generated constructor stub
	}
	
	ConcreteFakeVO vo1;
	ConcreteFakeVO vo2;
	ConcreteFakeVO voNull;
	
	Object someObjVO = new Object();
	
	@BeforeTest
	public void setUp() {
		
		vo1 = new ConcreteFakeVO();
		
		vo2 = new ConcreteFakeVO();
		
	}
	
	@Test
	public void testSetPK() {
		try {
			vo1.setPK(null);
			Assert.fail("O VO NAO deve permitir valores null como chave!");
		} catch (Exception e) {
			Assert.assertTrue((e instanceof BaseError), "Era esperado BaseError!");
		}
	}
	
	@Test
	public void testEquals() {
		try {
			org.testng.Assert.assertFalse(vo1.equals(voNull), "Testando null. Nao pode dar erro!");
			org.testng.Assert.assertFalse(vo1.equals(someObjVO), "Testando um obj de tipo diferente. Nao pode dar erro na avaliação!");
			
			vo1.setPK(1l);
			vo2.setPK(1l);
			org.testng.Assert.assertTrue(vo1.equals(vo2));
			
			vo1.setPK(1l);
			vo2.setPK(2l);
			org.testng.Assert.assertFalse(vo1.equals(vo2));
			
			vo1 = new ConcreteFakeVO();
			vo2 = new ConcreteFakeVO();
			org.testng.Assert.assertFalse(vo1.equals(vo2), "Testando VOs sem PK. Nao pode dar erro");
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("O VO deve permitir valores null como chave! " + e.getMessage());
		}
	}
	
	@Test
	public void testHashCode() {
		
		AbstractBaseVO<Long> voAbs = new ConcreteFakeVO(122l);
		
		Assert.assertEquals(voAbs.hashCode(), 0, "Para um VO sem chave o hashcode esperado é zero!");
		
		voAbs.setPK(2134234l);
		Assert.assertFalse(voAbs.hashCode() == 0, "Para um vo com chave o hash code nao pode ser zero! ");
		
	}
	
	@Test
	public void testConstruction() {
		
		try {
			ConcreteFakeVO cvo = new ConcreteFakeVO(123l);
			Assert.assertNotNull(cvo.getPK());
			Assert.assertNotSame(cvo.getPK().hashCode(), 0);
		} catch (Exception e) {
			Assert.fail("Deveria ter construído sem problemas");
		}
	}
	
	@Test
	public void testToString() {
		
		vo1.setPK(276215438l);
		vo1.setNome("Martin Fowler");
		String str = vo1.toString();
		
		Assert.assertTrue(str.indexOf("276215438") != -1);
		
		Assert.assertTrue(str.indexOf("Martin Fowler") != -1);
		
	}
	
}
