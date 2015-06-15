package br.com.dlp.jazzomr.poc;

import java.io.InputStream;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import br.com.dlp.jazzomr.event.EventProcessDTO;
import br.com.dlp.jazzomr.event.ParticipationHelperWS;
import br.com.dlp.jazzomr.exam.ExamVO;
import br.com.dlp.jazzomr.exceptions.JazzOMRRuntimeException;
import br.com.dlp.jazzomr.jasper.JazzOMRJRDataSourceProvider;
import br.com.dlp.jazzomr.person.PessoaVO;

@Test
public class JazzFetchAndQueryTests2  {
	public static Logger log = LoggerFactory.getLogger(JazzFetchAndQueryTests2.class);


	@Test
	public void testEventToJazzJRDataSource() {
		
		ExamVO exame = JazzOMRJRDataSourceProvider.getExame();
		List<PessoaVO> pessoas = JazzOMRJRDataSourceProvider.getPessoas();
		

		ParticipationHelperWS helperWS = new ParticipationHelperWS();

		
		EventProcessDTO eventDTO = helperWS.processParticipations("evento teste",exame, pessoas, 3, getJasperReport());

		
		System.out.println(eventDTO.getEventVO());
		
		
				
	}
	
	/**
	 * @return
	 * 
	 */
	private static JasperReport getJasperReport() {
		InputStream stream = JazzOMRJRDataSourceProvider.class.getResourceAsStream("/reports/ExamReportDSP2.jrxml");

		JasperReport jr;
		try {
			jr = JasperCompileManager.compileReport(stream);
		} catch (JRException e) {
			throw new JazzOMRRuntimeException("Erro ao tentar compilar relatorio!", e);
		}

		return jr;
	}


}
