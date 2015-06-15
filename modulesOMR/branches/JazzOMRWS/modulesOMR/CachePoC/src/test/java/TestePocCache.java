import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import br.com.poc.PessoaBusiness;
import br.com.poc.PessoaVO;

@ContextConfiguration(locations = "/ApplicationContext.xml")
@Test
public class TestePocCache  extends AbstractTestNGSpringContextTests{
	
	@Autowired
	private PessoaBusiness pessoaBusiness;
	
	@Test
	public void testCache(){
		
		PessoaVO pessoaVO;
		
		pessoaVO = pessoaBusiness.findByPk(3l);
		System.out.println("pessoaVO2.nome="+pessoaVO.getNome() );

		pessoaVO = pessoaBusiness.findByPk(3l);
		System.out.println("pessoaVO2.nome="+pessoaVO.getNome() );

		pessoaVO = pessoaBusiness.findByPk(3l);
		System.out.println("pessoaVO2.nome="+pessoaVO.getNome() );

		pessoaVO = pessoaBusiness.findByPk(3l);
		System.out.println("pessoaVO2.nome="+pessoaVO.getNome() );

		pessoaVO = pessoaBusiness.findByPk(3l);
		System.out.println("pessoaVO2.nome="+pessoaVO.getNome() );

		pessoaVO = pessoaBusiness.findByPk(3l);
		System.out.println("pessoaVO2.nome="+pessoaVO.getNome() );

	
		pessoaVO = pessoaBusiness.findByPk(3l);
		System.out.println("pessoaVO1.nome="+pessoaVO.getNome() );

	}

}
