import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import br.com.dlp.jazzav.endereco.CidadeVO;
import br.com.dlp.jazzav.endereco.EstadoVO;
import br.com.dlp.jazzav.endereco.LogradouroVO;
import br.com.dlp.jazzav.opcionais.OpcionalBusiness;
import br.com.dlp.jazzav.person.EnderecoVO;



@ContextConfiguration(locations = "/ApplicationContext_JazzFramework.xml")
public class TestConsultaLogradouro  extends AbstractTestNGSpringContextTests{
	
	@Resource
	public SessionFactory sessionFactory;	

	@Resource
	public HibernateTemplate hibernateTemplate;
	
	@Resource
	OpcionalBusiness opcionalBusiness; 

	
	public TestConsultaLogradouro() {
	}

	

	@Test
	public void testOnlyNumbersReplace(){
		
		System.out.println("03807-300".replaceAll("\\D", ""));
		
	}
	
	
	@Test
	public void testConsulta(){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(LogradouroVO.class);
		
		//criteria.add(Restrictions.eq("cep", "03807300"));
		
		List<LogradouroVO> logradouros = hibernateTemplate.find("" +
				" select l from LogradouroVO l " +
				"		left join fetch l.bairroVO b	" +
				"		left join fetch b.cidadeVO c	" +
				"		left join fetch c.estadoVO e	" +
				"where l.cep='03807300' ");
		
		
		//List<LogradouroVO> logradouros = hibernateTemplate.findByCriteria(criteria,10,1000);
		
		
		for (LogradouroVO logradouroVO : logradouros) {
			
			System.out.println(" |logradouro:"+logradouroVO.getCep());

			/*
			System.out.println(
					" |cep:"+logradouroVO.getCep() +" "+
				  " |rua:"+logradouroVO.getLogradouro()+" "+
				  " |complemento:"+logradouroVO.getComplemento()+" "+
					" |bairro:"+logradouroVO.getBairroVO().getDescricao()+" "+
					" |cidade:"+logradouroVO.getBairroVO().getCidadeVO().getDescricao()+" "+
					" |estado:"+logradouroVO.getBairroVO().getCidadeVO().getEstadoVO().getSigla()+" "
					);
					
			*/
		}
		
		
	}
	
	
	@Test
	public void testConsultaCidade(){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(CidadeVO.class);
		
		criteria.add(Restrictions.eq("descricao", "Campinas"));
		EstadoVO estado = new EstadoVO();
		estado.setPK(26);
		criteria.add(Restrictions.eq("estadoVO", estado));
		
		List<CidadeVO> cidades = hibernateTemplate.findByCriteria(criteria);
		
		
		for (CidadeVO cidade : cidades) {
			
			System.out.println(cidade.getDescricao()+" ");
			
		}
		
		
	}
	
	
}
